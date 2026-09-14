package practice2;

public abstract class Product {
  private String productCode;
  private String productName;
  private int quantity;
  private double productPrice;

  public Product(String productCode, String productName, int quantity, double productPrice) {
    if (quantity < 0) {
      throw new IllegalArgumentException("quantity must be >= 0");
    }

    this.productCode = productCode;
    this.productName = productName;
    this.quantity = quantity;
    this.productPrice = productPrice;
  }

  protected abstract double getVatRate();

  public abstract String evaluateConsumption();

  public double getVatAmount() {
    return productPrice * quantity * getVatRate();
  }

  public String getProductCode() {
    return productCode;
  }

  public void setProductCode(String productCode) {
    if (productCode == null || productCode.trim().isEmpty()) {
      throw new IllegalArgumentException("productCode cannot be null or empty");
    }

    this.productCode = productCode;
  }

  public String getProductName() {
    return productName;
  }

  public void setProductName(String productName) {
    this.productName = productName;
  }

  public int getQuantity() {
    return quantity;
  }

  public void setQuantity(int quantity) {
    if (quantity < 0) {
      throw new IllegalArgumentException("quantity must be >= 0");
    }
    this.quantity = quantity;
  }

  public double getProductPrice() {
    return productPrice;
  }

  public void setProductPrice(double productPrice) {
    this.productPrice = productPrice;
  }

  @Override
  public String toString() {
    return String.format("[%s] %s - %s%n  Quantity: %d | Price: %,.2f | VAT: %.2f",
        getClass().getSimpleName(), productCode, productName, quantity, productPrice, getVatAmount());
  }
}
