package joy_market.windows;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import joy_market.handlers.UserHandler;
public class UserWindow {
    private UserHandler handler = new UserHandler(); // Handles user registration
    
    // Show registration window
    public void show(Stage stage) {
        stage.setTitle("JoymarKet - Register");
    
        // Title label
        Label lblTitle = new Label("Register Account");
        lblTitle.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        
        // Input fields for registration
        TextField txtName = new TextField();
        txtName.setPromptText("Full Name");

        TextField txtEmail = new TextField();
        txtEmail.setPromptText("Email (must end with @gmail.com)");

        PasswordField txtPassword = new PasswordField();
        txtPassword.setPromptText("Password (min 6 chars)");

        PasswordField txtConfirm = new PasswordField();
        txtConfirm.setPromptText("Confirm Password");

        TextField txtPhone = new TextField();
        txtPhone.setPromptText("Phone (10–13 digits)");

        TextField txtAddress = new TextField();
        txtAddress.setPromptText("Address");

        ComboBox<String> cmbGender = new ComboBox<>();
        cmbGender.getItems().addAll("Male", "Female");
        cmbGender.setPromptText("Select Gender");
        
        // Register button and message label
        Button btnRegister = new Button("Register");
        Label lblMessage = new Label(); // Shows success or error messages
        
        // Link to go to login window
        Label lblLink = new Label("Already have an account? Login here");
        lblLink.setStyle("-fx-text-fill: blue; -fx-underline: true;");
        lblLink.setOnMouseClicked(e -> {
            LoginWindow loginWindow = new LoginWindow();
            loginWindow.show(stage);
        });
        
        // Handle Register button click
        btnRegister.setOnAction(e -> {
            String msg = handler.saveDataUser(
                txtName.getText(),
                txtEmail.getText(),
                txtPassword.getText(),
                txtConfirm.getText(),
                txtPhone.getText(),
                txtAddress.getText(),
                cmbGender.getValue()
            );
            lblMessage.setText(msg); // Display result message
        });
        
        // Layout setup using VBox
        VBox root = new VBox(10,
                lblTitle,
                txtName, txtEmail, txtPassword, txtConfirm,
                txtPhone, txtAddress, cmbGender,
                btnRegister, lblMessage, lblLink
        );
        root.setStyle("-fx-padding: 20; -fx-alignment: center;");
        
        // Show scene
        Scene scene = new Scene(root, 350, 450);
        stage.setScene(scene);
        stage.show();
    }
}
