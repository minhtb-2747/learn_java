package practice3;

public class Motorbike extends Vehicle {
  private double capacity;

  public Motorbike(String vehicleNumber,
      Manufacturer manufacturer, int yearOfManufacture, String color,
      VehicleOwner vehicleOwner, double capacity) {
    super(vehicleNumber, manufacturer, yearOfManufacture, color, vehicleOwner);

    setCapacity(capacity);
  }

  public double getCapacity() {
    return capacity;
  }

  public void setCapacity(double capacity) {
    if (capacity <= 0) {
      throw new IllegalArgumentException("Capacity must be greater than 0.");
    }

    this.capacity = capacity;
  }

  @Override
  public String toString() {
    return super.toString()
        + String.format(
            "%n  Capacity: %.2f cc",
            capacity);
  }
}
