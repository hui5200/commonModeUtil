package com.ailin.contoller;

import com.ailin.Pojo.User;
import com.ailin.mode.ResultMode;
import com.ailin.server.RedisServer;
import com.google.gson.Gson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.redis.util.RedisLockRegistry;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

@RestController
@RequestMapping("redis")
public class RedisController {

    @Autowired
    private RedisServer redisServer;

    @Autowired
    private RedisLockRegistry redisLockRegistry;

    @Autowired
    private RedissonClient redissonClient;

    private int num = 20;


    @RequestMapping(value = "getData")
    public ResultMode<User> getData(){

        String uuid = UUID.randomUUID().toString().replaceAll("-", "");

        User user = new User(uuid,"测试","M","福建",18);

        Gson gson = new Gson();
        String userJson = gson.toJson(user);

        redisServer.add("UserInfo", uuid, userJson);

        String s = redisServer.findDataByKey("UserInfo", uuid).toString();

        User user1 = gson.fromJson(s, User.class);

        return ResultMode.success(user1);
    }

    @RequestMapping(value = "deleteAll")
    public ResultMode<Long> deleteAll(){

        Map<String,Object> map = new HashMap<>();

        String[] s = new String[10];
        for (int i = 0; i < 10; i++) {
            s[i] = Integer.toString(i);
        }
        Long delete = redisServer.delete("test", s);
        map.put("delete",delete);

        return ResultMode.success(delete);
    }

    /**
     * 测试redis分布式锁(没有锁)
     */
    @GetMapping("testUnLock")
    public void testUnLock() throws InterruptedException {
        String s = Thread.currentThread().getName();
        if (num > 0) {
            System.out.println(s + "排号成功，号码是：" + num);
            num--;
        } else {
            System.out.println(s + "排号失败,号码已经被抢光");
        }
    }

    /**
     * 测试redis分布式锁(有锁)
     */
    @GetMapping("testLock")
    public void testLock() throws InterruptedException {
        Lock lock = null;
        try {
            lock = redisLockRegistry.obtain("lock");
            boolean isLock = lock.tryLock(1, TimeUnit.SECONDS);
            String s = Thread.currentThread().getName();
            if (num > 0 && isLock) {
                System.out.println(s + "排号成功，号码是：" + num);
                num--;
            } else {
                System.out.println(s + "排号失败,号码已经被抢光");
            }
        } finally {
            if(lock != null) {
                lock.unlock();
            }
        }
    }

    @GetMapping("testRedissonLock")
    public void testRedissonLock() throws InterruptedException {
        RLock lock = null;
        try {
            lock = redissonClient.getLock("lock1");
            String s = Thread.currentThread().getName();
            if (num > 0 && lock.tryLock(1, TimeUnit.SECONDS)) {
                System.out.println(s + "排号成功，号码是：" + num);
                num--;
            } else {
                System.out.println(s + "排号失败,号码已经被抢光");
            }
        } finally {
            if(lock != null) lock.unlock();
        }
    }

}
