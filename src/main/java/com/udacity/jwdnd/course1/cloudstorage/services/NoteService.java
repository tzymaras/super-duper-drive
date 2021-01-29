package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.NoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.note.Note;
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

    public int delete(Integer noteId) {
        return this.noteMapper.delete(noteId);
    }

    public int update(Note note) {
        return this.noteMapper.update(note);
    }

    public List<Note> getAllNotesForUser(Integer userId) {
        return this.noteMapper.getAllNotesForUser(userId);
    }

    public boolean noteExistsForUser(Integer noteId, Integer userId) {
        return this.noteMapper.noteExistsForUser(noteId, userId);
    }
}
