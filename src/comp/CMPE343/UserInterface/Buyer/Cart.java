package comp.CMPE343.UserInterface.Buyer;

import comp.CMPE343.Database.DatabaseConnector;
import comp.CMPE343.Logger;
import comp.CMPE343.Main;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.UUID;

public class Cart {


    public Scene scene;

    private GridPane grid;

    ProductPage parent;

    public ArrayList<Product> sepet;

    String adress;
    String Username;

    double total;


    public Cart(ArrayList<Product> sepet, ProductPage parent){
        this.sepet = sepet;
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


        ColumnConstraints Products = new ColumnConstraints();
        Products.setHgrow(Priority.ALWAYS);
        ColumnConstraints Sepet = new ColumnConstraints();
        Sepet.setHgrow(Priority.NEVER);

        grid.getColumnConstraints().addAll(Products, Sepet);

        // User Data
        Button logOutButton = new Button("Back to Main Page");
        logOutButton.setStyle("-fx-alignment: center; -fx-background-color: #3498db; -fx-font-size: 14px; -fx-font-weight: bold;");
        GridPane.setConstraints(logOutButton, 2, 0);


        Button finishPurchaseButton = new Button("Finish Purchase");
        finishPurchaseButton.setStyle("-fx-alignment: center; -fx-background-color: #3498db; -fx-font-size: 14px; -fx-font-weight: bold;");
        GridPane.setConstraints(finishPurchaseButton, 2, 4);

        logOutButton.setOnAction(e ->{
            ((Stage) this.scene.getWindow()).setScene(parent.scene);
        });

        finishPurchaseButton.setOnAction(e ->{
            Stage popUp = new Stage();
            popUp.initModality(Modality.APPLICATION_MODAL);
            popUp.initOwner(finishPurchaseButton.getParent().getScene().getWindow());
            VBox vbox = new VBox(10);

            Label welcomeText = new Label("Please choose a delivery Date!");
            welcomeText.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-alignment: center;");

            // Create a ToggleGroup for the radio buttons
            ToggleGroup toggleGroup = new ToggleGroup();

            // Create radio buttons
            RadioButton option1 = new RadioButton("Today");
            RadioButton option2 = new RadioButton("Tomorrow");
            RadioButton option3 = new RadioButton("48h later");

            // Set the default selection to Option 1
            option1.setSelected(true);

            // Add radio buttons to the ToggleGroup
            option1.setToggleGroup(toggleGroup);
            option2.setToggleGroup(toggleGroup);
            option3.setToggleGroup(toggleGroup);

            // Create a "Press me" button
            Button pressMeButton = new Button("Finish Purchase");
            pressMeButton.setStyle("-fx-alignment: center; -fx-background-color: #3498db; -fx-font-size: 14px; -fx-font-weight: bold;");

            vbox.getChildren().addAll(welcomeText ,option1, option2, option3, pressMeButton);
            pressMeButton.setOnAction(e2 ->{
                RadioButton selectedRadioButton = (RadioButton) toggleGroup.getSelectedToggle();
                String selectedOption = selectedRadioButton.getText();
                int dayIncrease = switch (selectedOption) {
                    case "Tomorrow" -> 1;
                    case "The Day After" -> 2;
                    default -> 0;
                };

                String itemStr = sepet.get(0).id + ":" + sepet.get(0).stock;;
                for (int i = 1; i < sepet.size(); i++) {
                    Product p = sepet.get(i);
                    itemStr += "," + p.id + ":" + p.stock;
                }

                DatabaseConnector.instance.sendRequest("INSERT INTO orders (address, customerName, orderItems, total, toBeDelivered) VALUES ('" + adress + "', '" + Username + "', '" + itemStr + "', " + total + ", NOW()" + (dayIncrease == 0 ? ");" : " + INTERVAL " + dayIncrease + " DAY);"));

                for (Product p : sepet){
                    DatabaseConnector.instance.sendRequest("UPDATE stock SET stock = stock - " + p.stock + " WHERE id = " + p.id + ";");
                }
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Order Successful");
                alert.showAndWait();

                ProductPage productPage = new ProductPage();

                ((Stage) grid.getScene().getWindow()).setScene(productPage.scene);
                popUp.close();
            });
            Scene scene = new Scene(vbox, 300, 200);
            popUp.setScene(scene);
            popUp.setTitle("Select a delivery time!");
            popUp.showAndWait();
        });

        grid.getChildren().addAll(logOutButton, finishPurchaseButton);


        // Products Columnu
        Label productsText = new Label("Select a product!");
        productsText.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-alignment: center;");
        grid.setConstraints(productsText, 0, 0);
        grid.getChildren().add(productsText);

        try{
            int counter = 1;
            for(Product p : sepet) {
                ProductPane pane = new ProductPane(p, this);
                GridPane.setConstraints(pane, 0, counter++);
                grid.getChildren().add(pane);
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }




        scene = new Scene(grid, 900, 540);
    }

    public void changeAmount(Product product){
        for (int i = 0; i < grid.getChildren().size(); i++) {
            Node node = grid.getChildren().get(i);
            if (node instanceof ProductPane && GridPane.getColumnIndex(node) == 0) {
                ProductPane productPane = (ProductPane) node;
                if (productPane.getProduct().productName.equals(product.productName)) {
                    double oldPrice = productPane.getProduct().price;
                    double oldStock = productPane.getProduct().stock;
                    productPane.getProduct().price = product.price;
                    productPane.getProduct().stock = product.stock;
                    String newText = productPane.priceLabel.getText().replace(String.format("%.1f", oldStock), String.format("%.1f", productPane.getProduct().stock)).replace(String.format("%.1f", oldPrice), String.format("%.1f", productPane.getProduct().price));

                    productPane.priceLabel.setText(newText);
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
        sepet.add(product);
    }

}
