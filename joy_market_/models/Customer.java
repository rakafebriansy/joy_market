package joy_market.models;

import javafx.beans.property.*;

public class Customer {
    private int id;
    private String email;
    private String password;
    private String fullName;
    private String phone;
    private String address;
    private String gender;
    private LongProperty balance;

    public Customer(int id, String email, String password, String fullName,
                String phone, String address, String gender, long balance) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.fullName = fullName;
        this.phone = phone;
        this.address = address;
        this.gender = gender;
        this.balance = new SimpleLongProperty(balance);
    }

    public int getId() { return id; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getFullName() { return fullName; }
    public String getPhone() { return phone; }
    public String getAddress() { return address; }
    public String getGender() { return gender; }
    public long getBalance() { return balance.get(); }
    
    public LongProperty balanceProperty() { return balance; }

    public void setId(int id) { this.id = id; }
    public void setEmail(String email) { this.email = email; }
    public void setPassword(String password) { this.password = password; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setAddress(String address) { this.address = address; }
    public void setGender(String gender) { this.gender = gender; }
    public void setBalance(long value) { balance.set(value); }

}
