import processing.core.PApplet;

public class MindEye extends PApplet {

    double[] dims = new double[] {800, 800};
    GlobalContainer globals = new GlobalContainer(dims, this);

    Line l;
    Sinusoid s;
    MathObjCol si;
    Polygon square;
    Polygon square2;

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

        square = new Polygon(
                new Point(-50, 50),
                new Point(50, 50),
                new Point(50, -50),
                new Point(-50, -50));


        square.transform(new Translate(40, 40, AnimationInterpolation.LINEAR, AnimationInterpolationDirection.IN, 1200, false),
                0);


        // End
        /*square.transform(new Translate(9999, 9999, AnimationInterpolation.LINEAR, AnimationInterpolationDirection.IN, 0, false),
                0);*/

    }

    @Override
    public void draw() {
        //clear();

        square.animate();
        square.draw(square.x, square.y);

        strokeWeight(0);
        noFill();
        stroke(color((int)((double)frameCount/1200 * 360) , 360, 360));
    }
}
