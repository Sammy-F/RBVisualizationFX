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
 * Handles the connectors between nodes
 */
public class Connector extends Line {

    private NodeCircle parentNode;
    private NodeCircle childNode;

//    public static final int maxConnectors = 8; //maximum before connectors look bad

    public Connector(NodeCircle parentNode, NodeCircle childNode) {

        this.parentNode = parentNode;
        this.childNode = childNode;

        initConnector();

    }

    /**
     * Method for initializing connector between parent and child nodes
     */
    private void initConnector() {

        setStrokeWidth(parentNode.getStroke());

        Insets childInsets = childNode.getInsets();
        Insets parentInsets = parentNode.getInsets();

        //from the center of the bottom
        setStartX(parentInsets.getLeft() + parentNode.getRadius());
        setStartY(parentInsets.getTop() + parentNode.getRadius()*2);

        setEndX(parentInsets.getLeft() + parentNode.getRadius());
        setEndY(parentInsets.getTop() + parentNode.getRadius()*2);

        KeyValue xKey = new KeyValue(this.endXProperty(), childInsets.getLeft() + childNode.getRadius());
        KeyValue yKey = new KeyValue(this.endYProperty(), childInsets.getTop());

        KeyFrame mKeyFrame = new KeyFrame(Duration.millis(600), xKey, yKey);

        Timeline timeline = new Timeline();
        timeline.setCycleCount(1);
        timeline.getKeyFrames().add(mKeyFrame);
        timeline.play();

        setEndX(childInsets.getLeft() + childNode.getRadius());
        setEndY(childInsets.getTop());

        //from the side:
//        if (childNode.getThisNode().isLeftChild()) {
//            setStartX(parentInsets.getLeft());
//            setEndX(childInsets.getLeft() + childNode.getRadius());
//
//            setStartY(parentInsets.getTop() + parentNode.getRadius());
//            setEndY(childInsets.getTop());
//        }
//
//        if (childNode.getThisNode().isRightChild()) {
//            setStartX(parentInsets.getLeft() + parentNode.getRadius()*2);
//            setEndX(childInsets.getLeft() + childNode.getRadius());
//
//            setStartY(parentInsets.getTop() + parentNode.getRadius());
//            setEndY(childInsets.getTop());
//        }


        //from the side of the bottom
//        if (childNode.getThisNode().isLeftChild()) {
//            setStartX(parentInsets.getLeft() + parentNode.getRadius() - parentNode.getRadius()*Math.cos(Math.PI/3));
//            setEndX(childInsets.getLeft() + childNode.getRadius());
//
//            setStartY(parentInsets.getTop() + parentNode.getRadius() + parentNode.getRadius()*Math.sin(Math.PI/3));
//            setEndY(childInsets.getTop());
//        }
//
//        if (childNode.getThisNode().isRightChild()) {
//            setStartX(parentInsets.getLeft() + parentNode.getRadius() + parentNode.getRadius()*Math.cos(Math.PI/3));
//            setEndX(childInsets.getLeft() + childNode.getRadius());
//
//            setStartY(parentInsets.getTop() + parentNode.getRadius() + parentNode.getRadius()*Math.sin(Math.PI/3));
//            setEndY(childInsets.getTop());
//        }


        //adjust angle for lower depths so it doesn't look weird
//        double num = maxConnectors - childNode.getThisNode().getLevel();
//
//        if (num < 1) {
//            num = 1;
//        }
//        double angleX = Math.cos(Math.PI/(2*num));
//        double angleY = Math.sin(Math.PI/(2*num));
//
//        if (childNode.getThisNode().isLeftChild()) {
//            setStartX(parentInsets.getLeft() + parentNode.getRadius() - parentNode.getRadius()*angleX);
//            setEndX(childInsets.getLeft() + childNode.getRadius());
//
//            setStartY(parentInsets.getTop() + parentNode.getRadius() + parentNode.getRadius()*angleY);
//            setEndY(childInsets.getTop());
//        }
//
//        if (childNode.getThisNode().isRightChild()) {
//            setStartX(parentInsets.getLeft() + parentNode.getRadius() + parentNode.getRadius()*angleX);
//            setEndX(childInsets.getLeft() + childNode.getRadius());
//
//            setStartY(parentInsets.getTop() + parentNode.getRadius() + parentNode.getRadius()*angleY);
//            setEndY(childInsets.getTop());
//        }
        // i did maths and i failed :( oh well
    }

    /**
     * Simple method added for clarity - call when nodes are updated so that
     * the connector can readjust
     */
    public void notifyNodesUpdated() { initConnector(); }

    public void setParentNode(NodeCircle parentNode) { this.parentNode = parentNode; }
    public void setChildNode(NodeCircle childNode) { this.childNode = parentNode; }
    public NodeCircle getParentNode() { return this.parentNode; }
    public NodeCircle getChildNode() { return this.childNode; }
}
