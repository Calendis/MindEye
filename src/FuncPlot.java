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

        // Populate concurrents if none exist
        if (concurrents.size() == 0)
        {
            // Nothing to do
            if (transformations.size() == 0)
            {
                return;
            }

            concurrents.add(transformations.poll());

            // If the transformation is marked as concurrent, add all concurrent animations
            if (concurrents.get(0).concurrent) {
                while(!transformations.isEmpty() && transformations.peek().concurrent) {
                    concurrents.add(transformations.poll());
                }
            }
        }

        // concurrents now contains all our relevant transformations, which will now perform
        for (Transformation ctr : concurrents) {

            switch (ctr.depth) {
                // Transform the coordinates of each constituent object
                case NESTED -> {
                    System.out.println("applying to " + constituents.size() + " constituents!");
                    for (MathObject c : constituents) {
                        System.out.println("    This constituent is at (" + c.x + ", " + c.y+")");
                        System.out.println("        Origin: (" + c.originX + ", " + c.originY + ")");
                        System.out.println("    targ: " + c.targX + ", " + c.targY);
                        double[] transTarg = ctr.apply(c);
                        c.targX = transTarg[0];
                        c.targY = transTarg[1];
                    }
                }

                // Transform the coordinate of the object itself
                case OUTER -> {
                    double[] transTarg = ctr.apply(this);
                    targX = transTarg[0];
                    targY = transTarg[1];

                    x = targX;
                    y = targY;
                }

                case ALL -> {
                    //
                }
            }
            ctr.advance(1);
        }

        // Depopulate concurrents when transformations have the done flag set
        while(!concurrents.isEmpty() && concurrents.get(0).done) {
            //originX = x;
            //originY = y;
            concurrents.remove(0);
        }

        // Once animation is done, set the coordinates
        for (MathObject c : constituents) {
            c.x = c.targX;
            c.y = c.targY;
        }
    }

    public void setStill() {
        targX = x;
        targY = y;
        originX = x;
        originY = y;
        for (MathObject c : constituents) {
            c.setStill();
        }

    }

    public void draw() {
        throw new ArithmeticException("FuncPlot must be interpolated before drawing! Call interpolation().draw() to draw this FuncPlot");
    }

}
