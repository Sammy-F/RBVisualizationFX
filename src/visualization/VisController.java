package visualization;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;

import javafx.event.ActionEvent;
import java.net.URL;
import java.util.ResourceBundle;

public class VisController implements Initializable {

    @FXML
    private TextField tfValue;

    @FXML
    private RadioButton radioInsert;

    @FXML
    private RadioButton radioRemove;

    @FXML
    private void handleAction(ActionEvent event) {
        tfValue.setText("Clicked");
        System.out.println(tfValue.getText());
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

}
