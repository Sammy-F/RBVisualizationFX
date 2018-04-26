package visualization;

import javafx.collections.ObservableList;
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
    private List<Connector> connectorList;

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
        } else {
            deleteNode(value);

        }

    }

    //insert node by inserting into rbt data structure then redraw
    private void insertNode(Double value) {
        clearTree();
        mTree.insert(value);
        redraw(mTree.getRoot(), INIT_XSPACING, INIT_INSERTIONX, 0);
    }

    //delete node by deleting from rbt data structure then redraw
    private void deleteNode(double value) {
        Double key = (Double)value;
        clearTree();
        mTree.delete(key);
        redraw(mTree.getRoot(), INIT_XSPACING, INIT_INSERTIONX, 0);
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

    private void redraw(RedBlackNode thisRoot, double xSpacing, double xVal, int level) {
        RBNodeCircle<Double> thisCircle;
        Double thisInsertionX;
        if (thisRoot != mTree.getNil()) {
            if (thisRoot.getParent().getRight() == thisRoot) {
                thisInsertionX = xVal + xSpacing;
                thisCircle = new RBNodeCircle(DEFAULT_RADIUS, thisInsertionX, 3*level*DEFAULT_RADIUS + DEFAULT_RADIUS, thisRoot.getKey(), thisRoot.getColor());
            } else if (thisRoot.getParent().getLeft() == thisRoot) {
                thisInsertionX = xVal - xSpacing;
                thisCircle = new RBNodeCircle(DEFAULT_RADIUS, thisInsertionX, 3*level*DEFAULT_RADIUS + DEFAULT_RADIUS, thisRoot.getKey(), thisRoot.getColor());
            } else {
                thisInsertionX = xVal;
                thisCircle = new RBNodeCircle(DEFAULT_RADIUS, thisInsertionX, DEFAULT_RADIUS, thisRoot.getKey(), thisRoot.getColor());
            }
            anchorPane.getChildren().add(thisCircle); //first, add the node in
            circleList.add(thisCircle);
        } else {
            thisInsertionX = 0.0;
        }

        System.out.println(circleList.toString());

        //TODO: I DID NOT COMMENT OUT YOUR CODE BECAUSE I DISLIKE IT OR ANYTHING, JUST NEED TO REFACTOR BECAUSE OF ALTERATIONS TO NODE...
        //HOW TO ALTER:
        //TO CHECK FOR RIGHT CHILD, SIMPLY DO NODE.GETRIGHT(), AND THEN CHECK IF THIS IS NOT EQUAL TO TREE.GETNIL()! pretty simple.... we got this

        if (thisRoot.getRight() != mTree.getNil() && thisRoot.getRight() != mTree.getNil()) { //TODO: Add connectors
            redraw(thisRoot.getLeft(), xSpacing/2, thisInsertionX, level + 1);
            redraw(thisRoot.getRight(), xSpacing/2, thisInsertionX, level + 1);
        } else if (thisRoot.getRight() != mTree.getNil()) {
            redraw(thisRoot.getRight(), xSpacing/2,  thisInsertionX, level + 1);
        } else if (thisRoot.getLeft() != mTree.getNil()) {
            redraw(thisRoot.getLeft(), xSpacing/2, thisInsertionX, level + 1);
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
//        connectorList = new ArrayList<>();
        mTree = new RBTree();
        circleList = new ArrayList<>();
        connectorList = new ArrayList<>();
    }
}
