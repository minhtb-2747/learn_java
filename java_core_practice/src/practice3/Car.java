package practice3;

public class Car extends Vehicle {
  private int numberOfSeats;
  private String engineType;

  public Car(String vehicleNumber,
      Manufacturer manufacturer, int yearOfManufacture, String color,
      VehicleOwner vehicleOwner, int numberOfSeats, String engineType) {
    super(vehicleNumber, manufacturer, yearOfManufacture, color, vehicleOwner);

    setNumberOfSeats(numberOfSeats);
    setEngineType(engineType);
  }

  public int getNumberOfSeats() {
    return numberOfSeats;
  }

  public final void setNumberOfSeats(int numberOfSeats) {
    if (numberOfSeats <= 0) {
      throw new IllegalArgumentException("Number of seats must be greater than 0.");
    }

    this.numberOfSeats = numberOfSeats;
  }

  public String getEngineType() {
    return engineType;
  }

  public final void setEngineType(String engineType) {
    if (engineType == null || engineType.trim().isEmpty()) {
      throw new IllegalArgumentException("Engine type cannot be null or empty.");
    }

    this.engineType = engineType;
  }

  @Override
  public String toString() {
    return super.toString()
        + String.format(
            "%n  Seats: %d | Engine: %s",
            numberOfSeats,
            engineType);
  }
}
