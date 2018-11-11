package fx;

import controller.FlexController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import model.TimeLog;
import observer.TimeLogObserver;
import utils.DisplayDialog;
import utils.Utils;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParsePosition;
import java.time.LocalDate;
import java.util.Locale;

public class WindowController implements TimeLogObserver {
    private FlexController flexController;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private GridPane gridPane;

    @FXML
    private MenuBar menuBar;

    @FXML
    private Menu menuFile;

    @FXML
    private MenuItem menuItemClose;

    @FXML
    private Menu menuHelp;

    @FXML
    private MenuItem menuItemAbout;

    @FXML
    private Label labelWorkedHours;

    @FXML
    private Label labelPlannedHours;

    @FXML
    private TextField textFieldWorkedHours;

    @FXML
    private TextField textFieldLunch;

    @FXML
    private TextField textFieldPlannedHours;

    @FXML
    private Label labelComment;

    @FXML
    private DatePicker datePicker;

    @FXML
    private TextArea textAreaComment;

    @FXML
    private Label labelDatePicker;

    @FXML
    private Button buttonSave;

    @FXML
    private Button buttonShowTimeLogs;

    @FXML
    private TableView<TimeLog> tableView;

    @FXML
    private TableColumn<TimeLog, LocalDate> tableColumnDate;

    @FXML
    private TableColumn<TimeLog, Double> tableColumnWorkedHours;

    @FXML
    private TableColumn<TimeLog, Double> tableColumntBalance;

    @FXML
    private TableColumn<TimeLog, String> tableColumntComment;

    @FXML
    private Label labelTableView;

    @FXML
    private Label labelArriveAtWork;

    @FXML
    private Label labelLeftWork;

    @FXML
    private TextField textFieldArriveAtWork;

    @FXML
    private TextField textFieldLeftWork;

    @FXML
    private Button buttonGenerateTime;

    @FXML
    private Label labelLunch;

    private ObservableList<TimeLog> data;


    public WindowController(){
        this.flexController = new FlexController(7.5);
    }

    @FXML
    public void initialize() throws IOException {
        textFieldPlannedHours.setText(String.valueOf(flexController.getDueHours()));
        flexController.addListener(this);
        initTextFormatter(textFieldWorkedHours);
        initTextFormatter(textFieldPlannedHours);
        initTextFormatter(textFieldLunch);
        datePicker.setValue(LocalDate.now());
        data = FXCollections.observableArrayList(flexController.getTimeLogsWithLimit(5));
        tableColumnDate.setCellValueFactory(new PropertyValueFactory<TimeLog, LocalDate>("workDay"));
        tableColumnWorkedHours.setCellValueFactory(new PropertyValueFactory<TimeLog, Double>("workedHours"));
        tableColumntBalance.setCellValueFactory(new PropertyValueFactory<TimeLog, Double>("balance"));
        tableColumntComment.setCellValueFactory(new PropertyValueFactory<TimeLog, String>("comment"));
        tableView.setItems(data);
    }
    private void initTextFormatter(TextInputControl textInputControl){
        DecimalFormat format = (DecimalFormat) DecimalFormat.getInstance(Locale.US);

        textInputControl.setTextFormatter( new TextFormatter<>(c ->
        {
            if ( c.getControlNewText().isEmpty() )
            {
                return c;
            }

            ParsePosition parsePosition = new ParsePosition( 0 );
            Object object = format.parse( c.getControlNewText(), parsePosition );

            if ( object == null || parsePosition.getIndex() < c.getControlNewText().length() )
            {
                return null;
            }
            else
            {
                return c;
            }
        }));

    }

    @FXML
    private void saveButtonAction(ActionEvent event) throws IOException {
        TimeLog timeLog = null;
        boolean isParseOK;
        try {
            double workWithoutLunch = Utils.deductLunch(Double.parseDouble(textFieldWorkedHours.getText()),
                                Double.parseDouble(textFieldLunch.getText()));
            timeLog = new TimeLog(LocalDate.now(), datePicker.getValue(), textAreaComment.getText(),
                    workWithoutLunch, Double.parseDouble(textFieldPlannedHours.getText()));
            isParseOK = true;
        } catch (NumberFormatException nfe){
            isParseOK = false;
        }
        String header = "";
        String text = "";
        if (isParseOK){
            if (flexController.addToLog(timeLog)){
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fx/SimpleStringDialog.fxml"));
                Parent root = loader.load();
                SimpleStringDialogController simpleStringDialogController = loader.getController();
                simpleStringDialogController.initStrings("Tidsregistrering klar",
                        String.format("Dagens arbetstid på %.1f är registrerad. %nDitt saldo för dagen är %.1f timmar.",
                        timeLog.getWorkedHours(), timeLog.getBalance()));
                Stage dialogStage = new Stage();
                dialogStage.setTitle("Information");
                dialogStage.setScene(new Scene(root, 400, 300));
                dialogStage.show();
                textFieldWorkedHours.setText("");
                textFieldLunch.setText("");
            }
        }
    }

    @FXML
    private void setButtonGenerateTimeAction(ActionEvent event){
        textFieldWorkedHours.setText();
    }

    @FXML
    private void setMenuItemCloseAction(ActionEvent event){
        Stage stage = (Stage) menuBar.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void setMenuItemAboutAction(ActionEvent event) throws IOException {
        DisplayDialog dialog = new DisplayDialog();
        dialog.displaySimpleDialog("FlexTid","Applikationen är skapad av Fredrik Harnevik");
    }

    @Override
    public void update() throws IOException {
        tableView.setItems(FXCollections.observableArrayList(flexController.getTimeLogsWithLimit(5)));

    }
}
