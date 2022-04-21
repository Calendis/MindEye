import java.util.ArrayList;

/*
  A FuncPlot is a drawable parametric function on the x-y plane
*/
public abstract class FuncPlot extends MathObjCol {

    // Returns the point along the function at t
    public abstract Point evaluate(double t);

    // Returns linear interpolation of two points on the FuncPlot
    public Line interpolate(double t, double dt) {
        System.out.println("interpolating in FP");
        Point p1 = evaluate(t);
        Point p2 = evaluate(t + dt);
        p1.setStill();
        p2.setStill();

        // Transform all interpolations
        // TODO: fix FuncPlot interpolate
        /*
        for (Transformation tr : transformations) {

            switch (tr.kind) {
                case SCALE:
                    p1.scale(tr.x);
                    p2.scale(tr.x);
                    p1.animate();
                    p2.animate();
                    break;

                case TRANSLATE:
                    p1.translate(tr.x, tr.y);
                    p2.translate(tr.x, tr.y);
                    p1.animate();
                    p2.animate();
                    break;

                case ROTATE:
                    p1.rotate(tr.x, tr.y, tr.theta);
                    p2.rotate(tr.x, tr.y, tr.theta);
                    p1.animate();
                    p2.animate();
                    break;

            }

        }

         */

        Line lineSeg = new Line(p1, p2);
        //lineSeg.setStill();
        //lineSeg.animate();

        lineSeg.setCullPoints(p1, p2);
        return lineSeg;

    }

    // Returns linear interpolation of range over t as a collection of Lines
    public MathObjCol interpolation(double start, double end, double dt) {
        ArrayList<Line> lines = new ArrayList();
        for (double t = start; t < end; t += dt) {
            lines.add(interpolate(t, dt));
        }

        return new MathObjCol(lines.toArray(new Line[0]));
        // constituents = new ArrayList<MathObject>(lines);
    }

    /*
        Transformations
     */
    public void scale(double x, double y, AnimationInterpolation interpolationKind, AnimationInterpolationDirection direction, AnimationDepth depth,
                      int frames, boolean concurrent) {
        transformations.add(new Scale(x, y, interpolationKind, direction, depth, frames, concurrent));
    }

    public void translate(double x, double y, AnimationInterpolation interpolationKind, AnimationInterpolationDirection direction, AnimationDepth depth,
                          int frames, boolean concurrent) {
        transformations.add(new Translate(x, y, interpolationKind, direction, depth, frames, concurrent));
    }

    public void rotate(double x, double y, double theta, AnimationInterpolation interpolationKind, AnimationInterpolationDirection direction, AnimationDepth depth,
                       int frames, boolean concurrent) {
        transformations.add(new Rotate(x, y, theta, interpolationKind, direction, depth, frames, concurrent));
    }

    // Animates the FuncPlot moving to its target position
    public void animate() {
        System.out.println("ERROR: FuncPlot.animate");
    }

    public void setStill() {
        targX = x;
        targY = y;

    }

    public void draw() {
        throw new ArithmeticException("FuncPlot must be interpolated before drawing! Call interpolation().draw() to draw this FuncPlot");
    }

}
