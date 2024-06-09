package egringotts.GUI;

import egringotts.*;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;

/**
 * FXML Controller class
 *
 * @author Asus
 */
public class AnalyticsPageController implements Initializable {

    @FXML
    private Stage stage;
    
    @FXML
    private Scene scene;
    
    @FXML
    private Parent root;
    
    @FXML
    private Label billField, otherField, totalField, daySpendField, entertainmentField, foodField, groceryField, exchangeField;

    @FXML
    private LineChart<String, Double> linechartMonthly;

    @FXML
    private ChoiceBox<String> monthChoice;

    @FXML
    private PieChart piechartType;
    
    private List<String> months;
    private PlatinumPatronus plat;
    private GoldenGalleon gold;
    private String monthSelected;
    private LocalDate currentDate = LocalDate.now();
    
    public void setUser(PlatinumPatronus plat){
        this.plat = plat;
    }
    
    public void setUser(GoldenGalleon gold){
        this.gold = gold;
    }
    
    public void getMonth(ActionEvent e){
        this.monthSelected = monthChoice.getValue();
        
        displayPiechart();
    }
    
    public void displayPiechart(){
        LocalDate currentDate = LocalDate.now();
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        if(plat != null){
            monthChoice.setVisible(true);
            int month = months.indexOf(monthSelected) + 1;

            ObservableList<PieChart.Data> pieData = 
                    FXCollections.observableArrayList(
                            new PieChart.Data("Ent", plat.getPercentageTypeForMonth("Entertainment", month)),
                            new PieChart.Data("Bill", plat.getPercentageTypeForMonth("Bill", month)),
                            new PieChart.Data("Grocery", plat.getPercentageTypeForMonth("Grocery", month)),
                            new PieChart.Data("Food", plat.getPercentageTypeForMonth("Food", month)),
                            new PieChart.Data("Exchange", plat.getPercentageTypeForMonth("Exchange", month)),
                            new PieChart.Data("Others", plat.getPercentageTypeForMonth("Others", month))
                    );

            piechartType.setData(pieData);

            entertainmentField.setText("Entertainment: " + decimalFormat.format(plat.getTypeSpendForMonth("Entertainment", month)) + "K");
            foodField.setText("Food: " + decimalFormat.format(plat.getTypeSpendForMonth("Food", month)) + "K");
            groceryField.setText("Grocery: " + decimalFormat.format(plat.getTypeSpendForMonth("Grocery", month)) + "K");
            billField.setText("Bill: " + decimalFormat.format(plat.getTypeSpendForMonth("Bill", month)) + "K");
            otherField.setText("Exchange: " + decimalFormat.format(plat.getTypeSpendForMonth("Exchange", month)) + "K");
            exchangeField.setText("Others: " + decimalFormat.format(plat.getTypeSpendForMonth("Other", month)) + "K");

            if(month == months.size()){
                totalField.setText("Spent So Far\n" + plat.getTotalSpendForMonth(month) + "K");
                daySpendField.setText("Daily average spending\n" + (decimalFormat.format(plat.getTotalSpendForMonth(month)/currentDate.getDayOfMonth())) + "K");
            }
            else{
                totalField.setText("This month's spending\n" + plat.getTotalSpendForMonth(month) + "K");
                daySpendField.setText("Daily average spending\n" + decimalFormat.format((plat.getTotalSpendForMonth(month)/LocalDate.of(currentDate.getYear(), month, 1).lengthOfMonth())) + "K");
            }
        }else{
            monthChoice.setVisible(false);
            ObservableList<PieChart.Data> pieData = 
                    FXCollections.observableArrayList(
                            new PieChart.Data("ent", gold.getPercentageTypeForCurrentMonth("Entertainment")),
                            new PieChart.Data("Bill", gold.getPercentageTypeForCurrentMonth("Bill")),
                            new PieChart.Data("Grocery", gold.getPercentageTypeForCurrentMonth("Grocery")),
                            new PieChart.Data("Food", gold.getPercentageTypeForCurrentMonth("Food")),
                            new PieChart.Data("Others", gold.getPercentageTypeForCurrentMonth("Others"))
                    );

            piechartType.setData(pieData);

            entertainmentField.setText("Entertainment: " + decimalFormat.format(gold.getTypeSpendForCurrentMonth("Entertainment")) + "K");
            foodField.setText("Food: " + decimalFormat.format(gold.getTypeSpendForCurrentMonth("Food")) + "K");
            groceryField.setText("Grocery: " + decimalFormat.format(gold.getTypeSpendForCurrentMonth("Grocery")) + "K");
            billField.setText("Bill: " + decimalFormat.format(gold.getTypeSpendForCurrentMonth("Bill")) + "K");
            exchangeField.setText("Exchange: " + decimalFormat.format(gold.getTypeSpendForCurrentMonth("Exchange")) + "K");
            otherField.setText("Other: " + decimalFormat.format(gold.getTypeSpendForCurrentMonth("Others")) + "K");
            
            totalField.setText("Spent So Far\n" + gold.getTotalSpendForCurrentMonth() + "K");
            daySpendField.setText("Daily average spending\n" + (gold.getTotalSpendForCurrentMonth()/currentDate.getDayOfMonth()) + "K");
        }
    }
    
    public void displayLinechart(){
        XYChart.Series series = new XYChart.Series();
        if(plat != null){
            for(int i = 0; i<months.size(); i++){
                series.getData().add(new XYChart.Data(months.get(i), plat.getMonthlySpend(i+1)));
            }
        }else{
            for(int i = 0; i<months.size(); i++){
                series.getData().add(new XYChart.Data(months.get(i), gold.getMonthlySpend(i+1)));
            }
        }
        
        linechartMonthly.getData().addAll(series);
    }
    
    public void display(){
        displayPiechart();
        displayLinechart();
    }
    
    @FXML
    private void dashboardMenu(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("MainDashboard.fxml"));
        root = loader.load();
        MainDashboardController main = loader.getController();
        if(plat != null){
            SilverSnitch cust = new SilverSnitch(plat.getKey(), "Platinum Patronus", plat.getPin(), plat.getUsername(), plat.getName(), plat.getPassword(), plat.getPhoneNum(), plat.getEmail(), plat.getDob(), plat.getAddress());
            main.display(cust);
        }else{
            SilverSnitch cust = new SilverSnitch(gold.getKey(), "Golden Galleon", gold.getPin(), gold.getUsername(), gold.getName(), gold.getPassword(), gold.getPhoneNum(), gold.getEmail(), gold.getDob(), gold.getAddress());
            main.display(cust);
        }
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
        if(plat != null){
            SilverSnitch cust = new SilverSnitch(plat.getKey(), "Platinum Patronus", plat.getPin(), plat.getUsername(), plat.getName(), plat.getPassword(), plat.getPhoneNum(), plat.getEmail(), plat.getDob(), plat.getAddress());
            acc.display(cust);
        }else{
            SilverSnitch cust = new SilverSnitch(gold.getKey(), "Golden Galleon", gold.getPin(), gold.getUsername(), gold.getName(), gold.getPassword(), gold.getPhoneNum(), gold.getEmail(), gold.getDob(), gold.getAddress());
            acc.display(cust);
        }
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
        if(plat != null){
            SilverSnitch cust = new SilverSnitch(plat.getKey(), "Platinum Patronus", plat.getPin(), plat.getUsername(), plat.getName(), plat.getPassword(), plat.getPhoneNum(), plat.getEmail(), plat.getDob(), plat.getAddress());
            trans.display(cust);
        }else{
            SilverSnitch cust = new SilverSnitch(gold.getKey(), "Golden Galleon", gold.getPin(), gold.getUsername(), gold.getName(), gold.getPassword(), gold.getPhoneNum(), gold.getEmail(), gold.getDob(), gold.getAddress());
            trans.display(cust);
        }
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
        if(plat != null){
            SilverSnitch cust = new SilverSnitch(plat.getKey(), "Platinum Patronus", plat.getPin(), plat.getUsername(), plat.getName(), plat.getPassword(), plat.getPhoneNum(), plat.getEmail(), plat.getDob(), plat.getAddress());
            cards.display(cust);
        }else{
            SilverSnitch cust = new SilverSnitch(gold.getKey(), "Golden Galleon", gold.getPin(), gold.getUsername(), gold.getName(), gold.getPassword(), gold.getPhoneNum(), gold.getEmail(), gold.getDob(), gold.getAddress());
            cards.display(cust);
        }
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
        if(plat != null){
            SilverSnitch cust = new SilverSnitch(plat.getKey(), "Platinum Patronus", plat.getPin(), plat.getUsername(), plat.getName(), plat.getPassword(), plat.getPhoneNum(), plat.getEmail(), plat.getDob(), plat.getAddress());
            exchange.display(cust);
        }else{
            SilverSnitch cust = new SilverSnitch(gold.getKey(), "Golden Galleon", gold.getPin(), gold.getUsername(), gold.getName(), gold.getPassword(), gold.getPhoneNum(), gold.getEmail(), gold.getDob(), gold.getAddress());
            exchange.display(cust);
        }
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
        if(plat != null){
            SilverSnitch cust = new SilverSnitch(plat.getKey(), "Platinum Patronus", plat.getPin(), plat.getUsername(), plat.getName(), plat.getPassword(), plat.getPhoneNum(), plat.getEmail(), plat.getDob(), plat.getAddress());
            setting.setCustomer(cust);
        }else{
            SilverSnitch cust = new SilverSnitch(gold.getKey(), "Golden Galleon", gold.getPin(), gold.getUsername(), gold.getName(), gold.getPassword(), gold.getPhoneNum(), gold.getEmail(), gold.getDob(), gold.getAddress());
            setting.setCustomer(cust);
        }
        setting.checkAdmin();
        setting.setProfile();
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);

        stage.setScene(scene);
        stage.show();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.months = new ArrayList<>();
        int currentMonthValue = java.time.LocalDate.now().getMonthValue();

        for (int i = 1; i <= currentMonthValue; i++) {
            String monthName = Month.of(i).name();
            months.add(monthName);
        }
        
        monthChoice.getItems().addAll(this.months);
        monthChoice.getSelectionModel().selectLast();
        monthChoice.setOnAction(this::getMonth);
        this.monthSelected = months.getLast();
    } 
}
