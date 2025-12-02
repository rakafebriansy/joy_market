package joy_market.handlers;

import joy_market.dataAccess.*;
import joy_market.models.*;
import joy_market.utils.*;

public class ProfileHandler {

    // Handle profile updates based on user role
    public String handleUpdateProfile(Object userObj, String role, String newPassword, String confirmPassword) {
        if (role == null) return "Role not specified!";
        
        // Check user role and call the corresponding update method
        switch (role.toUpperCase()) {
            case "CUSTOMER":
                return editProfile((Customer) userObj, newPassword, confirmPassword);
            case "ADMIN":
                return updateAdminProfile((Admin) userObj, newPassword, confirmPassword);
            case "COURIER":
                return updateCourierProfile((Courier) userObj, newPassword, confirmPassword);
            default:
                return "Invalid role!";
        }
    }
    
    // Handle customer profile update
    private String editProfile(Customer user, String newPassword, String confirmPassword) {
        // Validate required fields
        if (Validator.isEmpty(user.getFullName())) return "Name cannot be empty!";
        if (Validator.isEmpty(user.getEmail())) return "Email cannot be empty!";
        if (!user.getEmail().contains("@")) return "Invalid email format!";
        if (Validator.isEmpty(user.getPhone())) return "Phone cannot be empty!";
        if (Validator.isEmpty(user.getAddress())) return "Address cannot be empty!";

        boolean updatedSomething = false;
        StringBuilder updatedFields = new StringBuilder();
        
        // Handle password update if entered
        if (!Validator.isEmpty(newPassword)) {
            if (!newPassword.equals(confirmPassword)) return "Passwords do not match!";
            user.setPassword(PasswordHelper.hashPassword(newPassword));
            updatedFields.append("password, ");
            updatedSomething = true;
        }
        
        // Update customer profile in database
        if (CustomerDA.updateUser(user)) {
            if (updatedSomething)
                return "Updated " + updatedFields.substring(0, updatedFields.length() - 2) + " successfully!";
            else
                return "Profile updated successfully!";
        }

        return "Failed to update profile!";
    }
    
    // Handle admin profile update
    private String updateAdminProfile(Admin admin, String newPassword, String confirmPassword) {
        
        // Validate email
        if (Validator.isEmpty(admin.getEmail())) return "Email cannot be empty!";
        if (!admin.getEmail().contains("@")) return "Invalid email format!";

        boolean updatedSomething = false;
        StringBuilder updatedFields = new StringBuilder();
        
        // Handle password update
        if (!Validator.isEmpty(newPassword)) {
            if (!newPassword.equals(confirmPassword)) return "Passwords do not match!";
            admin.setPassword(PasswordHelper.hashPassword(newPassword));
            updatedFields.append("password, ");
            updatedSomething = true;
        }
        
        // Update admin profile in database
        boolean success = AdminDA.updateAdmin(admin);
        if (success) {
            if (updatedSomething)
                return "Updated " + updatedFields.substring(0, updatedFields.length() - 2) + " successfully!";
            else
                return "No changes were made.";
        } else {
            return "Update failed! Please try again.";
        }
    }
    
    // Handle courier profile update
    private String updateCourierProfile(Courier courier, String newPassword, String confirmPassword) {
        // Validate email
        if (Validator.isEmpty(courier.getEmail())) return "Email cannot be empty!";
        if (!courier.getEmail().contains("@")) return "Invalid email format!";

        boolean updatedSomething = false;
        StringBuilder updatedFields = new StringBuilder();
        
        // Handle password update
        if (!Validator.isEmpty(newPassword)) {
            if (!newPassword.equals(confirmPassword)) return "Passwords do not match!";
            courier.setPassword(PasswordHelper.hashPassword(newPassword));
            updatedFields.append("password, ");
            updatedSomething = true;
        }
        
        // Update courier profile in database
        if (CourierDA.updateCourier(courier)) {
            if (updatedSomething)
                return "Updated " + updatedFields.substring(0, updatedFields.length() - 2) + " successfully!";
            else
                return "Profile updated successfully!";
        }

        return "Failed to update courier profile!";
    }
}
