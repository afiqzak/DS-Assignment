package egringotts;
import java.sql.*;
//search function: user able to search their frens by name, id, email, contact num
public class search {

    public search() {}

    public void searchFriends(String searchKey) {
        String query = "SELECT * FROM customer WHERE Name_Customer LIKE ? OR Email_Customer LIKE ? OR PhoneNum_Customer LIKE ? OR ID_Customer = ?";

        try (Connection con = DBConnection.openConn();
            PreparedStatement ps = con.prepareStatement(query)) {
            String likeSearch = "%" + searchKey + "%";

            //parameters for LIKE searches
            ps.setString(1, likeSearch);
            ps.setString(2, likeSearch);
            ps.setString(3, likeSearch);

            //parameter for ID_Customer
            try {
                int customerId = Integer.parseInt(searchKey);
                ps.setInt(4, customerId);
            } catch (NumberFormatException e) {
                ps.setInt(4, -1); //all IDs are positive numbers
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    // Retrieve data from the database
                    int id = rs.getInt("ID_Customer");
                    String name = rs.getString("Name_Customer");
                    String phone = rs.getString("PhoneNum_Customer");
                    String email = rs.getString("Email_Customer");
                    String password = rs.getString("Password_Customer");
                    Date dob = rs.getDate("DOB");
                    String address = rs.getString("Address");

                    // Print all the info of the user
                    System.out.println("ID: " + id);
                    System.out.println("Name: " + name);
                    System.out.println("Phone: " + phone);
                    System.out.println("Email: " + email);
                    System.out.println("Password: " + password);
                    System.out.println("DOB: " + dob);
                    System.out.println("Address: " + address);
                }
            }
        } catch (SQLException e) {
        e.printStackTrace();
    }
}
}
