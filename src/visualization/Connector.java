package visualization;

import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.scene.shape.Line;

public class Connector extends Line {

    private NodeCircle parentNode;
    private NodeCircle childNode;

    public Connector(NodeCircle parentNode, NodeCircle childNode) {

        this.parentNode = parentNode;
        this.childNode = childNode;

        initConnector();

    }

    private void initConnector() {

        setStrokeWidth(parentNode.getStroke());

        Insets childInsets = childNode.getInsets();
        Insets parentInsets = parentNode.getInsets();

        if (childNode.getLayoutX() < parentNode.getLayoutX()) {
            setStartX(childInsets.getLeft() + childNode.getRadius());
            setEndX(parentInsets.getLeft() + parentNode.getRadius());

            setStartY(parentInsets.getTop() + parentNode.getRadius()*2);
            setEndY(childInsets.getTop());
        }
        else {
            setStartX(parentInsets.getLeft() + parentNode.getRadius());
            setEndX(childInsets.getLeft() + childNode.getRadius());

            setStartY(childInsets.getTop() + childNode.getRadius()*2);
            setEndY(parentInsets.getTop());
        }


    }
}
