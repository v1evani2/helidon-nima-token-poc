package com.example.oauth;

import java.util.logging.Logger;


public class TokenGenerator {
    
    private static final Logger LOGGER = Logger.getLogger(TokenGenerator.class.getName());

    public static String generateToken(String clientId, String secret, String confClientSecret, int expiry) {
        if (confClientSecret == null) {
            LOGGER.warning("stored secret is null");
            throw new RuntimeException("stored secret is null");
        }
       
        if (!confClientSecret.equals(secret)) {
            LOGGER.warning("stored secret is NOT MATCHING");

            throw new RuntimeException("Invalid secret is given");
        }
        return JwtUtil.createJwt(clientId, expiry);
    }
}