package visualization;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * Red-Black Tree Visualization Application class
 *
 * Authors: Samantha Fritsche and Katya Gurgel
 */
public class RBJavaFXApplication extends Application {

    private final int WINDOW_WIDTH = 1000;
    private final int WINDOW_HEIGHT = 600;

    private AnchorPane content;

    private Parent root;

    /**
     * Code for application.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        root = FXMLLoader.load(getClass().getResource("visualization_region2.fxml"));


        Scene scene = new Scene(root);

        scene.setFill(Color.BEIGE);

        primaryStage.setTitle("Red-Black Tree Visualization");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
