package egringotts;
/**
 *
 * @author Asus
 */
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
    
public class PensivePast {
    private final Connection connection;

    public PensivePast(Connection connection) {
        this.connection = connection;
    }
    
    public void printTable(ResultSet rs) throws SQLException{
        String format = "|%-15s |%-16s |%-10s |%-15s |%-13s |%-31s |%n";
        System.out.format("+----------------+-----------------+-----------+----------------+--------------+--------------------------------+%n");
        System.out.format("| Transaction ID | Account ID      | Amount    | Type           | Date         | Description                    |%n");
        System.out.format("+----------------+-----------------+-----------+----------------+--------------+--------------------------------+%n");

        while (rs.next()){
            int TransactionID = rs.getInt("ID_Transaction");
            String Receipent = rs.getString("Receipent");
            double Amount = rs.getDouble("Amount");
            String Type = rs.getString("Type");
            String Date = rs.getDate("Date").toString();
            String Desc = rs.getString("Description");
            
            System.out.format(format, TransactionID, Receipent, Amount, Type, Date, Desc);
        }
    }
    
    public void history(String Searchkey){
        String query = "SELECT ID_Transaction,Receipent,Amount,Type,Date,Description FROM transaction WHERE Sender = ? ORDER BY Date DESC";
        
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, Searchkey);
            ResultSet rs = ps.executeQuery();
            printTable(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void filter(String accNum, int minAmount, int maxAmount, String filterDate, String filterDateBy, String filterType) {
        StringBuilder query = new StringBuilder();
        query.append("SELECT ID_Transaction, Receipent, Amount, Type, Date, Description FROM transaction WHERE Sender = ? RDER BY Date DESC");

        List<String> filterConditions = new ArrayList<>();

        // Add filter conditions based on provided parameters
        if (minAmount != 0 && maxAmount != 0) {
          filterConditions.add("Amount >= ? AND Amount <= ?");
        } else if (minAmount != 0) {
          filterConditions.add("Amount >= ?");
        } else if (maxAmount != 0) {
          filterConditions.add("Amount <= ?"); // Handle filtering by max amount only
        }

        if (filterDate != null && !filterDate.isEmpty()) {
          if (filterDateBy != null && filterDateBy.equals("month")) {
            filterConditions.add("MONTH(Date) = ?"); // Filter by month
          } else if (filterDateBy != null && filterDateBy.equals("year")) {
            filterConditions.add("YEAR(Date) = ?"); // Filter by year
          } else {
            filterConditions.add("Date = ?"); // Default to exact date filtering
          }
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
              query.append(" OR ");
            }
          }
          query.append(")");
        }

        try (PreparedStatement ps = connection.prepareStatement(query.toString())) {
          int parameterIndex = 1;
          ps.setString(parameterIndex++, accNum); // Set sender filter

          // Set filter parameters based on added conditions
          for (String condition : filterConditions) {
            if (condition.contains("Amount")) {
              if (minAmount != 0 && maxAmount != 0) {
                ps.setInt(parameterIndex++, minAmount);
                ps.setInt(parameterIndex++, maxAmount);
              } else {
                ps.setInt(parameterIndex++, minAmount); // Or maxAmount if applicable
              }
            } else if (condition.contains("Date")) {
              ps.setString(parameterIndex++, filterDate); // Modify based on your date format
            } else if (condition.contains("Type")) {
              ps.setString(parameterIndex++, filterType);
            }
          }

          ResultSet rs = ps.executeQuery();
          printTable(rs);
        } catch (SQLException e) {
          e.printStackTrace();
        }
    }


}