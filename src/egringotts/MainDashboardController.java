package egringotts;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author Asus
 */
public class MainDashboardController {
    @FXML
    private Stage stage;
    
    @FXML
    private Scene scene;
    
    @FXML
    private Parent root;
    
    @FXML
    private void transferMenu(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("TransferPage.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    private void transactionMenu(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("TransactionPage.fxml"));
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
    private void accountsMenu(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("AccountsPage.fxml"));
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
}
