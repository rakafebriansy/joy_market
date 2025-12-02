package joy_market.windows;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.collections.*;
import java.util.List;

import joy_market.models.*;
import joy_market.dataAccess.*;
import joy_market.handlers.CartItemHandler;
import joy_market.handlers.ProfileHandler;
import joy_market.utils.Validator;
import joy_market.widgets.OrderHistoryTableItem;

public class ProductWindow {

    private Customer user; // The logged-in customer
    private ProfileHandler profileHandler = new ProfileHandler();

    public ProductWindow(Customer user) {
        this.user = user; // Initialize with the current customer
    }
    
    private TableView<Product> tblProducts;
    
    // Show main customer dashboard
    public void show(Stage stage) {
        OrderHistoryWindow orderHistoryWindow = new OrderHistoryWindow(user); // Order history tab
        
        TabPane tabPane = new TabPane();
        
        // -------------------- Order Tab --------------------
        Tab tabOrder = new Tab("Order");
        VBox orderRoot = new VBox(10);
        orderRoot.setPadding(new Insets(10));

        tblProducts = new TableView<>();

        // Columns: Name, Price, Stock, Action (Add to Cart)
        TableColumn<Product, String> colName = new TableColumn<>("Name");
        colName.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getName()));

        TableColumn<Product, String> colPrice = new TableColumn<>("Price");
        colPrice.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(String.valueOf(c.getValue().getPrice())));

        TableColumn<Product, String> colStock = new TableColumn<>("Stock");
        colStock.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(String.valueOf(c.getValue().getStock())));
        
        // Action column for adding products to cart
        TableColumn<Product, String> colAction = new TableColumn<>("Action");
        colAction.setCellFactory(col -> new TableCell<Product, String>() {
            private final Button btnAdd = new Button("Add to Cart");
            {
                btnAdd.setOnAction(e -> {
                    Product p = getTableView().getItems().get(getIndex());
                    CartItemDA.saveDA(user.getId(), p.getId(), 1); // Add 1 quantity to cart
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, p.getName() + " added to cart!", ButtonType.OK);
                    alert.showAndWait();
                });
            }
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btnAdd);
            }
        });

        tblProducts.getColumns().addAll(colName, colPrice, colStock, colAction);
        // Load products from database
        List<Product> products = ProductDA.getAllProducts();
        tblProducts.setItems(FXCollections.observableArrayList(products));
        
        // Button to view cart
        Button btnViewCart = new Button("View Cart");
        btnViewCart.setOnAction(e -> showCartPopup(orderHistoryWindow));

        orderRoot.getChildren().addAll(tblProducts, btnViewCart);
        tabOrder.setContent(orderRoot);
        
        // -------------------- Top Up Tab --------------------
        Tab tabTopUp = new Tab("Top Up");
        VBox topUpRoot = new VBox(10);
        topUpRoot.setPadding(new Insets(10));
        
        // Display current balance
        Label lblBalance = new Label();
        lblBalance.textProperty().bind(user.balanceProperty().asString("Current Balance: %d"));

        TextField txtAmount = new TextField();
        txtAmount.setPromptText("Amount to Top Up");

        Button btnTopUp = new Button("Top Up");
        btnTopUp.setOnAction(e -> {
            try {
                long amt = Long.parseLong(txtAmount.getText());
                if (amt <= 0) throw new Exception();
                user.setBalance(user.getBalance() + amt); // Add balance
                boolean a = CustomerDA.updateUser(user); // Update database
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Top up successful!", ButtonType.OK);
                alert.showAndWait();
            } catch (Exception ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid amount!", ButtonType.OK);
                alert.showAndWait();
            }
        });

        topUpRoot.getChildren().addAll(lblBalance, txtAmount, btnTopUp);
        tabTopUp.setContent(topUpRoot);

        // -------------------- Order History Tab --------------------
        Tab tabHistory = new Tab("Order History", orderHistoryWindow.getView());
        
        // -------------------- Profile Tab --------------------
        Tab tabProfile = new Tab("Edit Profile");
        VBox profileRoot = new VBox(10);
        profileRoot.setPadding(new Insets(10));
        
        // Profile fields
        TextField txtName = new TextField(user.getFullName());
        TextField txtEmail = new TextField(user.getEmail());
        TextField txtPhone = new TextField(user.getPhone());
        TextField txtAddress = new TextField(user.getAddress());
        ComboBox<String> cmbGender = new ComboBox<>();
        cmbGender.getItems().addAll("MALE","FEMALE","OTHER");
        cmbGender.setValue(user.getGender());

        PasswordField txtNewPass = new PasswordField();
        txtNewPass.setPromptText("New Password");
        PasswordField txtConfirmPass = new PasswordField();
        txtConfirmPass.setPromptText("Confirm Password");

        Button btnSaveProfile = new Button("Save Profile");
        btnSaveProfile.setOnAction(e -> {
            // Update local user data
            user.setFullName(txtName.getText().trim());
            user.setEmail(txtEmail.getText().trim());
            user.setPhone(txtPhone.getText().trim());
            user.setAddress(txtAddress.getText().trim());
            user.setGender(cmbGender.getValue());
            
            // Update database via ProfileHandler
            String res = profileHandler.handleUpdateProfile(user, "CUSTOMER", txtNewPass.getText(), txtConfirmPass.getText());
            Alert alert = new Alert(Alert.AlertType.INFORMATION, res, ButtonType.OK);
            alert.showAndWait();
        });

        profileRoot.getChildren().addAll(new Label("Full Name:"), txtName,
                new Label("Email:"), txtEmail,
                new Label("Phone:"), txtPhone,
                new Label("Address:"), txtAddress,
                new Label("Gender:"), cmbGender,
                new Label("New Password:"), txtNewPass,
                new Label("Confirm Password:"), txtConfirmPass,
                btnSaveProfile);

        tabProfile.setContent(profileRoot);

        tabPane.getTabs().addAll(tabOrder, tabTopUp, tabHistory, tabProfile);

        Scene scene = new Scene(tabPane, 800, 600);
        stage.setScene(scene);
        stage.setTitle("JoymarKet - Customer Dashboard");
        stage.show();
    }
    
    // -------------------- Cart Popup --------------------
    private void showCartPopup(OrderHistoryWindow orderHistoryWindow) {
        Stage cartStage = new Stage();
        VBox root = new VBox(10);
        root.setPadding(new Insets(10));

        TableView<CartItem> tblCart = new TableView<>();
        TableColumn<CartItem, String> colName = new TableColumn<>("Product");
        colName.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(
                ProductDA.read(c.getValue().getProductId()).getName()));

        TableColumn<CartItem, String> colCount = new TableColumn<>("Qty");
        colCount.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(
                String.valueOf(c.getValue().getCount())));

        TableColumn<CartItem, String> colPrice = new TableColumn<>("Price");
        colPrice.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(
                String.valueOf(ProductDA.read(c.getValue().getProductId()).getPrice() * c.getValue().getCount())));

        tblCart.getColumns().addAll(colName, colCount, colPrice);
        
        // Load cart items
        List<CartItem> items = CartItemDA.getCartItemsByUser(user.getId());
        tblCart.setItems(FXCollections.observableArrayList(items));
        
        // Remove item button
        Button btnRemoveItem = new Button("Remove Selected Item");
        btnRemoveItem.setOnAction(e -> {
            CartItem selected = tblCart.getSelectionModel().getSelectedItem();
            if (selected != null) {
                boolean success = CartItemHandler.deleteCartItem(selected.getId());
                if (success) refreshCartTable(tblCart);
                else showAlert("Failed to remove item!");
            }
        });
        
        // Clear all cart items
        Button btnClearCart = new Button("Clear Cart");
        btnClearCart.setOnAction(e -> {
            boolean success = CartItemHandler.clearCart(this.user.getId());
            if (success) refreshCartTable(tblCart);
            else showAlert("Failed to clear cart!");
        });

        // Pay button
        Button btnPay = new Button("Pay");
        btnPay.setOnAction(e -> {
            long total = items.stream().mapToLong(i ->
                    ProductDA.read(i.getProductId()).getPrice() * i.getCount()
            ).sum();

            if (user.getBalance() >= total) {
                user.setBalance(user.getBalance() - total); // Deduct balance
                
                int orderId = OrderHeaderDA.insertOrder(user.getId(), total);
                
                // Insert order items and update stock
                for (CartItem item : items) {
                    Product p = ProductDA.read(item.getProductId());
                    OrderHeaderDA.insertOrderItem(orderId, p.getId(), item.getCount(), p.getPrice());
                    
                    int newStock = p.getStock() - item.getCount();
                    if (newStock < 0) newStock = 0;
                    p.setStock(newStock);
                    
                    ProductDA.updateProductStock(p.getId(), newStock);
                }
                
                boolean a = CustomerDA.updateUser(user);// Update balance in database
                System.out.println(a);
                CartItemDA.clearCart(user.getId()); // Clear cart
                
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Payment successful!", ButtonType.OK);
                alert.showAndWait();
                cartStage.close();
                
                if (tblProducts != null) {
                    refresh(); // Refresh product table
                }

                if (orderHistoryWindow != null) {
                    orderHistoryWindow.refresh();
                }

            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Insufficient balance!", ButtonType.OK);
                alert.showAndWait();
            }
        });

        root.getChildren().addAll(tblCart, btnPay, btnRemoveItem, btnClearCart);
        cartStage.setScene(new Scene(root, 400, 400));
        cartStage.setTitle("Cart");
        cartStage.show();
    }
    // Refresh cart table
    private void refreshCartTable(TableView<CartItem> tblCart) {
        List<CartItem> items = CartItemDA.getCartItemsByUser(this.user.getId());
        ObservableList<CartItem> obsItems = FXCollections.observableArrayList(items);
        tblCart.setItems(obsItems);
    }
    // Show alert message
    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR, msg, ButtonType.OK);
        alert.showAndWait();
    }
    
    // Refresh product table
    public void refresh() {
        List<Product> products = ProductDA.getAllProducts();
        tblProducts.setItems(FXCollections.observableArrayList(products));
    }
}
