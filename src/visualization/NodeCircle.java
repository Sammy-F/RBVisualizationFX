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
public class NodeCircle extends StackPane {

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

        thisText.setFont(new Font((1.5*initRadius)/valueText.length()));
        thisText.setText(valueText);
        thisText.setStroke(Color.WHITE);
        thisText.setFill(Color.WHITE);
    }

    private void initCircle(double initRadius) {

        thisCircle = new Circle();

        thisCircle.setRadius(initRadius);

        thisCircle.setStrokeWidth(initRadius/10);

        if (thisNode.getColor() == Node.BLACK) {
            thisCircle.setFill(Color.DARKSLATEGRAY);
        } else {
            thisCircle.setFill(Color.RED);
        }

//        thisCircle.setFill(Color.TRANSPARENT);

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

    public Node getThisNode() {
        return thisNode;
    }


}
