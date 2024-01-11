package comp.CMPE343.UserInterface.Driver;
import java.time.LocalDateTime;
import java.util.List;

import comp.CMPE343.Database.DatabaseConnector;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

public class CarrierController {
    private final DatabaseConnector databaseAdapter;
    @FXML
    private ListView<Order> availableOrdersListView;

    @FXML
    private ListView<Order> selectedOrdersListView;

    @FXML
    private ListView<Order> completedOrdersListView;

    public CarrierController() {
        this.databaseAdapter = new DatabaseConnector();
    }

    @FXML
    private void initialize() {
        loadAvailableOrders();
    }

    private void loadAvailableOrders() {
        // Retrieve available orders from the database
        List<Order> availableOrders = databaseAdapter.getAvailableOrders();
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

    @FXML
    private void deliverOrder() {
        // Move completed orders from selected to completed
        Order completedOrder = selectedOrdersListView.getSelectionModel().getSelectedItem();
        if (completedOrder != null) {
            selectedOrdersListView.getItems().remove(completedOrder);
            completedOrdersListView.getItems().add(completedOrder);

            // Set delivery date and update database
            completedOrder.setDeliveryDate(LocalDateTime.now());
            databaseAdapter.completeOrder(completedOrder);
        }
    }
}
