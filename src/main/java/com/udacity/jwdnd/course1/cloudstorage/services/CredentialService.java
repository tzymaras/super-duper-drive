package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.credential.Credential;
import com.udacity.jwdnd.course1.cloudstorage.services.security.EncryptionService;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CredentialService {
    private final CredentialMapper credentialMapper;
    private static final EncryptionService encryptionService = new EncryptionService();

    public CredentialService(CredentialMapper credentialMapper) {
        this.credentialMapper = credentialMapper;
    }

    public int insert(Credential credential) {
        encryptPassword(credential);

        return this.credentialMapper.insert(credential);
    }

    public int update(Credential credential) {
        encryptPassword(credential);

        return this.credentialMapper.update(credential);
    }

    public int delete(Integer credentialId, Integer userId) {
        return this.credentialMapper.delete(credentialId, userId);
    }

    public List<Credential> getAllCredentialsForUser(Integer userId) {
        return this.credentialMapper.getAllCredentialsForUser(userId).stream()
            .peek(CredentialService::decryptCredentialPassword)
            .collect(Collectors.toList());
    }

    private static void decryptCredentialPassword(Credential cr) {
        cr.setPlainPassword(
            encryptionService.decryptValue(cr.getPassword(), cr.getKey())
        );
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
