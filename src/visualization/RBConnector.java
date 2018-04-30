package visualization;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.scene.shape.Line;
import javafx.util.Duration;

import java.lang.Math;


/**
 * Handles the connectors between nodes for the Red-Black Tree visualization
 *
 * Authors: Samantha Fritsche and Katya Gurgel
 */
public class RBConnector extends Line {

    private RBNodeCircle parentNode;
    private RBNodeCircle childNode;

    /**
     * Public constructor for an RBConnector
     *
     * @param parentNode        the parent node the connector touches
     * @param childNode         the child node the connector touches
     */
    public RBConnector(RBNodeCircle parentNode, RBNodeCircle childNode) {

        this.parentNode = parentNode;
        this.childNode = childNode;

        initConnector();

    }

    /**
     * Method for initializing connector between parent and child nodes
     */
    private void initConnector() {

        setStrokeWidth(2);

        Insets childInsets = childNode.getInsets();
        Insets parentInsets = parentNode.getInsets();

        //from the center of the bottom
        setStartX(parentInsets.getLeft() + parentNode.getThisCircle().getRadius());
        setStartY(parentInsets.getTop() + parentNode.getThisCircle().getRadius()*2);

        setEndX(parentInsets.getLeft() + parentNode.getThisCircle().getRadius());
        setEndY(parentInsets.getTop() + parentNode.getThisCircle().getRadius()*2);

        try {
            KeyValue xKey = new KeyValue(this.endXProperty(), childInsets.getLeft() + childNode.getThisCircle().getRadius());
            KeyValue yKey = new KeyValue(this.endYProperty(), childInsets.getTop());

            KeyFrame mKeyFrame = new KeyFrame(Duration.millis(600), xKey, yKey);

            Timeline timeline = new Timeline();
            timeline.setCycleCount(1);
            timeline.getKeyFrames().add(mKeyFrame);
            timeline.play();

            setEndX(childInsets.getLeft() + childNode.getThisCircle().getRadius());
            setEndY(childInsets.getTop());
        } catch (NullPointerException e) {}
    }

    /**
     * Simple method added for clarity - call when nodes are updated so that
     * the connector can readjust
     */
    public void notifyNodesUpdated() { initConnector(); }

    public void setParentNode(RBNodeCircle parentNode) { this.parentNode = parentNode; }
    public void setChildNode(RBNodeCircle childNode) { this.childNode = parentNode; }
}
