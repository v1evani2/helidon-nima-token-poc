package com.example.oauth;

import io.helidon.http.Status;
import io.helidon.webserver.http.ServerRequest;
import io.helidon.webserver.http.ServerResponse;
import io.helidon.webserver.http.Handler;
import jakarta.json.Json;
import jakarta.json.JsonObject;

import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;

import java.io.StringReader;
import java.util.logging.Logger;

public class TokenService implements Handler {
    private static final Logger LOGGER = Logger.getLogger(TokenService.class.getName());
    private static final RedisClient redisClient = RedisClient.create("redis://localhost:6379");
    private static final StatefulRedisConnection<String, String> connection = redisClient.connect();
    private static final RedisCommands<String, String> redisCommands = connection.sync();

    @Override
    public void handle(ServerRequest req, ServerResponse res) {
        String body = req.content().as(String.class);
        JsonObject json = Json.createReader(new StringReader(body)).readObject();
        String clientId = json.getString("client_id", null);
        String clientSecret = json.getString("client_secret", null);

        String confClientSecret = redisCommands.hget("oauth_clients", clientId);
        int confTokenExpiry = Integer.parseInt(redisCommands.get("token_expiry"));

        try {
            String token = TokenGenerator.generateToken(clientId, clientSecret, confClientSecret, confTokenExpiry);
            JsonObject response = Json.createObjectBuilder()
                .add("access_token", token)
                .add("token_type", "bearer")
                .add("expires_in", confTokenExpiry)
                .build();

            res.header("content-type", "application/json");
            res.send(response.toString());
        } catch (RuntimeException ex) {
            LOGGER.warning(ex.getMessage());
            res.status(Status.UNAUTHORIZED_401).send("Invalid client credentials");
        }
    }
}