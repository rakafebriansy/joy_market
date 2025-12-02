package joy_market.handlers;

import joy_market.dataAccess.CustomerDA;
import joy_market.models.Customer;
import joy_market.utils.PasswordHelper;
import joy_market.utils.Validator;

public class UserHandler {
	
	// Handle user registration process
    public String saveDataUser(String fullName, String email, String pass, String confirm,
            String phone, String address, String gender) {
		
		// Validate user input fields
		if (Validator.isEmpty(fullName)) return "Full name cannot be empty!";
		if (Validator.isEmpty(email)) return "Email cannot be empty!";
		if (!email.contains("@")) return "Email must contain '@'!";
		if (Validator.isEmpty(pass) || pass.length() < 6) return "Password must be at least 6 chars!";
		if (!pass.equals(confirm)) return "Passwords do not match!";
		if (Validator.isEmpty(phone) || !Validator.isNumeric(phone) ||
		phone.length() < 10 || phone.length() > 13) return "Invalid phone number!";
		if (Validator.isEmpty(address)) return "Address cannot be empty!";
		if (gender == null) return "Gender must be selected!";
		
		// Check if email already exists in the database
		if (CustomerDA.isEmailExist(email)) return "Email already registered!";
		
		// Hash the password before saving
		String hashedPass = PasswordHelper.hashPassword(pass);
		// Create a new customer object
		Customer user = new Customer(0, email, hashedPass, fullName, phone, address, gender, 0);
		
		// Save the user to the database
		boolean success = CustomerDA.saveDA(user);
		// Return message based on result
		return success ? "Registration successful!" : "Registration failed. Try again.";
		}
}
