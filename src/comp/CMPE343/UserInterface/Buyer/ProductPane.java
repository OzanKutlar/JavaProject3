package comp.CMPE343.UserInterface.Buyer;

import comp.CMPE343.Logger;
import controller.LoginFormController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
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

    boolean clickedBefore = false;

    public Product getProduct() {
        return product;
    }

    public static ProductPane getTest(ProductPage parent){
        Product testProduct = new Product();
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
        priceLabel = new Label(String.format("%.1f", product.price) + " TL/kg\n" + String.format("%.1f", product.stock) + " kg kaldı!");

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

    private void removeFromSepet(){
        Stage popUp = new Stage();
        popUp.initModality(Modality.APPLICATION_MODAL);
        popUp.initOwner(this.getParent().getScene().getWindow());

        Text text = new Text("Are you sure you want to remove " + product.productName + " from your cart?");

        Button button = new Button("Yes");
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
        Scene scene = new Scene(grid, 300, 200);
        popUp.setScene(scene);

// Show the pop-up stage
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
                    Logger.debugLog("Product", "Unparseable");
                }
            }
        });


        slider.valueProperty().addListener((observable, oldValue, newValue) -> {
            textField.setText(String.format("%.1f", newValue.doubleValue()));
        });

        Button button = new Button("Add to cart");
        button.setOnAction(e -> {
            // Add your confirmation logic here
            Logger.log("Confirmed: %d", slider.getValue());
            clickedBefore = true;
            Product toSepet = product;
            toSepet.stock = slider.getValue();
            toSepet.price = slider.getValue() * product.price;
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

        popUp.show();
    }

}
