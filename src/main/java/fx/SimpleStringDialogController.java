package fx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class SimpleStringDialogController {

    @FXML
    private Label labelHeader;

    @FXML
    private TextArea textArea;

    @FXML
    private Button buttonOK;

    @FXML
    public void initialize(){

    }
    public void initStrings(String header, String text){
        labelHeader.setText(header);
        textArea.setText(text);
    }
    @FXML
    public void setButtonOkAction(ActionEvent event){
        Stage stage = (Stage) buttonOK.getScene().getWindow();
        stage.close();
    }
}
