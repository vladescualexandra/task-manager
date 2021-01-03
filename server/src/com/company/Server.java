package com.company;

import com.company.common.Transport;
import com.company.database.Database;
import com.company.database.Interrogation;
import com.company.common.data.Severity;
import com.company.common.data.Status;
import com.company.common.data.Task;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Server implements AutoCloseable {

    private ServerSocket serverSocket;
    private ExecutorService executorService;

    @Override
    public void close() throws Exception {
        stop();
    }

    public void start(int port) throws IOException {
        stop();
        if (Database.openConnection()) {
            serverSocket = new ServerSocket(port);
            executorService = Executors.newFixedThreadPool(50 * Runtime.getRuntime().availableProcessors());
            final List<Socket> clients = Collections.synchronizedList(new ArrayList<>());

            executorService.execute(() -> {
                while (serverSocket != null && !serverSocket.isClosed()) {
                    try {
                        final Socket socket = serverSocket.accept();
                        executorService.submit(() -> {
                            try {
                                clients.add(socket);
                                while (socket != null && !socket.isClosed()) {
                                    try {
                                        clients.forEach(client -> {
                                            try {
                                                Transport.send("clear", client);
                                                for (Task task : Interrogation.returnTable()) {
                                                    Transport.send(task.toString(), client);
                                                }
                                            } catch (IOException e) {
                                            }
                                        });
                                        String message = Transport.receive(socket);
                                        clients.forEach(client -> {
                                            try {


                                                int i = Integer.parseInt(message.substring(0, 1));
                                                switch (i) {
                                                    case 1:
                                                        Task task = Task.convertToTask(message.substring(1));
                                                        Interrogation.insertRow(task);
                                                        break;
                                                    case 2:
                                                        Task task1 = Task.convertToTask(message.substring(1));
                                                        Interrogation.updateRow(task1);
                                                        break;
                                                    case 3:
                                                        Interrogation.deleteRow(message.substring(1));
                                                        break;
                                                    default:
                                                        System.out.println("default");
                                                        break;
                                                }
                                                for (Task task : Interrogation.returnTable()) {
                                                    Transport.send(task.toString(), client);
                                                }

                                            } catch (Exception e) {
//                                                System.err.println("Error 1: " + e.getMessage());
                                            }
                                        });
                                    } catch (Exception e) {
//                                        System.err.println("Error 2: " + e.getMessage());
                                    }
                                }

                            } catch (Exception e) {
                                System.err.println("Error 3: " + e.getMessage());
                            } finally {
                                clients.remove(socket);
                            }
                        });
                    } catch (Exception e) {
                        System.err.println("Error 4: " + e.getMessage());
                    }
                }
            });


        } else {
            System.out.println("Could not connect to the database.");
        }
    }

    public void stop() throws IOException {

        Database.closeConnection();

        if (executorService != null) {
            executorService.shutdown();
            executorService = null;
        }
        if (serverSocket != null) {
            serverSocket.close();
            serverSocket = null;
        }
    }

}