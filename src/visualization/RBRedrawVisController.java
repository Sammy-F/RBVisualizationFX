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
    private final double INIT_XSPACING = INIT_INSERTIONX/2;

    private boolean insertClicked = false;
    private boolean removeClicked = false;

//    private double insertionX;
//    private double xSpacing;

    private RBTree mTree;

    private List<NodeCircle> circleList;
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
    private void insertNode(double value) {
        Double key = (Double)value;
        clearTree();
        mTree.insert(key);
        redraw(mTree.getRoot());
    }

    //delete node by deleting from rbt data structure then redraw
    private void deleteNode(double value) {
        Double key = (Double)value;
        clearTree();
        mTree.delete(key);
        redraw(mTree.getRoot());
    }

    /**
     * Method clears all NodeCircles and Connectors
     */
    private void clearTree() {

        ObservableList<javafx.scene.Node> paneChildren = anchorPane.getChildren();

        paneChildren.removeAll(circleList);
        paneChildren.removeAll(connectorList);

    }

    private void redraw(RedBlackNode thisRoot) {
        anchorPane.getChildren().add(thisRoot.getCircle()); //first, add the node in

        //TODO: I DID NOT COMMENT OUT YOUR CODE BECAUSE I DISLIKE IT OR ANYTHING, JUST NEED TO REFACTOR BECAUSE OF ALTERATIONS TO NODE...
        //HOW TO ALTER:
        //TO CHECK FOR RIGHT CHILD, SIMPLY DO NODE.GETRIGHT(), AND THEN CHECK IF THIS IS NOT EQUAL TO TREE.GETNIL()! pretty simple.... we got this

//        if (thisRoot.hasRightChild() && thisRoot.hasLeftChild()) {
//            anchorPane.getChildren().add(thisRoot.getLCToChild()); //TODO: We will modify connectors in the insert/delete method, right?
//            anchorPane.getChildren().add(thisRoot.getRCToChild());
//            redraw(thisRoot.getLeftChild());
//            redraw(thisRoot.getRightChild());
//        } else if (thisRoot.hasRightChild()) {
//            anchorPane.getChildren().add(thisRoot.getRCToChild());
//            redraw(thisRoot.getRightChild());
//        } else if (thisRoot.hasLeftChild()) {
//            anchorPane.getChildren().add(thisRoot.getLCToChild());
//            redraw(thisRoot.getLeftChild());
//        }
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
