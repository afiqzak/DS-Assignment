package egringotts.GUI;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 *
 * @author afiqz
 */
public class Egringotts extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent loginroot = FXMLLoader.load(getClass().getResource("MainDashboard.fxml"));
        Scene login = new Scene(loginroot);
        stage.getIcons().add(new Image(getClass().getResourceAsStream("Logo.png")));
        stage.setScene(login);
        stage.setTitle("E-Gringotts");
        
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
    
}
