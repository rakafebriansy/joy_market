package joy_market.handlers;

import joy_market.dataAccess.ProductDA;
import joy_market.models.Product;
import joy_market.utils.Validator;

import java.util.List;

public class ProductHandler {

    // Add a new product after validating inputs
    public String handleAddProduct(String name, int categoryId, String priceStr, String stockStr, boolean isFresh, String description) {
        // Check if the product name is empty
        if (Validator.isEmpty(name)) return "Product name cannot be empty!";
        // Validate that price is a numeric value
        if (!Validator.isNumeric(priceStr)) return "Invalid price!";
        // Validate that stock is a numeric value
        if (!Validator.isNumeric(stockStr)) return "Invalid stock!";

        // Convert price and stock from String to numeric types
        long price = Long.parseLong(priceStr);
        int stock = Integer.parseInt(stockStr);

        // Create a new Product object
        Product product = new Product(name, price, stock, isFresh, description);

        // Insert product into database and return message based on result
        return ProductDA.insertProduct(product)
                ? "Product added successfully!"
                : "Failed to add product!";
    }

    // Edit an existing product
    public String editProduct(Product product) {
        return ProductDA.updateProduct(product)
                ? "Product updated successfully!"
                : "Failed to update product!";
    }
    
    // Delete a product by ID
    public boolean handleDeleteProduct(int id) {
        return ProductDA.deleteProduct(id);
    }
    
    // Get all products from database
    public List<Product> handleGetAll() {
        return ProductDA.getAllProducts();
    }
}
