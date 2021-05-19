package com.ailin.mapper;

import com.ailin.Pojo.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

public interface UserMapper extends Mapper<User> {

    @Insert("insert into user(user_id,name,age,sex,address) values (#{userId},#{name},#{age},#{sex},#{address})")
    int addUser(User user);

    @Select("select * from user where user_id = #{userId}")
    @Results({
            @Result(property = "userId", column = "user_id")
    })
    User getUserByUserId(String userId);
}
