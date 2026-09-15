package practice3;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {

  private static final Scanner scanner = new Scanner(System.in);

  public static void main(String[] args) {
    VehicleManager vehicleManager = new VehicleManager();

    while (true) {
      displayMenu();

      System.out.println("--------------------------------");
      int choice = readInt("Enter your choice: ");
      System.out.println("--------------------------------");

      try {

        switch (choice) {
          case 1:
            addCar(vehicleManager);
            break;
          case 2:
            addMotorbike(vehicleManager);
            break;
          case 3:
            addTruck(vehicleManager);
            break;
          case 4:
            displayAllVehicles(vehicleManager);
            break;
          case 5:
            searchVehicleByNumber(vehicleManager);
            break;
          case 6:
            searchVehiclesByOwnerId(vehicleManager);
            break;
          case 7:
            removeVehiclesByManufacturer(vehicleManager);
            break;
          case 8:
            displayManufacturerWithMostVehicles(vehicleManager);
            break;
          case 9:
            displaySortedVehicles(vehicleManager);
            break;
          case 10:
            displayCountByType(vehicleManager);
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
    System.out.println("===== VEHICLE MANAGEMENT =====");
    System.out.println("1. Add Car");
    System.out.println("2. Add Motorbike");
    System.out.println("3. Add Truck");
    System.out.println("4. Display All Vehicles");
    System.out.println("5. Search Vehicle By Vehicle Number");
    System.out.println("6. Search Vehicles By Owner ID Number");
    System.out.println("7. Remove All Vehicles Of A Manufacturer");
    System.out.println("8. Display Manufacturer With Most Vehicles");
    System.out.println("9. Sort Vehicles By Number Of Vehicles In Descending Order");
    System.out.println("10. Display Vehicle Quantity By Vehicle Type");
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

  private static Manufacturer readManufacturer(String prompt) {
    while (true) {
      String input = readString(prompt + " (HONDA, YAMAHA, TOYOTA, SUZUKI): ");
      try {
        return Manufacturer.valueOf(input.toUpperCase());
      } catch (IllegalArgumentException e) {
        System.out.println("Invalid manufacturer, please try again.");
      }
    }
  }

  private static VehicleOwner readVehicleOwner() {
    String idNumber = readString("Enter owner ID number: ");
    String fullName = readString("Enter owner full name: ");
    String email = readString("Enter owner email: ");

    return new VehicleOwner(idNumber, fullName, email);
  }

  private static void addCar(VehicleManager vehicleManager) {
    System.out.println();
    System.out.println("===== ADD CAR =====");

    String vehicleNumber = readString("Enter vehicle number: ");
    Manufacturer manufacturer = readManufacturer("Enter manufacturer");
    int yearOfManufacture = readInt("Enter year of manufacture: ");
    String color = readString("Enter color: ");
    VehicleOwner vehicleOwner = readVehicleOwner();
    int numberOfSeats = readInt("Enter number of seats: ");
    String engineType = readString("Enter engine type: ");

    Car car = new Car(vehicleNumber, manufacturer, yearOfManufacture, color, vehicleOwner, numberOfSeats,
        engineType);

    if (vehicleManager.addVehicle(car)) {
      System.out.println("\nCar added successfully.");
    } else {
      System.out.println("\nFailed to add car.");
    }
  }

  private static void addMotorbike(VehicleManager vehicleManager) {
    System.out.println();
    System.out.println("===== ADD MOTORBIKE =====");

    String vehicleNumber = readString("Enter vehicle number: ");
    Manufacturer manufacturer = readManufacturer("Enter manufacturer");
    int yearOfManufacture = readInt("Enter year of manufacture: ");
    String color = readString("Enter color: ");
    VehicleOwner vehicleOwner = readVehicleOwner();
    double capacity = readDouble("Enter capacity (cc): ");

    Motorbike motorbike = new Motorbike(vehicleNumber, manufacturer, yearOfManufacture, color, vehicleOwner,
        capacity);

    if (vehicleManager.addVehicle(motorbike)) {
      System.out.println("\nMotorbike added successfully.");
    } else {
      System.out.println("\nFailed to add motorbike.");
    }
  }

  private static void addTruck(VehicleManager vehicleManager) {
    System.out.println();
    System.out.println("===== ADD TRUCK =====");

    String vehicleNumber = readString("Enter vehicle number: ");
    Manufacturer manufacturer = readManufacturer("Enter manufacturer");
    int yearOfManufacture = readInt("Enter year of manufacture: ");
    String color = readString("Enter color: ");
    VehicleOwner vehicleOwner = readVehicleOwner();
    double tonnage = readDouble("Enter tonnage (tons): ");

    Truck truck = new Truck(vehicleNumber, manufacturer, yearOfManufacture, color, vehicleOwner, tonnage);

    if (vehicleManager.addVehicle(truck)) {
      System.out.println("\nTruck added successfully.");
    } else {
      System.out.println("\nFailed to add truck.");
    }
  }

  private static void displayAllVehicles(VehicleManager vehicleManager) {
    System.out.println();
    System.out.println("===== ALL VEHICLES =====");

    List<Vehicle> vehicles = vehicleManager.getVehicles();

    if (vehicles.isEmpty()) {
      System.out.println("No vehicle is being managed.");
      return;
    }

    for (Vehicle vehicle : vehicles) {
      System.out.println(vehicle);
      System.out.println("--------------------------------");
    }
  }

  private static void searchVehicleByNumber(VehicleManager vehicleManager) {
    System.out.println();
    System.out.println("===== SEARCH VEHICLE BY VEHICLE NUMBER =====");

    String vehicleNumber = readString("Enter vehicle number: ");
    Vehicle vehicle = vehicleManager.searchVehicleByNumber(vehicleNumber);

    if (vehicle == null) {
      System.out.println("No vehicle found with number " + vehicleNumber + ".");
    } else {
      System.out.println(vehicle);
    }
  }

  private static void searchVehiclesByOwnerId(VehicleManager vehicleManager) {
    System.out.println();
    System.out.println("===== SEARCH VEHICLES BY OWNER ID NUMBER =====");

    String ownerId = readString("Enter owner ID number: ");
    List<Vehicle> results = vehicleManager.searchVehiclesByOwnerId(ownerId);

    if (results.isEmpty()) {
      System.out.println("No vehicle found for owner " + ownerId + ".");
      return;
    }

    for (Vehicle vehicle : results) {
      System.out.println(vehicle);
    }
  }

  private static void removeVehiclesByManufacturer(VehicleManager vehicleManager) {
    System.out.println();
    System.out.println("===== REMOVE ALL VEHICLES OF A MANUFACTURER =====");

    Manufacturer manufacturer = readManufacturer("Enter manufacturer");

    if (vehicleManager.removeVehiclesByManufacturer(manufacturer)) {
      System.out.println("Removed all vehicles of " + manufacturer + ".");
    } else {
      System.out.println("Failed to remove vehicles.");
    }
  }

  private static void displayManufacturerWithMostVehicles(VehicleManager vehicleManager) {
    System.out.println();
    System.out.println("===== MANUFACTURER WITH MOST VEHICLES =====");

    Manufacturer most = vehicleManager.getManufacturerWithMostVehicles();

    if (most == null) {
      System.out.println("No vehicle is being managed.");
      return;
    }

    System.out.printf("%-20s%n", most);
  }

  private static void displaySortedVehicles(VehicleManager vehicleManager) {
    System.out.println();
    System.out.println("===== SORT VEHICLES BY NUMBER OF VEHICLES IN DESCENDING ORDER =====");

    List<Vehicle> sortedVehicles = vehicleManager.sortVehiclesByManufacturerCount();

    if (sortedVehicles.isEmpty()) {
      System.out.println("No vehicle is being managed.");
      return;
    }

    for (Vehicle vehicle : sortedVehicles) {
      System.out.println(vehicle);
    }
  }

  private static void displayCountByType(VehicleManager vehicleManager) {
    System.out.println();
    System.out.println("===== VEHICLE QUANTITY BY VEHICLE TYPE =====");

    Map<String, Integer> counts = vehicleManager.countVehiclesByType();

    counts.forEach((type, count) -> System.out.printf("%-20s %,12d%n", type, count));
  }
}
