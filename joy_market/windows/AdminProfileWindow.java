package joy_market.windows;

import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.geometry.Insets;
import joy_market.models.Admin;
import joy_market.handlers.ProfileHandler;

public class AdminProfileWindow {
    private Admin admin;

    public AdminProfileWindow(Admin admin) {
        this.admin = admin;
    }

    public Pane getView() {
        VBox root = new VBox(10);
        root.setPadding(new Insets(20));

        Label lblTitle = new Label("Admin Profile");
        lblTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        TextField txtEmail = new TextField(admin.getEmail());
        txtEmail.setEditable(false);

        PasswordField txtPassword = new PasswordField();
        txtPassword.setPromptText("New Password");

        PasswordField txtConfirm = new PasswordField();
        txtConfirm.setPromptText("Confirm Password");

        Button btnSave = new Button("Save Changes");
        Label lblMsg = new Label();

        ProfileHandler handler = new ProfileHandler();

        btnSave.setOnAction(e -> {
            String newPass = txtPassword.getText().trim();
            String confirm = txtConfirm.getText().trim();

            String result = handler.handleUpdateProfile(admin, "ADMIN", newPass, confirm);
            lblMsg.setText(result);
        });

        root.getChildren().addAll(
                lblTitle,
                new Label("Email:"), txtEmail,
                new Label("Password:"), txtPassword,
                new Label("Confirm Password:"), txtConfirm,
                btnSave, lblMsg
        );

        return root;
    }
}
