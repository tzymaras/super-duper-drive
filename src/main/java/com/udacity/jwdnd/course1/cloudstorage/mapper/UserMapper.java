package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {
    @Insert("INSERT into users (username, firstname, lastname, password, salt) " +
            "VALUES (#{username}, #{firstName}, #{lastName}, #{password}, #{salt})")
    int insert(User user);

    @Select("SELECT * FROM users WHERE username = #{username}")
    User getUserByUsername(String username);

    @Select("select count(*) from users where username=#{username}")
    boolean usernameExists(String username);
}