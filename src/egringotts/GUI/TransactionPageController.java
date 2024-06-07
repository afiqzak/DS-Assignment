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
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Asus
 */
public class TransactionPageController implements Initializable {

    @FXML
    private Stage stage;
    
    @FXML
    private Scene scene;
    
    @FXML
    private Parent root;
    
    @FXML
    private TableView<Transaction> history;
        
    @FXML
    private TableColumn<Transaction, String> receipentColumn, descColumn, typeColumn, methodColumn, dateColumn, amountColumn;
    
    @FXML
    private LineChart<String, Double> linechartMonthly;
    
    private PensivePast pensive;
    private Admin admin;
    private Customer cust;
    
    ObservableList<Transaction> list;
    
    public void setCustomer(egringotts.Customer cust){
        this.cust = cust;
        
        list = FXCollections.observableArrayList(pensive.history(cust.getKey()));
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }
    
    public void historyTable(){
        receipentColumn.setCellValueFactory(new PropertyValueFactory<Transaction,String>("receipent"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<Transaction,String>("type"));
        descColumn.setCellValueFactory(new PropertyValueFactory<Transaction,String>("description"));
        methodColumn.setCellValueFactory(new PropertyValueFactory<Transaction,String>("method"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<Transaction,String>("date"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<Transaction,String>("currAmount"));
        
        history.setItems(list);
    }
    
    public void displayLineChart(){
        XYChart.Series series = new XYChart.Series();
        series.getData().add(new XYChart.Data("Jan", cust.getMonthlySpend(1)));
        series.getData().add(new XYChart.Data("Feb", cust.getMonthlySpend(2)));
        series.getData().add(new XYChart.Data("Mar", cust.getMonthlySpend(3)));
        series.getData().add(new XYChart.Data("Apr", cust.getMonthlySpend(4)));
        series.getData().add(new XYChart.Data("May", cust.getMonthlySpend(5)));
        series.getData().add(new XYChart.Data("Jun", cust.getMonthlySpend(6)));
        series.getData().add(new XYChart.Data("Jul", cust.getMonthlySpend(7)));
        series.getData().add(new XYChart.Data("Aug", cust.getMonthlySpend(8)));
        series.getData().add(new XYChart.Data("Sep", cust.getMonthlySpend(9)));
        series.getData().add(new XYChart.Data("Okt", cust.getMonthlySpend(10)));
        series.getData().add(new XYChart.Data("Nov", cust.getMonthlySpend(11)));
        series.getData().add(new XYChart.Data("Dis", cust.getMonthlySpend(12)));
        
        linechartMonthly.getData().addAll(series);
        
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
    private void cardsMenu(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("CardsPage.fxml"));
        root = loader.load();
        CardsPageController cards = loader.getController();
        cards.setCustomer(cust);
        cards.display();
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
        
    }    
    
    
}
