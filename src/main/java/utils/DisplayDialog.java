package utils;

import fx.SimpleStringDialogController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class DisplayDialog {
    public void displaySimpleDialog(String header, String text) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fx/SimpleStringDialog.fxml"));
        Parent root = loader.load();
        SimpleStringDialogController simpleStringDialogController = loader.getController();
        simpleStringDialogController.initStrings(header,
                text);
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setTitle("Information");
        dialogStage.setScene(new Scene(root, 400, 300));
        dialogStage.showAndWait();

    }

}
