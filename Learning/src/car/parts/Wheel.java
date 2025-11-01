package src.car.parts;

public class Wheel {
    private int wheelDiameter;
    private String brand;
    private int tirePressure;
    public Wheel (int diameter, String brand) {
        this.wheelDiameter = diameter;
        this.brand = brand;
    }
    public int getWheelDiameter() {
        return wheelDiameter;
    }
    public String getBrand() {
        return brand;
    }
    public void setTirePressure(int tirePressure) {
        this.tirePressure = tirePressure;
    }
    public int getTirePressure() {
        return tirePressure;
    }

}
