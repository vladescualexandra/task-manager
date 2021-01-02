package com.company.database;

import java.sql.*;

public class Database {

    private static final String dbURL = "jdbc:derby:taskManager;create=true";
    private static final String dbUser = "admin";
    private static final String dbPassword = "admin";
    private static final String dbName = "taskManager";

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
    public static final String COLUMN_TASKS_PROJECT = "project_name";
    public static final String COLUMN_TASKS_ASSIGNED_TO = "assigned_to";
    public static final String COLUMN_TASKS_CREATED = "created";
    public static final String COLUMN_TASKS_CLOSED = "closed";
    public static final String COLUMN_TASKS_CREATED_BY = "created_by";



    private static final String CREATE_TABLE_TASKS = "CREATE TABLE " +
                TABLE_TASKS + " (" + COLUMN_TASKS_ID + " varchar(10) primary key, "
                                + COLUMN_TASKS_SUMMARY + " varchar(30), "
                                + COLUMN_TASKS_DESCRIPTION + " varchar(500), "
                                + COLUMN_TASKS_SEVERITY + " varchar(10), "
                                + COLUMN_TASKS_STATUS + " varchar(10), "
                                + COLUMN_TASKS_PROJECT + " varchar(15), "
                                + COLUMN_TASKS_ASSIGNED_TO + " varchar(20), "
                                + COLUMN_TASKS_CREATED + " date, "
                                + COLUMN_TASKS_CLOSED + " date, "
                                + COLUMN_TASKS_CREATED_BY + " varchar(20))";

    public static void main(String[] args) {


        if (openConnection()) {
            createTables();
            closeConnection();
        } else {
            System.err.println("Could not connect to the database.");
        }

    }


    public static boolean openConnection() {
        try {
            connection = DriverManager.getConnection(dbURL, dbUser, dbPassword);
            return true;
        } catch (SQLException e) {
            System.err.println("openConnection: " + e.getMessage());
            return false;
        }
   }

    public static void closeConnection() {
        try {
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

            statement.execute(CREATE_TABLE_TASKS);

        } catch (SQLException e) {
            System.err.println("createTables: " + e.getMessage());
        }
   }
}