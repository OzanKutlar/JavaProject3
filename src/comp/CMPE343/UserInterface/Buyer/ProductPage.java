package comp.CMPE343.UserInterface.Buyer;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;

public class ProductPage {


    public Scene scene;

    public ProductPage(){
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
//        grid.setPadding(new Insets(25,25,25,25));
        grid.setAlignment(Pos.CENTER);


        ColumnConstraints UserData = new ColumnConstraints();
        UserData.setHgrow(Priority.NEVER);
        ColumnConstraints Products = new ColumnConstraints();
        Products.setHgrow(Priority.ALWAYS);
        ColumnConstraints Sepet = new ColumnConstraints();
        Sepet.setHgrow(Priority.NEVER);

        grid.getColumnConstraints().addAll(UserData, Products, Sepet);

        // Products Columnu
        Label productsText = new Label("Select a product!");
        grid.setConstraints(productsText, 1, 0);
        grid.getChildren().add(productsText);
        for (int i = 1; i < 7; i++) {
            ProductPane pane = ProductPane.getTest();
            grid.setConstraints(pane, 1, i);
            grid.getChildren().add(pane);
        }



        // Sepet
        for (int i = 0; i < 3; i++) {
            ProductPane pane = ProductPane.getTest();
            grid.setConstraints(pane, 2, i);
            grid.getChildren().add(pane);
        }


        scene = new Scene(grid, 900, 540);
    }

}
