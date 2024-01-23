package comp.CMPE343.UserInterface.Buyer;

import comp.CMPE343.Database.DatabaseConnector;
import comp.CMPE343.Logger;
import comp.CMPE343.Main;
import controller.LoginFormController;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.UUID;

public class ProductPage {


    public Scene scene;

    private GridPane grid;

    public String Username;
    public String adress;

    public ArrayList<Product> sepet = new ArrayList<>();

    public void getUsername(){
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
    }

    public ProductPage(){
        grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
//        grid.setPadding(new Insets(25,25,25,25));
        grid.setAlignment(Pos.CENTER);
        getUsername();


        ColumnConstraints UserData = new ColumnConstraints();
        UserData.setHgrow(Priority.NEVER);
        ColumnConstraints Products = new ColumnConstraints();
        Products.setHgrow(Priority.ALWAYS);
        ColumnConstraints Sepet = new ColumnConstraints();
        Sepet.setHgrow(Priority.NEVER);

        grid.getColumnConstraints().addAll(UserData, Products, Sepet);

        Label welcomeText = new Label("Welcome " + Username);
        GridPane.setConstraints(welcomeText, 0, 0);
        welcomeText.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-alignment: center;");
        grid.getChildren().add(welcomeText);


        Button logOutButton = new Button("Log Out");
        GridPane.setConstraints(logOutButton, 0, 1);
        logOutButton.setStyle("-fx-alignment: center; -fx-background-color: #3498db; -fx-font-size: 14px; -fx-font-weight: bold;");

        Button orderButton = new Button("Check Orders");
        GridPane.setConstraints(orderButton, 0, 2);
        orderButton.setStyle("-fx-alignment: center; -fx-background-color: #3498db; -fx-font-size: 14px; -fx-font-weight: bold;");

        Button finishPurchaseButton = new Button("Finish Purchase");
        GridPane.setConstraints(finishPurchaseButton, 0, 4);
        finishPurchaseButton.setStyle("-fx-alignment: center; -fx-background-color: #3498db; -fx-font-size: 14px; -fx-font-weight: bold;");


        orderButton.setOnAction(e ->{
            OrdersPage cart = new OrdersPage(this);
            ((Stage) this.scene.getWindow()).setScene(cart.scene);
        });

        logOutButton.setOnAction(e ->{
            ((Stage) this.scene.getWindow()).setScene(LoginFormController.getScene());
        });

        finishPurchaseButton.setOnAction(e ->{
            Cart cart = new Cart(sepet,this);
            ((Stage) this.scene.getWindow()).setScene(cart.scene);
        });

        grid.getChildren().addAll(logOutButton, finishPurchaseButton, orderButton);


        // Products Columnu
        Label productsText = new Label("          Select a product!");
        grid.setConstraints(productsText, 1, 0);
        productsText.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-alignment: center;");
        grid.getChildren().add(productsText);

        UUID id = DatabaseConnector.instance.sendRequest("SELECT * FROM stock;");

        ResultSet resultSet = null;
        for (int i = 0; i < 20; i++) {
            try {
                resultSet = DatabaseConnector.instance.checkResult(id);
                if(resultSet != null){
                    break;
                }
                Thread.sleep(250);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        try{
            int counter = 1;
            while (resultSet.next()){
                Product p = new Product(resultSet.getString("productName"), resultSet.getDouble("price"), resultSet.getDouble("markup"));
                p.stock = resultSet.getDouble("stock");
                String img = resultSet.getString("image");
                if(!img.equals("default")){
                    p.imageLoc = img;
                }
                p.id = resultSet.getInt("id");
                ProductPane pane = new ProductPane(p, this);
                GridPane.setConstraints(pane, 1, counter++);
                grid.getChildren().add(pane);
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }

        Label sepet = new Label("Your Cart");
        sepet.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-alignment: center;");
        grid.setConstraints(sepet, 2, 0);
        grid.getChildren().add(sepet);


        scene = new Scene(grid, 900, 540);
    }

    public void removeFromSepet(Product product){
        for (int i = 0; i < grid.getChildren().size(); i++) {
            Node node = grid.getChildren().get(i);
            if (node instanceof ProductPane && GridPane.getColumnIndex(node) == 1) {
                ProductPane productPane = (ProductPane) node;
                if (productPane.getProduct().productName.equals(product.productName)) {
                    double oldStock = productPane.getProduct().stock;
                    productPane.getProduct().stock += product.stock;
                    String newText = productPane.priceLabel.getText().replace(String.format("%.1f", oldStock), String.format("%.1f", productPane.getProduct().stock));
                    productPane.priceLabel.setText(newText);
                    break;
                }
            }
        }


        for (int i = 0; i < grid.getChildren().size(); i++) {
            Node node = grid.getChildren().get(i);
            if (node instanceof ProductPane && GridPane.getColumnIndex(node) == 2) {
                ProductPane productPane = (ProductPane) node;
                Logger.log("Checking %d against %d", productPane.getProduct().price, product.price);
                if (productPane.getProduct().productName.equals(product.productName) && productPane.getProduct().price == product.price) {
                    grid.getChildren().remove(i);
                    break;
                }
            }
        }
        for (int i = 0; i < sepet.size(); i++) {
            if(sepet.get(i).productName.equals( product.productName)){
                sepet.remove(i);
                break;
            }
        }
    }

    public void addToSepet(Product product){
        for (int i = 0; i < grid.getChildren().size(); i++) {
            Node node = grid.getChildren().get(i);
            if (node instanceof ProductPane && GridPane.getColumnIndex(node) == 1) {
                ProductPane productPane = (ProductPane) node;
                if (productPane.getProduct().productName.equals(product.productName)) {
                    double oldStock = productPane.getProduct().stock;
                    productPane.getProduct().stock -= product.stock;
                    String newText = productPane.priceLabel.getText().replace(String.format("%.1f", oldStock), String.format("%.1f", productPane.getProduct().stock));

                    productPane.priceLabel.setText(newText);
                    break;
                }
            }
        }



        int foundInSepet = -1;
        for (int i = 0; i < grid.getChildren().size(); i++) {
            Node node = grid.getChildren().get(i);
            if (node instanceof ProductPane && GridPane.getColumnIndex(node) == 2) {
                ProductPane productPane = (ProductPane) node;
                if (productPane.getProduct().productName.equals(product.productName)) {
                    foundInSepet = i;
                    break;
                }
            }
        }
        if(foundInSepet == -1){
            ProductPane pane = new ProductPane(product, this, true);
            GridPane.setConstraints(pane, 2, sepet.size() + 1);
            grid.getChildren().add(pane);
            sepet.add(product);
        }
        else{
            ProductPane oldPane = (ProductPane) grid.getChildren().get(foundInSepet);
            double oldPrice = oldPane.getProduct().price;
            double oldStock = oldPane.getProduct().stock;
            oldPane.getProduct().price += product.price;
            oldPane.getProduct().stock += product.stock;
            String newText = oldPane.priceLabel.getText().replace(String.format("%.1f", oldStock), String.format("%.1f", oldPane.getProduct().stock)).replace(String.format("%.1f", oldPrice), String.format("%.1f", oldPane.getProduct().price));

            oldPane.priceLabel.setText(newText);
            for (int i = 0; i < sepet.size(); i++) {
                if(sepet.get(i).productName.equals( product.productName)){
                    sepet.remove(i);
                    break;
                }
            }
            sepet.add(product);
        }
    }

}
