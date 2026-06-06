package Domain.Exceptions;

public class RespostaNoValida extends RuntimeException{
    public RespostaNoValida(String message){
        super(message);
    }
}
