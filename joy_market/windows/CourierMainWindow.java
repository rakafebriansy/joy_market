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

public class CourierWindow {

    private Courier courier;
    private ProfileHandler profileHandler = new ProfileHandler();

    public CourierWindow(Courier courier) {
        this.courier = courier;
    }

    public void show(Stage stage) {
        TabPane tabPane = new TabPane();

        Tab tabOrders = new Tab("Orders");
        VBox ordersRoot = new VBox(10);
        ordersRoot.setPadding(new Insets(10));

        TableView<OrderTableItem> tblOrders = new TableView<>();

        TableColumn<OrderTableItem, String> colId = new TableColumn<>("Order ID");
        colId.setCellValueFactory(c -> 
            new SimpleStringProperty(String.valueOf(c.getValue().getId()))
        );

        TableColumn<OrderTableItem, String> colCustomer = new TableColumn<>("Customer");
        colCustomer.setCellValueFactory(c -> c.getValue().customerNameProperty());

        TableColumn<OrderTableItem, String> colStatus = new TableColumn<>("Status");
        colStatus.setCellValueFactory(c -> c.getValue().statusProperty());

        TableColumn<OrderTableItem, String> colTotal = new TableColumn<>("Total");
        colTotal.setCellValueFactory(c -> c.getValue().totalPriceProperty());

        tblOrders.getColumns().addAll(colId, colCustomer, colStatus, colTotal);

        Button btnMarkDelivered = new Button("Mark Delivered");
        btnMarkDelivered.setOnAction(e -> {
            OrderTableItem selected = tblOrders.getSelectionModel().getSelectedItem();
            if (selected == null) return;

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

        ordersRoot.getChildren().addAll(tblOrders, btnMarkDelivered);
        tabOrders.setContent(ordersRoot);

        Tab tabProfile = new Tab("Edit Profile");
        VBox profileRoot = new VBox(10);
        profileRoot.setPadding(new Insets(10));

        TextField txtEmail = new TextField(courier.getEmail());
        PasswordField txtNewPass = new PasswordField();
        txtNewPass.setPromptText("New Password");
        PasswordField txtConfirmPass = new PasswordField();
        txtConfirmPass.setPromptText("Confirm Password");

        Button btnSaveProfile = new Button("Save Profile");
        btnSaveProfile.setOnAction(e -> {
            courier.setEmail(txtEmail.getText().trim());

            String res = profileHandler.handleUpdateProfile(courier, "COURIER", txtNewPass.getText(), txtConfirmPass.getText());
            Alert alert = new Alert(Alert.AlertType.INFORMATION, res, ButtonType.OK);
            alert.showAndWait();
        });

        profileRoot.getChildren().addAll(new Label("Email:"), txtEmail,
                new Label("New Password:"), txtNewPass,
                new Label("Confirm Password:"), txtConfirmPass,
                btnSaveProfile);

        tabProfile.setContent(profileRoot);

        tabPane.getTabs().addAll(tabOrders, tabProfile);

        Scene scene = new Scene(tabPane, 800, 600);
        stage.setScene(scene);
        stage.setTitle("Courier Dashboard");
        stage.show();

        refreshOrders(tblOrders);
    }

    private void refreshOrders(TableView<OrderTableItem> tblOrders) {
        List<Order> orders = OrderHeaderDA.getOrdersByCourierId(courier.getId());
        ObservableList<OrderTableItem> items = FXCollections.observableArrayList();
        for (Order o : orders) {
            items.add(new OrderTableItem(o));
        }
        tblOrders.setItems(items);
    }
}
