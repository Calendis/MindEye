import processing.core.PApplet;

public class Sinusoid extends FuncPlot {
    double amp;
    double freqScale;
    double phaseOffset;

    public Sinusoid(double x, double y, double a, double f, double p) {
        this.x = x;
        this.y = y;
        amp = a;
        freqScale = f;
        phaseOffset = p;
    }

    public Point evaluate(double t) {
        return new Point(t + x, amp * Math.sin(freqScale * t + phaseOffset) + y);
    }

}
