package Domain.Exceptions;

public class EnquestaNoExisteix extends RuntimeException{
    public EnquestaNoExisteix(String message){
        super(message);
    }
}
