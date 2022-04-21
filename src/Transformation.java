public abstract class Transformation {
    Transform transformationKind;
    double x;
    double y;
    double theta;

    AnimationInterpolation interpolKind;
    AnimationInterpolationDirection direction;
    AnimationDepth depth;

    int frames;
    double currentFrame = 0;

    // Consecutive groups of transformations will be done simultaneously if concurrent is true
    boolean concurrent;

    // Useful because we may pass over a "done" transformation if it was done concurrently with a previous transformation
    boolean done = false;

    public Transformation(AnimationInterpolation ai, AnimationInterpolationDirection aid, AnimationDepth d,
                          int f, boolean c) {
        interpolKind = ai;
        direction = aid;
        depth = d;
        frames = f;
        concurrent = c;
    }

    public double[] apply(MathObject sfp) {

        double newX = sfp.originX;
        double newY = sfp.originY;

        // Interpolate arguments
        double progress = currentFrame / frames;
        if (progress >= 1) {
            done = true;
            sfp.setOrigPos(sfp.targX, sfp.targY);
            return new double[] {sfp.targX, sfp.targY};
        }

        double interpX = x * progress;
        double interpY = y * progress;
        double interpTheta = theta * progress;

        switch (transformationKind) {
            case SCALE -> {
                newX *= interpX;
                newY *= interpY;
            }

            case ROTATE -> {
                // Translate coords to pivot point
                newX -= x;
                newY -= y;

                // Rotate
                double tempX = newX * Math.cos(interpTheta) + newY * Math.sin(interpTheta);
                double tempY = newX * Math.sin(interpTheta) - newY * Math.cos(interpTheta);

                // Translate back
                newX = tempX + x;
                newY = tempY + y;

            }

            case TRANSLATE -> {
                newX += interpX;
                newY += interpY;
            }
        }
        return new double[] {newX, newY};
    }

    public void advance(double res) {
        currentFrame += res;
    }
}

enum Transform {
    SCALE,
    TRANSLATE,
    ROTATE
}

// Represents positional change through an animation. The derivative of each is the corresponding velocity
enum AnimationInterpolation {
    LINEAR,
    QUAD,
    SIN,
    EXP,
    CUSTOM
}
// Acceleration of the animation. IN is positive, OUT is negative. Does nothing for LINEAR AnimationInterpolation
enum AnimationInterpolationDirection {
    IN,
    OUT
}

enum AnimationDepth {
    NESTED,
    OUTER,
    ALL
}

class Scale extends Transformation {
    public Scale(double scaleX, double scaleY, AnimationInterpolation ai, AnimationInterpolationDirection aid, AnimationDepth d,
                 int frames, boolean c) {
        super(ai, aid, d, frames, c);
        x = scaleX;
        y = scaleY;
        transformationKind = Transform.SCALE;
    }
}

class Translate extends Transformation {
    public Translate(double transX, double transY, AnimationInterpolation ai, AnimationInterpolationDirection aid, AnimationDepth d,
                     int frames, boolean c) {
        super(ai, aid, d, frames, c);
        x = transX;
        y = transY;
        transformationKind = Transform.TRANSLATE;
    }
}

class Rotate extends Transformation {

    public Rotate(double rotX, double rotY, double theta, AnimationInterpolation ai, AnimationInterpolationDirection aid, AnimationDepth d,
                  int frames, boolean c) {
        super(ai, aid, d, frames, c);
        x = rotX;
        y = rotY;
        this.theta = theta;
        transformationKind = Transform.ROTATE;
    }
}
