package sample;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import common.Transport;
import common.data.Task;

public class Client implements AutoCloseable {

    public interface ClientCallback {
        void onTalk(String message);
    }

    private Socket socket;

    public Client(String host, int port, ClientCallback callback)
            throws UnknownHostException, IOException {
        socket = new Socket(host, port);
        new Thread(() -> {
            while (socket != null && !socket.isClosed()) {
                try {
                    callback.onTalk(Transport.receive(socket));
                } catch (Exception e) {
                }
            }
        }).start();
    }

    public void send(String message) throws IOException {
        Transport.send(message, socket);
    }

    @Override
    public void close() {
        try {
            System.out.println("Connection closed.");
            socket.close();
            System.exit(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}