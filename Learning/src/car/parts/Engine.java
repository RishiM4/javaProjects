package src.car.parts;

abstract class Engine {
    protected Engine (String fuelType, int peakRPM) {
        this.fuelType = fuelType;
        this.peakRPM = peakRPM;
    }
    private String fuelType;
    private int peakRPM;

    public String getFuelType() {
        return fuelType;
    }
    public int getPeakRPM() {
        return peakRPM;
    }

}
