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
    private void signupButton(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("login.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        
        stage.setScene(scene);
        //stage.setMaximized(true);
        stage.show();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
