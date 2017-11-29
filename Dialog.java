
import java.util.Optional;
import java.util.ArrayList;
import javafx.scene.control.*;

public class Dialog {
    
    // displays a drop-down menu of options for user to choose
    public static int getChoice(String title, String header, String content, 
    ArrayList<String> options) {
        String firstOption = options.get(0);
        ChoiceDialog<String> choiceDialog = new ChoiceDialog<>(firstOption, options);
        choiceDialog.setTitle(title);
        choiceDialog.setHeaderText(header);
        choiceDialog.setContentText(content);

        Optional<String> choice = choiceDialog.showAndWait();

        return options.indexOf(choice.get());
    }

    public static String getTextInput(String title, String header, String content) {
        TextInputDialog inputDialog = new TextInputDialog();
        inputDialog.setTitle(title);
        inputDialog.setHeaderText(header);
        inputDialog.setContentText(content);

        Optional<String> input = inputDialog.showAndWait();

        return input.get();
    }
}