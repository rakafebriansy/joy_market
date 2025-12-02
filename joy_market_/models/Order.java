package joy_market.models;

public class Order {
    private int id;
    private int userId;
    private boolean promo;
    private long totalPrice;
    private String status;
    private Integer courierId;

    public Order(int id, int userId, boolean promo, long totalPrice, String status, Integer courierId) {
        this.id = id;
        this.userId = userId;
        this.promo = promo;
        this.totalPrice = totalPrice;
        this.status = status;
        this.courierId = courierId;
    }

    public int getId() { return id; }
    public int getUserId() { return userId; }
    public boolean isPromo() { return promo; }
    public long getTotalPrice() { return totalPrice; }
    public String getStatus() { return status; }
    public Integer getCourierId() { return courierId; }

    public void setStatus(String status) { this.status = status; }
    public void setCourierId(Integer courierId) { this.courierId = courierId; }
}
