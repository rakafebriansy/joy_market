package joy_market.handlers;

import joy_market.dataAccess.AdminDA;
import joy_market.dataAccess.CourierDA;
import joy_market.dataAccess.CustomerDA;
import joy_market.models.Admin;
import joy_market.models.Courier;
import joy_market.models.Customer;
import joy_market.utils.PasswordHelper;
import joy_market.utils.Validator;

public class LoginHandler {
    
    // This method handles the login process for different user roles (Customer, Admin, Courier)
	public Object handleLogin(String email, String password, String role) {
        // Check if email, password, or role fields are empty
        if (Validator.isEmpty(email)) return "Email cannot be empty!";
        if (Validator.isEmpty(password)) return "Password cannot be empty!";
        if (Validator.isEmpty(role)) return "Please select role!";

        // Hash the entered password for security before comparing with the database
        String hashed = PasswordHelper.hashPassword(password);

        // Determine the login process based on the user role
        switch (role.toUpperCase()) {
            // If the role is CUSTOMER, verify user credentials from the Customer table
            case "CUSTOMER": {
                Customer user = CustomerDA.getUserByEmailAndPassword(email, hashed);
                if (user == null) return "Customer account not found!";
                return user; // Return the logged-in customer object
            }
            // If the role is ADMIN, verify admin credentials from the Admin table
            case "ADMIN": {
                Admin admin = AdminDA.getAdminByEmailAndPassword(email, hashed);
                if (admin == null) return "Admin account not found!";
                return admin; // Return the logged-in admin object
            }
            // If the role is COURIER, verify courier credentials from the Courier table
            case "COURIER": {
                Courier courier = CourierDA.getCourierByEmailAndPassword(email, hashed);
                if (courier == null) return "Courier account not found!";
                return courier; // Return the logged-in courier object
            }
            // If the role does not match any known type, return null
            default:
                return null;
        }
    }
}
