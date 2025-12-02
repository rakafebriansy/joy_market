package joy_market.models;

public class CartItem {
    private int id;
    private int cartId;
    private int productId;
    private int count;

    public CartItem(int id, int cartId, int productId, int count) {
        this.id = id;
        this.cartId = cartId;
        this.productId = productId;
        this.count = count;
    }

    public int getId() {
        return id;
    }

    public int getCartId() {
        return cartId;
    }

    public int getProductId() {
        return productId;
    }

    public int getCount() {
        return count;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
