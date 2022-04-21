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

        l = new Line(new Point(0, 90), new Point(50, 50));
        l.setStill();
        l.setAll(100, 0);

        //l.translate(30, 100, AnimationInterpolation.LINEAR, AnimationInterpolationDirection.IN, AnimationDepth.OUTER, 100, false);
        //l.rotate(0, 0, Math.toRadians(360), AnimationInterpolation.LINEAR, AnimationInterpolationDirection.IN, AnimationDepth.OUTER, 100, false);
        //l.translate(30, -100, AnimationInterpolation.LINEAR, AnimationInterpolationDirection.IN, AnimationDepth.OUTER, 100, false);
        //l.rotate(0, 0, Math.toRadians(360), AnimationInterpolation.LINEAR, AnimationInterpolationDirection.IN, AnimationDepth.OUTER, 200, false);

        //l.scale(3, -1, AnimationInterpolation.LINEAR, AnimationInterpolationDirection.IN, AnimationDepth.OUTER, 300, false);
        //l.scale(2, -11.1, AnimationInterpolation.LINEAR, AnimationInterpolationDirection.IN, 40, false);
    }

    @Override
    public void draw() {
        if (!done) {
            //clear();
            strokeWeight(1);
            noFill();
            stroke(color(frameCount%360, 360, 360));

            l.animate();
            l.draw(l.x, l.y);

            //done = true;
        }
    }
}
