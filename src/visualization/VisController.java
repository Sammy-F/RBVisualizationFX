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
import java.util.ResourceBundle;

public class VisController implements Initializable {

    private boolean insertClicked = false;
    private boolean removeClicked = false;

    private double insertionX = 20;

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

            newNodeCircle.setAlignment(Pos.CENTER);
//        childList.add(newNodeCircle);
            newNodeCircle.setPadding(new Insets(20, 20, 20, insertionX));

            insertionX += 60;

            anchorPane.getChildren().add(newNodeCircle);
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
    }

}
