
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
import java.util.*;

public class MainMenu {

    private Stage stage;
    private User user;
    private int menuWidth = 400;
    private int menuHeight = 350;
    // where text of players will be displayed
    private Label display = new Label();

    public MainMenu(User user) {
        this.user = user;
    }

    private void throwAlert(String title, String content) {
        Alert alertPrompt = new Alert(AlertType.INFORMATION);
        alertPrompt.setTitle(title);
        alertPrompt.setContentText(content);
        alertPrompt.showAndWait();
    }

    public void playersNotEnteredPrompt() {
        throwAlert("Update Players", "Players Not Entered");
    }

    public void start() throws Exception {

        this.stage = new Stage();
        stage.setTitle("Main Menu");

        // will contain Menu as well as the Display
        VBox mainPane = new VBox();

        display.setWrapText(true);
        display.setFont(new Font(15));

        // Main menu bar
        Menu fileMenu = new Menu("File");
        Menu playerMenu = new Menu("Players");
        Menu goalMenu = new Menu("Goals");
        Menu preferenceMenu = new Menu("Preferences");

        // Menu Items for Saving File
        MenuItem saveAndExit = new MenuItem("Save and Exit");
        saveAndExit.setOnAction(e -> {
            try {
                saveAndExit();
            } catch (Exception exception) {
                throw new RuntimeException();
            }
        });
        fileMenu.getItems().add(saveAndExit);

        // Player menu options
        MenuItem newPlayers = new MenuItem("New");
        newPlayers.setOnAction(e -> addNewPlayers());
        MenuItem updatePlayers = new MenuItem("Update");
        updatePlayers.setOnAction(e -> updatePlayers());
        MenuItem deletePlayers = new MenuItem("Delete Players");
        deletePlayers.setOnAction(e -> deletePlayers());
        MenuItem deleteAllPlayers = new MenuItem("Delete All Players");
        deleteAllPlayers.setOnAction(e -> deleteAllPlayers());

        playerMenu.getItems().addAll(
            newPlayers, updatePlayers, deletePlayers, deleteAllPlayers
        );

        // Goal Menu Option
        MenuItem addGoals = new MenuItem("Add Goals");
        addGoals.setOnAction(e -> addGoals());
        goalMenu.getItems().add(addGoals);

        // Menu Items for Preferences
        MenuItem changePreferences = new MenuItem("Change Preferences");
        changePreferences.setOnAction(e -> changePreferences());
        preferenceMenu.getItems().add(changePreferences);

        // Main Menu Bar
        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().addAll(fileMenu, playerMenu, goalMenu, preferenceMenu);

        // Main Pane
        mainPane.getChildren().addAll(menuBar, display);

        // Displays players
        // if user entered players in a previous session
        updateDisplay();

        Scene scene = new Scene(mainPane, menuWidth, menuHeight);
        stage.setScene(scene);
        stage.showAndWait();

    }

    public void saveAndExit() throws Exception {
        DataFile.writeUserFile(user, User.USER_FILE);
        stage.close();
    }

    public void addNewPlayers() {

        Stage addNewPlayerStage = new Stage();

        if (user.playersNotEntered()) {

            Button btnOK = new Button("OK");
            btnOK.setPrefWidth(80);

            Button btnCancel = new Button("Cancel");
            btnCancel.setPrefWidth(80);

            HBox paneButton = new HBox(btnOK, btnCancel);
            paneButton.setPadding(new Insets (10, 10, 10, 230));

            Label lblPlayers = new Label("Players");
            lblPlayers.setFont(new Font(15));
            lblPlayers.setPrefWidth(190);

            Label lblGoals = new Label("Goals");
            lblGoals.setFont(new Font(15));
            lblGoals.setPrefWidth(125);

            HBox paneLabels = new HBox(lblPlayers, lblGoals);
            paneLabels.setPadding(new Insets (10, 10, 10, 10));

            int maxPlayers = user.getNumberOfPlayersAllowed();
            TextField[] textPlayers = new TextField[maxPlayers];
            TextField[] textGoals = new TextField[maxPlayers];
            VBox panePlayersAndGoals = new VBox(5);
            for (int i = 0; i < maxPlayers; i++) {
                TextField txtPlayer = new TextField();
                txtPlayer.setPrefColumnCount(20);
                textPlayers[i] = txtPlayer;

                TextField txtGoals = new TextField();
                txtGoals.setPrefColumnCount(20);
                textGoals[i] = txtGoals;

                HBox panepPlayerAndGoalsRow = new HBox(txtPlayer, txtGoals);
                panePlayersAndGoals.setPadding(new Insets (20, 10, 5, 10));
                panePlayersAndGoals.getChildren().add(panepPlayerAndGoalsRow);
            }

            btnOK.setOnAction(e -> inputPlayers(textPlayers, textGoals, addNewPlayerStage));
            btnCancel.setOnAction(e -> addNewPlayerStage.close());

            BorderPane paneMain = new BorderPane();
            paneMain.setTop(paneLabels);
            paneMain.setCenter(panePlayersAndGoals);
            paneMain.setBottom(paneButton);

            Scene scene = new Scene(paneMain, 400, 350);
            addNewPlayerStage.setScene(scene);
            addNewPlayerStage.setTitle("Add Players");
            addNewPlayerStage.showAndWait();

            updateDisplay();
        } else {
            Dialog.showMessage("Players", "Players Already Entered", null);
        }
    }

    public void inputPlayers(TextField[] textPlayers, TextField[] textGoals, Stage stage) {
        for (int i = 0; i < textPlayers.length; i++) {
            String playerName = textPlayers[i].getText();
            String playerGoals = textGoals[i].getText();
            if (playerName.isEmpty() || !User.isInteger(playerGoals) ||
            user.nameAlreadyInputted(playerName) || Integer.parseInt(playerGoals) < 0) 
                continue;
            else {
                Player player = new Player(playerName);
                if (playerGoals.isEmpty()) player.setGoals(0);
                else player.setGoals(Integer.parseInt(playerGoals));
                user.getPlayers().add(player);
            }
        }
        stage.close();
    }

    public void updateDisplay() {
        if (user.playersNotEntered()) {
            display.setText("");
        } else {

            user.sortByNamePreference();
            user.sortByGoalPreferences();

            String playersAndGoals = "\n";
            for (String playerAndGoals : user.getPlayerNamesAndGoals()) {
                playersAndGoals += playerAndGoals + "\n";
            }

            String playerStats = "\n";
            String wantsPlayerStatsShown = user.getPreferences()[2][1];
            if (wantsPlayerStatsShown.equals("True")) {

                playerStats += user.returnTopScorers() + "\n" + 
                user.getGoalAverage() + "\n" + user.getGoalTotal();
            }

            display.setText(playersAndGoals + playerStats);
        }
    }

    public void updatePlayers() {

        if (user.playersNotEntered()) playersNotEnteredPrompt();
        else {
            user.sortByGoalPreferences();
            user.sortByNamePreference();

            int playerIndex = Dialog.getChoice("Players", "Select Players", "", user.getPlayerNames());
            if (playerIndex != -1) {
                String newName = Dialog.getTextInput("Update", "Enter a New Name", "Name: ");
                updatePlayer(newName, playerIndex);
            }
            updateDisplay();
        }
    }

    public void updatePlayer(String newName, int playerIndex) {

        if (user.nameAlreadyInputted(newName)) {
            String alertContent = "Player Already Entered has " + user.getPlayer(playerIndex).getGoals() +
                " " + user.goalOrGoals(user.getPlayer(playerIndex).getGoals());
            throwAlert("Update Players", alertContent);

        } else if (newName.isEmpty()) {
            throwAlert("Update Players", "Please Enter a Name");

        } else {
            user.getPlayer(playerIndex).setName(newName);
            user.getPlayer(playerIndex).setGoals(0);
        }
    }

    public void deletePlayers() {
        if (user.playersNotEntered()) playersNotEnteredPrompt();
        else {

            user.sortByNamePreference();

            int playerIndex = Dialog.getChoice("Delete", "Select Player to Delete", "", user.getPlayerNames());

            if (playerIndex != -1) {
                deletePlayer(playerIndex);
            }
            updateDisplay();
        }
    }

    public void deletePlayer(int playerIndex) {
        String playerName = user.getPlayer(playerIndex).getName();

        if (Dialog.getConfirmation("Delete", "Confirm", "Delete Player " + playerName + "?"))
            user.getPlayers().remove(playerIndex);
    }

    public void deleteAllPlayers() {
        
        if (user.playersNotEntered()) playersNotEnteredPrompt();
        else {
            if (Dialog.getConfirmation("Delete", "Delete All Players?", null))
                user.clearPlayers();
            updateDisplay();
        }
    }

    public void addGoals() {
        if (user.playersNotEntered()) playersNotEnteredPrompt();
        else {
            user.sortByNamePreference();
            user.sortByGoalPreferences();

            int chosenPlayer = Dialog.getChoice("Goals", "Select Player to Add Goals", null, 
            user.getPlayerNamesAndGoals());
            if (chosenPlayer != -1) {
                addGoalsToPlayer(chosenPlayer);
            }

            updateDisplay();
        }
    }

    public void addGoalsToPlayer(int chosenPlayer) {
        String inputtedGoals = Dialog.getTextInput("Goals", "How Many Goals Would You Like To Add",
        "Goals: ");

        if (User.isInteger(inputtedGoals) && Integer.parseInt(inputtedGoals) >= 0) {
            user.getPlayer(chosenPlayer).addGoals(Integer.parseInt(inputtedGoals));
        } else {
            Dialog.showMessage("Goals", "Error", "Negative or Non-Numeric Input");
        }
    }

    public void changePreferences() {
        if (!user.getIsVip()) {
            Dialog.showMessage("VIP Access", "Access Denied", 
            "Contact a Sassy Soccer representive for a VIP subscription");
        } else {
            int chosenPreference = Dialog.getChoice("Preferences", "Change Preferences", null,
            user.getPreferencesAndCurrentPreference());

            if (chosenPreference != -1) {
                user.changePreference(chosenPreference);
                
                String changedPreference = "Changed " + user.getPreferences()[chosenPreference][0] + 
                " to " + user.getPreferences()[chosenPreference][1];
                Dialog.showMessage("Preferences", "Preference Change", changedPreference);
            }
        }
        updateDisplay();
    }
}