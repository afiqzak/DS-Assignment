package egringotts;

import java.util.Random;
import java.sql.*;
import java.lang.*;
/**
 *
 * @author USER
 */
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
public class Customer implements User{
    private String accountNum;
    private String name;
    private String username;
    private String phoneNum;
    private String email;
    private String password;
    private String DOB;
    private String address;
    private Map<String, Double> balances;
    private String tier;

    public Customer() {
    }
    
    public Customer(String username, String name, String phoneNum, String email, String password, String DOB, String address){
        this.accountNum = newAccountNum();
        this.username = username;
        this.name = name;
        this.phoneNum = phoneNum;
        this.email = email;
        this.password = password;
        this.DOB = DOB;
        this.address = address;
        this.tier = setTier();
    }

    public Customer(String accountNum, String name, String username, String phoneNum, String email, String password, String DOB, String address, Map<String, Double> balances, String tier) {
        this.accountNum = accountNum;
        this.name = name;
        this.username = username;
        this.phoneNum = phoneNum;
        this.email = email;
        this.password = password;
        this.DOB = DOB;
        this.address = address;
        this.balances = balances;
        this.tier = tier;
    }
    
    
  
    public Map<String,Double> getBalances(){
        return balances;
    }
    
    public void setBalances(Map<String, Double> balances){
        this.balances = balances;
    }
 
// Implement User interface methods

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

    public String getAccountNum() {
        return accountNum;
    }

    public String getTier() {
        return tier;
    }
    
    public double getBalance(String currency) {
    double balance = 0;
    try(Connection DBConn = DBConnection.openConn();
        Statement Stmt = DBConn.createStatement();) {
        

        String SQL_Command = "SELECT " + currency + " FROM account WHERE AccountNum = '" + this.accountNum+ "'";
        
        ResultSet Rslt = Stmt.executeQuery(SQL_Command);
        if (Rslt.next()) {
            balance = Rslt.getInt(1);
        }
        
        Stmt.close();
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return balance;
}


    public void updateBalanceSender(double newBalance, String currency) throws SQLException {
        PreparedStatement pstmt = null;
    
        try(Connection DBConn = DBConnection.openConn();) {
            String accountNum = getAccountN();
            if (accountNum == null) {
                throw new SQLException("Account number is null");
            }
        
            String SQL_Command = "UPDATE account SET " + currency + " = ? WHERE AccountNum = ?";
            pstmt = DBConn.prepareStatement(SQL_Command);
            pstmt.setDouble(1, newBalance);
            pstmt.setString(2, accountNum);
        
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Failed to update balance, no rows affected.");
            }
        
            System.out.println("Balance updated successfully for account: " + accountNum);
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void updateBalanceRecipient(double newBalance, String currency, String accountNum) throws SQLException {
        DBConnection ToDB = new DBConnection();
        Connection DBConn = ToDB.openConn();
        PreparedStatement pstmt = null;
    
            String SQL_Command = "UPDATE account SET " + currency + "Balance = ? WHERE AccountNum = ?";
            pstmt = DBConn.prepareStatement(SQL_Command);
            pstmt.setDouble(1, newBalance);
            pstmt.setString(2, accountNum);
        
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Failed to update balance, no rows affected.");
            }
        
            System.out.println("Balance updated successfully for account: " + accountNum);
    }
    
    public void updateCustomerPassword(String password) throws SQLException {
        DBConnection ToDB = new DBConnection();
        Connection DBConn = ToDB.openConn();
        PreparedStatement pstmt = null;
        
        String SQL_Command ="UPDATE account SET password = " + password + "WHERE AccounntNum = ?";
        pstmt = DBConn.prepareStatement(SQL_Command);
        pstmt.setString(1, this.accountNum);
    }
    
    public String setTier() {
        if (getBalance("Knut") < 10000) {
            return "Silver Snitch";
        }
        else if (getBalance("Knut") >= 10000 && getBalance("Knut") < 50000) {
            return "Golden Galleon";
        }
        else if (getBalance("Knut") >= 50000) {
            return "Platinum Patronus";
        }
        return null;
    }
    
    public String generateAccountNum() {
        int n1, n2, n3;
        Random r = new Random();
        n1 = r.nextInt(10000);
        n2 = r.nextInt(10000);
        n3 = r.nextInt(10000);
        String accountNum = String.format("%04d", n1) + " " + String.format("%04d", n2) + " " + String.format("%04d", n3);
        return accountNum;
    }
    
    public String newAccountNum() {
        String accountNum;
        boolean foundDuplicate = true; // Assume duplicate initially to enter the loop

        do {
          accountNum = generateAccountNum(); // Generate random number

          try (Connection con = DBConnection.openConn();
               PreparedStatement stmt = con.prepareStatement("SELECT 1 FROM account WHERE AccountNum = ?")) {

            stmt.setString(1, accountNum); // Set parameter with prepared statement
            ResultSet rs = stmt.executeQuery();

            foundDuplicate = rs.next(); // Check if a row exists (duplicate)
          } catch (SQLException e) {
            e.printStackTrace();
          }
        } while (foundDuplicate);

        return accountNum;
      }
    
    public String getAccountN() {
        //return the actual account number from the database
        String accountNum = null;
        try(Connection DBConn = DBConnection.openConn();
            Statement Stmt = DBConn.createStatement();) {
            
            String SQL_Command = "SELECT AccountNum FROM account WHERE username = '" + this.getUsername() + "'";
            ResultSet Rslt = Stmt.executeQuery(SQL_Command);
            if (Rslt.next()) {
                accountNum = Rslt.getString("AccountNum");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return accountNum;
    }
    
    public double getPercentageType(String type){
        int total = 0, numType = 0;
        String SQL_Command = "SELECT COUNT(*) FROM transaction WHERE sender='" + this.accountNum + "';";
        try (Connection con = DBConnection.openConn();
             Statement stmt = con.prepareStatement(SQL_Command)) {

            ResultSet rs = stmt.executeQuery(SQL_Command);

            if(rs.next()){
                total = rs.getInt(1);
            }
            
            SQL_Command = "SELECT COUNT(*) FROM transaction WHERE sender='" + this.accountNum + "' AND Type='" + type + "';";
            
            rs = stmt.executeQuery(SQL_Command);
            
            if(rs.next()){
                numType = rs.getInt(1);
            }
        } catch (SQLException e) {
          e.printStackTrace();
        }
        return (double) numType / total * 100;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }  
    
    public static void main(String[] args) {
        Customer cust = Account.getCustomerByUsername("ali");
        System.out.println(cust.getPercentageType("entertainment"));
    }
}

