package egringotts;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Vector;

/**
 *
 * @author USER
 */
public class Admin implements User {

    private String accountNum;
    private String name;
    private String username;
    private String password;
    private String phoneNum;
    private String email;
    private String DOB;
    private String address;

    // Constructor
    public Admin(String accountNum, String username, String name, String phoneNum, String email, String password, String DOB, String address) {
        this.accountNum = accountNum;
        this.username = username;
        this.name = name;
        this.phoneNum = phoneNum;
        this.email = email;
        this.password = password;
        this.DOB = DOB;
        this.address = address;
    }

    public Admin(String username, String password) {
        this.username = username;
        this.password = password;
    }
    
    

    // Implement User interface methods
    public int getUserId() throws SQLException{
        int newId = 1;
        try (Connection con = DBConnection.openConn();
            Statement statement = con.createStatement()){
            String SQL_Command = "SELECT MAX(AccountNum) FROM admin";
            ResultSet Rslt = statement.executeQuery(SQL_Command);

            if (Rslt.next()) {
                // Retrieve the maximum ID from the result set
                int maxId = Rslt.getInt(1);
                // If maxId is 0, it means there are no existing admin records, so set newId to 1

                // Increment the maximum ID by 1 to generate a new unique ID
                newId = maxId + 1;

            }
        }catch (SQLException e ){
            e.printStackTrace();
        }
        return newId;
    }
    
    public void addAdmin(String accountNum, String name, String phoneNum, String email, String username, String password, String dob, String address){
        try (Connection con = DBConnection.openConn();
                Statement statement = con.createStatement()){
        String SQL_Command = "INSERT INTO admin (AccountNum, Name_Admin, PhoneNum_Admin, Email_Admin, username, Password_Admin, DOB, address) " +
                                    "VALUES ('" + accountNum + "','" + name + "','" + phoneNum + "','" + email + "','" +
                                    username + "','" + password + "','" + dob + "','" + address + "')";
        statement.executeUpdate(SQL_Command);
        }
        catch (SQLException e) {
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
    }
    
    public ArrayList<Customer> tableUser() {
        ArrayList<Customer> users = new ArrayList<>();
        String query = "SELECT * FROM account";
        
        try (Connection connection = DBConnection.openConn();
            PreparedStatement ps = connection.prepareStatement(query)) {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                do {
                  // Process the result set and create Transaction objects
                  users.add(Account.getCustomerByAccountNumber(rs.getString("AccountNum")));
                } while (rs.next());
              }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }
    
    public void updateAdminPassword(String password) throws SQLException {
        String SQL_Command ="UPDATE admin SET Password_Admin = '" + password + "' WHERE AccountNum = ?";
        try(Connection con = DBConnection.openConn();
            PreparedStatement statement = con.prepareStatement(SQL_Command)){
            statement.setString(1, this.getAccountNum());
            System.out.println(statement);
            statement.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public String getAccountNum() {
        return accountNum;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getPhoneNum() {
        return phoneNum;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public String getDOB() {
        return DOB;
    }

    @Override
    public String getAddress() {
        return address;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }
}
