package Domain.Exceptions;

public class PreguntaNoExisteix extends RuntimeException{
    public PreguntaNoExisteix(String message){
        super(message);
    }
}
