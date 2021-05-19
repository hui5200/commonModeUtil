package com.userJoin.services;

import com.userJoin.dto.GetUserInfodto;

public interface UserServer {

    byte[] getUserInfo(GetUserInfodto getUserInfodto);
}
