package joy_market.widgets;

import javafx.beans.property.*;

public class OrderHistoryTableItem {
    private IntegerProperty id;
    private StringProperty status;
    private LongProperty totalPrice;

    public OrderHistoryTableItem(int id, String status, long totalPrice) {
        this.id = new SimpleIntegerProperty(id);
        this.status = new SimpleStringProperty(status);
        this.totalPrice = new SimpleLongProperty(totalPrice);
    }

    public int getId() { return id.get(); }
    public IntegerProperty idProperty() { return id; }

    public String getStatus() { return status.get(); }
    public StringProperty statusProperty() { return status; }

    public long getTotalPrice() { return totalPrice.get(); }
    public LongProperty totalPriceProperty() { return totalPrice; }
}
