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
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.TimeLog;
import observer.TimeLogObserver;
import utils.DisplayDialogs;
import utils.Utils;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
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

    @FXML
    private Label labelBalance;

    @FXML
    private TextField textFieldBalance;

    @FXML
    private TextArea textAreaErrorPrompt;

    @FXML
    private MenuItem menuItemOpenDB;

    private ObservableList<TimeLog> data;


    public WindowController(){
        this.flexController = new FlexController();
    }

    @FXML
    public void initialize() throws IOException {
        flexController.addListener(this);

        //Initializes fields to only accept doubles
        Utils.initTextFormatter(textFieldWorkedHours);
        Utils.initTextFormatter(textFieldPlannedHours);
        Utils.initTextFormatter(textFieldLunch);
        datePicker.setValue(LocalDate.now());

        data = FXCollections.observableArrayList(flexController.getTimeLogsWithLimit(5));
        tableColumnDate.setCellValueFactory(new PropertyValueFactory<TimeLog, LocalDate>("workDay"));
        tableColumnWorkedHours.setCellValueFactory(new PropertyValueFactory<TimeLog, Double>("workedHours"));
        tableColumntBalance.setCellValueFactory(new PropertyValueFactory<TimeLog, Double>("balance"));
        tableColumntComment.setCellValueFactory(new PropertyValueFactory<TimeLog, String>("comment"));
        tableView.setItems(data);

        textFieldPlannedHours.setText(String.valueOf(flexController.getDueHours()));
        textFieldBalance.setText(String.format("%.2f", flexController.getTimeBalance()));
    }

    @FXML
    private void saveButtonAction(ActionEvent event) throws IOException {
        TimeLog timeLog = null;
        boolean isParseOK;

        //Check for future dates and in such case prevent user from saving data
        if (datePicker.getValue().isAfter(LocalDate.now())){
            DisplayDialogs dialogs = new DisplayDialogs();
            dialogs.displaySimpleDialog("Fel!", "Du kan inte registrera ett framtida datum.");
            return;
        }
        try {
            double workWithoutLunch = flexController.deductLunch(Double.parseDouble(textFieldWorkedHours.getText()),
                                Double.parseDouble(textFieldLunch.getText()));
            timeLog = new TimeLog(LocalDate.now(), datePicker.getValue(), textAreaComment.getText(),
                    workWithoutLunch, Double.parseDouble(textFieldPlannedHours.getText()));
            isParseOK = true;
        } catch (NumberFormatException nfe){
            isParseOK = false;
        }

        if (isParseOK){
            if (flexController.addToLog(timeLog)){
                DisplayDialogs dialog = new DisplayDialogs();
                dialog.displaySimpleDialog("Tidsregistrering klar",
                        String.format("Dagens arbetstid på %.1f är registrerad. %nDitt saldo för dagen är %.1f timmar.",
                                timeLog.getWorkedHours(), timeLog.getBalance()));
                textFieldWorkedHours.setText("");
                textFieldLunch.setText("");
                textFieldArriveAtWork.setText("");
                textFieldLeftWork.setText("");
            }
        }
    }

    @FXML
    private void setButtonGenerateTimeAction(ActionEvent event){
        String arrived = textFieldArriveAtWork.getText();
        String left = textFieldLeftWork.getText();
        double time = Utils.timeParser(arrived, left);
        if (time < 0) {
            textAreaErrorPrompt.setText("Du kan ange klockslagen i följande format:\n" +
                                        "0800 eller 08:00");
        } else {
            textFieldWorkedHours.setText(String.valueOf(time));
            textFieldLunch.requestFocus();
            textAreaErrorPrompt.setText("");

        }
    }

    @FXML
    private void setMenuItemCloseAction(ActionEvent event){
        Stage stage = (Stage) menuBar.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void setMenuItemOpenDBAction(ActionEvent event) throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Välj databasfil");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Sqlitefil *.db",
                                                                                "*.db"));
        File selectedfile = fileChooser.showOpenDialog(menuBar.getScene().getWindow());
        System.out.println(selectedfile.getAbsolutePath());
    }

    @FXML
    private void setMenuItemAboutAction(ActionEvent event) throws IOException {
        DisplayDialogs dialog = new DisplayDialogs();
        dialog.displaySimpleDialog("FlexTid","Applikationen är skapad av Fredrik Harnevik");
    }

    @FXML
    private void setButtonShowTimeLogsAction(ActionEvent event) throws IOException {
        Stage timeLogStage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fx/TableWindow.fxml"));
        Parent root = loader.load();
        TableWindowController tableWindowController = loader.getController();
        tableWindowController.setFlexController(this.flexController);
        timeLogStage.setTitle("Visa tidsregistreringar");
        timeLogStage.setScene(new Scene(root, 520, 600));
        timeLogStage.show();
    }

    @Override
    public void update() throws IOException {
        tableView.setItems(FXCollections.observableArrayList(flexController.getTimeLogsWithLimit(5)));
        textFieldBalance.setText(String.format("%.2f", flexController.getTimeBalance()));
    }
}
