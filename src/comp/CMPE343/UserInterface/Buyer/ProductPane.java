package comp.CMPE343.UserInterface.Buyer;

import comp.CMPE343.Logger;
import controller.LoginFormController;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;

public class ProductPane extends Region {


    private Product product;

    public static ProductPane getTest(){
        Product testProduct = new Product();
        testProduct.productName = "Test";
        testProduct.price = 192.2f;
        return new ProductPane("../Images/default.png", testProduct);
    }

    public ProductPane(String imageUrl, Product product) {
        this.product = product;

        // TODO : Add image to the left of the product. Maybe cancelled.
        ImageView imageView = new ImageView(String.valueOf(ProductPane.class.getResource(imageUrl)));
        imageView.setFitWidth(50);
        imageView.setFitHeight(50);



        Label label1 = new Label(product.productName);
        Label label2 = new Label(product.price + " TL");


        HBox hbox = new HBox(imageView,label1, label2);
        hbox.setSpacing(40);
        hbox.setPadding(new Insets(5, 5, 5, 5));

//        imageView.fitHeightProperty().bind(hbox.heightProperty());
//        imageView.fitHeightProperty().bind(hbox.heightProperty());

        this.setStyle("-fx-border-color: #ffffff; -fx-border-width: 3; -fx-border-radius: 1; -fx-background-color: #ffffff");
        this.getChildren().add(hbox);
        this.setOnMouseClicked(e -> onClick());
    }

    public void onClick(){
        Logger.log( "Clicked on product : %d", this.product.productName);
    }

}
