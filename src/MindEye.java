import processing.core.PApplet;

public class MindEye extends PApplet {

    double[] dims = new double[] {800, 800};
    GlobalContainer globals = new GlobalContainer(dims, this);

    Line l;

    boolean done = false;

    public static void main(String[] args) {
        PApplet.main("MindEye");
    }

    @Override
    public void settings() {
        size((int)dims[0], (int)dims[1]);
    }

    @Override
    public void setup() {
        frameRate(60);
        surface.setSize((int)dims[0], (int)dims[1]);
        colorMode(HSB, 360);
        clear();

        l = new Line(new Point(0, 90), new Point(50, 90));
        l.setStill();
        //l.rotate(0, 0, Math.toRadians(90), AnimationInterpolation.LINEAR, AnimationInterpolationDirection.IN, 1000, false);
        l.translate(0, -100, AnimationInterpolation.LINEAR, AnimationInterpolationDirection.IN, 10000, false);
        //l.translate(100, 0, AnimationInterpolation.LINEAR, AnimationInterpolationDirection.IN, 94, false);
        //l.translate(-15, -15, AnimationInterpolation.LINEAR, AnimationInterpolationDirection.IN, 84, false);
        //l.translate(0, 2, AnimationInterpolation.LINEAR, AnimationInterpolationDirection.IN, 54, false);
    }

    @Override
    public void draw() {
        if (!done) {
            //clear();
            strokeWeight(1);
            noFill();
            stroke(color(frameCount%360, 360, 360));

            l.animate();

            done = true;
        }
    }
}
