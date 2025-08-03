package car.parts;

public class GasEngine extends Engine{
    public GasEngine (String fuelType, int peakRPM, int cylinders, int horsepower, double capacity) {
        super(fuelType, peakRPM);
        this.cylinders = cylinders;
        this.horsepower = horsepower;
        this.capacity = capacity;
    }
    private int cylinders;
    private int horsepower;
    private double capacity;


    public int getCylinders() {
        return cylinders;
    }
    public int getHorsepower() {
        return horsepower;
    }
    public Double getCapacity() {
        return capacity;
    }
}
