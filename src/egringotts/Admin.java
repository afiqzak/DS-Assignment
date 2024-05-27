package egringotts;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author USER
 */
public class Admin implements User {

    private int userId;
    private String name;
    private String username;
    private String password;
    private String phoneNum;
    private String email;
    private String DOB;
    private String address;

    // Constructor
    public Admin(String username, String name, String phoneNum, String email, String password, String DOB, String address) {
        this.username = username;
        this.name = name;
        this.phoneNum = phoneNum;
        this.email = email;
        this.password = password;
        this.DOB = DOB;
        this.address = address;
    }

    public Admin(String username, String password) {
        this.username = username;
        this.password = password;
    }
    
    

    // Implement User interface methods
    
    @Override
    public int getUserId() throws SQLException{
        int newId = 1;
        try (Connection con = DBConnection.openConn();
            Statement statement = con.createStatement()){
            String SQL_Command = "SELECT MAX(ID_Admin) FROM admin";
            ResultSet Rslt = statement.executeQuery(SQL_Command);

            if (Rslt.next()) {
                // Retrieve the maximum ID from the result set
                int maxId = Rslt.getInt(1);
                // If maxId is 0, it means there are no existing admin records, so set newId to 1

                // Increment the maximum ID by 1 to generate a new unique ID
                newId = maxId + 1;

            }
        }catch (SQLException e ){
            e.printStackTrace();
        }
        return newId;

    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getPhoneNum() {
        return phoneNum;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public String getDOB() {
        return DOB;
    }

    @Override
    public String getAddress() {
        return address;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    

}
