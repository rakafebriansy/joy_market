package joy_market.models;

public class User {
    private int id;
    private String email;
    private String password;
    private String fullName;
    private String phone;
    private String address;
    private String gender;
    private long balance;

    public User(int id, String email, String password, String fullName,
                String phone, String address, String gender, long balance) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.fullName = fullName;
        this.phone = phone;
        this.address = address;
        this.gender = gender;
        this.balance = balance;
    }

    public int getId() { return id; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getFullName() { return fullName; }
    public String getPhone() { return phone; }
    public String getAddress() { return address; }
    public String getGender() { return gender; }
    public long getBalance() { return balance; }

    public void setBalance(long balance) { this.balance = balance; }
}
