package joy_market.windows;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import joy_market.handlers.RegisterHandler;
public class RegisterWindow {
    private RegisterHandler handler = new RegisterHandler();

    public void start(Stage stage) {
        stage.setTitle("JoymarKet - Register");

        Label lblTitle = new Label("Register Account");
        lblTitle.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

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

        Button btnRegister = new Button("Register");
        Label lblMessage = new Label();

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
            lblMessage.setText(msg);
        });

        VBox root = new VBox(10,
                lblTitle,
                txtName, txtEmail, txtPassword, txtConfirm,
                txtPhone, txtAddress, cmbGender,
                btnRegister, lblMessage
        );
        root.setStyle("-fx-padding: 20; -fx-alignment: center;");

        Scene scene = new Scene(root, 350, 450);
        stage.setScene(scene);
        stage.show();
    }
}
