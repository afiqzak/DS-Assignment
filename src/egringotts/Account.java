package egringotts;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 *
 * @author USER
 */
public class Account {
    
    public static boolean signUp(Customer user, String currency, double amount, String pin) {
        boolean done = true;
        String encryptedPIN = encryptPIN(pin);
        String SQL_Command = "SELECT username FROM account WHERE username ='" + user.getUsername() + "'"; 
        try (Connection con = DBConnection.openConn();
             Statement statement = con.createStatement()) {
            
            ResultSet Rslt = statement.executeQuery(SQL_Command);
            if (Rslt.next()) return false;
            if (done) {
                SQL_Command = "INSERT INTO account(AccountNum, Name_Customer, username, PhoneNum_Customer, Email_Customer, Password_Customer, DOB, Address, Tier, " + currency + ", Encrypted_PIN) " +
                              "VALUES ('" + user.getAccountNum() + "','" + user.getName() + "','" + user.getUsername() + "','" + user.getPhoneNum() + "','" +
                              user.getEmail() + "','" + user.getPassword() + "','" + user.getDOB() + "','" + user.getAddress() + "', '" + user.setTier() + "', " + amount + ", '" + encryptedPIN + "')";
                statement.executeUpdate(SQL_Command);
            }
            return true;
        } catch (SQLException e) {
            done = false;
            e.printStackTrace();
        }
        return done;
    }

    public static String signIn(String username, String pass, String pin) {
        String user = "";
        try (Connection con = DBConnection.openConn();
             Statement statement = con.createStatement()) {
            String SQL_Command = "SELECT Name_Admin FROM admin WHERE username ='" + username + "' AND Password_Admin ='" + pass + "'";
            ResultSet Rslt = statement.executeQuery(SQL_Command);
            if (Rslt.next()) {
                user = "admin";
            } else {
                SQL_Command = "SELECT Name_Customer, Encrypted_PIN FROM account WHERE username ='" + username + "' AND Password_Customer ='" + pass + "'";
                Rslt = statement.executeQuery(SQL_Command);
                if (Rslt.next()) {
                    String encryptedPIN = Rslt.getString("Encrypted_PIN");
                    if (verifyPIN(pin, encryptedPIN)) {
                        user = "customer";
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    private static String encryptPIN(String pin) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(pin.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    static boolean verifyPIN(String pin, String encryptedPIN) {
        return encryptPIN(pin).equals(encryptedPIN);
    }
}

