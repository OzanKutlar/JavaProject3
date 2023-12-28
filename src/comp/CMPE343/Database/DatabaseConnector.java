package comp.CMPE343.Database;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class DatabaseConnector {
    public static void sendTestSQL() {
        final String DATABASE_URL = "jdbc:mysql://localhost:3306/books?useTimezone=true&serverTimezone=UTC";
        final String SELECT_QUERY = "SELECT authorID, firstName, lastName FROM authors";
        // use try-with-resources to connect to and query the database
        try (
                Connection connection = DriverManager.getConnection(DATABASE_URL, "worker", "1234");
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(SELECT_QUERY);
        ) {
            // get ResultSet's meta data
            ResultSetMetaData metaData = resultSet.getMetaData();
            int numberOfColumns = metaData.getColumnCount();
            System.out.printf("Authors Table of Books Database:%n%n");


            while(resultSet.next()){
                System.out.println("AuID : " + resultSet.getInt("authorID") + " firstName : " + resultSet.getString("firstName") + "\n");
            }
            connection.close();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }
}
