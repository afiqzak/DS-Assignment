package egringotts;

/**
 *
 * @author wenhu
 */
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class CustomerDatabase {

    public static Customer getCustomerByAccountNumber(String accountNumber) {
        DBConnection dbConnection = new DBConnection();
        Connection conn = dbConnection.openConn();
        Customer customer = null;

        try {
            String query = "SELECT * FROM account WHERE AccountNum = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, accountNumber);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String username = resultSet.getString("username");
                String name = resultSet.getString("Name_Customer");
                String phoneNum = resultSet.getString("PhoneNum_Customer");
                String email = resultSet.getString("Email_Customer");
                String password = resultSet.getString("Password_Customer");
                String DOB = resultSet.getString("DOB");
                String address = resultSet.getString("Address");
                Map<String, java.lang.Double> balances = new HashMap<>();
                balances.put("Knut", resultSet.getDouble("KnutBalance"));
                balances.put("Sickle", resultSet.getDouble("SickleBalance"));
                balances.put("Galleon", resultSet.getDouble("GalleonBalance"));

                customer = new Customer(username, name, phoneNum, email, password, DOB, address, balances);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbConnection.closeConn();
        }

        return customer;
    }
}
