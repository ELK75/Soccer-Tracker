
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.*;
import javafx.stage.Stage;
import javafx.geometry.*;

public class Menu extends Application {

    @Override
    public void start(Stage primaryStage) {

        primaryStage.setTitle("Soccer Tracker");
        Label menuOptionLabel = new Label("Choose a menu option: ");

        menuListView = new ListView<String>();

        option1 = 
        menuListView.getItems().addAll("")


    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}