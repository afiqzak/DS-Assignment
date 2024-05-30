package egringotts.GUI;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 *
 * @author afiqz
 */
public class SignupController implements Initializable {
    
    @FXML
    private Stage stage;
    
    @FXML
    private Scene scene;
    
    @FXML
    private Parent root;
    
     @FXML
    private Label err;
    
    @FXML
    private TextField nameField,addressField,emailField,phoneNumField,poscodeField,usernameField;

    @FXML
    private DatePicker dobField;

    @FXML
    private PasswordField passwordField, passwordConfirmField;

    @FXML
    private ChoiceBox<String> stateField;
    private String[] State = {"Hogwarts","Forbidden Forest","Great Lake","Hogsmeade","London","Countryside","Salem"};
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        stateField.getItems().addAll(State);
    }    
    
    @FXML
    private void signupButton(ActionEvent event) throws IOException {
        String name, email, address, phoneNum, username, password, state;
        int poscode = 0;
        LocalDate dob;
        boolean next = true;
        name = nameField.getText();
        email = emailField.getText();
        phoneNum = phoneNumField.getText();
        dob = dobField.getValue();
        address = addressField.getText();
        //state = stateField.getTypeSelector();
        username = usernameField.getText();
        password = passwordField.getText();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");  // Adjust format as needed
        String dobString = dob.format(formatter);
        
        try{
            poscode = Integer.parseInt(poscodeField.getText());
        } catch (NumberFormatException e){
            next = false;
            err.setText("Number only");
        }
        
        egringotts.Customer cust = new egringotts.Customer(username, name, phoneNum, email, password, dobString, address + ", " + poscode);
        
        if(next){
            egringotts.Account.signUp(cust);
            root = FXMLLoader.load(getClass().getResource("login.fxml"));
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root);

            stage.setScene(scene);
            //stage.setMaximized(true);
            stage.show();
        }
    }
}
