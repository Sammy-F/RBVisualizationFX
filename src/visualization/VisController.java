package visualization;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;

import javafx.event.ActionEvent;
import javafx.scene.layout.AnchorPane;

import javax.swing.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.concurrent.ThreadLocalRandom;

public class VisController implements Initializable {

    private boolean insertClicked = false;
    private boolean removeClicked = false;

    private boolean firstNode = true;

    private double insertionX = 80;

//    private List<NodeCircle> nodeList;
    private List<Connector> connectorList;

    private Node root;

//    private RedBlackTree<NodeCircle> mTree;

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
        Integer value = Integer.parseInt(tfValue.getText());

        if (insertClicked) {
            Node newNode;
            insertionX = 100;

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
                    if (n.getValue() < value) {
                        insertionX -= 20;
                        if (n.hasLeftChild()) {
                            n = n.getLeftChild();
                        } else {
                            haveNext = false;
                            p = n;
                            left = true;
                        }
                    } else {
                        insertionX += 20;
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
                newNode.setParent(p);
                if (left) {
                    p.setLeftChild(newNode);
                } else {
                    p.setRightChild(newNode);
                }
            }
            NodeCircle newNodeCircle = new NodeCircle(10, newNode);
            newNode.setCircle(newNodeCircle);

            int randomInt = ThreadLocalRandom.current().nextInt(20, 70);

//            newNodeCircle.setAlignment(Pos.CENTER);
//            newNodeCircle.setPadding(new Insets(randomInt, 20, 20, insertionX));
//            newNodeCircle.setPadding(new Insets(newNodeCircle.getThisNode().getLevel()*20, 20, 20, insertionX));
            newNodeCircle.setPadding(new Insets(newNode.getLevel()*20+20, 20, 20, insertionX));

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
            if (newNode.hasParent()) {
                Connector newConnector = new Connector(newNode.getParent().getCircle(), newNode.getCircle());
                anchorPane.getChildren().add(newConnector);
                connectorList.add(newConnector);
            }

        }
        else {
            for (javafx.scene.Node child : anchorPane.getChildren()) {
                if (child instanceof NodeCircle && (((NodeCircle) child).getValue() == value)) {
                    anchorPane.getChildren().remove(child);
                    break;
                }
            }
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