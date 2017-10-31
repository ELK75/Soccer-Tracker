
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Collections;
import java.io.Serializable;

public class User implements Serializable {

    private static Scanner scan = new Scanner(System.in);
    private boolean vip = false;
    private ArrayList<Player> players = new ArrayList<Player>();
    private String[][] prefrences = {{"Goals sort order", "Descending"}, 
                                    {"Names sort order", "Ascending"}, 
                                    {"Show goal summary", "True"}};

    public static final String USER_FILE = "userFile.ser";

    // sets VIP status based upon whether the user
    // enters the correct password
    public void setVip() {
        System.out.print("\nPlease enter your VIP password to access the list...\n");

        // TODO CHANGE
        String password = "a";
        String tried_password = scan.nextLine();
        if (password.equals(tried_password)) {
            System.out.println("Access granted...");
            vip = true;
        } else {
            System.out.println("Incorrect password...");
            vip = false;
        }
    }

    public String[][] getPrefrences() {
        return prefrences;
    }

    public void alphabatizePlayers() {
        if (players != null)
            Collections.sort(players, (a,b) -> a.getName().compareTo(b.getName()));
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

    public void sortByGoals() {
        if (players != null)
            Collections.sort(players, (a,b) -> compare(a.getGoals(), b.getGoals()));
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

    // sorts in descending order
    private int compare(int one, int two) {
        if (one > two)
            return -1;
        else if (two < one)
            return 1;
        else
            return 0;
    }
}