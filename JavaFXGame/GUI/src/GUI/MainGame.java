package GUI;

import Logic.*;
import Logic.Cell;
import javafx.animation.Animation;
import javafx.animation.PathTransition;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.Effect;
import javafx.scene.effect.Glow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import java.awt.*;
import java.awt.event.ActionEvent;

import java.io.File;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.URL;
import java.util.*;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Handler;

/**
 * Created by alex on 24/12/2016.
 */
public class MainGame{

    @FXML public static ListView playersListview;
    @FXML private ListView playerList;
    @FXML public static ScrollPane m_theGameRoot;
    @FXML private ScrollPane boardScroller;
    @FXML private static ScrollPane boardScrollerSaver;
    @FXML public static Scene BlueSkin;
    @FXML public static Scene RedSkin;
    @FXML public static Pane m_mainScreen;
    @FXML private Pane paramsPane;
    @FXML private ChoiceBox skinChoicebox;
    @FXML private Button prevButton;
    @FXML private Button nextButton;
    @FXML private Button startGameButton;
    @FXML private Button loadGameButton;
    @FXML private Button restartButton;



    @FXML Label moves1Label;
    @FXML Label player1Label;
    @FXML Label id1Label;
    @FXML Label color1Label;
    @FXML Label score1Label;
    @FXML Label type1Label;
    @FXML Label compLabel;
    @FXML ProgressBar progressBar;
    @FXML Label progressPercentLabel;
    @FXML Label compMakingMoveLabel;


    @FXML ListView winnersListview;
    @FXML private Label finalScoresLabel;

    @FXML private CheckBox animationCheckbox;
    public static CellButton lastCompButton;

    @FXML private Button performButton;
    @FXML private Button quitGameButton;

    private SimpleIntegerProperty moves1;
    private SimpleStringProperty player1;
    private SimpleIntegerProperty id1;
    private SimpleStringProperty color1;
    private SimpleIntegerProperty score1;


    public static Stage m_MainGameStage;
    public static Scene m_MainScene;
    public static SimpleBooleanProperty gameStarted;
    public static CellButton [][] boardButtons;
    private static CellButton currentSelectedButton = new CellButton();
    public static SimpleIntegerProperty currentPlayerInd;
    public static BasicGamePlayers m_PlayerChoose;
    private int numOfPlayers = 0;
    public static Player currentPlayer;
    private Player nextPlayer;
    private int ColXButton = 0;
    private int RowXButton = 0;
    private boolean gameFinished = false;
    private int numOfMoves = 0;

    private boolean isBasicPlayerQuit = false;

    public static int numOfRows = 0;
    public static int numOfCols = 0;

    private int currentMove = 0;
    private int currentXMove =0;

    public MainGame() { };

    public void setController() throws IOException {

        FXMLLoader loader = new FXMLLoader();
        URL url = getClass().getResource("/resources/MainGameScreen.fxml");
        loader.setLocation(url);
        Parent root = loader.load(url.openStream());
        m_theGameRoot = (ScrollPane) root;
        Scene scene = new Scene(root, 1090, 620);
        //m_MainScene.getStylesheets().add(this.getClass().getResource("/resources/PinkStyle.css").toExternalForm());
        //m_MainScene.getStylesheets().add(this.getClass().getResource("/resources/ButtonStyle.css").toExternalForm());
        scene.getStylesheets().add(this.getClass().getResource("/resources/BoardButtonStyle.css").toExternalForm());
        m_MainGameStage = new Stage();
        m_MainGameStage.setTitle("Numberiada");
        m_MainGameStage.centerOnScreen();
        m_MainGameStage.setScene(scene);
        m_MainGameStage.initModality(Modality.WINDOW_MODAL);
        GameUI.m_primaryStage.hide();

        m_MainGameStage.show();
        m_mainScreen = (Pane) m_theGameRoot.getContent();
        m_MainScene = scene;
        gameStarted = new SimpleBooleanProperty(false);

        //setProperties();
        saveControllers();
        printBoard();
        printPlayers();
    }

    @FXML
    public void initialize() {
        enableSkins();

    }

    private void saveControllers(){

        for (Node n : m_mainScreen.getChildren()){

            if(n.getId().equals("boardScroller")){
                boardScrollerSaver = (ScrollPane) n;
            }
            else if(n.getId().equals("playerList")){
                playersListview = (ListView) n;
            }
        }
    }

    private void setProperties(){
        moves1 = new SimpleIntegerProperty(0);
        player1 = new SimpleStringProperty();
        id1 = new SimpleIntegerProperty(0);
        color1 = new SimpleStringProperty();
        score1 = new SimpleIntegerProperty(0);

    }

    private void printPlayers() {
        List<Player> playersFromBoard = GameLoader.myGame.GetPlayersList();
        playersListview.setEditable(true);
        int i=1;
        for (Player p: playersFromBoard){
            StringBuilder sb = new StringBuilder();
            playersListview.getItems().add(sb.append(String.format("%d. ",i)).append(p.GetPlayerName()).append(", ").append(p.getColorName()).append(", Score: ").append(p.getScore()));
            i++;
        }
    }

    private void printBoard() {
        Pane boardPane = new Pane();
        boardScrollerSaver.setContent(boardPane);
        numOfRows = GameLoader.myGame.GetBoard().NumOfRows();
        numOfCols = GameLoader.myGame.GetBoard().NumOfCols();
        boardPane.setPrefWidth((numOfCols+1)*35 + numOfCols*5);
        boardPane.setPrefHeight((numOfRows+1)*35 + numOfRows*5);
        drawButtons(numOfRows,numOfCols,boardPane, false);
    }

    private void drawButtons( int numOfRows, int numOfCols,Pane boardPane, boolean allowToClick) {
        boardPane.getChildren().clear();
        Cell[][] arrayOfCells = GameLoader.myGame.GetBoard().getCellBoard();
        boardButtons = new CellButton[numOfRows][numOfCols];
        /*for (int i = 0; i < numOfCols; i++){
            for (int j = 0; j < numOfRows; j++){
                boardButtons[i][j] = new CellButton();
            }
        }*/
        double x = boardPane.getLayoutX();
        double y =  boardPane.getLayoutY();
        double heightSaver = 0;
        for (int i = 0; i < numOfCols; i++) {
            Label colLabel = new Label();
            colLabel.setPrefHeight(35);
            colLabel.setPrefWidth(35);
            colLabel.setLayoutX(x + (i+1)*37);
            colLabel.setLayoutY(y);
            colLabel.setText(String.valueOf(i+1));
            colLabel.setStyle("-fx-font-weight: bold");
            colLabel.setAlignment(Pos.CENTER);
            //colLabel.setTextAlignment(TextAlignment.CENTER);
            boardPane.getChildren().add(colLabel);
            heightSaver = colLabel.getPrefHeight()+2;

        }
        y += heightSaver;
        for (int i = 0; i < numOfRows; i++) {
            Label rowLabel = new Label();
            rowLabel.setPrefHeight(35);
            rowLabel.setPrefWidth(35);
            rowLabel.setLayoutX(x);
            rowLabel.setLayoutY(y);
            rowLabel.setText(String.valueOf(i+1));
            rowLabel.setStyle("-fx-font-weight: bold");
            rowLabel.setAlignment(Pos.CENTER);
            boardPane.getChildren().add(rowLabel);
            x += rowLabel.getPrefWidth()+2;
            for (int j = 0; j < numOfCols; j++) {
                CellButton btn = new CellButton();
                btn.disableProperty().bind(gameStarted.not());
                btn.setLayoutX(x);
                btn.setLayoutY(y);
                btn.setPrefWidth(35);
                btn.setPrefHeight(35);
                if(arrayOfCells[i][j].GetNumInCell().equals("X")){
                    RowXButton = i;
                    ColXButton = j;
                }
                btn.setText(arrayOfCells[i][j].GetNumInCell());
                btn.setTextFill(arrayOfCells[i][j].getColor());
                btn.setId("boardButton");
                btn.setRow(i);
                btn.setColumn(j);
                boardButtons[i][j] = btn;
                btn.setOnAction((e) ->  buttonToggle(btn));

                if (arrayOfCells[i][j].GetNumInCell().equals(" ")) {
                    //btn.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
                    //btn.setDisable(true);
                }
                boardPane.getChildren().add(btn);
                x += 37;
            }
            x = boardPane.getLayoutX();
            y += rowLabel.getPrefHeight()+2;
        }
    }

    private void buttonToggle(CellButton button){
        if (button.getIsSelected().getValue()){
             if(button.equals(currentSelectedButton)){
                 currentSelectedButton = new CellButton();
             }

            button.setId("boardButton");
            button.getIsSelected().setValue(false);
        }
        else {
            if(!button.equals(currentSelectedButton)) {
                currentSelectedButton.setId("boardButton");
                currentSelectedButton.getIsSelected().setValue(false);
                currentSelectedButton = button;
                currentSelectedButton.setId("boardButtonSelected");
                currentSelectedButton.getIsSelected().setValue(true);
            }
            else {
                currentSelectedButton.getIsSelected().setValue(true);
                currentSelectedButton.setId("boardButtonSelected");
            }
        }
    }

    private void enableSkins(){

        skinChoicebox.getItems().add("Default Skin");
        skinChoicebox.getItems().add("Blue Skin");
        skinChoicebox.getItems().add("Red Skin");
        skinChoicebox.setValue("Default Skin");
        skinChoicebox.setDisable(false);

        skinChoicebox.getSelectionModel().selectedItemProperty().addListener( (v, oldValue, newValue) -> {

            if (newValue.equals("Blue Skin")){
                m_MainScene.getStylesheets().clear();
                m_MainScene.getStylesheets().add(this.getClass().getResource("/resources/BoardButtonStyle.css").toExternalForm());
                m_MainScene.getStylesheets().add(this.getClass().getResource("/resources/GameLabelStyle.css").toExternalForm());
                m_MainScene.getStylesheets().add(this.getClass().getResource("/resources/BlueStyle.css").toExternalForm());
            }
            else if (newValue.equals("Red Skin")){
                m_MainScene.getStylesheets().clear();
                m_MainScene.getStylesheets().add(this.getClass().getResource("/resources/BoardButtonStyle.css").toExternalForm());
                m_MainScene.getStylesheets().add(this.getClass().getResource("/resources/GameLabelStyle.css").toExternalForm());
                m_MainScene.getStylesheets().add(this.getClass().getResource("/resources/RedStyle.css").toExternalForm());
            }
            else {
                m_MainScene.getStylesheets().clear();
                m_MainScene.getStylesheets().add(this.getClass().getResource("/resources/BoardButtonStyle.css").toExternalForm());
            }
        });
    }

    private void enableButtons() {

        for (Node n : paramsPane.getChildren()) {
            if (n.isDisabled()) {
                n.setDisable(false);
            }
        }
    }

    private void bindLabelProperties(){
        moves1Label.textProperty().bind(Bindings.format("%d", moves1));
        player1Label.textProperty().bind(Bindings.format("%s", player1));
        id1Label.textProperty().bind(Bindings.format("%d", id1));
        color1Label.textProperty().bind(color1);
        score1Label.textProperty().bind(Bindings.format("%d", score1));
    }

    private void openFileError(String i_Message){

        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Error opening XML Game File");
        alert.setContentText(i_Message);
        alert.showAndWait();
    }


    @FXML
    private void loadNewGame() throws IOException, InterruptedException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        File xmlFile = fileChooser.showOpenDialog(m_MainGameStage);

        if(xmlFile != null){

            try {
                GameLoader.myGame.ClearPlayers();
                GameLoader.myGame.LoadXML(xmlFile.getPath());
                GameLoader.myGame.InitFromGeneratedGame();
                playerList.getItems().clear();
                GameLoader.myGame.getMovesList().clear();
                GameLoader.myGame.getMarkerList().clear();
                updateCurrentPlayerLabelsAfterLoadNewGame();
                winnersListview.getItems().clear();
                winnersListview.setVisible(false);
                finalScoresLabel.setVisible(false);
                if (GameLoader.myGame.isBasicGame()){
                    m_PlayerChoose = new BasicGamePlayers();
                    m_PlayerChoose.setController();
                }

                gameStarted.set(false);
                GameLoader.myGame.ResetMoves();
                currentPlayerInd = new SimpleIntegerProperty(0);
                currentPlayer = GameLoader.myGame.GetPlayersList().get(currentPlayerInd.getValue());
                //saveControllers();
                printBoard();
                printPlayers();
                updatePlayerListView();
                updateCurrentPlayerLabels();
                prevButton.setVisible(false);
                nextButton.setVisible(false);
                startGameButton.setVisible(true);
                restartButton.setVisible(false);
                startGameButton.setDisable(false);
                numOfMoves = 0;
                isBasicPlayerQuit = false;
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

    @FXML
    private void startGame() {
        //bindLabelProperties();
        gameStarted.set(true);
        startGameButton.setDisable(true);
        loadGameButton.setDisable(true);

        currentPlayerInd = new SimpleIntegerProperty(0);
        currentPlayer = GameLoader.myGame.GetPlayersList().get(currentPlayerInd.getValue());

        progressPercentLabel.setText("");
        progressBar.setProgress(0);
        compMakingMoveLabel.setText("");
        compLabel.setText("");
        numOfPlayers = GameLoader.myGame.GetPlayersList().size();

        PlayerCanMoveChecker();
    }

    @FXML
    private void restartGame(){
        GameLoader.myGame.clearPlayersScore();
        playerList.getItems().clear();
        try {
            GameLoader.myGame.InitFromGeneratedGame();
        }
        catch (XmlValueException exc5) {
            openFileError(exc5.GetMessage());
        }
        GameLoader.myGame.getMarkerList().clear();
        GameLoader.myGame.getMovesList().clear();
        updateCurrentPlayerLabelsAfterLoadNewGame();
        //gameStarted.set(false);
        GameLoader.myGame.ResetMoves();
        currentPlayerInd = new SimpleIntegerProperty(0);
        currentPlayer = GameLoader.myGame.GetPlayersList().get(currentPlayerInd.getValue());
        //saveControllers();
        printBoard();
        printPlayers();
        updatePlayerListView();
        updateCurrentPlayerLabels();
        enableButtons();
        prevButton.setVisible(false);
        nextButton.setVisible(false);
        startGameButton.setDisable(false);
        winnersListview.getItems().clear();
        winnersListview.setVisible(false);
        finalScoresLabel.setVisible(false);
        restartButton.setDisable(true);
        loadGameButton.setDisable(true);
        isBasicPlayerQuit = false;

        gameStarted.set(true);
        progressPercentLabel.setText("");
        progressBar.setProgress(0);
        compMakingMoveLabel.setText("");
        //int numOfRows = GameLoader.myGame.GetBoard().NumOfRows();
        //int numOfCols = GameLoader.myGame.GetBoard().NumOfCols();
        numOfPlayers = GameLoader.myGame.GetPlayersList().size();
        numOfMoves = 0;
        PlayerCanMoveChecker();

    }

    private void PlayerCanMoveChecker(){

        if(GameLoader.myGame.isBasicGame()){

            gameFinished = GameLoader.myGame.GetBoard().BasicCheckColRow(currentPlayerInd.getValue());
        }
        else {
            gameFinished = GameLoader.myGame.GetBoard().AdvancedCheckColRow();

        }

        if(gameFinished || isBasicPlayerQuit){

            gameFinished = true;
            winnersListview.getItems().clear();

            loadGameButton.setDisable(false);
            startGameButton.setVisible(false);
            restartButton.setVisible(true);
            restartButton.setDisable(false);

            nextButton.setVisible(true);
            prevButton.setVisible(true);
            if(numOfMoves > 0){
                prevButton.setDisable(false);
            }
            nextButton.setDisable(true);
            currentXMove = GameLoader.myGame.getMarkerList().size();
            currentMove = GameLoader.myGame.getMovesList().size();
            if(GameLoader.myGame.isBasicGame()){
                if(isBasicPlayerQuit){
                    currentPlayer = GameLoader.myGame.GetPlayersList().get((currentPlayerInd.getValue() + 1) % 2);
                    GameMessage("Congratulations " + currentPlayer.GetPlayerName() + ", You have Technically Won!");
                    currentPlayer = GameLoader.myGame.GetPlayersList().get((currentPlayerInd.getValue() + 1) % 2);
                }
                else {
                    GameMessage("Congratulations You have finished the Game!");
                }
            }
            else {
                GameMessage("Congratulations You have finished the Game!");
            }
            updateCurrentPlayerLabels();
            updateWinners();
        }
        else {
            updateCurrentPlayerLabels();


            while (!GameLoader.myGame.GetBoard().CanMakeMove(currentPlayerInd.getValue())) {

                if(!currentPlayer.getIsQuit()){
                    GameMessage("No available moves for you " + currentPlayer.GetPlayerName() + ", wait for your next turn!");
                }
                currentPlayerInd.set((currentPlayerInd.getValue() + 1) % numOfPlayers);
                currentPlayer = GameLoader.myGame.GetPlayersList().get(currentPlayerInd.getValue());
                updateCurrentPlayerLabels();
            }

            if (currentPlayer.GetIsComputer()) {
                performButton.setDisable(true);
                quitGameButton.setDisable(true);
                if(GameLoader.myGame.isBasicGame()) {
                    compBasicMoveMaker();
                }
                else {
                    compAdvancedMoveMaker();
                }
            }
            else {
                performButton.setDisable(false);
                quitGameButton.setDisable(false);
                updatePlayerListView();
                updateCurrentPlayerLabels();
            }
        }
    }

    private void updateWinners(){

        finalScoresLabel.setVisible(true);
        winnersListview.setVisible(true);
        playersListview.setEditable(true);
        List<Player> winners  = new ArrayList<Player>();
        for (Player p : GameLoader.myGame.GetPlayersList()){
            Player player = new Player();
            try {
                player = p.clone();
                winners.add(player);
            }
            catch (CloneNotSupportedException e){
                e.printStackTrace();
            }
        }

        Collections.sort(winners, Player.scoreComparator );

        for (Player p : winners){
            StringBuilder sb = new StringBuilder();
            winnersListview.getItems().add(sb.append(p.getScore()).append(" - ").append(p.GetPlayerName()));
        }
    }

    private void compAdvancedMoveMaker() {

        Task compMovesTask = new Task<Boolean>() {
                @Override
                    protected Boolean call() throws Exception {

                    while (currentPlayer.GetIsComputer()){
                        final int max = 100;

                        Thread.sleep(100);
                        Platform.runLater(() -> updatePlayerListView());
                        Thread.sleep(100);
                        Platform.runLater(() -> updateCurrentPlayerLabels());
                        Thread.sleep(100);

                        if(GameLoader.myGame.isBasicGame()){

                            gameFinished = GameLoader.myGame.GetBoard().BasicCheckColRow(currentPlayerInd.getValue());
                        }
                        else {
                            gameFinished = GameLoader.myGame.GetBoard().AdvancedCheckColRow();

                        }

                        if(gameFinished){
                            updateMessage("Done!");
                            return gameFinished;

                        }
                        else {
                            if(!GameLoader.myGame.GetBoard().CanMakeMove(currentPlayerInd.getValue())) {
                                Platform.runLater(() ->{
                                    compLabel.setText("No available moves for you " + currentPlayer.GetPlayerName() + ", wait for your next turn!");
                                });
                                Thread.sleep(2000);
                                Platform.runLater(() -> compLabel.setText(""));

                                currentPlayerInd.set((currentPlayerInd.getValue()+1)%numOfPlayers);
                                currentPlayer = GameLoader.myGame.GetPlayersList().get(currentPlayerInd.getValue());
                            }
                            else {

                                Thread.sleep(100);
                                updateMessage("Computer Player is making a move...");
                                updateProgress(0, max);
                                Thread.sleep(100);

                                for (int i = 1; i <= max; i++) {

                                    updateProgress(i, max);
                                    try {
                                        Thread.sleep(20);

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }

                                List<Integer> rowCol = GameLoader.myGame.GetBoard().compMoveAdvancedGame(currentPlayerInd.getValue());
                                int row = rowCol.get(0);
                                int col = rowCol.get(1);

                                Cell marker = new Cell(GameLoader.myGame.GetBoard().getRowX()-1,GameLoader.myGame.GetBoard().getColX()-1,Color.ORANGE,"X");
                                GameLoader.myGame.addMarkerToList(marker);
                                Cell temp = new Cell(row,col,GameLoader.myGame.GetBoard().getCellBoard()[row][col].getActualColor(),GameLoader.myGame.GetBoard().getCellBoard()[row][col].GetNumInCell());
                                GameLoader.myGame.addMoveToList(temp);


                                Thread.sleep(150);
                                Platform.runLater(() -> {
                                    boardButtons[GameLoader.myGame.GetBoard().getRowX() - 1][GameLoader.myGame.GetBoard().getColX() - 1].setText(" ");
                                });
                                Thread.sleep(150);
                                GameLoader.myGame.GetBoard().eraseX();
                                GameLoader.myGame.GetPlayerX(currentPlayerInd.getValue()).AddPoints(GameLoader.myGame.GetBoard().getCellBoard()[row][col].GetNumInCell());
                                GameLoader.myGame.GetBoard().setRowX(row + 1);
                                GameLoader.myGame.GetBoard().setColX(col + 1);
                                GameLoader.myGame.GetBoard().UpdateCell(row, col);

                                lastCompButton = boardButtons[GameLoader.myGame.GetBoard().getRowX()-1][GameLoader.myGame.GetBoard().getColX()-1];
                                Platform.runLater(() -> playAnimation());
                                if(animationCheckbox.isSelected()){
                                    Thread.sleep(2000);
                                }
                                else {
                                    Thread.sleep(200);
                                }

                                Platform.runLater(() -> {
                                    boardButtons[GameLoader.myGame.GetBoard().getRowX() - 1][GameLoader.myGame.GetBoard().getColX() - 1].setText("X");
                                    boardButtons[GameLoader.myGame.GetBoard().getRowX() - 1][GameLoader.myGame.GetBoard().getColX() - 1].setTextFill(Color.ORANGE);
                                });

                                //Thread.currentThread().sleep(150);
                                //Platform.runLater(() -> boardButtons[GameLoader.myGame.GetBoard().getRowX() - 1][GameLoader.myGame.GetBoard().getColX() - 1].setTextFill(Color.ORANGE));

                                Thread.currentThread().sleep(150);
                                Platform.runLater(() -> {
                                    updatePlayerListView();
                                });

                                GameLoader.myGame.AddMove();
                                Thread.sleep(200);
                                currentPlayerInd.set((currentPlayerInd.getValue() + 1) % numOfPlayers);
                                currentPlayer = GameLoader.myGame.GetPlayersList().get(currentPlayerInd.getValue());
                                numOfMoves = GameLoader.myGame.getNumOfMoves();
                                updateMessage("Done!");
                                updateProgress(0, max);
                                Thread.sleep(700);

                            }
                        }
                    }

                    return Boolean.TRUE;
                }
            @Override protected void succeeded() {
                super.succeeded();

                clearCompMoveLabels();

                performButton.setDisable(false);
                quitGameButton.setDisable(false);
                updatePlayerListView();
                updateCurrentPlayerLabels();
                PlayerCanMoveChecker();

            }
    };

        compMakingMoveLabel.textProperty().bind(compMovesTask.messageProperty());
        progressBar.progressProperty().bind(compMovesTask.progressProperty());
        progressPercentLabel.textProperty().bind(
                Bindings.concat(
                        Bindings.format(
                                "%.0f",
                                Bindings.multiply(
                                        compMovesTask.progressProperty(),
                                        100)),
                        " %"));


        //bindLabelProperties();
        Thread thread = new Thread(compMovesTask);
        //thread.setDaemon(true);
        thread.start();


    }

    private void compBasicMoveMaker() {
        Task compMovesBasicTask = new Task<Boolean>() {
            @Override
            protected Boolean call() throws Exception {

                while (currentPlayer.GetIsComputer()){
                    final int max = 100;

                    Thread.sleep(100);
                    Platform.runLater(() -> updatePlayerListView());
                    Thread.sleep(100);
                    Platform.runLater(() -> updateCurrentPlayerLabels());
                    Thread.sleep(100);

                    if(GameLoader.myGame.isBasicGame()){
                        gameFinished = GameLoader.myGame.GetBoard().BasicCheckColRow(currentPlayerInd.getValue());
                    }

                    if(gameFinished){
                        updateMessage("Done!");
                        return gameFinished;

                    }
                    else {
                        if(GameLoader.myGame.GetBoard().BasicCheckColRow(currentPlayerInd.getValue())) {

                            Platform.runLater(() -> compLabel.setText("No available moves for you " + currentPlayer.GetPlayerName() + ", wait for your next turn!"));
                            Thread.sleep(2000);
                            Platform.runLater(() -> compLabel.setText(""));
                            Thread.sleep(100);
                            currentPlayerInd.set((currentPlayerInd.getValue()+1)%numOfPlayers);
                            currentPlayer = GameLoader.myGame.GetPlayersList().get(currentPlayerInd.getValue());
                        }
                        else {

                            updateMessage("Computer Player is making a move...");
                            updateProgress(0, max);
                            Thread.sleep(100);
                            /*Platform.runLater(() -> {
                                updatePlayerListView();
                                updateCurrentPlayerLabels();
                            });*/
                            //Thread.sleep(100);

                            for (int i = 1; i <= max; i++) {

                                updateProgress(i, max);
                                try {
                                    Thread.sleep(20);

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }


                            int selection = GameLoader.myGame.GetBoard().compMoveBasicGame(currentPlayerInd.getValue());
                            Cell temp = new Cell(GameLoader.myGame.GetBoard().getRowX()-1,GameLoader.myGame.GetBoard().getColX()-1,Color.ORANGE,"X");
                            //temp.SetQuit();
                            GameLoader.myGame.addMarkerToList(temp);

                            Thread.sleep(150);
                            Platform.runLater(() -> {
                                boardButtons[GameLoader.myGame.GetBoard().getRowX()-1][GameLoader.myGame.GetBoard().getColX()-1].setText(" ");
                            });
                            Thread.sleep(150);
                            GameLoader.myGame.GetBoard().eraseX();

                            Cell move;

                            if (currentPlayerInd.getValue() == 0) {
                                GameLoader.myGame.GetPlayerX(currentPlayerInd.getValue()).AddPoints(GameLoader.myGame.GetBoard().getCellBoard()[GameLoader.myGame.GetBoard().getRowX() - 1][selection - 1].GetNumInCell());
                                move = new Cell(GameLoader.myGame.GetBoard().getRowX() - 1,selection - 1,GameLoader.myGame.GetBoard().getCellBoard()[GameLoader.myGame.GetBoard().getRowX() - 1][selection -1].getColor(),GameLoader.myGame.GetBoard().getCellBoard()[GameLoader.myGame.GetBoard().getRowX() - 1][selection -1].GetNumInCell());
                                //move.SetQuit();
                                GameLoader.myGame.addMoveToList(move);
                                GameLoader.myGame.GetBoard().setColX(selection);
                                GameLoader.myGame.GetBoard().UpdateCell(GameLoader.myGame.GetBoard().getRowX() - 1, selection - 1);


                            }
                            else if (currentPlayerInd.getValue() == 1){
                                GameLoader.myGame.GetPlayerX(currentPlayerInd.getValue()).AddPoints(GameLoader.myGame.GetBoard().getCellBoard()[selection - 1][GameLoader.myGame.GetBoard().getColX() - 1].GetNumInCell());
                                move = new Cell(selection - 1,GameLoader.myGame.GetBoard().getColX() - 1,GameLoader.myGame.GetBoard().getCellBoard()[selection -1][GameLoader.myGame.GetBoard().getColX() - 1].getColor(),GameLoader.myGame.GetBoard().getCellBoard()[selection -1][GameLoader.myGame.GetBoard().getColX() - 1].GetNumInCell());
                                //move.SetQuit();
                                GameLoader.myGame.addMoveToList(move);
                                GameLoader.myGame.GetBoard().setRowX(selection);
                                GameLoader.myGame.GetBoard().UpdateCell(selection - 1, GameLoader.myGame.GetBoard().getColX() - 1);
                            }


                            lastCompButton = boardButtons[GameLoader.myGame.GetBoard().getRowX()-1][GameLoader.myGame.GetBoard().getColX()-1];
                            Platform.runLater(() -> playAnimation());
                            if(animationCheckbox.isSelected()){
                                Thread.sleep(2000);
                            }
                            else {
                                Thread.sleep(200);

                            }

                            Platform.runLater(() -> {
                                boardButtons[GameLoader.myGame.GetBoard().getRowX()-1][GameLoader.myGame.GetBoard().getColX()-1].setText("X");
                                boardButtons[GameLoader.myGame.GetBoard().getRowX()-1][GameLoader.myGame.GetBoard().getColX()-1].setTextFill(Color.ORANGE);
                            });

                            //Thread.sleep(100);
                            //Platform.runLater(() -> {
                                //boardButtons[GameLoader.myGame.GetBoard().getRowX()-1][GameLoader.myGame.GetBoard().getColX()-1].setTextFill(Color.ORANGE);
                           // });

                            Thread.sleep(150);
                            Platform.runLater(() -> {
                                updatePlayerListView();
                            });

                            Thread.sleep(200);
                            currentPlayerInd.set((currentPlayerInd.getValue() + 1) % numOfPlayers);
                            currentPlayer = GameLoader.myGame.GetPlayersList().get(currentPlayerInd.getValue());
                            GameLoader.myGame.AddMove();

                            numOfMoves = GameLoader.myGame.getNumOfMoves();
                            updateMessage("Done!");
                            Thread.sleep(50);
                            updateProgress(0, max);
                            Thread.sleep(700);
                        }
                    }
                }

                return Boolean.TRUE;
            }
            @Override protected void succeeded() {
                super.succeeded();

                //updateMessage("Done!");
                /*try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }*/
                clearCompMoveLabels();
                performButton.setDisable(false);
                quitGameButton.setDisable(false);
                updatePlayerListView();
                updateCurrentPlayerLabels();
                PlayerCanMoveChecker();

            }
        };

        compMakingMoveLabel.textProperty().bind(compMovesBasicTask.messageProperty());
        progressBar.progressProperty().bind(compMovesBasicTask.progressProperty());
        progressPercentLabel.textProperty().bind(
                Bindings.concat(
                        Bindings.format(
                                "%.0f",
                                Bindings.multiply(
                                        compMovesBasicTask.progressProperty(),
                                        100)),
                        " %"));


        //bindLabelProperties();
        Thread thread = new Thread(compMovesBasicTask);
        //thread.setDaemon(true);
        thread.start();


    }

    private void GameMessage(String i_Message){

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Alert");
        alert.setHeaderText("Game Information");
        alert.setContentText(i_Message);
        alert.showAndWait();
    }

    private void updateCurrentPlayerLabels(){
        moves1Label.setText(String.valueOf(GameLoader.myGame.getNumOfMoves()));
        player1Label.setText(currentPlayer.GetPlayerName());
        id1Label.setText(String.valueOf(currentPlayer.getID()));
        color1Label.setText(currentPlayer.getColorName());
        color1Label.setTextFill(currentPlayer.getActualColor());
        score1Label.setText(String.valueOf(currentPlayer.GetNumOfPoints()));
        if(currentPlayer.GetIsComputer()){
            type1Label.setText("Computer");
        }
        else {
            type1Label.setText("Human");
        }

    }

    private void updateCurrentPlayerLabelsAfterLoadNewGame(){
        moves1Label.setText("");
        player1Label.setText("");
        id1Label.setText("");
        color1Label.setText("");
        color1Label.setTextFill(Color.GOLD);
        score1Label.setText("");
        type1Label.setText("");
    }


    public void clearCompMoveLabels() {
        compMakingMoveLabel.textProperty().unbind();
        progressPercentLabel.textProperty().unbind();
        progressBar.progressProperty().unbind();
        compMakingMoveLabel.setText("");
        progressPercentLabel.setText("");
        progressBar.setProgress(0);
    }


    private void performBasicComputerMove() {

        int selection = GameLoader.myGame.GetBoard().compMoveBasicGame(currentPlayerInd.getValue());
        Cell temp = new Cell(GameLoader.myGame.GetBoard().getRowX()-1,GameLoader.myGame.GetBoard().getColX()-1,Color.ORANGE,"X");
        //temp.SetQuit();
        GameLoader.myGame.addMarkerToList(temp);
        GameLoader.myGame.GetBoard().eraseX();
        boardButtons[GameLoader.myGame.GetBoard().getRowX()-1][GameLoader.myGame.GetBoard().getColX()-1].setText(" ");
        Cell move;

        if (currentPlayerInd.getValue() == 0) {
            GameLoader.myGame.GetPlayerX(currentPlayerInd.getValue()).AddPoints(GameLoader.myGame.GetBoard().getCellBoard()[GameLoader.myGame.GetBoard().getRowX() - 1][selection - 1].GetNumInCell());
            move = new Cell(GameLoader.myGame.GetBoard().getRowX() - 1,selection - 1,GameLoader.myGame.GetBoard().getCellBoard()[GameLoader.myGame.GetBoard().getRowX() - 1][selection -1].getColor(),GameLoader.myGame.GetBoard().getCellBoard()[GameLoader.myGame.GetBoard().getRowX() - 1][selection -1].GetNumInCell());
            //move.SetQuit();
            GameLoader.myGame.addMoveToList(move);
            GameLoader.myGame.GetBoard().setColX(selection);
            GameLoader.myGame.GetBoard().UpdateCell(GameLoader.myGame.GetBoard().getRowX() - 1, selection - 1);
        } else if (currentPlayerInd.getValue() == 1){
            GameLoader.myGame.GetPlayerX(currentPlayerInd.getValue()).AddPoints(GameLoader.myGame.GetBoard().getCellBoard()[selection - 1][GameLoader.myGame.GetBoard().getColX() - 1].GetNumInCell());
            move = new Cell(selection - 1,GameLoader.myGame.GetBoard().getColX() - 1,GameLoader.myGame.GetBoard().getCellBoard()[selection -1][GameLoader.myGame.GetBoard().getColX() - 1].getColor(),GameLoader.myGame.GetBoard().getCellBoard()[selection -1][GameLoader.myGame.GetBoard().getColX() - 1].GetNumInCell());
            //move.SetQuit();
            GameLoader.myGame.addMoveToList(move);
            GameLoader.myGame.GetBoard().setRowX(selection);
            GameLoader.myGame.GetBoard().UpdateCell(selection - 1, GameLoader.myGame.GetBoard().getColX() - 1);
        }

        boardButtons[GameLoader.myGame.GetBoard().getRowX()-1][GameLoader.myGame.GetBoard().getColX()-1].setText("X");
        boardButtons[GameLoader.myGame.GetBoard().getRowX()-1][GameLoader.myGame.GetBoard().getColX()-1].setTextFill(Color.BLACK);
        GameLoader.myGame.AddMove();
        numOfMoves = GameLoader.myGame.getNumOfMoves();
        updatePlayerListView();
    }


    private void performAdvancedComputerMove(){


        List<Integer> rowCol = GameLoader.myGame.GetBoard().compMoveAdvancedGame(currentPlayerInd.getValue());
        int row = rowCol.get(0);
        int col = rowCol.get(1);
        boardButtons[GameLoader.myGame.GetBoard().getRowX()-1][GameLoader.myGame.GetBoard().getColX()-1].setText(" ");
        GameLoader.myGame.GetBoard().eraseX();
        GameLoader.myGame.GetPlayerX(currentPlayerInd.getValue()).AddPoints(GameLoader.myGame.GetBoard().getCellBoard()[row][col].GetNumInCell());
        GameLoader.myGame.GetBoard().setRowX(row + 1);
        GameLoader.myGame.GetBoard().setColX(col + 1);
        GameLoader.myGame.GetBoard().UpdateCell(row , col);
        boardButtons[GameLoader.myGame.GetBoard().getRowX()-1][GameLoader.myGame.GetBoard().getColX()-1].setText("X");
        boardButtons[GameLoader.myGame.GetBoard().getRowX()-1][GameLoader.myGame.GetBoard().getColX()-1].setTextFill(Color.ORANGE);
        GameLoader.myGame.AddMove();
        updatePlayerListView();

        clearCompMoveLabels();
    }


    private void performHumanBasicMove() {
        Cell temp1 = new Cell(GameLoader.myGame.GetBoard().getRowX()-1,GameLoader.myGame.GetBoard().getColX()-1,Color.ORANGE,"X");
        //temp1.SetQuit();
        GameLoader.myGame.addMarkerToList(temp1);
        Cell temp2 = new Cell(currentSelectedButton.getRow(),currentSelectedButton.getColumn(),GameLoader.myGame.GetBoard().getCellBoard()[currentSelectedButton.getRow()][currentSelectedButton.getColumn()].getColor(),currentSelectedButton.getText());
        //temp2.SetQuit();
        GameLoader.myGame.addMoveToList(temp2);


        GameLoader.myGame.GetBoard().eraseX();
        boardButtons[GameLoader.myGame.GetBoard().getRowX()-1][GameLoader.myGame.GetBoard().getColX()-1].setText(" ");
        GameLoader.myGame.GetPlayerX(currentPlayerInd.getValue()).AddPoints(currentSelectedButton.getText());
        GameLoader.myGame.GetBoard().UpdateCell(currentSelectedButton.getRow(), currentSelectedButton.getColumn());
        GameLoader.myGame.GetBoard().setColX(currentSelectedButton.getColumn() + 1);
        GameLoader.myGame.GetBoard().setRowX(currentSelectedButton.getRow() + 1);
        GameLoader.myGame.AddMove();
        numOfMoves = GameLoader.myGame.getNumOfMoves();

        boardButtons[currentSelectedButton.getRow()][currentSelectedButton.getColumn()].setText("X");
        boardButtons[currentSelectedButton.getRow()][currentSelectedButton.getColumn()].setTextFill(Color.ORANGE);

        updatePlayerListView();
        currentPlayerInd.set((currentPlayerInd.getValue()+1)%numOfPlayers);
        currentSelectedButton.setSelected(false);
        currentSelectedButton.setId("boardButton");
        //checkWhosMove();
        currentPlayer = GameLoader.myGame.GetPlayersList().get(currentPlayerInd.getValue());

    }

    private void performHumanAdvancedMove() {
        GameLoader.myGame.addMarkerToList(new Cell(GameLoader.myGame.GetBoard().getRowX()-1,GameLoader.myGame.GetBoard().getColX()-1,Color.ORANGE,"X"));
        Cell temp = new Cell(GameLoader.myGame.getCellInBorad(currentSelectedButton.getRow(), currentSelectedButton.getColumn()).GetNumOfRow(),GameLoader.myGame.getCellInBorad(currentSelectedButton.getRow(), currentSelectedButton.getColumn()).GetNumOfCol(),GameLoader.myGame.getCellInBorad(currentSelectedButton.getRow(), currentSelectedButton.getColumn()).getActualColor(),GameLoader.myGame.getCellInBorad(currentSelectedButton.getRow(), currentSelectedButton.getColumn()).GetNumInCell());
        GameLoader.myGame.addMoveToList(temp);
        GameLoader.myGame.GetBoard().eraseX();
        boardButtons[GameLoader.myGame.GetBoard().getRowX()-1][GameLoader.myGame.GetBoard().getColX()-1].setText(" ");

        GameLoader.myGame.GetPlayerX(currentPlayerInd.getValue()).AddPoints(currentSelectedButton.getText());
        GameLoader.myGame.GetBoard().UpdateCell(currentSelectedButton.getRow(), currentSelectedButton.getColumn());
        GameLoader.myGame.GetBoard().setColX(currentSelectedButton.getColumn() + 1);
        GameLoader.myGame.GetBoard().setRowX(currentSelectedButton.getRow() + 1);
        GameLoader.myGame.AddMove();
        numOfMoves = GameLoader.myGame.getNumOfMoves();

        boardButtons[currentSelectedButton.getRow()][currentSelectedButton.getColumn()].setText("X");
        boardButtons[currentSelectedButton.getRow()][currentSelectedButton.getColumn()].setTextFill(Color.ORANGE);

        updatePlayerListView();
        currentPlayerInd.set((currentPlayerInd.getValue()+1)%numOfPlayers);
        currentSelectedButton.setSelected(false);
        currentSelectedButton.setId("boardButton");
        //checkWhosMove();
        currentPlayer = GameLoader.myGame.GetPlayersList().get(currentPlayerInd.getValue());
    }

    @FXML
    private void performMove() {
        if (GameLoader.myGame.isBasicGame()) {

            if (currentSelectedButton.getIsSelected().getValue()) {

                if (currentPlayerInd.getValue() == 0) {
                    if (boardButtons[currentSelectedButton.getRow()][currentSelectedButton.getColumn()].getText() != " " && boardButtons[currentSelectedButton.getRow()][currentSelectedButton.getColumn()].getText() != "X"){
                        if(GameLoader.myGame.GetBoard().getRowX() - 1 == currentSelectedButton.getRow()){
                            playAnimation();
                            performHumanBasicMove();

                        }
                        else {
                            Error("You need to choose a number in row number: "+ GameLoader.myGame.GetBoard().getRowX());
                        }
                    }
                    else {
                        Error("You need to choose a number");
                    }
                }
                else if (currentPlayerInd.getValue() == 1) {
                    if (boardButtons[currentSelectedButton.getRow()][currentSelectedButton.getColumn()].getText() != " " && boardButtons[currentSelectedButton.getRow()][currentSelectedButton.getColumn()].getText() != "X"){
                        if(GameLoader.myGame.GetBoard().getColX() - 1 == currentSelectedButton.getColumn()){
                            playAnimation();
                            performHumanBasicMove();

                        }
                        else {
                            Error("You need to choose a number in column number: "+ GameLoader.myGame.GetBoard().getColX());
                        }
                    }
                    else {
                        Error("You need to choose a number");
                    }
                }
            }
            else {
                Error("You need to select a cell first!");
            }
        }
        else {
            if (currentSelectedButton.getIsSelected().getValue()) {
                if (GameLoader.myGame.GetBoard().getColX() - 1 == currentSelectedButton.getColumn() || GameLoader.myGame.GetBoard().getRowX() - 1 == currentSelectedButton.getRow()) {
                    if (currentPlayer.getActualColor() == GameLoader.myGame.GetBoard().getCellBoard()[currentSelectedButton.getRow()][currentSelectedButton.getColumn()].getColor()) {
                        playAnimation();
                        performHumanAdvancedMove();

                    }
                    else {
                        Error("You need to choose your color");
                    }
                }
                else {
                    Error("You need to choose a number in row number: " + GameLoader.myGame.GetBoard().getRowX() + " or column number: " + GameLoader.myGame.GetBoard().getColX());
                }
            }
            else {
                Error("You need to select a cell first!");
            }
        }

        PlayerCanMoveChecker();
    }

    private void playAnimation(){
        if(animationCheckbox.isSelected()){
            Label lbl = new Label();
            Path path = new Path();
            if(currentPlayer.GetIsComputer()){

                lbl.setText(lastCompButton.getText());
                lbl.setTextFill(lastCompButton.getTextFill());
                path.getElements().add(new MoveTo(lastCompButton.getLocalToSceneTransform().getTx() + 15, lastCompButton.getLocalToSceneTransform().getTy() + 15));
                path.getElements().add(new LineTo(score1Label.getLocalToSceneTransform().getTx(), score1Label.getLocalToSceneTransform().getTy()));
            }
            else {

                lbl.setText(currentSelectedButton.getText());
                lbl.setTextFill(currentSelectedButton.getTextFill());
                path.getElements().add(new MoveTo(currentSelectedButton.getLocalToSceneTransform().getTx() + 15, currentSelectedButton.getLocalToSceneTransform().getTy() + 15));
                path.getElements().add(new LineTo(score1Label.getLocalToSceneTransform().getTx(), score1Label.getLocalToSceneTransform().getTy()));
            }

            lbl.setScaleX(2);
            lbl.setScaleY(2);
            lbl.toFront();
            m_mainScreen.getChildren().add(lbl);

            path.setFill(null);
            PathTransition pt = new PathTransition();
            pt.setDuration(Duration.millis(2000));
            pt.setPath(path);
            pt.setNode(lbl);

            pt.setCycleCount(1);
            pt.setOnFinished((e) -> lbl.setVisible(false));
            pt.play();
        }
    }


    public static void updatePlayerListView(){
        StringBuilder sb = new StringBuilder();
        if(currentPlayer.getIsQuit()){

            playersListview.getItems().set(currentPlayerInd.getValue(), sb.append(String.format("%d. ",currentPlayerInd.getValue() + 1)).append(currentPlayer.GetPlayerName()).append(", ").append(currentPlayer.getColorName()).append(", Score: ").append(currentPlayer.getScore()).append(" - Quit!"));
        }
        else{
            playersListview.getItems().set(currentPlayerInd.getValue(), sb.append(String.format("%d. ",currentPlayerInd.getValue() + 1)).append(currentPlayer.GetPlayerName()).append(", ").append(currentPlayer.getColorName()).append(", Score: ").append(currentPlayer.getScore()));
        }

    }

    private void Error (String i_Message){

        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Wrong move!");
        alert.setContentText(i_Message);
        alert.showAndWait();
    }

    @FXML
    private void quitGameAction(){

        if(!GameLoader.myGame.isBasicGame()) {
            for (int i = 0; i < numOfRows; i++) {
                for (int j = 0; j < numOfCols; j++) {
                    if (GameLoader.myGame.GetBoard().getCellBoard()[i][j].GetNumInCell() != " " && GameLoader.myGame.GetBoard().getCellBoard()[i][j].getColor() == currentPlayer.getActualColor()) {
                        Cell temp = new Cell(i, j, GameLoader.myGame.GetBoard().getCellBoard()[i][j].getActualColor(), GameLoader.myGame.GetBoard().getCellBoard()[i][j].GetNumInCell());
                        temp.SetQuit();
                        GameLoader.myGame.addMoveToList(temp);
                        GameLoader.myGame.GetBoard().getCellBoard()[i][j].SetNumInCell(" ");
                        boardButtons[i][j].setText(" ");
                    }
                }
            }

            currentPlayer.setIsQuit(true);
            updateCurrentPlayerLabels();
            updatePlayerListView();
            currentPlayerInd.set((currentPlayerInd.getValue() + 1) % numOfPlayers);
            currentPlayer = GameLoader.myGame.GetPlayersList().get(currentPlayerInd.getValue());
        }
        else {

            isBasicPlayerQuit = true;
        }
        PlayerCanMoveChecker();
    }

    @FXML
    private void nextMoveAction() {
        Cell move, marker;
        move = GameLoader.myGame.getMovesList().get(currentMove);
        marker = GameLoader.myGame.getMarkerList().get(currentXMove);
        showNextMoveOnBoard(move, marker);
        currentMove++;
        currentXMove++;
        numOfMoves = currentMove;
        prevButton.setDisable(false);
        if (currentMove >= GameLoader.myGame.getMovesList().size()) {
            nextButton.setDisable(true);
        }

        if(currentXMove >= GameLoader.myGame.getMarkerList().size()){
            nextButton.setDisable(true);
        }
        if(GameLoader.myGame.isBasicGame()){
            currentPlayerInd.set((currentPlayerInd.getValue() + 1) %2);
            currentPlayer = GameLoader.myGame.GetPlayerX(currentPlayerInd.getValue());
        }
        updateCurrentPlayerLabels();

    }
    // ADD
    @FXML
    private void prevMoveAction() {

        if(numOfMoves > 0){
            Cell move, marker;
            move = GameLoader.myGame.getMovesList().get(currentMove - 1);
            marker = GameLoader.myGame.getMarkerList().get(currentXMove - 1);
            showPrevMoveOnBoard(move, marker);
            currentMove--;
            currentXMove--;
            numOfMoves--;
            updateCurrentPlayerLabels();
            nextButton.setDisable(false);
        }

        if ((currentMove <= 0) || (numOfMoves <=0)) {
            prevButton.setDisable(true);
        }


    }
    // ADD
    private void showNextMoveOnBoard(Cell myMove, Cell myMarker){
        while (myMove.GetIsQuit()){
            currentPlayer = GameLoader.myGame.getPlayerByColor(myMove.getColor());
            currentPlayer.setIsQuit(true);
            currentPlayerInd.set(GameLoader.myGame.GetPlayersList().indexOf(currentPlayer));
            updatePlayerListView();
            boardButtons[myMove.GetNumOfRow()][myMove.GetNumOfCol()].setText(" ");
            currentMove++;
            myMove = GameLoader.myGame.getMovesList().get(currentMove);
        }
        boardButtons[myMarker.GetNumOfRow()][myMarker.GetNumOfCol()].setText(" ");
        boardButtons[myMove.GetNumOfRow()][myMove.GetNumOfCol()].setText("X");
        boardButtons[myMove.GetNumOfRow()][myMove.GetNumOfCol()].setTextFill(Color.ORANGE);
        if (GameLoader.myGame.isBasicGame()){
            //currentPlayerInd.set((currentPlayerInd.getValue() + 1) %2);
            currentPlayer = GameLoader.myGame.GetPlayerX(currentPlayerInd.getValue());
        }
        else{
            currentPlayer = GameLoader.myGame.getPlayerByColor(myMove.getColor());
        }
        currentPlayer.AddPoints(myMove.GetNumInCell());
        GameLoader.myGame.AddMove();
        currentPlayerInd.set(GameLoader.myGame.GetPlayersList().indexOf(currentPlayer));
        updatePlayerListView();
    }
    // ADD
    private void showPrevMoveOnBoard(Cell myMove, Cell myMarker){
        while (myMove.GetIsQuit()){
            currentPlayer = GameLoader.myGame.getPlayerByColor(myMove.getColor());
            currentPlayer.setIsQuit(false);
            currentPlayerInd.set(GameLoader.myGame.GetPlayersList().indexOf(currentPlayer));
            updatePlayerListView();
            boardButtons[myMove.GetNumOfRow()][myMove.GetNumOfCol()].setText(myMove.GetNumInCell());
            boardButtons[myMove.GetNumOfRow()][myMove.GetNumOfCol()].setTextFill(myMove.getActualColor());
            currentMove--;
            myMove = GameLoader.myGame.getMovesList().get(currentMove-1);
        }
        boardButtons[myMove.GetNumOfRow()][myMove.GetNumOfCol()].setText(myMove.GetNumInCell());
        boardButtons[myMove.GetNumOfRow()][myMove.GetNumOfCol()].setTextFill(myMove.getActualColor());
        boardButtons[myMarker.GetNumOfRow()][myMarker.GetNumOfCol()].setText("X");
        boardButtons[myMarker.GetNumOfRow()][myMarker.GetNumOfCol()].setTextFill(Color.ORANGE);

        if (GameLoader.myGame.isBasicGame()){
            currentPlayerInd.set((currentPlayerInd.getValue() + 1) %2);
            currentPlayer = GameLoader.myGame.GetPlayerX(currentPlayerInd.getValue());
        }
        else{
            currentPlayer = GameLoader.myGame.getPlayerByColor(myMove.getColor());
        }
        currentPlayer.subtractPoints(Integer.parseInt(myMove.GetNumInCell()));
        GameLoader.myGame.SubtractMove();
        currentPlayerInd.set(GameLoader.myGame.GetPlayersList().indexOf(currentPlayer));
        updatePlayerListView();
    }


    @FXML
    private void exitApp(){
        m_MainGameStage.close();
    }

}
