package practice2;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class Main {

  private static final Scanner scanner = new Scanner(System.in);

  public static void main(String[] args) {
    InventoryManager inventoryManager = new InventoryManager(100);

    while (true) {
      displayMenu();

      System.out.println("--------------------------------");
      int choice = readInt("Enter your choice: ");
      System.out.println("--------------------------------");

      try {

        switch (choice) {
          case 1:
            addFood(inventoryManager);
            break;
          case 2:
            addElectronics(inventoryManager);
            break;
          case 3:
            addCrockery(inventoryManager);
            break;
          case 4:
            System.out.println("--------------------------------");
            inventoryManager.displayProducts();
            break;
          case 5:
            displayInventoryQuantity(inventoryManager);
            break;

          case 6:
            displayTotalVatAmount(inventoryManager);
            break;
          case 7:
            System.out.println();
            System.out.println("===== CONSUMPTION EVALUATION =====");
            inventoryManager.displayConsumptionEvaluation();
            break;
          case 0:
            System.out.println("Exiting the program.");
            scanner.close();
            return;
          default:
            System.out.println("Invalid choice. Please try again.");

        }
      } catch (Exception e) {
        System.out.println("Error: " + e.getMessage());
      }

    }
  }

  private static void displayMenu() {
    System.out.println();
    System.out.println("===== INVENTORY MANAGEMENT =====");
    System.out.println("1. Add Food");
    System.out.println("2. Add Electronics");
    System.out.println("3. Add Crockery");
    System.out.println("4. Display Products");
    System.out.println("5. Display Inventory Quantity By Product Type");
    System.out.println("6. Display Total VAT By Product Type");
    System.out.println("7. Evaluate Consumption");
    System.out.println("0. Exit");
  }

  private static String readString(String prompt) {
    while (true) {
      System.out.print(prompt);
      String input = scanner.nextLine().trim();
      if (!input.isEmpty()) {
        return input;
      }
      System.out.println("Value cannot be empty, please try again.");
    }
  }

  private static int readInt(String prompt) {
    while (true) {
      String input = readString(prompt);
      try {
        return Integer.parseInt(input);
      } catch (NumberFormatException e) {
        System.out.println("Invalid integer, please try again.");
      }
    }
  }

  private static double readDouble(String prompt) {
    while (true) {
      String input = readString(prompt);
      try {
        return Double.parseDouble(input);
      } catch (NumberFormatException e) {
        System.out.println("Invalid number, please try again.");
      }
    }
  }

  private static LocalDate readDate(String prompt) {
    while (true) {
      String input = readString(prompt + " (yyyy-MM-dd): ");
      try {
        return LocalDate.parse(input);
      } catch (DateTimeParseException e) {
        System.out.println("Invalid date format, please use yyyy-MM-dd.");
      }
    }
  }

  private static void addFood(InventoryManager inventoryManager) {
    System.out.println();
    System.out.println("===== ADD FOOD PRODUCT =====");
    String productCode = readString("Enter product code: ");
    String productName = readString("Enter product name: ");
    int quantity = readInt("Enter quantity: ");
    double productPrice = readDouble("Enter product price: ");
    LocalDate manufactureDate = readDate("Enter manufacture date");
    LocalDate expiryDate = readDate("Enter expiry date");
    String supplierName = readString("Enter supplier name: ");

    Food food = new Food(productCode, productName, quantity, productPrice, manufactureDate, expiryDate,
        supplierName);

    if (inventoryManager.addProduct(food)) {
      System.out.println("Food product added successfully.");
    } else {
      System.out.println("Failed to add food product.");
    }
  }

  private static void addCrockery(InventoryManager inventoryManager) {
    System.out.println();
    System.out.println("===== ADD CROCKERY PRODUCT =====");

    String productCode = readString("Enter product code: ");
    String productName = readString("Enter product name: ");
    int quantity = readInt("Enter quantity: ");
    double productPrice = readDouble("Enter product price: ");
    String manufacturer = readString("Enter manufacturer: ");
    LocalDate arrivalDate = readDate("Enter arrival date");

    Crockery crockery = new Crockery(productCode, productName, quantity, productPrice, manufacturer, arrivalDate);

    if (inventoryManager.addProduct(crockery)) {
      System.out.println("Crockery product added successfully.");
    } else {
      System.out.println("Failed to add crockery product.");
    }
  }

  private static void addElectronics(InventoryManager inventoryManager) {
    System.out.println();
    System.out.println("===== ADD ELECTRONIC PRODUCT =====");

    String productCode = readString("Enter product code: ");
    String productName = readString("Enter product name: ");
    int quantity = readInt("Enter quantity: ");
    double productPrice = readDouble("Enter product price: ");
    int warrantyMonths = readInt("Enter warranty months: ");
    double capacityKW = readDouble("Enter capacity (KW): ");

    Electronics electronics = new Electronics(productCode, productName, quantity, productPrice, warrantyMonths,
        capacityKW);

    if (inventoryManager.addProduct(electronics)) {
      System.out.println("Electronics product added successfully.");
    } else {
      System.out.println("Failed to add electronics product.");
    }
  }

  private static void displayInventoryQuantity(InventoryManager inventoryManager) {
    System.out.println();
    System.out.println("===== INVENTORY QUANTITY BY PRODUCT TYPE =====");
    System.out.printf("%-20s %,12d%n", "Food", inventoryManager.getQuantityByType(Food.class));
    System.out.printf("%-20s %,12d%n", "Electronics", inventoryManager.getQuantityByType(Electronics.class));
    System.out.printf("%-20s %,12d%n", "Crockery", inventoryManager.getQuantityByType(Crockery.class));
    System.out.println("---------------------------------");
    System.out.printf("%-20s %,12d%n", "Total", inventoryManager.getTotalQuantity());
  }

  private static void displayTotalVatAmount(InventoryManager inventoryManager) {
    System.out.println();
    System.out.println("===== VAT AMOUNT BY PRODUCT TYPE =====");
    System.out.printf("%-20s %,16.2f VND%n", "Food", inventoryManager.getVatAmountByType(Food.class));
    System.out.printf("%-20s %,16.2f VND%n", "Electronics", inventoryManager.getVatAmountByType(Electronics.class));
    System.out.printf("%-20s %,16.2f VND%n", "Crockery", inventoryManager.getVatAmountByType(Crockery.class));
    System.out.println("-----------------------------------------");
    System.out.printf("%-20s %,16.2f VND%n", "Total", inventoryManager.getTotalVatAmount());
  }
}
