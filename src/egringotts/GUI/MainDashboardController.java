package egringotts.GUI;

import java.sql.Connection;
import egringotts.*;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 *
 * @author Asus
 */
public class MainDashboardController implements Initializable{
    @FXML
    private Label amountField1, amountField2, amountField3, dateField1, dateField2, dateField3, descField1, descField2, descField3, expField, expField2, nameField, nameField2, numField, numField2, balanceField;

    @FXML
    private Pane card1, card2;
    
    @FXML
    private Stage stage;
    
    @FXML
    private Scene scene;
    
    @FXML
    private Parent root;
    
    @FXML
    private ChoiceBox<String> currencyChoice;

    @FXML
    private PieChart piechartType;
    
    @FXML
    private BarChart<String, Double> barchartSpending;

    private Admin admin;
    
    private Customer cust;
    public int card;
    
    public void setCustomer(Customer cust){
        this.cust = cust;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
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
                }else if(card == 2){
                    expField2.setText(rs.getString("Expiration_Date"));
                    nameField2.setText(rs.getString("Name_Customer"));
                    numField2.setText(masking(rs.getString("Card_Number")));
                    card1.setVisible(true);
                    card2.setVisible(true);
                }else if(card==0){
                    card1.setVisible(false);
                    card2.setVisible(false);
                }
                this.card++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void displayRecentTrans(){
        Label[] desc = {descField1, descField2, descField3};
        Label[] date = {dateField1, dateField2, dateField3};
        Label[] amount = {amountField1, amountField2, amountField3};
        ArrayList<Transaction> trans = PensivePast.history(cust.getKey()); 
        
        for(int i = 0; i < 3 && trans.size() > i; i++){
            desc[i].setText(trans.get(i).getDescription());
            date[i].setText(trans.get(i).getDate());
            amount[i].setText(trans.get(i).getCurrAmount());
        }
      }
    
    public static String masking(String cardNum){
        String newCardNum = "";
        String[] num = cardNum.split(" ");
        for(int i = 0; i < num.length; i++){
            if(i == 2){
                newCardNum += num[i]; 
            }else{
                newCardNum += "**** ";
            }
        }
        return newCardNum;
    }
    
    public void displayBalance(){
        balanceField.setText(String.valueOf(cust.getBalance(currencyChoice.getValue()))+ currencyChoice.getValue().charAt(0));
    }
    
    public void getBalance(ActionEvent e){
        String currency = currencyChoice.getValue();
        balanceField.setText(String.valueOf(cust.getBalance(currency)) + currency.charAt(0));
    }
    
    public void displayPieChart(){
        ObservableList<PieChart.Data> pieData = 
                FXCollections.observableArrayList(
                        new PieChart.Data("ent", cust.getPercentageType("entertainment")),
                        new PieChart.Data("bill", cust.getPercentageType("bill")),
                        new PieChart.Data("grocery", cust.getPercentageType("grocery")),
                        new PieChart.Data("food", cust.getPercentageType("food")),
                        new PieChart.Data("other", cust.getPercentageType("other"))
                );
        
        pieData.forEach(data -> 
                data.nameProperty().bind(
                        Bindings.concat(
                                data.getName(), ": ", data.pieValueProperty(), "%"
                        )
                )
        );
        
        piechartType.getData().addAll(pieData);
    }
    
    public void displayBarChart(){
        XYChart.Series series = new XYChart.Series();
        series.getData().add(new XYChart.Data("Sun", cust.getTotalSpendByDay("Sunday")));
        series.getData().add(new XYChart.Data("Mon", cust.getTotalSpendByDay("Monday")));
        series.getData().add(new XYChart.Data("Teu", cust.getTotalSpendByDay("Tuesday")));
        series.getData().add(new XYChart.Data("Wed", cust.getTotalSpendByDay("Wednesday")));
        series.getData().add(new XYChart.Data("Thu", cust.getTotalSpendByDay("Thursday")));
        series.getData().add(new XYChart.Data("Fri", cust.getTotalSpendByDay("Friday")));
        series.getData().add(new XYChart.Data("Sat", cust.getTotalSpendByDay("Saturday")));
        
        barchartSpending.getData().addAll(series);
        
    }
    
    public void display(){
        displayCard();
        displayRecentTrans();
        displayBalance();
        displayPieChart();
        displayBarChart();
    }
        
    @FXML
    private void accountsMenu(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("AccountPage.fxml"));
        root = loader.load();
        AccountPageController acc = loader.getController();
        acc.setCustomer(cust);
        acc.displayBalance();
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
        cards.displayCard(cust.getKey());
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
        this.cust = null;
        this.admin = null;
        currencyChoice.getItems().addAll(Account.getCurrency());
        currencyChoice.getSelectionModel().selectFirst();
        currencyChoice.setOnAction(this::getBalance); 
    }    
}
