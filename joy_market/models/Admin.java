package joy_market.models;

import java.sql.Timestamp;

public class Admin {
    // Declare variables for admin properties
    private int id;
    private String email;
    private String password;
    
    // Constructor with all fields
    public Admin(int id, String email, String password) {
        this.id = id;
        this.email = email;
        this.password = password;
    }
    
    // Constructor without ID (used when creating a new admin)
    public Admin(String email, String password) {
        this.email = email;
        this.password = password;
    }
    
    // Getter for admin ID
    public int getId() { return id; }
    // Getter for admin email
    public String getEmail() { return email; }
    // Getter for admin password
    public String getPassword() { return password; }

    // Setter for admin ID
    public void setId(int id) { this.id = id; }
    // Setter for admin email
    public void setEmail(String email) { this.email = email; }
    // Setter for admin password
    public void setPassword(String password) { this.password = password; }
}
