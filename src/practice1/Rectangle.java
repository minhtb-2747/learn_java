package practice1;

public class Rectangle extends Shape {

  public Rectangle(double width, double height) {
    super(width, height);
  }

  @Override
  public double getArea() {
    return width * height;
  }

  @Override
  public double getPerimeter() {
    return 2 * (width + height);
  }

  @Override
  public void printInfo() {
    System.out.println("Rectangle: width = " + width + ", height = " + height);
    System.out.println("Area: " + getArea());
    System.out.println("Perimeter: " + getPerimeter());
  }
}
