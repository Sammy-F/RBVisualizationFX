package visualization;

/**
 * Handles a Node Object
 */
public class Node implements Comparable<Node> {

    public static final int BLACK = 0;
    public static final int RED = 1;

    private int level;

    private double value;
    private int color;

    private Node parent;

    private Node leftChild;
    private Node rightChild;

    private NodeCircle circle; //reversed the relationship between node and circle

    private boolean hasLC;
    private boolean hasRC;

    private boolean isLC;
    private boolean isRC;

    private Connector cToParent;
    private Connector lCToChild;
    private Connector rCToChild;

    public Node(double value, int color, int level) {

        this.value = value;
        this.color = color;
        this.level = level;

        hasLC = false;
        hasRC = false;
        isLC = false;
        isRC = false;

    }


    /**
     * Public methods
     */
    public void setColor(int color) { this.color = color; }
    public int getColor() { return color; }

    public void setValue(double value) { this.value = value; }
    public double getValue() { return value; }

    public void setLevel(int level) { this.level = level; }
    public int getLevel() { return level; }


    public void setParent(Node p, boolean isLeftChild) {
        parent = p;
        isLC = isLeftChild;
        isRC = !isLeftChild;
    }
    public Node getParent() { return parent; }

    public void setLeftChild(Node lc) {
        if (lc == null) {
            leftChild = null;
            hasLC = false;
        } else {
            leftChild = lc;
            hasLC = true;
        }
    }
    public Node getLeftChild() { return leftChild; }

    public void setRightChild(Node rc) {
        if (rc == null) {
            rightChild = null;
            hasRC = false;
        } else {
            rightChild = rc;
            hasRC = true;
        }
    }

    public Node getRightChild() { return rightChild; }

    public void setCircle(NodeCircle nc) { circle = nc; }
    public NodeCircle getCircle() { return circle; }

    public boolean hasLeftChild() { return hasLC; }
    public boolean hasRightChild() { return hasRC; }
    public boolean isLeftChild() { return isLC; }
    public boolean isRightChild() { return isRC; }


    @Override
    public int compareTo(Node other) {
        if (this.getValue() < other.getValue()) {
            return -1;
        } else if (this.getValue() == other.getValue()) {
            return 0;
        }
        else {
            return 1;
        }
    }

    public void notifyConnectorsUpdated() {
        if (cToParent != null) {
            cToParent.notifyNodesUpdated();
        }
        if (lCToChild != null) {
            lCToChild.notifyNodesUpdated();
        }
        if (rCToChild != null) {
            rCToChild.notifyNodesUpdated();
        }
    }

    @Override
    public String toString() {
        return Double.toString(this.getValue());
    }

    public void setCToParent(Connector newConnector) {
        this.cToParent = newConnector;
    }
    public Connector getCToParent() { return cToParent; }

    public void setlCToChild(Connector newConnector) {
        this.lCToChild = newConnector;
    }
    public Connector getLCToChild() { return lCToChild; }

    public void setRCToChild(Connector newConnector) {
        this.rCToChild = newConnector;
    }
    public Connector getRCToChild() { return rCToChild; }

    public void decrementLevel() { level--; }

    public void incrementLevel() { level++; }

}
