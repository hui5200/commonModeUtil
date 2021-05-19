package com.userJoin.services.impl;

import com.google.gson.Gson;
import com.userJoin.dto.GetUserInfodto;
import com.userJoin.model.ResultMode;
import com.userJoin.model.UserOutModel;
import com.userJoin.services.UserServer;
import gov.henan.iam.sdkforweb.auth.client.TacsAuthClient;
import gov.henan.iam.sdkforweb.auth.request.AuthRequest;
import gov.henan.iam.sdkforweb.auth.response.AuthResult;
import gov.henan.iam.sdkforweb.auth.response.Corporator;
import gov.henan.iam.sdkforweb.auth.response.NaturalUser;
import gov.henan.iam.sdkforweb.client.TacsHttpClient;
import gov.henan.iam.sdkforweb.exception.TacsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.Base64;

@Service
public class HeNanUserServerImpl implements UserServer {

    private static final Logger log = LoggerFactory.getLogger(HeNanUserServerImpl.class);

    @Value("${heNan.pathUrl}")
    private String pathUrl;

    //客户端
    private static TacsAuthClient authClient;

    @Override
    public byte[] getUserInfo(GetUserInfodto getUserInfodto) {
        log.info("开始获取统一信息===========");
        authClient = initClient();
        AuthRequest authRequest = new AuthRequest();
        authRequest.setTokenSNO(getUserInfodto.getAccessToken());
        UserOutModel userOutModel = new UserOutModel();
        try {
            if ("NA".equals(getUserInfodto.getSubjectType())) {//自然人
                AuthResult naturalUser = authClient.getNaturalUser(authRequest);
                log.info("获取自然人结果：code:{},msg:{}",naturalUser.getCode(),naturalUser.getMsg());
                NaturalUser user = naturalUser.getUser();
                if (user != null) {
                    userOutModel.setUserCard(user.getCertNo());
                    userOutModel.setUserName(user.getUserName());
                    userOutModel.setUserPhone(user.getUserMobile());
                }
            } else {//法人
                AuthResult corpUser = authClient.getCorpUser(authRequest);
                log.info("获取法人结果：code:{},msg:{}",corpUser.getCode(),corpUser.getMsg());
                Corporator corp = corpUser.getCorp();
                if (corp != null) {
                    userOutModel.setUserCard(corp.getCertificateSno());
                    userOutModel.setUserName(corp.getCorpName());
                    userOutModel.setUserPhone(corp.getUserMobile());

                    //法定代表人信息
                    userOutModel.setUserPhone(corp.getCertNo());
                    userOutModel.setUserPhone(corp.getUserName());
                }
            }
            log.info("统一用户信息：{}", userOutModel);
        } catch (TacsException e) {
            e.printStackTrace();
            return Base64.getDecoder().decode(new Gson().toJson(ResultMode.fail()));
        }
        userOutModel.setUserCard("55165165");
        userOutModel.setUserName("测试");
        userOutModel.setUserPhone("156165165156");
        String s = new Gson().toJson(ResultMode.success(userOutModel));
        return Base64.getEncoder().encode(s.getBytes());
    }

    //初始化客户端
    private TacsAuthClient initClient() {

        try {
            if (authClient == null) {
                URL url = Thread.currentThread().getContextClassLoader().getResource("tacs.cer");
                log.info("获取证书的url：{}", url);
                String srtStr = url.getPath();
                String tacsUrl = srtStr.substring(1, srtStr.length() - 9);
                System.out.println("tacsUrl>>>"+tacsUrl);
//                //jar包里面文件读取不到，修改地址到  WEB-INF 下面
//                String tacsUrl = srtStr.split("lib")[0];
//                //过滤前缀file
//                tacsUrl = tacsUrl.split("file:")[1];
//                tacsUrl = tacsUrl.replaceFirst("/tacs.cer","");
//                logger.info("拼接后的url地址：{}", tacsUrl);

                TacsHttpClient.init(tacsUrl, pathUrl);
                authClient = TacsAuthClient.getInstance();
                log.info("统一客户端对象：",authClient);
            }
        } catch (Exception e) {
            log.error("获取统一对象异常",e);
        }
        return authClient;
    }
}
