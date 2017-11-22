
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.geometry.*;
import javafx.scene.control.Alert.AlertType;
import java.io.*;
import javafx.scene.text.*;

public class GenericListView {

    private String lblText;
    private User user;
    private Button submitButton;
    private ListView<String> playerListView;

    public GenericListView(String lblText, User user) {
        this.lblText = lblText;
        this.user = user;
    }

    public Stage getStage() {

        Stage updateStage = new Stage();

        Label lblUpdate = new Label(lblText);
        
        this.playerListView = new ListView<String>();
        for (int i = 0; i < user.getNumberOfPlayers(); i++) {
            playerListView.getItems().add(user.getPlayer(i).getName());
        }

        HBox topBox = new HBox(60, lblUpdate, playerListView);
        topBox.setPadding(new Insets(10, 20, 10, 20));
        topBox.setAlignment(Pos.CENTER);

        this.submitButton = new Button("Select");

        VBox sceneBox = new VBox(topBox, submitButton);
        sceneBox.setAlignment(Pos.CENTER);

        Scene scene = new Scene(sceneBox, 500, 400);
        updateStage.setScene(scene);
        return updateStage;
    }

    public Button getSubmitButton() {
        return submitButton;
    }

    public ListView<String> getPlayerListView() {
        return playerListView;
    }
}