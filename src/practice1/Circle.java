package practice1;

public class Circle extends Shape {

  public Circle(double radius) {
    super(radius * 2, radius * 2); // width = height = 2r
  }

  public double getRadius() {
    return width / 2;
  }

  public double getArea() {
    double radius = getRadius();
    return Math.PI * radius * radius;
  }

  public double getCircumference() {
    return width * Math.PI;
  }

  @Override
  public void printInfo() {
    System.out.println("Circle: radius = " + getRadius());
    System.out.printf("Area: %.2f%n", getArea());
    System.out.printf("Circumference: %.2f%n", getCircumference());
  }
}
