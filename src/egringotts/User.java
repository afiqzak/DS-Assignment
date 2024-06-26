package egringotts;

import java.sql.SQLException;

/**
 *
 * @author USER
 */
public abstract class User {
    private String name;
    private String username;
    private String password;
    private String phoneNum;
    private String email;
    private String dob;
    private String address;
    private String pin;

    public User(String username, String name, String password, String phoneNum, String email, String dob, String address) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.phoneNum = phoneNum;
        this.email = email;
        this.dob = dob;
        this.address = address;
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public String getEmail() {
        return email;
    }

    public String getDob() {
        return dob;
    }

    public String getAddress() {
        return address;
    }

    public abstract String setTier();
    
    public abstract String generateKey();
    
    public abstract String getKey();
}