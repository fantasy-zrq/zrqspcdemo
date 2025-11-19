package com.example.model.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Singleton;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.model.common.constance.RedisConstanceKey;
import com.example.model.common.enums.CouponEnableStatusEnum;
import com.example.model.common.exception.ClientException;
import com.example.model.dto.req.CouponCreateReqDTO;
import com.example.model.dto.req.CouponTemplateQueryReqDTO;
import com.example.model.dto.req.CouponTemplateRedeemReqDTO;
import com.example.model.dto.resp.CouponTemplateQueryRespDTO;
import com.example.model.entity.CouponDO;
import com.example.model.entity.mapper.CouponMapper;
import com.example.model.entity.mapper.ReceiveMapper;
import com.example.model.service.CouponService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.example.model.common.constance.RedisConstanceKey.*;

@Service
@Slf4j(topic = "CouponServiceImpl")
@RequiredArgsConstructor
public class CouponServiceImpl extends ServiceImpl<CouponMapper, CouponDO> implements CouponService {

    private final CouponMapper couponMapper;
    private final StringRedisTemplate stringRedisTemplate;
    private final RedissonClient redissonClient;
    private final RBloomFilter<String> couponIdBloomFilter;
    private final ReceiveMapper receiveMapper;
    private static final String LUA_PATH = "lua/coupon_user_redeem_script.lua";

    @Override
    public void create(CouponCreateReqDTO requestParam) {
        //不做任何校验
        CouponDO ins = BeanUtil.toBean(requestParam, CouponDO.class);
        ins.setCouponStatus(CouponEnableStatusEnum.ACTIVE.status);
        ins.setDelFlag(0);
        couponMapper.insert(ins);
        Map<String, Object> map = BeanUtil.beanToMap(ins, new HashMap<>(), false, true);
        List<String> redisList = new ArrayList<>();
        Map<String, String> actualCacheTargetMap = map.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue() != null ? entry.getValue().toString() : ""
                ));

        actualCacheTargetMap.forEach((key, value) -> {
            redisList.add(key);
            redisList.add(value);
        });

        redisList.add(String.valueOf(requestParam.getEndTime().getTime() / 1000));

        DefaultRedisScript<Long> script = new DefaultRedisScript<>();
        script.setLocation(new ClassPathResource("lua/create_coupon_script.lua"));
        script.setResultType(Long.class);
        stringRedisTemplate.execute(
                script, List.of(String.format(REDIS_COUPON_CREATE_KEY, ins.getCouponId())), redisList.toArray()
        );
        couponIdBloomFilter.add(ins.getCouponId().toString());
        //延迟任务取消优惠券
    }

    @Override
    public CouponTemplateQueryRespDTO findCouponTemplate(CouponTemplateQueryReqDTO requestParam) {
        Long couponId = requestParam.getCouponId();
        boolean contains = couponIdBloomFilter.contains(couponId.toString());

        //bloomFilter不存在一定不存在
        if (!contains) {
            log.warn("该优惠券Id不存在于BloomFilter--[{}]", couponId);
            throw new ClientException("该优惠券Id不存在于BloomFilter--" + couponId);
        }
        //缓存0值防止缓存击穿
        String redisCacheZeroKey = String.format(REDIS_COUPON_FIND_CACHE_ZERO_KEY, couponId);
        Boolean hasKey = stringRedisTemplate.hasKey(redisCacheZeroKey);
        if (Boolean.TRUE.equals(hasKey)) {
            log.warn("恶意攻击数据库coupon_id--->[{}]", couponId);
            throw new ClientException("恶意攻击数据库coupon_id--->" + couponId);
        }
        String lockKey = String.format(REDIS_COUPON_FIND_LOCK_KEY, couponId);
        RLock lock = redissonClient.getLock(lockKey);
        CouponDO couponDO;

        try {
            lock.lock();
            hasKey = stringRedisTemplate.hasKey(redisCacheZeroKey);
            if (Boolean.TRUE.equals(hasKey)) {
                log.warn("恶意攻击数据库coupon_id--->[{}]", couponId);
                stringRedisTemplate.opsForValue().set(redisCacheZeroKey, couponId.toString(), 2, TimeUnit.MINUTES);
                throw new ClientException("恶意攻击数据库coupon_id--->" + couponId);
            }
            String couponRedisKey = String.format(REDIS_COUPON_CREATE_KEY, couponId);
            //bloomFilter说存在
            Map<Object, Object> couponMap = stringRedisTemplate.opsForHash().entries(couponRedisKey);
            //redis中存在
            if (CollectionUtil.isNotEmpty(couponMap)) {
                return BeanUtil.toBean(couponMap, CouponTemplateQueryRespDTO.class);
            }
            //查数据库
            couponDO = couponMapper.selectById(Wrappers.lambdaQuery(CouponDO.class)
                    .eq(CouponDO::getCouponId, requestParam.getCouponId())
                    .eq(CouponDO::getDelFlag, 0)
                    .eq(CouponDO::getCouponStatus, 0));
            if (Objects.isNull(couponDO)) {
                log.warn("该优惠券Id不存在于数据库--[{}]", couponId);
                throw new ClientException("该优惠券Id不存在于数据库--" + couponId);
            }
            //重建redis缓存，设置过期时间
            String luaScript = """
                    redis.call('HMSET', KEYS[1], unpack(ARGV, 1, #ARGV - 1))
                    redis.call('EXPIRE', KEYS[1], ARGV[#ARGV])
                    """;
            Map<String, Object> map = BeanUtil.beanToMap(couponDO, false, true);
            Map<String, String> stringMap = map.entrySet().stream().collect(Collectors.toMap(
                    Object::toString, Object::toString
            ));
            List<String> args = new ArrayList<>(stringMap.size() * 2 + 1);
            stringMap.forEach((key, value) -> {
                args.add(key);
                args.add(value);
            });
            args.add(String.valueOf(DateUtil.between(new Date(), couponDO.getEndTime(), DateUnit.SECOND)));

            stringRedisTemplate.execute(
                    new DefaultRedisScript<>(luaScript, Long.class),
                    List.of(couponRedisKey),
                    args.toArray()
            );
        } finally {
            lock.unlock();
        }
        return BeanUtil.toBean(couponDO, CouponTemplateQueryRespDTO.class);
    }

    /**
     * 这个兑换优惠券的场景，当平台给用户发的优惠券，用户点击领取，用户可能以及领取过了，但是这个优惠券可能可以多次领取
     */
    @Override
    public void redeemUserCoupon(CouponTemplateRedeemReqDTO requestParam) {
        Long couponId = requestParam.getCouponId();
        CouponTemplateQueryRespDTO couponTemplate = findCouponTemplate(BeanUtil.toBean(requestParam, CouponTemplateQueryReqDTO.class));
        if (Objects.isNull(couponTemplate)) {
            log.error("优惠券兑换错误---无该优惠券--couponId==>[{}]", couponId);
            throw new ClientException("优惠券兑换错误---无该优惠券--couponId==" + couponId);
        }

        Integer receiveNumber = couponTemplate.getReceiveNumber();
        //扣减redis库存
        DefaultRedisScript<Void> luaScript = Singleton.get(LUA_PATH, () -> {
            DefaultRedisScript<Void> redisScript = new DefaultRedisScript<>();
            redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource(LUA_PATH)));
            redisScript.setResultType(Void.class);
            return redisScript;
        });
        String REDIS_COUPON_DISTRIBUTION_LIMIT_KEY = RedisConstanceKey.REDIS_COUPON_DISTRIBUTION_LIMIT_KEY + "%s_%s";
        //user_id怎么拿
//        String.format(REDIS_COUPON_DISTRIBUTION_LIMIT_KEY,)

    }
}
