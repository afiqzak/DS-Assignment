package egringotts;

import java.sql.SQLException;

/**
 *
 * @author USER
 */
public interface User {
    int getUserId() throws SQLException;
    String getName();
    String getUsername();
    void setName(String name);
    String getPassword();
    String getPhoneNum();
    String getEmail();
    String getDOB();
    String getAddress();
}