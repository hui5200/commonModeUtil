package com.ailin.server.kafka;

import com.ailin.Pojo.User;
import com.ailin.server.RedisServer;
import com.alibaba.fastjson.JSONObject;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.google.gson.Gson;
import org.apache.kafka.clients.consumer.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.concurrent.*;

@Component
public class KafkaConsumerServer {

    private final static Logger logger = LoggerFactory.getLogger(KafkaConsumerServer.class);

    private static volatile KafkaConsumer<String,Object> kafkaConsumer;

    @Autowired
    private RedisServer redisServer;

    public KafkaConsumerServer(){
        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put(ConsumerConfig.CLIENT_ID_CONFIG, "client01");
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "group01");
        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        properties.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, 900000);
        properties.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 10);
        kafkaConsumer = new KafkaConsumer<>(properties);
        kafkaConsumer.subscribe(Collections.singletonList("topic01"));
//        onMessage();
    }

    public void onMessage(){
        ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat("kafka-user-info").build();
        ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(10, 50, 300,
                TimeUnit.SECONDS, new ArrayBlockingQueue<>(10), threadFactory);
        poolExecutor.submit(() -> {
            while (true){

                long beginTime = System.currentTimeMillis();
                long nowTime = 0L;
                try {
                    ConsumerRecords<String, Object> results = kafkaConsumer.poll(2000);
                    nowTime = System.currentTimeMillis();
                    Iterator<ConsumerRecord<String, Object>> iterator = results.iterator();
                    if(!iterator.hasNext()){
                        logger.info("kafka拉取用户数据为空！！！");
                        continue;
                    }
                    while (iterator.hasNext()){
                        ConsumerRecord<String, Object> next = iterator.next();
                        String key = next.key();
                        String value = (String)next.value();
                        Map map = JSONObject.parseObject(value, Map.class);
                        String userId = map.get("userId").toString();

                        String userInfo = (String)redisServer.findDataByKey("UserInfo", userId);
                        System.out.println(JSONObject.parseObject(userInfo, User.class));
                        System.out.println("每次消费时间："+ (nowTime-beginTime) + "ms");
                    }
                    //手动提交
                    kafkaConsumer.commitSync();

                }catch (Exception e){
                    logger.error("kafka消费异常：{}", e);
                }

            }
        });
    }

//    @KafkaListener(group = "group01", topics = "topic01")
    public void onMessage(ConsumerRecord<String, Object> record, Acknowledgment ack){

        System.out.println(record.value());
        String value = (String)record.value();
        if(StringUtils.isEmpty(value)){
            logger.info("kafka消费为空！！！");
            return;
        }
        Gson gson = new Gson();
        Map map = gson.fromJson(value, Map.class);
        for (Object o : map.values()) {
            String userInfo = (String)redisServer.findDataByKey("UserInfo", o.toString());
            logger.info("用户信息：{}", userInfo);
        }
        //手工提交
        ack.acknowledge();
    }

}
