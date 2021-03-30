package com.company;

import com.company.common.Settings;
import com.company.database.Database;
import com.company.database.Interrogation;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        try (Server server = new Server()) {
            server.start(8080);
            System.out.println("Server is running, type 'exit' to stop it.");
            System.out.println("Type 'logs' to print the logs.");
            try (Scanner scanner = new Scanner(System.in)) {
                while (true) {
                    String command = scanner.nextLine();
                    if (command == null || "exit".equalsIgnoreCase(command)) {
                        break;
                    } else if (command.equalsIgnoreCase("logs")) {
                        System.out.println(Interrogation.printTable(Database.TABLE_LOGS));
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            System.exit(0);
        }
    }
}
