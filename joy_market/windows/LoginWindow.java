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
    private LoginHandler handler = new LoginHandler();

    public void show(Stage stage) {
        stage.setTitle("JoymarKet - Login");

        Label lblTitle = new Label("Login");
        lblTitle.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        TextField txtEmail = new TextField();
        txtEmail.setPromptText("Email");

        PasswordField txtPassword = new PasswordField();
        txtPassword.setPromptText("Password");
        
        ComboBox<String> cmbRole = new ComboBox<>();
        cmbRole.getItems().addAll("Customer", "Admin", "Courier");
        cmbRole.setPromptText("Select Role");

        Button btnLogin = new Button("Login");
        Label lblMessage = new Label();

        Label lblLink = new Label("Don't have an account? Register here");
        lblLink.setStyle("-fx-text-fill: blue; -fx-underline: true;");
        lblLink.setOnMouseClicked(e -> {
            UserWindow userWindow = new UserWindow();
            userWindow.show(stage);
        });

        btnLogin.setOnAction(e -> {
            Object res = handler.handleLogin(
                txtEmail.getText(),
                txtPassword.getText(),
                cmbRole.getValue()
            );

            if (res instanceof String) {
                // Pesan error
                lblMessage.setText((String) res);
                lblMessage.setStyle("-fx-text-fill: red;");
                return;
            }

            Stage newStage = new Stage();
            stage.close();

            switch (cmbRole.getValue().toUpperCase()) {
                case "ADMIN":
                    new AdminMainWindow((Admin) res).show(newStage);
                    break;

                case "COURIER":
                    new CourierMainWindow((Courier) res).show(newStage);
                    break;

                case "CUSTOMER":
                    new ProductWindow((Customer) res).show(newStage);
                    break;
            }
        });


        VBox root = new VBox(10, lblTitle, txtEmail, txtPassword, cmbRole, btnLogin, lblMessage, lblLink);
        root.setStyle("-fx-padding: 20; -fx-alignment: center;");

        Scene scene = new Scene(root, 350, 300);
        stage.setScene(scene);
        stage.show();
    }
}
