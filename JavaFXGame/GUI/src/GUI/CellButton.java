package GUI;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.Button;

/**
 * Created by alex on 26/12/2016.
 */
public class CellButton extends Button{
    private SimpleBooleanProperty isSelected;
    private Integer row;
    private Integer column;

    public  CellButton(){
        isSelected = new SimpleBooleanProperty();
        isSelected.setValue(false);
    }

    public SimpleBooleanProperty getIsSelected() {
        return isSelected;
    }

    public Integer getRow(){ return row;}

    public Integer getColumn(){ return  column;}

    public void setRow(Integer i_row) { row = i_row;}

    public  void setColumn(Integer i_col) { column = i_col;}

    public void setSelected(boolean i_selected) { isSelected.setValue(i_selected);}

}
