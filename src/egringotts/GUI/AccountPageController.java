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
    private ChoiceBox<String> currencyChoice, currencyChoice1, currencyChoice2, typeChoice;

    @FXML
    private Button findButton, transferButton;

    @FXML
    private TextField findField, transferField, receipentField, amountField, descField;
    
    @FXML
    private VBox vbox;
    
    private String[] Type = {"Food","Bill","Grocery","Entertainment","Others"};
    
    private SilverSnitch cust;
    private Pane lastClickedPane = null;
    
    public void setCustomer(SilverSnitch cust){
        this.cust = cust;
    }
    
    public void displayBalance(){
        balanceField.setText(String.valueOf(cust.getBalance(currencyChoice.getValue()))+ currencyChoice.getValue().charAt(0));
    }
    
    public void getBalance(ActionEvent e){
        String currency = currencyChoice.getValue();
        balanceField.setText(String.valueOf(cust.getBalance(currency)) + currency.charAt(0));
    }
    
    public void display(SilverSnitch cust){
        setCustomer(cust);
        displayBalance();
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
    private void payButton(ActionEvent event) throws SQLException{
        String receipent = receipentField.getText();
        double amount = Double.parseDouble(amountField.getText());
        String currency = currencyChoice2.getValue();
        String type = typeChoice.getValue();
        String desc = descField.getText();
        
        Transaction trans = new Transaction(cust.getKey(), receipent, type, desc, "Transfer", amount);
        
        trans.recordTransaction(currency);
        displayBalance();
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

        Label usernameLabel = new Label(username);
        usernameLabel.setId("username");
        usernameLabel.setLayoutX(14.0);
        usernameLabel.setLayoutY(14.0);

        Label phoneLabel = new Label(phoneNumber);
        phoneLabel.setLayoutX(14.0);
        phoneLabel.setLayoutY(33.0);
        
        pane.setStyle("-fx-background-color: transparent; -fx-border-color: black; -fx-border-width: 1;");
        
        pane.setOnMouseEntered(e -> {
            if (lastClickedPane != pane) {
                pane.setStyle("-fx-background-color: lightgray;");
            }
        });
        pane.setOnMouseExited(e -> {
            if (lastClickedPane != pane) {
                pane.setStyle("-fx-background-color: transparent; -fx-border-color: black; -fx-border-width: 1;");
            }
        });
        pane.setOnMouseClicked(e -> {
            if (lastClickedPane != null) {
                lastClickedPane.setStyle("-fx-background-color: transparent; -fx-border-color: black; -fx-border-width: 1;");
            }
            pane.setStyle("-fx-background-color: coral;");
            lastClickedPane = pane;
        });
        pane.getChildren().addAll(usernameLabel, phoneLabel);
        return pane;
    }
    
    @FXML
    void transferButton(ActionEvent event) throws SQLException {
        if(lastClickedPane == null){
            System.out.println("Please pick a friend");
            return;
        }
        Label username = (Label) lastClickedPane.lookup("#username");

        if (username != null) {
          double amount = Double.valueOf(transferField.getText());
          String currency = currencyChoice1.getValue();
          SilverSnitch receipent = Account.getCustomerByUsername(username.getText());
          
          Transaction trans = new Transaction(cust.getKey(), receipent.getKey(), "Other", "Instant transfer to " + receipent.getName(), "Transfer", amount);
          
          trans.recordTransaction(currency);
          displayBalance();
        }
    }
    
    @FXML
    private void dashboardMenu(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("MainDashboard.fxml"));
        root = loader.load();
        MainDashboardController main = loader.getController();
        main.display(cust);
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
    private void cardsMenu(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("CardsPage.fxml"));
        root = loader.load();
        CardsPageController cards = loader.getController();
        cards.display(cust);
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
        currencyChoice.getItems().addAll(Account.getCurrency());
        currencyChoice1.getItems().addAll(Account.getCurrency());
        currencyChoice2.getItems().addAll(Account.getCurrency());
        typeChoice.getItems().addAll(Type);
        currencyChoice.getSelectionModel().selectFirst();
        currencyChoice.setOnAction(this::getBalance);
    } 
    
}