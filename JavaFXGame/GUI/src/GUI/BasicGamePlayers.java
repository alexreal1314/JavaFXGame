package GUI;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by alex on 24/12/2016.
 */


public class BasicGamePlayers  {

    public static Stage m_PlayerChooseStage;
    public static Scene m_BPScene;
    @FXML private Button continueButton;
    @FXML private TextField player1Textfield;
    @FXML private TextField player2Textfield;
    @FXML private CheckBox comp1Checkbox;
    @FXML private CheckBox comp2Checkbox;

    public BasicGamePlayers() { }

    public void setController() throws IOException{
        FXMLLoader loader = new FXMLLoader();
        //loader.setController((this));
        URL url = getClass().getResource("/resources/BasicPlayerChooser.fxml");
        Parent root = loader.load(url.openStream());
        //location = getClass().getResource("/resources/BasicPlayerChooser.fxml");
        //loader.setLocation(url);

        m_BPScene = new Scene(root, 364, 186);
        m_PlayerChooseStage = new Stage();
        m_PlayerChooseStage.setTitle("Basic Player Chooser");
        m_PlayerChooseStage.centerOnScreen();
        m_PlayerChooseStage.setScene(m_BPScene);
        m_PlayerChooseStage.initModality(Modality.WINDOW_MODAL);
        m_PlayerChooseStage.initOwner(GameUI.m_primaryStage);
        m_PlayerChooseStage.showAndWait();
    }

    @FXML
    private void startBasicGame(){
        String player1Name;
        String player2Name;

        if (player1Textfield.getText().isEmpty()) {
            player1Name = "Player_1";
        }
        else {
            player1Name = player1Textfield.getText();
        }

        if (player2Textfield.getText().isEmpty()){
            player2Name = "Player_2";
        }
        else {
            player2Name = player2Textfield.getText();
        }

        List<String> basicPlayers = new ArrayList<>();
        basicPlayers.add(player1Name);
        if (comp1Checkbox.isSelected()){
            basicPlayers.add("Computer");
        }
        else {
            basicPlayers.add("Human");
        }

        basicPlayers.add(player2Name);
        if (comp2Checkbox.isSelected()){
            basicPlayers.add("Computer");
        }
        else {
            basicPlayers.add("Human");
        }

        GameLoader.myGame.InitBasicPlayers(basicPlayers);
        m_PlayerChooseStage.close();
    }

    @FXML
    private void comp1CheckboxHandle(){

        if (comp1Checkbox.isSelected()){
            player1Textfield.setText("Computer_1");
            player1Textfield.setDisable(true);
        }
        else{
            player1Textfield.clear();
            player1Textfield.setPromptText("Enter Name");
            player1Textfield.setDisable(false);
        }
    }

    @FXML
    private void comp2CheckboxHandle(){
        if (comp2Checkbox.isSelected()){
            player2Textfield.setText("Computer_2");
            player2Textfield.setDisable(true);
        }
        else {
            player2Textfield.clear();
            player2Textfield.setPromptText("Enter Name");
            player2Textfield.setDisable(false);
        }

    }

}
