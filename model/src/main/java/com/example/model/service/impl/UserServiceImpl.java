package com.example.model.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.model.common.exception.ClientException;
import com.example.model.common.handlerchain.ChainFilterContext;
import com.example.model.common.utils.JWTUtil;
import com.example.model.common.utils.MD5EncryptUtil;
import com.example.model.dto.req.ExcelReqDTO;
import com.example.model.dto.req.UserLoginReqDTO;
import com.example.model.dto.req.UserRegisterReqDTO;
import com.example.model.entity.OrderDO;
import com.example.model.entity.UserDO;
import com.example.model.entity.mapper.UserMapper;
import com.example.model.executor.ExcelResolverThreadPool;
import com.example.model.mq.producer.RocketMqExcelProducer;
import com.example.model.service.UserService;
import com.mzt.logapi.context.LogRecordContext;
import com.mzt.logapi.starter.annotation.LogRecord;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.redisson.api.RBloomFilter;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static com.example.model.common.constance.RedisConstanceKey.*;
import static com.example.model.common.handlerchain.ChainBizMarkEnum.USER_LOGIN_MARK;

/**
 * @author zrq
 * @time 2025/8/6 21:12
 * @description
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, UserDO> implements UserService {

    private final StringRedisTemplate stringRedisTemplate;
    private final RBloomFilter<String> userNameBloomFilter;
    private final ChainFilterContext chainFilterContext;
    private final ExcelResolverThreadPool excelResolverThreadPool;
    private final RocketMQTemplate rocketMQTemplate;
    private final RocketMqExcelProducer rocketMqExcelProducer;

    @Override
    public void doRegister(UserRegisterReqDTO requestParam) {
        String username = requestParam.getUsername();
        if (!canUseName(username)) {
            throw new ClientException("用户名重复.不能注册");
        }
        //redis限流
        if (Boolean.FALSE.equals(stringRedisTemplate.opsForValue()
                .setIfAbsent(String.format(REDIS_USERNAME_DUPLICATE_KEY, username), username, 500L, TimeUnit.MILLISECONDS))) {
            throw new ClientException("用户注册频繁，稍后再试");
        }
        if (Boolean.TRUE.equals(stringRedisTemplate.hasKey(String.format(REDIS_USER_REGISTER_KEY, username)))) {
            throw new ClientException("用户名已经被注册，请换一个");
        }
        String md5Encrypted = MD5EncryptUtil.encryptWithSalt(requestParam.getPassword());
        requestParam.setPassword(md5Encrypted);
        UserDO bean = BeanUtil.toBean(requestParam, UserDO.class);
        try {
            baseMapper.insert(bean);
        } catch (DuplicateKeyException e) {
            throw new ClientException("mysql---索引重复");
        }
        userNameBloomFilter.add(username);
        stringRedisTemplate.opsForValue().set(String.format(REDIS_USER_REGISTER_KEY, username), username);
    }

    @Override
    public String doLogin(UserLoginReqDTO requestParam) {
        chainFilterContext.handler(USER_LOGIN_MARK.name(), requestParam);
        UserDO userDO = baseMapper.selectOne(Wrappers.lambdaQuery(UserDO.class)
                .eq(UserDO::getUsername, requestParam.getUsername())
                .eq(UserDO::getDelFlag, 0));
        if (userDO == null) {
            throw new ClientException("请注册再登录..");
        }
        String encrypted = MD5EncryptUtil.encryptWithSalt(requestParam.getPassword());
        System.out.println("encrypted = " + encrypted);
        if (!userDO.getPassword().equals(encrypted)) {
            throw new ClientException("用户名与密码不匹配..");
        }
        Map<String, String> map = new HashMap<>();
        map.put("username", userDO.getUsername());
        map.put("id", userDO.getId().toString());
        map.put("age", userDO.getAge().toString());
        map.put("sex", userDO.getSex().toString());
        String token = JWTUtil.generateToken(map);
        //redis存7天这个token,set可以做到每次刷新这个方法
        stringRedisTemplate.opsForValue()
                .set(String.format(REDIS_USER_LOGIN_KEY, requestParam.getUsername()), token, 7L, TimeUnit.DAYS);
        return token;
    }

    @Override
    @LogRecord(
            success = """
                    【{{#orderDO.purchaseName}}】--购买了--【{{#orderDO.productName}}--花费了【{{#orderDO.price.toString()}}】元】
                    """,
            type = "mock-order",
            bizNo = "bizNo--[{{#orderDO.orderNo}}]",
            fail = "【{{#orderDO.purchaseName}}】--购买了--【{{#orderDO.productName}}--操作失败",
            extra = "[Mock订单类型-->{MOCK_ORDER{#orderDO.orderTarget}}]---uuid-->{{#dbGenerateId}}")
    public void mockOrder(OrderDO orderDO) {
        log.info("mock-mock-mock---{}", orderDO);
        String uuid = UUID.randomUUID().toString();
        log.info("uuid-->{},送入spel上下文", uuid);
        LogRecordContext.putVariable("dbGenerateId", uuid);
//        throw new ClientException("mock--失败");
    }

    @Override
    public void mockExcel(ExcelReqDTO requestParam) {
        excelResolverThreadPool.execute(requestParam);
        //方案1：redis延迟队列
//        RBlockingQueue<Object> blockingQueue = redissonClient.getBlockingQueue("mock-excel-block-queue");
//        RDelayedQueue<Object> delayedQueue = redissonClient.getDelayedQueue(blockingQueue);
//        delayedQueue.offer(requestParam, 20L, TimeUnit.SECONDS);
        //方案2：mq消息队列

//        rocketMQTemplate.syncSendDelayTimeSeconds("zrq-spc-mock-excel-topic", requestParam, 20L);

        rocketMqExcelProducer.senMessage(requestParam);
    }


    /**
     * @param name 需要校验的姓名
     * @return 能用--true，不能用false
     */
    private Boolean canUseName(String name) {
        if (!userNameBloomFilter.contains(name)) {
            return Boolean.TRUE;
        }
        if (Boolean.TRUE.equals(stringRedisTemplate.hasKey(String.format(REDIS_USER_REGISTER_KEY, name)))) {
            return Boolean.FALSE;
        }
        //filter中有，redis中无--误判，或者过期
        UserDO userDO = baseMapper.selectOne(Wrappers.lambdaQuery(UserDO.class)
                .eq(UserDO::getUsername, name));
        //误判
        if (userDO == null) {
            return Boolean.TRUE;
        }
        //数据库中有
        return Boolean.FALSE;
    }
}
