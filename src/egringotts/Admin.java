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
public class Admin extends User {

    private int ID;
    private String pin;

    // Constructor

    public Admin(String pin, int ID, String username, String name, String password, String phoneNum, String email, String dob, String address) {
        super(username, name, password, phoneNum, email, dob, address);
        this.ID = ID;
        this.pin = pin;
    }

    public Admin(String username, String password) {
        super(username, password);
    }

    public Admin(String pin, String username, String name, String password, String phoneNum, String email, String dob, String address) throws SQLException {
        super(username, name, password, phoneNum, email, dob, address);
        this.ID = getUserId();
        this.pin = pin;
    }

    // Implement User interface methods
    public int getUserId() throws SQLException{
        int newId = 1;
        try (Connection con = DBConnection.openConn();
            Statement statement = con.createStatement()){
            String SQL_Command = "SELECT MAX(ID_Admin) FROM admin";
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
    
    public String getTotalUser(){
        String sql = "SELECT COUNT(*) FROM account";
        try (Connection con = DBConnection.openConn();
             Statement statement = con.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {
          if (rs.next()) {
            return rs.getString(1);
          } 
        }catch (Exception e) {
           System.out.println("Exception: " + e);
           e.printStackTrace();
       }
        return null;
    }
    
    public String getWeekTrans(){
        String sql = "SELECT COUNT(*) from transaction\n" +
            "where WEEK(Date) = WEEK(CURDATE());";
        try (Connection con = DBConnection.openConn();
             Statement statement = con.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {
          if (rs.next()) {
            return rs.getString(1);
          } 
        }catch (Exception e) {
           System.out.println("Exception: " + e);
           e.printStackTrace();
       }
        return null;
    }
    
    public int getMonthTrans(int month){
        String sql = "SELECT COUNT(*) from transaction\n" +
            "where MONTH(Date) = " + month + ";";
        try (Connection con = DBConnection.openConn();
             Statement statement = con.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {
          if (rs.next()) {
            return rs.getInt(1);
          } 
        }catch (Exception e) {
           System.out.println("Exception: " + e);
           e.printStackTrace();
       }
        return 0;
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
    
    public int getNewCode() throws SQLException {
      String sql = "SELECT MAX(code) FROM currency";
      try (Connection con = DBConnection.openConn();
           Statement statement = con.createStatement();
           ResultSet rs = statement.executeQuery(sql)) {
        if (rs.next()) {
          return rs.getInt(1) + 1;
        } else {
          // Handle case where no records exist (e.g., return 1)
          return 1;
        }
      }
    }

    public void addCurrency(String newCurrName, String symbol, String existingCurrency, float rate, double proFee) throws SQLException {
      int code = 0;
      boolean duplicateName = false;
      
      try (Connection con = DBConnection.openConn();
           Statement statement = con.createStatement();
           ResultSet rs = statement.executeQuery("SELECT symbol, code from currency")) {
        while(rs.next()){
            if(rs.getString("symbol").equals(symbol)){
                duplicateName = true;
                code = rs.getInt("code");
            }
        }
      }
      
      String sqlInsertCurrency = "INSERT INTO currency (code, symbol, display_name) VALUES (?, ?, ?);";
      String sqlGetExistingId = "SELECT code FROM currency WHERE symbol = '" + existingCurrency + "';";
      String sqlInsertExchangeRate = "INSERT INTO exchange_rate (currency_code_from, currency_code_to, rate, fee_rate) VALUES (?, ?, ?, ?);";
      String sqlInsertCurrencyAccount = "ALTER TABLE account ADD " + symbol + " float default 0.0;";

        try (Connection connection = DBConnection.openConn()) {
            // Separate prepared statements for security
            try (PreparedStatement psInsertCurrency = connection.prepareStatement(sqlInsertCurrency);
                 PreparedStatement psGetExistingId = connection.prepareStatement(sqlGetExistingId);
                 PreparedStatement psInsertExchangeRate = connection.prepareStatement(sqlInsertExchangeRate);
                 PreparedStatement psInsertCurrencyAcc = connection.prepareStatement(sqlInsertCurrencyAccount)) {
                
                if(!duplicateName){
                    // Set parameters for insert currency
                    code = getNewCode();
                    psInsertCurrency.setInt(1, code);
                    psInsertCurrency.setString(2, symbol);
                    psInsertCurrency.setString(3, newCurrName);
                    psInsertCurrency.executeUpdate();
                    
                    System.out.println(psInsertCurrencyAcc);
                    psInsertCurrencyAcc.executeUpdate();
                }

                // Get existing currency ID
                ResultSet rs = psGetExistingId.executeQuery(sqlGetExistingId);
                int codeTo = 0;
                if(rs.next())
                    codeTo = rs.getInt("code");

                // Set parameters for insert exchange rate
                psInsertExchangeRate.setInt(1, code);
                psInsertExchangeRate.setInt(2, codeTo);
                psInsertExchangeRate.setDouble(3, rate);
                psInsertExchangeRate.setDouble(4, proFee);
                psInsertExchangeRate.executeUpdate();

                
            }
        }
    }
    
    public ArrayList<SilverSnitch> tableUser() {
        ArrayList<SilverSnitch> users = new ArrayList<>();
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
        String SQL_Command ="UPDATE admin SET Password_Admin = '" + Account.encryptPIN(password) + "' WHERE AccountNum = ?";
        try(Connection con = DBConnection.openConn();
            PreparedStatement statement = con.prepareStatement(SQL_Command)){
            statement.setString(1, String.valueOf(this.ID));
            System.out.println(statement.toString());
            statement.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public String getKey() {
        return String.valueOf(ID);
    }

    @Override
    public String setTier() {
        return "Goblin";
    }

    @Override
    public String generateKey() {
        int newId = 1;
        try (Connection con = DBConnection.openConn();
            Statement statement = con.createStatement()){
            String SQL_Command = "SELECT MAX(ID_Admin) FROM admin";
            ResultSet Rslt = statement.executeQuery(SQL_Command);

            if (Rslt.next()) {
                // Retrieve the maximum ID from the result set
                int maxId = Integer.valueOf(Rslt.getString(1));
                // If maxId is 0, it means there are no existing admin records, so set newId to 1

                // Increment the maximum ID by 1 to generate a new unique ID
                newId = maxId + 1;

            }
        }catch (SQLException e ){
            e.printStackTrace();
        }
        return String.valueOf(newId);
    }
}
