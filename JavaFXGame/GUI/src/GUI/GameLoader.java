package GUI;

import Logic.Game;
import Logic.InvalidExtension;
import Logic.InvalidXMLException;
import Logic.XmlValueException;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import org.xml.sax.SAXException;
import javafx.scene.Node;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;

/**
 * Created by alex on 23/12/2016.
 */
public class GameLoader{

    public static Game myGame = new Game();
    private BorderPane m_Pane;
    public static MainGame m_MainGame;
    public static BasicGamePlayers m_PlayerChoose;

    @FXML
    private void loadGame() throws IOException, InterruptedException {

        boolean sourceLoaded = false;
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        File xmlFile = fileChooser.showOpenDialog(GameUI.m_primaryStage);
        Scene welcomeScene = GameUI.m_primaryStage.getScene();
        m_Pane = (BorderPane) welcomeScene.getRoot();
        if(xmlFile != null){

        try {
            myGame.LoadXML(xmlFile.getPath());
            myGame.InitFromGeneratedGame();
            if (myGame.isBasicGame()){
                m_PlayerChoose = new BasicGamePlayers();
                m_PlayerChoose.setController();
            }

            m_MainGame = new MainGame();
            m_MainGame.setController();
        }
        catch (JAXBException exc1) {
            openFileError("XML file not found or not matching the schema");
        }
        catch (SAXException exc2) {
            openFileError("XML schema file not found");
        }
        catch (InvalidXMLException exc3) {
            openFileError("XML file not valid");

        }
        catch (InvalidExtension exc4) {
            openFileError(exc4.GetMessage());
        }
        catch (XmlValueException exc5) {
            openFileError(exc5.GetMessage());
        }
        }
    }

    private void openFileError(String i_Message){

        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Error opening XML Game File");
        alert.setContentText(i_Message);
        alert.showAndWait();
    }

    @FXML
    private void closeGame(){
        GameUI.m_primaryStage.close();
    }

}
