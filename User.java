
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Collections;
import java.io.Serializable;
import javafx.stage.*;
import javafx.scene.control.*;

public class User implements Serializable {

    private static Scanner scan = new Scanner(System.in);
    private boolean isVip;
    private ArrayList<Player> players = new ArrayList<Player>();
    private String[][] preferences = {{"Goals sort order", "Descending"}, 
                                    {"Names sort order", "Ascending"}, 
                                    {"Show goal summary", "True"}};

    public static final String USER_FILE = "userFile.ser";

    // sets VIP status based upon whether the user
    // enters the correct password
    public void determineIfUserVip(Stage stage) {
        // TODO CHANGE TO "#ChelseaIsTheBest"
        String password = "a";
        VipMenu vipMenu = new VipMenu(password);
        vipMenu.start();
        isVip = vipMenu.getIsVip();
    }

    public String[][] getPreferences() {
        return preferences;
    }

    public void sortByNamesAsc() {
        Collections.sort(players, (a,b) -> a.getName().compareTo(b.getName()));
    }

    public void sortByNamesDesc() {
        Collections.sort(players, (a,b) -> b.getName().compareTo(a.getName()));
    }

    public void sortByNamePreference() {
        if (players != null) {
            if (preferences[1][1].equals("Ascending"))
                sortByNamesAsc();
            else if (preferences[1][1].equals("Descending"))
                sortByNamesDesc();
        }
    }

    public boolean getIsVip() {
        return isVip;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }

    public void clearPlayers() {
        players = new ArrayList<Player>();
    }

    public String[] getPlayerNames() {
        String[] playerNames = new String[getNumberOfPlayers()];
        for (int i = 0; i < playerNames.length; i++) {
            playerNames[i] = players.get(i).getName();
        }
        return playerNames;
    }

    public int[] getPlayerGoals() {
        int[] playerGoals = new int[getNumberOfPlayers()];
        for (int i = 0; i < playerGoals.length; i++) {
            playerGoals[i] = players.get(i).getGoals();
        }
        return playerGoals;
    }

    public void sortByGoalsDesc() {
        Collections.sort(players, (a,b) -> compare(a.getGoals(), b.getGoals()));
    }

    public void sortByGoalsAsc() {
        Collections.sort(players, (a,b) -> compare(b.getGoals(), a.getGoals()));
    }

    public void sortByGoalPreferences() {
        if (players != null) {
            if (preferences[0][1].equals("Descending"))
                sortByGoalsDesc();
            else if (preferences[0][1].equals("Ascending"))
                sortByGoalsAsc();
        }
    }

    public Player getPlayer(int index) {
        return players.get(index);
    }

    public int getNumberOfPlayersAllowed() {
        if (isVip) return 8;
        else return 5;
    }

    public int getNumberOfPlayers() {
        return players.size();
    }

    private int compare(int one, int two) {
        if (one > two) return -1;
        else if (one < two) return 1;
        else return 0;
    }
    public boolean playersNotEntered() {
        return getPlayers().isEmpty();
    }
    
    // method modified from StackOverflow
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
    
    public boolean nameAlreadyInputted(String name) {
        for (Player player : getPlayers()) {
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

    // returns either singular or plural form of goal
    public String goalOrGoals(int count) {
        return count == 1 ? "goal" : "goals";
    }
    
    public int getIndexFromName(String playerName) {
        for (int i = 0; i < getNumberOfPlayers(); i++) {
            if (getPlayer(i).getName().equals(playerName)) 
                return i;
        }
        return -1;
    }

    private int[] goalArray() {
        int[] goalArray = new int[getNumberOfPlayers()];
        for (int i = 0; i < getNumberOfPlayers(); i++) {
            goalArray[i] = getPlayer(i).getGoals();
        }
        return goalArray;
    }

    private int arraySum(int[] arr) {
        int arrSum = 0;
        for (int i = 0; i < arr.length; i++) {
            arrSum += arr[i];
        }
        return arrSum;
    }

    public String getGoalAverage() {
        return String.format("Average number of goals is %,.2f\n", arrayAverage(goalArray()));
    }
    
    public String getGoalTotal() {
        return String.format("The total number of goals is %,d\n\n", arraySum(goalArray()));
    }
    
    private double arrayAverage(int[] arr) {
        double total = arraySum(arr);
        double divisor = arr.length;
        return total / divisor;
    }
    
    private int getMostGoalsScored() {
        int goalsOfTopScorer = 0;
        for (Player player: players) {
            if (player.getGoals() > goalsOfTopScorer)
                goalsOfTopScorer = player.getGoals();
        }
        return goalsOfTopScorer;
    }
    
    // method is necessary since there is a possibility of
    // multiple top scorers
    private ArrayList<String> getTopScorers() {
        int mostGoalsScored = getMostGoalsScored();
        ArrayList<String> topScorers = new ArrayList<String>();
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).getGoals() == mostGoalsScored) {
                topScorers.add(players.get(i).getName());
            }
        }
        return topScorers;
    }
    
    public String returnTopScorers() {
        String listOfTopScorers = "Top Scorers: ";
        ArrayList<String> topScorers = getTopScorers();

        if (topScorers.size() >= 1)
            listOfTopScorers += topScorers.get(0);
        for (int i = 1; i < topScorers.size(); i++) {
            listOfTopScorers += ", " + topScorers.get(i);
        }
        
        return listOfTopScorers;
    }
    
    public String getPlayerAndGoalsString(int index) {
        String playerAndGoals = String.format(getPlayer(index).getName() + ", %,d " + 
            goalOrGoals(getPlayer(index).getGoals()), getPlayer(index).getGoals());
        return playerAndGoals;
    }
    
    public void changePreference(int input) {
        String current_preference = preferences[input-1][1];
        String new_preference = "";
        if (current_preference.equals("Ascending") || current_preference.equals("Descending"))
            // switches from ascending to descending or descending to ascending
            new_preference = current_preference.equals("Descending") ? "Ascending" : "Descending";
        else if (current_preference.equals("True") || current_preference.equals("False"))
            // switches from true to false and false to true
            new_preference = current_preference.equals("True") ? "False" : "True";
    
        preferences[input-1][1] = new_preference;
    }
}