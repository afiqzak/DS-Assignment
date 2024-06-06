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
public class Customer extends User{
    private String accountNum;
    private Map<String, Double> balances;
    private String tier;
    private CurrencyExchange exchange = new CurrencyExchange();

    public Customer(String accountNum, Map<String, Double> balances, String tier, String username, String name, String password, String phoneNum, String email, String dob, String address) {
        super(username, name, password, phoneNum, email, dob, address);
        this.accountNum = accountNum;
        this.balances = balances;
        this.tier = tier;
    }

    public Customer(String username, String password) {
        super(username, password);
    }

    public Customer(String username, String name, String password, String phoneNum, String email, String dob, String address) {
        super(username, name, password, phoneNum, email, dob, address);
        this.accountNum = generateAccountNum();
        this.tier = setTier();
    } 

    public Customer(String accountNum, Map<String, Double> balances, String username, String name, String password, String phoneNum, String email, String dob, String address) {
        super(username, name, password, phoneNum, email, dob, address);
        this.accountNum = accountNum;
        this.balances = balances;
    }
    
    
  
    public Map<String,Double> getBalances(){
        return balances;
    }
    
    public void setBalances(Map<String, Double> balances){
        this.balances = balances;
    }

    public String getKey() {
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
    
    @Override
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
    
    public double getTotalSpendByDay(String day){
        double sum = 0.0;
        String[] days = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
        
        int index = -1; // Initialize with an invalid index

        for (int i = 0; i < days.length; i++) {
            if (days[i].equals(day)) {
                index = i+1;
                break; // Exit the loop once found
            }
        }
        String SQL_Command = "SELECT DATE(Date) AS day, "
                + "SUBSTRING_INDEX(amount, ' ', -1) AS currency, "
                + "SUM(CAST(SUBSTRING_INDEX(amount, ' ', 1) AS DECIMAL)) AS totalSpend "
                + "FROM transaction "
                + "WHERE DAYOFWEEK(Date)=? AND DATE(Date) >= DATE_SUB(CURDATE(), INTERVAL 1 WEEK) AND Sender=? "
                + "GROUP BY DATE(Date), currency;";
        try (Connection con = DBConnection.openConn();
               PreparedStatement stmt = con.prepareStatement(SQL_Command)) {
            double amount = 0.0;

            stmt.setString(1, String.valueOf(index)); // Set parameter with prepared statement
            stmt.setString(2, this.accountNum);
            ResultSet rs = stmt.executeQuery();
            
            while(rs.next()){
                if(!(rs.getString("currency").equalsIgnoreCase("K"))){
                    amount = exchange.exchange(rs.getString("currency"), "K", rs.getDouble("totalSpend"));
                }else
                    amount = rs.getDouble("totalSpend");
                sum += amount;
            }
            return sum;
          } catch (SQLException e) {
            e.printStackTrace();
          }
        return sum;
    } 
    
    public static void main(String[] args) {
        Customer cust = Account.getCustomerByUsername("ali");
        System.out.println(cust.getTotalSpendByDay("Wednesday"));
    }

    @Override
    public String generateKey() {
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
}

