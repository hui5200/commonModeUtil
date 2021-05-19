package com.ailin.contoller;

import com.ailin.Pojo.User;
import com.ailin.mode.ResultMode;
import com.ailin.server.RedisServer;
import com.ailin.server.UserServer;
import com.ailin.util.CaptchaUtils;
import com.ailin.util.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("upload")
public class FileUpController {

    private final static Logger logger = LoggerFactory.getLogger(FileUpController.class);

    @Autowired
    private UserServer userServer;

    @Autowired
    private RedisServer redisServer;

    @PostMapping(value = "image")
    public ResultMode<User> upload(String name, String age, List<MultipartFile> files) {

        System.out.println(name + " : " + age);

        for (MultipartFile f : files) {
            System.out.println(f.getOriginalFilename());
        }
        String userId = "10706a902a1b42a99e396026a841955e";
        User user = new User();

        try {
//            String userInfo = (String)redisServer.findDataByKey("UserInfo", userId);
//            user = JSONObject.parseObject(userInfo, User.class);
        } catch (Exception e) {
            logger.info("获取用户缓存失败：{}", e);
        }
        if(user == null || StringUtils.isEmpty(user.getUserId())){
            logger.info("开始从数据库中获取用户数据");
            String token = "test";
            user = userServer.getUserByUserId(userId, token);
            String u = JSONObject.toJSONString(user);
            if(user != null && !StringUtils.isEmpty(user.getUserId())){
                redisServer.add("UserInfo", user.getUserId(), u);
            }
            logger.info("用户信息：{}", u);
        }
        user.setName(name);
        user.setAge(Integer.parseInt(age));
        return ResultMode.success(user);
    }

    @RequestMapping("test")
    public ResultMode<Map> test(){

        String s = HttpUtil.testUploadImage();

        Map jsonObject = JSONObject.parseObject(s, Map.class);
//        JsonObject jsonObject = JSONObject.parseObject(s, JsonObject.class);
        User user = null;
        Map data = JSONObject.parseObject(jsonObject.get("data").toString(), Map.class);
//        JsonObject data = jsonObject.get("data");
//        user.setUserId(data.get("userId").toString());
//        user.setName(data.get("name").toString());
//        user.setSex(data.get("sex").toString());
//        user.setAddress(data.get("address").toString());
//        user.setAge(data.get("age").toString());

        return ResultMode.success(data);
    }

    @RequestMapping("getImage")
    public ResultMode<String> getImage(HttpServletRequest request, HttpServletResponse response) throws IOException {

        CaptchaUtils.genCaptcha(request, response);

        String code = redisServer.findDataByKey("CodeList", "test").toString();
        return ResultMode.success(null);
    }
}
