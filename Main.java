
import java.util.Scanner;
import java.util.ArrayList;
import javafx.application.*;
import javafx.stage.*;


//
// GITHUB: https://github.com/Elijah-Kajinic/Soccer-Tracker
//

//
// TODO
//
// Add:
// TextArea
// TextInputDialog
//

public class Main extends Application {

    private User user;

    private void launchMainMenu() throws Exception {
        MainMenu mainMenu = new MainMenu(user);
        mainMenu.start();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        this.user = new User();

        // loads user file if exists
        if (DataFile.userFileExists(User.USER_FILE)) {
            user = DataFile.loadUserFile(User.USER_FILE);
        // if this is the user's first time using the program
        // they get prompted as to whether they are VIPs
        } else {
            user.determineIfUserVip(primaryStage);
        }

        launchMainMenu();
    }

    public static void main(String[] args) throws Exception {
        launch(args);
    }
}