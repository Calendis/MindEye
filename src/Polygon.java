import java.util.ArrayList;
import java.util.Arrays;

public class Polygon extends MathObjCol{
    public Polygon(Point... ps) {
        ArrayList<Point> points = new ArrayList<>(Arrays.asList(ps));

        for (int i = 0; i < points.size()-1; i++) {
            Line connectingLine = new Line(points.get(i), points.get(i+1));
            connectingLine.setStill();
            constituents.add(connectingLine);
        }

        Line finalLine = new Line(points.get(points.size()-1), points.get(0));
        finalLine.setStill();
        constituents.add(finalLine);
    }
}
