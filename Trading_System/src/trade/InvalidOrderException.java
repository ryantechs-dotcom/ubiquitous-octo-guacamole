package trade;

public class InvalidOrderException extends Exception{
    public InvalidOrderException(String message){
        super(message);
    }
}
