package com.hncu.manager;

import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * @Author caimeisahng
 * @Date 2024/7/13 4:08
 * @Version 1.0
 * manager 层，阿里开发手册推荐，与service相似，处于Service与dao层之间
 */

@Component
public class RedisManager {

    @Resource
    private RedisTemplate<String,Object> redisTemplate;

    private static final Logger logger = LoggerFactory.getLogger(RedisManager.class);


    public Object getValue(String key){
        // redis中可以存放数据类型String，list，hash,set,zset
        //此处可以使用String，list(被选择)
        return redisTemplate.opsForList().range(key,0,-1);
    }

    public <T> Object setValue(String key, Collection<T> data){
        // redis中可以存放数据类型String，list，hash,set,zset
        //此处可以使用String，list(被选择)

        Object[] t = new Object[data.size()];
        data.toArray(t);
        return redisTemplate.opsForList().leftPushAll(key,t);

    }
}
