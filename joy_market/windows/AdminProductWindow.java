package joy_market.windows;

import javafx.collections.*;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import joy_market.dataAccess.ProductDA;
import joy_market.models.Product;

public class AdminProductWindow {
    private TableView<Product> table = new TableView<>();
    private ObservableList<Product> products;

    public Pane getView() {
        VBox root = new VBox(10);
        root.setPadding(new Insets(20));

        Label lblTitle = new Label("Manage Products");
        lblTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        TableColumn<Product, String> colName = new TableColumn<>("Name");
        colName.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getName()));

        TableColumn<Product, Number> colPrice = new TableColumn<>("Price");
        colPrice.setCellValueFactory(c -> new javafx.beans.property.SimpleLongProperty(c.getValue().getPrice()));

        TableColumn<Product, Number> colStock = new TableColumn<>("Stock");
        colStock.setCellValueFactory(c -> new javafx.beans.property.SimpleIntegerProperty(c.getValue().getStock()));

        table.getColumns().addAll(colName, colPrice, colStock);
        refreshTable();

        Button btnAdd = new Button("Add");
        Button btnEdit = new Button("Edit");
        Button btnDelete = new Button("Delete");

        HBox buttonBar = new HBox(10, btnAdd, btnEdit, btnDelete);

        btnAdd.setOnAction(e -> showProductForm(null));
        btnEdit.setOnAction(e -> {
            Product selected = table.getSelectionModel().getSelectedItem();
            if (selected != null) showProductForm(selected);
        });
        btnDelete.setOnAction(e -> {
            Product selected = table.getSelectionModel().getSelectedItem();
            if (selected != null) {
                if (ProductDA.deleteProduct(selected.getId())) refreshTable();
            }
        });

        root.getChildren().addAll(lblTitle, table, buttonBar);
        return root;
    }

    private void refreshTable() {
        products = FXCollections.observableArrayList(ProductDA.getAllProducts());
        table.setItems(products);
    }

    private void showProductForm(Product existingProduct) {
        Stage dialog = new Stage();
        VBox form = new VBox(10);
        form.setPadding(new Insets(15));

        TextField txtName = new TextField(existingProduct != null ? existingProduct.getName() : "");
        TextField txtPrice = new TextField(existingProduct != null ? String.valueOf(existingProduct.getPrice()) : "");
        TextField txtStock = new TextField(existingProduct != null ? String.valueOf(existingProduct.getStock()) : "");
        TextArea txtDesc = new TextArea(existingProduct != null ? existingProduct.getDescription() : "");
        CheckBox chkFresh = new CheckBox("Is Fresh");
        if (existingProduct != null) chkFresh.setSelected(existingProduct.isFresh());

        Button btnSave = new Button("Save");
        btnSave.setOnAction(e -> {
        	Product p;
        	 try {
        		 if (existingProduct != null) {
        			    existingProduct.setName(txtName.getText().trim());
        			    existingProduct.setPrice(Long.parseLong(txtPrice.getText().trim()));
        			    existingProduct.setStock(Integer.parseInt(txtStock.getText().trim()));
        			    existingProduct.setFresh(chkFresh.isSelected());
        			    existingProduct.setDescription(txtDesc.getText().trim());
        			    p = existingProduct;
        			} else {
        			    p = new Product(
        			        txtName.getText().trim(),
        			        Long.parseLong(txtPrice.getText().trim()),
        			        Integer.parseInt(txtStock.getText().trim()),
        			        chkFresh.isSelected(),
        			        txtDesc.getText().trim()
        			    );
        			}

                

                 boolean success = (p.getId() == null)
                         ? ProductDA.insertProduct(p)
                         : ProductDA.updateProduct(p);

                 if (success) {
                     refreshTable();
                     dialog.close();
                 } else {
                     Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to save product!", ButtonType.OK);
                     alert.showAndWait();
                 }
             } catch (Exception ex) {
                 ex.printStackTrace();
                 Alert alert = new Alert(Alert.AlertType.ERROR, "Error: " + ex.getMessage(), ButtonType.OK);
                 alert.showAndWait();
             }        });

        form.getChildren().addAll(new Label("Name"), txtName,
                new Label("Price"), txtPrice,
                new Label("Stock"), txtStock,
                chkFresh, new Label("Description"), txtDesc, btnSave);

        dialog.setScene(new Scene(form, 300, 400));
        dialog.setTitle(existingProduct  == null ? "Add Product" : "Edit Product");
        dialog.show();
    }
}
