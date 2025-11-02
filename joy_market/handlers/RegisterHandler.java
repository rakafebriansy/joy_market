package joy_market.handlers;

import joy_market.dataAccess.CustomerDA;
import joy_market.dataAccess.UserDA;
import joy_market.models.Customer;
import joy_market.models.User;
import joy_market.utils.Validator;
import joy_market.utils.PasswordHelper;

public class RegisterHandler {

    public String handleRegister(String fullName, String email, String pass, String confirm,
                                 String phone, String address, String gender) {

        if (Validator.isEmpty(fullName)) return "Full name cannot be empty!";
        if (Validator.isEmpty(email)) return "Email must be filled!";
        if (!email.contains("@")) return "Email must contain '@'!";
        if (Validator.isEmpty(pass) || pass.length() < 6) return "Password must be at least 6 characters!";
        if (!pass.equals(confirm)) return "Passwords do not match!";
        if (Validator.isEmpty(phone) || !Validator.isNumeric(phone) || phone.length() < 10 || phone.length() > 13)
            return "Invalid phone number!";
        if (Validator.isEmpty(address)) return "Address must be filled!";
        if (gender == null) return "Gender must be selected!";

        UserDA userDA = new UserDA();
        CustomerDA customerDA = new CustomerDA();

        try {
            if (userDA.isEmailExist(email)) return "Email already registered!";

            String hashedPassword = PasswordHelper.hashPassword(pass);

            User user = new User(0, email, hashedPassword, "CUSTOMER");
            Customer customer = new Customer(0, 0, fullName, phone, address, gender, 0);

            boolean success = customerDA.insertCustomerWithUser(customer, user);

            return success ? "Registration successful!" : "Registration failed. Try again.";

        } catch (Exception e) {
            e.printStackTrace();
            return "An error occurred: " + e.getMessage();
        }
    }
}
