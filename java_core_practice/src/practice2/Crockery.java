package practice2;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Crockery extends Product {

  private String manufacturer;
  private LocalDate arrivalDate;

  public Crockery(String productCode, String productName, int quantity, double productPrice, String manufacturer,
      LocalDate arrivalDate) {
    super(productCode, productName, quantity, productPrice);

    setManufacturer(manufacturer);
    setArrivalDate(arrivalDate);
  }

  @Override
  protected double getVatRate() {
    return 0.1; // 10% VAT for crockery products
  }

  @Override
  public String evaluateConsumption() {
    long daysSinceArrival = ChronoUnit.DAYS.between(arrivalDate, LocalDate.now());
    if (getQuantity() > 50 && daysSinceArrival > 10)
      return "Slow selling";
    return "Not evaluated";
  }

  public String getManufacturer() {
    return manufacturer;
  }

  public final void setManufacturer(String manufacturer) {
    // validate empty manufacturer
    if (manufacturer == null || manufacturer.trim().isEmpty()) {
      throw new IllegalArgumentException("manufacturer cannot be null or empty");
    }

    this.manufacturer = manufacturer;
  }

  public LocalDate getArrivalDate() {
    return arrivalDate;
  }

  public final void setArrivalDate(LocalDate arrivalDate) {
    if (arrivalDate == null) {
      throw new IllegalArgumentException("arrivalDate cannot be null");
    }

    this.arrivalDate = arrivalDate;
  }

  @Override
  public String toString() {
    return super.toString()
        + String.format("%n  Manufacturer: %s | Arrival date: %s", manufacturer, arrivalDate);
  }
}
