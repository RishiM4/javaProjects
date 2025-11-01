package src.car;

public class IllegalFuelTypeException extends RuntimeException{
    public IllegalFuelTypeException() {
        super();
    }
    public IllegalFuelTypeException(String s) {
        super(s);
    }
}
