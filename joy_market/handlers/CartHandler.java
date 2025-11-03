package joy_market.handlers;

import joy_market.dataAccess.CartDA;

public class CartHandler {
    public static boolean clearCart(int userId) {
        return CartDA.clearCart(userId);
    }

    public static boolean removeCartItem(int cartItemId) {
        return CartDA.removeCartItem(cartItemId);
    }
}
