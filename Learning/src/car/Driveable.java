package src.car;

public interface Driveable {
    void drive();
    void park();
    void reverse();
    void turn(double bearing);
    void move(double speed);
    void stop();
}
