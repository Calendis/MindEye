public abstract class Transformation {
    Transform transformationKind;
    double x;
    double y;
    double theta;

    double startX = Double.NaN;
    double startY = Double.NaN;

    AnimationInterpolation interpolKind;
    AnimationInterpolationDirection direction;
    int frames;
    int currentFrame = 0;

    // Consecutive groups of transformations will be done simultaneously if concurrent is true
    boolean concurrent;

    // Useful because we may pass over a "done" transformation if it was done concurrently with a previous transformation
    boolean done = false;

    public Transformation(AnimationInterpolation ai, AnimationInterpolationDirection aid, int f, boolean c) {
        interpolKind = ai;
        direction = aid;
        frames = f;
        concurrent = c;
    }

    public double[] apply(MathObject sfp) {

        double newX = startX;
        double newY = startY;
        if (Double.isNaN(startX)) {
            newX = sfp.x;
            startX = sfp.x;
        }

        if (Double.isNaN(startY)) {
            newY = sfp.y;
            startY = sfp.y;
        }

        // Interpolate arguments
        double progress = (double)currentFrame / frames;
        System.out.println("progress: " + progress);
        if (progress >= 1) {
            done = true;
            return new double[] {newX, newY};
        }

        double interpX = x * progress;
        double interpY = y * progress;
        double interpTheta = theta;// * progress;

        switch (transformationKind) {
            case SCALE -> {

                newX *= interpX;
                newY *= interpY;
            }

            case ROTATE -> {
                // Translate coords to pivot point
                newX -= interpX;
                newY -= interpY;

                // Rotate
                double tempX = newX * Math.cos(interpTheta) + newY * Math.sin(interpTheta);
                double tempY = newX * Math.sin(interpTheta) - newY * Math.cos(interpTheta);

                // Translate back
                newX = tempX + interpX;
                newY = tempY + interpY;
            }

            case TRANSLATE -> {
                newX += interpX;
                newY += interpY;
            }
        }

        currentFrame ++;
        return new double[] {newX, newY};
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

class Scale extends Transformation {
    public Scale(double scaleX, double scaleY, AnimationInterpolation ai, AnimationInterpolationDirection aid, int frames, boolean c) {
        super(ai, aid, frames, c);
        x = scaleX;
        y = scaleY;
        transformationKind = Transform.SCALE;
    }
}

class Translate extends Transformation {
    public Translate(double transX, double transY, AnimationInterpolation ai, AnimationInterpolationDirection aid, int frames, boolean c) {
        super(ai, aid, frames, c);
        x = transX;
        y = transY;
        transformationKind = Transform.TRANSLATE;
    }
}

class Rotate extends Transformation {

    public Rotate(double rotX, double rotY, double theta, AnimationInterpolation ai, AnimationInterpolationDirection aid, int frames, boolean c) {
        super(ai, aid, frames, c);
        x = rotX;
        y = rotY;
        this.theta = theta;
        transformationKind = Transform.ROTATE;
    }
}
