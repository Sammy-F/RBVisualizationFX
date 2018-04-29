package visualization;

import java.util.ArrayDeque;
import java.util.ArrayList;

/**
 * Red-Black Tree
 */
public class RBTree<T extends Comparable<T>> {

//    private ArrayList<ModificationLog> mLogs;

    private ModificationLog mLog;

    private ArrayDeque<TreeModification<T>> changes;    //allows for recreating tree

    private RedBlackNode<T> nil = new RedBlackNode<T>();
    private RedBlackNode<T> root;

    //PUBLIC STUFF YOU CAN USE YAY:

    /**
     * Constructor for an empty Red-Black Tree,
     *
     * Sets the root and its p/lc/rc all to nil,
     *
     * And initializes a running list of all modifications
     * made to help with the copying process needed for our
     * visualization stepping ability
     */
    public RBTree() {
        root = nil;

        root.setLeft(nil);
        root.setRight(nil);
        root.setParent(nil);

        changes = new ArrayDeque<>();

        mLog = new ModificationLog();
    }

    /**
     * Generates and returns a copy of the tree; note that
     * the copy is a reference to a separate tree that is
     * identical in features to this one
     *
     * @return  RBTree of type T, a copy of this one
     */
    public RBTree<T> copy() {

        RBTree<T> copy = new RBTree<T>();

        for (TreeModification<T> change: changes) {
            if (change.isInsert()) {
                copy.insert(change.getKey());
            } else {
                copy.delete(change.getKey());
            }
        }

        ModificationLog newLog = new ModificationLog();
        for (LogModification mod : mLog.getLogArray()) {
            newLog.addChange(mod);
        }

//        mLogs.add(newLog);
        copy.setLog(newLog);

        return copy;
    }

    /**
     * Handle logic for node insertion and return the new tree
     */
    public void insert(T key) {

        mLog.addChange(LogModification.INSERTION, (Double) key);

        RedBlackNode<T> alreadyExists = findKeyNode(key);

        if (alreadyExists == nil) {

            TreeModification<T> change = new TreeModification<T>(key, true);
            changes.addLast(change);                                        //keeping track of changes for copying

            RedBlackNode<T> node = new RedBlackNode<>(key);

            insertNode(node);

            //DEBUG:
            System.out.println("INSERT: " + key.toString() + "\n" + this.toString() + "\n\n"); //TODO: comment out when no longer needed for debugging

        } else {
            mLog.addChange(LogModification.NODEEXISTS, -1);
        }
    }

    /**
     * Handle logic for node deletion and return the new tree
     */
    public void delete(T key) {

        TreeModification<T> change = new TreeModification<>(key, false);
        changes.addLast(change);                                        //keeping track of changes for copying

        RedBlackNode<T> toDelete = findKeyNode(key);

        if (toDelete != nil) {
            deleteNode(toDelete);
        } else {
            ModificationLog newLog = new ModificationLog();
            for (LogModification mod : mLog.getLogArray()) {
                newLog.addChange(mod);
            }
            mLog.addChange(LogModification.NODEISNIL, -1);
        }

        //DEBUG:
        System.out.println("DELETE: " + key.toString() + "\n" + this.toString() + "\n\n"); //TODO: comment out when no longer needed for debugging
    }

    //MYSTERIOUS MAGICAL PRIVATE BEHIND THE SCENES STUFF:

    /**
     * Left-Rotate function for tree. Note that x is the original root of the subtree
     * and y is its right child, and after the rotation y will be the subtree root and
     * x will be its left child.
     *
     * Note this method assumes that x.right is not nil! (we cannot rotate left in that case)
     *
     * The book we referenced helped with this method
     *
     * Time Complexity: O(1)
     *
     * @param x         the current root of the subtree we want to rotate left
     */
    private void leftRotate(RedBlackNode<T> x) {

        mLog.addChange(LogModification.LEFTROTATE, -1);

        RedBlackNode<T> y = x.getRight();  //set y
        x.setRight(y.getLeft());            //turn y's left subtree to x's right subtree

        if (y.getLeft() != nil) {
            y.getLeft().setParent(x);
        }

        y.setParent(x.getParent());         //y's parent is now x's old parent

        if (x.getParent() == nil) {
            this.root = y;
        } else if (x == x.getParent().getLeft()) {
            x.getParent().setLeft(y);
        } else {
            x.getParent().setRight(y);
        }

        y.setLeft(x);       //x is now y's left kid

        x.setParent(y);

    }

    /**
     * Right-Rotate function for tree. Note that x is the original root of the subtree
     * and y is its left child, and after the rotation y will be the subtree root and
     * x will be its right child.
     *
     * Note this method assumes that x.left is not nil! (we cannot rotate right in that case)
     *
     * The book we referenced helped with this method, though it helped more with the one
     * above
     *
     * Time Complexity: O(1)
     *
     * @param x         the current root of the subtree we want to rotate right
     */
    private void rightRotate(RedBlackNode<T> x) {

        mLog.addChange(LogModification.RIGHTROTATE, -1);

        RedBlackNode<T> y = x.getLeft();  //set y
        x.setLeft(y.getRight());            //turn y's right subtree to x's left subtree

        if (y.getRight() != nil) {
            y.getRight().setParent(x);
        }

        y.setParent(x.getParent());         //y's parent is now x's old parent

        if (x.getParent() == nil) {
            root = y;
        } else if (x == x.getParent().getRight()) {
            x.getParent().setRight(y);
        } else {
            x.getParent().setLeft(y);
        }

        y.setRight(x);       //x is now y's right kid

        x.setParent(y);

    }

    /**
     * This is where we actually implement the insertion of the node
     * into the red-black tree. After inserting the node like we mostly
     * normally would with a binary search tree, we call a cleanup method
     * to fix any red-black tree properties that were thrown off. (These
     * properties ensure efficient time complexities for all operations on
     * the rb tree.)
     *
     * (Differences from bst: nil stuff, color the new node red, and then the
     * fixing up afterwards)
     *
     * Time Complexity: O(log n)
     *
     * @param z     the node we insert
     */
    private void insertNode(RedBlackNode<T> z) {
        RedBlackNode<T> y = nil;
        RedBlackNode<T> x = root;

        while (x != nil) {
            y = x;

            //if this > other, compareTo returns pos
            //if this < other, compareTo returns neg
            //if this == other, compareTo returns zero
            if (z.getKey().compareTo(y.getKey()) < 0) {
                x = x.getLeft();
            } else {
                x = x.getRight();
            }
        }

        z.setParent(y);

        if (y == nil) {
            root = z;
        } else if (z.getKey().compareTo(y.getKey()) < 0) {
            y.setLeft(z);
        } else {
            y.setRight(z);
        }

        z.setLeft(nil);
        z.setRight(nil);
        z.setColor(RedBlackNode.RED);

        this.afterInsertFixTree(z);
    }


    /**
     * This method handles fixing the tree to preserve its red-black
     * properties after a new insert.
     *
     *
     * There are 3 possible cases for violations of the properties after
     * an insert:
     *
     * 1) z's uncle y is red
     *
     * 2) z's uncle y is black and z is a right child
     *
     * 3) z's uncle y is black and z is a left child
     *
     *
     * Time Complexity: O(log n)
     *
     * @param z         the node we just inserted
     */
    private void afterInsertFixTree(RedBlackNode<T> z) {
        Double zVal;
        if (z != nil) {
            zVal = (Double) z.getKey();
        } else {
            zVal = -1.0;
        }

        boolean logOnce = false; //I use this to check if certain things have been logged already (i.e. information about the node to delete)

        while (z.getParent().getColor() == RedBlackNode.RED) {
            if (z.getParent() == z.getParent().getParent().getLeft()) {     //there is a proof for why this node definitely exists

                RedBlackNode<T> y = z.getParent().getParent().getRight();

                if (y.getColor() == RedBlackNode.RED) {                  //case 1 (start)

                    if (!logOnce) {
                        mLog.addChange(LogModification.INSERTC1, (Double) z.getKey());
                        logOnce = true;
                    }

                    z.getParent().setColor(RedBlackNode.BLACK);
                    y.setColor(RedBlackNode.BLACK);
                    z.getParent().getParent().setColor(RedBlackNode.RED);
                    z = z.getParent().getParent();                       //case 1 (end)

                } else {
                    if (z == z.getParent().getRight()) {        //case 2 (start)
                        if (!logOnce) {
                            mLog.addChange(LogModification.INSERTC2, (Double) zVal);
                            logOnce = true;
                        }
                        logOnce = true;
                        z = z.getParent(); // SWITCHED THIS TO z = z.getParent()
                        leftRotate(z);                     //case 2 (end)

                    } else {
                        if (!logOnce) {
                            mLog.addChange(LogModification.INSERTC3, (Double) zVal);
                            logOnce = true;
                        }
                    }
                    z.getParent().setColor(RedBlackNode.BLACK);                 //case 3 (start)
                    z.getParent().getParent().setColor(RedBlackNode.RED);
                    rightRotate(z.getParent().getParent());                //case 3 (end)
                }

            } else {

                RedBlackNode<T> y = z.getParent().getParent().getLeft();

                if (y.getColor() == RedBlackNode.RED) {                  //case 1 (start)
                    if (!logOnce) {
                        mLog.addChange(LogModification.INSERTC1, (Double) zVal);
                        logOnce = true;
                    }
                    z.getParent().setColor(RedBlackNode.BLACK);
                    y.setColor(RedBlackNode.BLACK);
                    z.getParent().getParent().setColor(RedBlackNode.RED);
                    z = z.getParent().getParent();                       //case 1 (end)

                } else {
                    if (z == z.getParent().getLeft()) {        //case 2 (start)
                        mLog.addChange(LogModification.INSERTC2, zVal);
                        z = z.getParent();
                        rightRotate(z);                     //case 2 (end)

                    } else {
                        mLog.addChange(LogModification.INSERTC3, zVal);
                    }
                    z.getParent().setColor(RedBlackNode.BLACK);                 //case 3 (start)
                    z.getParent().getParent().setColor(RedBlackNode.RED);
                    leftRotate(z.getParent().getParent());                //case 3 (end)
                }

            }
        }

        root.setColor(RedBlackNode.BLACK);
    }

    /**
     * Essentially same as above, but without any duplicates
     *
     * Used for both deletion AND insertion now, since we need to check if the value exists already before inserts
     * (if we don't allow duplicates)
     *
     * Returns the node with the given key, or nil if no such node exists.
     *
     * Time Complexity: O(log n)
     *
     * @param key
     * @return
     */
    private RedBlackNode<T> findKeyNode(T key) {
        RedBlackNode<T> toDelete = nil;

        RedBlackNode<T> n = root;

        while (n != nil) {      //we stop when we hit a NIL leaf
            if (key.compareTo(n.getKey()) < 0) {        //could only be in n's left subtree
                n = n.getLeft();
            } else {
                if (key.compareTo(n.getKey()) > 0) {        //could only be in n's right subtree
                    n = n.getRight();
                } else {
                    if (key.compareTo(n.getKey()) == 0) {   //found it!
                        toDelete = n;
                        break;
                    }
                }
            }
        }

        return toDelete;        //NOTE: This MAY be NIL, make sure to always check!!

    }

    /**
     * Method for moving subtrees around; replaces subtree rooted at node
     * u with subtree rooted at node v; u's parent becomes v's parent and u's
     * parent gets v as its child
     *
     * Time Complexity: O(1)
     *
     * @param u
     * @param v
     */
    private void transplant(RedBlackNode<T> u, RedBlackNode<T> v) {

        System.out.println("Transplanting");
        mLog.addChange(LogModification.TRANSPLANT, (Double) u.getKey(), (Double) v.getKey());

        if (u.getParent() == nil) {
            root = v;
        } else if (u == u.getParent().getLeft()) {
            u.getParent().setLeft(v);
        } else {
            u.getParent().setRight(v);
        }

        v.setParent(u.getParent());

    }

    /**
     * Deletes a given node.
     *
     * Time Complexity: O(log n)
     *
     *
     * @param z
     */
    private void deleteNode(RedBlackNode<T> z) {

        if (z != nil && z != null) {
            mLog.addChange(LogModification.DELETION, (Double) z.getKey());
        }

        RedBlackNode<T> y = z;
        RedBlackNode<T> x;

        int yOriginalColor = y.getColor();

        if (z.getLeft() == nil) {

            x = z.getRight();
            transplant(z, z.getRight());

        } else if (z.getRight() == nil) {

            x = z.getLeft();
            transplant(z, z.getLeft());

        } else {

            y = treeMinimum(z.getRight());

            if (y == nil) {
                System.out.println("An error occurred in deleteNode where y = treeMinimum(z.getRight()) was nil"); //TODO: Remove after done debugging
            }

            yOriginalColor = y.getColor();
            x = y.getRight();

            if (y.getParent() == z) {
                x.setParent(y);

            } else {
                transplant(y, y.getRight());
                y.setRight(z.getRight());
                y.getRight().setParent(y);
            }

            transplant(z, y);

            y.setLeft(z.getLeft());
            y.getLeft().setParent(y);
            y.setColor(z.getColor());
        }

        System.out.println("here is z: " + z.toString());

        System.out.println("here is y: " + y.toString());

        if (yOriginalColor == RedBlackNode.BLACK) {
            System.out.println("we fix the tree!");
            afterDeleteFixTree(x);
        }
    }

    /**
     * Return the leftmost node starting from a node, root
     * If the node has no left children, the node itself is returned
     * @param root
     * @return
     */
    private RedBlackNode<T> treeMinimum(RedBlackNode<T> root) {
        while (root.getLeft() != nil && root.getLeft() != null) {
            root = root.getLeft();
        }
        return root;
    }

    /**
     * We run this from our deletion method when y's original color is Black since it could cause
     * issues if it is deleted.
     * @param x
     */
    private void afterDeleteFixTree(RedBlackNode<T> x) {
        System.out.println("Fixing");

        boolean logOnce = false;

        Double xVal;

        if (x != nil) {
            xVal = (Double) x.getKey();
        } else {
            xVal = 1000.0;
        }

        while (x != root && x.getColor() == RedBlackNode.BLACK) {

            if (x == x.getParent().getLeft()) {

                RedBlackNode<T> w = x.getParent().getRight();

                if (w.getColor() == RedBlackNode.RED) { //case one start

                    if (!logOnce) {
                        mLog.addChange(LogModification.DELETEC1, xVal);
                        logOnce = true;
                    }

                    w.setColor(RedBlackNode.BLACK);
                    x.getParent().setColor(RedBlackNode.RED);
                    leftRotate(x.getParent());
                    w = x.getParent().getRight(); //case one end

                }

                if (w.getLeft().getColor() == RedBlackNode.BLACK && w.getRight().getColor() == RedBlackNode.BLACK) { //case two start

                    if (!logOnce) {
                        mLog.addChange(LogModification.DELETEC2, xVal);
                        logOnce = true;
                    }

                    w.setColor(RedBlackNode.RED);
                    x = x.getParent(); //case two end

                } else {
                    if (w.getRight().getColor() == RedBlackNode.BLACK) { //case 3 start

                        if (!logOnce) {
                            mLog.addChange(LogModification.DELETEC3, xVal);
                            logOnce = true;
                        }

                        w.getLeft().setColor(RedBlackNode.BLACK);
                        w.setColor(RedBlackNode.RED);
                        rightRotate(w);
                        w = x.getParent().getRight(); //case 3 end

//                        delCase = DELETEC3;
                    } else {
                        if (!logOnce) {
                            mLog.addChange(LogModification.DELETEC4, xVal);
                            logOnce = true;
                        }
//                        delCase = DELETEC4;
                    }

                    w.setColor(x.getParent().getColor());        //case four start
                    x.getParent().setColor(RedBlackNode.BLACK);
                    w.getRight().setColor(RedBlackNode.BLACK);
                    leftRotate(x.getParent());

                    x = root; //case four end

                }

            } else {

                RedBlackNode<T> w = x.getParent().getLeft();

                if (w.getColor() == RedBlackNode.RED) { //case one start

                    if (!logOnce) {
                        mLog.addChange(LogModification.DELETEC1, xVal);
                        logOnce = true;
                    }

                    w.setColor(RedBlackNode.BLACK);
                    x.getParent().setColor(RedBlackNode.RED);
                    rightRotate(x.getParent());
                    w = x.getParent().getLeft();   //case one end

//                    delCase = DELETEC1;
                }

                if (w.getRight().getColor() == RedBlackNode.BLACK && w.getLeft().getColor() == RedBlackNode.BLACK) { //case two start

                    if (!logOnce) {
                        mLog.addChange(LogModification.DELETEC2, xVal);
                        logOnce = true;
                    }

                    w.setColor(RedBlackNode.RED);
                    x = x.getParent(); //case two end

//                    delCase = DELETEC2;

                } else {

                    if (w.getLeft().getColor() == RedBlackNode.BLACK) { //case 3 start

                        if (!logOnce) {
                            mLog.addChange(LogModification.DELETEC3, xVal);
                            logOnce = true;
                        }

                        w.getRight().setColor(RedBlackNode.BLACK);
                        w.setColor(RedBlackNode.RED);
                        leftRotate(w);
                        w = x.getParent().getLeft(); //case 3 end

//                        delCase = DELETEC3;
                    } else {
                        if (!logOnce) {
                            mLog.addChange(LogModification.DELETEC4, xVal);
                            logOnce = true;
                        }
//                        delCase = DELETEC4;
                    }

                    w.setColor(x.getParent().getColor());        //case four start
                    x.getParent().setColor(RedBlackNode.BLACK);
                    w.getLeft().setColor(RedBlackNode.BLACK);
                    rightRotate(x.getParent());

                    x = root; //case four end
                }
            }
        }

        x.setColor(RedBlackNode.BLACK);

    }


    //COOL, MORE PUBLIC STUFF:

    /**
     * We probably won't actually need this
     *
     * @param newRoot
     */
    public void setRoot(RedBlackNode newRoot)  {
        root = newRoot;
    }

    /**
     * WOOHOO, THE ROOT, YAY, WE CAN GET IT, YES WE CAN, WOOOOOOO
     *
     * It is 1am don't judge me.
     *
     * @return      the uncle's uncle's parent's right kid of the 97th
     *              largest value in the tree, jk, the root, of course
     */
    public RedBlackNode<T> getRoot() {
        return root;
    }

    /**
     * IMPORTANT STUFF!!!!!!!!!!!!!!!!!!!!!!
     *
     * Seems useless, but actually... this lets us check the returns
     * for the above two getters with nil!! If they equal nil, those
     * kids we were looking for essentially don't exist, since nil
     * is this abstract thingy we use to signal the end of the tree,
     * both on the top and bottom
     *
     * @return          the nil node (note that this is a pointer to
     *                  single nil node for this instance of an RBT!)
     */
    public RedBlackNode<T> getNil() {
        return nil;
    }

    public String traverseToString(String rootStr, RedBlackNode root, int num) {
        if (root != nil) {
//            num = num + 1;
            rootStr += num + ": " + root.toString() + "\n";
            if (root.getLeft() != nil) {
                rootStr = traverseToString(rootStr, root.getLeft(), 2*num);
            }
            if (root.getRight() != nil) {
                rootStr = traverseToString(rootStr, root.getRight(), 2*num+1);
            }
        }
        return rootStr;
    }

    public String toString() {
        String toStr = "";

        if (root == nil) {
            toStr = "Tree is empty";
        } else {
            int num = 1;
            toStr = traverseToString(toStr, root, num);
        }

        return toStr;
    }

    public ModificationLog getLog() {
        return mLog;
    }

    public void setLog(ModificationLog newLog) { this.mLog = newLog; }
}
