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

    private double insertionX = 20;

    private List<NodeCircle> nodeList;
    private List<Connector> connectorList;

    private RedBlackTree<NodeCircle> mTree;

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
            Node newNode = new Node(value, Node.BLACK);
            NodeCircle newNodeCircle = new NodeCircle(10, newNode);

            int randomInt = ThreadLocalRandom.current().nextInt(20, 70);

//            newNodeCircle.setAlignment(Pos.CENTER);
            newNodeCircle.setPadding(new Insets(randomInt, 20, 20, insertionX));

            insertionX += 60;

            anchorPane.getChildren().add(newNodeCircle);

            nodeList.add(newNodeCircle);

            for (int i = 0; i < nodeList.size(); i++) {
                if (i%2 == 0 && (i != nodeList.size()-1)) {
                    Connector newConnector = new Connector(nodeList.get(i), nodeList.get(i+1));
                    anchorPane.getChildren().add(newConnector);
                    connectorList.add(newConnector);
                }
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
        nodeList = new ArrayList<>();
        connectorList = new ArrayList<>();
    }

}