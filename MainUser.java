
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Collections;
import java.io.Serializable;
import javafx.stage.*;
import javafx.scene.control.*;
import java.util.Optional;


public class MainUser extends User implements Serializable {

    private boolean isVip;
    private String password = "#ChelseaIsTheBest";                            
    private static final String USER_FILE = "userFile.txt";
    // is what is displayed when a VIP user clicks connect to other players
    private ArrayList<SassyUser> connectedUsers = new ArrayList<SassyUser>();

    // gets an ArrayList of Strings ex: "Goal Sort Order : Ascending"
    public static ArrayList<String> getPreferencesAndCurrentPreference() {
        ArrayList<String> preferenceAndCurrentPreference = new ArrayList<String>();

        for (int i = 0; i < preferences.length; i++) {
            preferenceAndCurrentPreference.add(preferences[i][0] + ": " +
            preferences[i][1]);

        }
        return preferenceAndCurrentPreference;
    }

    public String getName() {
        return "You";
    }

    public String getPassword() {
        return password;
    }

    public static String getUserFile() {
        return "userFile.txt";
    }

    public void setPassword(String newPassword) {
        this.password = newPassword;
    }

    public boolean getIsVip() {
        return isVip;
    }

    public ArrayList<SassyUser> getConnectedUsers() {
        return connectedUsers;
    }

    public void setConnectedUsers(ArrayList<SassyUser> connectedUsers) {
        this.connectedUsers = connectedUsers;
    }

    public void setVip(boolean status) {
        this.isVip = status;
    }

    public void clearPlayers() {
        players = new ArrayList<Player>();
    }

    public int[] getPlayerGoals() {
        int[] playerGoals = new int[getNumberOfPlayers()];
        for (int i = 0; i < playerGoals.length; i++) {
            playerGoals[i] = players.get(i).getGoals();
        }
        return playerGoals;
    }

    public int getNumberOfPlayersAllowed() {
        if (isVip) return 8;
        else return 5;
    }
    
    public boolean nameAlreadyInputted(String name) {
        for (Player player : players) {
            if (player.getName().equals(name))
                return true;
        }
        return false;
    }
    
    public ListView<String> getPlayerListView() {
        ListView<String> playerListView = new ListView<String>();
        for (int i = 0; i < getNumberOfPlayers(); i++) {
            playerListView.getItems().add(getPlayer(i).getName());
        }
        return playerListView;
    }

    public ListView<String> getPlayerListViewWithGoals() {
        ListView<String> playerListViewWithGoals = new ListView<String>();
        for (int i = 0; i < getNumberOfPlayers(); i++) {
            String playerAndGoals = getPlayerAndGoalsString(i);
            playerListViewWithGoals.getItems().add(playerAndGoals);
        }
        return playerListViewWithGoals;
    }

    public ListView<String> getPreferenceListView() {
        ListView<String> preferenceListView = new ListView<String>();
        String preference = "";
        for (int i = 0; i < 3; i++) {
            preference += preferences[i][0] + " : " + preferences[i][1];
            preferenceListView.getItems().add(preference);
            preference = "";
        }
        return preferenceListView;
    }
    
    public static void changePreference(int index) {
        String current_preference = preferences[index][1];
        String new_preference = "";
        if (current_preference.equals("Ascending") || current_preference.equals("Descending"))
            // switches from ascending to descending or descending to ascending
            new_preference = current_preference.equals("Descending") ? "Ascending" : "Descending";
        else if (current_preference.equals("True") || current_preference.equals("False"))
            // switches from true to false and false to true
            new_preference = current_preference.equals("True") ? "False" : "True";
    
        preferences[index][1] = new_preference;
    }
}