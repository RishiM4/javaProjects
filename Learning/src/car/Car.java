package src.car;

import src.car.parts.Wheel;



public class Car implements Driveable{
    private Wheel wheel;
    public Wheel getWheel() {
        return wheel;
    }
    public void setWheel(Wheel wheel) {
        this.wheel = wheel;
    }
    @Override
    public void drive() {
        System.out.println("Set to mode drive");
    }
    @Override
    public void park() {
        System.out.println("Set to mode park");
    }
    @Override
    public void reverse() {
        System.out.println("Set to mode reverse");
    }
    @Override
    public void turn(double bearing) {
        System.out.println("Turning at bearing " + bearing);
    }
    @Override
    public void move(double speed) {
        System.out.println("Moving at a speed of " + speed);
    }
    @Override
    public void stop() {
        System.out.println("Stopping");
    }

    public static void main(String[] args) {
        

        
    }
}
