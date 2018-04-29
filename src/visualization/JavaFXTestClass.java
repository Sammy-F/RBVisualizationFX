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

    private Parent root;

    @Override
    public void start(Stage primaryStage) throws Exception {
        /**
         * Code for application.
         */
        root = FXMLLoader.load(getClass().getResource("visualization_region.fxml"));

//        childList = content.getChildren();

        Scene scene = new Scene(root);

        scene.setFill(Color.BEIGE);

        primaryStage.setTitle("Red-Black Tree Visualization");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

}
