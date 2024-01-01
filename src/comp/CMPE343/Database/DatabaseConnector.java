package comp.CMPE343.Database;

import java.sql.*;
import java.util.HashMap;
import static comp.CMPE343.Logger.*;
public class DatabaseConnector {


    static final String DATABASE_URL = "jdbc:mysql://localhost:3306/grocery?useTimezone=true&serverTimezone=UTC";
    static final String TEST_QUERY = "SELECT productID, stockLeft, discountRemover FROM stock";

    public HashMap<String, Runnable> requests = null;

    public static Thread DBThread = null;

    public static DatabaseConnector instance;

    public static void startDatabaseConnector(){
        if(DBThread == null){
            try{
                DBThread = new Thread(() -> {
                    log("UI Thread Begun.");
                    try {
                        instance = new DatabaseConnector();
                    } catch (SQLException e) {
                        log("Fatal Error : DB Connection unsuccessfull, this should not be possible. Something has gone wrong.");
                        e.printStackTrace();
                    }
                    log("UI Thread Ended.");
                });
                DBThread.setName("DataBase");
                log("Starting UI Thread");
                DBThread.start();
            }catch (Exception e){
                debugLog("Scene:Exception", "An exception occured, with the message : %d\nWith the stack trace %d", e.getMessage(), e.getStackTrace());
            }
        }
    }



    private DatabaseConnector() throws SQLException {
        // use try-with-resources to connect to and query the database
        Connection connection = DriverManager.getConnection(DATABASE_URL, "dbAgent", "1234");
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(TEST_QUERY);

        // get ResultSet's meta data
        ResultSetMetaData metaData = resultSet.getMetaData();
        int numberOfColumns = metaData.getColumnCount();
        debugLog("Database:Startup", "Current Stock Info : ");

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
             * doesn't exist.
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
