package visualization;

/**
 * Handles a Node Object
 */
public class Node implements Comparable<Node> {

    public static final int BLACK = 0;
    public static final int RED = 1;

    private int level;

    private int value;
    private int color;

    private Node parent;

    private Node leftChild;
    private Node rightChild;

    private NodeCircle circle; //reversed the relationship between node and circle

    private boolean hasLC;
    private boolean hasRC;
    private boolean hasP;

    public Node(int value, int color, int level) {

        this.value = value;
        this.color = color;
        this.level = level;

        hasLC = false;
        hasRC = false;
        hasP = false;

    }


    /**
     * Public methods
     */
    public void setColor(int color) { this.color = color; }
    public int getColor() { return color; }

    public void setValue(int value) { this.value = value; }
    public int getValue() { return value; }

    public void setLevel(int level) { this.level = level; }
    public int getLevel() { return level; }


    public void setParent(Node p) {
        parent = p;
        hasP = true;
    }
    public Node getParent() { return parent; }

    public void setLeftChild(Node lc) {
        leftChild = lc;
        hasLC = true;
    }
    public Node getLeftChild() { return leftChild; }

    public void setRightChild(Node rc) {
        rightChild = rc;
        hasRC = true;
    }
    public Node getRightChild() { return rightChild; }

    public void setCircle(NodeCircle nc) { circle = nc; }
    public NodeCircle getCircle() { return circle; }

    public boolean hasLeftChild() { return hasLC; }
    public boolean hasRightChild() { return hasRC; }
    public boolean hasParent() { return hasP; }


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

    @Override
    public String toString() {
        return Integer.toString(this.getValue());
    }

}
