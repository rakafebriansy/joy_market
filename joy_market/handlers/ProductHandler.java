package joy_market.handlers;

import joy_market.dataAccess.ProductDA;
import joy_market.models.Product;
import joy_market.utils.Validator;

import java.util.List;

public class ProductHandler {

    public String handleAddProduct(String name, int categoryId, String priceStr, String stockStr, boolean isFresh, String description) {
        if (Validator.isEmpty(name)) return "Product name cannot be empty!";
        if (!Validator.isNumeric(priceStr)) return "Invalid price!";
        if (!Validator.isNumeric(stockStr)) return "Invalid stock!";

        long price = Long.parseLong(priceStr);
        int stock = Integer.parseInt(stockStr);

        Product product = new Product(name, price, stock, isFresh, description);

        return ProductDA.insertProduct(product)
                ? "Product added successfully!"
                : "Failed to add product!";
    }

    public String handleUpdateProduct(Product product) {
        return ProductDA.updateProduct(product)
                ? "Product updated successfully!"
                : "Failed to update product!";
    }

    public boolean handleDeleteProduct(int id) {
        return ProductDA.deleteProduct(id);
    }

    public List<Product> handleGetAll() {
        return ProductDA.getAllProducts();
    }
}
