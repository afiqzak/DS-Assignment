package egringotts.GUI;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Asus
 */
public class SettingsPageController implements Initializable {

    @FXML
    private Stage stage;
    
    @FXML
    private Scene scene;
    
    @FXML
    private Parent root;
    
    @FXML
    private Pane security, profile;
    
    @FXML
    private PasswordField passField, newPassField;
    
    @FXML
    private Label accNumLabel, tierLabel, nameLabel, emailLabel, phoneLabel, usernameLabel, addressLabel, poscodeLabel, stateLabel, dobLabel;
    
    @FXML
    private void profileButton(ActionEvent event) throws IOException {
        security.setVisible(false);
        profile.setVisible(true);
    }
    @FXML
    private void securityButton(ActionEvent event) throws IOException {
        profile.setVisible(false);
        security.setVisible(true);
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
        root = FXMLLoader.load(getClass().getResource("AccountPage.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);

        stage.setScene(scene);
        stage.show();
    }
    @FXML
    private void logoutButton(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("Login.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);

        stage.setScene(scene);
        stage.show();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        security.setVisible(false);
        profile.setVisible(true);
        
        /*accNumLabel.setText();
        tierLabel.setText();
        nameLabel.setText();
        emailLabel.setText();
        phoneLabel.setText();
        usernameLabel.setText();
        addressLabel.setText();
        poscodeLabel.setText();
        stateLabel.setText();
        dobLabel.setText();*/
    } 
}

