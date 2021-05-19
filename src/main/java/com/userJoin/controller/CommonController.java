package com.userJoin.controller;

import com.userJoin.dto.GetUserInfodto;
import com.userJoin.services.UserServer;
import com.userJoin.services.factory.UserFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/common")
public class CommonController {

    @RequestMapping("getUserInfo")
    public byte[] getUserInfo(GetUserInfodto getUserInfodto){

        UserServer userServer = UserFactory.getUserServer(getUserInfodto.getTarget());
        if(userServer != null){
            return userServer.getUserInfo(getUserInfodto);
        }
        return null;
    }
}
