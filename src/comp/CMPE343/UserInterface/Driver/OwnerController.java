package comp.CMPE343.UserInterface.Driver;

import comp.CMPE343.Database.DatabaseConnector;
import comp.CMPE343.UserInterface.Buyer.Carrier;
import comp.CMPE343.UserInterface.Buyer.Product;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class OwnerController {
    private final DatabaseConnector databaseAdapter;
    @FXML
    private ListView<Order> ordersListView;

    @FXML
    private TextField productNameTextField;

    @FXML
    private TextField productPriceTextField;

    @FXML
    private TextField productThresholdTextField;
    @FXML
    private ListView<Product> productsListView;
    @FXML
    private TextField carrierNameTextField;
    @FXML
    private TextField carrierAddressTextField;
    @FXML
    private ListView<Carrier> carriersListView;

    public OwnerController() {
        this.databaseAdapter = DatabaseConnector.instance;
    }

    @FXML
    private void initialize() {
        loadOrders();
        loadProducts();
        loadDrivers();
    }

    private List<Order> getAllOrders(){
        List<Order> orders = new ArrayList<>();


        return orders;
    }

    private void loadOrders() {
        // Retrieve all orders from the database
        ordersListView.getItems().removeAll(ordersListView.getItems());
        List<Order> orders = getAvailableOrders();
        ordersListView.getItems().addAll(orders);
    }

    private void loadProducts(){
        productsListView.getItems().removeAll(productsListView.getItems());
        List<Product> products = getAvailableProducts();
        productsListView.getItems().addAll(products);
    }

    private void loadDrivers(){

    }

    private void addProduct(Product product){
        String query = "INSERT INTO stock (image, stock, price, markup, productName) VALUES ('%r', %r, %r, %r, '%r');";
        query = query.replaceFirst("%r", product.imageLoc);
        query = query.replaceFirst("%r", String.valueOf(product.stock));
        query = query.replaceFirst("%r", String.valueOf(product.price));
        query = query.replaceFirst("%r", String.valueOf(product.markupNumber));
        query = query.replaceFirst("%r", product.productName);
        DatabaseConnector.instance.sendRequest(query);
    }

    @FXML
    private void addProduct() {
        // Add a new product to the database
        String name = productNameTextField.getText();
        double price = Double.parseDouble(productPriceTextField.getText());
        Stage popUp = new Stage();
        popUp.initModality(Modality.APPLICATION_MODAL);
        popUp.initOwner(productNameTextField.getScene().getWindow());

        // Create a GridPane for the layout
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10));
        gridPane.setHgap(10);
        gridPane.setVgap(10);


        Label imageLabel = new Label("Image Location:");
        TextField imageTextField = new TextField();
        imageTextField.setText("default");
        Label countLabel = new Label("Stock Amount:");
        TextField stockCountField = new TextField();
        Label doublePriceLabel = new Label("Threshold:");
        TextField doublePriceField = new TextField();
        gridPane.add(imageLabel, 0, 0);
        gridPane.add(imageTextField, 1, 0);

        gridPane.add(countLabel, 0, 1);
        gridPane.add(stockCountField, 1, 1);

        gridPane.add(doublePriceLabel, 0, 2);
        gridPane.add(doublePriceField, 1, 2);


        Button confirmButton = new Button("Add Product");
        confirmButton.setOnAction(event -> {
            try {
                String imageLoc = imageTextField.getText();
                double stock = Double.parseDouble(stockCountField.getText());
                double thresholdField = Double.parseDouble(doublePriceField.getText());

                Product product = new Product();
                product.imageLoc = imageLoc;
                product.productName = name;
                product.stock = stock;
                product.price = price;
                product.markupNumber = thresholdField;

                addProduct(product);
                popUp.close();
            } catch (NumberFormatException e) {
                // Handle invalid input
                Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid input. Please enter valid numbers.", ButtonType.OK);
                alert.showAndWait();
            }
        });


        gridPane.add(confirmButton, 0, 3, 2, 1);
        Scene scene = new Scene(gridPane);
        popUp.setScene(scene);
        popUp.showAndWait();
    }

    private void removeProduct(Product product){
        String query = "DELETE FROM stock WHERE id = " + product.id + ";";
        DatabaseConnector.instance.sendRequest(query);
    }

    @FXML
    private void removeProduct() {
        // Remove a product from the database
        Product selectedProduct = productsListView.getSelectionModel().getSelectedItem();
        if (selectedProduct != null) {
            removeProduct(selectedProduct);
            productsListView.getItems().remove(selectedProduct);
        }
    }

    @FXML
    private void updateProduct() {
        // Update a product in the database
        Product selectedProduct = productsListView.getSelectionModel().getSelectedItem();
        if (selectedProduct != null) {
            Stage popUp = new Stage();
            popUp.initModality(Modality.APPLICATION_MODAL);
            popUp.initOwner(productNameTextField.getScene().getWindow());

            // Create a GridPane for the layout
            GridPane gridPane = new GridPane();
            gridPane.setPadding(new Insets(10));
            gridPane.setHgap(10);
            gridPane.setVgap(10);

            Label nameLabel = new Label("Product Name:");
            TextField nameTextField = new TextField();
            nameTextField.setText(selectedProduct.productName);
            gridPane.add(nameLabel, 0, 0);
            gridPane.add(nameTextField, 1, 0);

            Label priceLabel = new Label("Product Price:");
            TextField priceTextField = new TextField();
            priceTextField.setText(selectedProduct.price + "");
            gridPane.add(priceLabel, 0, 1);
            gridPane.add(priceTextField, 1, 1);


            Label imageLabel = new Label("Image Location:");
            TextField imageTextField = new TextField();
            imageTextField.setText(selectedProduct.imageLoc);
            gridPane.add(imageLabel, 0, 2);
            gridPane.add(imageTextField, 1, 2);


            Label countLabel = new Label("Stock Amount:");
            TextField stockCountField = new TextField();
            stockCountField.setText(selectedProduct.stock + "");
            gridPane.add(countLabel, 0, 3);
            gridPane.add(stockCountField, 1, 3);


            Label doublePriceLabel = new Label("Threshold:");
            TextField doublePriceField = new TextField();
            doublePriceField.setText(selectedProduct.markupNumber + "");
            gridPane.add(doublePriceLabel, 0, 4);
            gridPane.add(doublePriceField, 1, 4);


            Button confirmButton = new Button("Save Product");
            confirmButton.setOnAction(event -> {
                try {
                    productsListView.getItems().remove(selectedProduct);
                    String name = nameTextField.getText();
                    double price = Double.parseDouble(priceTextField.getText());
                    String imageLoc = imageTextField.getText();
                    double stock = Double.parseDouble(stockCountField.getText());
                    double thresholdField = Double.parseDouble(doublePriceField.getText());

                    Product product = new Product();
                    product.id = selectedProduct.id;
                    product.imageLoc = imageLoc;
                    product.productName = name;
                    product.stock = stock;
                    product.price = price;
                    product.markupNumber = thresholdField;

                    String query = "UPDATE stock SET image = '%r', stock = %r, price = %r, markup = %r, productName = '%r' WHERE id = %r;";
                    query = query.replaceFirst("%r", product.imageLoc);
                    query = query.replaceFirst("%r", String.valueOf(product.stock));
                    query = query.replaceFirst("%r", String.valueOf(product.price));
                    query = query.replaceFirst("%r", String.valueOf(product.markupNumber));
                    query = query.replaceFirst("%r", product.productName);
                    query = query.replaceFirst("%r", String.valueOf(selectedProduct.id));
                    DatabaseConnector.instance.sendRequest(query);
                    productsListView.getItems().add(product);
                    popUp.close();
                } catch (NumberFormatException e) {
                    // Handle invalid input
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid input. Please enter valid numbers.", ButtonType.OK);
                    alert.showAndWait();
                }
            });


            gridPane.add(confirmButton, 0, 5, 2, 1);
            Scene scene = new Scene(gridPane);
            popUp.setScene(scene);
            popUp.showAndWait();
        }
    }

    @FXML
    private void employCarrier() {
        // Employ a new carrier
        String name = carrierNameTextField.getText();
        String address = carrierAddressTextField.getText();
        Carrier carrier = new Carrier(name, address);
        databaseAdapter.employCarrier(carrier);
    }

    @FXML
    private void fireCarrier() {
        // Fire a carrier
        Carrier selectedCarrier = carriersListView.getSelectionModel().getSelectedItem();
        if (selectedCarrier != null) {
            databaseAdapter.fireCarrier(selectedCarrier);
            carriersListView.getItems().remove(selectedCarrier);
        }
    }

    private List<Order> getAvailableOrders(){
        UUID requestID = databaseAdapter.sendRequest("SELECT * FROM orders;");

        List<Order> orders = new ArrayList<>();
        // Waiting for a response for only 5 seconds.
        for (int i = 0; i < 20; i++) {
            ResultSet result = databaseAdapter.checkResult(requestID);
            if(result != null){
                try{
                    while (result.next()){
                        Order o = new Order();
                        o.id = result.getInt("id");
                        o.adres = result.getString("address");
                        o.customerName = result.getString("customerName");
                        o.total = result.getFloat("total");
                        o.toBeDelivered = result.getDate("toBeDelivered");
                        orders.add(o);
                    }
                }
                catch(Exception e){
                    e.printStackTrace();
                }
                break;
            }
            try{
                Thread.sleep(250);
            }
            catch(Exception e){
            }
        }

        return orders;
    }


    private List<Product> getAvailableProducts(){
        UUID requestID = databaseAdapter.sendRequest("SELECT * FROM stock;");

        List<Product> products = new ArrayList<>();
        // Waiting for a response for only 5 seconds.
        for (int i = 0; i < 20; i++) {
            ResultSet result = databaseAdapter.checkResult(requestID);
            if(result != null){
                try{
                    while (result.next()){
                        Product p = new Product();
                        p.id = result.getInt("id");
                        p.imageLoc = result.getString("image");
                        p.stock = result.getDouble("stock");
                        p.price = result.getDouble("price");
                        p.markupNumber = result.getDouble("markup");
                        p.productName = result.getString("productName");
                        products.add(p);
                    }
                }
                catch(Exception e){
                    e.printStackTrace();
                }
                break;
            }
            try{
                Thread.sleep(250);
            }
            catch(Exception e){
            }
        }

        return products;
    }
}