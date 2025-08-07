package com.example.model.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.model.common.exception.ClientException;
import com.example.model.common.utils.MD5EncryptUtil;
import com.example.model.dto.req.UserRegisterReqDTO;
import com.example.model.entity.UserDO;
import com.example.model.entity.mapper.UserMapper;
import com.example.model.service.UserService;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RBloomFilter;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

import static com.example.model.common.constance.RedisConstanceKey.REDIS_USERNAME_DUPLICATE_KEY;
import static com.example.model.common.constance.RedisConstanceKey.REDIS_USER_REGISTER_KEY;

/**
 * @author zrq
 * @time 2025/8/6 21:12
 * @description
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, UserDO> implements UserService {

    private final StringRedisTemplate stringRedisTemplate;
    private final RBloomFilter<String> userNameBloomFilter;

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
        String md5Encrypted = MD5EncryptUtil.md5Encode(requestParam.getPassword());
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
