package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.File;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FileMapper {
    @Select("SELECT * FROM files WHERE filename=#{fileName} AND userid=#{userId}")
    File get(String fileName, Integer userId);

    @Select("SELECT * FROM files WHERE userid=#{userid}")
    List<File> getAllUserFiles(Integer userId);

    @Select("SELECT EXISTS(SELECT * FROM files WHERE filename=#{filename} AND userid=#{userId})")
    boolean fileExists(String filename, Integer userId);

    @Insert("INSERT into files (filename, contenttype, filesize, userid, filedata) " +
            "VALUES (#{fileName}, #{contentType}, #{fileSize}, #{userId}, #{fileData})")
    int insert(File file);

    @Delete("DELETE FROM files WHERE filename=#{filename} AND userid=#{userId}")
    int delete(String filename, Integer userId);
}
