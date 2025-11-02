package joy_market.handlers;

import joy_market.dataAccess.UserDA;
import joy_market.models.User;
import joy_market.utils.PasswordHelper;
import joy_market.utils.Validator;

public class LoginHandler {

    public String handleLogin(String email, String password) {
        if (Validator.isEmpty(email)) return "Email cannot be empty!";
        if (Validator.isEmpty(password)) return "Password cannot be empty!";
        String hashed = PasswordHelper.hashPassword(password);

        User user = UserDA.getUserByEmailAndPassword(email, hashed);
        if (user == null) {
            return "Invalid email or password!";
        } else {
            return "Login successful! Welcome, " + user.getFullName();
        }
    }
}
