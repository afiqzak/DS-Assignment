package egringotts.GUI;

import egringotts.*;
import java.io.IOException;
import java.net.URL;
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
    
    private PensivePast pensive;
    private Admin admin;
    private Customer cust;
    
    ObservableList<Transaction> list;
    
    public void setCustomer(egringotts.Customer cust){
        this.cust = cust;
        
        list = FXCollections.observableArrayList(pensive.history(cust.getAccountNum()));
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
    
    @FXML
    private void dashboardMenu(ActionEvent event) throws IOException {
    root = FXMLLoader.load(getClass().getResource("MainDashboard.fxml"));
    stage = (Stage)((Node)event.getSource()).getScene().getWindow();
    scene = new Scene(root);
    
    stage.setScene(scene);
    stage.show();
    }
    @FXML
    private void accountsMenu(ActionEvent event) throws IOException {
    root = FXMLLoader.load(getClass().getResource("AccountPage.fxml"));
    stage = (Stage)((Node)event.getSource()).getScene().getWindow();
    scene = new Scene(root);
    
    stage.setScene(scene);
    stage.show();
    }
    @FXML
    private void cardsMenu(ActionEvent event) throws IOException {
    root = FXMLLoader.load(getClass().getResource("CardsPage.fxml"));
    stage = (Stage)((Node)event.getSource()).getScene().getWindow();
    scene = new Scene(root);
    
    stage.setScene(scene);
    stage.show();
    }
    @FXML
    private void exchangeMenu(ActionEvent event) throws IOException {
    root = FXMLLoader.load(getClass().getResource("ExchangePage.fxml"));
    stage = (Stage)((Node)event.getSource()).getScene().getWindow();
    scene = new Scene(root);
    
    stage.setScene(scene);
    stage.show();
    }
    @FXML
    private void analyticsMenu(ActionEvent event) throws IOException {
    root = FXMLLoader.load(getClass().getResource("AnalyticsPage.fxml"));
    stage = (Stage)((Node)event.getSource()).getScene().getWindow();
    scene = new Scene(root);
    
    stage.setScene(scene);
    stage.show();
    }
    @FXML
    private void settingsMenu(ActionEvent event) throws IOException {
    root = FXMLLoader.load(getClass().getResource("SettingsPage.fxml"));
    stage = (Stage)((Node)event.getSource()).getScene().getWindow();
    scene = new Scene(root);
    
    stage.setScene(scene);
    stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    
    
}
