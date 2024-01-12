package comp.CMPE343.UserInterface.Driver;

import comp.CMPE343.Database.DatabaseConnector;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CarrierController {
    private final DatabaseConnector databaseAdapter;
    @FXML
    private ListView<Order> availableOrdersListView;

    @FXML
    private ListView<Order> selectedOrdersListView;

    @FXML
    private ListView<Order> completedOrdersListView;

    public CarrierController() {
        this.databaseAdapter = DatabaseConnector.instance;
    }

    @FXML
    private void initialize() {
        loadAvailableOrders();
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

    private void loadAvailableOrders() {
        // Retrieve available orders from the database
        List<Order> availableOrders = getAvailableOrders();
        availableOrdersListView.getItems().addAll(availableOrders);
    }

    @FXML
    private void selectOrder() {
        // Move selected orders from available to selected
        Order selectedOrder = availableOrdersListView.getSelectionModel().getSelectedItem();
        if (selectedOrder != null) {
            availableOrdersListView.getItems().remove(selectedOrder);
            selectedOrdersListView.getItems().add(selectedOrder);
        }
    }

    private void completeOrder(Order completedOrder){
        databaseAdapter.sendRequest("UPDATE orders SET completed = true, completionTime = CURRENT_TIMESTAMP WHERE id = " + completedOrder.id + ";");
    }

    @FXML
    private void deliverOrder() {
        // Move completed orders from selected to completed
        Order completedOrder = selectedOrdersListView.getSelectionModel().getSelectedItem();
        if (completedOrder != null) {
            selectedOrdersListView.getItems().remove(completedOrder);
            completedOrdersListView.getItems().add(completedOrder);

            completeOrder(completedOrder);
        }
    }
}