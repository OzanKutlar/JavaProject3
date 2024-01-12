package comp.CMPE343;

import comp.CMPE343.Database.DatabaseConnector;
import comp.CMPE343.UserInterface.SceneManager;

import java.sql.ResultSet;
import java.util.UUID;

import static comp.CMPE343.Logger.*;

public class Main {


    public static void main(String[] args){
        log("Thread Started.");
        log("Checking Database");
        boolean dbState = DatabaseConnector.checkDatabaseExists();
        log("Database State : %d", dbState);
        if(dbState){
            DatabaseConnector.startDatabaseConnector();
        }
        log("Testing DB from main thread");
        while (DatabaseConnector.instance == null){
            try {
                Thread.sleep(50);
                log("Waiting");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        UUID savedID = DatabaseConnector.instance.sendRequest("SELECT id, stock, markup FROM stock");

        ResultSet resultSet = null;
        while (resultSet == null){
            try {
                resultSet = DatabaseConnector.instance.checkResult(savedID);
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        log("Results are : ");
        try{
            while (resultSet.next()){
                log("Product ID : %d\nStock Left : %d\nRemove Discount At : %d", resultSet.getInt("id"), resultSet.getDouble("stock"), resultSet.getDouble("markup"));
            }
            resultSet.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }


        SceneManager.startUI(args);
        log("Thread Ended.");

    }
}
