
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

    public MainMenu(User user) {
        this.user = user;
    }

    public void start() throws Exception {

        this.stage = new Stage();
        stage.setTitle("Main Menu");

        VBox pane = new VBox();

        // Main menu bar
        Menu fileMenu = new Menu("File");
        Menu playerMenu = new Menu("Players");
        Menu goalMenu = new Menu("Goals");
        Menu preferenceMenu = new Menu("Preferences");

        // File menu options
        MenuItem saveAndExit = new MenuItem("Save and Exit");
        // TODO FIX
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
        MenuItem viewPlayers = new MenuItem("View");
        viewPlayers.setOnAction(e -> viewPlayers());
        MenuItem updatePlayers = new MenuItem("Update");
        updatePlayers.setOnAction(e -> updatePlayers());
        MenuItem deletePlayers = new MenuItem("Delete Players");
        deletePlayers.setOnAction(e -> deletePlayers());
        MenuItem deleteAllPlayers = new MenuItem("Delete All Players");
        deleteAllPlayers.setOnAction(e -> deleteAllPlayers());

        playerMenu.getItems().addAll(
            newPlayers, viewPlayers, updatePlayers, deletePlayers, deleteAllPlayers
        );

        MenuItem addGoals = new MenuItem("Add Goals");
        addGoals.setOnAction(e -> addGoals());
        goalMenu.getItems().add(addGoals);

        MenuItem changePreferences = new MenuItem("Change Preferences");
        changePreferences.setOnAction(e -> changePreferences());
        preferenceMenu.getItems().add(changePreferences);

        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().addAll(fileMenu, playerMenu, goalMenu, preferenceMenu);
        pane.getChildren().add(menuBar);

        Scene scene = new Scene(pane, menuWidth, menuHeight);
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
        } else {
            Alert prompt = new Alert(AlertType.INFORMATION);
            prompt.setHeaderText("Add Players");
            prompt.setContentText("Players Already Entered");
            prompt.showAndWait();
        }
    }

    // change to only accept integers
    public void enterPlayer(String playerName, String playerGoals) {
        Player player = new Player(playerName);
        player.setGoals(Integer.parseInt(playerGoals));
        user.getPlayers().add(player);
    }

    public void inputPlayers(TextField[] textPlayers, TextField[] textGoals, Stage stage) {
        for (int i = 0; i < textPlayers.length; i++) {
            String playerName = textPlayers[i].getText();
            String playerGoals = textGoals[i].getText();
            if (playerName.isEmpty() || !User.isInteger(playerGoals) ||
                user.nameAlreadyInputted(playerName)) continue;
            else {
                Player player = new Player(playerName);
                if (playerGoals.isEmpty()) player.setGoals(0);
                else player.setGoals(Integer.parseInt(playerGoals));
                user.getPlayers().add(player);
            }
        }
        stage.close();
    }

    public void viewPlayers() {
        if (user.playersNotEntered()) {
            playersNotEnteredPrompt();
        } else {
            user.sortByNamePreference();
            user.sortByGoalPreferences();
            Stage stage = new Stage();
            VBox panelPlayerAndGoals = new VBox(5);
            for (int i = 0; i < user.getNumberOfPlayers(); i++) {
                Label lblPlayerAndGoals = new Label(user.getPlayerAndGoalsString(i));
                lblPlayerAndGoals.setFont(new Font(15));
                lblPlayerAndGoals.setPrefWidth(300);
                panelPlayerAndGoals.getChildren().add(lblPlayerAndGoals);
            }
            panelPlayerAndGoals.setPadding(new Insets (10, 0, 0, 10));


            VBox panelPlayerStats = new VBox(5);

            if (user.getPreferences()[2][1].equals("True")) {
                Label lblTopScorers = new Label(user.returnTopScorers());
                lblTopScorers.setFont(new Font(15));
                lblTopScorers.setPrefWidth(350);
                panelPlayerStats.getChildren().add(lblTopScorers);

                Label lblGoalAverage = new Label(user.getGoalAverage());
                lblGoalAverage.setFont(new Font(15));
                lblGoalAverage.setPrefWidth(350);
                panelPlayerStats.getChildren().add(lblGoalAverage);

                Label lblGoalTotal = new Label(user.getGoalTotal());
                lblGoalTotal.setFont(new Font(15));
                lblGoalTotal.setPrefWidth(350);
                panelPlayerStats.getChildren().add(lblGoalTotal);

                panelPlayerStats.setPadding(new Insets(30, 10, 10, 10));
            }

            HBox paneButton = new HBox();
            Button btnClose = new Button("Close");
            btnClose.setPadding(new Insets(15, 20, 15, 20));
            btnClose.setOnAction(e -> stage.close());
            paneButton.getChildren().add(btnClose);
            paneButton.setPadding(new Insets(0, 0, 10, 165));

            BorderPane paneMain = new BorderPane();
            paneMain.setTop(panelPlayerAndGoals);
            paneMain.setCenter(panelPlayerStats);
            paneMain.setBottom(paneButton);

            Scene scene = new Scene(paneMain, 400, 450);
            stage.setScene(scene);
            stage.setTitle("Player Stats");
            stage.showAndWait();
        }
    }

    public void updatePlayers() {

        if (user.playersNotEntered()) playersNotEnteredPrompt();
        else {
            user.sortByNamePreference();
            user.sortByGoalPreferences();

            GenericListView updateView = new GenericListView("Select Player to Update", user.getPlayerListViewWithGoals());
            Stage updateStage = updateView.getStage();
            updateView.getSubmitButton().setOnAction(e -> setPlayerName(
                updateView.getListView(), updateStage));
            updateStage.showAndWait();
        }
    }

    public void playersNotEnteredPrompt() {
        Alert prompt = new Alert(AlertType.INFORMATION);
        prompt.setHeaderText("Update Players");
        prompt.setContentText("Players Not Entered");
        prompt.showAndWait();
    }

    public void setPlayerName(ListView<String> playerListView, Stage parentStage) {
        if (user.playersNotEntered()) {
            playersNotEnteredPrompt();
        } else {
            String selectedPlayer = playerListView.getSelectionModel().getSelectedItem();
            selectedPlayer = selectedPlayer.substring(0, selectedPlayer.lastIndexOf(':'));
            if (selectedPlayer != null) {
                int playerIndex = user.getIndexFromName(selectedPlayer);

                GenericTextBox updateTextBox = new GenericTextBox("Update Player", "Name:");
                Stage updateStage = updateTextBox.getStage();
                
                updateTextBox.getTextField().setOnAction(e -> updatePlayer(
                    updateTextBox.getTextField().getText(), playerIndex, 
                    updateStage, parentStage));
                updateTextBox.getBtnOK().setOnAction(e -> updatePlayer(
                    updateTextBox.getTextField().getText(), playerIndex, 
                    updateStage, parentStage));
                updateTextBox.getBtnCancel().setOnAction(e -> updateStage.close());

                updateStage.showAndWait();
                
            }
        }
    }

    public void updatePlayer(String newName, int playerIndex, Stage currentStage, Stage parentStage) {
        Alert prompt = new Alert(AlertType.INFORMATION);
        prompt.setHeaderText("Update Players");
        if (user.nameAlreadyInputted(newName)) {
            prompt.setContentText("Player Already Entered has " + user.getPlayer(playerIndex).getGoals() +
                " " + user.goalOrGoals(user.getPlayer(playerIndex).getGoals()));
            prompt.showAndWait();
            currentStage.close();
            parentStage.close();
        } else if (newName.isEmpty()) {
            prompt.setContentText("Please Enter A Name");
            prompt.showAndWait();
        } else {
            user.getPlayer(playerIndex).setName(newName);
            user.getPlayer(playerIndex).setGoals(0);
            prompt.setContentText("Player Updated");
            prompt.showAndWait();
            currentStage.close();
            parentStage.close();
        }
    }

    public void deletePlayers() {
        if (user.playersNotEntered()) playersNotEnteredPrompt();
        else {
            user.sortByNamePreference();

            GenericListView deleteView = new GenericListView("Select Player to Delete", user.getPlayerListView());
            Stage deleteStage = deleteView.getStage();
            deleteView.getSubmitButton().setOnAction(e -> deletePlayerName(
                deleteView.getListView(), deleteStage));
            deleteStage.showAndWait();
        }
    }

    public void deletePlayerName(ListView<String> playerListView, Stage parentStage) {
        if (user.playersNotEntered()) playersNotEnteredPrompt();
        else {

            String selectedPlayer = playerListView.getSelectionModel().getSelectedItem();

            Alert prompt = new Alert(AlertType.CONFIRMATION);
            prompt.setHeaderText("Delete Player");
            prompt.setContentText("Are you sure you want to delete " + selectedPlayer + "?");
            ButtonType btnOK = new ButtonType("OK");
            ButtonType btnCancel = new ButtonType("Cancel");

            prompt.getButtonTypes().setAll(btnOK, btnCancel);
            Optional<ButtonType> result = prompt.showAndWait();

            if (result.get() == btnOK) {
                user.getPlayers().remove(user.getIndexFromName(selectedPlayer));
                Alert deletePrompt = new Alert(AlertType.INFORMATION);
                deletePrompt.setHeaderText("Delete Player");
                deletePrompt.setContentText("Player Deleted");
                deletePrompt.showAndWait();
                parentStage.close();
            } else if (result.get() == btnCancel) {
                parentStage.close();
            }
        }
    }

    public void deleteAllPlayers() {
        
        if (user.playersNotEntered()) playersNotEnteredPrompt();
        else {
            Alert prompt = new Alert(AlertType.CONFIRMATION);
            prompt.setHeaderText("Delete All Players");
            prompt.setContentText("Are you sure you want to delete all players?");
            ButtonType btnOK = new ButtonType("OK");
            ButtonType btnCancel = new ButtonType("Cancel");

            prompt.getButtonTypes().setAll(btnOK, btnCancel);
            Optional<ButtonType> result = prompt.showAndWait();
        
            if (result.get() == btnOK) {
                user.clearPlayers();
                Alert deletePrompt = new Alert(AlertType.INFORMATION);
                deletePrompt.setHeaderText("Delete All Players");
                deletePrompt.setContentText("All Players Deleted");
                deletePrompt.showAndWait();
            } else if (result.get() == btnCancel)
                prompt.close();
        }
    }

    public void addGoals() {
        if (user.playersNotEntered()) playersNotEnteredPrompt();
        else {
            user.sortByNamePreference();

            GenericListView goalView = new GenericListView("Select Player to Add Goals", user.getPlayerListViewWithGoals());
            Stage goalStage = goalView.getStage();
            goalView.getSubmitButton().setOnAction(e -> addPlayerGoals(
                goalView.getListView(), goalStage));
            goalStage.showAndWait();
        }
    }

    public void addPlayerGoals(ListView<String> playerListView, Stage parentStage) {

        String selectedPlayer = playerListView.getSelectionModel().getSelectedItem();
        selectedPlayer = selectedPlayer.substring(0, selectedPlayer.lastIndexOf(':'));
        int playerIndex = user.getIndexFromName(selectedPlayer);
        
        GenericTextBox updateTextBox = new GenericTextBox("Update Player", "Goals:");
        Stage updateStage = updateTextBox.getStage();

        updateTextBox.getTextField().setOnAction(e -> addGoalsToPlayer(
            updateTextBox.getTextField().getText(), playerIndex, 
            updateStage, parentStage));
        updateTextBox.getBtnOK().setOnAction(e -> addGoalsToPlayer(
            updateTextBox.getTextField().getText(), playerIndex, 
            updateStage, parentStage));
        updateTextBox.getBtnCancel().setOnAction(e -> updateStage.close());

        updateStage.showAndWait();
    }

    public void addGoalsToPlayer(String input, int playerIndex, Stage updateStage, Stage parentStage) {
        if (User.isInteger(input) && Integer.parseInt(input) >= 0) {
            user.getPlayer(playerIndex).addGoals(Integer.parseInt(input));

            Alert addGoalsPrompt = new Alert(AlertType.INFORMATION);
            addGoalsPrompt.setHeaderText("Update Goals");
            addGoalsPrompt.setContentText("Added " + input + " " + user.goalOrGoals(Integer.parseInt(input)));
            addGoalsPrompt.showAndWait();

            updateStage.close();
            parentStage.close();

        } else {
            Alert addGoalsPrompt = new Alert(AlertType.INFORMATION);
            addGoalsPrompt.setHeaderText("Invalid Input");
            addGoalsPrompt.setContentText("Please Enter a Postivie Number of Goals");
            addGoalsPrompt.showAndWait();
        }
    }

    public void changePreferences() {
        if (!user.getIsVip()) {
            Alert addGoalsPrompt = new Alert(AlertType.INFORMATION);
            addGoalsPrompt.setHeaderText("Access Denied");
            addGoalsPrompt.setContentText("Contact a Sassy Soccer representive for a VIP subscription");
            addGoalsPrompt.showAndWait();
        } else {
            GenericListView preferenceView = new GenericListView("Select Preference", user.getPreferenceListView());
            Stage preferenceStage = preferenceView.getStage();
            preferenceView.getSubmitButton().setOnAction(e -> changePreference(
                preferenceView.getListView(), preferenceStage));
            preferenceStage.showAndWait();
        }
    }

    public void changePreference(ListView<String> preferenceView, Stage goalStage) {
        String selectedPreference = preferenceView.getSelectionModel().getSelectedItem();
        int preferenceSelected = 1;

        if (selectedPreference.substring(0, 4).equals("Goal")) preferenceSelected = 1;
        if (selectedPreference.substring(0, 4).equals("Name")) preferenceSelected = 2;
        if (selectedPreference.substring(0, 4).equals("Show")) preferenceSelected = 3;

        user.changePreference(preferenceSelected);

        Alert preferencePrompt = new Alert(AlertType.INFORMATION);
        preferencePrompt.setContentText("Changed to " + user.getPreferences()[preferenceSelected-1][1]);
        preferencePrompt.showAndWait();

        goalStage.close();

    }
}