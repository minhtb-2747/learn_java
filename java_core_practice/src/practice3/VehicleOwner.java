package practice3;

public class VehicleOwner {
  private String idNumber;
  private String fullName;
  private String email;

  public VehicleOwner(String idNumber, String fullName, String email) {
    setIdNumber(idNumber);
    setFullName(fullName);
    setEmail(email);
  }

  public String getIdNumber() {
    return idNumber;
  }

  public String getFullName() {
    return fullName;
  }

  public String getEmail() {
    return email;
  }

  public void setIdNumber(String idNumber) {
    if (idNumber == null || !idNumber.matches("\\d{12}")) {
      throw new IllegalArgumentException(
          "ID number must contain exactly 12 digits.");
    }

    this.idNumber = idNumber;
  }

  public void setFullName(String fullName) {
    if (fullName == null || fullName.trim().isEmpty()) {
      throw new IllegalArgumentException(
          "Full name cannot be empty.");
    }

    this.fullName = fullName.trim();
  }

  public void setEmail(String email) {
    if (email == null
        || !email.matches(
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
      throw new IllegalArgumentException(
          "Invalid email format.");
    }

    this.email = email;
  }

  @Override
  public String toString() {
    return fullName + " (" + idNumber + ") - " + email;

  }
}
