package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FileService {
    private final FileMapper fileMapper;

    public FileService(FileMapper fileMapper) {
        this.fileMapper = fileMapper;
    }

    public File get(String fileName, Integer userId) {
        return this.fileMapper.get(fileName, userId);
    }

    public int insert(File file) {
        return this.fileMapper.insert(file);
    }

    public List<File> getAllUserFiles(Integer userId) {
        return this.fileMapper.getAllUserFiles(userId);
    }

    public int delete(String filename, Integer userId) {
        return this.fileMapper.delete(filename, userId);
    }

    public boolean fileExists(String filename, Integer userId) {
        return this.fileMapper.fileExists(filename, userId);
    }
}
