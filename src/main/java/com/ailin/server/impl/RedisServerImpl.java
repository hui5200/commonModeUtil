package com.ailin.server.impl;

import com.ailin.server.RedisServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RedisServerImpl implements RedisServer {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public Object findDataByKey(String hkey, String key) {

        return redisTemplate.opsForHash().get(hkey,key);
    }

    @Override
    public int add(String hkey, String key, String value) {

        redisTemplate.opsForHash().put(hkey, key ,value);
        return 0;
    }

    @Override
    public Long delete(String hkey, Object... keys) {

        return redisTemplate.opsForHash().delete(hkey, keys);
    }

    @Override
    public List<String> values(String hkey) {

        return null;
    }

    @Override
    public Map<String, Object> findAllByKey(String hkey) {
        Map<String,Object> result = new HashMap<>();
        Set<String> keys = (Set<String>)(Object)redisTemplate.opsForHash().keys(hkey);
        keys.forEach(s -> {
            result.put(s, findDataByKey(hkey,s));
        });
        return result;
    }
}
