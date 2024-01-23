package comp.CMPE343.UserInterface.Buyer;

import comp.CMPE343.Logger;
import comp.CMPE343.UserInterface.Driver.Order;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class OrderPane extends Region {


    private Order order;

    Label priceLabel;

    ProductPage parent;

    Cart parent2;

    boolean clickedBefore = false;

    public Order getOrder() {
        return order;
    }

    public static OrderPane getTest(){
        Order testProduct = new Order();
        testProduct.customerName = "Test";
        testProduct.total = 192.2f;
        testProduct.adres = "Test";
        testProduct.taken = false;
        return new OrderPane(testProduct);
    }

    public OrderPane(Order order) {
        this.order = order;
        boolean taken = order.taken;

        Label label1 = new Label("Order : " + order.id);
        priceLabel = new Label("Your order is currently " + (taken ? " is being delivered!" : " is in queue."));

        HBox hbox = new HBox(label1, priceLabel);
        hbox.setSpacing(40);
        hbox.setPadding(new Insets(5, 5, 5, 5));

        this.setStyle("-fx-border-color: #ffffff; -fx-border-width: 3; -fx-border-radius: 1; -fx-background-color: #ffffff");
        this.getChildren().add(hbox);
        this.setOnMouseClicked(e -> onClick());
    }
    public void onClick(){
        Stage popUp = new Stage();
        popUp.initModality(Modality.APPLICATION_MODAL);
        popUp.initOwner(this.getParent().getScene().getWindow());
        GridPane grid = new GridPane();

        Scene scene = new Scene(grid, 300, 200);
        popUp.setScene(scene);

        popUp.show();
    }

}
