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
            Node tobeDeleted = findNode(value, root);
            tobeDeleted.getCircle().setFillColor(Color.GOLDENROD);
            System.out.println("color changed");
            PauseTransition pause = new PauseTransition(Duration.seconds(.5));
            pause.setOnFinished(event1 -> tobeDeleted.getCircle().setFillColor(Color.DARKSLATEGRAY));
            pause.play();
        }

    }

    /**
     * Find the first node with the given value
     * @param value
     */
    private Node findNode(Double value, Node root) {

        if (root.getValue() == value) {
            return root;
        } else if (root.hasLeftChild() && root.hasRightChild()) {
            if (findNode(value, root.getLeftChild()) != null) {
                return findNode(value, root.getLeftChild());
            } else if (findNode(value, root.getRightChild()) != null) {
                return findNode(value, root.getRightChild());
            } else {
                return null;
            }
        } else if (root.hasLeftChild()) {
            if (findNode(value, root.getLeftChild()) != null) {
                return findNode(value, root.getLeftChild());
            }
        } else if (root.hasRightChild()) {
            if (findNode(value, root.getRightChild()) != null) {
                return findNode(value, root.getRightChild());
            }
        } else {
            return null;
        }

        return null;
    }

    private void deleteNode(Node nodeToDelete) {

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
        NodeCircle newNodeCircle = new NodeCircle(radius, newNode);
        newNode.setCircle(newNodeCircle);

//            newNodeCircle.setAlignment(Pos.CENTER);
//            newNodeCircle.setPadding(new Insets(randomInt, 20, 20, insertionX));
//            newNodeCircle.setPadding(new Insets(newNodeCircle.getThisNode().getLevel()*20, 20, 20, insertionX));
        newNodeCircle.setPadding(new Insets(newNode.getLevel()*40+20, 20, 20, insertionX));

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
        if (newNode.isLeftChild() || newNode.isRightChild()) {
            Connector newConnector = new Connector(newNode.getParent().getCircle(), newNode.getCircle());
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