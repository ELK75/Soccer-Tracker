
import java.util.Scanner;
import java.util.ArrayList;
import javafx.application.*;
import javafx.stage.*;


//
// GITHUB: https://github.com/Elijah-Kajinic/Soccer-Tracker
//

public class Main extends Application {

    private Stage stage;
    private User user;
    //
    // FUNCTIONS RELATING TO THE START MENU
    //

    /*
    private static void promptUser(User user) {
        String option1;
        String option4;

        if (playersNotEntered(user))
            option1 = "1. New list of players\n";
        else
            option1 = "1. Update/Delete players to track\n";

        if (user.getIsVip())
            option4 = "4. View/Update Preferences\n";
        else
            option4 = "4. View/Update Preferences(VIP only)\n";

        System.out.println();
        System.out.println(option1 +
                           "2. View my list of players and goals\n" +
                           "3. Update goals scored\n" +
                           option4 +
                           "5. Exit the program\n" +
                           "Please select a menu option (1-5)");
    }
    */


    // a main menu where the user enters where
    // they want to go
    /*
    private static void introScreen(User user) throws Exception {
        boolean wantsToExit = false;
        while (!wantsToExit) {
            IntroMenu intoMenu = new IntroMenu();
            promptUser(user);
            String input = getPromptInput(user);
            switch (Integer.parseInt(input)) {
                case 1: if (playersNotEntered(user))
                            enterPlayers(user);
                        else
                            updatePlayers(user);
                        break;
                case 2: viewPlayers(user);
                        break;
                case 3: updateGoals(user);
                        break;
                case 4: if (user.getIsVip())
                            showPreferences(user);
                        else
                            showPromptForSubscription();
                        break;
                case 5: 
                        DataFile.writeUserFile(user, User.USER_FILE);
                        wantsToExit = true;
                        break;
            }
        }
    }
    */

    private void launchMainMenu() throws Exception {
        MainMenu mainMenu = new MainMenu(user);
        mainMenu.start();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.stage = primaryStage;
        this.user = new User();

        // loads user file if exists
        if (DataFile.userFileExists(User.USER_FILE)) {
            user = DataFile.loadUserFile(User.USER_FILE);
        // if this is the user's first time using the program
        // they get prompted as to whether they are VIPs
        } else {
            user.determineIfUserVip(stage);
        }

        launchMainMenu();
    }

    public static void main(String[] args) throws Exception {
        launch(args);
    }
}