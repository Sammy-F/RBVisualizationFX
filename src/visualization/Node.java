package visualization;

/**
 * Handles a Node Object
 */
public class Node {

    public static final int BLACK = 0;
    public static final int RED = 1;

    private int depth;

    private int value;
    private int color;

    public Node(int value, int color) {

        this.value = value;
        this.color = color;

    }


    /**
     * Public methods
     */
    public void setColor(int color) { this.color = color; }
    public int getColor() { return color; }

    public void setValue(int value) { this.value = value; }
    public int getValue() { return value; }

    public void setDepth(int depth) { this.depth = depth; }
    public int getDepth() { return depth; }

}
