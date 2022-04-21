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

    // Animates each constituent m
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
                case NESTED -> {
                    System.out.println("applying to " + constituents.size() + " constituents!");
                    for (MathObject c : constituents) {
                        System.out.println("    This constituent is at (" + c.x + ", " + c.y+")");
                        System.out.println("    targ: " + c.targX + ", " + c.targY);
                        double[] transTarg = ctr.apply(c);
                        c.targX = transTarg[0];
                        c.targY = transTarg[1];
                    }
                }

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

        /*for (int i = 0; i < transformations.size(); i++) {
            Transformation tr = transformations.get(i);

            // Skip completed transformations. A transformation can be already done if it was part of a group of
            // concurrent transformations
            if (tr.done) {
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
            else {
                break;
            }

            // concurrents now contains all our relevant transformations, which will now perform
            for (Transformation ctr : concurrents) {
                System.out.println("    doing: " + ctr.transformationKind);

                switch (ctr.depth) {
                    case NESTED -> {
                        System.out.println("applying to " + constituents.size() + " constituents!");
                        for (MathObject c : constituents) {
                            System.out.println("    This constituent is at (" + c.x + ", " + c.y+")");
                            System.out.println("    targ: " + c.targX + ", " + c.targY);
                            double[] transTarg = ctr.apply(c);
                            c.targX = transTarg[0];
                            c.targY = transTarg[1];
                        }
                    }

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
        }*/

        // Once animation is done, set the coordinates
        for (MathObject c : constituents) {
            c.x = c.targX;
            c.y = c.targY;
        }
    }

    public void setStill() {
        super.setStill();
        for (MathObject c : constituents) {
            c.setStill();
        }
    }
}
