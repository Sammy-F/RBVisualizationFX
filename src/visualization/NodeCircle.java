package visualization;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;


/**
 * Handles the graphical parts of a node (used for BST visualization)
 *
 * Authors: Samantha Fritsche and Katya Gurgel
 */
public class NodeCircle extends StackPane {

    private final int HEIGHT_SCALAR = 150;  //the height difference between nodes on different levels
    private final int SHIFT_CONSTANT = 50;

    private VisNode thisNode; //the node tied to this graphical representation

    private Circle thisCircle; //the circle graphic used for a node
    private Text thisText; //text to display the value in the circle graphic

    private double radius; //radius of the circle for node

    double insertionX; //x value where the node will be displayed, this is tied to the level and left/rightness of node
    double xSpacing;

    /**
     * Constructor for the NodeCircle that sets the node, insertionX, xSpacing, and radius; initializes the circle and
     * text in the circle;
     * @param initRadius        radius of node
     * @param thisNode          the node associated with this graphical element
     * @param insertionX        the x value of the node's graphical element
     * @param xSpacing          the x spacing
     */
    public NodeCircle(double initRadius, VisNode thisNode, double insertionX, double xSpacing) {

        this.thisNode = thisNode;
        this.insertionX = insertionX;
        this.xSpacing = xSpacing;
        radius = initRadius;

        initCircle(initRadius);
        initText(initRadius);


        getChildren().addAll(thisCircle, thisText);
        setAlignment(thisCircle, Pos.CENTER);
        setAlignment(thisText, Pos.CENTER);

        this.setPadding(new Insets(thisNode.getLevel()*HEIGHT_SCALAR+20, 20, 20, insertionX));

    }

    /**
     * Initializes the text in the circle for the node on the screen
     * @param initRadius
     */
    private void initText(double initRadius) {

        thisText = new Text();

        String valueText = Double.toString(thisNode.getValue());

        thisText.setFont(new Font((1.5*initRadius)/valueText.length()));
        thisText.setText(valueText);
        thisText.setStroke(Color.WHITE);
        thisText.setFill(Color.WHITE);
    }

    /**
     * Initializes the circle for the node graphical representation
     * @param initRadius
     */
    private void initCircle(double initRadius) {

        thisCircle = new Circle();

        thisCircle.setRadius(initRadius);

        thisCircle.setStrokeWidth(initRadius/10);

        if (thisNode.getColor() == VisNode.BLACK) {
            thisCircle.setFill(Color.DARKSLATEGRAY);
        } else {
            thisCircle.setFill(Color.RED);
        }

    }

    /*
     * Public Getters and Setters:
     */

    public double getValue() {
        return thisNode.getValue();
    }

    public double getRadius() {
        return radius;
    }

    public double getStroke() {
        return radius/10;
    }

    public VisNode getThisNode() {
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
