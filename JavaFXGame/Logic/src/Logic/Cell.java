package Logic;

import java.awt.*;
import java.awt.Color;

/**
 * Created by alex on 21/12/2016.
 */
public class Cell {

    private String m_CharHolding;
    //private Cell.Color color;
    private int m_NumOfRow;
    private int m_NumOfCol;
    private javafx.scene.paint.Color myColor;
    boolean isQuit; // ADD

    public Cell(int i_NumOfRow, int i_NumOfCol, int i_Color)
    {
        m_NumOfRow = i_NumOfRow;
        m_NumOfCol = i_NumOfCol;
        myColor = getStatus(i_Color);
        m_CharHolding = " ";
        isQuit = false;
    }

    // ADD
    public Cell(int i_NumOfRow, int i_NumOfCol, javafx.scene.paint.Color i_Color, String i_CharHolding) {
        m_NumOfRow = i_NumOfRow;
        m_NumOfCol = i_NumOfCol;
        myColor = i_Color;
        m_CharHolding = i_CharHolding;
        isQuit = false;
    }

    public static javafx.scene.paint.Color getStatus(int color) {
        switch (color) {
            case 1:
                return javafx.scene.paint.Color.BLUE;
            case 2:
                return javafx.scene.paint.Color.RED;
            case 3:
                return javafx.scene.paint.Color.GREEN;
            case 4:
                return javafx.scene.paint.Color.WHITE;
            case 5:
                return javafx.scene.paint.Color.BLACK;
            case 6:
                return javafx.scene.paint.Color.PURPLE;
            default:

                return null;
        }
    }

    public javafx.scene.paint.Color getActualColor() {return myColor;}

    public javafx.scene.paint.Color getColor() {
        return myColor;
    }

    public int GetNumOfRow () {
        return m_NumOfRow;
    }

    public void SetNumOfRow (int i_NumOfRow) {
        m_NumOfRow = i_NumOfRow;
    }

    public int GetNumOfCol(){
        return m_NumOfCol;
    }

    public void SetNumOfCol(int i_NumOfCol){
        m_NumOfCol = i_NumOfCol;
    }

    public String GetNumInCell() {
        return m_CharHolding;
    }

    public void SetNumInCell(String Num) {
        m_CharHolding = Num;
    }

    public void SetColor(int i_Color){
        myColor = getStatus(i_Color);
    }

    public void SetColor(javafx.scene.paint.Color i_Color){
        myColor=i_Color;
    }

    // ADD
    public void SetQuit() {
        isQuit = true;
    }
    // ADD
    public boolean GetIsQuit() {
        return isQuit;
    }


    public enum Color {
        BLUE(1),
        RED(2),
        GREEN(3),
        WHITE(4),
        BLACK(5),
        PURPLE(6);

        private final int id;

        Color(int id) {
            this.id = id;
        }

        public int getValue() {
            return id;
        }


        public  Color getStatus(int color) {
            switch (color) {
                case 1:
                    return BLUE;
                case 2:
                    return RED;
                case 3:
                    return GREEN;
                case 4:
                    return BLACK;
                case 5:
                    return WHITE;
                case 6:
                    return PURPLE;
                default:
                    return null;
            }
        }
    }
}
