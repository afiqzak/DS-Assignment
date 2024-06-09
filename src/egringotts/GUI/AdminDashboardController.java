package egringotts.GUI;

import egringotts.*;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Asus
 */
public class AdminDashboardController implements Initializable {

    @FXML
    private Stage stage;
    
    @FXML
    private Scene scene;
    
    @FXML
    private Parent root;
    
    @FXML
    private TextField nameField, symbolField, rateField,proFeeField;
    
    @FXML
    private Label successLabel, totalUserField, weekTransactionField;
    
    @FXML
    private ChoiceBox<String> currencyChoice;
    
    @FXML
    private LineChart<String, Integer> monthlyLinechart;
    
    private Admin admin;
    private List<String> months;

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }
    
    public void display(){
        totalUserField.setText(admin.getTotalUser());
        weekTransactionField.setText(admin.getWeekTrans());
        
        XYChart.Series series = new XYChart.Series();
        for(int i = 0; i<months.size(); i++){
            series.getData().add(new XYChart.Data(months.get(i), admin.getMonthTrans(i+1)));
        }
        
        monthlyLinechart.getData().addAll(series);
    }
    
    @FXML
    private void settingsMenu(ActionEvent event) throws IOException, SQLException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("SettingsPage.fxml"));
        root = loader.load();
        SettingsPageController setting = loader.getController();
        setting.setAdmin(admin);
        setting.checkAdmin();
        setting.setProfile();
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);

        stage.setScene(scene);
        stage.show();
    }
    
    @FXML
    private void addCurrency(ActionEvent event) throws IOException, SQLException {
        String name = nameField.getText();
        String symbol = symbolField.getText();
        String choice = currencyChoice.getValue();
        float rate = Float.parseFloat(rateField.getText());
        double proFee = Double.parseDouble(proFeeField.getText());
        admin.addCurrency(name, symbol, choice, rate, proFee);
        successLabel.setVisible(true);
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.months = new ArrayList<>();
        int currentMonthValue = java.time.LocalDate.now().getMonthValue();

        for (int i = 1; i <= currentMonthValue; i++) {
            String monthName = Month.of(i).name();
            months.add(monthName);
        }
        successLabel.setVisible(false);
        currencyChoice.getItems().addAll(Account.getCurrency());
        currencyChoice.getSelectionModel().selectFirst();
    }    
    
}
