package joy_market.handlers;

import joy_market.dataAccess.AdminDA;
import joy_market.dataAccess.CourierDA;
import joy_market.dataAccess.UserDA;
import joy_market.models.Admin;
import joy_market.models.Courier;
import joy_market.models.User;
import joy_market.utils.PasswordHelper;
import joy_market.utils.Validator;

public class LoginHandler {

	public Object handleLogin(String email, String password, String role) {
        if (Validator.isEmpty(email)) return "Email cannot be empty!";
        if (Validator.isEmpty(password)) return "Password cannot be empty!";
        if (Validator.isEmpty(role)) return "Please select role!";

        String hashed = PasswordHelper.hashPassword(password);

        switch (role.toUpperCase()) {
            case "CUSTOMER": {
                User user = UserDA.getUserByEmailAndPassword(email, hashed);
                if (user == null) return "Customer account not found!";
                return user;
            }
            case "ADMIN": {
                Admin admin = AdminDA.getAdminByEmailAndPassword(email, hashed);
                if (admin == null) return "Admin account not found!";
                return admin;
            }
            case "COURIER": {
                Courier courier = CourierDA.getCourierByEmailAndPassword(email, hashed);
                if (courier == null) return "Courier account not found!";
                return courier;
            }
            default:
                return null;
        }
    }
}
