package com.company.database;

import com.company.database.data.Severity;
import com.company.database.data.Status;
import com.company.database.data.Task;
import com.company.database.data.Username;

import java.sql.*;
import java.util.ArrayList;

public class Interrogation extends Database {

    public static void main(String[] args) {

        if (openConnection()) {

        } else {
            System.err.println("Could not connect to the database.");
        }
    }


    /*
     *               PRINT TABLE
     */
    public static String printTable() {
        StringBuilder result = new StringBuilder();

        try {
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_UPDATABLE);

            resultSet = statement.executeQuery("SELECT * FROM " + Database.TABLE_TASKS);


            // Pozitionare la ultima linie din ResultSet
            resultSet.last();

            // Tiparire header
            resultSetMetaData = resultSet.getMetaData();
            int noColumns = resultSetMetaData.getColumnCount();
            for (int i = 1; i <= noColumns; i++) {
                result.append(resultSetMetaData.getColumnName(i)).append("\t\t");
            }

            resultSet.beforeFirst();

            while (resultSet.next()) {

                for (int i = 1; i <= noColumns; i++) {
                    result.append(resultSet.getString(i)).append("\t\n");
                }
            }

        } catch (SQLException e) {
            System.err.println("printTables: " + e.getMessage());
        }
        return result.toString();
    }

    /*
    *   RETURN ARRAY LIST OF TASKS
    */

    /*
     *       INSERT ROWS
     */

    public static boolean insertRow(Task task) {

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO " + TABLE_TASKS + "("
                            + COLUMN_TASKS_ID + ","
                            + COLUMN_TASKS_SUMMARY + ", "
                            + COLUMN_TASKS_DESCRIPTION + ", "
                            + COLUMN_TASKS_SEVERITY + ", "
                            + COLUMN_TASKS_STATUS + ") "
                            + " VALUES (?,?,?,?,?) ");

            preparedStatement.setString(1, task.getId());
            preparedStatement.setString(2, task.getSummary());
            preparedStatement.setString(3, task.getDescription());
            preparedStatement.setString(4, task.getSeverity().toString());
            preparedStatement.setString(5, task.getStatus().toString());

            preparedStatement.execute();

            return true;
        } catch (SQLException e) {
            System.err.println("insertRow: " + e.getMessage());
            return false;
        }
    }


    /*
    *   PRINT FILTERED TABLE
    *
    *  Columns
    *         1: id
    *         2: summary
    *         3: description
    *         4: severity
    *         5: status
    *
    */

    private static String getQuery(int[] columns, String[] values) {
        StringBuilder query = new StringBuilder("SELECT * FROM " + TABLE_TASKS);

        if (columns.length > 0) {

            query.append(" WHERE ");

            for (int i = 0; i < columns.length; i++) {

                switch (columns[i]) {
                    case 1:
                        query.append(COLUMN_TASKS_ID + " = '").append(values[i]).append("'");
                        break;
                    case 2:
                        query.append(COLUMN_TASKS_SUMMARY + " LIKE '%").append(values[i]).append("%'");
                        break;
                    case 3:
                        query.append(COLUMN_TASKS_DESCRIPTION + " LIKE '%").append(values[i]).append("%'");
                        break;
                    case 4:
                        query.append(COLUMN_TASKS_SEVERITY + " = '").append(values[i]).append("'");
                        break;
                    case 5:
                        query.append(COLUMN_TASKS_STATUS + " = '").append(values[i]).append("'");
                        break;
                }

                if (i < columns.length - 1) {
                    query.append(" AND ");
                }

            }


        }


        System.out.println(query.toString());

        return query.toString();
    }


}
