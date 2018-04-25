package visualization;

/**
 * Handles a Node Object
 *
 * NOTE: I RENAMED THIS BECAUSE I DIDN'T WANT TO ACCIDENTALLY USE NODE INSTEAD OF REDBLACKNODE, VISNODE SEEMED MORE
 * DISTINCTIVE AND IT DOES CONTAIN SOME VISUAL INFO WE DON'T NEED IT TO HAVE ANYMORE FOR RBTREE, FEEL FREE TO CHANGE
 * THE NAME
 */
public class VisNode implements Comparable<VisNode> {

    public static final int BLACK = 0;          //representation of the two colors
    public static final int RED = 1;

    private int level;      //level in tree, 0+

    private double value;   //numerical value of node
    private int color;      //red (1) or black (0)

    private VisNode parent;    //the parent node

    private VisNode leftChild;   //the left child node
    private VisNode rightChild;    //the right child node

    private NodeCircle circle; //graphical representation

    private boolean hasLC;  //has a left child boolean
    private boolean hasRC;  //has a right child boolean

    private boolean isLC;   //is a left child boolean
    private boolean isRC;   //is a right child boolean

    private Connector cToParent;    //connector to parent           (graphical connectors)
    private Connector lCToChild;    //connector to left child
    private Connector rCToChild;    //connector to right child

    /**
     * the constructor, takes and sets the value, color, and level and initializes some boolean variables
     */
    public VisNode(double value, int color, int level) {

        this.value = value;
        this.color = color;
        this.level = level;

        hasLC = false;
        hasRC = false;
        isLC = false;
        isRC = false;

    }


    /*
     * Public methods
     */

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

    /*
     * getters and setters!
     */

    //getters and setters to handle the updating of connectors
    public void setCToParent(Connector newConnector) {
        this.cToParent = newConnector;
    }
    public Connector getCToParent() { return cToParent; }

    public void setLCToChild(Connector newConnector) {
        this.lCToChild = newConnector;
    }
    public Connector getLCToChild() { return lCToChild; }

    public void setRCToChild(Connector newConnector) {
        this.rCToChild = newConnector;
    }
    public Connector getRCToChild() { return rCToChild; }

    public void decrementLevel() { level--; }

    public void incrementLevel() { level++; }

    public double getXSpacingFromLevel(double initSpacing) {
        return initSpacing/(Math.pow(2,(level)));
    }

    //the color
    public void setColor(int color) { this.color = color; }
    public int getColor() { return color; }

    //the value
    public void setValue(double value) { this.value = value; }
    public double getValue() { return value; }

    //the level
    public void setLevel(int level) { this.level = level; }
    public int getLevel() { return level; }


    //set and get parent node
    public void setParent(VisNode p, boolean isLeftChild) {
        parent = p;
        isLC = isLeftChild;
        isRC = !isLeftChild;
    }
    public VisNode getParent() { return parent; }


    //set and get the left and right children
    public void setLeftChild(VisNode lc) {
        if (lc == null) {
            leftChild = null;
            hasLC = false;
        } else {
            leftChild = lc;
            hasLC = true;
        }
    }
    public VisNode getLeftChild() { return leftChild; }

    public void setRightChild(VisNode rc) {
        System.out.println(isRC);
        if (rc == null) {
            rightChild = null;
            hasRC = false;
        } else {
            rightChild = rc;
            hasRC = true;
        }
        System.out.println(isRC);
    }

    public VisNode getRightChild() { return rightChild; }


    //set and get the associated graphical representation
    public void setCircle(NodeCircle nc) { circle = nc; }
    public NodeCircle getCircle() { return circle; }


    //the boolean getters
    public boolean hasLeftChild() { return hasLC; }
    public boolean hasRightChild() { return hasRC; }
    public boolean isLeftChild() { return isLC; }
    public boolean isRightChild() { return isRC; }

    //the boolean setters
    public void setIsLeftChild(boolean isLC) { this.isLC = isLC; }
    public void setIsRightChild(boolean isRC) { this.isRC = isRC; }
    public void setHasLeftChild(boolean hasLeftChild) { this.hasLC = hasLeftChild; }
    public void setHasRightChild(boolean hasRightChild) { this.hasRC = hasRightChild; }


    @Override
    public int compareTo(VisNode other) {
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
        return Double.toString(this.getValue());
    }

}
