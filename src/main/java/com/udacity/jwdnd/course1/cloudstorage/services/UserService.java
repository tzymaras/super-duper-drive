package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.security.HashService;
import com.udacity.jwdnd.course1.cloudstorage.services.security.PasswordValidator;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;

@Service
public class UserService {
    private final UserMapper userMapper;
    private final HashService hashService;

    public UserService(UserMapper userMapper, HashService hashService) {
        this.userMapper = userMapper;
        this.hashService = hashService;
    }

    public User getUserByUsername(String username) {
        return this.userMapper.getUserByUsername(username);
    }

    public boolean userWithUsernameExists(String username) {
        return this.userMapper.usernameExists(username);
    }

    public boolean isPasswordValid(String password) {
        return PasswordValidator.isPasswordValid(password);
    }

    /**
     * Creates a new {@code User} entry in the Users table
     *
     * @param user the {@code User} to be added
     * @return int number of inserted rows
     */
    public int createUser(User user) {
        byte[] salt = new byte[16];

        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(salt);

        String encodedSalt = Base64.getEncoder().encodeToString(salt);
        String hashedPassword = this.hashService.getHashedValue(user.getPassword(), encodedSalt);

        user.setUserId(null);
        user.setPassword(hashedPassword);
        user.setSalt(encodedSalt);

        return this.userMapper.insert(user);
    }
}
