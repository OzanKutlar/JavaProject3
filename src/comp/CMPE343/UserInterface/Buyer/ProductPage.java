package comp.CMPE343.UserInterface.Buyer;

import comp.CMPE343.Logger;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;

import java.util.ArrayList;

public class ProductPage {


    public Scene scene;

    private GridPane grid;

    public ArrayList<Product> sepet = new ArrayList<>();

    public ProductPage(){
        grid = new GridPane();
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
            ProductPane pane = ProductPane.getTest(this);
            grid.setConstraints(pane, 1, i);
            grid.getChildren().add(pane);
        }


        Label sepet = new Label("Sepetiniz");
        grid.setConstraints(sepet, 2, 0);
        grid.getChildren().add(sepet);


        scene = new Scene(grid, 900, 540);
    }

    public void removeFromSepet(Product product){
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

    private void adjustReal(Product p, int adjust){

    }

    public void addToSepet(Product product){
        int foundInSepet = -1;
        for (int i = 0; i < grid.getChildren().size(); i++) {
            Node node = grid.getChildren().get(i);
            if (node instanceof ProductPane && GridPane.getColumnIndex(node) == 2) {
                ProductPane productPane = (ProductPane) node;
                Logger.log("Checking %d against %d", productPane.getProduct().price, product.price);
                if (productPane.getProduct().productName.equals(product.productName) && productPane.getProduct().price == product.price) {
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
            double price = product.price / oldPane.getProduct().stock;
            double newStock = oldPane.getProduct().stock + product.stock;
            price = price * newStock;
            product.price = price;
            product.stock = newStock;
            for (int i = 0; i < sepet.size(); i++) {
                if(sepet.get(i).productName.equals( product.productName)){
                    sepet.remove(i);
                    break;
                }
            }
            sepet.add(product);
            ProductPane newPane = new ProductPane(product, this, true);
            grid.getChildren().set(foundInSepet, newPane);
        }
    }

}
