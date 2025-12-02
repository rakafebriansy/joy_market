package joy_market.handlers;

import joy_market.dataAccess.CartItemDA;

public class CartItemHandler {
    public static boolean clearCart(int userId) {
        return CartItemDA.clearCart(userId);
    }

    public static boolean deleteCartItem(int cartItemId) {
        return CartItemDA.deleteCartItem(cartItemId);
    }
}
