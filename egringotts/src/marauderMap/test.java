package marauderMap;
//d. Marauder's Map
//only testing
import java.sql.Connection;
import java.sql.SQLException;
public class test {
    public static void main(String[] args) {
        try (Connection connection = ConnectionManager.getConnection()) {
            search searchObj = new search(connection);
            searchObj.searchFriends("ali"); //can search by id, name, contact number
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}