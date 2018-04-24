package visualization;

import javafx.scene.layout.AnchorPane;
import sun.plugin.javascript.navig.Anchor;

/**
 * Tree Visualization
 */
public class RBTree {

    Node root;

    public final int RED_INT = 0;
    public final int BLACK_INT = 1;

    public RBTree() {

    }

    public void insertNode(Node node) {

    }

    public void deleteNode(int value, RBTree existingTree) {
        if (existingTree.getRoot() != null) {
            RBTree newTree = new RBTree();
        } else {
            System.out.println("No nodes exist.");
        }
    }

    /**
     * Remove all nodes from the drawing space to be redrawn
     */
    public void removeAll(AnchorPane anchorPane, Node root) {
        if (root != null) {

            anchorPane.getChildren().remove(root.getCircle());

            if (root.hasRightChild()) {
                removeAll(anchorPane, root.getRightChild());
            } else if (root.hasLeftChild()) {

                removeAll(anchorPane, root.getLeftChild());

            }
        }
    }

    public Node getRoot() {
        return root;
    }

}
