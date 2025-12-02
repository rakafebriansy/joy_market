package joy_market.handlers;

import joy_market.dataAccess.CartItemDA;

public class CartItemHandler {
    // Remove all cart items belonging to a specific user
    public static boolean clearCart(int userId) {
        // Call the data access method to clear the user's cart
        return CartItemDA.clearCart(userId);
    }
    
    // Delete a specific cart item by its ID
    public static boolean deleteCartItem(int cartItemId) {
        // Call the data access method to delete one cart item
        return CartItemDA.deleteCartItem(cartItemId);
    }
}
