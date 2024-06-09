package egringotts;
/**
 *
 * @author Asus
 */
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
    
public class PensivePast {
    private SilverSnitch cust;

    public PensivePast(SilverSnitch cust) {
        this.cust = cust;
    }
    
    public static ArrayList<Transaction> history(String accountNum){
        ArrayList<Transaction> trans = new ArrayList<>();
        String query = "SELECT * FROM transaction WHERE Sender = ? ORDER BY Date DESC";
        
        try (Connection connection = DBConnection.openConn();
            PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, accountNum);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                do {
                  // Process the result set and create Transaction objects
                  trans.add(new Transaction(
                      rs.getString("ID_Transaction"),
                      accountNum, // Assuming sender should be the provided accNum
                      rs.getString("Receipent"),
                      rs.getString("Type"),
                      rs.getString("Description"),
                      rs.getString("method"),
                      rs.getString("amount"),
                      rs.getString("balance"),
                      rs.getString("Date")
                  ));
                } while (rs.next());
              }
            //printTable(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return trans;
    }
    
    public static ArrayList<Transaction> historyExchange(String accountNum){
        ArrayList<Transaction> trans = new ArrayList<>();
        String query = "SELECT * FROM transaction WHERE Sender = ? AND Receipent = ?";
        
        try (Connection connection = DBConnection.openConn();
            PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, accountNum);
            ps.setString(2, accountNum);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                do {
                  // Process the result set and create Transaction objects
                  trans.add(new Transaction(
                      rs.getString("amount"),
                      accountNum, // Assuming sender should be the provided accNum
                      accountNum,
                      rs.getString("Type"),
                      rs.getString("Description"),
                      rs.getString("method"),
                      rs.getString("amount"),
                      rs.getString("balance"),
                      rs.getString("Date")
                  ));
                } while (rs.next());
              }
            //printTable(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return trans;
    }
    
    public static ArrayList<Transaction> filter(String accNum, int minAmount, int maxAmount, int filterDate, String filterDateBy, String filterType) {
        StringBuilder query = new StringBuilder();
        query.append("SELECT ID_Transaction, Receipent, Amount, balance, Type, method, Date, Description FROM transaction WHERE Sender = ? ");
        
        ArrayList<Transaction> filtered = new ArrayList<>();
        List<String> filterConditions = new ArrayList<>();

        // Add filter conditions based on provided parameters
        if (minAmount != 0 && maxAmount != 0) {
          filterConditions.add("Amount >= ? AND Amount <= ?");
        } else if (minAmount != 0) {
          filterConditions.add("Amount >= ?");
        } else if (maxAmount != 0) {
          filterConditions.add("Amount <= ?"); // Handle filtering by max amount only
        }

        if (filterDate != 0) {
          if (filterDateBy != null && filterDateBy.equals("month")) 
            filterConditions.add("MONTH(Date) = ?"); // Filter by month
        }

        if (filterType != null && !filterType.isEmpty()) {
          filterConditions.add("Type = ?");
        }

        // Append filter conditions with OR operators (if any)
        if (!filterConditions.isEmpty()) {
          query.append(" AND ( ");
          for (int i = 0; i < filterConditions.size(); i++) {
            query.append(filterConditions.get(i));
            if (i < filterConditions.size() - 1) {
              query.append(" AND ");
            }
          }
          query.append(")");
        }
        
        query.append("ORDER BY Date DESC");

        try (Connection connection = DBConnection.openConn();
             PreparedStatement ps = connection.prepareStatement(query.toString())) {
          int parameterIndex = 1;
          ps.setString(parameterIndex++, accNum); // Set sender filter

          // Set filter parameters based on added conditions
          for (String condition : filterConditions) {
            if (condition.contains("Amount")) {
              if (minAmount != 0 && maxAmount != 0) {
                ps.setInt(parameterIndex++, minAmount);
                ps.setInt(parameterIndex++, maxAmount);
              } else if(minAmount != 0 ){
                ps.setInt(parameterIndex++, minAmount); // Or maxAmount if applicable
              } else if(maxAmount != 0){
                  ps.setInt(parameterIndex++, maxAmount);
              }
            } else if (condition.contains("Date")) {
                ps.setInt(parameterIndex++, filterDate); // Modify based on your date format
            } else if (condition.contains("Type")) {
                ps.setString(parameterIndex++, filterType);
            }
          }
          ResultSet rs = ps.executeQuery();
          if (rs.next()) {
                do {
                  // Process the result set and create Transaction objects
                  filtered.add(new Transaction(
                      rs.getString("amount"),
                      accNum, // Assuming sender should be the provided accNum
                      rs.getString("Receipent"),
                      rs.getString("Type"),
                      rs.getString("Description"),
                      rs.getString("method"),
                      rs.getString("amount"),
                      rs.getString("balance"),
                      rs.getString("Date")
                  ));
                } while (rs.next());
              }
        } catch (SQLException e) {
          e.printStackTrace();
        }
        return filtered;
    }
}