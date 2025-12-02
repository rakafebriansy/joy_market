package joy_market.windows;

import javafx.collections.*;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import joy_market.dataAccess.ProductDA;
import joy_market.models.Product;

// This class manages the product management window for the admin
public class AdminProductWindow {
    // Table to display the list of products
    private TableView<Product> table = new TableView<>();
    // Observable list to store product data for the table
    private ObservableList<Product> products;
    
    // Returns the main view (UI layout) for the product management section
    public Pane getView() {
        VBox root = new VBox(10);
        root.setPadding(new Insets(20));
        
        // Title label
        Label lblTitle = new Label("Manage Products");
        lblTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        
        // Table column for product name
        TableColumn<Product, String> colName = new TableColumn<>("Name");
        colName.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getName()));
        
        // Table column for product price
        TableColumn<Product, Number> colPrice = new TableColumn<>("Price");
        colPrice.setCellValueFactory(c -> new javafx.beans.property.SimpleLongProperty(c.getValue().getPrice()));
        
        // Table column for product stock
        TableColumn<Product, Number> colStock = new TableColumn<>("Stock");
        colStock.setCellValueFactory(c -> new javafx.beans.property.SimpleIntegerProperty(c.getValue().getStock()));
        
        // Add all columns to the table
        table.getColumns().addAll(colName, colPrice, colStock);
        // Load product data into the table
        refreshTable();
        
        // Buttons for product actions
        Button btnAdd = new Button("Add");
        Button btnEdit = new Button("Edit");
        Button btnDelete = new Button("Delete");
        
        // Arrange buttons horizontally
        HBox buttonBar = new HBox(10, btnAdd, btnEdit, btnDelete);
        
        // Add button action — open form to add a new product
        btnAdd.setOnAction(e -> showProductForm(null));
        // Edit button action — open form to edit the selected product
        btnEdit.setOnAction(e -> {
            Product selected = table.getSelectionModel().getSelectedItem();
            if (selected != null) showProductForm(selected);
        });
        // Delete button action — delete the selected product
        btnDelete.setOnAction(e -> {
            Product selected = table.getSelectionModel().getSelectedItem();
            if (selected != null) {
                if (ProductDA.deleteProduct(selected.getId())) refreshTable();
            }
        });
        
        // Add components to layout
        root.getChildren().addAll(lblTitle, table, buttonBar);
        return root;
    }
    
    // Reloads product data from the database into the table
    private void refreshTable() {
        products = FXCollections.observableArrayList(ProductDA.getAllProducts());
        table.setItems(products);
    }
    
    // Displays a form for adding or editing a product
    private void showProductForm(Product existingProduct) {
        Stage dialog = new Stage();
        VBox form = new VBox(10);
        form.setPadding(new Insets(15));
        
        // Input fields for product details
        TextField txtName = new TextField(existingProduct != null ? existingProduct.getName() : "");
        TextField txtPrice = new TextField(existingProduct != null ? String.valueOf(existingProduct.getPrice()) : "");
        TextField txtStock = new TextField(existingProduct != null ? String.valueOf(existingProduct.getStock()) : "");
        TextArea txtDesc = new TextArea(existingProduct != null ? existingProduct.getDescription() : "");
        CheckBox chkFresh = new CheckBox("Is Fresh");
        if (existingProduct != null) chkFresh.setSelected(existingProduct.isFresh());
        
        // Save button to save or update product data
        Button btnSave = new Button("Save");
        btnSave.setOnAction(e -> {
        	Product p;
        	 try {
                // If editing existing product, update its values
        		 if (existingProduct != null) {
        			    existingProduct.setName(txtName.getText().trim());
        			    existingProduct.setPrice(Long.parseLong(txtPrice.getText().trim()));
        			    existingProduct.setStock(Integer.parseInt(txtStock.getText().trim()));
        			    existingProduct.setFresh(chkFresh.isSelected());
        			    existingProduct.setDescription(txtDesc.getText().trim());
        			    p = existingProduct;
        			} 
                    // If adding new product, create a new instance
                    else {
        			    p = new Product(
        			        txtName.getText().trim(),
        			        Long.parseLong(txtPrice.getText().trim()),
        			        Integer.parseInt(txtStock.getText().trim()),
        			        chkFresh.isSelected(),
        			        txtDesc.getText().trim()
        			    );
        			}

                
                // Insert or update the product in the database
                 boolean success = (p.getId() == null)
                         ? ProductDA.insertProduct(p)
                         : ProductDA.updateProduct(p);
                
                // If successful, refresh table and close dialog
                 if (success) {
                     refreshTable();
                     dialog.close();
                 } else {
                    // Show error message if failed
                     Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to save product!", ButtonType.OK);
                     alert.showAndWait();
                 }
             } catch (Exception ex) {
                 ex.printStackTrace();
                 Alert alert = new Alert(Alert.AlertType.ERROR, "Error: " + ex.getMessage(), ButtonType.OK);
                 alert.showAndWait();
             }        });
        
        // Add input fields and button to form layout
        form.getChildren().addAll(new Label("Name"), txtName,
                new Label("Price"), txtPrice,
                new Label("Stock"), txtStock,
                chkFresh, new Label("Description"), txtDesc, btnSave);
        
        // Set up dialog properties and display
        dialog.setScene(new Scene(form, 300, 400));
        dialog.setTitle(existingProduct  == null ? "Add Product" : "Edit Product");
        dialog.show();
    }
}
