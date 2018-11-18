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
import javafx.util.StringConverter;
import model.TimeLog;
import observer.TimeLogObserver;
import utils.DisplayDialog;
import utils.Utils;

import java.io.IOException;
import java.time.LocalDate;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

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
        this.flexController = new FlexController(7.5);
    }

    @FXML
    public void initialize() throws IOException {
        flexController.addListener(this);
        Utils.initTextFormatter(textFieldWorkedHours);
        Utils.initTextFormatter(textFieldPlannedHours);
        textFieldPlannedHours.setText(String.valueOf(flexController.getDueHours()));
        Utils.initTextFormatter(textFieldLunch);
        datePicker.setValue(LocalDate.now());

        data = FXCollections.observableArrayList(flexController.getTimeLogsWithLimit(5));
        tableColumnDate.setCellValueFactory(new PropertyValueFactory<TimeLog, LocalDate>("workDay"));
        tableColumnWorkedHours.setCellValueFactory(new PropertyValueFactory<TimeLog, Double>("workedHours"));
        tableColumntBalance.setCellValueFactory(new PropertyValueFactory<TimeLog, Double>("balance"));
        tableColumntComment.setCellValueFactory(new PropertyValueFactory<TimeLog, String>("comment"));
        tableView.setItems(data);

        textFieldBalance.setText(String.valueOf(flexController.getTimeBalance()));
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
                DisplayDialog dialog = new DisplayDialog();
                dialog.displaySimpleDialog("Tidsregistrering klar",
                        String.format("Dagens arbetstid på %.1f är registrerad. %nDitt saldo för dagen är %.1f timmar.",
                                timeLog.getWorkedHours(), timeLog.getBalance()));
                textFieldWorkedHours.setText("");
                textFieldLunch.setText("");
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
            textFieldWorkedHours.setText(String.valueOf(Utils.timeParser(arrived, left)));
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
        DisplayDialog dialog = new DisplayDialog();
        dialog.displaySimpleDialog("Fel", "Inte implementerat ännu.");
//        FileChooser fileChooser = new FileChooser();
//        fileChooser.setTitle("Välj databasfil");
//        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Sqlitedatabas",
//                                                                                "*.db"));
//        File selectedfile = fileChooser.showOpenDialog(menuBar.getScene().getWindow());
//
    }

    @FXML
    private void setMenuItemAboutAction(ActionEvent event) throws IOException {
        DisplayDialog dialog = new DisplayDialog();
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
        textFieldBalance.setText(String.valueOf(flexController.getTimeBalance()));
    }
}
