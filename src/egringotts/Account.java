
package egringotts;

import java.lang.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author USER
 */
public class Account <E extends User>{
    private E account;

    public Account(E account) {
        this.account = account;
    } 
 
    public boolean signUp(String currency, double amount) {
        boolean done = true;
        String SQL_Command = "SELECT username FROM account WHERE username ='" + account.getUsername() + "'"; 
            try (Connection con = DBConnection.openConn();
                Statement statement = con.prepareStatement(SQL_Command)){
                // Inquire if the username exists.
                ResultSet Rslt = statement.executeQuery(SQL_Command); 
                if(Rslt.next()) return false;
                if (done) {
                    // Save the user details
                       SQL_Command = "INSERT INTO account(AccountNum, Name_Customer, username, PhoneNum_Customer, Email_Customer, Password_Customer, DOB, Address, Tier, " + currency + ") " +
                                    "VALUES ('" + account.generateKey() + "','" + account.getName() + "','" + account.getUsername() + "','" + account.getPhoneNum() + "','" +
                                    account.getEmail() + "','" + account.getPassword() + "','" + account.getDob() + "','" + account.getAddress() + "', '" + account.setTier() + "', " + amount + ")";
                       statement.executeUpdate(SQL_Command);
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
        try (Connection con = DBConnection.openConn();
            Statement statement = con.createStatement()){
            // SQL query command
            String SQL_Command;
            SQL_Command = "SELECT Name_Admin FROM admin WHERE username ='" + account.getUsername() + "' AND Password_Admin ='" + account.getPassword() + "'";
            ResultSet Rslt = statement.executeQuery(SQL_Command);
            if(Rslt.next()) user = "admin";
            else {
                SQL_Command = "SELECT Name_Customer FROM account WHERE username ='" + account.getUsername() + "' AND Password_Customer ='" + account.getPassword() + "'";
                Rslt = statement.executeQuery(SQL_Command);
                if(Rslt.next()) user = "customer";
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
                /*Map<String, java.lang.Double> balances = new HashMap<>();
                balances.put("Knut", resultSet.getDouble("KnutBalance"));
                balances.put("Sickle", resultSet.getDouble("SickleBalance"));
                balances.put("Galleon", resultSet.getDouble("GalleonBalance"));*/

                admin = new Admin(ID, name, username, phoneNum, email, password, DOB, address);
            }
            preparedStatement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return admin;
    }
    
    public static GoldenGalleon getCustomerByAccountNumber(String accountNumber) {
        GoldenGalleon customer = null;

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
                Map<String, java.lang.Double> balances = new HashMap<>();
                balances.put("Knut", resultSet.getDouble("Knut"));
                balances.put("Sickle", resultSet.getDouble("Sickle"));
                balances.put("Galleon", resultSet.getDouble("Galleon"));

                customer = new GoldenGalleon(accountNumber, balances, tier, username, name, password, phoneNum, email, DOB, address);
            }
            preparedStatement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return customer;
    }
    
    public static GoldenGalleon getCustomerByUsername(String username) {
        GoldenGalleon customer = null;

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
                Map<String, java.lang.Double> balances = new HashMap<>();
                balances.put("Knut", resultSet.getDouble("Knut"));
                balances.put("Sickle", resultSet.getDouble("Sickle"));
                balances.put("Galleon", resultSet.getDouble("Galleon"));
                customer = new GoldenGalleon(accountNum, balances, tier, username, name, password, phoneNum, email, DOB, address);
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
                if(i>10)
                    currencys.add(Rslt.getString("Field"));
            }
        } catch (Exception e) {
            System.out.println("Exception: " + e);
            e.printStackTrace();
        }
        return currencys;
    }
}



