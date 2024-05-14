package marauderMap;
//receipt is printed after every transactions
import java.awt.print.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Font;
import java.awt.RenderingHints;
import static java.awt.print.Printable.NO_SUCH_PAGE;
import static java.awt.print.Printable.PAGE_EXISTS;
import java.util.Random;

public class receipt implements Printable {
    private String transactionId;
    private String accountNum;
    private double amount;
    private String type;
    private String description;
    private Date transactionDate;

    public receipt(String transactionId, String accountNum, double amount, String type, String description, Date transactionDate) {
        this.transactionId = transactionId;
        this.accountNum = accountNum;
        this.amount = amount;
        this.type = type;
        this.description = description;
        this.transactionDate = transactionDate;
    }

    @Override
    public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) {
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
        String[] receipt = {
                "Transaction ID: " + transactionId,
                "Date: " + dateFormat.format(transactionDate),
                "Account Number: " + accountNum,
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

    //generate random transaction id with 3 alphabets and 6 digits
    public static String generateTransactionId() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < 3; i++) {
            char c = (char) (random.nextInt(26) + 'A');
            sb.append(c);
        }

        for (int i = 0; i < 6; i++) {
            int digit = random.nextInt(10);
            sb.append(digit);
        }

        return sb.toString();
    }

    //check if the account number exists before the transaction proceed
    public static boolean accExist(String accountNum) {
        String query = "SELECT COUNT(*) FROM account WHERE AccountNum = ?";
        try (Connection connection = ConnectionManager.getConnection();
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

    //try to add transactions and print receipt
    public static void addTransAndPrint(String accountNum, double amount, String type, String description) {
        if (!accExist(accountNum)) {
            System.out.println("Account number " + accountNum + " does not exist. Cannot add transaction.");
            return;
        }

        String insertSQL = "INSERT INTO transaction (ID_Transaction, AccountNum, Amount, Type, Description, Date) VALUES (?, ?, ?, ?, ?, ?)";
        Date transactionDate = new Date();
        String transactionId = generateTransactionId();

        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement ps = connection.prepareStatement(insertSQL)) {

            ps.setString(1, transactionId);
            ps.setString(2, accountNum);
            ps.setDouble(3, amount);
            ps.setString(4, type);
            ps.setString(5, description);
            ps.setTimestamp(6, new java.sql.Timestamp(transactionDate.getTime()));

            int affectedRows = ps.executeUpdate();

            if (affectedRows > 0) {
                receipt receipt = new receipt(transactionId, accountNum, amount, type, description, transactionDate);
                receipt.printReceipt();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //only testing
    public static void main(String[] args) {
        addTransAndPrint("1234 1233 1234", 188.88, "food", "nice food");
    }
}
