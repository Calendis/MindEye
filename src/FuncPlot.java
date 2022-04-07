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
    public void scale(double x, double y, AnimationInterpolation interpolationKind, AnimationInterpolationDirection direction,
                      int frames, boolean concurrent) {
        transformations.add(new Scale(x, y, interpolationKind, direction, frames, concurrent));
    }

    public void translate(double x, double y, AnimationInterpolation interpolationKind,
                          AnimationInterpolationDirection direction, int frames, boolean concurrent) {
        transformations.add(new Translate(x, y, interpolationKind, direction, frames, concurrent));
    }

    public void rotate(double x, double y, double theta, AnimationInterpolation interpolationKind,
                       AnimationInterpolationDirection direction, int frames, boolean concurrent) {
        System.out.println("rotating in FP");
        transformations.add(new Rotate(x, y, theta, interpolationKind, direction, frames, concurrent));
    }

    // Animates the FuncPlot moving to its target position
    public void animate() {
        System.out.println("animating in FP");
        for (int i = 0; i < transformations.size(); i++) {
            System.out.println("transformation i: " + i);
            Transformation tr = transformations.get(i);

            // Skip completed transformations. A transformation can be already done if it was part of a group of
            // concurrent transformations
            if (tr.done) {
                continue;
            }

            // All transformations to be animated simultaneously
            // Often, this may be populated with only one element!
            ArrayList<Transformation> concurrents = new ArrayList();
            concurrents.add(tr);

            // If the transformation is set to be done concurrently, it must be concurrently WITH something
            // So we look ahead for more transformations set to be concurrent
            if (tr.concurrent) {
                int j = i + 1;
                while (j < transformations.size()) {
                    Transformation laterTransform = transformations.get(j);

                    // If we find a concurrent transform, add it to the list and record that in the done flag
                    if (laterTransform.concurrent) {

                        // Concurrent transforms must share a common interpolKind, direction, and framecount
                        Transformation firstTransform = concurrents.get(0);
                        if (
                                firstTransform.interpolKind != laterTransform.interpolKind
                                        ||
                                        firstTransform.direction != laterTransform.direction
                                        ||
                                        firstTransform.frames != laterTransform.frames
                        ) {
                            throw new ArithmeticException("Concurrent transformations must share a common interpolKind " +
                                    "and direction!");
                        }

                        laterTransform.done = true;
                        concurrents.add(laterTransform);
                        j++;
                    }
                    // If we find a non-concurrent transform, then we stop looking. Concurrent transforms MUST
                    // come consecutively in the list. This way, multiple groups of consecutive transforms can be defined
                    else {

                        // Warn the user if they have defined a single "concurrent" transformation
                        if (concurrents.size() < 2) {
                            System.out.println(
                                    "WARNING: isolated concurrent transformation! Do not define transformations " +
                                            "as concurrent if no adjacent transformations are defined as concurrent!");
                        }

                        break;
                    }
                }
            }

            // concurrents now contains all our relevant transformations, which will now perform
            for (Transformation ctr : concurrents) {
                switch (ctr.transformationKind) {
                    // A scaling multiples coords by given values
                    case SCALE -> {
                        targX *= ctr.x;
                        targY *= ctr.y;
                    }

                    // A rotation translates to the origin, rotates by a given amount, and translates back
                    case ROTATE -> {

                        // Translate coordinates to pivot point
                        targX -= ctr.x;
                        targY -= ctr.y;

                        // Rotate
                        double tempTargX = targX * Math.cos(ctr.theta) - targY * Math.sin(ctr.theta);
                        double tempTargY = targX * Math.sin(ctr.theta) - targY * Math.cos(ctr.theta);

                        // Translate back
                        targX = tempTargX + ctr.x;
                        targY = tempTargY + ctr.y;
                    }

                    // A translation simply adds to the coordinates by the given amounts
                    case TRANSLATE -> {
                        targX += ctr.x;
                        targY += ctr.y;
                    }
                }
            }

            // We now have our transformed coords as targX, targY. So we animate according to the common direction and frames
            AnimationInterpolationDirection direction = concurrents.get(0).direction;
            int frames = concurrents.get(0).frames;
            System.out.println("frames: " + frames);
            System.out.println("targ: (" + targX + ", " + targY + "), pos: (" + x + ", " + y + ")");
            double dx = targX - x;
            double dy = targY - y;
            double stepX = dx / (double)frames;
            double stepY = dy / (double)frames;
            System.out.println("d: (" + dx + ", " + dy + "), " + "step: (" + stepX + ", " + stepY + ")");

            for (int animFrame = 0; animFrame < frames; animFrame++) {
                // Linear interpolation
                // TODO: Other AnimationInterpolations

                double interX = (double)animFrame * stepX;
                double interY = (double)animFrame * stepY;

                //System.out.println("Anim coords: " + interX + ", " + interY);
                draw(interX, interY);
                //this.x = interX;
                //this.y = interY;

            }

            // Once animation is done, set the coordinates
            this.x = targX;
            this.y = targY;
        }
    }

    public void setStill() {
        targX = x;
        targY = y;

    }

    public void draw() {
        throw new ArithmeticException("FuncPlot must be interpolated before drawing! Call interpolation().draw() to draw this FuncPlot");
    }

}
