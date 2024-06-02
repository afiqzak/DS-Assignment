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
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 *
 * @author afiqz
 */
public class LoginController implements Initializable {
    @FXML
    private Label errorLabel;

    @FXML
    private Button login;

    @FXML
    private PasswordField passField;

    @FXML
    private Button signup;

    @FXML
    private TextField usernameField;
    
    @FXML
    private Stage stage;
    
    @FXML
    private Scene mainDashboard;
    
    @FXML
    private Parent root;
    
    @FXML
    private void loginAction(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("MainDashboard.fxml"));
        root = loader.load();
        MainDashboardController main = loader.getController();
        String user = egringotts.Account.signIn(usernameField.getText(), passField.getText());
        System.out.println(user);
        if(!user.isEmpty()){
            if(user.equalsIgnoreCase("admin")) {
                egringotts.Admin admin = new egringotts.Admin(usernameField.getText(), passField.getText());
                main.setAdmin(admin);
            }else{
                egringotts.Customer cust = egringotts.Account.getCustomerByUsername(usernameField.getText());
                main.setCustomer(cust);
                
            } 
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            mainDashboard = new Scene(root);

            stage.setScene(mainDashboard);
            //stage.setMaximized(true);
            stage.show();
        }else{
            errorLabel.setText("incorrect username or password");
        }
        
        /*switch(user){
        case "admin":
        egringotts.Admin admin = new egringotts.Admin(usernameField.getText(), passField.getText());
        break;
        case "customer":
        egringotts.Customer cust = new egringotts.Customer(usernameField.getText(), passField.getText());
        break;
        case null:
        errorLabel.setText("incorrect username or password");
        }*/
    }
    
    @FXML
    private void signupAction(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("signup.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        mainDashboard = new Scene(root);
        
        stage.setScene(mainDashboard);
        //stage.setMaximized(true);
        stage.show();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }    
    
}
