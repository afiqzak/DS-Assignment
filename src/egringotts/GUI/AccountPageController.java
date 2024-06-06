package egringotts.GUI;

import egringotts.*;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Border;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Asus
 */
public class AccountPageController implements Initializable {

    @FXML
    private Stage stage;
    
    @FXML
    private Scene scene;
    
    @FXML
    private Parent root;
    
    @FXML
    private Label balanceField;

    @FXML
    private ChoiceBox<String> currencyChoice, currencyChoice1;

    @FXML
    private Button findButton, transferButton;

    @FXML
    private TextField findField, transferField;
    
    @FXML
    private VBox vbox;
    
    private Customer cust;
    private Admin admin;
    
    public void setCustomer(Customer cust){
        this.cust = cust;
    }
    
    public void setAdmin(Admin admin) {
        this.admin = admin;
    }
    
    public void displayBalance(){
        balanceField.setText(String.valueOf(cust.getBalance(currencyChoice.getValue()))+ currencyChoice.getValue().charAt(0));
    }
    
    public void getBalance(ActionEvent e){
        String currency = currencyChoice.getValue();
        balanceField.setText(String.valueOf(cust.getBalance(currency)) + currency.charAt(0));
    }
    
    @FXML
    private void findFriend(ActionEvent event) {
        vbox.getChildren().clear();
        try (Connection con = DBConnection.openConn();
               PreparedStatement stmt = con.prepareStatement("SELECT username, PhoneNum_Customer FROM account WHERE PhoneNum_Customer LIKE ? OR username LIKE ?;")) {

            stmt.setString(1, findField.getText()+"%"); // Set parameter with prepared statement
            stmt.setString(2, findField.getText()+"%");
            ResultSet rs = stmt.executeQuery();

            while(rs.next()){
                Pane accountPane = createAccountPane(rs.getString("username"), rs.getString("PhoneNum_Customer"));
                vbox.getChildren().add(accountPane);
            }
          } catch (SQLException e) {
            e.printStackTrace();
          }
    }
    
    @FXML
    private Pane createAccountPane(String username, String phoneNumber) {
        Pane pane = new Pane();
        pane.setPrefHeight(74.0);
        pane.setPrefWidth(188.0);
        pane.setMaxHeight(74.0);
        pane.setMaxWidth(188.0);
        pane.setMinHeight(74.0);
        pane.setMinWidth(188.0);
        
        pane.setStyle("-fx-border-color: black; -fx-border-width: 1;");

        Label usernameLabel = new Label(username);
        usernameLabel.setLayoutX(14.0);
        usernameLabel.setLayoutY(14.0);

        Label phoneLabel = new Label(phoneNumber);
        phoneLabel.setLayoutX(14.0);
        phoneLabel.setLayoutY(33.0);

        pane.getChildren().addAll(usernameLabel, phoneLabel);
        return pane;
    }
    
    @FXML
    private void dashboardMenu(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("MainDashboard.fxml"));
        root = loader.load();
        MainDashboardController main = loader.getController();
        main.setCustomer(cust);
        main.display();
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
        trans.setCustomer(cust);
        trans.historyTable();
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);

        stage.setScene(scene);
        stage.show();
    }
    
    @FXML
    private void cardsMenu(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("CardsPage.fxml"));
        root = loader.load();
        CardsPageController cards = loader.getController();
        cards.displayCard(cust.getAccountNum());
        cards.setCustomer(cust);
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
        exchange.setCustomer(cust);
        exchange.historyTable();
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
        analytics.setCustomer(cust);
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
        if(admin != null) setting.setAdmin(admin);
        setting.checkAdmin();
        setting.setProfile();
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);

        stage.setScene(scene);
        stage.show();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        currencyChoice.getItems().addAll(Account.getCurrency());
        currencyChoice.getSelectionModel().selectFirst();
        currencyChoice.setOnAction(this::getBalance);
    } 
    
}