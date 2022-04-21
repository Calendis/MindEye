import processing.core.PApplet;

public class MindEye extends PApplet {

    double[] dims = new double[] {800, 800};
    GlobalContainer globals = new GlobalContainer(dims, this);

    Line l;
    Sinusoid s;
    MathObjCol si;

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
        frameRate(6000);
        surface.setSize((int)dims[0], (int)dims[1]);
        colorMode(HSB, 360);
        clear();

        l = new Line(new Point(0, -100), new Point(-0, 100));
        l.setStill();

        s = new Sinusoid(0, 0, 50, 0.08, 0);
        s.setStill();

        si = s.interpolation(-50, 50, 0.05);
        si.setStill();

        //si.translate(100, 100, AnimationInterpolation.LINEAR, AnimationInterpolationDirection.IN, AnimationDepth.NESTED, 1200, true);
        //si.rotate(0, 0, Math.toRadians(360), AnimationInterpolation.LINEAR, AnimationInterpolationDirection.IN, AnimationDepth.OUTER, 1200, true);
        //si.translate(9999, 9999, AnimationInterpolation.LINEAR, AnimationInterpolationDirection.IN, AnimationDepth.OUTER, 0, false);

        //l.translate(100, 100, AnimationInterpolation.LINEAR, AnimationInterpolationDirection.IN, 0, 1200, true);
        si.translate(50, 400, AnimationInterpolation.LINEAR, AnimationInterpolationDirection.IN, 0, 2400, false);
        si.rotate(100, 0, Math.toRadians(360), AnimationInterpolation.LINEAR, AnimationInterpolationDirection.IN, 0, 2400, true);
        si.rotate(0, 100, Math.toRadians(360*3), AnimationInterpolation.LINEAR, AnimationInterpolationDirection.IN, 2, 2400, true);
        si.translate(9999, 9999, AnimationInterpolation.LINEAR, AnimationInterpolationDirection.IN, 0, 0, false);
    }

    @Override
    public void draw() {
        //clear();

        strokeWeight(0);
        noFill();
        stroke(color((int)((double)frameCount/2400 * 360) , 360, 360));

        si.animate();
        si.draw(si.x, si.y-200);

        //l.animate();
        //l.draw(l.x, l.y);

        //done = true;
        //frameRate(0);
    }
}
