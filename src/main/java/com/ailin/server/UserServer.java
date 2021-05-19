package com.ailin.server;

import com.ailin.Pojo.LicenseIndexReference;
import com.ailin.Pojo.User;

public interface UserServer {


    /**
     * 添加用户
     * @param user
     * @return
     */
    int addUser(User user);

    /**
     * 通过userId获取用户信息
     * @param userId
     * @param token
     * @return
     */
    User getUserByUserId(String userId, String token);

    int addLicenseIndexReference(LicenseIndexReference licenseIndex);
}
