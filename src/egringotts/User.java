/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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