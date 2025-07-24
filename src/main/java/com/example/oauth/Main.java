package com.example.oauth;

import java.io.InputStream;
import java.util.logging.LogManager;

import io.helidon.webserver.WebServer;
import io.helidon.webserver.http.HttpRouting;

public class Main {
    public static void main(String[] args) throws Exception {
        try (InputStream is = Main.class.getResourceAsStream("/logging.properties")) {
            LogManager.getLogManager().readConfiguration(is);
        }

        WebServer server = WebServer.builder()
            .port(8080)
            .routing(Main::routing)
            .build();

        server.start();
        System.out.println("Nima server started on http://localhost:" + server.port());
    }

    private static void routing(HttpRouting.Builder routing) {
        routing.post("/token", new TokenService());
    }
}