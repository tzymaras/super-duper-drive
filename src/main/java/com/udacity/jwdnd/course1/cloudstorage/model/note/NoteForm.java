package com.udacity.jwdnd.course1.cloudstorage.model.note;

public class NoteForm {
    private Integer noteId;
    private String noteTitle;
    private String noteDescription;
    private Integer userId;
    private String action;

    public Integer getNoteId() {
        return noteId;
    }

    public void setNoteId(Integer noteId) {
        this.noteId = noteId;
    }

    public String getNoteTitle() {
        return noteTitle;
    }

    public void setNoteTitle(String noteTitle) {
        this.noteTitle = noteTitle;
    }

    public String getNoteDescription() {
        return noteDescription;
    }

    public void setNoteDescription(String noteDescription) {
        this.noteDescription = noteDescription;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    @Override
    public String toString() {
        return "NoteForm{" +
            "noteId=" + noteId +
            ", noteTitle='" + noteTitle + '\'' +
            ", noteDescription='" + noteDescription + '\'' +
            ", userId=" + userId +
            ", action='" + action + '\'' +
            '}';
    }
}