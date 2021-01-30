package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.credential.Credential;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CredentialMapper {
    @Insert("INSERT into credentials (url, username, key, password, userid) " +
            "VALUES (#{url}, #{username}, #{key}, #{password}, #{userId})")
    int insert(Credential credential);

    @Delete("DELETE FROM credentials WHERE credentialid=#{credentialId} AND userid=#{userId}")
    int delete(Credential credential);

    @Update("UPDATE credentials SET url=#{url}, username=#{username}, key=#{key}, password=#{password} " +
            "WHERE credentialid=#{credentialId} AND userid=#{userId}")
    int update(Credential credential);

    @Select("SELECT * FROM credentials WHERE userid=#{userId}")
    List<Credential> getAllCredentialsForUser(Integer userId);
}
