import java.util.ArrayList;
import java.util.Arrays;

/*
  A MathObjCol is simply a grouping of MathObjects, such as a linear interpolation of a function
*/
public class MathObjCol extends MathObject {

    ArrayList<MathObjCol> constituents;

    public MathObjCol(MathObjCol... mathObjCols) {
        constituents = new ArrayList<>();
        constituents.addAll(Arrays.asList(mathObjCols));
    }

    public void add(MathObjCol c) {
        constituents.add(c);
    }

    /*
        Transformations
        TODO: use a single method for all of these, and take a Transformation directly
     */
    public void scale(double x, double y, AnimationInterpolation interpolationKind, AnimationInterpolationDirection direction, AnimationDepth depth,
                      int frames, boolean concurrent) {
        transformations.add(new Scale(x, y, interpolationKind, direction, depth, frames, concurrent));
    }

    public void translate(double x, double y, AnimationInterpolation interpolationKind, AnimationInterpolationDirection direction, int depth,
                          int frames, boolean concurrent) {

        if (depth < 0) {
            throw new ArithmeticException("negative depth!");
        }
        else if (depth == 0) {
            transformations.add(new Translate(x, y, interpolationKind, direction, AnimationDepth.OUTER, frames, concurrent));
        }
        else {
            for (MathObjCol c : constituents) {
                c.translate(x, y, interpolationKind, direction, depth-1, frames, concurrent);
            }
        }
    }

    public void rotate(double x, double y, double theta, AnimationInterpolation interpolationKind, AnimationInterpolationDirection direction, int depth,
                       int frames, boolean concurrent) {
        if (depth < 0) {
            throw new ArithmeticException("rotate negative depth!");
        }
        else if (depth == 0) {
            transformations.add(new Rotate(x, y, theta, interpolationKind, direction, AnimationDepth.OUTER, frames, concurrent));
        }
        else {
            if (constituents.size() == 0) {
                throw new ArithmeticException("animation too deep!");
            }
            for (MathObjCol c : constituents)
                c.rotate(x, y, theta, interpolationKind, direction, depth-1, frames, concurrent);
        }
    }

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

    public void animate() {

        for (MathObjCol c : constituents) {
            c.animate();
        }

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

            // TODO: REMOVE, as depth is now handled earlier via recursion
            switch (ctr.depth) {

                // Transform the coordinates of each constituent object
                case NESTED -> {
                    for (MathObject c : constituents) {

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
                    // TODO: Implement "ALL" animation as both OUTER and NESTED combined
                }
            }
            ctr.advance(1);
        }

        // Depopulate concurrents when transformations have the done flag set
        while(!concurrents.isEmpty() && concurrents.get(0).done) {

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


}
