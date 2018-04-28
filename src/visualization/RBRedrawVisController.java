package visualization;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;

import javafx.event.ActionEvent;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.*;

/**
 * Creates RB Tree Visualization by redrawing tree each time.
 */
public class RBRedrawVisController implements Initializable {

    private final int DEFAULT_RADIUS = 30;
    private final int HEIGHT_SCALAR = 150;
    private final double INIT_INSERTIONX = 470;
    private final double INIT_XSPACING = INIT_INSERTIONX;

    private boolean insertClicked = false;
    private boolean removeClicked = false;

    private RBTree<Double> mTree; //we need to set the input class or we get an unchecked call error

    private List<RBNodeCircle> circleList;
    private List<RBConnector> connectorList;

    @FXML
    private TextField tfValue;

    @FXML
    private RadioButton radioInsert;

    @FXML
    private RadioButton radioRemove;

    @FXML
    private AnchorPane anchorPane;
//
//    private RBTree<Double> backTree;
//
//    private RBTree<Double> backBackTree;
//
//    private RBTree<Double> forwardTree;
//
//    private RBTree<Double> forwardForwardTree;

    private Deque<RBTree> backTreeStack;

    private Deque<RBTree> forwardTreeStack;

    /**
     * Handles what to do when go is clicked; differs for insertions/deletions
     * @param event
     */
    @FXML
    private void handleAction(ActionEvent event) {

//        backBackTree = backTree;
//        backTree = mTree;
//
//        forwardTree = new RBTree();         //overwrite any forward steps we were saving before
//        forwardForwardTree = new RBTree();


        backTreeStack.push(mTree.copy());                    //add old tree to back steps in case we choose to step back
        forwardTreeStack = new ArrayDeque<RBTree>();    //empty any stored forward steps


        Double value = Double.parseDouble(tfValue.getText()); //the value entered by user is set for the insert/delete

        if (insertClicked) {
            insertNode(value);
        } else {
            deleteNode(value);

        }

    }


    @FXML
    private void handleBack(ActionEvent event) {
        if (backTreeStack.peek() != null) {
            forwardTreeStack.push(mTree.copy());
            mTree = backTreeStack.pop();
            updateTree();
        }
    }

    @FXML
    private void handleForward(ActionEvent event) {
        if (forwardTreeStack.peek() != null) {
            backTreeStack.push(mTree.copy());
            mTree = forwardTreeStack.pop();
            updateTree();
        }
    }

    private void updateTree() {
        clearTree();
        redraw(mTree.getRoot(), INIT_XSPACING, INIT_INSERTIONX, 0);
    }


    //insert node by inserting into rbt data structure then redraw
    private void insertNode(Double value) {
        if (0 <= value && value < 999) {

//            if (value % .1 > 0) {
//                if (value % .1 < .05) {
//                    value = value - (value % .1);       //round to nearest .1
//                } else {
//                    value = value - (value % .1) + .1;
//                }
//            }

            mTree.insert(value);
            updateTree();
        }
    }

    //delete node by deleting from rbt data structure then redraw
    private void deleteNode(double value) {
        if (0 <= value && value < 999) {

//            if (value % .1 > 0) {
//                if (value % .1 < .05) {
//                    value = value - (value % .1);       //round to nearest .1
//                } else {
//                    value = value - (value % .1) + .1;
//                }
//            }

            mTree.delete(value);
            updateTree();
        }
    }

    /**
     * Method clears all NodeCircles and Connectors
     */
    private void clearTree() {

        ObservableList<javafx.scene.Node> paneChildren = anchorPane.getChildren();

        paneChildren.removeAll(circleList);
        paneChildren.removeAll(connectorList);

        circleList.clear();
        connectorList.clear();

    }

    /**
     * Method redraws the entire RBTree recursively. As each new RBNodeCircle is added,
     * it is returned at the end so that we can generate an RBConnector.
     * @param thisRoot
     * @param xSpacing
     * @param xVal
     * @param level
     * @return
     */
    private RBNodeCircle redraw(RedBlackNode thisRoot, double xSpacing, double xVal, int level) {
        RBNodeCircle<Double> thisCircle;
        Double thisInsertionX;
        if (thisRoot != mTree.getNil()) {
            if (thisRoot.getParent().getRight() == thisRoot) {
                thisInsertionX = xVal + xSpacing;
                thisCircle = new RBNodeCircle(DEFAULT_RADIUS, thisInsertionX, 3.5*level*DEFAULT_RADIUS + DEFAULT_RADIUS, thisRoot.getKey(), thisRoot.getColor());
            } else if (thisRoot.getParent().getLeft() == thisRoot) {
                thisInsertionX = xVal - xSpacing;
                thisCircle = new RBNodeCircle(DEFAULT_RADIUS, thisInsertionX, 3.5*level*DEFAULT_RADIUS + DEFAULT_RADIUS, thisRoot.getKey(), thisRoot.getColor());
            } else {
                thisInsertionX = xVal;
                thisCircle = new RBNodeCircle(DEFAULT_RADIUS, thisInsertionX, DEFAULT_RADIUS, thisRoot.getKey(), thisRoot.getColor());
            }
            anchorPane.getChildren().add(thisCircle); //first, add the node in              //TODO: CURRENTLY GET ERROR HERE!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            circleList.add(thisCircle);
        } else {
            thisCircle = new RBNodeCircle<>();
            thisInsertionX = 0.0;
        }

        if (thisRoot.getRight() != mTree.getNil() && thisRoot.getLeft() != mTree.getNil()) { //TODO: Add connectors
            RBConnector mLConnector = new RBConnector(thisCircle, redraw(thisRoot.getLeft(), xSpacing/2, thisInsertionX, level + 1));   //TODO: AND ERROR HERE!!!!!!!!!!!!!!!!
            connectorList.add(mLConnector);
            anchorPane.getChildren().add(mLConnector);

            RBConnector mRConnector = new RBConnector(thisCircle, redraw(thisRoot.getRight(), xSpacing/2, thisInsertionX, level + 1));  //TODO: AND ERROR HERE!!!!!!!!!!!!!!!!
            connectorList.add(mRConnector);
            anchorPane.getChildren().add(mRConnector);
        } else if (thisRoot.getRight() != mTree.getNil()) {
            RBConnector mRConnector = new RBConnector(thisCircle, redraw(thisRoot.getRight(), xSpacing/2, thisInsertionX, level + 1));
            connectorList.add(mRConnector);
            anchorPane.getChildren().add(mRConnector);
        } else if (thisRoot.getLeft() != mTree.getNil()) {
            RBConnector mLConnector = new RBConnector(thisCircle, redraw(thisRoot.getLeft(), xSpacing/2, thisInsertionX, level + 1));
            connectorList.add(mLConnector);
            anchorPane.getChildren().add(mLConnector);
        }

        return thisCircle;

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
        mTree = new RBTree();

        backTreeStack = new ArrayDeque<RBTree>();
        forwardTreeStack = new ArrayDeque<RBTree>();

        circleList = new ArrayList<>();
        connectorList = new ArrayList<>();
    }
}
