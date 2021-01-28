package com.udacity.jwdnd.course1.cloudstorage.services.security;

import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class AuthenticationService implements AuthenticationProvider {
    private final HashService hashService;
    private final UserService userService;

    public AuthenticationService(HashService hashService, UserService userService) {
        this.hashService = hashService;
        this.userService = userService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        if (!this.userService.userWithUsernameExists(username)) {
            return null;
        }

        User user = this.userService.getUserByUsername(username);
        String password = authentication.getCredentials().toString();
        String hashedPassword = this.hashService.getHashedValue(password, user.getSalt());

        if (!user.getPassword().equals(hashedPassword)) {
            return null;
        }

        return new UsernamePasswordAuthenticationToken(username, password, new ArrayList<>());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}

