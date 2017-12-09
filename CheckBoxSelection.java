
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.geometry.*;
import java.util.ArrayList;

public class CheckBoxSelection {

    private CheckBox[] selections;
    private Stage stage;
    private String stageTitle;

    //todo use
    private boolean userPressedOK = false;

    public CheckBoxSelection(CheckBox[] selections, String stageTitle) {
        this.selections = selections;
        this.stageTitle = stageTitle;
    }

    public static CheckBox[] getCheckBoxes(ArrayList<String> options) {
        CheckBox[] checkBoxes = new CheckBox[options.size()];
        for (int i = 0; i < options.size(); i++) {
            checkBoxes[i] = new CheckBox(options.get(i));
        }
        return checkBoxes;
    }

    public void show() {

        stage = new Stage();
        stage.setTitle(stageTitle);

        VBox checkBoxPane = new VBox();

        for (int i = 0; i < selections.length; i++) {
            CheckBox checkBox = selections[i];
            checkBox.setPadding(new Insets(5, 0, 5, 10));
            checkBoxPane.getChildren().add(checkBox);
        }

        GenericButtonSelection genericButtonSelection = new GenericButtonSelection();
        HBox paneButton = genericButtonSelection.getHBox();
        paneButton.setPadding(new Insets (10, 10, 10, 140));

        genericButtonSelection.getBtnOK().setOnAction(e -> {
            userPressedOK = true;
            stage.close();
        });
        genericButtonSelection.getBtnCancel().setOnAction(e -> {
            stage.close();
        });

        BorderPane paneMain = new BorderPane();
        paneMain.setTop(checkBoxPane);
        paneMain.setBottom(paneButton);

        Scene scene = new Scene(paneMain, 300, 300);
        stage.setScene(scene);
        stage.showAndWait();
    }

    public boolean getUserPressedOK() {
        return userPressedOK;
    }
}