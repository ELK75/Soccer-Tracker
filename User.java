
import java.util.Scanner;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collections;

public class User {

    private static Scanner scan = new Scanner(System.in);
    private boolean vip;
    private ArrayList<Player> players = new ArrayList<Player>();

    // sets VIP status based upon whether the user
    // enters the correct password
    public void setVip() {
        String password = "#ChelseaIsTheBest";
    
        System.out.print("\nPlease enter your VIP password to access the list...\n");
        String tried_password = scan.next();
        if (password.equals(tried_password)) {
            System.out.println("Access granted...");
            vip = true;
        } else {
            System.out.println("Incorrect password...");
            vip = false;
        }
    }

    public void alphabatizePlayers() {
        if (players != null)
            Collections.sort(players, (a,b) -> a.getName().compareTo(b.getName()));
    }

    public boolean getVip() {
        return vip;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public void clearPlayers() {
        players = new ArrayList<Player>();
    }

    public Player makePlayer(String name) {
        Player player = new Player();
        player.setName(name);
        return player;
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

    // sorts in descending order
    private int compare(int one, int two) {
        if (one > two)
            return -1;
        else if (two < one)
            return 1;
        else
            return 0;
    }

    public void sortByGoals() {
        if (players != null)
            Collections.sort(players, (a,b) -> compare(a.getGoals(), b.getGoals()));
    }

    public Player getPlayer(int index) {
        return players.get(index);
    }

    public int getNumberOfPlayersAllowed() {
        if (vip)
            return 8;
        else
            return 5;
    }

    public int getNumberOfPlayers() {
        return players.size();
    }
}