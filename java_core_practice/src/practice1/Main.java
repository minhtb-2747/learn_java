package practice1;

public class Main {

  public static void main(String[] args) {
    Shape shape = new Shape(3, 4);
    shape.printInfo();

    System.out.println("------------------------------");
    Rectangle rectangle = new Rectangle(5, 10);
    rectangle.printInfo();

    System.out.println("------------------------------");
    Circle circle = new Circle(4);
    circle.printInfo();
  }
}
