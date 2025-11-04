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
import joy_market.dataAccess.OrderDA;
import joy_market.dataAccess.CustomerDA;
import joy_market.models.Order;
import joy_market.models.Courier;
import joy_market.widgets.OrderTableItem;
import javafx.beans.property.*;

public class OrderHeaderWindow {

    private TableView<OrderTableItem> table;

    public BorderPane getView() {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        Label lblTitle = new Label("Orders Management");
        lblTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        table = new TableView<>();
        table.setPrefHeight(400);

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

        table.getColumns().addAll(colId, colCustomer, colStatus, colTotal, colPromo);

        Button btnAssign = new Button("Assign Courier");
        btnAssign.setOnAction(e -> assignCourier());
        
        Button btnDelivered = new Button("Mark as Delivered");
        btnDelivered.setOnAction(e -> markAsDelivered());

        VBox topBox = new VBox(10, lblTitle, btnAssign, btnDelivered);
        topBox.setPadding(new Insets(0, 0, 10, 0));


        root.setTop(topBox);
        root.setCenter(table);

        refreshTable();

        return root;
    }

    private void refreshTable() {
        List<Order> orders = OrderDA.getAllOrders();
        ObservableList<OrderTableItem> items = FXCollections.observableArrayList();

        for (Order o : orders) {
        	items.add(new OrderTableItem(o));
        }

        table.setItems(items);
    }

    private void assignCourier() {
        OrderTableItem selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Select an order first!", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        if (!"PENDING".equals(selected.getStatus())) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Order is not pending!", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        List<Courier> couriers = CourierDA.getAllCouriers();
        Map<String, Integer> nameToId = couriers.stream()
            .collect(Collectors.toMap(Courier::getEmail, Courier::getId));

        ChoiceDialog<String> dlg = new ChoiceDialog<>();
        dlg.getItems().addAll(nameToId.keySet());
        dlg.setTitle("Assign Courier");

        Optional<String> result = dlg.showAndWait();
        result.ifPresent(name -> {
            int courierId = nameToId.get(name);
            OrderDA.assignCourier(selected.getId(), courierId);
            refreshTable();
        });
    }
    
    private void markAsDelivered() {
        OrderTableItem selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Select an order first!", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        if (!"IN_PROGRESS".equals(selected.getStatus())) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Only orders in progress can be marked delivered!", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        boolean success = OrderDA.updateOrderStatus(selected.getId(), "DELIVERED");
        if (success) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Order marked as delivered!", ButtonType.OK);
            alert.showAndWait();
            refreshTable();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to update order status.", ButtonType.OK);
            alert.showAndWait();
        }
    }

}
