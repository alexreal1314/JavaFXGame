package Logic;

import java.math.BigInteger;
import java.util.Comparator;

/**
 * Created by alex on 19/11/2016.
 */
public class Player implements Cloneable{
    //private final String compName = "Computer";
    private String m_PlayerName;
    private int m_SumOfPoints = 0;
    private boolean m_IsPlayerComputer;
    private int m_PlayerID;
    //private Cell.Color m_Color;
    private int m_Color;
    private javafx.scene.paint.Color actualColor;
    private boolean isQuit;

    public static final Comparator<Player> scoreComparator = new MyComparator();

    public int getScore(){return m_SumOfPoints;}

    @Override
    public Player clone() throws CloneNotSupportedException {

        return (Player)super.clone();
    }


    static class MyComparator implements Comparator<Player>{

        @Override
        public int compare(Player p1, Player p2) {
            if(p1.getScore() <= p2.getScore()){
                return 1;
            }
            else {
                return -1;
            }
        }
    }

    public Player(int i_PlayerId, String i_PlayerName, String i_PlayerType, int i_Color/*Cell.Color i_Color*/) {
        m_PlayerID = i_PlayerId;
        m_PlayerName = i_PlayerName;
        String strComp = i_PlayerType.substring(0, 5);
        if (strComp.equals("Human")){
            m_IsPlayerComputer = false;
        }
        else {
            m_IsPlayerComputer = true;
        }
        m_Color = i_Color;
        actualColor = Cell.getStatus(m_Color);
        isQuit = false;

    }

    public void setScore(int score){
        m_SumOfPoints = 0;
    }

    public boolean getIsQuit(){
        return isQuit;
    }

    public void setIsQuit(boolean value ){
        isQuit = value;
    }

    public javafx.scene.paint.Color getActualColor(){return actualColor;}

    public Player() {}

    public void AddPoints(String num){
        int newNum = Integer.parseInt(num);
        m_SumOfPoints += newNum;
    }

    public void AddPoints(int num){
        m_SumOfPoints += num;
    }

    public void subtractPoints(int num){
        m_SumOfPoints = m_SumOfPoints - num;
    }

    public int getID() { return m_PlayerID;}

    public boolean GetIsComputer() {
        return m_IsPlayerComputer;
    }

    public int GetNumOfPoints(){
        return m_SumOfPoints;
    }

    public String GetPlayerName(){
        return m_PlayerName;
    }

    public int /*Cell.Color*/ GetColor(){
        return m_Color;
    }

    public String getColorName(){
        switch (m_Color) {
            case 1:
                return "Blue";
            case 2:
                return "Red";
            case 3:
                return "Green";
            case 4:
                return "White";
            case 5:
                return "Black";
            case 6:
                return "Purple";
            default:

                return null;
        }

    }


}
