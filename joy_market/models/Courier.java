package joy_market.models;

import java.sql.Timestamp;

public class Courier {
    private int id;
    private String email;
    private String password;

    public Courier(int id, String email, String password) {
        this.id = id;
        this.email = email;
        this.password = password;
    }

    public Courier(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public int getId() { return id; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }

    public void setId(int id) { this.id = id; }
    public void setEmail(String email) { this.email = email; }
    public void setPassword(String password) { this.password = password; }
}
