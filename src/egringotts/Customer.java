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
    
    private int userId;
    private String name;
    private String username;
    private String phoneNum;
    private String email;
    private String password;
    private String DOB;
    private String address;
    private Map<String, Double> balances;
    private String tier;
    
    
    public Customer(String username, String password) {
        this.username = username;
        this.password = password;
    }
    
    public Customer(String username, String name, String phoneNum, String email, String password, String DOB, String address){
        this.username = username;
        this.name = name;
        this.phoneNum = phoneNum;
        this.email = email;
        this.password = password;
        this.DOB = DOB;
        this.address = address;
    }

    public Customer(String username, String name, String phoneNum, String email, String password, String DOB, String address, Map<String, Double> balances) {
        this.userId = userId;
        this.name = name;
        this.username = username;
        this.phoneNum = phoneNum;
        this.email = email;
        this.password = password;
        this.DOB = DOB;
        this.address = address;
        this.balances = balances;
    }
    
    
    
    public Map<String,Double> getBalances(){
        return balances;
    }
    
    public void setBalances(Map<String, Double> balances){
        this.balances = balances;
    }
 
// Implement User interface methods
    @Override
    public int getUserId() {
        return userId;
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

    public int getBalance(String currency) {
    int balance = 0;
    try(Connection DBConn = DBConnection.openConn();
        Statement Stmt = DBConn.createStatement();) {
        

        String SQL_Command = "SELECT " + currency + "Balance FROM account WHERE username = '" + this.getUsername()+ "'";
        
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
        
            String SQL_Command = "UPDATE account SET " + currency + "Balance = ? WHERE AccountNum = ?";
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
    



    
    public String getTier() {
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
    
    public String getAccountNum() {
        Random r = new Random();
        int num1 = r.nextInt(10000);
        int num2 = r.nextInt(10000);
        int num3 = r.nextInt(10000);
        String accountnum= String.format("%04d", num1) + " " + String.format("%04d", num2) + " " + String.format("%04d", num3);
        return accountnum;
    }
    
    public String getAccountN() {
        //return the actual account number from the database
        String accountNum = null;
        try(Connection DBConn = DBConnection.openConn();
            Statement Stmt = DBConn.createStatement();) {
            
            String SQL_Command = "SELECT AccountNum FROM account WHERE username = '" + this.getUsername() + "'";
            ResultSet Rslt = Stmt.executeQuery(SQL_Command);
            if (Rslt.next()) {
                accountNum = Rslt.getString(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return accountNum;
    }
    

    @Override
    public void setName(String name) {
        this.name = name;
    }  
}

