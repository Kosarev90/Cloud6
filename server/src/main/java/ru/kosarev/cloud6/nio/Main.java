package ru.kosarev.cloud6.nio;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        NioServer nioServer = new NioServer();
        nioServer.start();
    }
}
