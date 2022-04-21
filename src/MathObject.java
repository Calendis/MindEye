import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

/*
  A MathObject is made up of other, simpler, MathObject inheritors
  It implements scale, translate, and rotate from Transformation
*/
public abstract class MathObject {

    // Defines position
    double x;
    double y;

    // Defines target position
    double targX = x;
    double targY = y;

    // Defines origin position during a transformation
    double originX = x;
    double originY = y;

    int currentFrame = 0;
    int grain = 1000;

    // List of transformations to be animated when animate() is called
    public LinkedList<Transformation> transformations = new LinkedList<>();
    public ArrayList<Transformation> concurrents = new ArrayList<>();

    public double[] pos() {
        return new double[] {x, y};
    }

    public double[] targ() {
        return new double[] {targX, targY};
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

    public abstract void animate();
    public abstract void setStill();

    public abstract void draw();
    public abstract void draw(double x, double y);

    // x position within window, rather than in Cartesian space
    public double windowX() {
        return x + GlobalContainer.dims[0] / 2d;
    }

    // y position within window, rather than in Cartesian space
    public double windowY() {
        return -y + GlobalContainer.dims[1] / 2d;
    }

    public double windowX(double x) {
        return x + GlobalContainer.dims[0] / 2d;
    }

    public double windowY(double y) {
        return -y + GlobalContainer.dims[1] / 2d;
    }

    public void setOrigPos(double x, double y) {
        originX = x;
        originY = y;
    }

    // Consequence of weird design. Maybe I'll be able to do away with this eventually
    public void setAll(double x, double y) {
        this.x = x;
        this.y = y;
        originX = x;
        originY = y;
        targX = x;
        targY = y;
    }
}

