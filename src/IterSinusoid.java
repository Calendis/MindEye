public class IterSinusoid extends FuncPlot {
    double amp;
    double freqScale;
    double phaseOffset;
    double iters;

    public IterSinusoid(double x, double y, double a, double f, double p, int iters) {
        this.x = x;
        this.y = y;
        amp = a;
        freqScale = f;
        phaseOffset = p;
        this.iters = iters;
    }

    public Point evaluate(double t) {
        double totalY = 0;

        for (int i = 0; i < iters; i++) {
            double power = Math.pow(2, i);
            totalY += amp * Math.sin(power * freqScale * t + phaseOffset) / power;
        }

        return new Point(t + x, totalY + y);
    }

}
