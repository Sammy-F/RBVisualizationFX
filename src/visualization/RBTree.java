package visualization;

import java.util.ArrayDeque;

/**
 * Tree Visualization
 */
public class RBTree {

    private Node root;

    private ArrayDeque<TreeModification> changes;    //allows for recreation of tree (feel free to do this another way if you find something better)

    public RBTree() {
        changes = new ArrayDeque<>();
    }

    public RBTree copy() {
        //TODO: return an actual copy of this tree
//        return this;

        RBTree copy = new RBTree();

        for (TreeModification change: changes) {
            if (change.isInsert()) {
                copy.insert(change.getVal());
            } else {
                copy.delete(change.getVal());
            }
        }

        return copy;
    }

    /**
     * Handle logic for node insertion and return the new tree
     * @return
     */
    public void insert(double value) {
        //TODO
        TreeModification change = new TreeModification(value, true);
        changes.addLast(change);                                        //keeping track of changes for copying
    }

    /**
     * Handle logic for node deletion and return the new tree
     * @return
     */
    public void delete(double value) {
        //TODO
        TreeModification change = new TreeModification(value, false);
        changes.addLast(change);                                        //keeping track of changes for copying
    }

    public void setRoot(Node newRoot) { root = newRoot; }
    public Node getRoot() { return root; }

}
