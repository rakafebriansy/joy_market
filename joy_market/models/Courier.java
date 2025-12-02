package joy_market.models;

import java.sql.Timestamp;

public class Courier {
    // Declare variables for courier details
    private int id; // Unique ID of the courier
    private String email; // Courier's email address
    private String password; // Courier's account password
    
    // Constructor with all fields
    public Courier(int id, String email, String password) {
        this.id = id;
        this.email = email;
        this.password = password;
    }
    
    // Constructor without ID (used for new couriers before saving to database)
    public Courier(String email, String password) {
        this.email = email;
        this.password = password;
    }
    
    // Getter for courier ID
    public int getId() { return id; }
    // Getter for courier email
    public String getEmail() { return email; }
    // Getter for courier password
    public String getPassword() { return password; }
    
    // Setter for courier ID
    public void setId(int id) { this.id = id; }
    // Setter for courier email
    public void setEmail(String email) { this.email = email; }
    // Setter for courier password
    public void setPassword(String password) { this.password = password; }
}
