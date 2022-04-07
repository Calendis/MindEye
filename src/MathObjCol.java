import java.util.ArrayList;

/*
  A MathObjCol is simply a grouping of MathObjects, such as a linear interpolation of a function
*/
public class MathObjCol extends MathObject {

    ArrayList<MathObject> constituents;

    public MathObjCol(MathObjCol... mathObjCols) {
        constituents = new ArrayList();

        for (MathObjCol c : mathObjCols) {
            constituents.add(c);
        }
    }

    public void add(MathObjCol c) {
        constituents.add(c);
    }

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
        System.out.println("Rotating in MOC");
        for (MathObject c : constituents) {
            System.out.println("Rotating constituent " + c.getClass());
            c.rotate(x, y, theta, interpolationKind, direction, frames, concurrent);
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

    // Animates each constituent m
    public void animate() {
        System.out.println("Animating in MOC");
        for (MathObject c : constituents) {
            c.animate();
        }

    }

    public void setStill() {
        for (MathObject c : constituents) {
            c.setStill();
        }
    }


}
