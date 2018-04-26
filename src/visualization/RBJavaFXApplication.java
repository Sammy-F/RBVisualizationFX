package visualization;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class RBJavaFXApplication extends Application {

    private final int WINDOW_WIDTH = 1000;
    private final int WINDOW_HEIGHT = 600;

    private AnchorPane content;

    private Parent root;

    @Override
    public void start(Stage primaryStage) throws Exception {
        /**
         * Code for application.
         */
        root = FXMLLoader.load(getClass().getResource("visualization_region2.fxml"));

//        childList = content.getChildren();

        Scene scene = new Scene(root);

        scene.setFill(Color.BEIGE);

        primaryStage.setTitle("A Screen");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
