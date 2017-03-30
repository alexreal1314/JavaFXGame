package Logic;

/**
 * Created by alex on 30/11/2016.
 */
public class XmlValueException extends Exception{
    private String msg;

    public XmlValueException(String message) {
        super(message);
        msg = message;
    }

    public String GetMessage(){
        return msg;
    }
}
