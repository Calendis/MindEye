public class Line extends FuncPlot {

    double m;
    double b;

    // Defines boundaries for drawing the line
    //Point cullStart;
    //Point cullEnd;

    // Line created from two points
    public Line(Point p1, Point p2) {
        add(p1);
        add(p2);
        //x = (p1.x + p2.x) / 2;
        //y = (p1.y + p2.y) / 2;
        //setCullPoints(p1, p2);

        // Find slope-intercept
        m = (y(1) - y(0)) / (x(1) - x(0));
        b = y(0) - m * x(0);
    }

    // Line created from slope and intercept
    public Line(double m, double b) {
        this.m = m;
        this.b = b;
    }

    public Point evaluate(double t) {

        // Parameterize
        double xt = m * t + b;
        double yt = (t - b) / m;

        return new Point(xt, yt);
    }

    public double x(int i) {
        return constituents.get(i).x;
    }

    public double y(int i) {
        return constituents.get(i).y;
    }

    public double minX() {
        return Math.min(x(0), x(1));
    }

    public double maxX() {
        return Math.max(x(0), x(1));
    }

    public double minY() {
        return Math.min(y(0), y(1));
    }

    public double maxY() {
        return Math.max(y(0), y(1));
    }

    // Sets the endpoints for drawing the line
    public void setCullPoints(Point p1, Point p2) {
        constituents.clear();
        add(p1);
        add(p2);
    }

    // Calculates the window boundary as the outer cull points
    public void findOuterCullPoints() {

        Point p1;
        Point p2;

        double windowMinX = -GlobalContainer.dims[0] / 2;
        double windowMinY = -GlobalContainer.dims[1] / 2;
        double windowMaxX = GlobalContainer.dims[0] / 2;
        double windowMaxY = GlobalContainer.dims[1] / 2;

        // Set x and y to minimum and maximum and calculate the respective corresponding x and y
        double xAtYMin = (windowMinY - m) / b;
        double xAtYMax = (windowMaxY - m) / b;

        double yAtXMin = m * windowMinX + b;
        double yAtXMax = m * windowMaxX + b;

        if (xAtYMin >= windowMinX && xAtYMin <= windowMaxX) {
            p1 = new Point(xAtYMin, windowMinY);
        } else {
            p1 = new Point(windowMinX, yAtXMin);
        }


        if (xAtYMax >= windowMinX && xAtYMax <= windowMaxX) {
            p2 = new Point(xAtYMax, windowMaxY);
        } else {
            p2 = new Point(windowMaxX, yAtXMax);
        }

        setCullPoints(p1, p2);

    }

    // Rather than wastefully plotting parametric pixels on a line, we can use ...
    // ... built-in line-drawing functionality
    public void draw() {

        if (constituents.get(0) == null || constituents.get(1) == null) {
            findOuterCullPoints();
        }

        float windowCullStartX = (float) (constituents.get(0).x + GlobalContainer.dims[0] / 2);
        float windowCullStartY = (float) (-constituents.get(0).y + GlobalContainer.dims[1] / 2);
        float windowCullEndX = (float) (constituents.get(1).x + GlobalContainer.dims[0] / 2);
        float windowCullEndY = (float) (-constituents.get(1).y + GlobalContainer.dims[1] / 2);

        GlobalContainer.pApplet.line(windowCullStartX, windowCullStartY, windowCullEndX, windowCullEndY);
    }

    public void draw(double x, double y) {

        if (constituents.get(0) == null || constituents.get(1) == null) {
            findOuterCullPoints();
        }

        float windowCullStartX = (float) (constituents.get(0).x + x + GlobalContainer.dims[0] / 2);
        float windowCullStartY = (float) (-constituents.get(0).y - y + GlobalContainer.dims[1] / 2);
        float windowCullEndX = (float) (constituents.get(1).x + x + GlobalContainer.dims[0] / 2);
        float windowCullEndY = (float) (-constituents.get(1).y - y + GlobalContainer.dims[1] / 2);

        GlobalContainer.pApplet.line(windowCullStartX, windowCullStartY, windowCullEndX, windowCullEndY);

    }

}
