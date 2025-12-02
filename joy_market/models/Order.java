package joy_market.models;

// Represents an order made by a customer in the system
public class Order {
    // Unique identifier for the order
    private int id;
    // ID of the user who made the order
    private int userId;
    // Indicates whether a promo was applied to the order
    private boolean promo;
    // Total price of the order
    private long totalPrice;
    // Current status of the order (e.g., "Pending", "Delivered")
    private String status;
    // ID of the courier assigned to deliver the order (can be null if not assigned yet)
    private Integer courierId;

    // Constructor to initialize all order fields
    public Order(int id, int userId, boolean promo, long totalPrice, String status, Integer courierId) {
        this.id = id;
        this.userId = userId;
        this.promo = promo;
        this.totalPrice = totalPrice;
        this.status = status;
        this.courierId = courierId;
    }
    
    // Getter for order ID
    public int getId() { return id; }
    // Getter for user ID
    public int getUserId() { return userId; }
    // Getter to check if promo was applied
    public boolean isPromo() { return promo; }
    // Getter for total price
    public long getTotalPrice() { return totalPrice; }
    // Getter for order status
    public String getStatus() { return status; }
    // Getter for courier ID
    public Integer getCourierId() { return courierId; }
    
    // Setter to update order status
    public void setStatus(String status) { this.status = status; }
    // Setter to assign or update courier ID
    public void setCourierId(Integer courierId) { this.courierId = courierId; }
}
