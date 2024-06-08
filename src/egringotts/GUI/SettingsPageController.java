package egringotts.GUI;

import egringotts.*;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
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
    private Pane security, profile, user, adminPane, customerPane;
    
    @FXML
    private PasswordField passField, newPassField, passF;
    
    @FXML
    private Button profileButton, securityButton, userButton;
    
    @FXML
    private Label accNumLabel, tierLabel, nameLabel, emailLabel, phoneLabel, usernameLabel, addressLabel, 
            dobLabel, incorrectLabel, successLabel,successLabel1;
    
    @FXML
    private TextField nameF, emailF, phoneF, addressF, usernameF;
    
    @FXML
    private DatePicker dobF;
    
    @FXML
    private TableView<Customer> userTable;
        
    @FXML
    private TableColumn<Customer, String> accNumColumn, addressColumn,dobColumn, emailColumn,nameColumn,phoneColumn,tierColumn;
    
    private egringotts.Customer cust;
    private Admin admin;
    
    ObservableList<Customer> list ;
    
    public void setCustomer(egringotts.Customer cust){
        this.cust = cust;
        customerPane.setVisible(true);
        adminPane.setVisible(false);
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
        adminPane.setVisible(true);
        customerPane.setVisible(false);
        list = FXCollections.observableArrayList(admin.tableUser());
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

        accNumColumn.setCellValueFactory(new PropertyValueFactory<Customer,String>("accountNum"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<Customer,String>("name"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<Customer,String>("phoneNum"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<Customer,String>("email"));
        dobColumn.setCellValueFactory(new PropertyValueFactory<Customer,String>("DOB"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<Customer,String>("address"));
        tierColumn.setCellValueFactory(new PropertyValueFactory<Customer,String>("tier"));
        
        userTable.setItems(list);
    }
    
    @FXML
    private void updatePassButton(ActionEvent event) throws IOException, SQLException {
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
    private void addAdminButton(ActionEvent event) throws IOException, SQLException {
        String name = nameF.getText();
        String email = emailF.getText();
        String phone = phoneF.getText();
        String address = addressF.getText();
        LocalDate dobf = dobF.getValue();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");  // Adjust format as needed
        String dob = dobf.format(formatter);
        String username = usernameF.getText();
        String password = passF.getText();
        String accountNum = Integer.toString(admin.getUserId());
        
        admin.addAdmin(accountNum, name, phone, email, username, password, dob, address);
        successLabel1.setVisible(false);
    }
    
    @FXML
    private void adminMenu(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("AdminDashboard.fxml"));
        root = loader.load();
        AdminDashboardController Admin = loader.getController();
        Admin.setAdmin(admin);
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);

        stage.setScene(scene);
        stage.show();
    }
    
    @FXML
    private void dashboardMenu(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("MainDashboard.fxml"));
        root = loader.load();
        MainDashboardController main = loader.getController();
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
        successLabel1.setVisible(false);
        
        this.admin = null;
        this.cust = null;
        
    } 
    
    public void setProfile() throws SQLException{
        if(cust != null){
            accNumLabel.setText("  " + cust.getKey());
            tierLabel.setText("  " + cust.getTier());
            nameLabel.setText("  " + cust.getName());
            emailLabel.setText("  " + cust.getEmail());
            phoneLabel.setText("  " + cust.getPhoneNum());
            usernameLabel.setText("  " + cust.getUsername());
            addressLabel.setText("  " + cust.getAddress());
            dobLabel.setText("  " + cust.getDob());
        } else {
            accNumLabel.setText("  " + admin.getKey());
            tierLabel.setVisible(false);
            nameLabel.setText("  " + admin.getName());
            emailLabel.setText("  " + admin.getEmail());
            phoneLabel.setText("  " + admin.getPhoneNum());
            usernameLabel.setText("  " + admin.getUsername());
            addressLabel.setText("  " + admin.getAddress());
            dobLabel.setText("  " + admin.getDob());
        }
    }
}

