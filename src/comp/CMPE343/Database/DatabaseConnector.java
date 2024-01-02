package comp.CMPE343.Database;

import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetFactory;
import javax.sql.rowset.RowSetProvider;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.LinkedBlockingDeque;

import static comp.CMPE343.Logger.*;
public class DatabaseConnector {


    static final String DATABASE_URL = "jdbc:mysql://localhost:3306/grocery?useTimezone=true&serverTimezone=UTC";
    public static final String TEST_QUERY = "SELECT productID, stockLeft, discountRemover FROM stock";

    private ArrayList<DatabaseRequest> requests = new ArrayList<>();
    private ArrayList<DatabaseRequest> results = new ArrayList<>();

    public static Thread DBThread = null;

    public static DatabaseConnector instance;

    public UUID sendRequest(String requestString){
        DatabaseRequest request = new DatabaseRequest(requestString);
        synchronized (requests){
            requests.add(request);
            requests.notify();
        }
        return request.requestID;
    }

    public ResultSet checkResult(UUID id){
        ResultSet returnValue = null;
        debugLog("Database", "Checking Results for UUID %d", id);
        synchronized (results){
            for(DatabaseRequest request : results){
                if(Objects.equals(request.requestID.toString(), id.toString())){
                    returnValue = request.resultSet;
                }
            }
            results.notify();
        }
        return returnValue;
    }



    public static void startDatabaseConnector(){
        if(DBThread == null){
            try{
                DBThread = new Thread(() -> {
                    log("Thread Begun.");
                    try {
                        instance = new DatabaseConnector();
                        instance.startQueue();
                    } catch (SQLException e) {
                        log("Fatal Error : DB or table doesnt exist.");
                        e.printStackTrace();
                    }
                    log("Thread Ended.");
                });
                DBThread.setName("DataBase");
                log("Starting DB Thread");
                DBThread.start();
            }catch (Exception e){
                debugLog("Scene:Exception", "An exception occured, with the message : %d\nWith the stack trace %d", e.getMessage(), e.getStackTrace());
            }
        }
    }

    private Connection connection;



    private DatabaseConnector() throws SQLException {
        // use try-with-resources to connect to and query the database
        connection = DriverManager.getConnection(DATABASE_URL, "dbAgent", "1234");
    }

    public void startQueue(){
        log("Started Queue");
        while (true){
            synchronized (requests){
                if(!requests.isEmpty()){
                    DatabaseRequest request = requests.remove(0);
                    log("Found a request : %d", request.request);
                    try {
                        Statement statement = connection.createStatement();
                        CachedRowSet returnValue = RowSetProvider.newFactory().createCachedRowSet();
                        ResultSet resultSet = statement.executeQuery(request.request);
                        returnValue.populate(resultSet);
                        request.resultSet = returnValue;
                        synchronized (results){
                            results.add(request);
                            results.notify();
                        }
                        resultSet.close();
                        statement.close();
                    } catch (SQLException e) {
                        debugLog("Database", "Exception occurred in queue");
                        results.add(request);
                    }
                }
                requests.notify();
            }
        }
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
