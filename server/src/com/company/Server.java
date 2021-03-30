package com.company;

import com.company.common.Transport;
import com.company.common.data.Log;
import com.company.common.data.Operation;
import com.company.database.Database;
import com.company.database.Interrogation;
import com.company.common.data.Task;

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
                                                selectEvent(client);
                                            } catch (IOException e) {
                                                System.err.println("Error 0: " + e.getMessage());
                                            }
                                        });

                                        String message = Transport.receive(socket);
                                        handleMessageReceived(clients, socket, message);
                                        sendTasksToClients(clients);
                                    } catch (Exception e) {
                                        break;
                                    }
                                }
                            } catch (Exception e) {
                                System.err.println("Error 1: " + e.getMessage());
                            } finally {
                                clients.remove(socket);
                            }
                        });
                    } catch (Exception e) {
                    }
                }
            });
        } else {
            System.out.println("Could not connect to the database.");
        }
    }

    private void sendTasksToClients(List<Socket> clients) {
        clients.forEach(client -> {
            try {
                selectEvent(client);
            } catch (Exception e) {
                System.err.println("Error 3: " + e.getMessage());
            }
        });
    }

    private void handleMessageReceived(List<Socket> clients, Socket socket, String message) {
        Log log = new Log();
        int i = Integer.parseInt(message.substring(0, 1));
        switch (i) {
            case 0 -> clients.remove(socket);
            case 1 -> insertEvent(message, log);
            case 2 -> updateEvent(message, log);
            case 3 -> deleteEvent(message, log);
            default -> System.out.println("default");
        }
        Interrogation.log(log);
    }

    private void selectEvent(Socket client) throws IOException {
        for (Task t : Interrogation.returnTable()) {
            Transport.send(t.toString(), client);
        }
    }

    private void insertEvent(String message, Log log) {
        Task task = Task.convertToTask(message.substring(1));
        task = Interrogation.insertRow(task);
        assert task != null;
        log.setTask_id(task.getId());
        log.setOperation(Operation.INSERT);
    }

    private void updateEvent(String message, Log log) {
        Task task = Task.convertToTask(message.substring(1));
        task = Interrogation.updateRow(task);
        assert task != null;
        log.setTask_id(task.getId());
        log.setOperation(Operation.UPDATE);
    }

    private void deleteEvent(String message, Log log) {
        int task_id = Interrogation.deleteRow(Integer.parseInt(message.substring(1)));
        log.setTask_id(task_id);
        log.setOperation(Operation.DELETE);
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