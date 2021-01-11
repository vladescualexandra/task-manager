package com.company.database;

import java.sql.*;

public class Database {

    private static final String dbURL = "jdbc:derby:task-manager;create=true";
    private static final String dbUser = "admin";
    private static final String dbPassword = "admin";
    private static final String dbName = "task-manager";

    public static Connection connection;
    public static Statement statement;
    public static PreparedStatement preparedStatement;
    public static ResultSet resultSet;
    public static ResultSetMetaData resultSetMetaData;

    public static final String TABLE_TASKS = "tasks";

    public static final String COLUMN_TASKS_ID = "id";
    public static final String COLUMN_TASKS_SUMMARY = "summary";
    public static final String COLUMN_TASKS_DESCRIPTION = "description";
    public static final String COLUMN_TASKS_SEVERITY = "severity";
    public static final String COLUMN_TASKS_STATUS = "status";




    private static final String CREATE_TABLE_TASKS = "CREATE TABLE " +
                TABLE_TASKS + " (" + COLUMN_TASKS_ID + " int primary key NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), "
                                + COLUMN_TASKS_SUMMARY + " varchar(50), "
                                + COLUMN_TASKS_DESCRIPTION + " varchar(500), "
                                + COLUMN_TASKS_SEVERITY + " varchar(50), "
                                + COLUMN_TASKS_STATUS + " varchar(50))";



    public static final String TABLE_LOGS = "logs";

    public static final String COLUMN_LOGS_ID = "id";
    public static final String COLUMN_LOGS_TASK_ID = "task_id";
    public static final String COLUMN_LOGS_TASK_OPERATION = "operation";



    private static final String CREATE_TABLE_LOGS = "CREATE TABLE " + TABLE_LOGS
                            + " (" + COLUMN_TASKS_ID + " int primary key NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),"
                            + COLUMN_LOGS_TASK_ID + " int, "
                            + COLUMN_LOGS_TASK_OPERATION + " varchar(20))";


//    public static void main(String[] args)  {
//        if (openConnection()) {
//            createTables();
//            closeConnection();
//        }
//    }


    private static final String JDBC_DRIVER_NAME = "org.apache.derby.jdbc.ClientDriver";
    public static boolean openConnection() {
        try {
            System.out.println("Open connection to DB.");
//            Class.forName(JDBC_DRIVER_NAME);
            connection = DriverManager.getConnection(dbURL, dbUser, dbPassword);
            return true;
        } catch (SQLException  e) {
            System.err.println("openConnection: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public static void closeConnection() {
        try {
            System.out.println("Close connection to DB.");

            if (statement != null)
                statement.close();

            if (preparedStatement != null)
                preparedStatement.close();

            if (resultSet != null)
                resultSet.close();

            if (connection != null)
                connection.close();

        } catch (SQLException e) {
            System.err.println("closeConnection: " + e.getMessage());
            e.printStackTrace();
        }
   }

   private static void createTables() {
        try {
            statement = connection.createStatement();
//            statement.execute("DROP TABLE " + TABLE_TASKS);
//            statement.execute("DROP TABLE " + TABLE_LOGS);
            statement.execute(CREATE_TABLE_TASKS);
            statement.execute(CREATE_TABLE_LOGS);

        } catch (SQLException e) {
            System.err.println("createTables: " + e.getMessage());
        }
   }
}
