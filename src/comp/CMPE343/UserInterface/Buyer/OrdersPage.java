package comp.CMPE343.UserInterface.Buyer;

import comp.CMPE343.Database.DatabaseConnector;
import comp.CMPE343.Logger;
import comp.CMPE343.Main;
import comp.CMPE343.UserInterface.Driver.Order;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.UUID;

public class OrdersPage {

    public Scene scene;

    private GridPane grid;

    ProductPage parent;

    public ArrayList<Order> orders = new ArrayList<>();

    String adress;
    String Username;

    double total;

    public OrdersPage(ProductPage parent){
        this.parent = parent;
        grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
//        grid.setPadding(new Insets(25,25,25,25));
        grid.setAlignment(Pos.CENTER);

        UUID id = DatabaseConnector.instance.sendRequest("SELECT * FROM user where id="+ Main.userID + ";");

        ResultSet resultSet = null;
        for (int i = 0; i < 20; i++) {
            try {
                resultSet = DatabaseConnector.instance.checkResult(id);
                if(resultSet != null){
                    break;
                }
                Thread.sleep(250);
            } catch (InterruptedException e) {
                Logger.log("Interrupted in Cart");
            }
        }

        try{
            resultSet.next();
            adress = resultSet.getString("address");
            Username = resultSet.getString("username");
        }
        catch(Exception e){
            e.printStackTrace();
        }

        id = DatabaseConnector.instance.sendRequest("SELECT * FROM orders where address="+ adress + ";");

        resultSet = null;
        for (int i = 0; i < 20; i++) {
            try {
                resultSet = DatabaseConnector.instance.checkResult(id);
                if(resultSet != null){
                    break;
                }
                Thread.sleep(250);
            } catch (InterruptedException e) {
                Logger.log("Interrupted in Orders");
            }
        }

        try{
            while (resultSet.next()){
                Order o = new Order();
                o.adres = resultSet.getString("address");
                o.id = resultSet.getInt("id");
                o.total = resultSet.getDouble("total");
                o.taken = resultSet.getBoolean("taken");
                orders.add(o);
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }


        ColumnConstraints Orders = new ColumnConstraints();
        Orders.setHgrow(Priority.ALWAYS);
        ColumnConstraints Sepet = new ColumnConstraints();
        Sepet.setHgrow(Priority.NEVER);

        grid.getColumnConstraints().addAll(Orders, Sepet);

        // User Data
        Button logOutButton = new Button("Back to Main Page");
        logOutButton.setStyle("-fx-alignment: center; -fx-background-color: #3498db; -fx-font-size: 14px; -fx-font-weight: bold;");
        GridPane.setConstraints(logOutButton, 1, 0);


        logOutButton.setOnAction(e ->{
            ((Stage) this.scene.getWindow()).setScene(parent.scene);
        });

        grid.getChildren().addAll(logOutButton);


        // Products Columnu
        Label productsText = new Label("Confirm your products!");
        productsText.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-alignment: center;");
        grid.setConstraints(productsText, 0, 0);
        grid.getChildren().add(productsText);

        try{
            int counter = 1;
            for(Order p : orders) {
                OrderPane pane = new OrderPane(p);
                GridPane.setConstraints(pane, 0, counter++);
                grid.getChildren().add(pane);
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }




        scene = new Scene(grid, 900, 540);
    }
}
