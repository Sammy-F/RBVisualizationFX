package visualization;

import javafx.scene.layout.AnchorPane;
import sun.plugin.javascript.navig.Anchor;

/**
 * Tree Visualization
 */
public class RBTree {

    private Node root;
    private RBTree lastTree;

    public final int RED_INT = 0;
    public final int BLACK_INT = 1;

    public RBTree() {

    }

    public RBTree insertNode(Node node, AnchorPane anchorPane) {

        RBTree newTree = new RBTree();
        newTree.setLastTree(this);
        removeAll(anchorPane, root);

        return newTree;

    }

    public RBTree deleteNode(int value, AnchorPane anchorPane) {
        if (root != null) {
            RBTree newTree = new RBTree(); // Generate a new tree to insert nodes into
            newTree.setLastTree(this); // Set the new tree's last tree to be this tree
            removeAll(anchorPane, root); // Remove from the drawing space all of the nodes in this tree

            return newTree;
        } else {
            System.out.println("No nodes exist.");
            return this;
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

    public void setLastTree(RBTree lastTree) {
        this.lastTree = lastTree;
    }

}
