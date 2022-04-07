import java.util.ArrayList;
import java.util.stream.Collectors;

/*
  This class is a tiny bit of a hack. It covers a special case in the hierarchy of math objects
  It re-overrides certain methods defined in MathObjCol and overridden by FuncPlot.
  The reason for this is that FuncPlots need special handling for their transformations,
  since they need to be interpolated at draw-time. "Simple FuncPlots" like lines, points, or
  basic geometric shapes need no such interpolation or special handling, but they may still use
  FuncPlot functionality that wouldn't be appropriate to define in MathObjCol
*/
public abstract class SimpleFuncPlot extends FuncPlot {

    /*public void scale(double x, double y, AnimationInterpolation interpolationKind, AnimationInterpolationDirection direction,
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
    }*/

    // Animates each constituent m
    public void animate() {
        System.out.println("Animating in SFP");

        for (int i = 0; i < transformations.size(); i++) {
            Transformation tr = transformations.get(i);

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
                System.out.println("concurrency!");
                int j = i + 1;

                while (j < transformations.size()) {
                    System.out.println("    j: " + j);
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

                        // fixme: this
                        //laterTransform.done = true;
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
                System.out.println("applying!");
                double[] transTarg =  ctr.apply(this);
                targX = transTarg[0];
                targY = transTarg[1];
            }
        }

        // Filter out completed transformations
        /*ArrayList<Transformation> filteredTransformations = new ArrayList<>(
                transformations.stream().filter(p -> p.done == false).collect(Collectors.toList()));
        transformations = filteredTransformations;*/

        // Once animation is done, set the coordinates
        //draw();
        x = targX;
        y = targY;
    }

    public void setStill() {
        super.setStill();
        for (MathObject c : constituents) {
            c.setStill();
        }
    }
}
