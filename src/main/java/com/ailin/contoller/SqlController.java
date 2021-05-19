package com.ailin.contoller;

import com.ailin.Pojo.LicenseIndexReference;
import com.ailin.Pojo.User;
import com.ailin.anotation.Auth;
import com.ailin.dto.getUserDto;
import com.ailin.mapper.UserMapper;
import com.ailin.mode.CloudRequest;
import com.ailin.mode.ResultMode;
import com.ailin.server.RedisServer;
import com.ailin.server.UserServer;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.UUID;

@RestController
@RequestMapping("sql")
public class SqlController extends BaseController {
    private final static Logger logger = LoggerFactory.getLogger(SqlController.class);

    @Autowired
    private UserServer userServer;

    @Autowired
    private RedisServer redisServer;

    @RequestMapping("addUser")
    public ResultMode<String> addUser(){

        User user = new User();
        user.setUserId(UUID.randomUUID().toString().replaceAll("-",""));
        user.setName("kafka测试1");
        user.setAddress("南安");
        user.setAge(14);
        user.setSex("F");

        int i = userServer.addUser(user);
        logger.info("用户id：{}", user.getUserId());

        if(i != 0){
            redisServer.add("UserInfo", user.getUserId(), JSONObject.toJSONString(user));
        }else {
            return ResultMode.fail();
        }

        return ResultMode.success(null);

    }

    @RequestMapping("addLicenseIndexReference")
    public ResultMode<String> addLicenseIndexReference(){

        String idCard = "1314555";
        String route = "";
        String licenseId = "";
        String licenseNumber = "";
        String licenseName = "";
        String catalogId = "";
        String catalogTypeCode = "";
        String catalogTypeName = "";
        Date sourceUpdateTime = new Date();
        String issueUnitName = "";
        Integer state = 11;
        String category = "";
        String area = "";
        LicenseIndexReference licenseIndex = new LicenseIndexReference(idCard, route, licenseId, licenseNumber,
                licenseName, catalogTypeName, catalogTypeCode, catalogId, sourceUpdateTime, issueUnitName, state, category, area);

        int i = userServer.addLicenseIndexReference(licenseIndex);

        return ResultMode.success(null);

    }

    @RequestMapping(value = "getUser", method = RequestMethod.POST)
//    @Auth(isAuth = true)
    public ResultMode<User> getUser(getUserDto dto, @com.ailin.anotation.RequestParam(required = true) String s, @RequestParam int i){

        String userId = "10706a902a1b42a99e396026a841955e";
        User user = new User();

        CloudRequest token = getCloudRequest();

        try {
//            String userInfo = (String)redisServer.findDataByKey("UserInfo", userId);
//            user = JSONObject.parseObject(userInfo, User.class);
        } catch (Exception e) {
            logger.info("获取用户,缓存失败", e);
        }
        if(user == null || StringUtils.isEmpty(user.getUserId())){
            logger.info("开始从数据库中获取用户数据");
            user = userServer.getUserByUserId(userId,dto.getToken());
            String u = JSONObject.toJSONString(user);
            if(user != null && !StringUtils.isEmpty(user.getUserId())){
                redisServer.add("UserInfo", user.getUserId(), u);
            }
            logger.info("用户信息：{}", u);
        }

        return ResultMode.success(user);

    }
}
