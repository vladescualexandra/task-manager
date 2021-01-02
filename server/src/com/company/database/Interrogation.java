package com.company.database;

import com.company.database.Database;
import data.Severity;
import data.Status;
import data.Task;
import data.Username;

import java.sql.*;
import java.util.ArrayList;

public class Interrogation extends Database {

    public static void main(String[] args) {

        if (openConnection()) {

            int[] columns = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
            String[] values = {"TW100", "est", "des",
                    Severity.TRIVIAL.toString(), Status.ACTIVE.toString(),
                    "ehn", Username.ADMIN.toString(), "2020-11-16",
                    "2020-11-16", Username.ADMIN.toString()};

            String query = getQuery(columns, values);
            printTable(query);


        } else {
            System.err.println("Could not connect to the database.");
        }
    }


    /*
     *               PRINT TABLE
     */
    public static void printTable(String query) {
        try {
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_UPDATABLE);

            resultSet = statement.executeQuery(query);


            // Pozitionare la ultima linie din ResultSet
            resultSet.last();

            // Tiparire header
            resultSetMetaData = resultSet.getMetaData();
            int noColumns = resultSetMetaData.getColumnCount();
            for (int i = 1; i <= noColumns; i++) {
                System.out.print(resultSetMetaData.getColumnName(i) + "\t\t");
            }
            System.out.println();

            resultSet.beforeFirst();

            while (resultSet.next()) {

                for (int i = 1; i <= noColumns; i++) {
                    System.out.print(resultSet.getString(i) + "\t");
                }
                System.out.println();
            }

            System.out.println();
        } catch (SQLException e) {
            System.err.println("printTables: " + e.getMessage());
        }
    }

    /*
    *   RETURN ARRAY LIST OF TASKS
    */

    /*
     *       INSERT ROWS
     */

    private static boolean insertRow(Task task) {

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO " + TABLE_TASKS + "("
                            + COLUMN_TASKS_ID + ","
                            + COLUMN_TASKS_SUMMARY + ", "
                            + COLUMN_TASKS_DESCRIPTION + ", "
                            + COLUMN_TASKS_SEVERITY + ", "
                            + COLUMN_TASKS_STATUS + ", "
                            + COLUMN_TASKS_PROJECT + ", "
                            + COLUMN_TASKS_ASSIGNED_TO + ", "
                            + COLUMN_TASKS_CREATED + ", "
                            + COLUMN_TASKS_CLOSED + ", "
                            + COLUMN_TASKS_CREATED_BY + ") "
                            + " VALUES (?,?,?,?,?,?,?,?,?,?) ");

            preparedStatement.setString(1, task.getId());
            preparedStatement.setString(2, task.getSummary());
            preparedStatement.setString(3, task.getDescription());
            preparedStatement.setString(4, task.getSeverity().toString());
            preparedStatement.setString(5, task.getStatus().toString());
            preparedStatement.setString(6, task.getProject());
            preparedStatement.setString(7, task.getAssigned_to().toString());
            preparedStatement.setString(8, task.getCreated().toString());
            preparedStatement.setString(9, task.getClosed().toString());
            preparedStatement.setString(10, task.getCreated_by().toString());

            System.out.println(preparedStatement.toString());
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
            // 1: id
            // 2: summary
            // 3: description
            // 4: severity
            // 5: status
            // 6: project
            // 7: assigned to
            // 8: created
            // 9: closed
            // 10: created by
    *
    */

    private static String getQuery(int[] columns, String[] values) {
        StringBuilder query = new StringBuilder("SELECT * FROM " + TABLE_TASKS);

        if (columns.length > 0) {

            query.append(" WHERE ");

            for (int i = 0; i < columns.length; i++) {

                switch (columns[i]) {
                    case 1:
                        query.append(COLUMN_TASKS_ID + " = '" + values[i] + "'");
                        break;
                    case 2:
                        query.append(COLUMN_TASKS_SUMMARY + " LIKE '%" + values[i] + "%'");
                        break;
                    case 3:
                        query.append(COLUMN_TASKS_DESCRIPTION + " LIKE '%" + values[i] + "%'");
                        break;
                    case 4:
                        query.append(COLUMN_TASKS_SEVERITY + " = '" + values[i] + "'");
                        break;
                    case 5:
                        query.append(COLUMN_TASKS_STATUS + " = '" + values[i] + "'");
                        break;
                    case 6:
                        query.append(COLUMN_TASKS_PROJECT + " LIKE '%" + values[i] + "%'");
                        break;
                    case 7:
                        query.append(COLUMN_TASKS_ASSIGNED_TO + " = '" + values[i] + "'");
                        break;
                    case 8:
                        query.append(COLUMN_TASKS_CREATED + " = '" + values[i] + "'");
                        break;
                    case 9:
                        query.append(COLUMN_TASKS_CLOSED + " = '" + values[i] + "'");
                        break;
                    case 10:
                        query.append(COLUMN_TASKS_CREATED_BY + " = '" + values[i] + "'");
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
