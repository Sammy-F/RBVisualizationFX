package visualization;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.Comparator;

/**
 * Handles the graphical parts of a node
 */
public class NodeCircle extends StackPane implements Comparable<NodeCircle> {

    private Node thisNode;

    private Circle thisCircle;
    private Text thisText;

    private double radius;

    public NodeCircle(double initRadius, Node thisNode) {

        this.thisNode = thisNode;
        radius = initRadius;

        initCircle(initRadius);
        initText(initRadius);

//        super.setLayoutX(initX - initRadius);
//        super.setLayoutY(initY - initRadius);

        getChildren().addAll(thisCircle, thisText);
        setAlignment(thisCircle, Pos.CENTER);
        setAlignment(thisText, Pos.CENTER);

    }

    private void initText(double initRadius) {

        thisText = new Text();

        String valueText = Integer.toString(thisNode.getValue());

        thisText.setFont(new Font((3*initRadius)/valueText.length()));
        thisText.setText(valueText);
    }

    private void initCircle(double initRadius) {

        thisCircle = new Circle();

        thisCircle.setRadius(initRadius);

        thisCircle.setStrokeWidth(initRadius/10);

        if (thisNode.getColor() == Node.BLACK) {
            thisCircle.setStroke(Color.BLACK);
        } else {
            thisCircle.setStroke(Color.RED);
        }

        thisCircle.setFill(Color.TRANSPARENT);

    }

    public int getValue() {
        return thisNode.getValue();
    }

    public double getRadius() {
        return radius;
    }

    public double getStroke() {
        return radius/10;
    }

    @Override
    public int compareTo(NodeCircle other) {
        if (this.thisNode.getValue() < other.thisNode.getValue()) {
            return -1;
        } else if (this.thisNode.getValue() == other.thisNode.getValue()) {
            return 0;
        }
        else {
            return 1;
        }
    }

    @Override
    public String toString() {
        return Integer.toString(this.thisNode.getValue());
    }
}
