package view.animations;

import java.util.List;

import javafx.scene.Node;
import javafx.scene.layout.HBox;

public class ArcNodes {
    private double nodesGap, containerWidth;
    private double nodeWidth = 150.0;
    private double containerX = 0;
    private double containerY = 0;
    private double centerX, centerY, radius; // Change the center to modify the hand curvature
    private List<Node> nodes;
    private HBox container;

    public ArcNodes(double nodesGap, double containerWidth, List<Node> nodes, HBox container) {
        this.nodesGap = nodesGap;
        this.containerWidth = containerWidth;
        this.nodes = nodes;
        this.container = container;
    }

    public void play(double curvature) {
        assignCenter(curvature);
        adjustCards();
    }

    private void adjustnodesGap() {
        container.setSpacing(containerWidth / nodes.size() - nodeWidth);
    }

    private void assignCenter(double y) {
        centerX = containerX;
        centerY = y;
        radius = y;
    }

    private double getNodeX(Node node) {
        int position = nodes.indexOf(node) + 1;
        int totalCards = nodes.size();
        // TODO la calcola male la posizione, infatti il problema della curvatura si
        // trova qua
        position = position - (totalCards % 2 == 0 ? totalCards / 2 : (totalCards + 1) / 2);
        return (nodesGap + nodeWidth / 2) * position;
    }

    private double getNodeY(Node node) {
        return containerY;
    }

    private double findOnCircleY(Node node) {
        // (x-x0)^2 + (y-y0)^2 = r^2 -> y = y0 +- sqrt{-(x-x0)^2 + r^2}
        double nodeX = getNodeX(node);
        double sol = centerY - Math.sqrt(-Math.pow(nodeX - centerX, 2) + Math.pow(radius, 2));
        return sol; // TODO scegliere la soluzione corretta
    }

    // TODO Fare caching dei valori così da non doverli ripetere e metterli in una
    // variabile final.
    private void adjustY(Node node) {
        node.setTranslateY(findOnCircleY(node) - getNodeY(node));
    }

    private void pointCenter(Node node) {
        double angle1 = Math.atan2(containerY - centerY, containerX - centerX);
        double angle2 = Math.atan2(getNodeY(node) - centerY, getNodeX(node) - centerX);
        node.setRotate(Math.toDegrees(angle2 - angle1));
    }

    // TODO fare un metodo publico pointCenter che ti curva la carta in modo che punti il centro dell'oggetto ArcNodes

    private void adjustCards() {
        adjustnodesGap();
        for (Node node : nodes) {
            adjustY(node);
            pointCenter(node);
        }
    }
}
