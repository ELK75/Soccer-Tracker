
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Collections;
import java.io.Serializable;
import javafx.stage.*;

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
    
    private void askToContinue() {
        System.out.print("-Press enter to continue-");
        try{System.in.read();}
        catch(Exception e){}
        scan.nextLine();
    }
    
    // returns either singular or plural form of goal
    public String goalOrGoals(int count) {
        return count == 1 ? "goal" : "goals";
    }
    
    private String getBetweenTwoNumbers(int numOne, int numTwo) {
        String promptForInput;
        if (numTwo == numOne)
            promptForInput = "\nPlease select number " + numOne;
        else promptForInput = "\nPlease select a number between " 
            + numOne + " and " + numTwo;
        System.out.println(promptForInput);
    
        String input = scan.nextLine();
    
        while (true) {
            if (input.isEmpty()) return input;
            
            if (!isInteger(input) || 
            Integer.parseInt(input) < numOne || Integer.parseInt(input) > numTwo) {
                System.out.println(promptForInput);
                input = scan.nextLine();
            } else break;
        }
        return input;       
    }
    
    private String getPromptInput() {
        String input;
        // loops until user enters correct input
        while (true) {
            input = scan.nextLine();
            if (!isInteger(input) || Integer.parseInt(input) < 1 || Integer.parseInt(input) > 5) {
                System.out.println("\nPlease enter a number (1-5)...");
                askToContinue();
                //promptUser();
            } else if ((Integer.parseInt(input) == 2 || Integer.parseInt(input) == 3) 
                && playersNotEntered()) {
                    System.out.println("\nMust enter players first...");
                    askToContinue();
                    //promptUser();
            } else {
                break;
            }
        }
        return input;
    }
    
    //
    // FUNCTIONS RELATING TO OPTION 1
    //
    
    public void updatePlayer(int input) {
        System.out.println("Please enter a name...");
        String name = scan.nextLine();
        // if user just presses enter does nothing
        if (!name.isEmpty()) {
            if (!nameAlreadyInputted(name)) {
                getPlayer(input-1).setName(name);
                getPlayer(input-1).setGoals(0);
            } else {
                int numberOfGoals = getPlayer(input-1).getGoals();
                System.out.println();
                System.out.println("Name already entered. Has " +
                + numberOfGoals + " " + goalOrGoals(numberOfGoals) + ".");
                askToContinue();
            }
        }
    }
    
    private boolean wantsToClearPlayers() {
        System.out.println("\nWARNING: This will clear all players.");
    
        while (true) {
            System.out.println("Are you sure you want to continue? (y/n)");
            String input = scan.nextLine();
            if (input.equals("y") || input.equals("Y"))
                return true;
            else if (input.equals("n") || input.equals("N") || input.isEmpty())
                return false;
            else {
                System.out.println("\nCould not understand input...");
            }
        }
    }
    
    public int getIndexFromName(String playerName) {
        for (int i = 0; i < getNumberOfPlayers(); i++) {
            if (getPlayer(i).getName().equals(playerName)) 
                return i;
        }
        return -1;
    }

    private void updatePlayers() {
    
        System.out.println();
    
        sortByNamesAsc();
        for (int i = 0; i < getNumberOfPlayers(); i++) {
            System.out.println((i+1) + ".) " + getPlayer(i).getName());
        }
    
        int inputToClearAllPlayers = getNumberOfPlayers()+1;
        System.out.println(inputToClearAllPlayers + ".) <Clear list of players>");
    
        String input = getBetweenTwoNumbers(1, inputToClearAllPlayers);
        if (!input.isEmpty()) {
            if (Integer.parseInt(input) == inputToClearAllPlayers) {
                if (wantsToClearPlayers())
                    clearPlayers();
            } else
                updatePlayer(Integer.parseInt(input));
        }
    }
    
    private void inputPlayers() {
    
        System.out.println("\nEnter the names of " + 
        getNumberOfPlayersAllowed() + " players");
        System.out.println("Press enter without a name to exit...");
        for (int i = 0; i < getNumberOfPlayersAllowed(); i++) {
            String enterPlayerPrompt = "\nPlease enter the name of player " + (i+1) + 
                " (max is " + getNumberOfPlayersAllowed() + ")";
            System.out.println(enterPlayerPrompt);
    
            String name = scan.nextLine();
            while (i != 0 && nameAlreadyInputted(name)) {
                System.out.println("\n-Player already entered-");
                System.out.println(enterPlayerPrompt);
                name = scan.nextLine();
            }
            if (name.isEmpty())
                break;
            else {
                Player player = new Player(name);
                getPlayers().add(player);
                if (i == getNumberOfPlayersAllowed() - 1) {
                    System.out.println("\nMax players entered...");
                    askToContinue();
                }
            }
        }
    }
    
    public void enterPlayers() {
        inputPlayers();
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

    /*
    public void viewPlayers() {
        sortByNamePreference();
        sortByGoalPreferences();
        System.out.println();
        for (int i = 0; i < getNumberOfPlayers(); i++) {
            System.out.printf(getPlayer(i).getName() + ", %,d " + 
            goalOrGoals(getPlayer(i).getGoals()) + "\n", getPlayer(i).getGoals());
        }
    
        System.out.println();
        if (getPreferences()[2][1].equals("True")) {
            printTopScorers();
            printPlayerStats();
        }
    
        askToContinue();
    } */
    
    //
    // FUNCTIONS RELATING TO OPTION 3
    //
    
    private void addGoals(int player) {
    
        // subtracts one to get index of player
        int playerIndex = player - 1;
    
        System.out.printf("\n" + getPlayer(playerIndex).getName() + " has %,d " +
        goalOrGoals(getPlayer(playerIndex).getGoals()) + " how many do you want to add...\n", 
        getPlayer(playerIndex).getGoals());
    
        String input = scan.nextLine();
        int goalsToAdd = 0;
        String promptForInvalidInput = "Please enter a positive number...";
        while (true) {
            if (input.isEmpty()) break;
            else if (!isInteger(input)) System.out.println(promptForInvalidInput);
            else {
                goalsToAdd = Integer.parseInt(input);
                if (goalsToAdd < 0) System.out.println(promptForInvalidInput);
                else {
                    getPlayer(playerIndex).addGoals(goalsToAdd);
                    break;
                }
            }
            input = scan.nextLine();
        }
    }
    
    private void printPlayerGoals() {
        sortByNamesAsc();
        sortByGoalsDesc();
        for (int i = 0; i < getNumberOfPlayers(); i++) {
            System.out.printf(i+1 + ".) " + getPlayer(i).getName() + ", %,d " +
             goalOrGoals(getPlayer(i).getGoals()) + "\n", getPlayer(i).getGoals());
        }
    }
    
    public void updateGoals() {
        System.out.println();
        printPlayerGoals();
        String playerNumber = getBetweenTwoNumbers(1, getNumberOfPlayers());
        if (!playerNumber.isEmpty()) addGoals(Integer.parseInt(playerNumber));
    }
    
    //
    // FUNCTIONS RELATING TO OPTION 4
    //
    
    public void showPromptForSubscription() {
        System.out.println("\nAccess denied. Please contact a Sassy Soccer sales rep " +
        "for a VIP subscription");
    }
    
    private void changePreference(int input, String[][] preferences) {
        String current_preference = preferences[input-1][1];
        String new_preference = "";
        if (current_preference.equals("Ascending") || current_preference.equals("Descending"))
            // switches from ascending to descending or descending to ascending
            new_preference = current_preference.equals("Descending") ? "Ascending" : "Descending";
        else if (current_preference.equals("True") || current_preference.equals("False"))
            // switches from true to false and false to true
            new_preference = current_preference.equals("True") ? "False" : "True";
    
        preferences[input-1][1] = new_preference;
    
        System.out.println();
        System.out.println(preferences[input-1][0] + " was changed to " + new_preference);
    }
    
    private void changePreferences(String[][] preferences) {
        String input = getBetweenTwoNumbers(1, preferences.length);
        if (!input.isEmpty()) {
            changePreference(Integer.parseInt(input), preferences);
        }
    }
    
    public void showPreferences() {
        System.out.println();
        String[][] preferences = getPreferences();
        for (int i = 0; i < preferences.length; i++) {
            String spaces = String.format(
                "%" + (25-preferences[i][0].length()) + "s", "");
            System.out.println(
                preferences[i][0] + spaces + "[" + preferences[i][1] + "]");
        }
    
        changePreferences(preferences);
    }
}