package practice1;

public class Circle extends Shape {
  public Circle(double radius) {
    super(radius * 2, radius * 2); // width = height = 2r
  }

  @Override
  public double getArea() {
    double radius = width / 2;
    return Math.PI * radius * radius;
  }

  @Override
  public double getPerimeter() {
    return Math.PI * width; // diameter = width
  }

  public double getCircumference() {
    return getPerimeter();
  }

  public void printInfo() {
    System.out.println("Circle: radius = " + (width / 2));
    System.out.printf("Area: %.2f%n", getArea());
    System.out.printf("Circumference: %.2f%n", getPerimeter());
  }
}
