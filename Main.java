
import java.util.Scanner;
import java.util.ArrayList;
import javafx.application.*;
import javafx.stage.*;
import cs401.sassy.tracker.data.*;

//
// GITHUB: https://github.com/Elijah-Kajinic/Soccer-Tracker
//

public class Main extends Application {

    private MainUser mainUser;
    private ArrayList<SassyUser> sassyUsers = new ArrayList<SassyUser>();

    private void launchMainMenu() throws Exception {
        MainMenu mainMenu = new MainMenu(mainUser, sassyUsers);
        mainMenu.start();
    }

    private void determineIfUserVip() throws Exception {
        String password = mainUser.getPassword();
        
        String attemptedPassword = Dialog.getTextInput("VIP Access", "Enter VIP Password", "Password");
        if (attemptedPassword != null) {
            if (attemptedPassword.equals(password)) { 
                mainUser.setVip(true);
                Dialog.showMessage("VIP", "Login Successful", null);
                ChangePasswordBox changePasswordBox = new ChangePasswordBox(mainUser);
                changePasswordBox.changePassword();
            } else {
                Dialog.showMessage("VIP", "Login Unsuccessful", null);
                mainUser.setVip(false);
            }
            launchMainMenu();
        }
    }

    public void getSassyUsers() {
        cs401.sassy.tracker.data.User[] givenSassyUsers = SassyUserProvider.getInstance().getUsers();
        for (int i = 0; i < givenSassyUsers.length; i++) {
            SassyUser sassyUser = new SassyUser(givenSassyUsers[i]);
            sassyUsers.add(sassyUser);
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        this.mainUser = new MainUser();
        getSassyUsers();

        // loads mainUser file if exists
        if (DataFile.userFileExists(MainUser.getUserFile())) {
            mainUser = DataFile.loadUserFile(MainUser.getUserFile());
            launchMainMenu();
        // if this is the mainUser's first time using the program
        // they get prompted as to whether they are VIPs
        } else {
            determineIfUserVip();
        }
    }

    public static void main(String[] args) throws Exception {
        launch(args);
    }
}