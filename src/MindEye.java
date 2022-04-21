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


        s = new Sinusoid(0, 0, 100, 0.1, 0);
        s.setStill();

        si = s.interpolation(-100, 100, 0.1);

        si.translate(100, 100, AnimationInterpolation.LINEAR, AnimationInterpolationDirection.IN, AnimationDepth.NESTED, 200, true);
        //si.rotate(0, -50, Math.toRadians(360), AnimationInterpolation.LINEAR, AnimationInterpolationDirection.IN, AnimationDepth.OUTER, 500, true);

        //l.rotate(0, -50, Math.toRadians(360), AnimationInterpolation.LINEAR, AnimationInterpolationDirection.IN, AnimationDepth.NESTED, 500, true);
    }

    @Override
    public void draw() {
        //clear();

        strokeWeight(0);
        noFill();
        stroke(color((int)((double)frameCount/2400 * 360) , 360, 360));

        si.animate();
        si.draw(si.x, si.y);

        //l.animate();
        //l.draw(l.x, l.y);

        //done = true;
        //frameRate(0);
    }
}
