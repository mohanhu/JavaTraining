package com.codelooper.training.core.di;

import java.io.IOException;

import okhttp3.mockwebserver.MockWebServer;

// Keeps one MockWebServer instance accessible to the test DI module.
public class MockServerHolder {

    public static MockWebServer server;

    public static void start() throws IOException {
        if (server == null) {
            server = new MockWebServer();
            server.start();
        }
    }

    public static void shutdown() throws IOException {
        if (server != null) {
            server.shutdown();
            server = null;
        }
    }
}