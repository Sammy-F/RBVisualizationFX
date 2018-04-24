package visualization;

/**
 * Tree Visualization
 */
public class RBTree {

    private Node root;
    private RBTree lastTree;

    public RBTree() {

    }

    /**
     * Handle logic for node insertion and return the new tree
     * @return
     */
    public RBTree insertNode() {
        RBTree newTree = new RBTree();
        newTree.setLastTree(this);

        return newTree;
    }

    /**
     * Handle logic for node deletion and return the new tree
     * @return
     */
    public RBTree deleteNode() {
        RBTree newTree = new RBTree();
        newTree.setLastTree(this);

        return newTree;

    }

    public void setRoot(Node newRoot) { root = newRoot; }
    public Node getRoot() { return root; }

    public void setLastTree(RBTree newLastTree) { lastTree = newLastTree; }
    public RBTree lastTree() { return lastTree; }

}
