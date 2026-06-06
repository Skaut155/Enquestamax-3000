package Domain.Exceptions;

public class AdminNoExisteix extends RuntimeException{
    public AdminNoExisteix(String message){
        super(message);
    }
}
