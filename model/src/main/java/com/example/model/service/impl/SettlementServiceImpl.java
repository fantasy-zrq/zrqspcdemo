package com.example.model.service.impl;

import cn.hutool.core.lang.Singleton;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.model.common.exception.ClientException;
import com.example.model.dto.req.CouponCreatePaymentGoodsReqDTO;
import com.example.model.dto.req.CouponCreatePaymentReqDTO;
import com.example.model.dto.req.CouponProcessPaymentReqDTO;
import com.example.model.dto.req.CouponProcessRefundReqDTO;
import com.example.model.entity.CouponDO;
import com.example.model.entity.ReceiveDO;
import com.example.model.entity.SettlementDO;
import com.example.model.entity.mapper.CouponMapper;
import com.example.model.entity.mapper.ReceiveMapper;
import com.example.model.entity.mapper.SettlementMapper;
import com.example.model.service.CouponService;
import com.example.model.service.SettlementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static com.example.model.common.constance.RedisConstanceKey.*;

/**
 * @author zrq
 * @time 2025/11/23 15:42
 * @description
 */
@Slf4j(topic = "SettlementServiceImpl")
@Service
@RequiredArgsConstructor
public class SettlementServiceImpl extends ServiceImpl<SettlementMapper, SettlementDO> implements SettlementService {

    private final RedissonClient redissonClient;
    private final StringRedisTemplate stringRedisTemplate;
    private final ReceiveMapper receiveMapper;
    private final CouponService couponService;
    private final CouponMapper couponMapper;
    private final TransactionTemplate transactionTemplate;
    private final SettlementMapper settlementMapper;
    private static final String LUA_PATH = "lua/settlement_create_update_redis_script.lua";
    private static final String LUA_PATH_REFUND = "lua/settlement_refund_update_redis_script.lua";

    /**
     * 只用于锁定优惠券
     */
    @Override
    public void createPaymentRecord(CouponCreatePaymentReqDTO requestParam) {
        String lockKey = String.format(LOCK_COUPON_SETTLEMENT_KEY, requestParam.getUserId(), requestParam.getCouponId());
        RLock lock = redissonClient.getLock(lockKey);
        if (!lock.tryLock()) {
            throw new ClientException("正在创建优惠券核算记录..");
        }
        try {
            SettlementDO settlementDO = settlementMapper.selectOne(Wrappers.lambdaQuery(SettlementDO.class)
                    .eq(SettlementDO::getCouponId, requestParam.getCouponId())
                    .eq(SettlementDO::getUserId, requestParam.getUserId())
                    .eq(SettlementDO::getOrderId, requestParam.getOrderId())
                    .in(SettlementDO::getStatus, 0, 2));
            if (Objects.nonNull(settlementDO)) {
                throw new ClientException("优惠券已经使用");
            }
            //代表该优惠券还没有被核验
            //t_receive扣减receive_number--redis扣减领取数量并判断核验该张券以后结果为0则删除该集合
            ReceiveDO receiveDO = receiveMapper.selectOne(Wrappers.lambdaQuery(ReceiveDO.class)
                    .eq(ReceiveDO::getUserId, requestParam.getUserId())
                    .eq(ReceiveDO::getCouponId, requestParam.getCouponId()));
            if (Objects.isNull(receiveDO) || receiveDO.getStatus() != 0 || receiveDO.getEndTime().before(new Date())) {
                throw new ClientException("优惠券状态异常...无法核验");
            }
            //核验优惠券金额和消费规则是否正确
            CouponDO couponDO = couponMapper.selectOne(Wrappers.lambdaQuery(CouponDO.class)
                    .eq(CouponDO::getCouponId, requestParam.getCouponId())
                    .eq(CouponDO::getShopNumber, requestParam.getShopNumber())
                    .eq(CouponDO::getDelFlag, 0));
            if (Objects.isNull(couponDO)) {
                throw new ClientException("优惠券异常..");
            }
            JSONObject consumerRule = JSON.parseObject(couponDO.getConsumeRule());
            BigDecimal discountAmount;

            if (couponDO.getTarget().equals(0)) {
                //某个商品专属优惠券，隶属于某个店铺
                CouponCreatePaymentGoodsReqDTO goods = requestParam.getGoodsList()
                        .stream()
                        .filter(each -> Objects.equals(each.getGoodsNumber(), couponDO.getGoods()))
                        .findFirst()
                        .orElse(null);
                if (Objects.isNull(goods)) {
                    throw new ClientException("该优惠券只能用于固定商品，无法被核验..");
                }
                BigDecimal maximumDiscountAmount = consumerRule.getBigDecimal("maximumDiscountAmount");
                /**
                 * {"termsOfUse": 10, "validityPeriod": "48", "maximumDiscountAmount": 3, "explanationOfUnmetConditions": "3"}
                 */
                switch (couponDO.getType()) {
                    case 0 -> {
                        //0：立减券
                        discountAmount = maximumDiscountAmount;
                        break;
                    }
                    case 1 -> {
                        //1：满减券
                        discountAmount = maximumDiscountAmount;
                        break;
                    }
                    case 2 -> {
                        //2：折扣券
                        BigDecimal discountRate = consumerRule.getBigDecimal("discountRate");
                        discountAmount = goods.getGoodsAmount().multiply(discountRate);
                        if (discountAmount.compareTo(maximumDiscountAmount) >= 0) {
                            //这里maximumDiscountAmount限定了最多就只能扣减maximumDiscountAmount大小的金额
                            //即使这里是百分比折扣券也不能超过这个限制，类似于九折券，最多抵扣50元，
                            //即使goods.getGoodsAmount().multiply(discountRate)结果是60
                            //也只能扣减50
                            discountAmount = maximumDiscountAmount;
                        }
                        break;
                    }
                    default -> throw new ClientException("无效的优惠券类型");
                }
            } else {
                //店铺通用优惠券
                BigDecimal maximumDiscountAmount = consumerRule.getBigDecimal("maximumDiscountAmount");

                switch (couponDO.getType()) {
                    case 0 -> {
                        //0：立减券
                        discountAmount = maximumDiscountAmount;
                        break;
                    }
                    case 1 -> {
                        //1：满减券
                        discountAmount = maximumDiscountAmount;
                        break;
                    }
                    case 2 -> {
                        //2：折扣券
                        BigDecimal discountRate = consumerRule.getBigDecimal("discountRate");
                        discountAmount = requestParam.getOrderAmount().multiply(discountRate);
                        if (discountAmount.compareTo(maximumDiscountAmount) >= 0) {
                            //这里maximumDiscountAmount限定了最多就只能扣减maximumDiscountAmount大小的金额
                            //即使这里是百分比折扣券也不能超过这个限制，类似于九折券，最多抵扣50元，
                            //即使goods.getGoodsAmount().multiply(discountRate)结果是60
                            //也只能扣减50
                            discountAmount = maximumDiscountAmount;
                        }
                        break;
                    }
                    default -> throw new ClientException("无效的优惠券类型");
                }
            }
            if (requestParam.getOrderAmount().subtract(discountAmount).compareTo(requestParam.getPayableAmount()) != 0) {
                throw new ClientException("优惠券计算方式出错");
            }
            transactionTemplate.executeWithoutResult(status -> {
                try {
                    SettlementDO build = SettlementDO.builder()
                            .orderId(requestParam.getOrderId())
                            .userId(requestParam.getUserId())
                            .couponId(requestParam.getCouponId())
                            .status(0)
                            .build();
                    settlementMapper.insert(build);
                    //这里不做扣减，这里只做锁库存
                    //receiveMapper.decrementReceiveNumber(requestParam.getUserId(), requestParam.getCouponId());
                    receiveMapper.update(null, Wrappers.lambdaUpdate(ReceiveDO.class)
                            .eq(ReceiveDO::getUserId, requestParam.getUserId())
                            .eq(ReceiveDO::getCouponId, requestParam.getCouponId())
                            .set(ReceiveDO::getStatus, 3));
                } catch (Exception e) {
                    log.error("创建优惠券结算单失败", e);
                    status.setRollbackOnly();
                    throw new RuntimeException(e);
                }
            });
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void processPayment(CouponProcessPaymentReqDTO requestParam) {
        String lockKey = String.format(LOCK_COUPON_SETTLEMENT_KEY, requestParam.getUserId(), requestParam.getCouponId());
        RLock lock = redissonClient.getLock(lockKey);
        if (!lock.tryLock()) {
            throw new ClientException("正在解除优惠券锁定");
        }
        try {
            transactionTemplate.executeWithoutResult(status -> {
                //设置status为0或者1
                try {
                    receiveMapper.decrementReceiveNumber(requestParam.getUserId(), requestParam.getCouponId());
                    String limitKeyTemplate = REDIS_COUPON_DISTRIBUTION_LIMIT_KEY + "%s" + "_" + "%s";
                    String limitKey = String.format(limitKeyTemplate, requestParam.getUserId(), requestParam.getCouponId());
                    String receiveKey = String.format(REDIS_COUPON_DISTRIBUTION_RECEIVED_KEY, requestParam.getUserId());
                    DefaultRedisScript<Long> luaScript = Singleton.get(LUA_PATH, () -> {
                        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();
                        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource(LUA_PATH)));
                        redisScript.setResultType(Long.class);
                        return redisScript;
                    });
                    Long res = stringRedisTemplate.execute(luaScript, List.of(limitKey, receiveKey), String.valueOf(requestParam.getUserId()), String.valueOf(requestParam.getCouponId()));
                    log.info("res====>{}", res);
                    //将锁定的settlement改为2
                    settlementMapper.update(null, Wrappers.lambdaUpdate(SettlementDO.class)
                            .eq(SettlementDO::getCouponId, requestParam.getCouponId())
                            .eq(SettlementDO::getUserId, requestParam.getUserId())
                            .eq(SettlementDO::getOrderId, requestParam.getOrderId())
                            .set(SettlementDO::getStatus, 2));
                } catch (Exception e) {
                    log.error("优惠券解锁锁定失败。。");
                    status.setRollbackOnly();
                    throw new RuntimeException(e);
                }
            });
        } finally {
            lock.unlock();
        }
    }

    /**
     * 仅仅会在优惠券已经核销，并且用户退款时被调用
     */
    @Override
    public void processRefund(CouponProcessRefundReqDTO requestParam) {
        //t_settlement设置status=3，t_receive的receive_number+1，将redis两个队列给恢复
        //这里有个问题，无法得知用户使用了哪些优惠券使用了几张
        String lockKey = String.format(LOCK_COUPON_SETTLEMENT_KEY, requestParam.getUserId(), requestParam.getCouponId());
        RLock lock = redissonClient.getLock(lockKey);
        if (!lock.tryLock()) {
            throw new ClientException("正在处理退款流程");
        }
        try {
            transactionTemplate.executeWithoutResult(status -> {
                try {
                    settlementMapper.update(null, Wrappers.lambdaUpdate(SettlementDO.class)
                            .eq(SettlementDO::getCouponId, requestParam.getCouponId())
                            .eq(SettlementDO::getUserId, requestParam.getUserId())
                            .eq(SettlementDO::getOrderId, requestParam.getOrderId())
                            .set(SettlementDO::getStatus, 3));
                    receiveMapper.updateReceiveNumberAndStatus(requestParam.getUserId(), requestParam.getCouponId());

                    String limitKeyTemplate = REDIS_COUPON_DISTRIBUTION_LIMIT_KEY + "%s" + "_" + "%s";
                    String limitKey = String.format(limitKeyTemplate, requestParam.getUserId(), requestParam.getCouponId());
                    String receiveKey = String.format(REDIS_COUPON_DISTRIBUTION_RECEIVED_KEY, requestParam.getUserId());
                    DefaultRedisScript<Long> luaScript = Singleton.get(LUA_PATH_REFUND, () -> {
                        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();
                        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource(LUA_PATH_REFUND)));
                        redisScript.setResultType(Long.class);
                        return redisScript;
                    });
                    stringRedisTemplate.execute(luaScript,
                            List.of(limitKey, receiveKey),
                            String.valueOf(requestParam.getUserId()),
                            String.valueOf(requestParam.getCouponId()),
                            String.valueOf(new Date().getTime()));
                } catch (Exception e) {
                    log.error("退款流程出错");
                    status.setRollbackOnly();
                    throw new RuntimeException(e);
                }
            });
        } finally {
            lock.unlock();
        }
    }
}
