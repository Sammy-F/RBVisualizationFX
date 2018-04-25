package visualization;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;

import javafx.event.ActionEvent;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class RBVisController implements Initializable {

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

    //The underlines for duplicate code were annoying, feel free to uncomment

//    @FXML
//    private void handleAction(ActionEvent event) {
//        Double value = Double.parseDouble(tfValue.getText());  //NOTE: WOULD WE WANT TO USE DOUBLES MAYBE? example: 4.5
//
//        if (insertClicked) {
//            insertNode(value);
//        }
//        else {
//            tobeDeleted = findFirstNode(value, root);
//            findDeepestNode(value, root);
//            if (tobeDeleted != null) {
//                tobeDeleted.getCircle().setFillColor(Color.GOLDENROD);
//                PauseTransition pause = new PauseTransition(Duration.seconds(.5));
//                pause.setOnFinished(event1 -> tobeDeleted.getCircle().setFillColor(Color.DARKSLATEGRAY));
//                pause.play();
//                PauseTransition pause2 = new PauseTransition(Duration.seconds(.5));
//                pause2.setOnFinished(event1 -> deleteNode(tobeDeleted));
//                pause2.play();
//            } else {
//                System.out.println("Node does not exist.");
//            }
//        }
//
//    }
//
//    /**
//     * Modifies currentDeepest, which should be the first occurrence of the
//     * node, found using findFirstNode
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
//
//    /**
//     * Method for handling deletion of a node with a left child only
//     * @param nodeToDelete
//     */
//    private void deleteNode(Node nodeToDelete) {
//        //TODO: Node deletion
//    }
//
//    /**
//     * Find the minimum of the subtree, including the initial node
//     * @param root
//     * @return
//     */
//    private Node findMinOfSubtree(Node root) {
//
//        if (!root.hasLeftChild() && !root.hasRightChild()) { //I'm a leaf so just return me ^_^
//            return root;
//        } else if (!root.hasLeftChild() && root.hasRightChild()) { //I'm not a leaf but I'm the minimum. :O
//            return root;
//        } else { //I do have a left child still, hehe
//            return findMinOfSubtree(root.getLeftChild());
//        }
//
//    }
//
//    /**
//     * Decrements the level of all nodes in a subtree, including
//     * the root. In addition, it modifies the padding vals for nodes to the
//     * appropriate and notifies any Connectors of the change.
//     * @param toReduce
//     */
//    private void reduceTreeLevelsByOne(Node toReduce) {
//
//        toReduce.decrementLevel();
//
//        if (toReduce.isRightChild()) {
//            toReduce.getCircle().setInsertionX(toReduce.getCircle().getInsertionX() - ((toReduce.getCircle().getxSpacing()*2)));
//            toReduce.getCircle().setXSpacing(toReduce.getXSpacingFromLevel(INIT_XSPACING));
//            toReduce.getCircle().setPadding(new Insets(toReduce.getLevel()*HEIGHT_SCALAR+20, 20, 20,
//                    (toReduce.getCircle().getInsertionX())));
//        } else {
//            toReduce.getCircle().setInsertionX(toReduce.getCircle().getInsertionX()+(2*toReduce.getCircle().getxSpacing()));
//            toReduce.getCircle().setXSpacing(toReduce.getXSpacingFromLevel(INIT_XSPACING));
//            toReduce.getCircle().setPadding(new Insets(toReduce.getLevel()*HEIGHT_SCALAR+20, 20, 20,
//                    toReduce.getCircle().getInsertionX()));
//        }
//
//        if (toReduce.getLevel() == 0) {
//            root = toReduce;
//            toReduce.setIsLeftChild(false);
//            toReduce.setIsRightChild(false);
//        }
//
//        toReduce.notifyConnectorsUpdated();
//
//        if (!toReduce.hasLeftChild() && !toReduce.hasRightChild()) {
//        }
//        else if (toReduce.hasLeftChild() && !toReduce.hasRightChild()) {
////            root.getLeftChild().setCToParent(root.getCToParent());
////            root.getCToParent().setChildNode(root.getLeftChild().getCircle());
//            reduceTreeLevelsByOne(toReduce.getLeftChild());
//        } else if (toReduce.hasRightChild() && !toReduce.hasLeftChild()) {
////            root.getRightChild().setCToParent(root.getCToParent());
////            root.getCToParent().setChildNode(root.getRightChild().getCircle());
//            reduceTreeLevelsByOne(toReduce.getRightChild());
//        } else {
//            reduceTreeLevelsByOne(toReduce.getLeftChild());
//            reduceTreeLevelsByOne(toReduce.getRightChild());
//        }
//    }

    /**
     * Handles insertion of a new node with the value
     * input by the user.
     */
    private void insertNode(Double value) {

        //TODO: Node Insertion

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