package joy_market.widgets;

import javafx.beans.property.SimpleStringProperty;

public class OrderTableItem {
    private SimpleStringProperty code, status, customer, total;

    public OrderTableItem(String code, String status, String customer, String total) {
        this.code = new SimpleStringProperty(code);
        this.status = new SimpleStringProperty(status);
        this.customer = new SimpleStringProperty(customer);
        this.total = new SimpleStringProperty(total);
    }

    public SimpleStringProperty codeProperty() { return code; }
    public SimpleStringProperty statusProperty() { return status; }
    public SimpleStringProperty customerProperty() { return customer; }
    public SimpleStringProperty totalProperty() { return total; }
}
