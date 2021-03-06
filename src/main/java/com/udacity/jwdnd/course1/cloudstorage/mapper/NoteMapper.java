package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface NoteMapper {
    @Insert("INSERT into notes (notetitle, notedescription, userid) " +
            "VALUES (#{noteTitle}, #{noteDescription}, #{userId})")
    int insert(Note note);

    @Delete("DELETE FROM notes WHERE noteid=#{noteId} AND userid=#{userId}")
    int delete(Note note);

    @Update("UPDATE notes SET notetitle=#{noteTitle}, notedescription=#{noteDescription} " +
            "WHERE noteid=#{noteId} AND userid=#{userId}")
    int update(Note note);

    @Select("SELECT * FROM notes WHERE userid=#{userId}")
    List<Note> getAllNotesForUser(Integer userId);
}
