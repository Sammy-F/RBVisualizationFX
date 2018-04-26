package visualization;

import java.util.ArrayDeque;

/**
 * Red-Black Tree
 */
public class RBTree<T extends Comparable<T>> {

    //INSTANCE VARIABLES:

    private ArrayDeque<TreeModification<T>> changes;    //allows for recreation of tree (feel free to do this another way if you find something better)

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
    }

    /**
     * Generates and returns a copy of the tree; note that
     * the copy is a reference to a separate tree that is
     * identical in features to this one
     *
     * @return  RBTree of type T, a copy of this one
     */
    public RBTree<T> copy() {
        //TODO: return an actual copy of this tree

        RBTree<T> copy = new RBTree<T>();

        for (TreeModification<T> change: changes) {
            if (change.isInsert()) {
                copy.insert(change.getKey());
            } else {
                copy.delete(change.getKey());
            }
        }

        return copy;
    }

    /**
     * Handle logic for node insertion and return the new tree
     */
    public void insert(T key) {
        //TODO
        TreeModification<T> change = new TreeModification<T>(key, true);
        changes.addLast(change);                                        //keeping track of changes for copying

        RedBlackNode<T> node = new RedBlackNode<>(key);

        insertNode(node);
    }

    /**
     * Handle logic for node deletion and return the new tree
     */
    public void delete(T key) {
        //TODO
        TreeModification<T> change = new TreeModification<>(key, false);
        changes.addLast(change);                                        //keeping track of changes for copying

        RedBlackNode<T> toDelete = findLowest(key);

        if (toDelete != nil) {
            deleteNode(toDelete);       //TODO: the delete function, which calls both transplant and afterDeleteFix, which are also both incomplete
        }
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

        //TODO: IF ROTATIONS BUG ITS BECAUSE THIS ONE WAS LEFT AS AN EXERCISE, NOT THE OTHER ONE, THAT ONE IS JUST DANDY
        //NOTE: I literally just swapped all the lefts to rights and vice versa, hopefully that works, I think it should
        //....but who knows

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
        RedBlackNode<T> y = nil;        //OH CRAP, SAME HERE, LETS HOPE (this is how pseudocode did it, soo....?)
        RedBlackNode<T> x = root;   //TODO: OH GOD I HOPE THIS X=ROOT THING DOESN'T MESS UP...

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
        } // the while loop closes after this

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
        while (z.getParent().getColor() == RedBlackNode.RED) {
            if (z.getParent() == z.getParent().getParent().getLeft()) {     //there is a proof for why this node definitely exists

                RedBlackNode<T> y = z.getParent().getParent().getRight();

                if (y.getColor() == RedBlackNode.RED) {                  //case 1 (start)
                    z.getParent().setColor(RedBlackNode.BLACK);
                    y.setColor(RedBlackNode.BLACK);
                    z.getParent().getParent().setColor(RedBlackNode.RED);
                    z = z.getParent().getParent();                       //case 1 (end)
                } else {
                    if (z == z.getParent().getRight()) {        //case 2 (start)
                        z = z.getParent(); // SWITCHED THIS TO z = z.getParent()
                        this.leftRotate(z);                     //case 2 (end)
                    }
                    z.getParent().setColor(RedBlackNode.BLACK);                 //case 3 (start)
                    z.getParent().getParent().setColor(RedBlackNode.RED);
                    this.rightRotate(z.getParent().getParent());                //case 3 (end)
                }

            } else {  //SHOULD HAVE ALL LEFTS/RIGHTS SWAPPED!!! //TODO: MAKE SURE THAT I SWAPPED THEM

                RedBlackNode<T> y = z.getParent().getParent().getLeft();

                if (y.getColor() == RedBlackNode.RED) {                  //case 1 (start)
                    z.getParent().setColor(RedBlackNode.BLACK);
                    y.setColor(RedBlackNode.BLACK);
                    z.getParent().getParent().setColor(RedBlackNode.RED);
                    z = z.getParent().getParent();                       //case 1 (end)
                } else {
                    if (z == z.getParent().getLeft()) {        //case 2 (start)
                        z.setParent(z);
                        this.rightRotate(z);                     //case 2 (end)
                    }
                    z.getParent().setColor(RedBlackNode.BLACK);                 //case 3 (start)
                    z.getParent().getParent().setColor(RedBlackNode.RED);
                    this.leftRotate(z.getParent().getParent());                //case 3 (end)
                }

            }
        }

        root.setColor(RedBlackNode.BLACK);
    }

    /**
     * Method for finding the lowest instance of a node in the tree. This
     * is a useful helper method for prepping for deletion; you need an actual
     * node to delete, and lower nodes are easier and cheaper to delete.
     *
     * (Note: this code is 100% original not from book at all)
     *
     * IMPORTANT: CHECK THE RETURN IS NOT EQUAL TO NIL!!!!!!!!!!!!!!
     *
     * Time Complexity: O(log n)
     *
     * @param key           the key we want to find the node of in the tree
     * @return              the lowest node instance of this key
     */
    private RedBlackNode<T> findLowest(T key) {
        RedBlackNode<T> toDelete = nil;

        RedBlackNode<T> n = root;

        while (n != nil) {      //we stop when we hit a NIL leaf
            if (key.compareTo(n.getKey()) < 0) {        //could only be in n's left subtree
                n = n.getLeft();
            } else {
                if (key.compareTo(n.getKey()) > 0) {        //could only be in n's right subtree
                    n = n.getRight();
                } else {
                    if (key.compareTo(n.getKey()) == 0) {   //could only be n or in n's right subtree
                        toDelete = n;
                        n = n.getRight();
                    }
                }
            }
        }

        return toDelete;

        //similar code from the bstviscontroller that I used as a reference, since we already coded it:

//        VisNode toDelete = null;
//        if (root.getValue() == value) {
//            toDelete = root;
//        }
//
//        VisNode n = root;
//
//        while (true) {
//            if (value < n.getValue() && n.hasLeftChild()) { //could only be in n's left subtree
//                n = n.getLeftChild();
//            } else {
//                if (value > n.getValue() && n.hasRightChild()) { //could only be in n's right subtree
//                    n = n.getRightChild();
//                } else {
//                    if (n.getValue() == value) { //could only be n or in n's right subtree, if n has right subtree
//                        toDelete = n;
//                        if (n.hasRightChild()) {
//                            n = n.getRightChild();
//                        } else {
//                            break; //the value was a leaf or had no right children and we found it
//                        }
//                    } else {
//                        break; //no lower instances possible anymore
//                    }
//                }
//            }
//        }
//
//        if (toDelete != null) {
//            return toDelete;
//        } else {
//            return null;
//        }
    }

    //TODO: THE DELETION STUFF BELOW, THE THREE METHODS (see pages 320something in book, the pseudocode helps so much)

    /**
     * Method for moving subtrees around; replaces subtree rooted at node
     * u with subtree rooted at node v; u's parent becomes v's parent and u's
     * parent gets v as its child
     *
     *
     * EDIT THIS EDIT THIS EDIT THIS EDIT THIS EDIT EDIT EDIT EDIT!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
     *
     * @param u
     * @param v
     */
    private void transplant(RedBlackNode<T> u, RedBlackNode<T> v) {

        if (u.getParent() == nil) {
            root = v;
        } else if (u == u.getParent().getLeft()) {
            u.getParent().setLeft(v);
        } else { // if
            u.getParent().setRight(v);
        }

        v.setParent(u.getParent());

    }

    private void deleteNode(RedBlackNode<T> z) { //Finished converting the pseudocode

        RedBlackNode<T> y = z;
        RedBlackNode<T> x;
        int yOriginalColor = z.getColor();
        if (z.getLeft() == nil) {
            x = z.getRight();
            transplant(z, z.getRight());
        } else if (z.getRight() == nil) {
            x = z.getLeft();
            transplant(z, z.getRight());
        } else {
            y = treeMinimum(z.getRight());
            yOriginalColor = y.getColor();
            x = y.getRight();
            if (y.getParent() == z) {
                x.setParent(y);
            } else {
                transplant(y, y.getRight());
                y.setRight(z.getRight());
                y.getRight().setParent(y);
            }
            y.setLeft(z.getLeft());
            y.getLeft().setParent(y);
            y.setColor(z.getColor());
        }
        if (yOriginalColor == RedBlackNode.BLACK) {
            afterDeleteFixTree(x);
        }
    }

    private RedBlackNode<T> treeMinimum(RedBlackNode<T> root) { //TODO: Implement
        while (root.getLeft() != nil) {
            root = root.getLeft();
        }
        return root;
    }

    private void afterDeleteFixTree(RedBlackNode<T> x) {

        while (x != root && x.getColor() == RedBlackNode.BLACK) {
            if (x == x.getParent().getLeft()) {
                RedBlackNode<T> w = x.getParent().getRight();
                if (w.getColor() == RedBlackNode.RED) { //case one start
                    w.setColor(RedBlackNode.BLACK);
                    x.getParent().setColor(RedBlackNode.RED);
                    leftRotate(x.getParent()); //case one end
                }
                if (w.getLeft().getColor() == RedBlackNode.BLACK && w.getRight().getColor() == RedBlackNode.BLACK) { //case two start
                    w.setColor(RedBlackNode.RED);
                    x = x.getParent(); //case two end
                } else if (w.getRight().getColor() == RedBlackNode.BLACK) { //case 3 start
                    w.getLeft().setColor(RedBlackNode.BLACK);
                    w.setColor(RedBlackNode.RED);
                    rightRotate(w);
                    w = x.getParent().getRight(); //case 3 end
                }
                if (w.getColor() == RedBlackNode.BLACK && w.getRight().getColor() == RedBlackNode.RED) { //case four start
                    w.setColor(x.getParent().getColor()); // not positive which level this code chunk is supposed to be in  //case four start
                    x.getParent().setColor(RedBlackNode.BLACK);
                    w.getRight().setColor(RedBlackNode.BLACK);
                    leftRotate(x.getParent());
                    x = root; //case four end
                }
            } else { // NOTE: not sure that I did this right - it just said i should exchange left and right, so i did that
                RedBlackNode<T> w = x.getParent().getLeft();
                if (w.getColor() == RedBlackNode.RED) {
                    w.setColor(RedBlackNode.BLACK);
                    x.getParent().setColor(RedBlackNode.RED);
                    rightRotate(x.getParent()); // especially not sure if I was supposed to flip this to right or not
                }
                if (w.getRight().getColor() == RedBlackNode.BLACK && w.getLeft().getColor() == RedBlackNode.BLACK) {
                    w.setColor(RedBlackNode.RED);
                    x = x.getParent();
                } else {
                    if (w.getLeft().getColor() == RedBlackNode.BLACK) {
                        w.getRight().setColor(RedBlackNode.BLACK);
                        w.setColor(RedBlackNode.RED);
                        leftRotate(w); //again, not sure if I was supposed to flip the type of rotation
                        w = x.getParent().getLeft();
                    }
                    w.setColor(x.getParent().getColor()); // not positive which level this code chunk is supposed to be in  //case four start
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

}
