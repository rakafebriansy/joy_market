package joy_market.windows;

import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.collections.*;
import javafx.geometry.Insets;
import joy_market.models.Customer;
import joy_market.models.Order;
import joy_market.widgets.OrderHistoryTableItem;
import joy_market.dataAccess.OrderHeaderDA;

import java.util.List;

public class OrderHistoryWindow {

    private Customer user; // The customer whose order history will be displayed
    private TableView<OrderHistoryTableItem> table; // Table to show order history

    public OrderHistoryWindow(Customer user) {
        this.user = user;  // Initialize with the current customer
    }
    
    // Create and return the main view
    public Pane getView() {
        VBox root = new VBox(10); // Vertical layout with spacing 10
        root.setPadding(new Insets(10)); // Padding around the layout
        
        // Title label
        Label lblTitle = new Label("Order History");
        lblTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        this.table = new TableView<>(); // Initialize table
        
        // Define columns for the table
        TableColumn<OrderHistoryTableItem, Number> colId = new TableColumn<>("Order ID");
        colId.setCellValueFactory(c -> c.getValue().idProperty());

        TableColumn<OrderHistoryTableItem, String> colStatus = new TableColumn<>("Status");
        colStatus.setCellValueFactory(c -> c.getValue().statusProperty());

        TableColumn<OrderHistoryTableItem, Number> colTotal = new TableColumn<>("Total Price");
        colTotal.setCellValueFactory(c -> c.getValue().totalPriceProperty());
        
        // Add columns to the table
        table.getColumns().addAll(colId, colStatus, colTotal);
        
        // Fetch orders for the current customer from database
        List<Order> orders = OrderHeaderDA.getOrdersByUserId(user.getId());
        ObservableList<OrderHistoryTableItem> items = FXCollections.observableArrayList();
        // Convert Order objects to OrderHistoryTableItem for the table
        for (Order o : orders) {
            items.add(new OrderHistoryTableItem(
                    o.getId(),
                    o.getStatus(),
                    o.getTotalPrice()
            ));
        }
        table.setItems(items); // Set data to table
        
        // Add title and table to the layout
        root.getChildren().addAll(lblTitle, table);
        return root; // Return the layout
    }
    
    // Refresh the table data with latest orders
    public void refresh() {
        List<Order> orders = OrderHeaderDA.getOrdersByUserId(user.getId());
        ObservableList<OrderHistoryTableItem> items = FXCollections.observableArrayList();
        for (Order o : orders) {
            items.add(new OrderHistoryTableItem(o.getId(), o.getStatus(), o.getTotalPrice()));
        }
        table.setItems(items); // Update table with new data
    }


}
