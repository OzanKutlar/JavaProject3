package comp.CMPE343.Database;

import comp.CMPE343.UserInterface.Buyer.Carrier;
import comp.CMPE343.UserInterface.Buyer.Product;

import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetProvider;
import java.sql.*;
import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

import static comp.CMPE343.Logger.debugLog;
import static comp.CMPE343.Logger.log;
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

    public Connection connection;



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
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
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


    public void updateProduct(Product selectedProduct, Product updatedProduct) {
        try {
            String query = "UPDATE products SET name = ?, price = ?, threshold = ? WHERE id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, updatedProduct.getName());
                preparedStatement.setDouble(2, updatedProduct.getPrice());
                preparedStatement.setDouble(3, updatedProduct.getThreshold());
                preparedStatement.setInt(4, selectedProduct.getId()); // Assuming id is the primary key

                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            debugLog("Database", "Exception occurred while updating product");
            e.printStackTrace();
        }
    }
    public void employCarrier(Carrier carrier) {
        try {
            String query = "INSERT INTO carriers (name, address) VALUES (?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, carrier.getName());
                preparedStatement.setString(2, carrier.getAddress());

                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            debugLog("Database", "Exception occurred while employing carrier");
            e.printStackTrace();
        }
    }
    public void fireCarrier(Carrier carrier) {
        try {
            String query = "DELETE FROM carriers WHERE id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, carrier.getId()); // Use the instance method on the provided Carrier

                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            debugLog("Database", "Exception occurred while firing carrier");
            e.printStackTrace();
        }
    }
}
