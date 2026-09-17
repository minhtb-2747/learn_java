package practice1;

public class Shape {
  protected double width;
  protected double height;

  public Shape(double width, double height) {
    this.width = width;
    this.height = height;
  }

  public void printInfo() {
    System.out.println("Shape: width = " + width + ", height = " + height);
  }
}
