package egringotts;

import java.sql.SQLException;

/**
 *
 * @author USER
 */
public class User {
    private String name;
    private String username;
    private String password;
    private String phoneNum;
    private String email;
    private String dob;
    private String address;

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

    
}