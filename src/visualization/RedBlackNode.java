package visualization;

/**
 * Created by katya on 4/24/2018.
 */
public class RedBlackNode<T extends Comparable<T>> {

    public static final int BLACK = 0;          //representation of the two colors
    public static final int RED = 1;

    private int color;

    private T key;

    private RedBlackNode<T> left;

    private RedBlackNode<T> right;

    private RedBlackNode<T> parent;

    private NodeCircle circle; //graphical component!

    public RedBlackNode(){
        color = BLACK;
        parent = null;
        left = null;
        right = null;
    }

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

}
