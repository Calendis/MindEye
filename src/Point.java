/*
  The basic Transformation methods are defined on Point
  A Point is "static". This does not mean it is not mutable, but rather
  that given parameter t, evaluate(t) will always return x and y
*/
public class Point extends FuncPlot {

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Point evaluate(double t) {
        return new Point(x, y);
    }

    public void draw() {
        GlobalContainer.pApplet.point((float)windowX(), (float)windowY());

    }

    public void draw(double x, double y) {
        GlobalContainer.pApplet.point((float)windowX(x), (float)windowY(y));
    }

}
