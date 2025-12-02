package joy_market.models;


// Represents a product sold in the Joy Market application
public class Product {
    // Unique identifier for the product (can be null for new products)
    private Integer id;
    // Name of the product
    private String name;
    // Price of the product
    private long price;
    // Available stock quantity
    private int stock;
    // Indicates whether the product is fresh (e.g., perishable goods)
    private boolean isFresh;
    // Description of the product
    private String description;
    
    // Constructor with ID (used when loading existing products from the database)
    public Product(int id, String name, long price, int stock, boolean isFresh, String description) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.isFresh = isFresh;
        this.description = description;
    }
    
    // Constructor without ID (used when adding a new product)
    public Product(String name, long price, int stock, boolean isFresh, String description) {
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.isFresh = isFresh;
        this.description = description;
    }
    
    // Getter for product ID
    public Integer getId() { return id; }
    // Getter for product name
    public String getName() { return name; }
    // Getter for product price
    public long getPrice() { return price; }
    // Getter for product stock
    public int getStock() { return stock; }
    // Getter to check if the product is fresh
    public boolean isFresh() { return isFresh; }
    // Getter for product description
    public String getDescription() { return description; }
    
    // Setter to update the product name
    public void setName(String name) { this.name = name; }
    // Setter to update the product price
    public void setPrice(long price) { this.price = price; }
    // Setter to update the product stock
    public void setStock(int stock) { this.stock = stock; }
    // Setter to update freshness status
    public void setFresh(boolean fresh) { isFresh = fresh; }
    // Setter to update the product description
    public void setDescription(String description) { this.description = description; }
}
