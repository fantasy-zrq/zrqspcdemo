package com.example.model.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.model.dto.req.UserRegisterReqDTO;
import com.example.model.entity.User;
import com.example.model.entity.mapper.UserMapper;
import com.example.model.service.UserService;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RBloomFilter;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import static com.example.model.common.constance.RedisConstanceKey.REDISUSERREGISTERKEY;

/**
 * @author zrq
 * @time 2025/8/6 21:12
 * @description
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private final StringRedisTemplate stringRedisTemplate;
    private final RBloomFilter<String> userNameBloomFilter;

    @Override
    public void doRegister(UserRegisterReqDTO requestParam) {
    }


    /**
     * @param name 需要校验的姓名
     * @return 能用--true，不能用false
     */
    private Boolean canUseName(String name) {
        if (!userNameBloomFilter.contains(name)) {
            return Boolean.TRUE;
        }
        if (Boolean.TRUE.equals(stringRedisTemplate.hasKey(String.format(REDISUSERREGISTERKEY, name)))) {
            return Boolean.FALSE;
        }
        //filter中有，redis中无--误判，或者过期
        User user = baseMapper.selectOne(Wrappers.lambdaQuery(User.class)
                .eq(User::getUsername, name));
        //误判
        if (user == null) {
            return Boolean.TRUE;
        }
        //数据库中有
        return Boolean.FALSE;
    }
}
