package practice3;

import java.time.Year;

public abstract class Vehicle {

  private String vehicleNumber;
  private Manufacturer manufacturer;
  private int yearOfManufacture;
  private String color;
  private VehicleOwner vehicleOwner;

  public Vehicle(String vehicleNumber,
      Manufacturer manufacturer, int yearOfManufacture, String color,
      VehicleOwner vehicleOwner) {

    setVehicleNumber(vehicleNumber);
    setManufacturer(manufacturer);
    setYearOfManufacture(yearOfManufacture);
    setColor(color);
    setVehicleOwner(vehicleOwner);
  }

  public String getVehicleNumber() {
    return vehicleNumber;
  }

  public final void setVehicleNumber(String vehicleNumber) {
    if (vehicleNumber == null || vehicleNumber.trim().isEmpty() || vehicleNumber.length() != 5) {
      throw new IllegalArgumentException("Vehicle number must have exactly 5 characters.");
    }

    this.vehicleNumber = vehicleNumber;
  }

  public final void setManufacturer(Manufacturer manufacturer) {
    if (manufacturer == null) {
      throw new IllegalArgumentException(
          "Manufacturer cannot be null.");
    }

    this.manufacturer = manufacturer;
  }

  public Manufacturer getManufacturer() {
    return manufacturer;
  }

  public final void setYearOfManufacture(int yearOfManufacture) {
    int currentYear = Year.now().getValue();

    if (yearOfManufacture <= 2000
        || yearOfManufacture > currentYear) {
      throw new IllegalArgumentException(
          "Year must be greater than 2000 and "
              + "less than or equal to current year.");
    }

    this.yearOfManufacture = yearOfManufacture;
  }

  public final void setColor(String color) {
    if (color == null || color.trim().isEmpty()) {
      throw new IllegalArgumentException(
          "Color cannot be empty.");
    }

    this.color = color;
  }

  public final void setVehicleOwner(VehicleOwner vehicleOwner) {
    if (vehicleOwner == null) {
      throw new IllegalArgumentException(
          "Vehicle owner cannot be null.");
    }

    this.vehicleOwner = vehicleOwner;
  }

  public VehicleOwner getVehicleOwner() {
    return vehicleOwner;
  }

  @Override
  public String toString() {
    return String.format(
        "[%s] %s%n"
            + "  Manufacturer: %s%n"
            + "  Year: %d%n"
            + "  Color: %s%n"
            + "  Owner: %s",
        getClass().getSimpleName(),
        vehicleNumber,
        manufacturer,
        yearOfManufacture,
        color,
        vehicleOwner);
  }
}
