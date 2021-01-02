package com.company;

import com.company.common.Transport;
import com.company.database.Database;
import com.company.database.Interrogation;
import com.company.database.data.Severity;
import com.company.database.data.Status;
import com.company.database.data.Task;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Date;
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

                                    String message = Transport.receive(socket);
                                    System.out.println(message);

                                    System.out.println(clients.size());
                                    clients.forEach(client -> {
                                        try {
                                            if (Database.openConnection()) {
                                                Task task = new Task();
                                                task.setId(message);
                                                task.setSummary("Project");
                                                task.setDescription("Issues and all u kno");
                                                task.setSeverity(Severity.IMPORTANT);
                                                task.setStatus(Status.ACTIVE);

                                                Interrogation.insertRow(task);
                                                Transport.send(Interrogation.printTable(), client);
                                                Database.closeConnection();
                                            } else {
                                                Transport.send("Could not connect to the DB.", client);
                                            }
//                                            Transport.send(message, client);
                                        } catch (Exception e) {
//                                            System.err.println("Error 1: " + e.getMessage());
                                        }
                                    });

                                } catch (Exception e) {
//                                    System.err.println("Error 2: " + e.getMessage());
                                }
                            }
                        } catch (Exception e) {
//                            System.err.println("Error 3: " + e.getMessage());
                        } finally {
                            clients.remove(socket);
                        }
                    });
                } catch (Exception e) {
//                    System.err.println("Error 4: " + e.getMessage());
                }
            }
        });
    }

    public void stop() throws IOException {
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