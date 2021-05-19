package com.ailin.contoller;

import com.ailin.mode.ResultMode;
import com.ailin.server.kafka.KafkaProducerServer;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("kafka")
public class KafkaController {

    @Autowired
    private KafkaProducerServer kafkaProducerServer;

    @RequestMapping("sendKafkaMessage")
    public ResultMode<String> sendKafkaMessage(){

        String userId = "10706a902a1b42a99e396026a841955e";

        Map<String,Object> data = new HashMap<>();
        data.put("userId",userId);
        String o = JSONObject.toJSONString(data);

        kafkaProducerServer.sendMessage("topic02", userId, o);
        return ResultMode.success(null);
    }
}
