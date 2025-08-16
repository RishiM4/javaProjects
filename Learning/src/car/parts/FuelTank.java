package car.parts;
import java.io.Serializable;

import car.IllegalFuelTypeException;


/**
 * The {@code FuelTank} class is designed to represent a fuel tank 
 * of the car class.
 * <p>
 * The class {@code FuelTank} includes methods for getting the {@code FuelType}, 
 * capacity, and current fuel level, as well as methods refueling and draining the fuel.
 * <p>
 * 
 * Here is an example of how to use the {@code FuelTank} class:
 *
 *<blockquote><pre>
 *     
 *FuelTank fuelTank = new FuelTank(FuelType.GAS, 40, 0);
 *  String fuelType = fuelTank.getFuelType();
 *  double capacity =  fuelTank.getCapacity();
 *  double currentCapacity = fuelTank.getCurrentFuelLevel();
 *  fuelTank.refuel(10);
 *  fuelTank.drain(5);
 *     
 * </pre></blockquote> 
 * @since Java 24.1
 * @author Rishi Mohan
 * @see {@link car.Car}
 * @see {@link car.parts.ElectricFuelTank}
 */
public class FuelTank implements Serializable{
    private FuelType fuelType;
    private double capacity;
    private double currentFuelLevel;
    /**
     * 
     * @param fuelType - Sets the {@code FuelType} of the {@code FuelTank}.
     * @param capacity - Sets the maximum capacity of the {@code FuelTank}.
     * @param currentFuelLevel - Sets the fuel level of the {@code FuelTank}.
     * 
     * @throws IllegalFuelTypeException If {@code FuelType} is equal to {@code FuelType.ELECTRIC}, use class {@link car.parts.ElectricFuelTank} instead.
     * @throws IllegalArgumentException If given capacity is less than or equal to zero.
     * @throws IllegalArgumentException If current fuel level is less than zero.
     */
    public FuelTank(FuelType fuelType, double capacity, double currentFuelLevel) {
        if (fuelType == FuelType.ELECTRIC) {
            throw new IllegalFuelTypeException("Class FuelTank cannot hold FuelType.ELECTRIC, use class ElectricFuelTank instead.");
        }
        if (capacity <= 0) {
            throw new IllegalArgumentException("Cannot have a capacity less than or equal to zero.");
        }
        if (currentFuelLevel < 0) {
            throw new IllegalArgumentException("Cannot have a current fuel level less than zero.");
        }
        this.fuelType = fuelType;
        this.capacity = capacity;
        this.currentFuelLevel = currentFuelLevel;
    }/**
     * Sets the {@code currentFuelLevel} to {@code 0}
     * @param fuelType - Sets the {@code FuelType} of the {@code FuelTank}.
     * @param capacity - Sets the maximum capacity of the {@code FuelTank}.
     *
     * 
     * @throws IllegalFuelTypeException If {@code FuelType} is equal to {@code FuelType.ELECTRIC}; Use class {@link car.parts.ElectricFuelTank} instead.
     * @throws IllegalArgumentException If given capacity is less than or equal to zero.
     */
    public FuelTank(FuelType fuelType, double capacity) {
        if (fuelType == FuelType.ELECTRIC) {
            throw new IllegalFuelTypeException("Class FuelTank cannot hold FuelType.ELECTRIC, use class ElectricFuelTank instead.");
        }
        if (capacity <= 0) {
            throw new IllegalArgumentException("Cannot have a capacity less than or equal to zero.");
        }
        this.fuelType = fuelType;
        this.capacity = capacity;
        this.currentFuelLevel = 0;
    }
    /**
     * Default Constructor of class {@code FuelTank}, sets the parameters as following:
     * <p>
     * {@code fuelType} to {@code FuelType.ELECTRIC}.
     * <p>
     * {@code capacity} to {@code 40}.
     * <p>
     * {@code currentFuelLevel} to {@code 0}.
     */
    public FuelTank() {
        this.fuelType = FuelType.GAS;
        this.capacity = 40;
        this.currentFuelLevel = 0;
    }

    
    /**
     * 
     * @return The {@code FuelType} that the fuel tank holds.
     */
    public FuelType getFuelType() {
        return fuelType;
    }
    /**
     * 
     * @return The capacity of the fuel tank as a double variable.
     */
    public double getCapacity() {
        return capacity;
    }
    /**
     * 
     * @return The current fuel level of the fuel tank as a double variable.
     */
    public double getCurrentFuelLevel() {
        return currentFuelLevel;
    }
    /**
     * Adds a specified amount of fuel to the fuel tank.
     * @param fuel The amount of fuel you would like to add.
     * @return The fuel level after all modifications
     * @throws IllegalArgumentException If parameter {@code fuel} is less than zero
     * @throws IllegalArgumentException If the fuel added to the current fuel level exceeds the maximum capacity.
     */
    public double refuel(double fuel) {
        if (fuel < 0) {
            throw new IllegalArgumentException("Cannot add a negative amount of fuel");
        }
        else if (currentFuelLevel + fuel <= capacity) {
            currentFuelLevel += fuel;
        }
        else {
            
            throw new IllegalArgumentException("Fuel level " + (currentFuelLevel+fuel) + " is out of bounds for capacity " + capacity);
        }
        return currentFuelLevel;
    }
    /**
     * Removes a specified amount of fuel from the fuel tank.
     * @param fuel The amount of fuel you would like to remove.
     * @return The fuel level after all modifications
     * @throws IllegalArgumentException If parameter {@code fuel} is less than zero
     * @throws IllegalArgumentException If the fuel subtracted from the current fuel level is less than zero.
     */
    public double drain(double fuel) {
        if (fuel < 0) {
            throw new IllegalArgumentException("Cannot remove a negative amount of fuel");
        }
        else if (currentFuelLevel - fuel >= 0) {
            currentFuelLevel -= fuel;
        }
        else {
            throw new IllegalArgumentException("Fuel level " + (currentFuelLevel-fuel) + " cannot be less than zero.");
        }
        return currentFuelLevel;
    }
    
    public static void main(String[] args) {
        System.out.println(FuelType.DIESEL);
        FuelTank tt = new FuelTank();
        tt.getCapacity();
        
        
    
    }
}
