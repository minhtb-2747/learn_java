package practice2;

import java.time.LocalDate;

public class Food extends Product {
  private LocalDate manufactureDate;
  private LocalDate expiryDate;
  private String supplierName;

  public Food(String productCode, String productName, int quantity, double productPrice, LocalDate manufactureDate,
      LocalDate expiryDate, String supplierName) {
    super(productCode, productName, quantity, productPrice);

    validateDateRange(manufactureDate, expiryDate);
    this.manufactureDate = manufactureDate;
    this.expiryDate = expiryDate;

    setSupplierName(supplierName);
  }

  // expiryDate must be after or equal manufactureDate
  private static void validateDateRange(LocalDate manufactureDate, LocalDate expiryDate) {
    if (manufactureDate == null) {
      throw new IllegalArgumentException("manufactureDate cannot be null");
    }

    if (expiryDate == null) {
      throw new IllegalArgumentException("expiryDate cannot be null");
    }

    if (expiryDate.isBefore(manufactureDate)) {
      throw new IllegalArgumentException("expiryDate must be after or equal manufactureDate");
    }
  }

  @Override
  protected double getVatRate() {
    return 0.05; // 5% VAT for food products
  }

  @Override
  public String evaluateConsumption() {
    if (getQuantity() > 0 && LocalDate.now().isAfter(expiryDate)) {
      return "Difficult to sell";
    }
    return "Not evaluated";
  }

  public LocalDate getManufactureDate() {
    return manufactureDate;
  }

  public final void setManufactureDate(LocalDate manufactureDate) {
    validateDateRange(manufactureDate, this.expiryDate);

    this.manufactureDate = manufactureDate;
  }

  public LocalDate getExpiryDate() {
    return expiryDate;
  }

  public final void setExpiryDate(LocalDate expiryDate) {
    validateDateRange(this.manufactureDate, expiryDate);

    this.expiryDate = expiryDate;
  }

  public String getSupplierName() {
    return supplierName;
  }

  public final void setSupplierName(String supplierName) {
    // validate empty supplierName
    if (supplierName == null || supplierName.trim().isEmpty()) {
      throw new IllegalArgumentException("supplierName cannot be null or empty");
    }

    this.supplierName = supplierName;
  }

  @Override
  public String toString() {
    return super.toString()
        + String.format("%n  Manufacture date: %s | Expiry date: %s | Supplier: %s",
            manufactureDate, expiryDate, supplierName);
  }
}
