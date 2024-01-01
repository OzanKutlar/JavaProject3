package comp.CMPE343.Database;

import java.sql.*;
import java.util.HashMap;
import static comp.CMPE343.Logger.*;
public class DatabaseConnector {


    static final String DATABASE_URL = "jdbc:mysql://localhost:3306/grocery?useTimezone=true&serverTimezone=UTC";
    static final String START_QUERY = "SELECT authorID, firstName, lastName FROM authors";

    public HashMap<String, Runnable> requests = null;


    public DatabaseConnector() throws SQLException {
        // use try-with-resources to connect to and query the database
        Connection connection = DriverManager.getConnection(DATABASE_URL, "dbAgent", "1234");
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(START_QUERY);

        // get ResultSet's meta data
        ResultSetMetaData metaData = resultSet.getMetaData();
        int numberOfColumns = metaData.getColumnCount();
        System.out.printf("Authors Table of Books Database:%n%n");

        while(resultSet.next()){
        System.out.println("AuID : " + resultSet.getInt("authorID") + " firstName : " + resultSet.getString("firstName") + "\n");
        }
        resultSet.close();
        statement.close();
        connection.close();
    }

    public static boolean checkDatabaseExists(){
        try{
            /**
             * Checks whether a database exists or not. The connection gives out a SQLException when the database in the url
             * doesnt exist.
             */
            Connection connection = DriverManager.getConnection(DATABASE_URL, "dbAgent", "1234");
            connection.close();
        }
        catch(SQLException e){
            return false;
        }
        return true;
    }



}
