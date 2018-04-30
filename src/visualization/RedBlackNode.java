package visualization;

/**
 * Class for type T Red-Black Tree Nodes
 *
 * Authors: Samantha Fritsche and Katya Gurgel
 */
public class RedBlackNode<T extends Comparable<T>> {

    public static final int BLACK = 0;          //representation of the two colors
    public static final int RED = 1;

    private int color;  //node color

    private T key;  //key value

    private RedBlackNode<T> left;   //left child node

    private RedBlackNode<T> right;  //right child node

    private RedBlackNode<T> parent; //parent node

    private NodeCircle circle; //graphical component!

    /**
     * Public constructor for RedBlackNode that does not take a key and sets every parent/child to null
     *
     * (Used to create NIL)
     */
    public RedBlackNode(){
        color = BLACK;
        parent = null;
        left = null;
        right = null;
    }

    /**
     * Public constructor for RedBlackNode that does take the key and sets the key of this node as well as
     * setting parent/children to null
     *
     * @param key
     */
    public RedBlackNode(T key){
        this();
        this.key = key;
    }


    // public getters and setters:

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public T getKey() {
        return key;
    }

    public void setKey(T key) {
        this.key = key;
    }

    /**
     * Getter for the left child node of a node.
     *
     * NOTE: THIS MAY RETURN NIL! THERE MUST BE A CHECK FOR THIS
     * WHENEVER CALLING THIS GETTER!!!!!! (see rbt class for this!)
     *
     * @return          the left child node of this node
     */
    public RedBlackNode<T> getLeft() {
        return left;
    }

    public void setLeft(RedBlackNode<T> left) {
        this.left = left;
    }

    /**
     * Getter for the right child node of a node.
     *
     * NOTE: THIS MAY RETURN NIL! THERE MUST BE A CHECK FOR THIS
     * WHENEVER CALLING THIS GETTER!!!!!! (see rbt class for this!)
     *
     * @return          the right child node of this node
     */
    public RedBlackNode<T> getRight() {
        return right;
    }

    public void setRight(RedBlackNode<T> right) {
        this.right = right;
    }

    /**
     * Getter for the parent node of a node.
     *
     * NOTE: THIS MAY RETURN NIL! THERE MUST BE A CHECK FOR THIS
     * WHENEVER CALLING THIS GETTER!!!!!! (see rbt class for this!)
     *
     * @return          the parent node of this node
     */
    public RedBlackNode<T> getParent() {
        return parent;
    }

    public void setParent(RedBlackNode<T> parent) {
        this.parent = parent;
    }

    public NodeCircle getCircle() {
        return circle;
    }

    public void setCircle(NodeCircle circle) {
        this.circle = circle;
    }

    /**
     * Public toString for an individual node
     *
     * @return
     */
    public String toString() {
        String colorStr;
        if (color == BLACK) {
            colorStr = "Black,";
        } else {
            colorStr = "Red,";
        }
        String toStr = "Node color: " + colorStr + " Node value: " + key.toString();
        try {
            toStr += ", Left Child Value: " + left.getKey().toString();
        } catch (NullPointerException e) { }

        try {
            toStr += ", Right Child Value: " + right.getKey().toString();
        } catch (NullPointerException e) {}

        return toStr;
    }

}
