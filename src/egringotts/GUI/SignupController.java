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
    private Label err, errPass, errPoscode, errUsername, errPin;
    
    @FXML
    private TextField nameField,addressField,emailField,phoneNumField,poscodeField,usernameField;

    @FXML
    private DatePicker dobField;

    @FXML
    private PasswordField passwordField, passwordConfirmField, pinField, pinConfirmField;
    
    @FXML
    private ChoiceBox<String> currency;

    @FXML
    private TextField amountField;

    @FXML
    private ChoiceBox<String> stateField;
    
    private String[] State = {"Hogwarts","Forbidden Forest","Great Lake","Hogsmeade","London","Countryside","Salem"};
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        stateField.getItems().addAll(State);
        currency.getItems().addAll(egringotts.Account.getCurrency());
    }    
    
    @FXML
    private void signupButton(ActionEvent event) throws IOException {
        //check if ll of the box have been filled
        err.setText("");
        errPass.setText("");
        errPoscode.setText("");
        if(nameField.getText().isEmpty() || addressField.getText().isEmpty() || emailField.getText().isEmpty() || phoneNumField.getText().isEmpty() || poscodeField.getText().isEmpty() ||
                usernameField.getText().isEmpty() || dobField.getValue() == null || stateField.getTypeSelector().isEmpty() || passwordField.getText().isEmpty() || passwordConfirmField.getText().isEmpty()){
            err.setText("Please fill in all of the information");
            return;
        }
        String name, email, address, phoneNum, username, password, confirmPass, state, pc, pin, confirmPin;
        int poscode;
        LocalDate dob;
        name = nameField.getText();
        email = emailField.getText();
        phoneNum = phoneNumField.getText();
        dob = dobField.getValue();
        address = addressField.getText();
        state = stateField.getValue();
        username = usernameField.getText();
        password = passwordField.getText();
        confirmPass = passwordConfirmField.getText();
        pin = pinField.getText();
        confirmPin = pinConfirmField.getText();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");  // Adjust format as needed
        String dobString = dob.format(formatter);
        
        //check if poscode contain letter
        try{
            poscode = Integer.parseInt(poscodeField.getText());
        } catch (NumberFormatException e){
            errPoscode.setText("Number only");
            return;
        }
        
        //chechk if pin contain letter
        if(containsLetter(pin) || containsLetter(confirmPin)){
            errPin.setText("Number Only");
        }
        
        //chechk if password match
        if(!confirmPass.equals(password)){
            errPass.setText("Password does not match");
            return;
        }
        
        //chechk if pin match
        if(!confirmPin.equals(pin)){
            errPass.setText("Pin does not match");
            return;
        }
        
        egringotts.SilverSnitch cust = new egringotts.SilverSnitch(pin, username, name, password, phoneNum, email, dobString, address + ", " + state);
        egringotts.Account<egringotts.SilverSnitch> acc = new egringotts.Account<>(cust);
        
        //check if username already exist or not
        if(!acc.signUp(currency.getValue(), Double.parseDouble(amountField.getText()), pin)){
            errUsername.setText("username already exist");
            return;
        }
        root = FXMLLoader.load(getClass().getResource("login.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);

        stage.setScene(scene);
        //stage.setMaximized(true);
        stage.show();
    }
    
    private static boolean containsLetter(String str) {
        for (char c : str.toCharArray()) {
            if (Character.isLetter(c)) {
              return true;
            }
        }
        return false;
    }
}
