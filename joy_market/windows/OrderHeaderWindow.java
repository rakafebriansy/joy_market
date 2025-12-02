package joy_market.windows;

import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.geometry.Insets;
import javafx.collections.*;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import joy_market.dataAccess.CourierDA;
import joy_market.dataAccess.OrderHeaderDA;
import joy_market.dataAccess.CustomerDA;
import joy_market.models.Order;
import joy_market.models.Courier;
import joy_market.widgets.OrderTableItem;
import javafx.beans.property.*;

public class OrderHeaderWindow {

    private TableView<OrderTableItem> table; // Table to display order data

    public BorderPane getView() {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10)); // Set padding for layout
        
        // Title label
        Label lblTitle = new Label("Orders Management");
        lblTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        
        // Create the main table to show order data
        table = new TableView<>();
        table.setPrefHeight(400);
        
        // Define table columns
        TableColumn<OrderTableItem, String> colId = new TableColumn<>("Order ID");
        colId.setCellValueFactory(c -> new SimpleStringProperty(String.valueOf(c.getValue().getId())));

        TableColumn<OrderTableItem, String> colCustomer = new TableColumn<>("Customer");
        colCustomer.setCellValueFactory(c -> c.getValue().customerNameProperty());

        TableColumn<OrderTableItem, String> colStatus = new TableColumn<>("Status");
        colStatus.setCellValueFactory(c -> c.getValue().statusProperty());

        TableColumn<OrderTableItem, String> colTotal = new TableColumn<>("Total");
        colTotal.setCellValueFactory(c -> c.getValue().totalPriceProperty());

        TableColumn<OrderTableItem, String> colPromo = new TableColumn<>("Promo");
        colPromo.setCellValueFactory(c -> c.getValue().promoProperty());
        
        // Add all columns to the table
        table.getColumns().addAll(colId, colCustomer, colStatus, colTotal, colPromo);
        
        // Button to assign courier to an order
        Button btnAssign = new Button("Assign Courier");
        btnAssign.setOnAction(e -> assignCourier());
        
        // Button to mark an order as delivered
        Button btnDelivered = new Button("Mark as Delivered");
        btnDelivered.setOnAction(e -> markAsDelivered());
        
        // Top layout containing title and buttons
        VBox topBox = new VBox(10, lblTitle, btnAssign, btnDelivered);
        topBox.setPadding(new Insets(0, 0, 10, 0));

        // Place components in the main layout
        root.setTop(topBox);
        root.setCenter(table);

        refreshTable(); // Load data into table

        return root;
    }
    
    // Refresh table with updated order data
    private void refreshTable() {
        List<Order> orders = OrderHeaderDA.getOrderHeader(); // Fetch orders from database
        ObservableList<OrderTableItem> items = FXCollections.observableArrayList();

        for (Order o : orders) {
        	items.add(new OrderTableItem(o)); // Convert Order to table item
        }

        table.setItems(items); // Display items in table
    }
    
    // Assign courier to selected order
    private void assignCourier() {
        OrderTableItem selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Select an order first!", ButtonType.OK);
            alert.showAndWait();
            return;
        }
        
        // Only pending orders can be assigned
        if (!"PENDING".equals(selected.getStatus())) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Order is not pending!", ButtonType.OK);
            alert.showAndWait();
            return;
        }
        
        // Get all couriers from database
        List<Courier> couriers = CourierDA.getAllCouriers();
        Map<String, Integer> nameToId = couriers.stream()
            .collect(Collectors.toMap(Courier::getEmail, Courier::getId));
        
        // Show dialog to choose courier
        ChoiceDialog<String> dlg = new ChoiceDialog<>();
        dlg.getItems().addAll(nameToId.keySet());
        dlg.setTitle("Assign Courier");

        Optional<String> result = dlg.showAndWait();
        result.ifPresent(name -> {
            int courierId = nameToId.get(name);
            OrderHeaderDA.assignCourier(selected.getId(), courierId); // Update courier in DB
            refreshTable(); // Refresh table after update
        });
    }
    
    // Mark selected order as delivered
    private void markAsDelivered() {
        OrderTableItem selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Select an order first!", ButtonType.OK);
            alert.showAndWait();
            return;
        }
        
        // Only orders in progress can be marked delivered
        if (!"IN_PROGRESS".equals(selected.getStatus())) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Only orders in progress can be marked delivered!", ButtonType.OK);
            alert.showAndWait();
            return;
        }
        
        // Update order status in database
        boolean success = OrderHeaderDA.editDeliveryStatus(selected.getId(), "DELIVERED");
        if (success) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Order marked as delivered!", ButtonType.OK);
            alert.showAndWait();
            refreshTable(); // Reload table to show updated status
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to update order status.", ButtonType.OK);
            alert.showAndWait();
        }
    }

}
