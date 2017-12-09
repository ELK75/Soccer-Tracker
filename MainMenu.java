
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
    private MainUser mainUser;
    private ArrayList<SassyUser> sassyUsers = new ArrayList<SassyUser>();
    private ArrayList<SassyUser> connectedUsers;
    private CheckBox[] sassyUserCheckBoxes;
    private int menuWidth = 400;
    private int menuHeight = 350;
    // where text of players and stats will be displayed
    private TextArea display = new TextArea();

    public MainMenu(MainUser mainUser, ArrayList<SassyUser> sassyUsers) {
        this.mainUser = mainUser;
        this.sassyUsers = sassyUsers;
    }

    public static boolean isInteger(String str) {
        if (str == null || str.trim().isEmpty()) {
            return false;
        }
        for (int i = 0; i < str.length(); i++) {
            // testing if there is a negative sign. if there is it skips over
            // it unless the input is JUST a negative sign
            if (str.charAt(i) == '-' && i == 0 && str.length() > 1)
                continue;
            if (!Character.isDigit(str.charAt(i)))
                return false;
        }
        return true;
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

    public void userNotVipPrompt() {
        Dialog.showMessage("VIP Access", "Access Denied", 
        "Contact a Sassy Soccer representive for a VIP subscription");
    }

    public void start() throws Exception {

        this.stage = new Stage();
        stage.setTitle("Main Menu");

        // will contain Menu as well as the Display
        VBox mainPane = new VBox();

        display.setWrapText(true);
        display.setFont(new Font(15));
        display.setPrefHeight(340);

        // Main menu bar
        Menu fileMenu = new Menu("File");
        Menu playerMenu = new Menu("Players");
        Menu goalMenu = new Menu("Goals");
        Menu vipMenu = new Menu("VIP");

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

        // Menu Items for VIP Users
        MenuItem changePreferences = new MenuItem("Change Preferences");
        changePreferences.setOnAction(e -> changePreferences());
        MenuItem changePassword = new MenuItem("Change Password");
        changePassword.setOnAction(e -> changePassword());
        MenuItem connectToOtherUsers = new MenuItem("Connect to Other Users");
        connectToOtherUsers.setOnAction(e -> connectToOtherUsers());
        vipMenu.getItems().addAll(changePreferences, changePassword, connectToOtherUsers);

        // Main Menu Bar
        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().addAll(fileMenu, playerMenu, goalMenu, vipMenu);

        this.sassyUserCheckBoxes = CheckBoxSelection.getCheckBoxes(SassyUser.getUserNames(sassyUsers));
        connectedUsers = mainUser.getConnectedUsers();

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
        mainUser.setConnectedUsers(connectedUsers);
        DataFile.writeUserFile(mainUser, MainUser.getUserFile());
        stage.close();
    }

    public void addNewPlayers() {

        Stage addNewPlayerStage = new Stage();

        if (mainUser.playersNotEntered()) {

            GenericButtonSelection genericButtonSelection = new GenericButtonSelection();

            HBox paneButton = genericButtonSelection.getHBox();
            paneButton.setPadding(new Insets (10, 10, 10, 230));

            Label lblPlayers = new Label("Players");
            lblPlayers.setFont(new Font(15));
            lblPlayers.setPrefWidth(190);

            Label lblGoals = new Label("Goals");
            lblGoals.setFont(new Font(15));
            lblGoals.setPrefWidth(125);

            HBox paneLabels = new HBox(lblPlayers, lblGoals);
            paneLabels.setPadding(new Insets (10, 10, 10, 10));

            // creates text boxes for user input player and goals
            int maxPlayers = mainUser.getNumberOfPlayersAllowed();
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

            genericButtonSelection.getBtnOK().setOnAction(e -> 
                inputPlayers(textPlayers, textGoals, addNewPlayerStage));
            genericButtonSelection.getBtnCancel().setOnAction(e -> 
                addNewPlayerStage.close());

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
            if (playerName.isEmpty() || !isInteger(playerGoals) ||
            mainUser.nameAlreadyInputted(playerName) || Integer.parseInt(playerGoals) < 0) 
                continue;
            else {
                Player player = new Player(playerName);
                if (playerGoals.isEmpty()) player.setGoals(0);
                else player.setGoals(Integer.parseInt(playerGoals));
                mainUser.getPlayers().add(player);
            }
        }
        stage.close();
    }

    public void displayUser(User user) {
        if (!user.playersNotEntered()) {

            user.sortByGoalsBreakTieByName();

            String name = "\n" + "User: " + user.getName();

            String playersAndGoals = "\n";
            for (String playerAndGoals : user.getPlayerNamesAndGoals()) {
                playersAndGoals += playerAndGoals + "\n";
            }

            String playerStats = "\n";
            String wantsPlayerStatsShown = mainUser.getPreferences()[2][1];
            if (wantsPlayerStatsShown.equals("True")) {
                playerStats += user.returnTopScorers() + "\n" + 
                user.getGoalAverage() + "\n" + user.getGoalTotal();
            }

            display.setText(display.getText() + name + playersAndGoals + playerStats + "\n");
        }
    }

    public void updateDisplay() {
        display.setText("");

        displayUser(mainUser);

        for (User user : connectedUsers) {
            displayUser(user);
        }
    }

    public void updatePlayers() {

        if (mainUser.playersNotEntered()) playersNotEnteredPrompt();
        else {
            mainUser.sortByGoalPreference();
            mainUser.sortByNamePreference();

            int playerIndex = Dialog.getChoice("Players", "Select Players", "", mainUser.getPlayerNames());
            if (playerIndex != -1) {
                String newName = Dialog.getTextInput("Update", "Enter a New Name", "Name: ");
                updatePlayer(newName, playerIndex);
            }
            updateDisplay();
        }
    }

    public void updatePlayer(String newName, int playerIndex) {

        if (mainUser.nameAlreadyInputted(newName)) {
            String alertContent = "Player Already Entered has " + mainUser.getPlayer(playerIndex).getGoals() +
                " " + mainUser.goalOrGoals(mainUser.getPlayer(playerIndex).getGoals());
            throwAlert("Update Players", alertContent);

        } else if (newName.isEmpty()) {
            throwAlert("Update Players", "Please Enter a Name");

        } else {
            mainUser.getPlayer(playerIndex).setName(newName);
            mainUser.getPlayer(playerIndex).setGoals(0);
        }
    }

    public void deletePlayers() {
        if (mainUser.playersNotEntered()) playersNotEnteredPrompt();
        else {

            mainUser.sortByNamePreference();

            int playerIndex = Dialog.getChoice("Delete", "Select Player to Delete", "", mainUser.getPlayerNames());

            if (playerIndex != -1) {
                deletePlayer(playerIndex);
            }
            updateDisplay();
        }
    }

    public void deletePlayer(int playerIndex) {
        String playerName = mainUser.getPlayer(playerIndex).getName();

        if (Dialog.getConfirmation("Delete", "Confirm", "Delete Player " + playerName + "?"))
            mainUser.getPlayers().remove(playerIndex);
    }

    public void deleteAllPlayers() {
        
        if (mainUser.playersNotEntered()) playersNotEnteredPrompt();
        else {
            if (Dialog.getConfirmation("Delete", "Delete All Players?", null))
                mainUser.clearPlayers();
            updateDisplay();
        }
    }

    public void addGoals() {
        if (mainUser.playersNotEntered()) playersNotEnteredPrompt();
        else {
            mainUser.sortByGoalsBreakTieByName();

            int chosenPlayer = Dialog.getChoice("Goals", "Select Player to Add Goals", null, 
            mainUser.getPlayerNamesAndGoals());
            if (chosenPlayer != -1) {
                addGoalsToPlayer(chosenPlayer);
            }

            updateDisplay();
        }
    }

    public void addGoalsToPlayer(int chosenPlayer) {
        String inputtedGoals = Dialog.getTextInput("Goals", "How Many Goals Would You Like To Add",
        "Goals: ");

        if (isInteger(inputtedGoals) && Integer.parseInt(inputtedGoals) >= 0) {
            mainUser.getPlayer(chosenPlayer).addGoals(Integer.parseInt(inputtedGoals));
        } else {
            Dialog.showMessage("Goals", "Error", "Negative or Non-Numeric Input");
        }
    }

    public void changePreferences() {
        if (!mainUser.getIsVip()) {
            userNotVipPrompt();
        } else {
            int chosenPreference = Dialog.getChoice("Preferences", "Change Preferences", null,
            MainUser.getPreferencesAndCurrentPreference());

            if (chosenPreference != -1) {
                MainUser.changePreference(chosenPreference);
                
                String changedPreference = "Changed " + mainUser.getPreferences()[chosenPreference][0] + 
                " to " + mainUser.getPreferences()[chosenPreference][1];
                Dialog.showMessage("Preferences", "Preference Change", changedPreference);
            }
        }
        updateDisplay();
    }

    public void changePassword() {
        if (!mainUser.getIsVip()) {
            userNotVipPrompt();
        } else {
            ChangePasswordBox changePasswordBox = new ChangePasswordBox(mainUser);
            changePasswordBox.changePassword();
        }
    }

    private boolean shareAUser(ArrayList<SassyUser> arrayListOne, 
                                ArrayList<SassyUser> arrayListTwo) {
        for (int i = 0; i < arrayListOne.size(); i++) {
            for (int j = 0; j < arrayListTwo.size(); j++) {
                if (arrayListOne.get(i).equals(arrayListTwo.get(j)))
                    return true;
            }
        }
        return false;
    }

    // if the main user is already connected to the user
    // this function will check the corresponding check box
    public void setSelectedForAlreadyConnectedUsers() {
        for (int i = 0; i < sassyUsers.size(); i++) {
            boolean mainUserConnectedToSassyUser = shareAUser(connectedUsers, sassyUsers); 
            if (mainUserConnectedToSassyUser) {
                sassyUserCheckBoxes[i].setSelected(true);
            }
        }
    }

    public void connectToOtherUsers() {
        if (!mainUser.getIsVip()) {
            userNotVipPrompt();
        } else {

            // TODO fix so that when the user has checked the checkbox
            // of users from a previous session it persists
            //setSelectedForAlreadyConnectedUsers();

            CheckBoxSelection userSelection = new CheckBoxSelection(
                sassyUserCheckBoxes, "Select User to Track");
            userSelection.show();

            if (userSelection.getUserPressedOK()) {

                connectedUsers.clear();

                for (int i = 0; i < sassyUserCheckBoxes.length; i++) {
                    if (sassyUserCheckBoxes[i].isSelected()) {
                        connectedUsers.add(sassyUsers.get(i));
                    }
                }
                updateDisplay();
            }
        }
    }
}