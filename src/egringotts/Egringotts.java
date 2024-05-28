package egringotts;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author afiqz
 */
public class Egringotts extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent loginroot = FXMLLoader.load(getClass().getResource("login.fxml"));
        //Parent signuproot = FXMLLoader.load(getClass().getResource("signup.fxml"));
        //Parent mainroot = FXMLLoader.load(getClass().getResource("MainDashboard.fxml"));
        
        Scene login = new Scene(loginroot);
        //Scene signup = new Scene(signuproot);
        //Scene main = new Scene(mainroot);
        
        //stage.setMaximized(true);
        stage.setScene(login);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
