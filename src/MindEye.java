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

        si.transform(
                new Translate(50, 100, AnimationInterpolation.LINEAR, AnimationInterpolationDirection.IN, 1800, false),
                0

        );


        si.transform(
                new Rotate(100, 0, Math.toRadians(360), AnimationInterpolation.LINEAR, AnimationInterpolationDirection.IN, 1800, false),
                2
        );

        si.transform(
                new Rotate(0, 100, Math.toRadians(360*4), AnimationInterpolation.LINEAR, AnimationInterpolationDirection.IN, 1200, true),
                0
        );
        si.transform(
                new Rotate(0, 100, Math.toRadians(-360), AnimationInterpolation.LINEAR, AnimationInterpolationDirection.IN, 1200, true),
                2
        );


    }

    @Override
    public void draw() {
        //clear();

        strokeWeight(0);
        noFill();
        stroke(color((int)((double)frameCount/3000 * 360) , 360, 360));

        si.animate();
        si.draw(si.x, si.y - 100);

        //l.animate();
        //l.draw(l.x, l.y);

        //done = true;
        //frameRate(0);
    }
}
