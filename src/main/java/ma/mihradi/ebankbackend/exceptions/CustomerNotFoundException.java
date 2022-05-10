package ma.mihradi.ebankbackend.exceptions;



public class CustomerNotFoundException extends Exception {
    public CustomerNotFoundException(String message) {
        super(message);
    }
}
