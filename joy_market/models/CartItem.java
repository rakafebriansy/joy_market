package joy_market.models;

public class CartItem {
    // Declare variables for cart item details
    private int id; // Unique ID for the cart item
    private int cartId; // ID of the cart this item belongs to
    private int productId; // ID of the product in the cart
    private int count; // Quantity of the product in the cart
    
    // Constructor to initialize a cart item with all fields
    public CartItem(int id, int cartId, int productId, int count) {
        this.id = id;
        this.cartId = cartId;
        this.productId = productId;
        this.count = count;
    }
    
    // Getter for item ID
    public int getId() {
        return id;
    }
    
    // Getter for cart ID
    public int getCartId() {
        return cartId;
    }
    
    // Getter for product ID
    public int getProductId() {
        return productId;
    }

    // Getter for product quantity
    public int getCount() {
        return count;
    }
    
    // Setter for item ID
    public void setId(int id) {
        this.id = id;
    }
    
    // Setter for cart ID
    public void setCartId(int cartId) {
        this.cartId = cartId;
    }
    
    // Setter for product ID
    public void setProductId(int productId) {
        this.productId = productId;
    }
    
    // Setter for product quantity
    public void setCount(int count) {
        this.count = count;
    }
}
