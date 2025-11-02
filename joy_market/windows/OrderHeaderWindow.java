package joy_market.windows;

import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.collections.*;
import javafx.geometry.Insets;
import joy_market.widgets.OrderTableItem;

public class OrderHeaderWindow {
    public BorderPane getView() {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        Label lblTitle = new Label("Orders Management");
        lblTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        TableView<OrderTableItem> table = new TableView<>();

        TableColumn<OrderTableItem, String> colCode = new TableColumn<>("Order Code");
        colCode.setCellValueFactory(c -> c.getValue().codeProperty());

        TableColumn<OrderTableItem, String> colStatus = new TableColumn<>("Status");
        colStatus.setCellValueFactory(c -> c.getValue().statusProperty());

        TableColumn<OrderTableItem, String> colCustomer = new TableColumn<>("Customer");
        colCustomer.setCellValueFactory(c -> c.getValue().customerProperty());

        TableColumn<OrderTableItem, String> colTotal = new TableColumn<>("Total");
        colTotal.setCellValueFactory(c -> c.getValue().totalProperty());

        table.getColumns().addAll(colCode, colStatus, colCustomer, colTotal);

        // Dummy data
        ObservableList<OrderTableItem> data = FXCollections.observableArrayList(
            new OrderTableItem("ORD001", "PENDING", "Raka Febrian", "120000"),
            new OrderTableItem("ORD002", "PAID", "Dina Putri", "180000"),
            new OrderTableItem("ORD003", "DELIVERED", "Rafi Akbar", "220000")
        );
        table.setItems(data);

        root.setTop(lblTitle);
        root.setCenter(table);
        return root;
    }
}
