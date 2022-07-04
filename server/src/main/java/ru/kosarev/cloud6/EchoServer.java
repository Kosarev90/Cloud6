package ru.kosarev.cloud6;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class EchoServer {
    public static void main(String[] args) throws IOException {

        try (ServerSocket server = new ServerSocket(8190)){
            System.out.println("Server connected");
            while (true){
                Socket socket =server.accept();
                ChatHandler chatHandler = new ChatHandler(socket);
                new Thread(chatHandler).start();
            }
        }

    }
}
