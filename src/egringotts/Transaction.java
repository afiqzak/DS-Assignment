/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package egringotts;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.Vector;

/**
 *
 * @author USER
 */
public class Transaction {

    private String transactionID, sender, receipent, type, date, description;
    private int amount, sbalance, rbalance;

    public Transaction() {
    }
    

    public Transaction(String sender, String receipent, int amount, String type, String description) {
        this.sender = sender;
        this.receipent = receipent;
        this.amount = amount;
        this.type = type;
        this.description = description;
    }

    
    public String getTransactionId() throws SQLException {
        
        String newId = "1001"; // Default starting ID if no existing IDs are found
        try(Connection con = DBConnection.openConn();
            Statement statement = con.createStatement()){
            String SQL_Command = "SELECT MAX(ID_Transaction) FROM transaction";
            ResultSet Rslt = statement.executeQuery(SQL_Command);

        
            if (Rslt.next()) {
            String lastId = Rslt.getString(1);
            if (lastId != null) {
                // Increment the maximum ID by 1 to generate a new unique ID
                int newNumericPart = Integer.parseInt(lastId) + 1;
                newId = String.valueOf(newNumericPart);
            }
        }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return newId;
}

    
    public String recordTransaction() throws SQLException {
        String transactionID = null;
        
        try (Connection con = DBConnection.openConn();
            Statement statement = con.createStatement()) {
            // Generate a transaction ID
            transactionID = getTransactionId();
            
            // Get current date
            LocalDate currentDate = LocalDate.now();
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String formattedDate = currentDate.format(dateFormatter);

            // Get current time
            LocalTime currentTime = LocalTime.now();
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
            String formattedTime = currentTime.format(timeFormatter);
            
            // Fetch sender current balance from database
            String senderBalance = "SELECT Balance FROM account WHERE AccountNum = '" + sender + "'";
            ResultSet SBalanceResult = statement.executeQuery(senderBalance);
            if (SBalanceResult.next()) {
                sbalance = SBalanceResult.getInt("Balance");
            }
            // Update balance
            sbalance -= amount;
            
            // Fetch receipent current balance from database
            String receipentBalance = "SELECT Balance FROM account WHERE AccountNum = '" + receipent + "'";
            ResultSet RBalanceResult = statement.executeQuery(receipentBalance);
            if (RBalanceResult.next()) {
                rbalance = RBalanceResult.getInt("Balance");
            }
            
            // Update balance
            rbalance += amount;

            
            // Insert transaction into database
            String sql = "INSERT INTO transaction (ID_Transaction, Sender, Receipent, Amount, balance, Type, Date, Description) "
                    + "VALUES ('" + transactionID + "', '" + sender + "', '" + receipent + "', " + amount + ", " + sbalance + ", '" + type + "', '" + formattedDate + " " + formattedTime + "', '" + description + "')";
            statement.executeUpdate(sql);
            
            // Update the balance in the account table
            String updateSenderBalanceQuery = "UPDATE account SET Balance = " + sbalance + " WHERE AccountNum = '" + sender + "'";
            statement.executeUpdate(updateSenderBalanceQuery);
            
            String updateReceipentBalanceQuery = "UPDATE account SET Balance = " + rbalance + " WHERE AccountNum = '" + receipent + "'";
            statement.executeUpdate(updateReceipentBalanceQuery);
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transactionID;
    }

    public Vector searchTransaction(String startDate, String endDate) {
        Vector transactionRecords = new Vector();
        try (Connection con = DBConnection.openConn();
            Statement statement = con.createStatement()){

            // Select transactions between start and end dates
            String sql = "SELECT * FROM transaction WHERE Date BETWEEN '" + startDate + "' AND '" + endDate + "'";
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                Vector<String> transaction = new Vector<>();
                transaction.add(resultSet.getString("ID_Transaction"));
                transaction.add(resultSet.getString("Sender"));
                transaction.add(resultSet.getString("Receipent"));
                transaction.add(resultSet.getString("Amount"));
                transaction.add(resultSet.getString("Balance"));
                transaction.add(resultSet.getString("Type"));
                transaction.add(resultSet.getString("Date"));
                transaction.add(resultSet.getString("Description"));
                transactionRecords.add(transaction);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transactionRecords;
    }
    
    
}
