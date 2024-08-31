package com.hncu.service.impl;

import com.hncu.service.RedisService;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RedisServiceImpl implements RedisService {


    @Resource
    private RedisTemplate<String,Object> redisTemplate;
    //private RedisManager redisManager;

    @Override
    public void setValue(String key, Object value) {
        redisTemplate.opsForValue().set(key,value);
    }

    @Override
    public Object getValue(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    @Override
    public Boolean delete(String key) {
        return redisTemplate.delete(key);
    }

    @Override
    public Boolean expire(String key, long timeout, TimeUnit unit) {
        // return redisManager.expire(key, timeout, unit);
        return redisTemplate.expire(key,timeout,unit);
    }
}
