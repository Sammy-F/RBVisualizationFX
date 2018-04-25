package visualization;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;


/**
 * Handles the graphical parts of a node
 */
public class NodeCircle extends StackPane {

    //TODO: MAJOR IMPORTANT!!!!!!!!!!!! NEED TO REFACTOR FOR DIFFERENT NODE CLASS. ALSO, LATER WE MAY WANT TO CUT EXCESS STUFF, SINCE WE DON'T DO AS MUCH. WAIT WE STILL NEED THIS FOR BSTVISCONTROLLER. OMG I AM SORRY. MAYBE WE CAN JUST MAKE A COPY OF THIS CLASS FOR THE RBT DRAWING STUFF. IF WE GET SUPER AMBITIOUS WE COULD MAKE BOTH EXTEND THE SAME ABSTRACT CLASS OR SOMETHING WHO KNOWS. SORRY, I MESSED UP SOME CODE...

    private final int HEIGHT_SCALAR = 150;  //the height difference between nodes on different levels
    private final int SHIFT_CONSTANT = 50;  //WHAT IS THIS FOR? ??????? (all caps so I won't forget this Q)

    private VisNode thisNode; //the node tied to this graphical representation

    private Circle thisCircle; //the circle graphic used for a node
    private Text thisText; //text to display the value in the circle graphic

    private double radius; //radius of the circle for node

    double insertionX; //x value where the node will be displayed, this is tied to the level and left/rightness of node
    double xSpacing;  //THIS TOO, WHAT IS THIS FOR? ??????????????

    /**
     * Constructor for the NodeCircle that sets the node, insertionX, xSpacing, and radius; initializes the circle and
     * text in the circle;
     * @param initRadius
     * @param thisNode
     * @param insertionX
     * @param xSpacing
     */
    public NodeCircle(double initRadius, VisNode thisNode, double insertionX, double xSpacing) {

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

//        thisCircle.setFill(Color.TRANSPARENT);

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
