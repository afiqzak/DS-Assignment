package egringotts.GUI;

import egringotts.*;
import egringotts.DBConnection;
import static egringotts.GUI.MainDashboardController.masking;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Asus
 */
public class CardsPageController implements Initializable {

    @FXML
    private Stage stage;
    
    @FXML
    private Scene scene;
    
    @FXML
    private Parent root;
    
    @FXML
    private Label expField, expField2, expField3, nameField, nameField2, nameField3, numField, numField2, numField3, successLabel;
    
    @FXML
    private TextField cardNumField,expDateField,ccvField,creditLimitField;
    
    @FXML
    private ChoiceBox<String> typeField;
    
    @FXML
    private BarChart<String, Double> barchartCard;
    
    @FXML
    private Pane card1, card2, card3;
    private SilverSnitch cust;  
    
    private String[] type = {"Credit Card","Debit Card"};
    
    public int card;
    
    public void setCustomer(egringotts.SilverSnitch cust){
        this.cust = cust;
    }
    
    public void displayCard(){
        String query = "SELECT card.AccountNum, card.Card_Number,card.Expiration_Date, account.Name_Customer FROM card INNER JOIN account ON card.AccountNum=account.AccountNum WHERE card.AccountNum = ?;";
        this.card = 1;
        
        try (Connection con = DBConnection.openConn();
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, cust.getKey());
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                if(card == 1){
                    expField.setText(rs.getString("Expiration_Date"));
                    nameField.setText(rs.getString("Name_Customer"));
                    numField.setText(masking(rs.getString("Card_Number")));
                    card1.setVisible(true);
                    card2.setVisible(false);
                    card3.setVisible(false);
                }else if(card == 2){
                    expField2.setText(rs.getString("Expiration_Date"));
                    nameField2.setText(rs.getString("Name_Customer"));
                    numField2.setText(masking(rs.getString("Card_Number")));
                    card1.setVisible(true);
                    card2.setVisible(true);
                    card3.setVisible(false);
                }else if(card == 3){
                    expField3.setText(rs.getString("Expiration_Date"));
                    nameField3.setText(rs.getString("Name_Customer"));
                    numField3.setText(masking(rs.getString("Card_Number")));
                    card1.setVisible(true);
                    card2.setVisible(true);
                    card3.setVisible(true);
                }else if(card==0){
                    card1.setVisible(false);
                    card2.setVisible(false);
                    card3.setVisible(false);
                }
                this.card++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void displayBarchart(){
        XYChart.Series credit = new XYChart.Series();
        credit.setName("Credit Card");
        credit.getData().add(new XYChart.Data("Sun", cust.getSumCard(credit.getName(), "Sunday")));
        credit.getData().add(new XYChart.Data("Mon", cust.getSumCard(credit.getName(), "Monday")));
        credit.getData().add(new XYChart.Data("Teu", cust.getSumCard(credit.getName(), "Teusday")));
        credit.getData().add(new XYChart.Data("Wed", cust.getSumCard(credit.getName(), "Wednesday")));
        credit.getData().add(new XYChart.Data("Thu", cust.getSumCard(credit.getName(), "Thursday")));
        credit.getData().add(new XYChart.Data("Fri", cust.getSumCard(credit.getName(), "Friday")));
        credit.getData().add(new XYChart.Data("Sat", cust.getSumCard(credit.getName(), "Saturday")));
        
        XYChart.Series debit = new XYChart.Series();
        debit.setName("Debit Card");
        debit.getData().add(new XYChart.Data("Sun", cust.getSumCard(debit.getName(), "Sunday")));
        debit.getData().add(new XYChart.Data("Mon", cust.getSumCard(debit.getName(), "Monday")));
        debit.getData().add(new XYChart.Data("Tue", cust.getSumCard(debit.getName(), "Teusday")));
        debit.getData().add(new XYChart.Data("Wed", cust.getSumCard(debit.getName(), "Wednesday")));
        debit.getData().add(new XYChart.Data("Thu", cust.getSumCard(debit.getName(), "Thursday")));
        debit.getData().add(new XYChart.Data("Fri", cust.getSumCard(debit.getName(), "Friday")));
        debit.getData().add(new XYChart.Data("Sat", cust.getSumCard(debit.getName(), "Saturday")));
        
        
        barchartCard.getData().addAll(credit, debit);
    }
    
    public void display(SilverSnitch cust){
        setCustomer(cust);
        displayBarchart();
        displayCard();
    }
    
    @FXML
    private void addCardButton(ActionEvent event) throws IOException, SQLException {
        try (Connection con = DBConnection.openConn();
                Statement statement = con.createStatement()){
        String SQL_Command = "INSERT INTO card (cvv, AccountNum, Card_Number, Expiration_Date, Credit_limit, type) " +
                                    "VALUES ('" + ccvField.getText() + "','" + cust.getKey() + "','" + cardNumField.getText() + "','" + expDateField.getText() + "','" +
                                    creditLimitField.getText() + "','" + typeField.getValue() + "')";
        statement.executeUpdate(SQL_Command);
        successLabel.setVisible(true);
        }
    }
    
    @FXML
    private void dashboardMenu(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("MainDashboard.fxml"));
        root = loader.load();
        MainDashboardController main = loader.getController();
        main.setCustomer(cust);
        main.display(cust);
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);

        stage.setScene(scene);
        stage.show();
    }
    @FXML
    private void accountsMenu(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("AccountPage.fxml"));
        root = loader.load();
        AccountPageController acc = loader.getController();
        acc.display(cust);
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);

        stage.setScene(scene);
        stage.show();
    }
    @FXML
    private void transactionMenu(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("TransactionPage.fxml"));
        root = loader.load();
        TransactionPageController trans = loader.getController();
        trans.display(cust);
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);

        stage.setScene(scene);
        stage.show();
    }
    @FXML
    private void exchangeMenu(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ExchangePage.fxml"));
        root = loader.load();
        ExchangePageController exchange = loader.getController();
        exchange.display(cust);
        exchange.displayBalance();
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);

        stage.setScene(scene);
        stage.show();
    }
    @FXML
    private void analyticsMenu(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("AnalyticsPage.fxml"));
        root = loader.load();
        AnalyticsPageController analytics = loader.getController();
        PlatinumPatronus plat = new PlatinumPatronus(cust.getKey(), cust.getBalances(), cust.getUsername(), cust.getName(), cust.getPassword(), cust.getPhoneNum(), cust.getEmail(), cust.getDob(), cust.getAddress());
        analytics.setUser(plat);
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);

        stage.setScene(scene);
        stage.show();
    }
    @FXML
    private void settingsMenu(ActionEvent event) throws IOException, SQLException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("SettingsPage.fxml"));
        root = loader.load();
        SettingsPageController setting = loader.getController();
        setting.setCustomer(cust);
        setting.checkAdmin();
        setting.setProfile();
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);

        stage.setScene(scene);
        stage.show();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        typeField.getItems().addAll(type);
        successLabel.setVisible(false);
    } 
    
}
