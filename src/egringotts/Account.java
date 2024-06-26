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
public class Account<E extends User> {
    private E account;
    private EmailNotification emailNotification = new EmailNotification();
    
    public Account(E account) {
        this.account = account;
    }

    public E getAccount() {
        return account;
    }
 
    public boolean signUp(String currency, double amount, String pin) {
        boolean done = true;
        String SQL_Command = "SELECT username FROM account WHERE username ='" + account.getUsername() + "'"; 
            try (Connection con = DBConnection.openConn();
                Statement statement = con.prepareStatement(SQL_Command)){
                // Inquire if the username exists.
                ResultSet Rslt = statement.executeQuery(SQL_Command); 
                if(Rslt.next()) return false;
                if (done) {
                    // Save the user details
                       SQL_Command = "INSERT INTO account(AccountNum, Name_Customer, username, PhoneNum_Customer, Email_Customer, Password_Customer, DOB, Address, Tier, " + currency + ", Encrypted_PIN) " +
                                    "VALUES ('" + account.generateKey() + "','" + account.getName() + "','" + account.getUsername() + "','" + account.getPhoneNum() + "','" +
                                    account.getEmail() + "','" + encryptPIN(account.getPassword()) + "','" + account.getDob() + "','" + account.getAddress() + "', '" + account.setTier() + "', " + amount + ", '" + encryptPIN(pin) + "')";
                       
                       statement.executeUpdate(SQL_Command);
                        // Send sign-up email
                        emailNotification.sendSignUpEmail(account.getEmail(), account.getUsername());
                }
                return true;
            }
            catch (SQLException e) {
               done = false;
               System.out.println("SQLException: " + e);
               while (e != null) {
                   System.out.println("SQLState: " + e.getSQLState());
                   System.out.println("Message: " + e.getMessage());
                   System.out.println("Vendor: " + e.getErrorCode());
                   e = e.getNextException();
                   System.out.println("");
               }
           } catch (Exception e) {
               done = false;
               System.out.println("Exception: " + e);
               e.printStackTrace();
           }
        return done;
    }
    
    public String signIn() {
        String user = "";
        String encryptedPIN = "";
        try (Connection con = DBConnection.openConn();
            Statement statement = con.createStatement()){
            // SQL query command
            String SQL_Command;
            SQL_Command = "SELECT Name_Admin, Email_Admin, Encrypted_PIN FROM admin WHERE username ='" + account.getUsername() + "' AND Password_Admin ='" + encryptPIN(account.getPassword()) + "'";
            ResultSet Rslt = statement.executeQuery(SQL_Command);
            if(Rslt.next()){
                user="admin";
            }
            else {
                SQL_Command = "SELECT Name_Customer, Email_Customer, Encrypted_PIN FROM account WHERE username ='" + account.getUsername() + "' AND Password_Customer ='" + encryptPIN(account.getPassword()) + "'";
                Rslt = statement.executeQuery(SQL_Command);
                if(Rslt.next()) {
                    encryptedPIN = Rslt.getString("Encrypted_PIN");
                    user = "customer";
                }
            }
        } catch (SQLException e) {
            System.out.println("SQLException: " + e);
            while (e != null) {
                System.out.println("SQLState: " + e.getSQLState());
                System.out.println("Message: " + e.getMessage());
                System.out.println("Vendor: " + e.getErrorCode());
                e = e.getNextException();
                System.out.println("");
            }
        } catch (Exception e) {
            System.out.println("Exception: " + e);
            e.printStackTrace();
        }
        // Return the username of the signed-in user
        return user;
    }
    
    public boolean pinVerification(String user){
        String encryptedPIN = "";
        String SQL_Command = "";
        try (Connection con = DBConnection.openConn();
            Statement statement = con.createStatement()){
            if(user.equalsIgnoreCase("admin")){
                // SQL query command
                SQL_Command = "SELECT Name_Admin, Email_Admin, Encrypted_PIN FROM admin WHERE username ='" + account.getUsername() + "' AND Password_Admin ='" + encryptPIN(account.getPassword()) + "'";
                ResultSet RsltAdmin = statement.executeQuery(SQL_Command);
                if(RsltAdmin.next()){
                    encryptedPIN = RsltAdmin.getString("Encrypted_PIN");
                    if (verifyPIN(account.getPin(), encryptedPIN)) {
                        // email from the result set
                        String email = RsltAdmin.getString("Email_Admin");
                        // send sign-in email for Admin
                        emailNotification.sendSignInEmail(email, RsltAdmin.getString("Name_Admin"));
                        return true;
                    }
                }
            }else if(user.equalsIgnoreCase("customer")){
                SQL_Command = "SELECT Name_Customer, Email_Customer, Encrypted_PIN FROM account WHERE username ='" + account.getUsername() + "' AND Password_Customer ='" + encryptPIN(account.getPassword()) + "'";
                ResultSet RsltCust = statement.executeQuery(SQL_Command);
                if(RsltCust.next()) {
                    encryptedPIN = RsltCust.getString("Encrypted_PIN");
                    if (verifyPIN(account.getPin(), encryptedPIN)) {
                        // email from the result set
                        String email = RsltCust.getString("Email_Customer");

                        // send sign-in email
                        emailNotification.sendSignInEmail(email, RsltCust.getString("Name_Customer"));
                        return true;
                    }
                }
            }else return false;
        } catch (SQLException e) {
            System.out.println("SQLException: " + e);
            while (e != null) {
                System.out.println("SQLState: " + e.getSQLState());
                System.out.println("Message: " + e.getMessage());
                System.out.println("Vendor: " + e.getErrorCode());
                e = e.getNextException();
                System.out.println("");
            }
        } catch (Exception e) {
            System.out.println("Exception: " + e);
            e.printStackTrace();
        }
        // Return the username of the signed-in user
        return false;
    }
    
    public static Admin getAdminByUsername(String username){
        Admin admin = null;

        try (Connection conn = DBConnection.openConn();){
            String query = "SELECT * FROM admin WHERE username = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int ID = resultSet.getInt("ID_Admin");
                String name = resultSet.getString("Name_Admin");
                String phoneNum = resultSet.getString("PhoneNum_Admin");
                String email = resultSet.getString("Email_Admin");
                String password = resultSet.getString("Password_Admin");
                String DOB = resultSet.getString("DOB");
                String address = resultSet.getString("Address");
                String pin = resultSet.getString("Encrypted_PIN");

                admin = new Admin(pin, ID, username, name, password, phoneNum, email, DOB, address);
            }
            preparedStatement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return admin;
    }
    
    public static SilverSnitch getCustomerByAccountNumber(String accountNumber) {
        SilverSnitch customer = null;

        try (Connection conn = DBConnection.openConn();){
            String query = "SELECT * FROM account WHERE AccountNum = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, accountNumber);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String username = resultSet.getString("username");
                String name = resultSet.getString("Name_Customer");
                String phoneNum = resultSet.getString("PhoneNum_Customer");
                String email = resultSet.getString("Email_Customer");
                String password = resultSet.getString("Password_Customer");
                String DOB = resultSet.getString("DOB");
                String address = resultSet.getString("Address");
                String tier = resultSet.getString("Tier");
                String pin = resultSet.getString("Encrypted_PIN");

                customer = new SilverSnitch(accountNumber, tier, pin, username, name, password, phoneNum, email, DOB, address);
            }
            preparedStatement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return customer;
    }
    
    public static SilverSnitch getCustomerByUsername(String username) {
        SilverSnitch customer = null;

        try (Connection conn = DBConnection.openConn();){
            String query = "SELECT * FROM account WHERE username = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String accountNum = resultSet.getString("accountNum");
                String name = resultSet.getString("Name_Customer");
                String phoneNum = resultSet.getString("PhoneNum_Customer");
                String email = resultSet.getString("Email_Customer");
                String password = resultSet.getString("Password_Customer");
                String DOB = resultSet.getString("DOB");
                String address = resultSet.getString("Address");
                String tier = resultSet.getString("Tier");
                String pin = resultSet.getString("Encrypted_PIN");
                customer = new SilverSnitch(accountNum, tier, pin, username, name, password, phoneNum, email, DOB, address);
            }
            preparedStatement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return customer;
    }
    
    public static ArrayList<String> getCurrency(){
        ArrayList<String> currencys = new ArrayList<>();
        try (Connection con = DBConnection.openConn();
            Statement statement = con.createStatement()){
            String SQL_Command = "SHOW COLUMNS FROM account";
            ResultSet Rslt = statement.executeQuery(SQL_Command);
            int i = 1;
            while(Rslt.next()){
                i++;
                if(i>11)
                    currencys.add(Rslt.getString("Field"));
            }
        } catch (Exception e) {
            System.out.println("Exception: " + e);
            e.printStackTrace();
        }
        return currencys;
    }

    public static String encryptPIN(String pin) {
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
    
    public 

    static boolean verifyPIN(String pin, String encryptedPIN) {
        return encryptPIN(pin).equals(encryptedPIN);
    }
    
    public static void main(String[] args) {
        System.out.println(encryptPIN("1234"));;
    }
}

