
import java.util.Scanner;
import java.util.ArrayList;
import javafx.application.*;
import javafx.stage.*;


//
// GITHUB: https://github.com/Elijah-Kajinic/Soccer-Tracker
//

public class Main extends Application {

    private User user;

    private void launchMainMenu() throws Exception {
        MainMenu mainMenu = new MainMenu(user);
        mainMenu.start();
    }

    private void determineIfUserVip() throws Exception {
        String password = user.getPassword();
        
        String attemptedPassword = Dialog.getTextInput("VIP Access", "Enter VIP Password", "Password");
        if (attemptedPassword != null) {
            if (attemptedPassword.equals(password)) { 
                user.setVip(true);
                Dialog.showMessage("VIP", "Login Successful", null);
                ChangePasswordBox changePasswordBox = new ChangePasswordBox(user);
                changePasswordBox.changePassword();
            } else {
                Dialog.showMessage("VIP", "Login Unsuccessful", null);
                user.setVip(false);
            }
            launchMainMenu();
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        this.user = new User();

        // loads user file if exists
        if (DataFile.userFileExists(User.USER_FILE)) {
            user = DataFile.loadUserFile(User.USER_FILE);
            launchMainMenu();
        // if this is the user's first time using the program
        // they get prompted as to whether they are VIPs
        } else {
            determineIfUserVip();
        }
    }

    public static void main(String[] args) throws Exception {
        launch(args);
    }
}