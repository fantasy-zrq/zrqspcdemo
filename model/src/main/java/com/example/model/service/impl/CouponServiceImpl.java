package com.example.model.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.model.common.enums.CouponEnableStatusEnum;
import com.example.model.dto.req.CouponCreateReqDTO;
import com.example.model.entity.CouponDO;
import com.example.model.entity.mapper.CouponMapper;
import com.example.model.service.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.example.model.common.constance.RedisConstanceKey.REDIS_COUPON_CREATE_KEY;

@Service
@RequiredArgsConstructor
public class CouponServiceImpl extends ServiceImpl<CouponMapper, CouponDO> implements CouponService {

    private final CouponMapper couponMapper;
    private final StringRedisTemplate stringRedisTemplate;


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
        //延迟任务取消优惠券
    }
}
