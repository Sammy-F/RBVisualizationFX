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
            setStartX(childInsets.getLeft() + childNode.getRadius());  //did not fix these since this never happens yet
            setEndX(parentInsets.getLeft() + parentNode.getRadius());

            setStartY(childInsets.getTop());
            setEndY(parentInsets.getTop() + parentNode.getRadius()*2);
        }
        else {
            setStartX(parentInsets.getLeft() + parentNode.getRadius()*2);  //these look slightly prettier now
            setEndX(childInsets.getLeft() + childNode.getRadius()); //(also yeah, this change is tiny, I will do more!)

            setStartY(parentInsets.getTop() + parentNode.getRadius()*2);
            setEndY(childInsets.getTop() + childNode.getRadius());
        }


    }
}
