package visualization;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;

import javafx.event.ActionEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * A class for handling the logic of our BST visualization program.
 *
 * NOTE: THIS CLASS IS VERY BUGGY AND WE ONLY MADE THIS AS AN EXPERIMENT BEFORE JUMPING INTO OUR RBT CODE.
 *
 * We could have theoretically deleted this, since it was buggy after all, but it was a significant amount of
 * work to create, and it was our first step, so we kept it.
 *
 * Authors: Samantha Fritsche and Katya Gurgel
 */
public class BSTVisController implements Initializable {

    private final int DEFAULT_RADIUS = 30;
    private final int HEIGHT_SCALAR = 150;
    private final double INIT_INSERTIONX = 470;
    private final double INIT_XSPACING = INIT_INSERTIONX/2;

    private VisNode tobeDeleted;

    private boolean insertClicked = false;
    private boolean removeClicked = false;

//    private double insertionX;
//    private double xSpacing;

//    private List<NodeCircle> nodeList;
    private List<Connector> connectorList;

    private VisNode root = null;

    @FXML
    private TextField tfValue;

    @FXML
    private RadioButton radioInsert;

    @FXML
    private RadioButton radioRemove;

    @FXML
    private AnchorPane anchorPane;

    /**
     * Handles what to do when go is clicked; differs for insertions/deletions
     * @param event
     */
    @FXML
    private void handleAction(ActionEvent event) {
        Double value = Double.parseDouble(tfValue.getText()); //the value entered by user is set for the insert/delete

        if (insertClicked) {
            insertNode(value);
        }
        else {
            //if the root is null we do nothing, or else we get null pointer exceptions
            if (root != null) {
//            tobeDeleted = findFirstNode(value, root); //first we find the first valid isntance of the node we want to delete
//            findDeepestNode(value, root); //then we find the deepest instance - this modifies tobeDeleted
                tobeDeleted = findDelete(value, root); //finds lowest instance of deletion value

                if (tobeDeleted != null) {

                    //Color the node to be deleted to indicate to the user which it is.
                    tobeDeleted.getCircle().setFillColor(Color.GOLDENROD);
                    PauseTransition pause = new PauseTransition(Duration.seconds(.5));

                    // Pause after coloring before returning to the original color.
                    pause.setOnFinished(event1 -> tobeDeleted.getCircle().setFillColor(Color.DARKSLATEGRAY));
                    pause.play();

                    PauseTransition pause2 = new PauseTransition(Duration.seconds(.5));

                    pause2.setOnFinished(event1 -> deleteNode(tobeDeleted)); //Delete the node we found
                    pause2.play();

                } else {
                    System.out.println("Node does not exist.");
                }
            } else {
                System.out.println("EMPTY TREE");
            }
        }

    }


    //I think the function below finds the same thing as the two above combined, and also without recursion, which
    //may be less efficient (though do we really care about efficiency? not a lot...) and takes less space
    /**
     * Finds the lowest instance of the node to delete
     * @param value
     * @param root
     * @return
     */
    private VisNode findDelete(Double value, VisNode root) {
        VisNode toDelete = null;
        if (root.getValue() == value) {
            toDelete = root;
        }

        VisNode n = root;

        while (true) {
            if (value < n.getValue() && n.hasLeftChild()) { //could only be in n's left subtree
                n = n.getLeftChild();
            } else {
                if (value > n.getValue() && n.hasRightChild()) { //could only be in n's right subtree
                    n = n.getRightChild();
                } else {
                    if (n.getValue() == value) { //could only be n or in n's right subtree, if n has right subtree
                        toDelete = n;
                        if (n.hasRightChild()) {
                            n = n.getRightChild();
                        } else {
                            break; //the value was a leaf or had no right children and we found it
                        }
                    } else {
                        break; //no lower instances possible anymore
                    }
                }
            }
        }

        if (toDelete != null) {
            return toDelete;
        } else {
            return null;
        }
    }

    /**
     * Method that handles the logic of which type of deletion we want to perform
     * and then calls the relevant method
     * @param nodeToDelete
     */
    private void deleteNode(VisNode nodeToDelete) {
        //I think there is stuff just buggy in general, even when there is a single child, because I can get
        //weirddd things to happen with a tree containing only parents of single children

        if (!nodeToDelete.hasRightChild() && !nodeToDelete.hasLeftChild()) { //Check if the node has no children

            deleteNodeWithNoChildren(nodeToDelete);

        } else if (nodeToDelete.hasLeftChild() && !nodeToDelete.hasRightChild()) { //Check if the node has a left child

            deleteNodeWithLeftChild(nodeToDelete);

        } else if (nodeToDelete.hasRightChild() && !nodeToDelete.hasLeftChild()) { //Check if the node has a right child

            deleteNodeWithRightChild(nodeToDelete);

        } else { //Case where the node has two children
            System.out.println("I have two children and don't currently do anything.");
            deleteNodeWithTwoChildren();
        }
    }

    /**
     * Method for handling deletion with no children
     * @param nodeToDelete
     */
    private void deleteNodeWithNoChildren(VisNode nodeToDelete) {
        System.out.println("I have no children.");
        anchorPane.getChildren().remove(nodeToDelete.getCircle());
        anchorPane.getChildren().remove(nodeToDelete.getCToParent());
        if (nodeToDelete.isRightChild()) {
            nodeToDelete.getParent().setRightChild(null);
            nodeToDelete.getParent().setHasRightChild(false); //this should already be handled when setting the child as null, but w/e
            nodeToDelete.getParent().setRCToChild(null); //set the right child connector of the parent to null

        } else if (nodeToDelete.isLeftChild()) {
            nodeToDelete.getParent().setLeftChild(null);
            nodeToDelete.getParent().setHasLeftChild(false);
            nodeToDelete.getParent().setLCToChild(null);
        } else {
            root = null; //if the node we're deleting isn't a child, it must be the root so let's set it to null
        }
    }

    /**
     * Method for handling deletion of a node with a left child only
     * @param nodeToDelete
     */
    private void deleteNodeWithLeftChild(VisNode nodeToDelete) { //Sorry, this is super convoluted!

        VisNode lc = nodeToDelete.getLeftChild(); //we know it has a left child, this var makes code more concise

        anchorPane.getChildren().remove(nodeToDelete.getCircle()); //remove the node from the scene

        if (nodeToDelete.isLeftChild() || nodeToDelete.isRightChild()) {

            VisNode p = nodeToDelete.getParent(); //we know it has a parent

            //remove the node's parent connector from the scene
            anchorPane.getChildren().remove(nodeToDelete.getCToParent());

            if (nodeToDelete.isLeftChild()) { //check if the node is a left child

                //remove connector from node to its left child from screen
                anchorPane.getChildren().remove(nodeToDelete.getLCToChild());

                //add connector from node's parent to node's left child (this whole method is for nodes w/only a left child)
                Connector lcToP = new Connector(p.getCircle(), lc.getCircle());

                //left child's connector to parent should be the new one created above
                lc.setCToParent(lcToP);

                //parent's new connector to left child should be the one above
                p.setLCToChild(lcToP);

                //deal with connectors update
                lc.notifyConnectorsUpdated();

                //parent's new left child is the left child of deleted node
                p.setLeftChild(lc);

                lc.setParent(p, true);

                anchorPane.getChildren().add(lcToP);

            } else { //the node is a right child

                anchorPane.getChildren().remove(nodeToDelete.getLCToChild());

                Connector lcToP = new Connector(p.getCircle(), lc.getCircle());

                lc.setCToParent(lcToP);

                p.setRCToChild(lcToP);

                lc.notifyConnectorsUpdated();

                lc.setIsRightChild(true);
                lc.setIsLeftChild(false);

                p.setRightChild(lc);

//                nodeToDelete.getLeftChild().setParent(nodeToDelete.getLeftChild(), false); //BUG
                lc.setParent(p, false);

                anchorPane.getChildren().add(lcToP);
            }

        } else { //the node must be the root

            anchorPane.getChildren().remove(nodeToDelete.getCircle());
            anchorPane.getChildren().remove(nodeToDelete.getLCToChild());

            lc.setCToParent(null);

            double ix = root.getCircle().getInsertionX();
            double xs = root.getCircle().getxSpacing();

            root = lc;

            root.setLevel(1);
            root.setParent(null, false);

            root.setIsRightChild(false);
            root.setIsLeftChild(false);

            root.setCToParent(null);

            root.getCircle().setInsertionX(ix);
            root.getCircle().setInsertionX(xs);

            root.notifyConnectorsUpdated();
        }

        reduceTreeLevelsByOne(lc);

    }

    /**
     * Method for handling deletion of a node with a right child only
     * @param nodeToDelete
     */
    private void deleteNodeWithRightChild(VisNode nodeToDelete) {

        VisNode rc = nodeToDelete.getRightChild(); //we know it has a right child, this var makes code more concise

        anchorPane.getChildren().remove(nodeToDelete.getCircle()); //remove the node from the scene

        if (nodeToDelete.isLeftChild() || nodeToDelete.isRightChild()) {

            VisNode p = nodeToDelete.getParent(); //we know it has a parent

            //remove the node's parent connector from the scene
            anchorPane.getChildren().remove(nodeToDelete.getCToParent());

            if (nodeToDelete.isRightChild()) { //check if the node is a right child

                //remove connector from node to its right child from screen
                anchorPane.getChildren().remove(nodeToDelete.getRCToChild());

                //add connector from node's parent to node's right child (this whole method is for nodes w/only a right child)
                Connector rcToP = new Connector(p.getCircle(), rc.getCircle());

                //right child's connector to parent should be the new one created above
                rc.setCToParent(rcToP);

                //parent's new connector to right child should be the one above
                p.setLCToChild(rcToP);

                //deal with connectors update
                rc.notifyConnectorsUpdated();

                //parent's new right child is the right child of deleted node
                p.setLeftChild(rc);

                rc.setParent(p, false);

                anchorPane.getChildren().add(rcToP);

            } else { //the node to delete is a left child

                anchorPane.getChildren().remove(nodeToDelete.getRCToChild());

                Connector rcToP = new Connector(p.getCircle(), rc.getCircle());

                rc.setCToParent(rcToP);

                p.setRCToChild(rcToP);

                rc.notifyConnectorsUpdated();

                rc.setIsLeftChild(true);
                rc.setIsRightChild(false);

                p.setLeftChild(rc);

//                nodeToDelete.getLeftChild().setParent(nodeToDelete.getLeftChild(), false); //BUG
                rc.setParent(p, true);

                anchorPane.getChildren().add(rcToP);
            }

        } else { //the node must be the root

            anchorPane.getChildren().remove(nodeToDelete.getCircle());
            anchorPane.getChildren().remove(nodeToDelete.getRCToChild());

            rc.setCToParent(null);                             //trying to cover all bases, wherever bugs could happen

            double ix = root.getCircle().getInsertionX();
            double xs = root.getCircle().getxSpacing();

            root = rc;

            root.setLevel(1);
            root.setParent(null, false);

            root.setIsRightChild(false);
            root.setIsLeftChild(false);

            root.setCToParent(null);

            root.getCircle().setInsertionX(ix);
            root.getCircle().setInsertionX(xs);

            root.notifyConnectorsUpdated();

        }

        reduceTreeLevelsByOne(rc);

    }


    /**
     * Remove a node with two children
     */
    private void deleteNodeWithTwoChildren() {

        VisNode sub = findMinOfSubtree(tobeDeleted.getRightChild());

        System.out.println("Sub val = " + sub.getValue());

        // The substitute node can't have a left child, so it can only have a right child or no children
        if (sub.hasRightChild()) {

            tobeDeleted.setValue(sub.getValue()); //instead of deleting, just replace value
            tobeDeleted.getCircle().getThisText().setText(Double.toString(tobeDeleted.getValue()));

            reduceTreeLevelsByOne(sub.getRightChild());

            // Generate a new connector using the updated nodes
            Connector childConnector = new Connector(sub.getParent().getCircle(), sub.getRightChild().getCircle());
            anchorPane.getChildren().remove(sub.getCToParent());
            anchorPane.getChildren().remove(sub.getRCToChild());

            // Need to handle parent connectors
            if (sub.isLeftChild()) {
                sub.getParent().setLeftChild(sub.getRightChild());
                sub.getRightChild().setParent(sub.getParent(), true);
                sub.getRightChild().setIsLeftChild(true);
                sub.getRightChild().setIsRightChild(false);
            } else {
                sub.getParent().setRightChild(sub.getRightChild());
                sub.getRightChild().setParent(sub, false);

            }

            anchorPane.getChildren().add(childConnector);
            sub.getParent().notifyConnectorsUpdated();
            sub.getRightChild().notifyConnectorsUpdated();

        } else { //the node is a leaf (easy!) (sort of!)

            tobeDeleted.setValue(sub.getValue());
            anchorPane.getChildren().remove(sub.getCircle());
            anchorPane.getChildren().remove(sub.getCToParent());

            tobeDeleted.getCircle().getThisText().setText(Double.toString(tobeDeleted.getValue()));

            if (sub.isLeftChild()) {   //sub is at least a child of the right child of the deleted node
                sub.getParent().setLCToChild(null);
                sub.getParent().setHasLeftChild(false);
                sub.getParent().setLeftChild(null);
            } else {                                    //sub is the min of the right subtree, so if it is a right child, it is the only node in the subtree
                sub.getParent().setRCToChild(null);
                sub.getParent().setHasRightChild(false);
                sub.getParent().setRightChild(null);
            }
        }

    }

    /**
     * Find the minimum of the subtree, including the initial node
     * @param root
     * @return
     */
    private VisNode findMinOfSubtree(VisNode root) {

        if (!root.hasLeftChild() && !root.hasRightChild()) { //I'm a leaf so just return me ^_^
            return root;
        } else if (!root.hasLeftChild() && root.hasRightChild()) { //I'm not a leaf but I'm the minimum. :O
            return root;
        } else { //I do have a left child still, hehe
            return findMinOfSubtree(root.getLeftChild());
        }

    }

    /**
     * Decrements the level of all nodes in a subtree, including
     * the root. In addition, it modifies the padding vals for nodes to the
     * appropriate and notifies any Connectors of the change.
     * @param toReduce
     */
    private void reduceTreeLevelsByOne(VisNode toReduce) {

        // First we decrement the level of toReduce
        toReduce.decrementLevel();

        // Then handle different cases for what toReduce could be
        if (toReduce.isRightChild()) {
            toReduce.getCircle().setInsertionX(toReduce.getCircle().getInsertionX() - ((toReduce.getCircle().getxSpacing()*2)));
            toReduce.getCircle().setXSpacing(toReduce.getXSpacingFromLevel(INIT_XSPACING));
            toReduce.getCircle().setPadding(new Insets(toReduce.getLevel()*HEIGHT_SCALAR+20, 20, 20,
                    (toReduce.getCircle().getInsertionX())));
        } else {
            toReduce.getCircle().setInsertionX(toReduce.getCircle().getInsertionX()+(2*toReduce.getCircle().getxSpacing()));
            toReduce.getCircle().setXSpacing(toReduce.getXSpacingFromLevel(INIT_XSPACING));
            toReduce.getCircle().setPadding(new Insets(toReduce.getLevel()*HEIGHT_SCALAR+20, 20, 20,
                    toReduce.getCircle().getInsertionX()));
        }

        // If the level of toReduce now = 0, it has become the root, so make sure to set these things
        if (toReduce.getLevel() == 0) {
            root = toReduce;
            toReduce.setIsLeftChild(false);
            toReduce.setIsRightChild(false);
        }

        //Our nodes have changed location, so notify the connectors to update their locations as well
        toReduce.notifyConnectorsUpdated();

        //Handle cases to call reduceTreeLevelsByOne recursively on
        //the left and right subtrees of toReduce
        if (!toReduce.hasLeftChild() && !toReduce.hasRightChild()) {
        }
        else if (toReduce.hasLeftChild() && !toReduce.hasRightChild()) {
            reduceTreeLevelsByOne(toReduce.getLeftChild());
        } else if (toReduce.hasRightChild() && !toReduce.hasLeftChild()) {
            reduceTreeLevelsByOne(toReduce.getRightChild());
        } else {
            reduceTreeLevelsByOne(toReduce.getLeftChild());
            reduceTreeLevelsByOne(toReduce.getRightChild());
        }
    }

    /**
     * Handles insertion of a new node with the value
     * input by the user.
     */
    private void insertNode(Double value) {

        VisNode newNode;
        double insertionX = INIT_INSERTIONX;
        double xSpacing = INIT_XSPACING;

        boolean left = false;
        boolean right = false;

        int thisLevel = 0;

        if (root == null) {
            newNode = new VisNode(value, VisNode.BLACK, 0);
            root = newNode;
            root.setIsRightChild(false);
            root.setIsLeftChild(false);
        } else {
            boolean haveNext = true;
            VisNode n = root;

            VisNode p = n;

            while (haveNext) {
                thisLevel++;
                System.out.println("Insertion value = " + value);
                System.out.println("Check value = " + n.getValue());
                if (value < n.getValue()) {
                    insertionX -= xSpacing;
                    xSpacing /= 2;
                    if (n.hasLeftChild()) {
                        n = n.getLeftChild();
                    } else {
                        haveNext = false;
                        p = n;
                        left = true;
                    }
                } else {
                    insertionX += xSpacing;
                    xSpacing /= 2;
                    if (n.hasRightChild()) {
                        n = n.getRightChild();
                    } else {
                        haveNext = false;
                        p = n;
                        right = true;
                    }
                }
            }
            newNode = new VisNode(value, VisNode.BLACK, thisLevel);
            newNode.setParent(p, left);
            if (left) {
                p.setLeftChild(newNode);
                p.setHasLeftChild(true);
                newNode.setIsLeftChild(true);
            } else if (right) {
                p.setRightChild(newNode);
                p.setHasRightChild(true);
                newNode.setIsRightChild(true);
            } else {
                root = newNode;
            }
        }

        System.out.println("The root is a right child: " + root.isRightChild());
        System.out.println("The root is a left child: " + root.isLeftChild());
        System.out.println("The root has a right child: " + root.hasRightChild());
        System.out.println("The root has a left child: " + root.hasLeftChild());
        if (root.getLeftChild() == null && root.getRightChild() == null) {
            System.out.println("The root has no children.");
        } else if (root.getLeftChild() != null && root.getRightChild() == null) {
            System.out.println("Value of left child of root is: " + root.getLeftChild().getValue());
        } else if (root.getRightChild() != null && root.getLeftChild() == null) {
            System.out.println("Value of right child of root is: " + root.getRightChild().getValue());
        } else {
            System.out.println("Value of left child of root is: " + root.getLeftChild().getValue());
            System.out.println("Value of right child of root is: " + root.getRightChild().getValue());
        }

        NodeCircle newNodeCircle = new NodeCircle(DEFAULT_RADIUS, newNode, insertionX, xSpacing);
        newNode.setCircle(newNodeCircle);

        anchorPane.getChildren().add(newNodeCircle);

        //adding connectors:
        if (newNode.isLeftChild()) {
            Connector newConnector = new Connector(newNode.getParent().getCircle(), newNode.getCircle());
            newNode.getParent().setLCToChild(newConnector);
            newNode.setCToParent(newConnector);
            anchorPane.getChildren().add(newConnector);
            connectorList.add(newConnector);
        } else if (newNode.isRightChild()) {
            Connector newConnector = new Connector(newNode.getParent().getCircle(), newNode.getCircle());
            newNode.getParent().setRCToChild(newConnector);
            newNode.setCToParent(newConnector);
            anchorPane.getChildren().add(newConnector);
            connectorList.add(newConnector);
        }

    }

    /**
     * Sets some instance variables when insert is clicked
     *
     * @param event
     */
    @FXML
    private void insertClicked(ActionEvent event) {
        radioInsert.setSelected(true);
        radioRemove.setSelected(false);
        insertClicked = true;
        removeClicked = false;
    }

    /**
     * Sets some instance variables when remove is clicked
     *
     * @param event
     */
    @FXML
    private void removeClicked(ActionEvent event) {
        radioRemove.setSelected(true);
        radioInsert.setSelected(false);
        removeClicked = true;
        insertClicked = false;
    }

    /**
     * Intializes the visualization controller
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
//        nodeList = new ArrayList<>();
        connectorList = new ArrayList<>();
    }

}