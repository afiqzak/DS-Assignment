package egringotts;

import java.util.Random;
import java.sql.*;
import java.lang.*;
import java.text.DecimalFormat;
/**
 *
 * @author USER
 */
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
public class SilverSnitch extends User{
    private String accountNum;
    private String tier;
    private CurrencyExchange exchange = new CurrencyExchange();

    public SilverSnitch(String accountNum, String tier, String username, String name, String password, String phoneNum, String email, String dob, String address) {
        super(username, name, password, phoneNum, email, dob, address);
        this.accountNum = accountNum;
        this.tier = tier;
    }

    public SilverSnitch(String username, String password) {
        super(username, password);
    }

    public SilverSnitch(String username, String name, String password, String phoneNum, String email, String dob, String address) {
        super(username, name, password, phoneNum, email, dob, address);
        this.accountNum = generateAccountNum();
        this.tier = setTier();
    } 

    public SilverSnitch(String accountNum, String username, String name, String password, String phoneNum, String email, String dob, String address) {
        super(username, name, password, phoneNum, email, dob, address);
        this.accountNum = accountNum;
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
        double total = 0.0;
        double spendType = 0.0;
        String SQL_Command = "SELECT\n" +
            "    SUM(CASE WHEN Type = '" + type + "' THEN amount ELSE 0 END) AS type_expenses,\n" +
            "    SUM(CAST(SUBSTRING_INDEX(amount, ' ', 1) AS DECIMAL)) AS totalSpend,\n" +
            "    SUBSTRING_INDEX(amount, ' ', -1) AS currency\n" +
            "FROM transaction\n" +
            "WHERE MONTH(Date) = MONTH(CURDATE())\n" +
            "AND Sender='" + this.accountNum + "' \n" +
            "GROUP BY currency;";
        try (Connection con = DBConnection.openConn();
            PreparedStatement stmt = con.prepareStatement(SQL_Command)) {

            ResultSet rs = stmt.executeQuery(SQL_Command);
            
            double totalCurr;
            double spendTypeCurr;
            while(rs.next()){
                if(!(rs.getString("currency").equalsIgnoreCase("K"))){
                    totalCurr = exchange.exchange(rs.getString("currency"), "K", rs.getDouble("totalSpend"));
                    spendTypeCurr = exchange.exchange(rs.getString("currency"), "K", rs.getDouble("type_expenses"));
                }else{
                    totalCurr = rs.getDouble("totalSpend");
                    spendTypeCurr = rs.getDouble("type_expenses");
                }
                total += totalCurr;
                spendType += spendTypeCurr;
            }
        } catch (SQLException e) {
          e.printStackTrace();
        }
        double percentage = (spendType / total) * 100;
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        return Double.parseDouble(decimalFormat.format(percentage));
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
        String SQL_Command = "SELECT DATE(Date) AS day, \n" +
            "SUBSTRING_INDEX(amount, ' ', -1) AS currency, \n" +
            "SUM(CAST(SUBSTRING_INDEX(amount, ' ', 1) AS DECIMAL)) AS totalSpend \n" +
            "FROM transaction \n" +
            "WHERE DAYOFWEEK(Date)=? AND WEEK(Date) = WEEK(CURDATE()) AND Sender=? \n" +
            "GROUP BY DATE(Date), currency;";
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
    
    public double getSumCard(String method, String day){
        double sum = 0.0;
        String[] days = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
        
        int index = -1; // Initialize with an invalid index

        for (int i = 0; i < days.length; i++) {
            if (days[i].equals(day)) {
                index = i+1;
                break; // Exit the loop once found
            }
        }
        String SQL_Command = "SELECT DATE(Date) AS day, \n" +
            "SUBSTRING_INDEX(amount, ' ', -1) AS currency, \n" +
            "SUM(CAST(SUBSTRING_INDEX(amount, ' ', 1) AS DECIMAL)) AS totalSpend \n" +
            "FROM transaction \n" +
            "WHERE DAYOFWEEK(Date)=? AND WEEK(Date) = WEEK(CURDATE()) AND Sender=? AND method=? \n" +
            "GROUP BY DATE(Date), currency;";
        try (Connection con = DBConnection.openConn();
               PreparedStatement stmt = con.prepareStatement(SQL_Command)) {
            double amount = 0.0;

            stmt.setString(1, String.valueOf(index)); // Set parameter with prepared statement
            stmt.setString(2, this.accountNum);
            stmt.setString(3, method);
            ResultSet rs = stmt.executeQuery();
            
            while(rs.next()){
                if(!(rs.getString("currency").equalsIgnoreCase("K"))){
                    amount = exchange.exchange(rs.getString("currency"), "K", rs.getDouble("totalSpend"));
                }else
                    amount = rs.getDouble("totalSpend");
                sum += amount;
                System.out.println(amount);
            }
            return sum;
          } catch (SQLException e) {
            e.printStackTrace();
          }
        return sum;
    }
}

