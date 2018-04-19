package visualization;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;

import javafx.event.ActionEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import javax.swing.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.concurrent.ThreadLocalRandom;

import java.lang.Math;
import java.util.concurrent.TimeUnit;

public class VisController implements Initializable {

    private Node tobeDeleted;

    private boolean insertClicked = false;
    private boolean removeClicked = false;

    private boolean firstNode = true;

    private double insertionX;
    private double xSpacing;

//    private List<NodeCircle> nodeList;
    private List<Connector> connectorList;

    private Node root;

//    private RedBlackTree<NodeCircle> mTree;
    private double radius = 15;

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
     * @param root
     */
    private void findDeepestNode(Double value, Node root) {

        System.out.println("modified");

        if (root.getValue() == value) {
            if (root.getLevel() > tobeDeleted.getLevel()) {
                tobeDeleted = root;
            }
        }
        if (root.hasLeftChild() && root.hasRightChild()) {
            findDeepestNode(value, root.getLeftChild());
        } else if (root.hasLeftChild()) {
            findDeepestNode(value, root.getLeftChild());
        } else if (root.hasRightChild()) {
            findDeepestNode(value, root.getRightChild());
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
            System.out.println("I have no children.");
            anchorPane.getChildren().remove(nodeToDelete.getCircle());
            anchorPane.getChildren().remove(nodeToDelete.getCToParent());
            if (nodeToDelete.isRightChild()) {
                nodeToDelete.getParent().setRightChild(null);
            } else {
                nodeToDelete.getParent().setLeftChild(null);
            }
        } else if (nodeToDelete.hasLeftChild() && !nodeToDelete.hasRightChild()) { //Check if the node has a left child
            anchorPane.getChildren().remove(nodeToDelete.getCircle());
            anchorPane.getChildren().remove(nodeToDelete.getLeftChild().getCToParent());

            System.out.println("I have a left child.");

            if (nodeToDelete.isLeftChild()) {
                nodeToDelete.getParent().getLCToChild().setChildNode(nodeToDelete.getLeftChild().getCircle());  //switch the parent's connector to have the new node
                nodeToDelete.getLeftChild().setCToParent(nodeToDelete.getCToParent()); //switch the child node to use the parent's connector
                nodeToDelete.getParent().setLeftChild(nodeToDelete.getLeftChild());
                nodeToDelete.getLeftChild().setParent(nodeToDelete.getParent(), true);
                nodeToDelete.getLeftChild().notifyConnectorsUpdated();
            } else if (nodeToDelete.isRightChild()) {
                nodeToDelete.getParent().getRCToChild().setChildNode(nodeToDelete.getLeftChild().getCircle());  //switch the parent's connector to have the new node
                nodeToDelete.getLeftChild().setCToParent(nodeToDelete.getCToParent()); //switch the child node to use the parent's connector
                nodeToDelete.getParent().setLeftChild(nodeToDelete.getLeftChild());
                nodeToDelete.getLeftChild().setParent(nodeToDelete.getParent(), true);
                nodeToDelete.getLeftChild().notifyConnectorsUpdated();
            } else {
                root = nodeToDelete.getLeftChild(); //TODO: Add handling for root case in node logic
            }

            reduceTreeLevelsByOne(nodeToDelete);

        } else if (nodeToDelete.hasRightChild() && !nodeToDelete.hasLeftChild()) { //Check if the node has a right child
            anchorPane.getChildren().remove(nodeToDelete.getCircle());
            anchorPane.getChildren().remove(nodeToDelete.getRightChild().getCToParent());

            System.out.println("I have a right child.");

            if (nodeToDelete.isLeftChild()) {
                nodeToDelete.getParent().getLCToChild().setChildNode(nodeToDelete.getRightChild().getCircle());  //switch the parent's connector to have the new node
                nodeToDelete.getRightChild().setCToParent(nodeToDelete.getCToParent()); //switch the child node to use the parent's connector
                nodeToDelete.getParent().setLeftChild(nodeToDelete.getRightChild());
                nodeToDelete.getRightChild().setParent(nodeToDelete.getParent(), true);
                nodeToDelete.getRightChild().notifyConnectorsUpdated();
            } else if (nodeToDelete.isRightChild()) {
                nodeToDelete.getParent().getRCToChild().setChildNode(nodeToDelete.getRightChild().getCircle());
                nodeToDelete.getRightChild().setCToParent(nodeToDelete.getCToParent());
                nodeToDelete.getParent().setRightChild(nodeToDelete.getRightChild());
                nodeToDelete.getRightChild().setParent(nodeToDelete.getParent(), false);
                nodeToDelete.getRightChild().notifyConnectorsUpdated();
            } else {
                root = nodeToDelete.getRightChild(); //TODO: Add handling for root case in node logic
            }

        } else { //Case where the node has two children (shit!)
            //TODO: Deletion with two nodes.
            System.out.println("I have two children and don't currently do anything.");
        }
    }

    /**
     * Decrements the level of all nodes in a subtree, including
     * the root. In addition, it modifies the padding vals for nodes to the
     * appropriate and notifies any Connectors of the change.
     * @param toReduce
     */
    private void reduceTreeLevelsByOne(Node toReduce) {

        if (!toReduce.hasLeftChild() && !toReduce.hasRightChild()) {
            toReduce.decrementLevel();
            toReduce.getCircle().setPadding(new Insets(toReduce.getLevel()*40+20, 20, 20, toReduce.getCircle().getInsertionX() + ((toReduce.getCircle().getxSpacing()*2)-(toReduce.getCircle().getxSpacing()))));
            toReduce.notifyConnectorsUpdated();
        }
        else if (toReduce.hasLeftChild() && !toReduce.hasRightChild()) {

            toReduce.decrementLevel();
//            root.getLeftChild().setCToParent(root.getCToParent());
//            root.getCToParent().setChildNode(root.getLeftChild().getCircle());
            toReduce.getCircle().setPadding(new Insets(toReduce.getLevel()*40+20, 20, 20, toReduce.getCircle().getInsertionX() + ((toReduce.getCircle().getxSpacing()*2)-(toReduce.getCircle().getxSpacing()))));
            toReduce.notifyConnectorsUpdated();
            reduceTreeLevelsByOne(toReduce.getLeftChild());
        } else if (toReduce.hasRightChild() && !toReduce.hasLeftChild()) {
            toReduce.decrementLevel();
//            root.getRightChild().setCToParent(root.getCToParent());
//            root.getCToParent().setChildNode(root.getRightChild().getCircle());
            toReduce.getCircle().setPadding(new Insets(toReduce.getLevel()*40+20, 20, 20, toReduce.getCircle().getInsertionX() + ((toReduce.getCircle().getxSpacing()*2)-(toReduce.getCircle().getxSpacing()))));
            toReduce.notifyConnectorsUpdated();
            reduceTreeLevelsByOne(toReduce.getRightChild());
        } else {
            toReduce.decrementLevel();
            toReduce.getCircle().setPadding(new Insets(toReduce.getLevel()*40+20, 20, 20, toReduce.getCircle().getInsertionX() + ((toReduce.getCircle().getxSpacing()*2)-(toReduce.getCircle().getxSpacing()))));
            toReduce.notifyConnectorsUpdated();
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
        insertionX = 370;           //ideally this would be the center of the screen
        xSpacing = insertionX/2;

        if (firstNode) {
            newNode = new Node(value, Node.BLACK, 0);
            root = newNode;
            firstNode = false;
        } else {
            boolean haveNext = true;
            Node n = root;

            Node p = n;

            boolean left = false;
            boolean right = false;

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
            } else {
                p.setRightChild(newNode);
            }
        }
        NodeCircle newNodeCircle = new NodeCircle(radius, newNode, insertionX, xSpacing);
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