package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.NoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService {
    private final NoteMapper noteMapper;

    public NoteService(NoteMapper noteMapper) {
        this.noteMapper = noteMapper;
    }

    public int insert(Note note) {
        return this.noteMapper.insert(note);
    }

    public int delete(Note note) {
        return this.noteMapper.delete(note);
    }

    public int update(Note note) {
        return this.noteMapper.update(note);
    }

    public List<Note> getAllNotesForUser(Integer userId) {
        return this.noteMapper.getAllNotesForUser(userId);
    }
}
