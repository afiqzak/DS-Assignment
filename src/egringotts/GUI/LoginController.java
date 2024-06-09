package egringotts.GUI;

import egringotts.*;
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
    private Label errorLabel, pinText;

    @FXML
    private Button login;

    @FXML
    private PasswordField passField, pinField;

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
    
    private boolean isFill = false;
    private String user;
    private Account<User> acc;
    
    private static boolean containsLetter(String str) {
        for (char c : str.toCharArray()) {
            if (Character.isLetter(c)) {
              return true;
            }
        }
        return false;
    }
    
    @FXML
    private void loginAction(ActionEvent event) throws IOException, SQLException {
        errorLabel.setText("");
        
        if(this.isFill){
            //check if pin contain letter
            if(containsLetter(pinField.getText())){
                errorLabel.setText("Pin cannot contains letter");
                return;
            }
            this.acc.getAccount().setPin(pinField.getText());
            if(user.equalsIgnoreCase("admin") && acc.pinVerification(user)){
                FXMLLoader loader = new FXMLLoader(getClass().getResource("AdminDashboard.fxml"));
                root = loader.load();
                AdminDashboardController admin = loader.getController();
                egringotts.Admin Admin = egringotts.Account.getAdminByUsername(usernameField.getText());
                admin.setAdmin(Admin);
                admin.display();
                stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                adminDashboard = new Scene(root);
                stage.setScene(adminDashboard);
                stage.show();
            }else if(user.equalsIgnoreCase("customer") && acc.pinVerification(user)){
                FXMLLoader loader = new FXMLLoader(getClass().getResource("MainDashboard.fxml"));
                root = loader.load();
                MainDashboardController main = loader.getController();
                egringotts.SilverSnitch cust = egringotts.Account.getCustomerByUsername(usernameField.getText());
                main.display(cust);
                stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                mainDashboard = new Scene(root);
                stage.setScene(mainDashboard);
                stage.show();
            }else{
               errorLabel.setText("incorrect pin"); 
               this.isFill = false;
                pinText.setVisible(false);
                pinField.setVisible(false);
                usernameField.setText("");
                passField.setText("");
                pinField.setText("");
                return;
            }
        }
        this.acc = new Account(new Admin(usernameField.getText(), passField.getText()));

        this.user = acc.signIn();
        if(!user.isEmpty()){
            this.isFill = true;
            System.out.println(this.isFill);
            pinText.setVisible(true);
            pinField.setVisible(true);
            return;
        }else{
            errorLabel.setText("incorrect username or password");
            return;
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
        pinText.setVisible(false);
        pinField.setVisible(false);
    }    
    
}
