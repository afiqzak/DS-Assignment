package egringotts.GUI;

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
        Parent loginroot = FXMLLoader.load(getClass().getResource("Login.fxml"));
        Scene login = new Scene(loginroot);
        
        stage.setScene(login);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
    
}
