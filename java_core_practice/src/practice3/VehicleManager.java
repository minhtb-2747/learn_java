package practice3;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VehicleManager {

  private List<Vehicle> vehicles;

  public VehicleManager() {
    vehicles = new ArrayList<>();
  }

  // add a vehicle to the list
  public boolean addVehicle(Vehicle vehicle) {
    if (vehicle == null) {
      System.out.println("Cannot add a null vehicle.");
      return false;
    }

    if (searchVehicleByNumber(vehicle.getVehicleNumber()) != null) {
      System.out.println("Vehicle number already exists.");

      return false;
    }

    vehicles.add(vehicle);
    return true;
  }

  // Get all vehicles being managed
  public List<Vehicle> getVehicles() {
    return vehicles;
  }

  // 2. Search for transport by vehicle number
  public Vehicle searchVehicleByNumber(String vehicleNumber) {
    for (Vehicle vehicle : vehicles) {
      if (vehicle.getVehicleNumber().equals(vehicleNumber)) {
        return vehicle;
      }
    }
    return null;
  }

  // 3. Find the vehicle owner's vehicle with the corresponding cmnd number
  public List<Vehicle> searchVehiclesByOwnerId(String ownerId) {
    List<Vehicle> results = new ArrayList<>();

    for (Vehicle vehicle : vehicles) {
      if (vehicle.getVehicleOwner().getIdNumber().equals(ownerId)) {
        results.add(vehicle);
      }
    }

    return results;
  }

  // 4. Delete all vehicles of any manufacturer.
  public boolean removeVehiclesByManufacturer(Manufacturer manufacturer) {
    if (manufacturer == null) {
      System.out.println("Manufacturer cannot be null.");
      return false;
    }

    vehicles.removeIf(vehicle -> vehicle.getManufacturer() == manufacturer);
    return true;
  }

  // 5. Indicate which manufacturer has the most vehicles under management
  public Manufacturer getManufacturerWithMostVehicles() {
    if (vehicles.isEmpty()) {
      return null;
    }

    // Count vehicles by manufacturer
    int[] counts = new int[Manufacturer.values().length];

    for (Vehicle vehicle : vehicles) {
      counts[vehicle.getManufacturer().ordinal()]++;
    }

    // Find the manufacturer with the maximum count
    int maxCount = 0;
    Manufacturer mostFrequentManufacturer = null;

    for (int i = 0; i < counts.length; i++) {
      if (counts[i] > maxCount) {
        maxCount = counts[i];
        mostFrequentManufacturer = Manufacturer.values()[i];
      }
    }
    return mostFrequentManufacturer;
  }

  // 6. Sort vehicles by number of vehicles in descending order
  public List<Vehicle> sortVehiclesByManufacturerCount() {
    // Count vehicles by manufacturer
    int[] counts = new int[Manufacturer.values().length];

    for (Vehicle vehicle : vehicles) {
      counts[vehicle.getManufacturer().ordinal()]++;
    }

    vehicles.sort(Comparator
        .comparingInt((Vehicle vehicle) -> counts[vehicle.getManufacturer().ordinal()])
        .reversed().thenComparing(Vehicle::getManufacturer));

    return vehicles;
  }

  // 7. Statistics of each vehicle type, how many vehicles are being managed.
  public Map<String, Integer> countVehiclesByType() {
    Map<String, Integer> counts = new HashMap<>();

    for (Vehicle vehicle : vehicles) {
      String type = vehicle.getClass().getSimpleName();
      counts.merge(type, 1, Integer::sum);
    }

    return counts;
  }
}
