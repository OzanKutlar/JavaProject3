package comp.CMPE343.UserInterface.Owner;

import java.util.List;

import comp.CMPE343.Database.DatabaseConnector;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

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

    public OwnerController() {
        this.databaseAdapter = new DatabaseConnector();
    }

    @FXML
    private void initialize() {
        loadOrders();
        loadReport();
    }

    private void loadOrders() {
        // Retrieve all orders from the database
        List<Order> orders = databaseAdapter.getAllOrders();
        ordersListView.getItems().addAll(orders);
    }

    @FXML
    private void addProduct() {
        // Add a new product to the database
        String name = productNameTextField.getText();
        double price = Double.parseDouble(productPriceTextField.getText());
        int threshold = Integer.parseInt(productThresholdTextField.getText());
        Product product = new Product(name, price, threshold);
        databaseAdapter.addProduct(product);
    }

    @FXML
    private void removeProduct() {
        // Remove a product from the database
        Product selectedProduct = productsListView.getSelectionModel().getSelectedItem();
        if (selectedProduct != null) {
            databaseAdapter.removeProduct(selectedProduct);
            productsListView.getItems().remove(selectedProduct);
        }
    }

    @FXML
    private void updateProduct() {
        // Update a product in the database
        Product selectedProduct = productsListView.getSelectionModel().getSelectedItem();
        if (selectedProduct != null) {
            String name = productNameTextField.getText();
            double price = Double.parseDouble(productPriceTextField.getText());
            int threshold = Integer.parseInt(productThresholdTextField.getText());
            Product updatedProduct = new Product(name, price, threshold);
            databaseAdapter.updateProduct(selectedProduct, updatedProduct);
            productsListView.refresh();
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

    private void loadReport() {
        // Retrieve data for the report from the database
        List<Product> products = databaseAdapter.getAllProducts();
        XYChart.Series series = new XYChart.Series();
        for (Product product : products) {
            series.getData().add(new XYChart.Data(product.getName(), product.getSales()));
        }
        reportChart.getData().add(series);
    }
}