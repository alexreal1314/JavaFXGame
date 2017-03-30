package Logic;
import Generated.GameDescriptor;
import javafx.beans.property.SimpleStringProperty;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import java.util.*;

import Generated.GameDescriptor.Board.Structure.Squares.Square;
/**
 * Created by alex on 20/11/2016.
 */
public class Game {
    public final int k_PlayerOne = 0;
    public final int k_PlayerTwo = 1;
    private Board m_Board;
    private int numOfMoves = 0;
    private long startTime;
    private XMLReader xmlReader = new XMLReader();
    private GameDescriptor generatedGame;
    public static List<Player> players = new ArrayList<Player>();
    private List<Cell> movesList = new ArrayList<>(); // ADD
    private List<Cell> markerList = new ArrayList<>(); // ADD

    HashMap<Integer, String> playersID;
    HashMap<Integer, String> playersColor;

    private SimpleStringProperty numOfMovesString;
    private Boolean basicGame = false;


    public void clearPlayersScore(){

        for (Player p : players){
            p.setScore(0);
        }

    }

    public Player getPlayerByColor(javafx.scene.paint.Color i_color){
        if (i_color == players.get(0).getActualColor())
            return players.get(0);
        else if (i_color == players.get(1).getActualColor())
            return players.get(1);
        else if (i_color == players.get(2).getActualColor())
            return players.get(2);
        else if (i_color == players.get(3).getActualColor())
            return players.get(3);
        else if (i_color == players.get(4).getActualColor())
            return players.get(4);
        else if (i_color == players.get(5).getActualColor())
            return players.get(5);
        else
            return null;
    }

    public List<Player> GetPlayersList() { return players; }

    // ADD
    public void addMoveToList(Cell myMove){
        movesList.add(myMove);
    }
    // ADD
    public void addMarkerToList(Cell myMarker){
        markerList.add(myMarker);
    }
    // ADD
    public List<Cell> getMarkerList () { return markerList; }

    public Cell getCellInBorad(int row, int col) { return m_Board.getCellBoard()[row][col]; }
    // ADD
    public List<Cell> getMovesList () { return movesList; }

    public boolean isBasicGame() {
        return basicGame;
    }

    public Board GetBoard(){
        return m_Board;
    }

    public long calcTimeFromStart(){
        long currentTime = System.currentTimeMillis() - startTime;
        return currentTime / 1000;
    }

    public SimpleStringProperty AddMove(){
        numOfMoves +=1;
        numOfMovesString.setValue(String.valueOf(numOfMoves));
        return numOfMovesString;
    }

    public SimpleStringProperty SubtractMove(){
        numOfMoves -=1;
        numOfMovesString.setValue(String.valueOf(numOfMoves));
        return numOfMovesString;
    }

    public int getNumOfMoves(){
        return numOfMoves;
    }

    public Game() {
        numOfMoves = 0;
        numOfMovesString = new SimpleStringProperty();
    }

    public void ClearPlayers(){
        players.clear();
    }

    public void ResetMoves(){
        numOfMoves = 0;
    }

    public void InitTime(){
        startTime = System.currentTimeMillis();
    }

    public Player GetPlayerX(int i_NumOfPlayer){
        return players.get(i_NumOfPlayer);
    }

    public String GetNameOfPlayerX(int i_NumOfPlayer)
    {
        return players.get(i_NumOfPlayer).GetPlayerName();
    }

    public boolean GetIsPlayerComp(int i_NumOfPlayer)
    {
        return players.get(i_NumOfPlayer).GetIsComputer();
    }

    public void LoadXML(String source) throws JAXBException, SAXException, InvalidXMLException, InvalidExtension, XmlValueException {
        xmlReader.LoadXML(source);
    }

    public void LoadXML() throws JAXBException, SAXException, InvalidXMLException, InvalidExtension, XmlValueException {

    }

    public void InitFromGeneratedGame() throws XmlValueException{
        generatedGame = xmlReader.GetXMLGame();
        // CHECKS
        if(generatedGame.getGameType().equals("Basic")) {
            basicGame = true;
            basicTypeChecker();
            InitBasicBoard();
        }
        else if (generatedGame.getGameType().equals("Advance")){
            basicGame = false;
            basicTypeChecker();
            advancedTypeChecker();
            InitAdvancedBoard();
        }
        else {
            throw new XmlValueException("Invalid game type!");
        }

    }

    private void advancedTypeChecker() throws XmlValueException {

        int numOfPlayers = generatedGame.getPlayers().getPlayer().size();
        players = new ArrayList<>(numOfPlayers);
        playersID = new HashMap<>(numOfPlayers);
        String playerName;
        String playerType;
        playersColor = new HashMap<>(numOfPlayers);


        for (int i = 0; i < numOfPlayers; i++) {

            int playerId = generatedGame.getPlayers().getPlayer().get(i).getId().intValue();
            playerName = generatedGame.getPlayers().getPlayer().get(i).getName();
            playerType = generatedGame.getPlayers().getPlayer().get(i).getType();
            int playerColor = generatedGame.getPlayers().getPlayer().get(i).getColor();

            if (!playersID.isEmpty() && playersID.containsKey(playerId)) {

                players.clear();
                throw new XmlValueException("There are 2 players with the same ID value!");
            }
            else if (!playersColor.isEmpty() && playersColor.containsKey(playerColor)){
                players.clear();
                throw new XmlValueException("There are 2 players with the same Color!");
            }
            else {
                //Cell.Color color = Cell.Color.getStatus(playerColor);
                players.add(new Player(playerId, playerName, playerType, playerColor));
                playersID.put(playerId, playerName);
                playersColor.put(playerColor, playerName);
            }
        }

        if(generatedGame.getBoard().getStructure().getType().equals("Random")){
            int range = Math.abs(generatedGame.getBoard().getStructure().getRange().getTo() - generatedGame.getBoard().getStructure().getRange().getFrom());
            if (generatedGame.getBoard().getStructure().getRange().getFrom() < 0)
                range++;
            int boardSize = generatedGame.getBoard().getSize().intValue();
            if(((boardSize*boardSize-1)/range/numOfPlayers < 1)){
                players.clear();
                throw new XmlValueException("Invalid board size according to number of players and range");
            }
        }

        if (generatedGame.getPlayers().getPlayer().size() < 3 || generatedGame.getPlayers().getPlayer().size() > 6){
            throw new XmlValueException("Invalid number of players, number of players needs to be 3-6");
        }


    }

        private void basicTypeChecker() throws  XmlValueException{
        if (generatedGame.getBoard().getStructure().getType().equals("Random")) {
            if (generatedGame.getBoard().getSize().intValue() < 5 || generatedGame.getBoard().getSize().intValue() > 50) {
                throw new XmlValueException("Invalid Board Size, Required: 5 - 50");
            }
            if (generatedGame.getBoard().getStructure().getRange().getFrom() > generatedGame.getBoard().getStructure().getRange().getTo()) {
                throw new XmlValueException("Invalid Range Of Values, 'from' attribute needs to be smaller than 'to'");
            }
            int numbers = Math.abs(generatedGame.getBoard().getStructure().getRange().getTo() - generatedGame.getBoard().getStructure().getRange().getFrom());

            if (generatedGame.getBoard().getStructure().getRange().getFrom() < 0) {
                numbers++;
            }

            if (numbers > (generatedGame.getBoard().getSize().intValue() * generatedGame.getBoard().getSize().intValue()) - 1) {
                throw new XmlValueException("Invalid Board Size To Range Ratio, Required Condition: (Size^2 - 1)/|Range| >= 1");
            }
            if (generatedGame.getBoard().getStructure().getRange().getFrom() < -99 || generatedGame.getBoard().getStructure().getRange().getTo() > 99) {
                throw new XmlValueException("Invalid Range, Range needs to be [-99, 99]");
            }
        } else if (generatedGame.getBoard().getStructure().getType().equals("Explicit")) {
            if (generatedGame.getBoard().getSize().intValue() < 5 || generatedGame.getBoard().getSize().intValue() > 50) {
                throw new XmlValueException("Invalid Board Size, Required: 5 - 50");
            }


            for (Square sqr : generatedGame.getBoard().getStructure().getSquares().getSquare()) {
                if (sqr.getColumn().intValue() > generatedGame.getBoard().getSize().intValue() || sqr.getRow().intValue() > generatedGame.getBoard().getSize().intValue()) {
                    throw new XmlValueException("Invalid Square Coordinates, Each Cell's Coordinates Needs To Be Within Board Bounds");
                }

                if (sqr.getValue().intValue() < -99 || sqr.getValue().intValue() > 99) {
                    throw new XmlValueException("Invalid Value, Numbers Need To Be In Range Of [-99, 99]");
                }
            }
            for (Square sqr1 : generatedGame.getBoard().getStructure().getSquares().getSquare()) {
                for (Square sqr2 : generatedGame.getBoard().getStructure().getSquares().getSquare()) {
                    if (sqr1 != sqr2) {
                        if ((sqr1.getRow().intValue() == sqr2.getRow().intValue()) && (sqr1.getColumn().intValue() == sqr2.getColumn().intValue())) {
                            throw new XmlValueException("Duplicate Cells Found, Each Cell Must Have Unique Coordinates");
                        }
                    }
                }
            }

            if(generatedGame.getBoard().getStructure().getSquares().getMarker().getRow().intValue() > generatedGame.getBoard().getSize().intValue() || generatedGame.getBoard().getStructure().getSquares().getMarker().getColumn().intValue() > generatedGame.getBoard().getSize().intValue()){
                throw new XmlValueException("Invalid Row or Column Coordinates of the Marker!");
            }
        }

    }

    public void InitBasicBoard(){
        m_Board = new Board(generatedGame.getBoard().getSize(),generatedGame.getBoard().getSize());
        if (generatedGame.getBoard().getStructure().getType().equals("Random")) {
            m_Board.initBasicRandomBoard(generatedGame.getBoard().getStructure().getRange().getFrom(), generatedGame.getBoard().getStructure().getRange().getTo());
        }
        else if (generatedGame.getBoard().getStructure().getType().equals("Explicit")){
            m_Board.initExplicitBoard(generatedGame.getBoard().getStructure().getSquares().getSquare(), generatedGame.getBoard().getStructure().getSquares().getMarker());
        }
    }

    private void InitAdvancedBoard(){
        m_Board = new Board(generatedGame.getBoard().getSize(),generatedGame.getBoard().getSize());
        if (generatedGame.getBoard().getStructure().getType().equals("Random")) {
            m_Board.initAdvancedRandomBoard(generatedGame.getBoard().getStructure().getRange().getFrom(), generatedGame.getBoard().getStructure().getRange().getTo());
        }
        else if (generatedGame.getBoard().getStructure().getType().equals("Explicit")){
            m_Board.initAdvancedExplicitBoard(generatedGame.getBoard().getStructure().getSquares().getSquare(), generatedGame.getBoard().getStructure().getSquares().getMarker());
        }

    }

    public void InitBasicPlayers(List<String> i_basicPlayers) {

        Player player1 = new Player(1, i_basicPlayers.get(0),i_basicPlayers.get(1), 5/*Cell.Color.BLACK*/);
        Player player2 = new Player(2, i_basicPlayers.get(2),i_basicPlayers.get(3), 5/*Cell.Color.BLACK*/);
        players.add(player1);
        players.add(player2);
    }

}
