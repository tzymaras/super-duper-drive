package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.services.security.EncryptionService;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CredentialService {
    private final CredentialMapper credentialMapper;
    private final EncryptionService encryptionService;

    public CredentialService(CredentialMapper credentialMapper, EncryptionService encryptionService) {
        this.credentialMapper = credentialMapper;
        this.encryptionService = encryptionService;
    }

    public int insert(Credential credential) {
        encryptPassword(credential);

        return this.credentialMapper.insert(credential);
    }

    public int update(Credential credential) {
        encryptPassword(credential);

        return this.credentialMapper.update(credential);
    }

    public int delete(Credential credential) {
        return this.credentialMapper.delete(credential);
    }

    public List<Credential> getAllCredentialsForUser(Integer userId) {
        return this.credentialMapper.getAllCredentialsForUser(userId).stream()
                .peek(this::decryptCredentialPassword)
                .collect(Collectors.toList());
    }

    private void decryptCredentialPassword(Credential cr) {
        String decryptedPassword = this.encryptionService.decryptValue(cr.getPassword(), cr.getKey());
        cr.setPlainPassword(decryptedPassword);
    }

    private void encryptPassword(Credential credential) {
        byte[] key = new byte[16];

        SecureRandom random = new SecureRandom();
        random.nextBytes(key);

        String encodedKey = Base64.getEncoder().encodeToString(key);
        String encryptedPassword = encryptionService.encryptValue(credential.getPassword(), encodedKey);

        credential.setKey(encodedKey);
        credential.setPassword(encryptedPassword);
    }
}
