/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package egringotts;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

/**
 *
 * @author afiqz
 */
public class PlatinumPatronus extends SilverSnitch{
    private CurrencyExchange exchange = new CurrencyExchange();

    public PlatinumPatronus(String pin, String accountNum, String username, String name, String password, String phoneNum, String email, String dob, String address) {
        super(pin, accountNum, username, name, password, phoneNum, email, dob, address);
    }
    
    public double getPercentageTypeForMonth(String type, int targetMonth) {
        return getTypeSpendForMonth(type, targetMonth) / getTotalSpendForMonth(targetMonth) * 100;
    }
    
    public double getTotalSpendForMonth(int targetMonth){
        double total = 0.0;
        String SQL_Command = "SELECT\n" +
            "    SUM(CAST(SUBSTRING_INDEX(amount, ' ', 1) AS DECIMAL)) AS totalSpend,\n" +
            "    SUBSTRING_INDEX(amount, ' ', -1) AS currency\n" +
            "FROM transaction\n" +
            "WHERE MONTH(Date) = " + targetMonth + "\n" +
            "AND Sender='" + this.getKey() + "' \n" +
            "GROUP BY currency;";
        try (Connection con = DBConnection.openConn();
            PreparedStatement stmt = con.prepareStatement(SQL_Command)) {

            ResultSet rs = stmt.executeQuery(SQL_Command);
            
            double totalCurr;
            double spendTypeCurr;
            while(rs.next()){
                if(!(rs.getString("currency").equalsIgnoreCase("K"))){
                    totalCurr = exchange.exchange(rs.getString("currency"), "K", rs.getDouble("totalSpend"));
                }else{
                    totalCurr = rs.getDouble("totalSpend");
                }
                total += totalCurr;
            }
        } catch (SQLException e) {
          e.printStackTrace();
        }
        return total;
    }
    
    public double getTypeSpendForMonth(String type, int targetMonth){
        double spendType = 0.0;
        String SQL_Command = "SELECT\n" +
            "    SUM(CASE WHEN Type = '" + type + "' THEN amount ELSE 0 END) AS type_expenses,\n" +
            "    SUBSTRING_INDEX(amount, ' ', -1) AS currency\n" +
            "FROM transaction\n" +
            "WHERE MONTH(Date) = " + targetMonth + "\n" +
            "AND Sender='" + this.getKey() + "' \n" +
            "GROUP BY currency;";
        try (Connection con = DBConnection.openConn();
            PreparedStatement stmt = con.prepareStatement(SQL_Command)) {

            ResultSet rs = stmt.executeQuery(SQL_Command);
            
            double totalCurr;
            double spendTypeCurr;
            while(rs.next()){
                if(!(rs.getString("currency").equalsIgnoreCase("K"))){
                    spendTypeCurr = exchange.exchange(rs.getString("currency"), "K", rs.getDouble("type_expenses"));
                }else{
                    spendTypeCurr = rs.getDouble("type_expenses");
                }
                spendType += spendTypeCurr;
            }
        } catch (SQLException e) {
          e.printStackTrace();
        }
        return spendType;
    }
    
    public double getMonthlySpend(int month){
        double sum = 0.0;

        String SQL_Command = "SELECT SUM(CAST(SUBSTRING_INDEX(amount, ' ', 1) AS DECIMAL)) AS totalSpend,\n" +
            "       SUBSTRING_INDEX(amount, ' ', -1) AS currency\n" +
            "FROM transaction\n" +
            "WHERE YEAR(Date) = YEAR(CURDATE())\n" +
            "  AND Sender = ?\n" +
            "  AND MONTH(Date) = ?\n" +
            "GROUP BY YEAR(Date), MONTH(Date), currency";
        try (Connection con = DBConnection.openConn();
               PreparedStatement stmt = con.prepareStatement(SQL_Command)) {
            double amount = 0.0;

            stmt.setString(1, this.getKey()); // Set parameter with prepared statement
            stmt.setInt(2, month);
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

}
