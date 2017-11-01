
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Collections;
import java.io.Serializable;

public class User implements Serializable {

    private static Scanner scan = new Scanner(System.in);
    private boolean vip = false;
    private ArrayList<Player> players = new ArrayList<Player>();
    private String[][] preferences = {{"Goals sort order", "Descending"}, 
                                    {"Names sort order", "Ascending"}, 
                                    {"Show goal summary", "True"}};

    public static final String USER_FILE = "userFile.ser";

    // sets VIP status based upon whether the user
    // enters the correct password
    public void setVip() {
        System.out.print("\nPlease enter your VIP password to access the list...\n");

        String password = "#ChelseaIsTheBest";
        String tried_password = scan.nextLine();
        if (password.equals(tried_password)) {
            System.out.println("\nAccess granted...");
            vip = true;
        } else {
            System.out.println("\nIncorrect password...");
            vip = false;
        }
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

    public boolean getVipStatus() {
        return vip;
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
        if (vip) return 8;
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
}