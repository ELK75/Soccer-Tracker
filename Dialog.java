
import java.util.Optional;
import java.util.ArrayList;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.scene.layout.*;
import javafx.util.*;

public class Dialog {
    
    // displays a drop-down menu of options for user to choose
    public static int getChoice(String title, String header, String content, 
    ArrayList<String> options) {
        String firstOption = options.get(0);
        ChoiceDialog<String> choiceDialog = new ChoiceDialog<>(firstOption, options);
        choiceDialog.setTitle(title);
        choiceDialog.setHeaderText(header);
        choiceDialog.setContentText(content);
        choiceDialog.getDialogPane().setPrefSize(350, 100);

        Optional<String> choice = choiceDialog.showAndWait();

        if (choice.isPresent()) return options.indexOf(choice.get());
        // returns -1 to let other functions know user aborted dialog
        else return -1;
    }

    public static String getTextInput(String title, String header, String content) {
        TextInputDialog inputDialog = new TextInputDialog();
        inputDialog.setTitle(title);
        inputDialog.setHeaderText(header);
        inputDialog.setContentText(content);
        inputDialog.getDialogPane().setPrefSize(350, 100);

        Optional<String> input = inputDialog.showAndWait();
        
        if (input.isPresent()) return input.get();
        else return null;
    }

    public static boolean getConfirmation(String title, String header, String content) {
        Alert confirmationDialog = new Alert(AlertType.CONFIRMATION);
        confirmationDialog.setTitle(title);
        confirmationDialog.setHeaderText(header);
        confirmationDialog.setContentText(content);
        confirmationDialog.getDialogPane().setPrefSize(350, 100);

        Optional<ButtonType> selectedButton = confirmationDialog.showAndWait();
        if (selectedButton.get() == ButtonType.OK) {
            return true;
        } else {
            return false;
        }   
    }

    public static void showMessage(String title, String header, String content) {
        Alert message = new Alert(AlertType.INFORMATION);
        message.setTitle(title);
        message.setHeaderText(header);
        message.setContentText(content);

        message.showAndWait();
    }
}