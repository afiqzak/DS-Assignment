package egringotts;

import java.lang.*;
import java.sql.*;

/**
 *
 * @author USER
 */
public class Account<T extends User> {
    
    private T user;
    
    public Account(T user) {
        this.user = user;
    }
   
    

    
    public boolean signUp() {
        boolean done = !user.getUsername().equals("") && !user.getPassword().equals("");
        if (user instanceof Customer) {
            Customer customer = (Customer) user;
            done = !customer.getAddress().equals("") && done && !customer.getName().equals("") && !customer.getPhoneNum().equals("") && !customer.getEmail().equals("") && !customer.getDOB().equals("") && customer.getBalance()!=0;
        }
        else if (user instanceof Admin) {
            Admin admin = (Admin) user;
            done = done && !admin.getName().equals("") && !admin.getPhoneNum().equals("") && !admin.getEmail().equals("") && !admin.getDOB().equals("") && !admin.getAddress().equals("");
        }
        
        
            if (done) {
                try (Connection con = DBConnection.openConn();
                    Statement statement = con.createStatement()){
                    // SQL query command
                    String SQL_Command = "SELECT username FROM account WHERE username ='" + user.getUsername() + "'"; 
                    // Inquire if the username exists.
                    ResultSet Rslt = statement.executeQuery(SQL_Command); 
                    done = done && !Rslt.next();
                    if (done) {
                        // Save the user details based on the type
                        if (user instanceof Customer) {
                            Customer customer = (Customer) user;
                            SQL_Command = "INSERT INTO account(AccountNum, username, Name_Customer, PhoneNum_Customer, Email_Customer, Password_Customer, DOB, Address, Balance, Tier) " +
                                          "VALUES ('"+customer.getAccountNum()+ "','"+customer.getUsername()+ "','"+customer.getName()+ "','"+customer.getPhoneNum()+ "','"+customer.getEmail()+ "','"+customer.getPassword()+ "','"+customer.getDOB()+ "','"+customer.getAddress()+ "','"+customer.getBalance()+ "','"+customer.getTier()+ "')"; 
                        } else if (user instanceof Admin) {
                            Admin admin = (Admin) user;
                            SQL_Command = "INSERT INTO admin(ID_Admin, username, Name_Admin, PhoneNum_Admin, Email_Admin, Password_Admin, DOB, Address) VALUES ('"+admin.getUserId()+ "','"+admin.getUsername()+  "','"+admin.getName()+ "','"+admin.getPhoneNum()+ "','"+admin.getEmail()+ "','"+admin.getPassword()+ "','"+admin.getDOB()+ "','"+admin.getAddress()+ "')"; 
                        }
                        statement.executeUpdate(SQL_Command);
                    }
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
        return false;
    }


    public String signIn() {
    boolean done = !user.getUsername().equals("") && !user.getPassword().equals("");
    try (Connection con = DBConnection.openConn();
        Statement statement = con.createStatement()){
        if (done) {
            // SQL query command
            String SQL_Command;
            if (user instanceof Customer) {
                Customer customer = (Customer) user;
                SQL_Command = "SELECT Name_Customer FROM account WHERE username ='" + customer.getUsername() + "' AND Password_Customer ='" + customer.getPassword() + "'";
            } else if (user instanceof Admin) {
                Admin admin = (Admin) user;
                SQL_Command = "SELECT Name_Admin FROM admin WHERE username ='" + admin.getUsername() + "' AND Password_Admin ='" + admin.getPassword() + "'";
            } else {
                return null; // Unsupported user type
            }
            // Inquire if the username and password exist.
            ResultSet Rslt = statement.executeQuery(SQL_Command); 
            done = done && Rslt.next();
            if (done) {
                // Retrieve the username from the result set
                String name = Rslt.getString(1);
                // Set the username in the user object
                user.setName(name);
            }
        }
    } catch (SQLException e) {
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
    // Return the username of the signed-in user
    return done ? user.getName() : null;
}

}



