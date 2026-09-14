package practice2;

public class InventoryManager {

  private Product[] products;
  private int productCount;

  public InventoryManager(int capacity) {
    if (capacity <= 0) {
      throw new IllegalArgumentException("capacity must be > 0");
    }
    products = new Product[capacity];
    productCount = 0;
  }

  public boolean addProduct(Product product) {
    if (productCount >= products.length) {
      System.out.println("Inventory is full.");
      return false;
    }

    if (isDuplicateProductCode(product.getProductCode())) {
      System.out.println();
      System.out.println("Duplicate product code: " + product.getProductCode());
      return false;
    }

    products[productCount++] = product;
    System.out.println("Product added: " + product.getProductCode() + " - " + product.getProductName());
    return true;
  }

  private boolean isDuplicateProductCode(String productCode) {
    for (int i = 0; i < productCount; i++) {
      if (products[i].getProductCode().equals(productCode)) {
        return true;
      }
    }
    return false;
  }

  public int getQuantityByType(Class<? extends Product> type) {
    int totalQuantity = 0;
    for (int i = 0; i < productCount; i++) {
      if (type.isInstance(products[i])) {
        totalQuantity += products[i].getQuantity();
      }
    }
    return totalQuantity;
  }

  public int getTotalQuantity() {
    return getQuantityByType(Product.class);
  }

  public double getVatAmountByType(Class<? extends Product> type) {
    double totalVatAmount = 0;
    for (int i = 0; i < productCount; i++) {
      if (type.isInstance(products[i])) {
        totalVatAmount += products[i].getVatAmount();
      }
    }
    return totalVatAmount;
  }

  public double getTotalVatAmount() {
    return getVatAmountByType(Product.class);
  }

  public void displayProducts() {
    if (productCount == 0) {
      System.out.println("No products.");
      return;
    }

    for (int i = 0; i < productCount; i++) {
      System.out.println(products[i]);
      System.out.println("--------------------------------");
    }
  }

  public void displayConsumptionEvaluation() {
    if (productCount == 0) {
      System.out.println("No products.");
      return;
    }

    for (int i = 0; i < productCount; i++) {
      System.out.println(products[i]);
      System.out.println("  Consumption Evaluation: " + products[i].evaluateConsumption());
      System.out.println("--------------------------------");
    }
  }
}
