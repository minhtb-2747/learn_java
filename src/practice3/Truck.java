package practice3;

public class Truck extends Vehicle {

  private double tonnage;

  public Truck(String vehicleNumber,
      Manufacturer manufacturer, int yearOfManufacture, String color,
      VehicleOwner vehicleOwner, double tonnage) {
    super(vehicleNumber, manufacturer, yearOfManufacture, color, vehicleOwner);

    setTonnage(tonnage);
  }

  public double getTonnage() {
    return tonnage;
  }

  public void setTonnage(double tonnage) {
    if (tonnage <= 0) {
      throw new IllegalArgumentException("Tonnage must be greater than 0.");
    }

    this.tonnage = tonnage;
  }

  @Override
  public String toString() {
    return super.toString()
        + String.format(
            "%n  Tonnage: %.2f tons",
            tonnage);
  }
}
