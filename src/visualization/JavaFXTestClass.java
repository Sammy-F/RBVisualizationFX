package visualization;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * class that runs shit. yay.
 */
public class JavaFXTestClass extends Application {

    private final int WINDOW_WIDTH = 1000;
    private final int WINDOW_HEIGHT = 600;

    private AnchorPane content;

//    private ObservableList childList;

    private RedBlackTree rbTree;

    private Parent root;

    @Override
    public void start(Stage primaryStage) throws Exception {
        /**
         * Code for application.
         */
        root = FXMLLoader.load(getClass().getResource("visualization_region.fxml"));

//        childList = content.getChildren();

        Node blackTestNode = new Node(10, Node.BLACK);
        addNode(blackTestNode);

        Scene scene = new Scene(root);

        scene.setFill(Color.BEIGE);

        primaryStage.setTitle("A Screen");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Take a node and assign its color and draw it.
     * @param aNode
     */
    @FXML
    private void addNode(Node aNode) {

        //TODO:NODE ADDING
        //TODO:Automatically get color and assign location

//        rbTree.addValue(aNode.getValue());

        aNode.setColor(getColor());
        NodeCircle newNodeCircle = new NodeCircle(30, aNode);
        newNodeCircle.setAlignment(Pos.CENTER);
//        childList.add(newNodeCircle);
        newNodeCircle.setPadding(new Insets(WINDOW_HEIGHT/2, WINDOW_WIDTH/2, WINDOW_HEIGHT/2, WINDOW_WIDTH/2));

    }

    /**
     * Actually assign color. ^_^
     */
    private int getColor() {

        return Node.BLACK;

    }

    public void handleButtonAction() {
        //TODO:Handle button action
    }

    public static void main(String args[]) {
        launch(args);
    }

}
