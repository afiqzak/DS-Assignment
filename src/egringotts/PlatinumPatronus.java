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
public class PlatinumPatronus extends Customer{
    private CurrencyExchange exchange = new CurrencyExchange();

    public PlatinumPatronus(String accountNum, Map<String, Double> balances, String username, String name, String password, String phoneNum, String email, String dob, String address) {
        super(accountNum, balances, username, name, password, phoneNum, email, dob, address);
    }
    
    public double getPercentageTypeForMonth(String type, int targetMonth) {
        double total = 0.0;
        double spendType = 0.0;
        String SQL_Command = "SELECT\n" +
            "    SUM(CASE WHEN Type = '" + type + "' THEN amount ELSE 0 END) AS type_expenses,\n" +
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
        return (double) spendType / total * 100;
    }
    
    public static void main(String[] args) {
        Customer cust = Account.getCustomerByUsername("ali");
        PlatinumPatronus plat = new PlatinumPatronus(cust.getKey(), cust.getBalances(), cust.getUsername(), cust.getName(), cust.getPassword(), cust.getPhoneNum(), cust.getEmail(), cust.getDob(), cust.getAddress());
        System.out.println(plat.getPercentageTypeForMonth("entertainment", 5));
    }

}
