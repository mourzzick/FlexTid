package fx;

import controller.FlexController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import model.TimeLog;

import javafx.event.ActionEvent;
import utils.DisplayDialog;
import utils.Utils;

import java.io.IOException;

public class EditTimeLogWindowController {

    private FlexController flexController;
    private TimeLog timeLog;

    @FXML
    private GridPane gridPane;

    @FXML
    private Label labelHeader;

    @FXML
    private Label labelDate;

    @FXML
    private Label labelWorkedHours;

    @FXML
    private Label labelPlannedHours;

    @FXML
    private Label labelComment;

    @FXML
    private TextField textFieldWorkedHours;

    @FXML
    private TextField textFieldPlannedHours;

    @FXML
    private TextArea textAreaComment;

    @FXML
    private Button buttonSave;

    @FXML
    private Button buttonCancel;

    @FXML
    private Button buttonDelete;

    @FXML
    private TextField textFieldDate;

    @FXML
    public void initialize(){
        Utils.initTextFormatter(textFieldPlannedHours);
        Utils.initTextFormatter(textFieldWorkedHours);
    }
    public void setTimeLog(TimeLog timeLog){
        this.timeLog = timeLog;
        textAreaComment.setText(timeLog.getComment());
        textFieldDate.setText(timeLog.getWorkDay().toString());
        textFieldDate.setBackground(Background.EMPTY);
        textFieldPlannedHours.setText(String.valueOf(timeLog.getDueHours()));
        textFieldWorkedHours.setText(String.valueOf(timeLog.getWorkedHours()));
        textFieldWorkedHours.requestFocus();
    }
    public void setFlexController(FlexController flexController){
        this.flexController = flexController;
    }

    @FXML
    private void setButtonSaveAction(ActionEvent event) throws IOException {
        timeLog.setDueHours(Double.parseDouble(textFieldPlannedHours.getText()));
        timeLog.setComment(textAreaComment.getText());
        timeLog.setWorkedHours(Double.parseDouble(textFieldWorkedHours.getText()));
        boolean isUpdateOK = flexController.updateTimeLog(timeLog);
        if (isUpdateOK) {
            DisplayDialog dialog = new DisplayDialog();
            dialog.displaySimpleDialog("Uppdatering klar",
                            "Uppdateringen lyckades, du kan nu se dina nya värden i tabellen.");
        }
        Stage stage = (Stage) textAreaComment.getScene().getWindow();
        stage.close();
    }
    @FXML
    private void setButtonCancelAction(ActionEvent event){
        Stage stage = (Stage) textAreaComment.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void setButtonDeleteAction(ActionEvent event) throws IOException {
        boolean isDeleteOK = flexController.deleteTimeLog(timeLog);
        if (isDeleteOK){
            DisplayDialog dialog = new DisplayDialog();
            dialog.displaySimpleDialog("Radering klar",
                    "Raderingen lyckades, tidsregistreringen är nu borttagen.");
        }
        Stage stage = (Stage) textAreaComment.getScene().getWindow();
        stage.close();
    }
}




























