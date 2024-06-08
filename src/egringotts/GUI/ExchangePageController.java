package egringotts.GUI;

import egringotts.*;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Asus
 */
public class ExchangePageController implements Initializable {

    @FXML
    private Stage stage;
    
    @FXML
    private Scene scene;
    
    @FXML
    private Parent root;
    
    @FXML
    private TableView<Transaction> history;
    
    @FXML
    private TableColumn<Transaction, String> amountColumn, receivedColumn, descColumn, dateColumn;
    
    @FXML
    private Label balanceField,convertedField;
    
    @FXML
    private TextField amountField;
    
    @FXML
    private ChoiceBox<String> currencyChoice,currencyChoice1, currencyChoice2;
    
    @FXML
    private Button analyticsButton;
    
    @FXML
    private VBox menu;
    
    private SilverSnitch cust;
    private PensivePast pensive;
    private CurrencyExchange exchange;
    private Transaction trans;
    
    ObservableList<Transaction> list;
    
    public void setCustomer(SilverSnitch cust){
        this.cust = cust;
        
        list = FXCollections.observableArrayList(pensive.historyExchange(cust.getKey()));
        if(cust.getTier().equals("Silver Snitch"))
            menu.getChildren().remove(analyticsButton);
    }
    
    public void historyTable(){
        descColumn.setCellValueFactory(new PropertyValueFactory<Transaction,String>("description"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<Transaction,String>("currAmount"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<Transaction,String>("date"));
        
        history.setItems(list);
    }
    
    public void displayBalance(){
        balanceField.setText(String.valueOf(cust.getBalance(currencyChoice.getValue()))+ currencyChoice.getValue().charAt(0));
    }
    
    public void getBalance(ActionEvent e){
        String currency = currencyChoice.getValue();
        balanceField.setText(String.valueOf(cust.getBalance(currency)) + currency.charAt(0));
    }
    
    public void getConverted(ActionEvent e){
        double amount = Double.valueOf(amountField.getText());
        String from = currencyChoice1.getValue();
        String to = currencyChoice2.getValue();
        
        convertedField.setText(Double.toString(exchange.exchange(String.valueOf(from.charAt(0)), String.valueOf(to.charAt(0)), amount)));
    }
    
    public void display(SilverSnitch cust){
        setCustomer(cust);
        historyTable();
        displayBalance();
    }
    
    @FXML
    private void exchangeButton(ActionEvent event) throws IOException, SQLException{
        double amount = Double.parseDouble(amountField.getText());
        String from = currencyChoice1.getValue();
        String to = currencyChoice2.getValue();
        double converted = Double.parseDouble(convertedField.getText());
        double fee = exchange.totalFee(from, to, amount);
        double total = exchange.totalFee(from, to, amount);
        String desc  = "Convert " + amount + " " + from + " to " + converted + " " + to;
        
        trans = new Transaction(cust.getKey(), cust.getKey(), "Exchange", desc, "Transfer", fee);
        trans.performCurrencyExchange(cust, from, to, total, converted);
        
        
        displayBalance();
        historyTable();
    }
    
    @FXML
    private void dashboardMenu(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("MainDashboard.fxml"));
        root = loader.load();
        MainDashboardController main = loader.getController();
        main.setCustomer(cust);
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
        currencyChoice.getSelectionModel().selectFirst();
        currencyChoice.setOnAction(this::getBalance);
        currencyChoice1.getItems().addAll(Account.getCurrency());
        currencyChoice2.getItems().addAll(Account.getCurrency());
        currencyChoice2.setOnAction(this::getConverted);
        this.exchange = new CurrencyExchange();
    } 
    
}
