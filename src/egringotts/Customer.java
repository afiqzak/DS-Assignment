/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package egringotts;

import java.util.Random;

/**
 *
 * @author USER
 */
public class Customer implements User{
    
    private int userId;
    private String name;
    private String username;
    private String phoneNum;
    private String email;
    private String password;
    private String DOB;
    private String address;
    private int balance;
    private String tier;

    public Customer(String username, String password) {
        this.username = username;
        this.password = password;
    }
    
    

    // Constructor
    public Customer(String username, String name, String phoneNum, String email, String password, String DOB, String address, int balance) {
        this.username = username;
        this.name = name;
        this.phoneNum = phoneNum;
        this.email = email;
        this.password = password;
        this.DOB = DOB;
        this.address = address;
        this.balance = balance;
    }

    // Implement User interface methods
    @Override
    public int getUserId() {
        return userId;
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

    public int getBalance() {
        return balance;
    }
    
    public String getTier() {
        if (getBalance() < 10000) {
            return "Silver Snitch";
        }
        else if (getBalance() >= 10000 && getBalance() < 50000) {
            return "Golden Galleon";
        }
        else if (getBalance() >= 50000) {
            return "Platinum Patronus";
        }
        return null;
    }
    
    public String getAccountNum() {
        Random r = new Random();
        int num1 = r.nextInt(10000);
        int num2 = r.nextInt(10000);
        int num3 = r.nextInt(10000);
        String accountnum= String.format("%04d", num1) + " " + String.format("%04d", num2) + " " + String.format("%04d", num3);
        return accountnum;
    }
    

    @Override
    public void setName(String name) {
        this.name = name;
    }

    
    
}

