/*
    Stores the PApplet, and the screen dimensions. These references are needed across all MathObjs
    Really, this is abject nonsense. I hate Java
 */

import processing.core.PApplet;

public class GlobalContainer {
    public static PApplet pApplet;
    public static double[] dims;

    public GlobalContainer(double[] dims, PApplet pApplet) {
        GlobalContainer.pApplet = pApplet;
        GlobalContainer.dims = dims;
    }
}