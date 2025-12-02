package joy_market.models;

public class Product {
    private Integer id;
    private String name;
    private long price;
    private int stock;
    private boolean isFresh;
    private String description;

    public Product(int id, String name, long price, int stock, boolean isFresh, String description) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.isFresh = isFresh;
        this.description = description;
    }
    
    public Product(String name, long price, int stock, boolean isFresh, String description) {
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.isFresh = isFresh;
        this.description = description;
    }

    public Integer getId() { return id; }
    public String getName() { return name; }
    public long getPrice() { return price; }
    public int getStock() { return stock; }
    public boolean isFresh() { return isFresh; }
    public String getDescription() { return description; }

    public void setName(String name) { this.name = name; }
    public void setPrice(long price) { this.price = price; }
    public void setStock(int stock) { this.stock = stock; }
    public void setFresh(boolean fresh) { isFresh = fresh; }
    public void setDescription(String description) { this.description = description; }
}
