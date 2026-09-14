package practice2;

import java.time.LocalDate;

public class Food extends Product {
  private LocalDate manufactureDate;
  private LocalDate expiryDate;
  private String supplierName;

  public Food(String productCode, String productName, int quantity, double productPrice, LocalDate manufactureDate,
      LocalDate expiryDate, String supplierName) {
    super(productCode, productName, quantity, productPrice);

    // expiryDate must be after or equal manufactureDate
    if (expiryDate.isBefore(manufactureDate)) {
      throw new IllegalArgumentException("expiryDate must be after or equal manufactureDate");
    }

    this.manufactureDate = manufactureDate;
    this.expiryDate = expiryDate;
    this.supplierName = supplierName;
  }

  @Override
  public double getVatRate() {
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

  public void setManufactureDate(LocalDate manufactureDate) {

    // validate empty manufactureDate
    if (manufactureDate == null || manufactureDate.toString().trim().isEmpty()) {
      throw new IllegalArgumentException("manufactureDate cannot be null");
    }

    // manufactureDate must be before or equal expiryDate
    if (manufactureDate.isAfter(expiryDate)) {
      throw new IllegalArgumentException("manufactureDate must be before or equal expiryDate");
    }

    this.manufactureDate = manufactureDate;
  }

  public LocalDate getExpiryDate() {
    return expiryDate;
  }

  public void setExpiryDate(LocalDate expiryDate) {
    // validate empty expiryDate
    if (expiryDate == null || expiryDate.toString().trim().isEmpty()) {
      throw new IllegalArgumentException("expiryDate cannot be null");
    }

    // expiryDate must be after or equal manufactureDate
    if (expiryDate.isBefore(manufactureDate)) {
      throw new IllegalArgumentException("expiryDate must be after or equal manufactureDate");
    }

    this.expiryDate = expiryDate;
  }

  public String getSupplierName() {

    return supplierName;
  }

  public void setSupplierName(String supplierName) {
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
