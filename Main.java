
import java.util.Scanner;
import java.util.ArrayList;

public class Main {

    private static Scanner scan = new Scanner(System.in);

    //
    // MISCELLANEOUS FUNCTIONS
    //

    private static boolean playersNotEntered(User user) {
        return user.getPlayers().isEmpty();
    }

    // method modified from StackOverflow
    private static boolean isInteger(String str) {
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

    private static boolean nameAlreadyInputted(String name, User user) {
        for (Player player : user.getPlayers()) {
            if (player.getName().equals(name))
                return true;
        }
        return false;
    }

    private static void askToContinue() {
        System.out.print("-Press enter to continue-");
        try{System.in.read();}
        catch(Exception e){}
        scan.nextLine();
    }

    // returns either singular or plural form of goal
    private static String goalOrGoals(int count) {
        return count == 1 ? "goal" : "goals";
    }

    private static String getBetweenTwoNumbers(int numOne, int numTwo) {
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

    private static String getPromptInput(User user) {
        String input;
        // loops until user enters correct input
        while (true) {
            input = scan.nextLine();
            if (!isInteger(input) || Integer.parseInt(input) < 1 || Integer.parseInt(input) > 5) {
                System.out.println("\nPlease enter a number (1-5)...");
                askToContinue();
                promptUser(user);
            } else if ((Integer.parseInt(input) == 2 || Integer.parseInt(input) == 3) 
                && playersNotEntered(user)) {
                    System.out.println("\nMust enter players first...");
                    askToContinue();
                    promptUser(user);
            } else {
                break;
            }
        }
        return input;
    }

    //
    // FUNCTIONS RELATING TO OPTION 1
    //

    private static void updatePlayers(User user, int input) {
        System.out.println("Please enter a name...");
        String name = scan.nextLine();
        // if user just presses enter does nothing`
        if (name.isEmpty()) {}
        else if (!nameAlreadyInputted(name, user)) {
            user.getPlayer(input-1).setName(name);
            user.getPlayer(input-1).setGoals(0);
        } else {
            int numberOfGoals = user.getPlayer(input-1).getGoals();
            System.out.println("Name already entered. Has " +
            + numberOfGoals + " " + goalOrGoals(numberOfGoals) + ".");
            askToContinue();
        }
    }

    private static boolean wantsToClearPlayers() {
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

    private static void showPlayers(User user) {
        System.out.println();
        for (int i = 0; i < user.getNumberOfPlayers(); i++) {
            System.out.println((i+1) + ".) " + user.getPlayer(i).getName());
        }

        int inputToClearAllPlayers = user.getNumberOfPlayers()+1;
        System.out.println(inputToClearAllPlayers + ".) <Clear list of players>");

        String input = getBetweenTwoNumbers(1, inputToClearAllPlayers);
        if (!input.isEmpty()) {
            if (Integer.parseInt(input) == inputToClearAllPlayers) {
                if (wantsToClearPlayers())
                    user.clearPlayers();
            } else
                updatePlayers(user, Integer.parseInt(input));
        }
    }

    private static void inputPlayers(User user) {

        System.out.println("\nEnter the names of " + 
        user.getNumberOfPlayersAllowed() + " players");
        System.out.println("Press enter without a name to exit...");
        for (int i = 0; i < user.getNumberOfPlayersAllowed(); i++) {
            System.out.println("\nPlease enter the name of player " + (i+1) + 
            " (max is " + user.getNumberOfPlayersAllowed() + ")");

            String name = scan.nextLine();
            while (i != 0 && nameAlreadyInputted(name, user)) {
                System.out.println("Name already entered");
                name = scan.nextLine();
            }
            if (name.isEmpty())
                break;
            else {
                Player player = new Player(name);
                user.getPlayers().add(player);
                if (i == user.getNumberOfPlayersAllowed() - 1)
                    System.out.println("\nMax players entered...");
            }
        }
    }
    
    private static void enterPlayers(User user) {
        if (playersNotEntered(user)) {
            inputPlayers(user);
        } else {
            user.alphabatizePlayers();
            showPlayers(user);
        }
    }

    //
    // FUNCTIONS RELATING TO OPTION 2
    //

    private static void printPlayerStats(User user) {
        int[] goalArray = new int[user.getNumberOfPlayers()];
        for (int i = 0; i < user.getNumberOfPlayers(); i++) {
            goalArray[i] = user.getPlayer(i).getGoals();
        }
        System.out.printf("Average number of goals is %,.2f\n", arrayAverage(goalArray));
        System.out.printf("The total number of goals is %,d\n\n", arraySum(goalArray));
    }

    private static int arraySum(int[] arr) {
        int arrSum = 0;
        for (int i = 0; i < arr.length; i++) {
            arrSum += arr[i];
        }
        return arrSum;
    }

    private static double arrayAverage(int[] arr) {
        double total = arraySum(arr);
        double divisor = arr.length;
        return total / divisor;
    }

    private static int getMostGoalsScored(ArrayList<Player> players) {
        int goalsOfTopScorer = 0;
        for (Player player: players) {
            if (player.getGoals() > goalsOfTopScorer)
                goalsOfTopScorer = player.getGoals();
        }
        return goalsOfTopScorer;
    }

    // method is necessary since there is a possibility of
    // multiple top scorers
    // TODO SHOW USER IN A CLEANER MANNER
    private static void printTopScorers(ArrayList<Player> players) {
        int mostGoalsScored = getMostGoalsScored(players);
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).getGoals() == mostGoalsScored) {
                System.out.println(players.get(i).getName() + " is a top goal scorer");
            }

        }
    }

    private static void viewPlayers(User user) {
        user.alphabatizePlayers();
        user.sortByGoals();
        System.out.println();
        for (int i = 0; i < user.getNumberOfPlayers(); i++) {
            System.out.printf(user.getPlayer(i).getName() + ", %,d " + 
            goalOrGoals(user.getPlayer(i).getGoals()) + "\n", user.getPlayer(i).getGoals());
        }

        System.out.println();
        printTopScorers(user.getPlayers());
        printPlayerStats(user);

        askToContinue();
    }

    //
    // FUNCTIONS RELATING TO OPTION 3
    //
    
    private static void addGoals(int player, User user) {

        // subtracts one to get index of player
        int playerIndex = player - 1;

        System.out.printf("\n" + user.getPlayer(playerIndex).getName() + " has %,d " +
        goalOrGoals(user.getPlayer(playerIndex).getGoals()) + " how many do you want to add...\n", 
        user.getPlayer(playerIndex).getGoals());

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
                    user.getPlayer(playerIndex).addGoals(goalsToAdd);
                    break;
                }
            }
            input = scan.nextLine();
        }
    }
    
    private static void printPlayerGoals(User user) {
        user.alphabatizePlayers();
        user.sortByGoals();
        for (int i = 0; i < user.getNumberOfPlayers(); i++) {
            System.out.printf(i+1 + ".) " + user.getPlayer(i).getName() + ", %,d " +
             goalOrGoals(user.getPlayer(i).getGoals()) + "\n", user.getPlayer(i).getGoals());
        }
    }

    private static void updateGoals(User user) {
        System.out.println();
        printPlayerGoals(user);
        String playerNumber = getBetweenTwoNumbers(1, user.getNumberOfPlayers());
        if (!playerNumber.isEmpty()) addGoals(Integer.parseInt(playerNumber), user);
    }

    //
    // FUNCTIONS RELATING TO OPTION 4
    //

    public static void showPromptForSubscription() {
        System.out.println("\nAccess denied. Please contact a Sassy Soccer sales rep " +
        "for a VIP subscription");
        askToContinue();
    }

    public static void changePreference(int input, String[][] preferences) {
        String current_preference = preferences[input-1][1];
        String new_preference = "";
        if (current_preference == "Ascending" || current_preference == "Descending")
            // switches from ascending to descending or descending to ascending
            new_preference = current_preference == "Descending" ? "Ascending" : "Descending";
        else if (current_preference == "True" || current_preference == "False")
            // switches from true to false and false to true
            new_preference = current_preference == "True" ? "False" : "True";

        preferences[input-1][1] = new_preference;

        System.out.println();
        System.out.println(preferences[input-1][0] + " was changed to " + new_preference);
    }

    public static void changePreferences(User user, String[][] preferences) {
        String input = getBetweenTwoNumbers(1, preferences.length);
        if (!input.isEmpty()) {
            changePreference(Integer.parseInt(input), preferences);
        }
    }

    public static void showPrefrences(User user) {
        System.out.println();
        String[][] preferences = user.getPrefrences();
        for (int i = 0; i < preferences.length; i++) {
            String spaces = String.format(
                "%" + (25-preferences[i][0].length()) + "s", "");
            System.out.println(
                preferences[i][0] + spaces + "[" + preferences[i][1] + "]");
        }

        changePreferences(user, preferences);
    }

    //
    // FUNCTIONS RELATING TO THE START MENU
    //

    private static void promptUser(User user) {
        String option1;
        String option4;

        if (playersNotEntered(user))
            option1 = "1. New list of players\n";
        else
            option1 = "1. Update/Delete players to track\n";

        if (user.getVipStatus())
            option4 = "4. View/Update Prefrences\n";
        else
            option4 = "4. View/Update Prefrences(VIP only)\n";

        System.out.println();
        System.out.println(option1 +
                           "2. View my list of players and goals\n" +
                           "3. Update goals scored\n" +
                           option4 +
                           "5. Exit the program\n" +
                           "Please select a menu option (1-5)");
    }


    // a main menu where the user enters where
    // they want to go
    private static void introScreen(User user) throws Exception {
        boolean wantsToExit = false;
        while (!wantsToExit) {
            promptUser(user);
            String input = getPromptInput(user);
            switch (Integer.parseInt(input)) {
                case 1: enterPlayers(user);
                        break;
                case 2: viewPlayers(user);
                        break;
                case 3: updateGoals(user);
                        break;
                case 4: if (user.getVipStatus())
                            showPrefrences(user);
                        else
                            showPromptForSubscription();
                        break;
                case 5: 
                        DataFile.writeUserFile(user, User.USER_FILE);
                        wantsToExit = true;
                        break;
            }
        }
    }

    private static void start() throws Exception {
        User user = new User();
        // loads user file if it exists
        if (DataFile.userFileExists(User.USER_FILE)) {
            user = DataFile.loadUserFile(User.USER_FILE);
        // if this is the user's first time using the program
        // they get prompted as to whether they are VIPs
        } else {
            user.setVip();
        }
        introScreen(user);
    }

    public static void main(String args[]) throws Exception {
        start();
    }
}