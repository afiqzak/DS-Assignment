package egringotts;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import static java.awt.print.Printable.NO_SUCH_PAGE;
import static java.awt.print.Printable.PAGE_EXISTS;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Random;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author USER
 */
public class Transaction implements Printable{

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
    
    
    
    
    
    public String generateTransactionId() throws SQLException {
        
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
            transactionID = generateTransactionId();
            String mathod = "Transfer";
            
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
            transactionID = generateTransactionId();

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
    
    @Override
    public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) {
        try {
            if (pageIndex > 0) {
                return NO_SUCH_PAGE;
            }
            
            Graphics2D g2d = (Graphics2D) graphics;
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            
            g2d.translate(pageFormat.getImageableX() + 50, pageFormat.getImageableY() + 50);
            
            Font titleFont = new Font("Serif", Font.BOLD, 18);
            Font bodyFont = new Font("Monospaced", Font.PLAIN, 12);
            
            int y = 0;
            int titleLineHeight = 20;
            int bodyLineHeight = 15;
            
            g2d.setFont(titleFont);
            g2d.drawString("E-GRINGOTTS RECEIPT", 0, y);
            y += titleLineHeight;
            
            g2d.setFont(bodyFont);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date();
            date.getTime();
            String[] receipt = {
                "Transaction ID: " + generateTransactionId(),
                "Date: " + dateFormat.format(date),
                "Account Number: " + this.receipent,
                "Amount: " + String.format("%.2f", amount),
                "Type: " + type,
                "Description: " + description,
                "Thank you for using E-Gringotts! Your magical transfer has been successfully",
                "completed.",
                "For any inquiries or further assistance, owl us at support@egringotts.com",
                "May your galleons multiply like Fizzing Whizbees!"
            };
            
            for (String line : receipt) {
                g2d.drawString(line, 0, y);
                y += bodyLineHeight;
            }
            
            return PAGE_EXISTS;
        } catch (SQLException ex) {
            Logger.getLogger(Transaction.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    public void printReceipt() {
        PrinterJob job = PrinterJob.getPrinterJob();
        job.setPrintable(this);
        boolean doPrint = job.printDialog();
        if (doPrint) {
            try {
                job.print();
            } catch (PrinterException e) {
                e.printStackTrace();
            }
        }
    }

    //check if the account number exists before the transaction proceed
    public static boolean accExist(String accountNum) {
        String query = "SELECT COUNT(*) FROM account WHERE AccountNum = ?";
        try (Connection connection = DBConnection.openConn();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, accountNum);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
