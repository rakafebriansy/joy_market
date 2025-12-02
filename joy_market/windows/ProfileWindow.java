package joy_market.windows;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import joy_market.handlers.ProfileHandler;
import joy_market.models.*;

public class ProfileWindow {
    private final ProfileHandler handler = new ProfileHandler(); // Handles profile updates
    
    // Show profile edit window
    public void show(Stage stage, Object currentUser, String role) {
        stage.setTitle("Edit Profile - " + role);
        
        // Input fields
        TextField txtEmail = new TextField();
        TextField txtName = new TextField();
        TextField txtPhone = new TextField();
        TextArea txtAddress = new TextArea();
        ComboBox<String> cmbGender = new ComboBox<>();
        cmbGender.getItems().addAll("MALE", "FEMALE");

        PasswordField txtNewPass = new PasswordField();
        PasswordField txtConfirmPass = new PasswordField();
        
        // Fill input fields with current user data
        if (role.equalsIgnoreCase("CUSTOMER")) {
            Customer u = (Customer) currentUser;
            txtEmail.setText(u.getEmail());
            txtName.setText(u.getFullName());
            txtPhone.setText(u.getPhone());
            txtAddress.setText(u.getAddress());
            cmbGender.setValue(u.getGender());
        } else if (role.equalsIgnoreCase("ADMIN")) {
            Admin a = (Admin) currentUser;
            txtEmail.setText(a.getEmail());
        } else {
            Courier c = (Courier) currentUser;
            txtEmail.setText(c.getEmail());
        }

        Button btnSave = new Button("Save Changes");
        Label lblMsg = new Label(); // Label to show success/error messages
        
        // Handle Save button click
        btnSave.setOnAction(e -> {
            String message;
            if (role.equalsIgnoreCase("CUSTOMER")) {
                // Create updated Customer object
                Customer updated = new Customer(
                        ((Customer) currentUser).getId(),
                        txtName.getText(),
                        txtEmail.getText(),
                        ((Customer) currentUser).getPassword(),
                        txtPhone.getText(),
                        txtAddress.getText(),
                        cmbGender.getValue(),
                        ((Customer) currentUser).getBalance()
                );
                message = handler.handleUpdateProfile(updated, role, txtNewPass.getText(), txtConfirmPass.getText());
            } else if (role.equalsIgnoreCase("ADMIN")) {
                // Create updated Admin object
                Admin updated = new Admin(((Admin) currentUser).getId(), txtEmail.getText(), ((Admin) currentUser).getPassword());
                message = handler.handleUpdateProfile(updated, role, txtNewPass.getText(), txtConfirmPass.getText());
            } else {
                // Create updated Courier object
                Courier updated = new Courier(((Courier) currentUser).getId(), txtEmail.getText(), ((Courier) currentUser).getPassword());
                message = handler.handleUpdateProfile(updated, role, txtNewPass.getText(), txtConfirmPass.getText());
            }
            lblMsg.setText(message); // Show the result message
        });
         // Layout using GridPane
        GridPane grid = new GridPane();
        grid.setVgap(8);
        grid.setHgap(10);
        grid.setStyle("-fx-padding: 20;");

        int row = 0;
        if (role.equalsIgnoreCase("CUSTOMER")) {
            // Add customer-specific fields
            grid.addRow(row++, new Label("Full Name:"), txtName);
            grid.addRow(row++, new Label("Email:"), txtEmail);
            grid.addRow(row++, new Label("Phone:"), txtPhone);
            grid.addRow(row++, new Label("Address:"), txtAddress);
            grid.addRow(row++, new Label("Gender:"), cmbGender);
        } else {
            // Only email for Admin and Courier
            grid.addRow(row++, new Label("Email:"), txtEmail);
        }
        
        // Password fields and save button
        grid.addRow(row++, new Label("New Password:"), txtNewPass);
        grid.addRow(row++, new Label("Confirm Password:"), txtConfirmPass);
        grid.addRow(row++, btnSave, lblMsg);
        
         // Show scene
        Scene scene = new Scene(grid, 450, 400);
        stage.setScene(scene);
        stage.show();
    }
}
