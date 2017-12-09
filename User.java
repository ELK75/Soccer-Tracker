
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

public abstract class User implements Serializable {

    protected ArrayList<Player> players = new ArrayList<Player>();
    protected static String[][] preferences = {{"Goals sort order", "Descending"}, 
                                                {"Names sort order", "Ascending"}, 
                                                {"Show goal summary", "True"}};

    public int getNumberOfPlayers() {
        return players.size();
    }

    public boolean playersNotEntered() {
        return players.isEmpty();
    }

    public Player getPlayer(int index) {
        return players.get(index);
    }

    public String[][] getPreferences() {
        return preferences;
    }

    public String getPlayerAndGoalsString(int index) {
        String playerAndGoals = String.format(getPlayer(index).getName() + ", %,d " + 
            goalOrGoals(getPlayer(index).getGoals()), getPlayer(index).getGoals());
        return playerAndGoals;
    }

    public ArrayList<String> getPlayerNamesAndGoals() {
        ArrayList<String> playerNamesAndGoals = new ArrayList<String>();
        for (int i = 0; i < getNumberOfPlayers(); i++) {
            playerNamesAndGoals.add(getPlayerAndGoalsString(i));
        }
        return playerNamesAndGoals;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }

    public ArrayList<String> getPlayerNames() {
        ArrayList<String> playerNames = new ArrayList<String>();
        for (int i = 0; i < players.size(); i++) {
            playerNames.add(players.get(i).getName());
        }
        return playerNames;
    }

    private int compare(int one, int two) {
        if (one > two) return -1;
        else if (one < two) return 1;
        else return 0;
    }

    private void sortByNamesAsc() {
        Collections.sort(players, (a,b) -> a.getName().compareTo(b.getName()));
    }

    private void sortByNamesDesc() {
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

    private void sortByGoalsDesc() {
        Collections.sort(players, (a,b) -> compare(a.getGoals(), b.getGoals()));
    }

    private void sortByGoalsAsc() {
        Collections.sort(players, (a,b) -> compare(b.getGoals(), a.getGoals()));
    }

    public void sortByGoalPreference() {
        if (players != null) {
            if (preferences[0][1].equals("Descending"))
                sortByGoalsDesc();
            else if (preferences[0][1].equals("Ascending"))
                sortByGoalsAsc();
        }
    }

    // sorts by names. breaks tie in names by goals
    public void sortByGoalsBreakTieByName() {
        sortByNamePreference();
        sortByGoalPreference();
    }
    //
    // METHODS RELATING TO PLAYER STATS
    //

    // returns either singular or plural form of goal
    public String goalOrGoals(int count) {
        return count == 1 ? "goal" : "goals";
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
        return String.format("Average number of goals is %,.2f", arrayAverage(goalArray()));
    }
    
    public String getGoalTotal() {
        return String.format("The total number of goals is %,d", arraySum(goalArray()));
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

    public abstract String getName();

}