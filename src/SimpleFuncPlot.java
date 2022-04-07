import java.util.ArrayList;

/*
  This class is a tiny bit of a hack. It covers a special case in the hierarchy of math objects
  It re-overrides certain methods defined in MathObjCol and overridden by FuncPlot.
  The reason for this is that FuncPlots need special handling for their transformations,
  since they need to be interpolated at draw-time. "Simple FuncPlots" like lines, points, or
  basic geometric shapes need no such interpolation or special handling, but they may still use
  FuncPlot functionality that wouldn't be appropriate to define in MathObjCol
*/
public abstract class SimpleFuncPlot extends FuncPlot {

    public void scale(double x, double y, AnimationInterpolation interpolationKind, AnimationInterpolationDirection direction,
                      int frames, boolean concurrent) {
        for (MathObject c : constituents) {
            c.scale(x, y, interpolationKind, direction, frames, concurrent);
        }
    }

    // Translate all consituent objects
    public void translate(double x, double y, AnimationInterpolation interpolationKind,
                          AnimationInterpolationDirection direction, int frames, boolean concurrent) {
        for (MathObject c : constituents) {
            c.translate(x, y, interpolationKind, direction, frames, concurrent);
        }
    }

    // Rotate all constituent objects

    public void rotate(double x, double y, double theta, AnimationInterpolation interpolationKind,
                       AnimationInterpolationDirection direction, int frames, boolean concurrent) {

        System.out.println("rotating in SFP...");
        for (MathObject c : constituents) {
            System.out.println("rotating constituent " + c.getClass());
            c.rotate(x, y, theta, interpolationKind, direction, frames, concurrent);
        }
    }



    /*
    public void draw() {
        for (MathObject c : constituents) {
            c.draw();
        }
    }


    public void draw(double x, double y) {
        for (MathObject c : constituents) {
            c.draw(x, y);
        }
    }

     */



    // Animates each constituent m
    public void animate() {
        System.out.println("Animating in SFP");
        for (MathObject c : constituents) {
            System.out.println("animating c: " + c.getClass());
            for (int i = 0; i < c.transformations.size(); i++) {
                Transformation tr = c.transformations.get(i);

                // Skip completed transformations. A transformation can be already done if it was part of a group of
                // concurrent transformations
                if (tr.done) {
                    System.out.println("Skipping completed transformation.");
                    continue;
                }

                // All transformations to be animated simultaneously
                // Often, this may be populated with only one element!
                ArrayList<Transformation> concurrents = new ArrayList<>();
                concurrents.add(tr);

                // If the transformation is set to be done concurrently, it must be concurrently WITH something
                // So we look ahead for more transformations set to be concurrent
                if (tr.concurrent) {
                    System.out.println("Concurrent detected...");
                    int j = i + 1;
                    System.out.println("i: " + i + ", transformations len: " + c.transformations.size());
                    while (j < c.transformations.size()) {
                        System.out.println("    j: " + j);
                        Transformation laterTransform = c.transformations.get(j);

                        // If we find a concurrent transform, add it to the list and record that in the done flag
                        if (laterTransform.concurrent) {

                            System.out.println("Later concurrency detected!");

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
                            else {
                                System.out.println("concurrency successful!");
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
                            c.targX *= ctr.x;
                            c.targY *= ctr.y;
                        }

                        // A rotation translates to the origin, rotates by a given amount, and translates back
                        case ROTATE -> {

                            // Translate coordinates to pivot point
                            c.targX -= ctr.x;
                            c.targY -= ctr.y;

                            // Rotate
                            double tempTargX = c.targX * Math.cos(ctr.theta) - c.targY * Math.sin(ctr.theta);
                            double tempTargY = c.targX * Math.sin(ctr.theta) - c.targY * Math.cos(ctr.theta);

                            // Translate back
                            c.targX = tempTargX + ctr.x;
                            c.targY = tempTargY + ctr.y;
                        }

                        // A translation simply adds to the coordinates by the given amounts
                        case TRANSLATE -> {
                            c.targX += ctr.x;
                            c.targY += ctr.y;
                        }
                    }
                }

                // We now have our transformed coords as targX, targY. So we animate according to the common direction and frames
                AnimationInterpolationDirection direction = concurrents.get(0).direction;
                int frames = concurrents.get(0).frames;

                double dx = c.targX - c.x;
                double dy = c.targY - c.y;
                double stepX = dx / (double)frames;
                double stepY = dy / (double)frames;
                System.out.println("    d: (" + dx + ", " + dy + "), " + "step: (" + stepX + ", " + stepY + ")");

                for (int animFrame = 0; animFrame < frames; animFrame++) {
                    // Linear interpolation
                    // TODO: Other AnimationInterpolations

                    double interX = (double)animFrame * stepX;
                    double interY = (double)animFrame * stepY;

                    //System.out.println("Anim coords: " + interX + ", " + interY);
                    draw(interX, interY);
                    GlobalContainer.pApplet.redraw();
                    //c.x = interX;
                    //c.y = interY;

                }

                // Once animation is done, set the coordinates
                //c.x = c.targX;
                //c.y = c.targY;
                System.out.println("x: " + c.x + ", tx: " + c.targX);
                System.out.println("y: " + c.y + ", ty: " + c.targY);

            }
        }

    }

    public void setStill() {
        super.setStill();
        for (MathObject c : constituents) {
            c.setStill();
        }
    }
}
