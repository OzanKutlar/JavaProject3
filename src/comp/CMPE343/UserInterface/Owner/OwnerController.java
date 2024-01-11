package comp.CMPE343.UserInterface.Owner;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import comp.CMPE343.Database.DatabaseConnector;
import comp.CMPE343.UserInterface.Driver.Order;
import comp.CMPE343.UserInterface.Buyer.Product;
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
        this.databaseAdapter = DatabaseConnector.instance;
    }

    @FXML
    private void initialize() {
        loadOrders();
        loadReport();
    }

    private List<Order> getAllOrders(){
        List<Order> orders = new ArrayList<>();


        return orders;
    }

    private void loadOrders() {
        // Retrieve all orders from the database
        List<Order> orders = getAllOrders();
        ordersListView.getItems().addAll(orders);
    }

    private void addProduct(Product product){

    }

    @FXML
    private void addProduct() {
        // Add a new product to the database
        String name = productNameTextField.getText();
        double price = Double.parseDouble(productPriceTextField.getText());
        double threshold = Integer.parseInt(productThresholdTextField.getText());
//        double stock = TODO : Add Stock
        Product product = new Product(name, price, threshold);
        addProduct(product);
    }

    private void removeProduct(Product product){

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
                        o.adres = result.getString("adres");
                        o.customerName = result.getString("customer");
                        o.total = result.getFloat("price");
                        o.toBeDelivered = result.getDate("time");
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

    private void loadReport() {
        // Retrieve data for the report from the database
        List<Product> products = getAllProducts();
        XYChart.Series series = new XYChart.Series();
        for (Product product : products) {
            series.getData().add(new XYChart.Data(product.getName(), product.getSales()));
        }
        reportChart.getData().add(series);
    }
}