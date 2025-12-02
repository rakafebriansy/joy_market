package joy_market.windows;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.*;
import javafx.geometry.Insets;
import joy_market.dataAccess.OrderHeaderDA;
import joy_market.dataAccess.CourierDA;
import joy_market.handlers.ProfileHandler;
import joy_market.models.Order;
import joy_market.models.Courier;
import joy_market.widgets.OrderTableItem;

import java.util.List;

// This class creates the main dashboard window for the Courier role
public class CourierWindow {

    private Courier courier; // Stores courier information
    private ProfileHandler profileHandler = new ProfileHandler(); // Handles profile updates
    
    // Constructor to initialize the courier object
    public CourierWindow(Courier courier) {
        this.courier = courier;
    }
    
    // Displays the courier window
    public void show(Stage stage) {
        TabPane tabPane = new TabPane();  // Container for multiple tabs
        
        // --- Orders Tab ---
        Tab tabOrders = new Tab("Orders");
        VBox ordersRoot = new VBox(10);
        ordersRoot.setPadding(new Insets(10));
        
        // Table for displaying courier orders
        TableView<OrderTableItem> tblOrders = new TableView<>();
        
        // Column for Order ID
        TableColumn<OrderTableItem, String> colId = new TableColumn<>("Order ID");
        colId.setCellValueFactory(c -> 
            new SimpleStringProperty(String.valueOf(c.getValue().getId()))
        );
        
        // Column for Customer Name
        TableColumn<OrderTableItem, String> colCustomer = new TableColumn<>("Customer");
        colCustomer.setCellValueFactory(c -> c.getValue().customerNameProperty());
        
        // Column for Order Status
        TableColumn<OrderTableItem, String> colStatus = new TableColumn<>("Status");
        colStatus.setCellValueFactory(c -> c.getValue().statusProperty());
        
        // Column for Total Price
        TableColumn<OrderTableItem, String> colTotal = new TableColumn<>("Total");
        colTotal.setCellValueFactory(c -> c.getValue().totalPriceProperty());
        
        // Add all columns to the table
        tblOrders.getColumns().addAll(colId, colCustomer, colStatus, colTotal);
        
        // Button to mark an order as delivered
        Button btnMarkDelivered = new Button("Mark Delivered");
        btnMarkDelivered.setOnAction(e -> {
            OrderTableItem selected = tblOrders.getSelectionModel().getSelectedItem();
            if (selected == null) return;
            
            // Only orders with status "IN_PROGRESS" can be marked as delivered
            if ("IN_PROGRESS".equals(selected.getStatus())) {
                boolean success = OrderHeaderDA.editDeliveryStatus(selected.getId(), "DELIVERED");
                if (success) {
                    refreshOrders(tblOrders);
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Order marked as delivered!", ButtonType.OK);
                    alert.showAndWait();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to update order status!", ButtonType.OK);
                    alert.showAndWait();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Only IN_PROGRESS orders can be marked delivered!", ButtonType.OK);
                alert.showAndWait();
            }
        });
        
        // Add the table and button to the layout
        ordersRoot.getChildren().addAll(tblOrders, btnMarkDelivered);
        tabOrders.setContent(ordersRoot);
        
        // --- Profile Tab ---
        Tab tabProfile = new Tab("Edit Profile");
        VBox profileRoot = new VBox(10);
        profileRoot.setPadding(new Insets(10));
        
        // Email input field
        TextField txtEmail = new TextField(courier.getEmail());
        // Password input fields
        PasswordField txtNewPass = new PasswordField();
        txtNewPass.setPromptText("New Password");
        PasswordField txtConfirmPass = new PasswordField();
        txtConfirmPass.setPromptText("Confirm Password");
        
        // Save profile button
        Button btnSaveProfile = new Button("Save Profile");
        btnSaveProfile.setOnAction(e -> {
            courier.setEmail(txtEmail.getText().trim());
            
            // Handle profile update using the ProfileHandler
            String res = profileHandler.handleUpdateProfile(courier, "COURIER", txtNewPass.getText(), txtConfirmPass.getText());
            Alert alert = new Alert(Alert.AlertType.INFORMATION, res, ButtonType.OK);
            alert.showAndWait();
        });
        
        // Add all profile elements to the layout
        profileRoot.getChildren().addAll(new Label("Email:"), txtEmail,
                new Label("New Password:"), txtNewPass,
                new Label("Confirm Password:"), txtConfirmPass,
                btnSaveProfile);

        tabProfile.setContent(profileRoot);
        
        // Add both tabs to the TabPane
        tabPane.getTabs().addAll(tabOrders, tabProfile);
        
        // Set up the scene and show the window
        Scene scene = new Scene(tabPane, 800, 600);
        stage.setScene(scene);
        stage.setTitle("Courier Dashboard");
        stage.show();
        
        // Load courier's orders into the table
        refreshOrders(tblOrders);
    }
    
    // Refresh the table with the latest orders from the database
    private void refreshOrders(TableView<OrderTableItem> tblOrders) {
        List<Order> orders = OrderHeaderDA.getOrdersByCourierId(courier.getId());
        ObservableList<OrderTableItem> items = FXCollections.observableArrayList();
        for (Order o : orders) {
            items.add(new OrderTableItem(o));
        }
        tblOrders.setItems(items);
    }
}
