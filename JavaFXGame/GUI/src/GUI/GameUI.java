package GUI;/**
 * Created by alex on 23/12/2016.
 */

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;


public class GameUI extends Application {

    public static Stage m_primaryStage;

    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void start(Stage primaryStage) throws IOException {
        m_primaryStage = primaryStage;
        primaryStage.setTitle("Numberiada Ex2");
        FXMLLoader loader = new FXMLLoader();
        URL url = getClass().getResource("/resources/WelcomeScreen.fxml");
        Parent root = loader.load(url.openStream());
        Scene scene = new Scene (root, 600, 400);
        m_primaryStage.setScene(scene);
        scene.getStylesheets().add(this.getClass().getResource("/resources/WelcomeScreenStyle.css").toExternalForm());
        m_primaryStage.show();

    }
}
