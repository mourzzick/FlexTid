package utils;

import fx.SimpleStringDialogController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

public class DisplayDialogs {
    public void displaySimpleDialog(String header, String text) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fx/SimpleStringDialog.fxml"));
        Parent root = loader.load();
        SimpleStringDialogController simpleStringDialogController = loader.getController();
        simpleStringDialogController.initStrings(header,
                text);
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setTitle("Information");
        dialogStage.setScene(new Scene(root, 300, 200));
        dialogStage.showAndWait();
    }
    public boolean displayConfirmDialog(String title, String header, String text){
        ButtonType yes = new ButtonType("Ja", ButtonBar.ButtonData.YES);
        ButtonType no = new ButtonType("Nej", ButtonBar.ButtonData.NO);
        Alert alert = new Alert(Alert.AlertType.WARNING, text, yes, no);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(text);
        alert.setGraphic(null);
        Optional<ButtonType> result = alert.showAndWait();
        return result.get().getButtonData() == ButtonBar.ButtonData.YES;
    }
}
