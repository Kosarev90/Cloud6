package ru.kosarev.cloud6;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Network {
    private final DataInputStream is; // входящий поток
    private final DataOutputStream os; // исходящий поток


    public Network(int port) throws IOException {
        Socket socket = new Socket("localhost", port);
        is = new DataInputStream(socket.getInputStream());
        os = new DataOutputStream(socket.getOutputStream());
    }
    public String readString() throws IOException {
        return is.readUTF();
    }
    public void writeMessage(String message) throws IOException {
        os.writeUTF(message);
        os.flush();
    }
    public int readInt() throws IOException {
        return is.readInt();
    }
    public DataOutputStream getOs(){
        return  os;
    }
    public DataInputStream getIs(){
        return is;
    }
}
