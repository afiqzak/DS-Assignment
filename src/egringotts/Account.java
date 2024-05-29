
package egringotts;

import java.lang.*;
import java.sql.*;

/**
 *
 * @author USER
 */
public class Account {
 
    public static boolean signUp(Customer user) {
        boolean done = !user.getUsername().equals("") && !user.getPassword().equals("") && !user.getAddress().equals("") && !user.getName().equals("") && !user.getPhoneNum().equals("") && !user.getEmail().equals("") && !user.getDOB().equals("") && (user.getBalance("Knut")!=0 || user.getBalance("Sickle")!=0 || user.getBalance("Galleon")!= 0);

            if (done) {
                try (Connection con = DBConnection.openConn();
                    Statement statement = con.createStatement()){
                    // SQL query command
                    String SQL_Command = "SELECT username FROM account WHERE username ='" + user.getUsername() + "'"; 
                    // Inquire if the username exists.
                    ResultSet Rslt = statement.executeQuery(SQL_Command); 
                    done = done && !Rslt.next();
                    if (done) {
                        // Save the user details
                           SQL_Command = "INSERT INTO account(AccountNum, Username, Name_Customer, PhoneNum_Customer, Email_Customer, Password_Customer, DOB, Address, KnutBalance, SickleBalance, GalleonBalance, Tier) " +
                                        "VALUES ('" + user.getAccountNum() + "','" + user.getUsername() + "','" + user.getName() + "','" + user.getPhoneNum() + "','" +
                                        user.getEmail() + "','" + user.getPassword() + "','" + user.getDOB() + "','" + user.getAddress() + "','" +
                                        user.getBalance("Knut") + "','" + user.getBalance("Sickle") + "','" + user.getBalance("Galleon") + "','" + 
                                        user.getTier() + "')";
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
        return false;
    }


    public static String signIn(String username, String pass) {
        String user = "";
        try (Connection con = DBConnection.openConn();
            Statement statement = con.createStatement()){
            // SQL query command
            String SQL_Command;
            SQL_Command = "SELECT Name_Admin FROM admin WHERE Name_Admin ='" + username + "' AND Password_Admin ='" + pass + "'";
            ResultSet Rslt = statement.executeQuery(SQL_Command);
            if(Rslt.next()) user = "admin";
            else {
                SQL_Command = "SELECT Name_Customer FROM account WHERE username ='" + username + "' AND Password_Customer ='" + pass + "'";
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
}



