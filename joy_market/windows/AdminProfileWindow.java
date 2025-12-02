package joy_market.windows;

import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.geometry.Insets;
import joy_market.models.Admin;
import joy_market.handlers.ProfileHandler;

// This class creates the admin profile window interface
public class AdminProfileWindow {
    // Stores the admin data for the current session
    private Admin admin;
    
    // Constructor to initialize the admin object
    public AdminProfileWindow(Admin admin) {
        this.admin = admin;
    }
    
    // Returns the UI layout (Pane) for the admin profile section
    public Pane getView() {
        VBox root = new VBox(10); // Vertical layout with spacing
        root.setPadding(new Insets(20)); // Padding around the layout
        
        // Title label for the section
        Label lblTitle = new Label("Admin Profile");
        lblTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        
        // Display the admin's email (not editable)
        TextField txtEmail = new TextField(admin.getEmail());
        txtEmail.setEditable(false);
        
        // Input field for new password
        PasswordField txtPassword = new PasswordField();
        txtPassword.setPromptText("New Password");
        
        // Input field to confirm the new password
        PasswordField txtConfirm = new PasswordField();
        txtConfirm.setPromptText("Confirm Password");
        
        // Button to save the new password
        Button btnSave = new Button("Save Changes");
        // Label to show messages or feedback
        Label lblMsg = new Label();
        
        // Handler to process profile updates
        ProfileHandler handler = new ProfileHandler();
        
        // Action when the "Save Changes" button is clicked
        btnSave.setOnAction(e -> {
            // Retrieve input values
            String newPass = txtPassword.getText().trim();
            String confirm = txtConfirm.getText().trim();
        
            // Call handler to process password update
            String result = handler.handleUpdateProfile(admin, "ADMIN", newPass, confirm);
            // Show result message in an alert dialog
            Alert alert = new Alert(Alert.AlertType.INFORMATION, result, ButtonType.OK);
            alert.showAndWait();
        });
        
        // Add all UI elements to the layout
        root.getChildren().addAll(
                lblTitle,
                new Label("Email:"), txtEmail,
                new Label("Password:"), txtPassword,
                new Label("Confirm Password:"), txtConfirm,
                btnSave, lblMsg
        );

        return root; // Return the completed layout
    }
}
