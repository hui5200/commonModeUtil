package com.ailin.server.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Service
public class KafkaProducerServer {

    private final static Logger logger = LoggerFactory.getLogger(KafkaProducerServer.class);

    @Autowired
    private KafkaTemplate<String,Object> kafkaTemplate;

    public void sendMessage(String topic, String key, Object data){

        ListenableFuture<SendResult<String, Object>> future = kafkaTemplate.send(topic, key , data);

        future.addCallback(new ListenableFutureCallback<SendResult<String, Object>>() {
            @Override
            public void onFailure(Throwable ex) {
                logger.error("消息发送失败: {}", ex.getMessage());
            }

            @Override
            public void onSuccess(SendResult<String, Object> result) {

                logger.info("消息发送成功: {}", result.getProducerRecord().value());
            }
        });
    }
}
