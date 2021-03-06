import java.util.ArrayList;

/*
  A FuncPlot is a drawable parametric function on the x-y plane
*/
public abstract class FuncPlot extends MathObjCol {

    // Returns the point along the function at t
    public abstract Point evaluate(double t);

    // Returns linear interpolation of two points on the FuncPlot
    public Line interpolate(double t, double dt) {
        Point p1 = evaluate(t);
        //p1.x += x;
        //p1.y += y;

        Point p2 = evaluate(t + dt);
        //p2.x += x;
        //p2.y += y;

        p1.setStill();
        p2.setStill();

        Line lineSeg = new Line(p1, p2);
        lineSeg.setCullPoints(p1, p2);

        //lineSeg.x += p1.x;
        //lineSeg.y += p1.y;
        lineSeg.setStill();
        return lineSeg;

    }

    // Returns linear interpolation of range over t as a collection of Lines
    public MathObjCol interpolation(double start, double end, double dt) {
        ArrayList<Line> lines = new ArrayList<>();
        for (double t = start; t < end; t += dt) {
            lines.add(interpolate(t, dt));
        }

        return new MathObjCol(lines.toArray(new Line[0]));
    }

    public void draw() {
        throw new ArithmeticException("FuncPlot must be interpolated before drawing! Call interpolation().draw() to draw this FuncPlot");
    }

    public void draw(double x, double y) {
        throw new ArithmeticException("FuncPlot must be interpolated before drawing! Call interpolation().draw() to draw this FuncPlot");
    }

}
