package fx;

import controller.FlexController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import model.TimeLog;

import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

public class TableWindowController {

    public TableWindowController(){
        this.flexController = new FlexController(7.5);
    }
    private FlexController flexController;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private GridPane gridPane;

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
    private DatePicker datePickerFrom;

    @FXML
    private DatePicker datePickerTo;

    @FXML
    private Button buttonUpdateTable;

    @FXML
    private Button buttonExport;

    private ObservableList<TimeLog> data;

    public void initialize() throws IOException {
        LocalDate from = LocalDate.now().withDayOfMonth(1);
        LocalDate to = LocalDate.now().with(TemporalAdjusters.lastDayOfMonth());
        datePickerFrom.setValue(from);
        datePickerTo.setValue(to);
        data = FXCollections.observableArrayList(flexController.getTimeLogs(from, to));
        tableColumnDate.setCellValueFactory(new PropertyValueFactory<TimeLog, LocalDate>("workDay"));
        tableColumnWorkedHours.setCellValueFactory(new PropertyValueFactory<TimeLog, Double>("workedHours"));
        tableColumntBalance.setCellValueFactory(new PropertyValueFactory<TimeLog, Double>("balance"));
        tableColumntComment.setCellValueFactory(new PropertyValueFactory<TimeLog, String>("comment"));
        tableView.setItems(data);


    }

    @FXML
    private void setButtonUpdateTableAction(ActionEvent event) throws IOException {
        data = FXCollections.observableArrayList(flexController.getTimeLogs(datePickerFrom.getValue(),
                                                                            datePickerTo.getValue()));
        tableView.setItems(data);
    }

}
