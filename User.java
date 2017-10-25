
import java.util.Scanner;
import java.util.Arrays;

public class User {

    private static Scanner scan = new Scanner(System.in);
    private boolean vip;
    private Player[] players = null;

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

    // creates an array of Player Objects
    public void createPlayers() {
        if (vip) {
            players = new Player[8];
        } else {
            players = new Player[5];
        }
        for (int i = 0; i < players.length; i++)
            players[i] = new Player();
    }

    // this method is for when the user presses
    // enter without entering a name
    public void clearNullPlayers() {
        // if the first name is null it means the user
        // has not entered his players yet
        if (players[0].getName() != null) {
            int nonNullCount = 0;
            for (int i = 0; i < players.length; i++) {
                if (players[i].getName() != null)
                    nonNullCount += 1;
            }

            if (nonNullCount == 0)
                players = null;
            else {
                Player[] playersWithoutNull = new Player[nonNullCount];
                for (int i = 0; i < playersWithoutNull.length; i++)
                    playersWithoutNull[i] = players[i];
        
                players = playersWithoutNull.clone();
            }
        }
    }

    public void alphabatizePlayers() {
        if (players != null)
            Arrays.sort(players, (a,b) -> a.getName().compareTo(b.getName()));
    }

    public boolean getVip() {
        return vip;
    }

    public Player[] getPlayers() {
        return players;
    }

    public String[] getPlayerNames() {
        String[] playerNames = new String[getNumberOfPlayers()];
        for (int i = 0; i < playerNames.length; i++) {
            playerNames[i] = players[i].getName();
        }
        return playerNames;
    }

    public int[] getPlayerGoals() {
        int[] playerGoals = new int[getNumberOfPlayers()];
        for (int i = 0; i < playerGoals.length; i++) {
            playerGoals[i] = players[i].getGoals();
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
            Arrays.sort(players, (a,b) -> compare(a.getGoals(), b.getGoals()));
    }

    public Player getPlayer(int index) {
        return players[index];
    }

    public int getNumberOfPlayers() {
        return players.length;
    }
}