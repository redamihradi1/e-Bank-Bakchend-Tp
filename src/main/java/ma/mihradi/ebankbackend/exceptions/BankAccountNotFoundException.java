package ma.mihradi.ebankbackend.exceptions;

public class BankAccountNotFoundException extends Exception{
    public BankAccountNotFoundException(String s) {
        super(s);
    }
}
