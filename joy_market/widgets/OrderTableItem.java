package joy_market.widgets;

import javafx.beans.property.*;
import joy_market.models.Order;
import joy_market.dataAccess.CustomerDA;

public class OrderTableItem {
    private IntegerProperty id;
    private StringProperty customerName;
    private StringProperty status;
    private LongProperty totalPrice;
    private BooleanProperty promo;

    private IntegerProperty courierId;

    public OrderTableItem(Order order) {
        this.id = new SimpleIntegerProperty(order.getId());
        this.customerName = new SimpleStringProperty(CustomerDA.getUserById(order.getUserId()).getFullName());
        this.status = new SimpleStringProperty(order.getStatus());
        this.totalPrice = new SimpleLongProperty(order.getTotalPrice());
        this.promo = new SimpleBooleanProperty(order.isPromo());
        this.courierId = new SimpleIntegerProperty(order.getCourierId() != null ? order.getCourierId() : 0);
    }
    
    public IntegerProperty idProperty() { return id; }
    public StringProperty customerNameProperty() { return customerName; }
    public StringProperty statusProperty() { return status; }
    public StringProperty totalPriceProperty() { return new SimpleStringProperty(String.valueOf(totalPrice.get()));}

    public StringProperty promoProperty() { return new SimpleStringProperty(promo.get() ? "Yes" : "No"); }

    public int getId() { return id.get(); }
    public int getCourierId() { return courierId.get(); }
    public String getStatus() { return status.get(); }

    public void setStatus(String newStatus) { status.set(newStatus); }
    public void setCourierId(int id) { courierId.set(id); }
}
