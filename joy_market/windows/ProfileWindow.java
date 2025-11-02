package joy_market.windows;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import joy_market.handlers.ProfileHandler;
import joy_market.models.*;

public class ProfileWindow {
    private final ProfileHandler handler = new ProfileHandler();

    public void show(Stage stage, Object currentUser, String role) {
        stage.setTitle("Edit Profile - " + role);

        TextField txtEmail = new TextField();
        TextField txtName = new TextField();
        TextField txtPhone = new TextField();
        TextArea txtAddress = new TextArea();
        ComboBox<String> cmbGender = new ComboBox<>();
        cmbGender.getItems().addAll("MALE", "FEMALE");

        PasswordField txtNewPass = new PasswordField();
        PasswordField txtConfirmPass = new PasswordField();

        if (role.equalsIgnoreCase("CUSTOMER")) {
            User u = (User) currentUser;
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
        Label lblMsg = new Label();

        btnSave.setOnAction(e -> {
            String message;
            if (role.equalsIgnoreCase("CUSTOMER")) {
                User updated = new User(
                        ((User) currentUser).getId(),
                        txtName.getText(),
                        txtEmail.getText(),
                        ((User) currentUser).getPassword(),
                        txtPhone.getText(),
                        txtAddress.getText(),
                        cmbGender.getValue(),
                        ((User) currentUser).getBalance()
                );
                message = handler.handleUpdateProfile(updated, role, txtNewPass.getText(), txtConfirmPass.getText());
            } else if (role.equalsIgnoreCase("ADMIN")) {
                Admin updated = new Admin(((Admin) currentUser).getId(), txtEmail.getText(), ((Admin) currentUser).getPassword());
                message = handler.handleUpdateProfile(updated, role, txtNewPass.getText(), txtConfirmPass.getText());
            } else {
                Courier updated = new Courier(((Courier) currentUser).getId(), txtEmail.getText(), ((Courier) currentUser).getPassword());
                message = handler.handleUpdateProfile(updated, role, txtNewPass.getText(), txtConfirmPass.getText());
            }
            lblMsg.setText(message);
        });

        GridPane grid = new GridPane();
        grid.setVgap(8);
        grid.setHgap(10);
        grid.setStyle("-fx-padding: 20;");

        int row = 0;
        if (role.equalsIgnoreCase("CUSTOMER")) {
            grid.addRow(row++, new Label("Full Name:"), txtName);
            grid.addRow(row++, new Label("Email:"), txtEmail);
            grid.addRow(row++, new Label("Phone:"), txtPhone);
            grid.addRow(row++, new Label("Address:"), txtAddress);
            grid.addRow(row++, new Label("Gender:"), cmbGender);
        } else {
            grid.addRow(row++, new Label("Email:"), txtEmail);
        }

        grid.addRow(row++, new Label("New Password:"), txtNewPass);
        grid.addRow(row++, new Label("Confirm Password:"), txtConfirmPass);
        grid.addRow(row++, btnSave, lblMsg);

        Scene scene = new Scene(grid, 450, 400);
        stage.setScene(scene);
        stage.show();
    }
}
