import java.util.ArrayList;

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

    // List of transformations to be animated when animate() is called
    public ArrayList<Transformation> transformations = new ArrayList();

    public double[] pos() {
        return new double[] {x, y};
    }

    public double[] targ() {
        return new double[] {targX, targY};
    }


    // TODO
    public abstract void scale(double x, double y, AnimationInterpolation interpolationKind, AnimationInterpolationDirection direction,
                      int frames, boolean concurrent);

    public abstract void translate(double x, double y, AnimationInterpolation interpolationKind,
                          AnimationInterpolationDirection direction, int frames, boolean concurrent);

    public abstract void rotate(double x, double y, double theta, AnimationInterpolation interpolationKind,
                       AnimationInterpolationDirection direction, int frames, boolean concurrent);

    /*
        Adds respective transformations to the transformation list (transformations)
        TODO: refactor all transformations as matrices
     */
    /*
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
        System.out.println("rotating in MO");
        transformations.add(new Rotate(x, y, theta, interpolationKind, direction, frames, concurrent));
    }

     */

   /*
    Iterate over all queued transformations and animate according to type, frame count, interpolation, etc.
   */
    public abstract void animate();
    /*
    public void animate() {
        for (int i = 0; i < transformations.size(); i++) {
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
                int j = i;
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
                        targX = tempTargX + x;
                        targY = tempTargY + y;
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
            double dx = targX - x;
            double dy = targY = y;
            double stepX = dx / frames;
            double stepY = dy / frames;

            for (int animFrame = 0; animFrame < frames; animFrame++) {
                // Linear interpolation
                // TODO: Other AnimationInterpolations

                double interX = animFrame * stepX;
                double interY = animFrame * stepY;

                System.out.println("drawing something...");
                draw(interX, interY);

            }

            // Once animation is done, set the coordinates
            x = targX;
            y = targY;
        }
    }

     */

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

}

