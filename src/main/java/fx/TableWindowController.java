package fx;

import controller.FlexController;
import export.ExportService;
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
import org.apache.poi.ss.usermodel.Workbook;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class TableWindowController implements TimeLogObserver {

    public TableWindowController(){
        this.flexController = new FlexController();
    }
    private FlexController flexController;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private GridPane gridPane;

    @FXML
    private DatePicker datePickerFrom;

    @FXML
    private DatePicker datePickerTo;

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
    private Label labelDatePickerFrom;

    @FXML
    private Label labelDatePickerTo;

    @FXML
    private Button buttonUpdateTable;

    @FXML
    private Button buttonExport;

    @FXML
    private Button buttonEditTimeLog;

    @FXML
    private TextField textFieldErrorPrompt;

    private ObservableList<TimeLog> data;

    public void initialize() throws IOException {
        flexController.addListener(this);
        LocalDate from = LocalDate.now().minusMonths(1);
        LocalDate to = LocalDate.now();
        datePickerFrom.setValue(from);
        datePickerTo.setValue(to);
        data = FXCollections.observableArrayList(flexController.getTimeLogs(from, to));
        tableColumnDate.setCellValueFactory(new PropertyValueFactory<TimeLog, LocalDate>("workDay"));
        tableColumnWorkedHours.setCellValueFactory(new PropertyValueFactory<TimeLog, Double>("workedHours"));
        tableColumntBalance.setCellValueFactory(new PropertyValueFactory<TimeLog, Double>("balance"));
        tableColumntComment.setCellValueFactory(new PropertyValueFactory<TimeLog, String>("comment"));
        tableView.setItems(data);
        datePickerFrom.requestFocus();
    }

    public void setFlexController(FlexController controller){
        this.flexController = controller;
        flexController.addListener(this);
    }

    @FXML
    private void setButtonUpdateTableAction(ActionEvent event) throws IOException {
        data = FXCollections.observableArrayList(flexController.getTimeLogs(datePickerFrom.getValue(),
                                                                            datePickerTo.getValue()));
        tableView.setItems(data);
    }

    @FXML
    private void setButtonEditTimeLogAction(ActionEvent event) throws IOException {
        TimeLog timeLog = tableView.getSelectionModel().getSelectedItem();
        if (timeLog == null) {
            textFieldErrorPrompt.setText("Du måste markera en rad!");
        } else {
            textFieldErrorPrompt.setText("");
            Stage editTimeLogStage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fx/EditTimeLogWindow.fxml"));
            Parent root = loader.load();
            EditTimeLogWindowController editTimeLogWindowController = loader.getController();
            editTimeLogStage.setTitle("Redigera tidsregistrering");
            editTimeLogStage.setScene(new Scene(root, 600, 400));
            editTimeLogWindowController.setTimeLog(timeLog);
            editTimeLogWindowController.setFlexController(this.flexController);
            editTimeLogStage.show();
        }
    }
    @Override
    public void update() throws IOException {
        data = FXCollections.observableArrayList(flexController.getTimeLogs(datePickerFrom.getValue(),
                datePickerTo.getValue()));
        tableView.setItems(data);
    }

    @FXML
    private void setButtonExportAction(ActionEvent event) throws IOException {
        ExportService exportService = new ExportService();
        Workbook workbook = exportService.exportToExcel(tableView.getItems());
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Spara dokument...");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Excelarbetsbok", "*.xlsx"));
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        //fileChooser.setInitialFileName(String.format("%s_%s%s", "Export", dateTimeFormatter.format(LocalDateTime.now()), ".xlsx"));
        File file = fileChooser.showSaveDialog(datePickerFrom.getScene().getWindow());
        if (file != null) {
        OutputStream outputStream = new FileOutputStream(file);
        workbook.write(outputStream);
        }
    }
}
