package view.media;

import java.util.Iterator;

/**
 * Place some nodes on the permiter of a circle, and rotate each node so that it
 * points toward the center of the circle.
 */
public class ArcNodes implements Iterable<double[]> {
    private int n;
    private double r, gap, elementW;

    /**
     * 
     * @param n         The number of elements that will be translated.
     * @param curvature The more is the curvature, the more elements' coordinates
     *                  tend to look like an horizontal line. The less is the
     *                  curvature, the more elements' coordinates tend to form
     *                  like a small circle.
     * @param gap       The gap between each element.
     */
    public ArcNodes(int n, double r, double gap, double elementW) {
        this.n = n;
        this.r = r;
        this.gap = gap;
        this.elementW = elementW;
    }

    @Override
    public Iterator<double[]> iterator() {
        return new ArcNodesIterator(n, r, gap, elementW);
    }
}

class ArcNodesIterator implements Iterator<double[]> {
    private int i = 1;
    private int n;
    private double x0, y0, r;
    private double gap, elementW;

    public ArcNodesIterator(int n, double r, double gap, double elementW) {
        /* _________________ */

        this.n = n;
        this.x0 = 0.0;
        this.y0 = r;
        this.r = r;
        this.gap = gap;
        this.elementW = elementW;
    }

    @Override
    public boolean hasNext() {
        return i <= n;
    }

    @Override
    public double[] next() {
        int j = i - (n % 2 == 0 ? n / 2 : (n + 1) / 2);
        double x = (gap + elementW / 2) * j;
        double y = y0 - Math.sqrt(-Math.pow(x - x0, 2) + Math.pow(r, 2));
        double angle = Math.toDegrees(Math.atan2(-y0, x - x0) - Math.atan2(-y0, -x0));

        i++;

        return new double[] { x, y, angle };
    }
}


// private double findX(Node node) {
//     int i = getChildren().indexOf(node) + 1;
//     int n = getChildren().size();
//     int j = i - (n % 2 == 0 ? n / 2 : (n + 1) / 2);
//     return (gap + elementW / 2) * j;
// }

// private void adjustY(Node node) {
//     double x = findX(node);
//     double y = y0 - Math.sqrt(-Math.pow(x - x0, 2) + Math.pow(radius, 2));
//     node.setTranslateY(y);
// }

// private void pointCenter(Node node) {
//     double x = findX(node);
//     double angle = Math.toDegrees(Math.atan2(-y0, x - x0) - Math.atan2(-y0, -x0));
//     node.setRotate(angle);
// }
