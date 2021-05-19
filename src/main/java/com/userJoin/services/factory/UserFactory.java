package com.userJoin.services.factory;

import com.userJoin.config.SourceEnum;
import com.userJoin.services.UserServer;
import com.userJoin.services.impl.HeNanUserServerImpl;
import com.userJoin.services.impl.ZzUserServerImpl;
import com.userJoin.util.SpringUtils;

public class UserFactory {

    public static UserServer getUserServer(String target){

        if(SourceEnum.H_NAN.getType().equals(target)){
            return SpringUtils.getBean(HeNanUserServerImpl.class);
        }
        if(SourceEnum.Z_ZHOU.getType().equals(target)){
            return SpringUtils.getBean(ZzUserServerImpl.class);
        }
        return null;
    }
}
