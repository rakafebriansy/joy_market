package joy_market.models;

public class Customer {
    private int id;
    private int userId;
    private String fullName;
    private String phone;
    private String address;
    private String gender;
    private double balance;

    public Customer(int userId, String fullName, String phone, String address, String gender, double balance) {
        this.userId = userId;
        this.fullName = fullName;
        this.phone = phone;
        this.address = address;
        this.gender = gender;
        this.balance = balance;
    }

    public Customer(int id, int userId, String fullName, String phone, String address, String gender, double balance) {
        this.id = id;
        this.userId = userId;
        this.fullName = fullName;
        this.phone = phone;
        this.address = address;
        this.gender = gender;
        this.balance = balance;
    }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }

    public void setUserId(int userId) { this.userId = userId; }

    public String getFullName() { return fullName; }

    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getPhone() { return phone; }

    public void setPhone(String phone) { this.phone = phone; }

    public String getAddress() { return address; }

    public void setAddress(String address) { this.address = address; }

    public String getGender() { return gender; }

    public void setGender(String gender) { this.gender = gender; }

    public double getBalance() { return balance; }

    public void setBalance(double balance) { this.balance = balance; }
}
