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

    private final int HEIGHT_SCALAR = 150;
    private final int SHIFT_CONSTANT = 50;

    private Node thisNode;

    private Circle thisCircle;
    private Text thisText;

    private double radius;

    double insertionX;
    double xSpacing;

    public NodeCircle(double initRadius, Node thisNode, double insertionX, double xSpacing) {

        this.thisNode = thisNode;
        this.insertionX = insertionX;
        this.xSpacing = xSpacing;
        radius = initRadius;

        initCircle(initRadius);
        initText(initRadius);

//        super.setLayoutX(initX - initRadius);
//        super.setLayoutY(initY - initRadius);

        getChildren().addAll(thisCircle, thisText);
        setAlignment(thisCircle, Pos.CENTER);
        setAlignment(thisText, Pos.CENTER);

        this.setPadding(new Insets(thisNode.getLevel()*HEIGHT_SCALAR+20, 20, 20, insertionX));

    }

    private void initText(double initRadius) {

        thisText = new Text();

        String valueText = Double.toString(thisNode.getValue());

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

    public double getValue() {
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

    public void setFillColor(Color newColor) {
        thisCircle.setFill(newColor);
    }

    public double getInsertionX() { return insertionX; }

    public double getxSpacing() { return xSpacing; }

    public void setInsertionX(double insertionX) { this.insertionX = insertionX; }

    public void setXSpacing(double xSpacing) { this.xSpacing = xSpacing; }

    public Text getThisText() { return thisText; }


}
