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
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
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
    private Pane security, profile, user;
    
    @FXML
    private PasswordField passField, newPassField;
    
    @FXML
    private Button profileButton, securityButton, userButton;
    
    @FXML
    private Label accNumLabel, tierLabel, nameLabel, emailLabel, phoneLabel, usernameLabel, addressLabel, dobLabel, incorrectLabel, successLabel;
    
    @FXML
    private TableView<Customer> userTable;
        
    @FXML
    private TableColumn<Customer, String> accNumColumn;

    @FXML
    private TableColumn<Customer, String> addressColumn;

    @FXML
    private TableColumn<Customer, String> dobColumn;

    @FXML
    private TableColumn<Customer, String> emailColumn;

    @FXML
    private TableColumn<Customer, String> nameColumn;

    @FXML
    private TableColumn<Customer, String> phoneColumn;

    @FXML
    private TableColumn<Customer, String> tierColumn;
    
    private egringotts.Customer cust;
    private Admin admin;
    
    ObservableList<Customer> list = FXCollections.ObservableArrayList(
            
    );
    
    public void setCustomer(egringotts.Customer cust){
        this.cust = cust;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }
    
    public void checkAdmin(){
        if(admin != null) {
            userButton.setVisible(true);
        }else if (user != null){
            userButton.setVisible(false);
        } 
    }
    
    @FXML
    private void profileButton(ActionEvent event) throws IOException {
        security.setVisible(false);
        user.setVisible(false);
        profile.setVisible(true);
    }
    @FXML
    private void securityButton(ActionEvent event) throws IOException {
        profile.setVisible(false);
        user.setVisible(false);
        security.setVisible(true);
    }
    @FXML
    private void userButton(ActionEvent event) throws IOException {
        profile.setVisible(false);
        security.setVisible(false);
        user.setVisible(true);
    }
    
    @FXML
    private void updatePassButton(ActionEvent event) throws IOException, SQLException {
        System.out.println(admin.getAccountNum());
        String password = newPassField.getText();
        if (cust != null && passField.getText().equals(cust.getPassword())){
            cust.updateCustomerPassword(password);
            incorrectLabel.setVisible(false);
            successLabel.setVisible(true);
        }else if (admin!=null && passField.getText().equals(admin.getPassword())){
            admin.getPassword();
            admin.updateAdminPassword(password);
            incorrectLabel.setVisible(false);
            successLabel.setVisible(true);
        }else
            incorrectLabel.setVisible(true);
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
        user.setVisible(false);
        profile.setVisible(true);
        incorrectLabel.setVisible(false);
        successLabel.setVisible(false);
    } 
    
    public void setProfile() throws SQLException{
        if(cust != null){
            accNumLabel.setText("  " + cust.getAccountNum());
            tierLabel.setText("  " + cust.getTier());
            nameLabel.setText("  " + cust.getName());
            emailLabel.setText("  " + cust.getEmail());
            phoneLabel.setText("  " + cust.getPhoneNum());
            usernameLabel.setText("  " + cust.getUsername());
            addressLabel.setText("  " + cust.getAddress());
            dobLabel.setText("  " + cust.getDOB());
        } else {
            accNumLabel.setText("  " + admin.getAccountNum());
            tierLabel.setVisible(false);
            nameLabel.setText("  " + admin.getName());
            emailLabel.setText("  " + admin.getEmail());
            phoneLabel.setText("  " + admin.getPhoneNum());
            usernameLabel.setText("  " + admin.getUsername());
            addressLabel.setText("  " + admin.getAddress());
            dobLabel.setText("  " + admin.getDOB());
        }
    }
}

