package comp.CMPE343.UserInterface.Buyer;

import comp.CMPE343.Logger;
import controller.LoginFormController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.awt.*;

public class ProductPane extends Region {


    private Product product;

    Label priceLabel;

    ProductPage parent;

    Cart parent2;

    boolean clickedBefore = false;

    public Product getProduct() {
        return product;
    }

    public static ProductPane getTest(ProductPage parent){
        Product testProduct = new Product("Test", 192.2, 100);
        testProduct.productName = "Test";
        testProduct.price = 192.2f;
        testProduct.stock = 100;
        return new ProductPane(testProduct, parent);
    }

    public ProductPane(Product product, ProductPage parent) {
        this.product = product;
        this.parent = parent;
        ImageView imageView = new ImageView(String.valueOf(ProductPane.class.getResource(product.imageLoc)));
        imageView.setFitWidth(50);
        imageView.setFitHeight(50);

        Label label1 = new Label(product.productName);
        priceLabel = new Label(String.format("%.1f", product.price) + " TL/kg\nOnly " + String.format("%.1f", product.stock) + " kg left!");

        HBox hbox = new HBox(imageView, label1, priceLabel);
        hbox.setSpacing(40);
        hbox.setPadding(new Insets(5, 5, 5, 5));

        this.setStyle("-fx-border-color: #ffffff; -fx-border-width: 3; -fx-border-radius: 1; -fx-background-color: #ffffff");
        this.getChildren().add(hbox);
        this.setOnMouseClicked(e -> onClick());
    }

    public ProductPane(Product product, ProductPage parent, boolean sepet) {
        this.product = product;
        this.parent = parent;
        ImageView imageView = new ImageView(String.valueOf(ProductPane.class.getResource(product.imageLoc)));
        imageView.setFitWidth(50);
        imageView.setFitHeight(50);

        Label label1 = new Label(product.productName);
        priceLabel = new Label(String.format("%.1f", product.price) + " TL\n" + String.format("%.1f", product.stock) + " kg");

        HBox hbox = new HBox(imageView, label1, priceLabel);
        hbox.setSpacing(40);
        hbox.setPadding(new Insets(5, 5, 5, 5));

        this.setStyle("-fx-border-color: #ffffff; -fx-border-width: 3; -fx-border-radius: 1; -fx-background-color: #ffffff");
        this.getChildren().add(hbox);
        this.setOnMouseClicked(e -> removeFromSepet());
    }

    public ProductPane(Product product, Cart parent) {
        this.product = product;
        this.parent2 = parent;
        ImageView imageView = new ImageView(String.valueOf(ProductPane.class.getResource(product.imageLoc)));
        imageView.setFitWidth(50);
        imageView.setFitHeight(50);

        Label label1 = new Label(product.productName);
        priceLabel = new Label(String.format("%.1f", product.price) + " TL\n" + String.format("%.1f", product.stock) + " kg");

        HBox hbox = new HBox(imageView, label1, priceLabel);
        hbox.setSpacing(40);
        hbox.setPadding(new Insets(5, 5, 5, 5));

        this.setStyle("-fx-border-color: #ffffff; -fx-border-width: 3; -fx-border-radius: 1; -fx-background-color: #ffffff");
        this.getChildren().add(hbox);
        this.setOnMouseClicked(e -> changeAmount());
    }

    private void changeAmount(){
        Stage popUp = new Stage();
        popUp.initModality(Modality.APPLICATION_MODAL);
        popUp.initOwner(this.getParent().getScene().getWindow());

        TextField textField = new TextField("0.0");
        Slider slider = new Slider(0, Math.round(product.stock), 0);
        slider.setMajorTickUnit(10);
        slider.setMinorTickCount(5);
        slider.setShowTickMarks(true);
        slider.setShowTickLabels(true);
        slider.setValue(product.stock);


        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty()) {
                try{
                    slider.setValue(Double.parseDouble(newValue));
                }
                catch(Exception e){
                    Logger.debugLog("Product", "Unparseable");
                }
            }
        });


        slider.valueProperty().addListener((observable, oldValue, newValue) -> {
            textField.setText(String.format("%.1f", newValue.doubleValue()));
        });

        Button button = new Button("Lower the KG");
        button.setStyle("-fx-alignment: center; -fx-background-color: #3498db; -fx-font-size: 14px; -fx-font-weight: bold;");
        button.setOnAction(e -> {
            // Add your confirmation logic here
            Logger.log("Confirmed: %d", slider.getValue());
            clickedBefore = true;
            Product toSepet = new Product(product);
            toSepet.stock = slider.getValue();
            toSepet.price = slider.getValue() * (product.price/product.stock);
            parent2.changeAmount(toSepet);
            popUp.close();
        });

        Text text = new Text("Please select how much " + product.productName + " you want.");
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setAlignment(Pos.CENTER);
        GridPane.setConstraints(text, 0, 0);
        GridPane.setConstraints(slider, 0, 1);
        GridPane.setConstraints(textField, 1, 0);
        GridPane.setConstraints(button, 0, 2, 2, 1);

        grid.getChildren().addAll(text, slider, textField, button);
        Scene scene = new Scene(grid, 300, 200);
        popUp.setScene(scene);
        popUp.setTitle("Change Item Count");

        popUp.show();
    }

    private void removeFromSepet(){
        Stage popUp = new Stage();
        popUp.initModality(Modality.APPLICATION_MODAL);
        popUp.initOwner(this.getParent().getScene().getWindow());

        Text text = new Text("Item : " + product.productName + "\nAre you sure you want to remove this item from your cart?");

        Button button = new Button("Yes");
        button.setStyle("-fx-alignment: center; -fx-background-color: #3498db; -fx-font-size: 14px; -fx-font-weight: bold;");
        button.setOnAction(e -> {
            Logger.log("Confirmed: Removing %d from sepet", product.productName);
            parent.removeFromSepet(product);
            popUp.close();
        });

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setAlignment(Pos.CENTER);
        GridPane.setConstraints(text, 0, 0);
        GridPane.setConstraints(button, 0, 2, 2, 1);

        grid.getChildren().addAll(text, button);
        Scene scene = new Scene(grid, 350, 200);
        popUp.setScene(scene);
        popUp.setTitle("Remove from Cart");

        popUp.show();
    }

    public void onClick(){
        Stage popUp = new Stage();
        popUp.initModality(Modality.APPLICATION_MODAL);
        popUp.initOwner(this.getParent().getScene().getWindow());

        TextField textField = new TextField("0.0");
        Slider slider = new Slider(0, Math.round(product.stock), 0);
        slider.setMajorTickUnit(10);
        slider.setMinorTickCount(5);
        slider.setShowTickMarks(true);
        slider.setShowTickLabels(true);


        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty()) {
                try{
                    slider.setValue(Double.parseDouble(newValue));
                }
                catch(Exception e){
                    slider.setValue(0);
                    Logger.debugLog("Product", "Unparseable");
                }
            }
        });


        slider.valueProperty().addListener((observable, oldValue, newValue) -> {
            textField.setText(String.format("%.1f", newValue.doubleValue()));
        });

        Button button = new Button("Add to cart");
        button.setStyle("-fx-alignment: center; -fx-background-color: #3498db; -fx-font-size: 14px; -fx-font-weight: bold;");
        button.setOnAction(e -> {
            // Add your confirmation logic here
            Logger.log("Confirmed: %d", slider.getValue());
            if(slider.getValue() == 0){
                Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid input. Please enter valid numbers.", ButtonType.OK);
                alert.showAndWait();
                popUp.close();
                return;
            }
            clickedBefore = true;
            Product toSepet = new Product(product);
            toSepet.stock = slider.getValue();
            toSepet.price = 0;
            if(product.stock - toSepet.stock <= product.markupNumber){
                double diff;
                if(product.stock > product.markupNumber){
                    diff = product.markupNumber - (product.stock - toSepet.stock);
                }else{
                    diff = toSepet.stock;
                }

                Logger.log("Over Markup value, charging double for %d amount", diff);
                double diff2 = toSepet.stock - diff;
                toSepet.price += diff * product.price * 2;
                toSepet.price += diff2 * product.price;
            }
            else{
                toSepet.price = slider.getValue() * product.price;
            }
            parent.addToSepet(toSepet);
            popUp.close();
        });

        Text text = new Text("Please select how much " + product.productName + " you want.");
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setAlignment(Pos.CENTER);
        GridPane.setConstraints(text, 0, 0);
        GridPane.setConstraints(slider, 0, 1);
        GridPane.setConstraints(textField, 1, 0);
        GridPane.setConstraints(button, 0, 2, 2, 1);

        grid.getChildren().addAll(text, slider, textField, button);
        Scene scene = new Scene(grid, 300, 200);
        popUp.setScene(scene);
        popUp.setTitle("Add to cart");

        popUp.show();
    }

}
