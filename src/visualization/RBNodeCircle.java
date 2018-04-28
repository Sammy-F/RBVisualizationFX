package visualization;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * Handles the graphical parts of a node
 */
public class RBNodeCircle<T extends Comparable> extends StackPane {

    private Circle thisCircle; //the circle graphic used for a node
    private Text thisText; //text to display the value in the circle graphic

    private double radius; //radius of the circle for node

    double insertionX; //x value where the node will be displayed, this is tied to the level and left/rightness of node
    double insertionY;

    int color;

    String value;

    NumberFormat formatter;

    public RBNodeCircle() {}

    /**
     * Constructor for the NodeCircle that sets the node, insertionX, xSpacing, and radius; initializes the circle and
     * text in the circle;
     * @param initRadius
     * @param insertionX
     */
    public RBNodeCircle(double initRadius, double insertionX, double insertionY, T value, int color) {

        this.insertionX = insertionX;
        this.insertionY = insertionY;

//        if ((double)value % .1 > 0) {
        formatter = new DecimalFormat("#0.0");
//        } else {
//            formatter = new DecimalFormat("#0");
//        }



//        this.value = value.toString();
        this.value = formatter.format(value);

        this.color = color;
        radius = initRadius;

        if (color == RedBlackNode.BLACK) {
//            System.out.println("Node is black");
        } else {
//            System.out.println("Node is red");
        }

        initCircle(initRadius);
        initText(initRadius);

//        super.setLayoutX(initX - initRadius);
//        super.setLayoutY(initY - initRadius);

        getChildren().addAll(thisCircle, thisText);
        setAlignment(thisCircle, Pos.CENTER);
        setAlignment(thisText, Pos.CENTER);

        this.setPadding(new Insets(insertionY, 0, 0, insertionX));

    }

    /**
     * Initializes the text in the circle for the node on the screen
     * @param initRadius
     */
    private void initText(double initRadius) {

        thisText = new Text();

        String valueText = value;

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

        if (color == RedBlackNode.BLACK) {
            thisCircle.setFill(Color.DARKSLATEGRAY);
        } else {
            thisCircle.setFill(Color.ORANGERED);
        }

//        thisCircle.setFill(Color.TRANSPARENT);

    }

    public String getValue() { return value; }

    public Circle getThisCircle() { return thisCircle; }

}