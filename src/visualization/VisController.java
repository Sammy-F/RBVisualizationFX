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

public class VisController implements Initializable {

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

    @FXML
    private void handleAction(ActionEvent event) {
        Double value = Double.parseDouble(tfValue.getText());  //NOTE: WOULD WE WANT TO USE DOUBLES MAYBE? example: 4.5

        if (insertClicked) {
            insertNode(value);
        }
        else {
            tobeDeleted = findFirstNode(value, root);
            findDeepestNode(value, root);
            if (tobeDeleted != null) {
                tobeDeleted.getCircle().setFillColor(Color.GOLDENROD);
                PauseTransition pause = new PauseTransition(Duration.seconds(.5));
                pause.setOnFinished(event1 -> tobeDeleted.getCircle().setFillColor(Color.DARKSLATEGRAY));
                pause.play();
                PauseTransition pause2 = new PauseTransition(Duration.seconds(.5));
                pause2.setOnFinished(event1 -> deleteNode(tobeDeleted));
                pause2.play();
            } else {
                System.out.println("Node does not exist.");
            }
        }

    }


    /**
     * Modifies currentDeepest, which should be the first occurrence of the
     * node, found using findFirstNode
     * @param value
     * @param mRoot
     */
    private void findDeepestNode(Double value, Node mRoot) {

        System.out.println("modified");

        if (mRoot.getValue() == value) {
            if (mRoot.getLevel() > tobeDeleted.getLevel()) {
                tobeDeleted = mRoot;
            }
        }
        if (mRoot.hasLeftChild() && mRoot.hasRightChild()) {
            findDeepestNode(value, mRoot.getLeftChild());
        } else if (mRoot.hasLeftChild()) {
            findDeepestNode(value, mRoot.getLeftChild());
        } else if (mRoot.hasRightChild()) {
            findDeepestNode(value, mRoot.getRightChild());
        }
    }

    /**
     * Find the first node with the given value
     * @param value
     */
    private Node findFirstNode(Double value, Node root) {

        if (root.getValue() == value) {
            return root;
        } else if (root.hasLeftChild() && root.hasRightChild()) {
            if (findFirstNode(value, root.getLeftChild()) != null) {
                return findFirstNode(value, root.getLeftChild());
            } else if (findFirstNode(value, root.getRightChild()) != null) {
                return findFirstNode(value, root.getRightChild());
            } else {
                return null;
            }
        } else if (root.hasLeftChild()) {
            if (findFirstNode(value, root.getLeftChild()) != null) {
                return findFirstNode(value, root.getLeftChild());
            }
        } else if (root.hasRightChild()) {
            if (findFirstNode(value, root.getRightChild()) != null) {
                return findFirstNode(value, root.getRightChild());
            }
        } else {
            return null;
        }

        return null;
    }

    private void deleteNode(Node nodeToDelete) {

        if (!nodeToDelete.hasRightChild() && !nodeToDelete.hasLeftChild()) { //Check if the node has no children

            deleteNodeWithNoChildren(nodeToDelete);

        } else if (nodeToDelete.hasLeftChild() && !nodeToDelete.hasRightChild()) { //Check if the node has a left child

            deleteNodeWithLeftChild(nodeToDelete);

        } else if (nodeToDelete.hasRightChild() && !nodeToDelete.hasLeftChild()) { //Check if the node has a right child

            deleteNodeWithRightChild(nodeToDelete);

        } else { //Case where the node has two children (shit!)
            //TODO: Deletion with two nodes.
            System.out.println("I have two children and don't currently do anything.");
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
        } else if (nodeToDelete.isLeftChild()) {
            nodeToDelete.getParent().setLeftChild(null);
        } else {
            root = null;
        }
    }

    /**
     * Method for handling deletion of a node with a left child only
     * @param nodeToDelete
     */
    private void deleteNodeWithLeftChild(Node nodeToDelete) {

        anchorPane.getChildren().remove(nodeToDelete.getCircle());
        anchorPane.getChildren().remove(nodeToDelete.getCToParent());

        if (nodeToDelete.isLeftChild()) { //check if the node is a left child
            anchorPane.getChildren().remove(nodeToDelete.getLCToChild());
            Connector connector = new Connector(nodeToDelete.getParent().getCircle(), nodeToDelete.getLeftChild().getCircle());
            nodeToDelete.getLeftChild().setCToParent(connector);
            nodeToDelete.getParent().setlCToChild(connector);
            nodeToDelete.getLeftChild().notifyConnectorsUpdated();
            anchorPane.getChildren().add(connector);
        } else if (nodeToDelete.isRightChild()) { //check if the node is a right child
            anchorPane.getChildren().remove(nodeToDelete.getRCToChild());
            Connector connector = new Connector(nodeToDelete.getParent().getCircle(), nodeToDelete.getLeftChild().getCircle());
            nodeToDelete.getLeftChild().setCToParent(connector);
            nodeToDelete.getParent().setRCToChild(connector);
            nodeToDelete.getLeftChild().notifyConnectorsUpdated();
            nodeToDelete.getLeftChild().setIsRightChild(true);
            nodeToDelete.getLeftChild().setIsLeftChild(false);
            anchorPane.getChildren().add(connector);
        } else { //the node must be the root
            root = nodeToDelete.getLeftChild(); //TODO: Add handling for root case in node logic
            anchorPane.getChildren().remove(nodeToDelete.getCircle());
            anchorPane.getChildren().remove(nodeToDelete.getLCToChild());
            nodeToDelete.getLeftChild().setCToParent(null);
            nodeToDelete.getLeftChild().setIsLeftChild(false);
            nodeToDelete.getLeftChild().notifyConnectorsUpdated();
        }

        reduceTreeLevelsByOne(nodeToDelete);

    }

    /**
     * Method for handling deletion of a node with a right child only
     * @param nodeToDelete
     */
    private void deleteNodeWithRightChild(Node nodeToDelete) {

        System.out.println("The node is a right child: " + nodeToDelete.isRightChild());
        System.out.println("The node is a left child: " + nodeToDelete.isLeftChild());
        System.out.println("The node has a right child: " + nodeToDelete.hasRightChild());
        System.out.println("The node has a left child: " + nodeToDelete.hasLeftChild());

        anchorPane.getChildren().remove(nodeToDelete.getCircle());
        anchorPane.getChildren().remove(nodeToDelete.getRightChild().getCToParent());

        System.out.println("I have a right child.");

        if (nodeToDelete.isLeftChild()) {
            anchorPane.getChildren().remove(nodeToDelete.getParent().getLCToChild());
            Connector connector = new Connector(nodeToDelete.getParent().getCircle(), nodeToDelete.getRightChild().getCircle());
            nodeToDelete.getRightChild().setCToParent(connector);
            nodeToDelete.getParent().setlCToChild(connector);
            nodeToDelete.getRightChild().notifyConnectorsUpdated();
            nodeToDelete.getRightChild().setIsLeftChild(true);
            nodeToDelete.getRightChild().setIsRightChild(false);
            anchorPane.getChildren().add(connector);
        } else if (nodeToDelete.isRightChild()) {
            anchorPane.getChildren().remove(nodeToDelete.getParent().getRCToChild());
            Connector connector = new Connector(nodeToDelete.getParent().getCircle(), nodeToDelete.getRightChild().getCircle());
            nodeToDelete.getRightChild().setCToParent(connector);
            nodeToDelete.getParent().setRCToChild(connector);
            nodeToDelete.getRightChild().notifyConnectorsUpdated();
            anchorPane.getChildren().add(connector);
        } else {
            root = nodeToDelete.getRightChild(); //TODO: Add handling for root case in node logic
            anchorPane.getChildren().remove(nodeToDelete.getCircle());
            anchorPane.getChildren().remove(nodeToDelete.getRCToChild());
            nodeToDelete.getRightChild().setCToParent(null);
            nodeToDelete.getRightChild().setIsRightChild(false);
            nodeToDelete.getRightChild().notifyConnectorsUpdated();
        }

        reduceTreeLevelsByOne(nodeToDelete);

    }

    /**
     * Remove a node with two children
     */
    private void deleteNodeWithTwoChildren() {

        Node substitute = findMinOfSubtree(tobeDeleted.getRightChild());

        if (substitute.hasRightChild()) { //the node is not a leaf so we need to handle its subtree
            anchorPane.getChildren().remove(tobeDeleted); //first get rid of the original

        } else { //the node is a leaf (easy!)
            anchorPane.getChildren().remove(tobeDeleted);
            substitute.getCircle().setPadding(tobeDeleted.getCircle().getPadding()); // move the substitute to the old node's location
            substitute.getCircle().setXSpacing(tobeDeleted.getCircle().getxSpacing());
            substitute.getCircle().setInsertionX(tobeDeleted.getCircle().getInsertionX());

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

        toReduce.decrementLevel();

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

        if (toReduce.getLevel() == 0) {
            root = toReduce;
            toReduce.setIsLeftChild(false);
            toReduce.setIsRightChild(false);
        }

        toReduce.notifyConnectorsUpdated();

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
        double insertionX = INIT_INSERTIONX;           //ideally this would be the center of the screen
        double xSpacing = INIT_XSPACING;

        boolean left = false;
        boolean right = false;

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
            newNode = new Node(value, Node.BLACK, 1+p.getLevel());
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
            newNode.getParent().setlCToChild(newConnector);
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