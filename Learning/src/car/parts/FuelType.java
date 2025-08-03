package car.parts;

public enum FuelType {
    ELECTRIC ("Electric"),
    GAS ("Gas"),
    DIESEL("Diesel"),
    HYDROGEN("Hydrogen");

    private final String fuelType;

    FuelType(String s) {
        this.fuelType = s;
    }
    @Override
    public String toString() {
        return "This car is powered by " + fuelType + "!";
    }
}
