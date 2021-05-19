package com.ailin.server.impl;

import com.ailin.Pojo.LicenseIndexReference;
import com.ailin.Pojo.User;
import com.ailin.anotation.Auth;
import com.ailin.mapper.LicenseIndexReferenceDao;
import com.ailin.mapper.UserMapper;
import com.ailin.server.UserServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServerImpl implements UserServer {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private LicenseIndexReferenceDao licenseIndexReferenceDao;

    @Override
    public int addUser(User user) {
        int i = userMapper.addUser(user);
        return i;
    }

    @Override
    public User getUserByUserId(String userId, String token) {

        User user = userMapper.getUserByUserId(userId);
        System.out.println("");
        return user;
    }

    @Override
    @Transactional()
    public int addLicenseIndexReference(LicenseIndexReference licenseIndex) {

        int i = licenseIndexReferenceDao.insert(licenseIndex);
        return i;
    }
}
