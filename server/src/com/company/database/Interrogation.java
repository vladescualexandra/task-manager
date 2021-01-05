package com.company.database;

import com.company.common.data.Log;
import com.company.common.data.Task;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Interrogation extends Database {

    /*  Columns
     *         1: id
     *         2: summary
     *         3: description
     *         4: severity
     *         5: status
     */


    public static List<Task> returnTable() {
        List<Task> result = new ArrayList<>();
        try {
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_UPDATABLE);

            resultSet = statement.executeQuery("SELECT * FROM " + Database.TABLE_TASKS);

            resultSet.beforeFirst();

            while (resultSet.next()) {

                Task task = new Task();
                task.setId(resultSet.getInt(1));
                task.setSummary(resultSet.getString(2));
                task.setDescription(resultSet.getString(3));
                task.setSeverity(resultSet.getString(4));
                task.setStatus(resultSet.getString(5));

                result.add(task);
            }

        } catch (SQLException e) {
            System.err.println("returnTable: " + e.getMessage());
        }

        return result;
    }

    public static String printTable(String table) {
        StringBuilder result = new StringBuilder();
        try {
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_UPDATABLE);

            resultSet = statement.executeQuery("SELECT * FROM " + table);
            resultSet.last();

            // Print header
            resultSetMetaData = resultSet.getMetaData();
            int noColumns = resultSetMetaData.getColumnCount();
            for (int i = 1; i <= noColumns; i++) {
                result.append(resultSetMetaData.getColumnName(i)).append("\t\t");
            }
            result.append("\n");

            resultSet.beforeFirst();
            while (resultSet.next()) {
                for (int i = 1; i <= noColumns; i++) {
                    result.append(resultSet.getString(i)).append("\t\t\t");
                }
                result.append("\n");
            }

        } catch (SQLException e) {
            System.err.println("printTables: " + e.getMessage());
        }
        return result.toString();
    }

    public static Task insertRow(Task task) {

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO " + TABLE_TASKS + "("
                            + COLUMN_TASKS_SUMMARY + ", "
                            + COLUMN_TASKS_DESCRIPTION + ", "
                            + COLUMN_TASKS_SEVERITY + ", "
                            + COLUMN_TASKS_STATUS + ") "
                            + " VALUES (?,?,?,?) ");

            preparedStatement.setString(1, task.getSummary());
            preparedStatement.setString(2, task.getDescription());
            preparedStatement.setString(3, task.getSeverity().toString());
            preparedStatement.setString(4, task.getStatus().toString());

            preparedStatement.execute();


            return returnTable().get(returnTable().size() - 1);
        } catch (SQLException e) {
            System.err.println("insertRow: " + e.getMessage());
            return null;
        }
    }

    public static Task updateRow(Task task) {

        try {
            String query = "UPDATE " + TABLE_TASKS + " SET "
                    + COLUMN_TASKS_SUMMARY + " = ? , "
                    + COLUMN_TASKS_DESCRIPTION + " = ? , "
                    + COLUMN_TASKS_SEVERITY + " = ? , "
                    + COLUMN_TASKS_STATUS + " = ? "
                    + "WHERE " + COLUMN_TASKS_ID
                    + " = " + task.getId();

            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setString(1, task.getSummary());
            preparedStatement.setString(2, task.getDescription());
            preparedStatement.setString(3, task.getSeverity().toString());
            preparedStatement.setString(4, task.getStatus().toString());

            preparedStatement.execute();
            preparedStatement.close();

            return task;
        } catch (SQLException e) {
            System.err.println("updateRow: " + e.getMessage());
            return null;
        }
    }

    public static int deleteRow(int id) {
        try {
            statement = connection.createStatement();
            statement.execute("DELETE FROM " + TABLE_TASKS + " WHERE " + COLUMN_TASKS_ID + " = " + id);
            return id;
        } catch (SQLException throwable) {
            throwable.printStackTrace();
            return -1;
        }
    }

    public static boolean log(Log log) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO " + TABLE_LOGS + "("
                            + COLUMN_LOGS_TASK_ID + ", "
                            + COLUMN_LOGS_TASK_OPERATION + ") "
                            + " VALUES (?,?) ");

            preparedStatement.setInt(1, log.getTask_id());
            preparedStatement.setString(2, log.getOperation().toString());

            preparedStatement.execute();

            return true;
        } catch (SQLException e) {
            System.err.println("insertRow: " + e.getMessage());
            return false;
        }
    }
}
