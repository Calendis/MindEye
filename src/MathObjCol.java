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

    public void transform(Transformation t, int depth, int ignoreUpper) {

        if (depth < 0) {
            throw new ArithmeticException("Negative depth!");
        }
        else if (depth == 0) {
            transformations.add(t);
        }
        else {
            //transformations.add(t);
            if (constituents.size() < depth) {
                throw new ArithmeticException("animation too deep!");
            }

            for (MathObjCol c : constituents) {
                c.transform(t.copy(), depth-1, ignoreUpper-1);
            }
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

        if (constituents.size() > 0) {
            for (MathObjCol c : constituents) {
                c.animate();
            }
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

            double[] transTarg = ctr.apply(this);
            targX = transTarg[0];
            targY = transTarg[1];

            x = targX;
            y = targY;
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
