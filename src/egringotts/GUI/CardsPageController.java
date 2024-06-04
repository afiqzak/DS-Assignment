package egringotts.GUI;

import egringotts.DBConnection;
import static egringotts.GUI.MainDashboardController.masking;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Asus
 */
public class CardsPageController implements Initializable {

    @FXML
    private Stage stage;
    
    @FXML
    private Scene scene;
    
    @FXML
    private Parent root;
    
    @FXML
    private Label expField, expField2, expField3, nameField, nameField2, nameField3, numField, numField2, numField3;
    
    @FXML
    private Pane card1, card2, card3;
    
    public int card;
    
    public void displayCard(String accNum){
        String query = "SELECT card.AccountNum, card.Card_Number,card.Expiration_Date, account.Name_Customer FROM card INNER JOIN account ON card.AccountNum=account.AccountNum WHERE card.AccountNum = ?;";
        this.card = 1;
        
        try (Connection con = DBConnection.openConn();
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, accNum);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                if(card == 1){
                    expField.setText(rs.getString("Expiration_Date"));
                    nameField.setText(rs.getString("Name_Customer"));
                    numField.setText(masking(rs.getString("Card_Number")));
                    card2.setVisible(false);
                }else if(card == 2){
                    expField2.setText(rs.getString("Expiration_Date"));
                    nameField2.setText(rs.getString("Name_Customer"));
                    numField2.setText(masking(rs.getString("Card_Number")));
                    card2.setVisible(true);
                }else if(card == 3){
                    expField3.setText(rs.getString("Expiration_Date"));
                    nameField3.setText(rs.getString("Name_Customer"));
                    numField3.setText(masking(rs.getString("Card_Number")));
                    card3.setVisible(true);
                }else if(card==0){
                    card1.setVisible(false);
                    card2.setVisible(false);
                    card3.setVisible(false);
                }
                this.card++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    private void dashboardMenu(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("MainDashboard.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);

        stage.setScene(scene);
        stage.show();
    }
    @FXML
    private void accountsMenu(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("AccountPage.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);

        stage.setScene(scene);
        stage.show();
    }
    @FXML
    private void transactionMenu(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("TransactionPage.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);

        stage.setScene(scene);
        stage.show();
    }
    @FXML
    private void exchangeMenu(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("ExchangePage.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);

        stage.setScene(scene);
        stage.show();
    }
    @FXML
    private void analyticsMenu(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("AnalyticsPage.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);

        stage.setScene(scene);
        stage.show();
    }
    @FXML
    private void settingsMenu(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("SettingsPage.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);

        stage.setScene(scene);
        stage.show();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    } 
    
}
