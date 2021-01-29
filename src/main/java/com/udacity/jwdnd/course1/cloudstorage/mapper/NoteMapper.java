package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.note.Note;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface NoteMapper {
    @Insert("INSERT into notes (notetitle, notedescription, userid) " +
            "VALUES (#{noteTitle}, #{noteDescription}, #{userId})")
    int insert(Note note);

    @Delete("DELETE FROM notes WHERE noteid=#{noteId}")
    int delete(Integer noteId);

    @Update("UPDATE notes SET notetitle=#{noteTitle}, notedescription=#{noteDescription}, userid=#{userId} " +
            "WHERE noteid=#{noteId}")
    int update(Note note);

    @Select("SELECT * FROM notes WHERE userid=#{userId}")
    List<Note> getAllNotesForUser(Integer userId);

    @Select("SELECT EXISTS(SELECT * FROM notes WHERE noteid=#{noteId} and userid=#{userId})")
    boolean noteExistsForUser(Integer noteId, Integer userId);
}
