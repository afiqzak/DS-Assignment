package egringotts.GUI;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
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
    private Scene mainDashboard,adminDashboard,signUp;
    
    @FXML
    private Parent root;
    
    @FXML
    private void loginAction(ActionEvent event) throws IOException, SQLException {
        String user = egringotts.Account.signIn(usernameField.getText(), passField.getText());
        System.out.println(user);
        if(!user.isEmpty()){
            if(user.equalsIgnoreCase("admin")) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("AdminDashboard.fxml"));
                root = loader.load();
                AdminDashboardController admin = loader.getController();
                egringotts.Admin Admin = egringotts.Account.getAdminByUsername(usernameField.getText());
                admin.setAdmin(Admin);
                System.out.println(Admin.getID());
                stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                adminDashboard = new Scene(root);
                stage.setScene(adminDashboard);
            }else{
                FXMLLoader loader = new FXMLLoader(getClass().getResource("MainDashboard.fxml"));
                root = loader.load();
                MainDashboardController main = loader.getController();
                egringotts.Customer cust = egringotts.Account.getCustomerByUsername(usernameField.getText());
                main.setCustomer(cust);
                main.display();
                stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                mainDashboard = new Scene(root);
                stage.setScene(mainDashboard);
            } 
            stage.show();
        }else{
            errorLabel.setText("incorrect username or password");
        }
    }
    
    @FXML
    private void signupAction(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("signup.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        signUp = new Scene(root);
        
        stage.setScene(signUp);
        stage.show();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }    
    
}
