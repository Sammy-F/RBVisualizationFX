package visualization;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
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

    private Deque<RBTree> backTreeStack;
    private Deque<RBTree> forwardTreeStack;

    Label infoText;

    @FXML
    private TextField tfValue;

    @FXML
    private RadioButton radioInsert;

    @FXML
    private RadioButton radioRemove;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private ScrollPane scrollPane;

    /**
     * Handles what to do when go is clicked; differs for insertions/deletions
     * @param event
     */
    @FXML
    private void handleAction(ActionEvent event) {

        backTreeStack.push(mTree.copy());                    //add old tree to back steps in case we choose to step back
        forwardTreeStack = new ArrayDeque<RBTree>();    //empty any stored forward steps


        try {
            Double value = Double.parseDouble(tfValue.getText()); //the value entered by user is set for the insert/delete

            if (insertClicked) {
                insertNode(value);
            } else {
                deleteNode(value);

            }
        } catch (NumberFormatException e) {
            mTree.getLog().addChange(LogModification.INVALIDINPUT, -1);
            infoText.setText(mTree.getLog().toString());
        }

        scrollPane.setVvalue(1.0);

    }


    @FXML
    private void handleBack(ActionEvent event) {
        if (backTreeStack.peek() != null) {
            forwardTreeStack.push(mTree.copy());

            mTree.getLogChanges().push(mTree.getLog().removeChange()); //For the log, we handle the Modifications
            infoText.setText(mTree.getLog().getLogString());
            scrollPane.setVvalue(1.0);

            mTree = backTreeStack.pop();
            updateTree();

        } else {
            infoText.setText("There's nothing left in your history.");
        }
    }

    @FXML
    private void handleForward(ActionEvent event) {
        if (forwardTreeStack.peek() != null) {
            backTreeStack.push(mTree.copy());

            try {
                LogModification oldMod = mTree.getLogChanges().pop();
                mTree.getLog().addChange(oldMod);
                infoText.setText(mTree.getLog().getLogString());
                scrollPane.setVvalue(1.0);
            } catch (NoSuchElementException e) {
                infoText.setText("Try adding or deleting a node to see what happens!");
            }

            mTree = forwardTreeStack.pop();
            updateTree();
        }
        else {
            infoText.setText("This is the most recent tree.");
        }
    }

    @FXML
    private void handleReset(ActionEvent event) {
        mTree.getLog().clearLog();
        clearTree();
        initStuff();
    }

    private void updateTree() {
        clearTree();
        redraw(mTree.getRoot(), INIT_XSPACING, INIT_INSERTIONX, 0);
    }


    //insert node by inserting into rbt data structure then redraw
    private void insertNode(Double value) {
        if (0 <= value && value <= 999) {
            mTree.insert(value);
            updateTree();
            infoText.setText(mTree.getLog().getLogString());
        } else {
//            mTree.setInsCase(RBTree.INSERTC0);
            mTree.getLog().addChange(LogModification.NODEISNIL, value);
            infoText = new Label(mTree.getLog().getLogString());
            anchorPane.getChildren().remove(infoText);
            scrollPane.setContent(infoText);
            anchorPane.getChildren().add(infoText);
//            infoText.setText(ModificationLog.getLogString());
        }
    }

    //delete node by deleting from rbt data structure then redraw
    private void deleteNode(double value) {
        if (0 <= value && value <= 999) {
            mTree.delete(value);
            updateTree();
            infoText.setText(mTree.getLog().getLogString());
        } else {
            mTree.getLog().addChange(LogModification.NODEISNIL, value);
            infoText.setText(mTree.getLog().getLogString());
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

    private void initStuff() {
        mTree = new RBTree();

        backTreeStack = new ArrayDeque<RBTree>();
        forwardTreeStack = new ArrayDeque<RBTree>();

        circleList = new ArrayList<>();
        connectorList = new ArrayList<>();

        infoText = new Label();
        infoText.setText("");
        infoText.setWrapText(true);
        scrollPane.setContent(infoText);
        scrollPane.setVvalue(1.0);
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
        initStuff();
    }
}
