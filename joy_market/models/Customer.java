package joy_market.models;

import javafx.beans.property.*;

public class Customer {
    // Declare customer attributes
    private int id; // Unique ID of the customer
    private String email; // Customer's email address
    private String password; // Customer's account password
    private String fullName; // Full name of the customer
    private String phone; // Customer's phone number
    private String address; // Customer's home address
    private String gender; // Customer's gender
    private LongProperty balance; // Customer's balance (using JavaFX property for UI binding)

    // Constructor to initialize all customer data
    public Customer(int id, String email, String password, String fullName,
                String phone, String address, String gender, long balance) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.fullName = fullName;
        this.phone = phone;
        this.address = address;
        this.gender = gender;
        this.balance = new SimpleLongProperty(balance); // initialize balance property
    }
    
    // Getter methods to access private fields
    public int getId() { return id; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getFullName() { return fullName; }
    public String getPhone() { return phone; }
    public String getAddress() { return address; }
    public String getGender() { return gender; }
    public long getBalance() { return balance.get(); } // get value from balance property
    
    // Used for JavaFX data binding in UI
    public LongProperty balanceProperty() { return balance; }
    
    // Setter methods to modify private fields
    public void setId(int id) { this.id = id; }
    public void setEmail(String email) { this.email = email; }
    public void setPassword(String password) { this.password = password; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setAddress(String address) { this.address = address; }
    public void setGender(String gender) { this.gender = gender; }
    public void setBalance(long value) { balance.set(value); } // update balance value

}
