package Logic;

/**
 * Created by alex on 16/12/2016.
 */
public class InvalidExtension extends Exception {
    private String msg;

    public InvalidExtension(String message) {
        super(message);
        msg = message;
    }

    public String GetMessage(){
        return msg;
    }
}
