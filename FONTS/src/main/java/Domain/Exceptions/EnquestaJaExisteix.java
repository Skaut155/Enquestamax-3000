package Domain.Exceptions;

public class EnquestaJaExisteix extends RuntimeException{
    public EnquestaJaExisteix(String message){
        super(message);
    }
}
