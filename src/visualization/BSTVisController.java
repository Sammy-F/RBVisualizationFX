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

public class BSTVisController implements Initializable {

    private final int DEFAULT_RADIUS = 30;
    private final int HEIGHT_SCALAR = 150;
    private final double INIT_INSERTIONX = 470;
    private final double INIT_XSPACING = INIT_INSERTIONX/2;

    private Node tobeDeleted;

    private boolean insertClicked = false;
    private boolean removeClicked = false;

//    private double insertionX;
//    private double xSpacing;

//    private List<NodeCircle> nodeList;
    private List<Connector> connectorList;

    private Node root = null;

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


//    /**
//     * Modifies mRoot, which initially should be the first occurrence of the
//     * node, found using findFirstNode, to recursively find the deepest occurrence of the value,
//     * if there is a duplicate
//     * @param value
//     * @param mRoot
//     */
//    private void findDeepestNode(Double value, Node mRoot) {
//
//        System.out.println("modified");
//
//        if (mRoot.getValue() == value) {
//            if (mRoot.getLevel() > tobeDeleted.getLevel()) {
//                tobeDeleted = mRoot;
//            }
//        }
//        if (mRoot.hasLeftChild() && mRoot.hasRightChild()) {
//            findDeepestNode(value, mRoot.getLeftChild());
//            findDeepestNode(value, mRoot.getRightChild());
//        } else if (mRoot.hasLeftChild()) {
//            findDeepestNode(value, mRoot.getLeftChild());
//        } else if (mRoot.hasRightChild()) {
//            findDeepestNode(value, mRoot.getRightChild());
//        }
//    }
//
//    /**
//     * Find the first node with the given value
//     * @param value
//     */
//    private Node findFirstNode(Double value, Node root) {
//
//        // This causes us to return as soon as we find a valid Node
//        if (root.getValue() == value) {
//            return root;
//        } else if (root.hasLeftChild() && root.hasRightChild()) {
//            if (findFirstNode(value, root.getLeftChild()) != null) {
//                return findFirstNode(value, root.getLeftChild());
//            } else if (findFirstNode(value, root.getRightChild()) != null) {
//                return findFirstNode(value, root.getRightChild());
//            } else {
//                return null;
//            }
//        } else if (root.hasLeftChild()) {
//            if (findFirstNode(value, root.getLeftChild()) != null) {
//                return findFirstNode(value, root.getLeftChild());
//            }
//        } else if (root.hasRightChild()) {
//            if (findFirstNode(value, root.getRightChild()) != null) {
//                return findFirstNode(value, root.getRightChild());
//            }
//        } else {
//            return null;
//        }
//
//        return null;
//    }

    //I think the function below finds the same thing as the two above combined, and also without recursion, which
    //may be less efficient (though do we really care about efficiency? not a lot...) and takes less space
    /**
     * Finds the lowest instance of the node to delete
     * @param value
     * @param root
     * @return
     */
    private Node findDelete(Double value, Node root) {
        Node toDelete = null;
        if (root.getValue() == value) {
            toDelete = root;
        }

        Node n = root;

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
            System.out.println("To delete has left child: " + toDelete.hasLeftChild());
            System.out.println("To delete has right child: " + toDelete.hasRightChild());
            System.out.println("To delete is left child: " + toDelete.isLeftChild());
            System.out.println("To delete is right child: " + toDelete.isRightChild());
            System.out.println("///////////////////////");
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
    private void deleteNode(Node nodeToDelete) {
        //TODO: I think there is stuff just buggy in general, even when there is a single child, because I can get
        //weirddd things to happen with a tree containing only parents of single children

        if (!nodeToDelete.hasRightChild() && !nodeToDelete.hasLeftChild()) { //Check if the node has no children

            deleteNodeWithNoChildren(nodeToDelete);

        } else if (nodeToDelete.hasLeftChild() && !nodeToDelete.hasRightChild()) { //Check if the node has a left child

            deleteNodeWithLeftChild(nodeToDelete);

        } else if (nodeToDelete.hasRightChild() && !nodeToDelete.hasLeftChild()) { //Check if the node has a right child

            deleteNodeWithRightChild(nodeToDelete);

        } else { //Case where the node has two children (shit!)
            //TODO: Deletion with two nodes.
            deleteNodeWithTwoChildren();
        }
    }

    /**
     * Method for handling deletion with no children
     * @param nodeToDelete
     */
    private void deleteNodeWithNoChildren(Node nodeToDelete) {
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
    private void deleteNodeWithLeftChild(Node nodeToDelete) { //Sorry, this is super convoluted!

        Node lc = nodeToDelete.getLeftChild(); //we know it has a left child, this var makes code more concise

        anchorPane.getChildren().remove(nodeToDelete.getCircle()); //remove the node from the scene

        reduceTreeLevelsByOne(lc);

        if (nodeToDelete.isLeftChild() || nodeToDelete.isRightChild()) {

            Node p = nodeToDelete.getParent(); //we know it has a parent

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

//            root = nodeToDelete.getLeftChild(); //TODO: Add handling for root case in node logic
//            anchorPane.getChildren().remove(nodeToDelete.getCircle());
//            anchorPane.getChildren().remove(nodeToDelete.getLCToChild());
//            nodeToDelete.getLeftChild().setCToParent(null);
//            nodeToDelete.getLeftChild().setIsLeftChild(false);
//            nodeToDelete.getLeftChild().notifyConnectorsUpdated();

//            anchorPane.getChildren().remove(nodeToDelete.getCircle());
//            anchorPane.getChildren().remove(nodeToDelete.getLCToChild());
//            root = lc;
//            root.setCToParent(null);
//            root.setIsLeftChild(false);
//            root.notifyConnectorsUpdated();

            anchorPane.getChildren().remove(nodeToDelete.getCircle());
            anchorPane.getChildren().remove(nodeToDelete.getLCToChild());

            lc.setCToParent(null);

            double ix = root.getCircle().getInsertionX();
            double xs = root.getCircle().getxSpacing();

            root = lc;

//            root.setLevel(1);
            root.setParent(null, false);

            root.setIsRightChild(false);
            root.setIsLeftChild(false);

            root.setCToParent(null);

//            root.getCircle().setInsertionX(ix);
//            root.getCircle().setInsertionX(xs);

            root.notifyConnectorsUpdated();  //TODO: CHECK if notifyConnectorsUpdated method MAYBE has bugs? I didn't get to that yet...
        }

//        reduceTreeLevelsByOne(lc);

    }

    /**
     * Method for handling deletion of a node with a right child only
     * @param nodeToDelete
     */
    private void deleteNodeWithRightChild(Node nodeToDelete) {

        Node rc = nodeToDelete.getRightChild(); //we know it has a right child, this var makes code more concise

        anchorPane.getChildren().remove(nodeToDelete.getCircle()); //remove the node from the scene

        reduceTreeLevelsByOne(rc);

        if (nodeToDelete.isLeftChild() || nodeToDelete.isRightChild()) {

            Node p = nodeToDelete.getParent(); //we know it has a parent

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

            //from the left child method, for reference:
//            root = nodeToDelete.getLeftChild(); //TODO: Add handling for root case in node logic
//            anchorPane.getChildren().remove(nodeToDelete.getCircle());
//            anchorPane.getChildren().remove(nodeToDelete.getLCToChild());
//            nodeToDelete.getLeftChild().setCToParent(null);
//            nodeToDelete.getLeftChild().setIsLeftChild(false);
//            nodeToDelete.getLeftChild().notifyConnectorsUpdated();

            //from right child method below
//            root = nodeToDelete.getRightChild(); //TODO: Add handling for root case in node logic
//            anchorPane.getChildren().remove(nodeToDelete.getCircle());
//            anchorPane.getChildren().remove(nodeToDelete.getRCToChild());
//            nodeToDelete.getRightChild().setCToParent(null);
//            nodeToDelete.getRightChild().setIsRightChild(false);
//            nodeToDelete.getRightChild().notifyConnectorsUpdated();

             //TODO: Add handling for root case in node logic
            anchorPane.getChildren().remove(nodeToDelete.getCircle());
            anchorPane.getChildren().remove(nodeToDelete.getRCToChild());

            rc.setCToParent(null);                             //trying to cover all bases, wherever bugs could happen

            double ix = root.getCircle().getInsertionX();
            double xs = root.getCircle().getxSpacing();

            root = rc;

//            root.setLevel(0);
            root.setParent(null, false);

            root.setIsRightChild(false);
            root.setIsLeftChild(false);

            root.setCToParent(null);

//            root.getCircle().setInsertionX(ix);
//            root.getCircle().setInsertionX(xs);

            root.notifyConnectorsUpdated();

            //TODO: CHECK, DOES THIS LOOK GOOD TO YOU? NOTE THAT I COPIED THIS OVER TO THE HAS LEFT CHILD METHOD ABOVE

            //I finally got it to maybe not bug out every time you remove a root!!!

//            rc.setCToParent(null);
//            rc.setIsRightChild(false);
//            rc.notifyConnectorsUpdated();
        }

//        reduceTreeLevelsByOne(rc);

    }

    //WHY DID I COMMENT: sorry I got genuinely confused by the differences between the function below and the one above,
    //and I had fixed a few things in the left child method, I made a new function by copying that one and refactoring
//    /**
//     * Method for handling deletion of a node with a right child only
//     * @param nodeToDelete
//     */
//    private void deleteNodeWithRightChild(Node nodeToDelete) {
//
////        System.out.println("The node is a right child: " + nodeToDelete.isRightChild());
////        System.out.println("The node is a left child: " + nodeToDelete.isLeftChild());
////        System.out.println("The node has a right child: " + nodeToDelete.hasRightChild());
////        System.out.println("The node has a left child: " + nodeToDelete.hasLeftChild());
//
//        Node rc = nodeToDelete.getRightChild(); //we know it has a left child, this var makes code more concise
//
//        anchorPane.getChildren().remove(nodeToDelete.getCircle());
//
////        System.out.println("I have a right child.");
//
//        if (nodeToDelete.isLeftChild() || nodeToDelete.isRightChild()) {
//
//            Node p = nodeToDelete.getParent(); //we know it has a parent
//
//            //remove the node's parent connector from the scene
////            anchorPane.getChildren().remove(rc.getCToParent()); //original, but I think this is buggy/at least not parallel to the deleteNodeWithLeftChild method
//            anchorPane.getChildren().remove(nodeToDelete.getCToParent()); //got confused, commented whole thing, easier to copy other function
//
//            if (nodeToDelete.isLeftChild()) {
//                anchorPane.getChildren().remove(nodeToDelete.getParent().getLCToChild());
//                Connector connector = new Connector(nodeToDelete.getParent().getCircle(), nodeToDelete.getRightChild().getCircle());
//
//                nodeToDelete.getRightChild().setCToParent(connector);
//                nodeToDelete.getParent().setLCToChild(connector);
//                nodeToDelete.getRightChild().notifyConnectorsUpdated();
//                nodeToDelete.getRightChild().setIsLeftChild(true);
//                nodeToDelete.getRightChild().setIsRightChild(false);
//                nodeToDelete.getParent().setLeftChild(nodeToDelete.getRightChild());
//                nodeToDelete.getRightChild().setParent(nodeToDelete.getParent(), true);
//                anchorPane.getChildren().add(connector);
//
//            } else if (nodeToDelete.isRightChild()) {
//
//                anchorPane.getChildren().remove(nodeToDelete.getParent().getRCToChild());
//                Connector connector = new Connector(nodeToDelete.getParent().getCircle(), nodeToDelete.getRightChild().getCircle());
//                nodeToDelete.getRightChild().setCToParent(connector);
//                nodeToDelete.getParent().setRCToChild(connector);
//                nodeToDelete.getRightChild().notifyConnectorsUpdated();
//                nodeToDelete.getParent().setRightChild(nodeToDelete.getRightChild());
//                nodeToDelete.getRightChild().setParent(nodeToDelete.getParent(), false);
//                anchorPane.getChildren().add(connector);
//            }
//
//        } else {
//            root = nodeToDelete.getRightChild(); //TODO: Add handling for root case in node logic
//            anchorPane.getChildren().remove(nodeToDelete.getCircle());
//            anchorPane.getChildren().remove(nodeToDelete.getRCToChild());
//            nodeToDelete.getRightChild().setCToParent(null);
//            nodeToDelete.getRightChild().setIsRightChild(false);
//            nodeToDelete.getRightChild().notifyConnectorsUpdated();
//        }
//
//        reduceTreeLevelsByOne(nodeToDelete.getRightChild());
//
//    }

    /**
     * Remove a node with two children
     */
    private void deleteNodeWithTwoChildren() {

        Node sub = findMinOfSubtree(tobeDeleted.getRightChild());

        // The substitute node can't have a left child, so it can only have a right child or no children
        if (sub.hasRightChild()) {
            //TODO: BETTER SOLUTION

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
//                sub.setHasRightChild(false);         //BUG????????????????????????????????????
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
    private Node findMinOfSubtree(Node root) {

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
    private void reduceTreeLevelsByOne(Node toReduce) {

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
//            root.getLeftChild().setCToParent(root.getCToParent());
//            root.getCToParent().setChildNode(root.getLeftChild().getCircle());
            reduceTreeLevelsByOne(toReduce.getLeftChild());
        } else if (toReduce.hasRightChild() && !toReduce.hasLeftChild()) {
//            root.getRightChild().setCToParent(root.getCToParent());
//            root.getCToParent().setChildNode(root.getRightChild().getCircle());
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

        Node newNode;
        double insertionX = INIT_INSERTIONX; //ideally this would be the center of the screen
        double xSpacing = INIT_XSPACING;

        boolean left = false;
        boolean right = false;

        int thisLevel = 0;

        if (root == null) {
            newNode = new Node(value, Node.BLACK, 0);
            root = newNode;
            root.setIsRightChild(false);
            root.setIsLeftChild(false);
        } else {
            boolean haveNext = true;
            Node n = root;

            Node p = n;

            while (haveNext) {
                thisLevel++;
                System.out.println("Insertion value = " + value);
                System.out.println("Check value = " + n.getValue());
                if (value < n.getValue()) {
//                        insertionX -= xSpacing/(n.getLevel()+1);  //need a way so kids don't overlap in very full tree
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
//                        insertionX += xSpacing/Math.pow((n.getLevel()+1),2);
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
            newNode = new Node(value, Node.BLACK, thisLevel);
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

        //TODO: Correct logic and remove this testing blurb.
        System.out.println("The root is a right child: " + root.isRightChild());
        System.out.println("The root is a left child: " + root.isLeftChild());
        System.out.println("The root has a right child: " + root.hasRightChild());
        System.out.println("The root has a left child: " + root.hasLeftChild());
        System.out.println("///////////////////////");
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

//            newNodeCircle.setAlignment(Pos.CENTER);
//            newNodeCircle.setPadding(new Insets(randomInt, 20, 20, insertionX));
//            newNodeCircle.setPadding(new Insets(newNodeCircle.getThisNode().getLevel()*20, 20, 20, insertionX));

//        newNodeCircle.setPadding(new Insets(newNode.getLevel()*40+20, 20, 20, insertionX)); //added this to the NodeCircle initialization instead

//            insertionX += 60;

        anchorPane.getChildren().add(newNodeCircle);

//            nodeList.add(newNodeCircle);
//
//            for (int i = 0; i < nodeList.size(); i++) {
//                if (i%2 == 0 && (i != nodeList.size()-1)) {
//                    Connector newConnector = new Connector(nodeList.get(i), nodeList.get(i+1));
//                    anchorPane.getChildren().add(newConnector);
//                    connectorList.add(newConnector);
//                }
//            }
        //new way of adding connectors:
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

        //print info about inserted node TODO: Remove when finished
        System.out.println("New node has left child: " + newNode.hasLeftChild());
        System.out.println("New node has right child: " + newNode.hasRightChild());
        System.out.println("New node is left child: " + newNode.isLeftChild());
        System.out.println("New node is right child: " + newNode.isRightChild());
        System.out.println("///////////////////////");

    }

    @FXML
    private void insertClicked(ActionEvent event) {
        radioInsert.setSelected(true);
        radioRemove.setSelected(false);
        insertClicked = true;
        removeClicked = false;
    }

    @FXML
    private void removeClicked(ActionEvent event) {
        radioRemove.setSelected(true);
        radioInsert.setSelected(false);
        removeClicked = true;
        insertClicked = false;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
//        nodeList = new ArrayList<>();
        connectorList = new ArrayList<>();
    }

}