package joy_market.windows;

import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.collections.*;
import javafx.geometry.Insets;
import joy_market.models.Customer;
import joy_market.models.Order;
import joy_market.widgets.OrderHistoryTableItem;
import joy_market.dataAccess.OrderDA;

import java.util.List;

public class OrderHistoryWindow {

    private Customer user;
    private TableView<OrderHistoryTableItem> table;

    public OrderHistoryWindow(Customer user) {
        this.user = user;
    }

    public Pane getView() {
        VBox root = new VBox(10);
        root.setPadding(new Insets(10));

        Label lblTitle = new Label("Order History");
        lblTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        this.table = new TableView<>();

        TableColumn<OrderHistoryTableItem, Number> colId = new TableColumn<>("Order ID");
        colId.setCellValueFactory(c -> c.getValue().idProperty());

        TableColumn<OrderHistoryTableItem, String> colStatus = new TableColumn<>("Status");
        colStatus.setCellValueFactory(c -> c.getValue().statusProperty());

        TableColumn<OrderHistoryTableItem, Number> colTotal = new TableColumn<>("Total Price");
        colTotal.setCellValueFactory(c -> c.getValue().totalPriceProperty());

        table.getColumns().addAll(colId, colStatus, colTotal);

        List<Order> orders = OrderDA.getOrdersByUserId(user.getId());
        ObservableList<OrderHistoryTableItem> items = FXCollections.observableArrayList();
        for (Order o : orders) {
            items.add(new OrderHistoryTableItem(
                    o.getId(),
                    o.getStatus(),
                    o.getTotalPrice()
            ));
        }
        table.setItems(items);

        root.getChildren().addAll(lblTitle, table);
        return root;
    }
    
    public void refresh() {
        List<Order> orders = OrderDA.getOrdersByUserId(user.getId());
        ObservableList<OrderHistoryTableItem> items = FXCollections.observableArrayList();
        for (Order o : orders) {
            items.add(new OrderHistoryTableItem(o.getId(), o.getStatus(), o.getTotalPrice()));
        }
        table.setItems(items);
    }


}
