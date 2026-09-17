package practice2;

public class Electronics extends Product {
  private int warrantyMonths;
  private double capacityKW;

  public Electronics(String productCode, String productName, int quantity, double productPrice, int warrantyMonths,
      double capacityKW) {
    super(productCode, productName, quantity, productPrice);

    setWarrantyMonths(warrantyMonths);
    setCapacityKW(capacityKW);
  }

  @Override
  protected double getVatRate() {
    return 0.1; // 10% VAT for electronics products
  }

  @Override
  public String evaluateConsumption() {
    if (getQuantity() < 3)
      return "Fast selling";
    return "Not evaluated";
  }

  public int getWarrantyMonths() {
    return warrantyMonths;
  }

  public final void setWarrantyMonths(int warrantyMonths) {
    // validate warrantyMonths >= 0
    if (warrantyMonths < 0) {
      throw new IllegalArgumentException("warrantyMonths must be >= 0");
    }
    this.warrantyMonths = warrantyMonths;
  }

  public double getCapacityKW() {
    return capacityKW;
  }

  public final void setCapacityKW(double capacityKW) {
    if (capacityKW < 0) {
      throw new IllegalArgumentException("capacityKW must be >= 0");
    }
    this.capacityKW = capacityKW;
  }

  @Override
  public String toString() {
    return super.toString()
        + String.format("%n  Warranty: %d months | Capacity: %.2f KW", warrantyMonths, capacityKW);
  }
}
