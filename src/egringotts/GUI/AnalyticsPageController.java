package egringotts.GUI;

import egringotts.*;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
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
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;

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
    private ChoiceBox<String> monthChoice;
    
    @FXML
    private BarChart<String, Double> barchartMethod;

    @FXML
    private LineChart<String, Double> linechartWeekly;
    
    @FXML
    private PieChart piechartType;

    
    private List<String> months;
    private PlatinumPatronus cust;
    private String monthSelected;
    
    public void setUser(PlatinumPatronus cust){
        this.cust = cust;
    }
    
    public void getMonth(ActionEvent e){
        this.monthSelected = monthChoice.getValue();
    }
    
    public void displayPiechart(){
        int month = months.indexOf(monthSelected) + 1;
        
        ObservableList<PieChart.Data> pieData = 
                FXCollections.observableArrayList(
                        new PieChart.Data("ent", cust.getPercentageTypeForMonth("entertainment", month)),
                        new PieChart.Data("bill", cust.getPercentageTypeForMonth("bill", month)),
                        new PieChart.Data("grocery", cust.getPercentageTypeForMonth("grocery", month)),
                        new PieChart.Data("food", cust.getPercentageTypeForMonth("food", month)),
                        new PieChart.Data("other", cust.getPercentageTypeForMonth("other", month))
                );
        
        piechartType.setData(pieData);
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
