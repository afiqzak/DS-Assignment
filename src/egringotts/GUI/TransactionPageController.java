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
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
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
    
    @FXML
    private Pane monthltExpense;
    
    @FXML
    private Button analyticsButton;
    
    @FXML
    private VBox menu;
    
    private PensivePast pensive;
    private SilverSnitch cust;
    
    ObservableList<Transaction> list;
    
    public void setCustomer(egringotts.SilverSnitch cust){
        this.cust = cust;
        
        list = FXCollections.observableArrayList(pensive.history(cust.getKey()));
        if(cust.getTier().equals("Silver Snitch"))
            menu.getChildren().remove(analyticsButton);
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
    
    public void display(SilverSnitch cust){
        setCustomer(cust);
        historyTable();
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
        if(cust.getTier().equals("Platinum Patronus")){
            PlatinumPatronus plat = new PlatinumPatronus(cust.getKey(), cust.getBalances(), cust.getUsername(), cust.getName(), cust.getPassword(), cust.getPhoneNum(), cust.getEmail(), cust.getDob(), cust.getAddress());
            analytics.setUser(plat);
        }else{
            GoldenGalleon gold = new GoldenGalleon(cust.getKey(), cust.getBalances(), cust.getUsername(), cust.getName(), cust.getPassword(), cust.getPhoneNum(), cust.getEmail(), cust.getDob(), cust.getAddress());
            analytics.setUser(gold);
        } 
        analytics.display();
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
        
    }    
    
    
}
