package practice2;

public abstract class Product {
  private String productCode;
  private String productName;
  private int quantity;
  private double productPrice;

  public Product(String productCode, String productName, int quantity, double productPrice) {
    setProductCode(productCode);
    setProductName(productName);
    setQuantity(quantity);
    setProductPrice(productPrice);
  }

  protected abstract double getVatRate();

  public abstract String evaluateConsumption();

  public double getVatAmount() {
    return productPrice * quantity * getVatRate();
  }

  public String getProductCode() {
    return productCode;
  }

  public final void setProductCode(String productCode) {
    if (productCode == null || productCode.trim().isEmpty()) {
      throw new IllegalArgumentException("productCode cannot be null or empty");
    }

    this.productCode = productCode;
  }

  public String getProductName() {
    return productName;
  }

  public final void setProductName(String productName) {
    if (productName == null || productName.trim().isEmpty()) {
      throw new IllegalArgumentException("productName cannot be null or empty");
    }

    this.productName = productName;
  }

  public int getQuantity() {
    return quantity;
  }

  public final void setQuantity(int quantity) {
    if (quantity < 0) {
      throw new IllegalArgumentException("quantity must be >= 0");
    }
    this.quantity = quantity;
  }

  public double getProductPrice() {
    return productPrice;
  }

  public final void setProductPrice(double productPrice) {
    if (productPrice < 0) {
      throw new IllegalArgumentException("productPrice must be >= 0");
    }

    this.productPrice = productPrice;
  }

  @Override
  public String toString() {
    return String.format("[%s] %s - %s%n  Quantity: %d | Price: %,.2f | VAT: %,.2f",
        getClass().getSimpleName(), productCode, productName, quantity, productPrice, getVatAmount());
  }
}
