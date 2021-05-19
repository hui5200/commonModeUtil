package com.ailin.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
//@PropertySource({"classpath:application.yml"})
public class RedisConfigure {

    private static final Logger logger = Logger.getLogger(RedisConfigure.class);

    @Value("${redis.host}")
    private String host;

    @Value("${redis.port}")
    private int port;

    @Value("${redis.pwd}")
    private String pwd;

    @Value("${redis.timeout}")
    private int timeout;

    @Value("${redis.maxIdle}")
    private int maxIdle;

    @Value("${redis.maxTotal}")
    private int maxTotal;

    @Value("${redis.maxWaitMillis}")
    private int maxWaitMillis;

    @Value("${redis.database}")
    private int database;

//    @Value("${redis.minEvictableIdleTimeMillis}")
//    private int minEvictableIdleTimeMillis;
//
//    @Value("${redis.numTestsPerEvictionRun}")
//    private int numTestsPerEvictionRun;
//
//    @Value("${redis.timeBetweenEvictionRunsMillis}")
//    private int timeBetweenEvictionRunsMillis;
//
//    @Value("${redis.testOnBorrow}")
//    private int testOnBorrow;
//
//    @Value("${redis.testWhileIdle}")
//    private int testWhileIdle;

    @Bean
    public JedisPoolConfig jedisPoolConfig(){

        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxIdle(this.maxIdle);
        config.setMaxWaitMillis(this.maxWaitMillis);
        config.setMaxTotal(this.maxTotal);
        return config;
    }

    @Bean
    public JedisConnectionFactory jedisConnectionFactory(){

        JedisConnectionFactory connectionFactory = new JedisConnectionFactory();
        connectionFactory.setHostName(this.host);
        connectionFactory.setPassword(this.pwd);
        connectionFactory.setPort(this.port);
        connectionFactory.setDatabase(this.database);
        connectionFactory.setTimeout(this.timeout);
        connectionFactory.setPoolConfig(jedisPoolConfig());

        logger.info("redis初始化成功！");
        return connectionFactory;
    }

    @Bean
    public RedisTemplate<?,?> getRedisTemplate(@Qualifier("jedisConnectionFactory") RedisConnectionFactory factory){
        RedisTemplate<?,?> redisTemplate = new RedisTemplate();

        redisTemplate.setConnectionFactory(factory);
        //key序列化方式;但是如果方法上有Long等非String类型的话，会报类型转换错误；
        //Long类型不可以会出现异常信息;
        RedisSerializer<String> redisSerializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(redisSerializer);
        redisTemplate.setHashKeySerializer(redisSerializer);

        //JdkSerializationRedisSerializer序列化方式;
        JdkSerializationRedisSerializer jdkRedisSerializer=new JdkSerializationRedisSerializer();
        //Json序列化方式
        Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        serializer.setObjectMapper(objectMapper);

        redisTemplate.setValueSerializer(serializer);
        redisTemplate.setHashValueSerializer(serializer);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }
}
