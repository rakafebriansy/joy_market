package joy_market.windows;

import javafx.scene.Scene;

import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import joy_market.handlers.LoginHandler;
import joy_market.windows.AdminMainWindow;
import joy_market.models.Admin;
import joy_market.models.Courier;
import joy_market.models.Customer;

public class LoginWindow {
    private LoginHandler handler = new LoginHandler(); // Handles login logic

    public void show(Stage stage) {
        stage.setTitle("JoymarKet - Login"); // Set window title
        
        // Title label
        Label lblTitle = new Label("Login");
        lblTitle.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        
        // Email input
        TextField txtEmail = new TextField();
        txtEmail.setPromptText("Email");
        
        // Password input
        PasswordField txtPassword = new PasswordField();
        txtPassword.setPromptText("Password");
        
        // Role selection (Customer, Admin, or Courier)
        ComboBox<String> cmbRole = new ComboBox<>();
        cmbRole.getItems().addAll("Customer", "Admin", "Courier");
        cmbRole.setPromptText("Select Role");
        
        // Login button
        Button btnLogin = new Button("Login");
        Label lblMessage = new Label();
        
        // Register link for new users
        Label lblLink = new Label("Don't have an account? Register here");
        lblLink.setStyle("-fx-text-fill: blue; -fx-underline: true;");
        lblLink.setOnMouseClicked(e -> {
            // Open registration window
            UserWindow userWindow = new UserWindow();
            userWindow.show(stage);
        });
        
        // Handle login button click
        btnLogin.setOnAction(e -> {
            // Call login handler with entered credentials
            Object res = handler.handleLogin(
                txtEmail.getText(),
                txtPassword.getText(),
                cmbRole.getValue()
            );
            
            // If result is a string, it means an error message
            if (res instanceof String) {
                // Pesan error
                lblMessage.setText((String) res);
                lblMessage.setStyle("-fx-text-fill: red;");
                return;
            }
            
            // Open new window according to user role
            Stage newStage = new Stage();
            stage.close();

            switch (cmbRole.getValue().toUpperCase()) {
                case "ADMIN":
                    new AdminMainWindow((Admin) res).show(newStage);
                    break;

                case "COURIER":
                    new CourierWindow((Courier) res).show(newStage);
                    break;

                case "CUSTOMER":
                    new ProductWindow((Customer) res).show(newStage);
                    break;
            }
        });

        // Layout configuration
        VBox root = new VBox(10, lblTitle, txtEmail, txtPassword, cmbRole, btnLogin, lblMessage, lblLink);
        root.setStyle("-fx-padding: 20; -fx-alignment: center;");
        
        // Set up scene and show stage
        Scene scene = new Scene(root, 350, 300);
        stage.setScene(scene);
        stage.show();
    }
}
