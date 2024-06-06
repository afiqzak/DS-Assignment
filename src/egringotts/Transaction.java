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

    private String transactionID, sender, receipent, type, description, method, currAmount, currBalance, date;
    private Double amount, balance;

    public Transaction() {
    }

    public Transaction(String transactionID, String sender, String receipent, String type, String method, String description, double amount, double balance, String date) {
        this.transactionID = transactionID;
        this.sender = sender;
        this.receipent = receipent;
        this.type = type;
        this.method = method;
        this.description = description;
        this.amount = amount;
        this.balance = balance;
        this.date = date;
    }

    public Transaction(String transactionID, String sender, String receipent, String type, String description, String method, String currAmount, String currBalance, String date) {
        this.transactionID = transactionID;
        this.sender = sender;
        this.receipent = receipent;
        this.type = type;
        this.description = description;
        this.method = method;
        this.currAmount = currAmount;
        this.currBalance = currBalance;
        this.date = date;
    }

    public Transaction(String sender, String receipent, String type, String description, String method, Double amount) {
        this.sender = sender;
        this.receipent = receipent;
        this.type = type;
        this.description = description;
        this.method = method;
        this.amount = amount;
    }
    
    
    
    
    
    public String getTransactionId() throws SQLException {
        
        String newId = "1"; // Default starting ID if no existing IDs are found
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

    
    public String recordTransaction(String currency) throws SQLException {
        double rbalance = 0.0;
        String transactionID = null;
        
        try (Connection con = DBConnection.openConn();
            Statement statement = con.createStatement()) {
            // Generate a transaction ID
            transactionID = getTransactionId();
            String method = "Transfer";
            
            // Fetch sender current balance from database
            String senderBalance = "SELECT " + currency + " FROM account WHERE AccountNum = '" + this.sender + "'";
            ResultSet SBalanceResult = statement.executeQuery(senderBalance);
            if (SBalanceResult.next()) {
                this.balance = SBalanceResult.getDouble(currency);
            }
            // Update balance
            this.balance -= this.amount;
            this.currBalance = String.valueOf(this.balance) + " " + currency.charAt(0);
            this.currAmount = String.valueOf(this.amount) + " " + currency.charAt(0);
            
            // Fetch receipent current balance from database
            String receipentBalance = "SELECT " + currency + " FROM account WHERE AccountNum = '" + this.receipent + "'";
            ResultSet RBalanceResult = statement.executeQuery(receipentBalance);
            if (RBalanceResult.next()) {
                rbalance = RBalanceResult.getInt(currency);
            }
            
            // Update balance
            rbalance += this.amount;

            
            // Insert transaction into database
            String sql = "INSERT INTO transaction (ID_Transaction, Sender, Receipent, Amount, balance, method, Type, Description) "
                    + "VALUES ('" + transactionID + "', '" + this.sender + "', '" + this.receipent + "', '" + this.currAmount + "', '" + this.currBalance + "', '" + method + "', '" + this.type + "', '" + this.description + "')";
            statement.executeUpdate(sql);
            
            // Update the balance in the account table
            String updateSenderBalanceQuery = "UPDATE account SET " + currency + " = " + this.balance + " WHERE AccountNum = '" + this.sender + "'";
            statement.executeUpdate(updateSenderBalanceQuery);
            
            String updateReceipentBalanceQuery = "UPDATE account SET " + currency + " = " + rbalance + " WHERE AccountNum = '" + this.receipent + "'";
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
    
    public double getAmount() {
        return amount;
    }

    public String getType() {
        return type;
    }

    public String getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public String getTransactionID() {
        return transactionID;
    }

    public String getSender() {
        return sender;
    }

    public String getReceipent() {
        return receipent;
    }

    public String getMethod() {
        return method;
    }

    public double getBalance() {
        return balance;
    }

    public String getCurrAmount() {
        return currAmount;
    }

    public String getCurrBalance() {
        return currBalance;
    }
    
    
    
    //modification
     // Method to record a transaction for currency exchange
    public String recordCurrencyExchange(String sender, String recipient, double amount, double convertedAmount, double processingFee, double senderBalance, double recipientBalance) throws SQLException {
        String transactionID = null;
        try (Connection connection = DBConnection.openConn();
            Statement statement = connection.createStatement();){
            // Generate a transaction ID
            transactionID = getTransactionId();

            // Get current date and time
            LocalDate currentDate = LocalDate.now();
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String formattedDate = currentDate.format(dateFormatter);

            LocalTime currentTime = LocalTime.now();
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
            String formattedTime = currentTime.format(timeFormatter);

            // Record the currency exchange transaction
            String sql = "INSERT INTO transaction (ID_Transaction, Sender, Receipent, Amount, balance, Type, Date, Description) "
            + "VALUES ('" + transactionID + "', '" + sender + "', '" + recipient + "', " + amount + ", " + senderBalance + ", 'Currency Exchange', '" + formattedDate + " " + formattedTime + "', 'Converted " + amount + " to " + convertedAmount + " with processing fee " + processingFee + "')";
            statement.executeUpdate(sql);
//
//            // Update sender's balance
//            String updateSenderBalanceQuery = "UPDATE account SET Balance = " + senderBalance + " WHERE AccountNum = '" + sender + "'";
//            statement.executeUpdate(updateSenderBalanceQuery);
//
//            // Update recipient's balance
//            String updateRecipientBalanceQuery = "UPDATE account SET Balance = " + recipientBalance + " WHERE AccountNum = '" + recipient + "'";
//            statement.executeUpdate(updateRecipientBalanceQuery);

            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transactionID;
    }
}
