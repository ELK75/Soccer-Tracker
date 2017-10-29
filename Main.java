
import java.util.Scanner;
// TODO remove this import if not neccessary
import java.util.Arrays;
// TODO make so program runs on an arraylist
import java.util.ArrayList;


public class Main {

    private static Scanner scan = new Scanner(System.in);

    //
    // MISCELLANEOUS FUNCTIONS
    //

    private static boolean playersNotEntered(User user) {
        return user.getPlayers().isEmpty();
    }

    // method from StackOverflow
    private static boolean isInteger(String str) {
        if (str == null || str.trim().isEmpty()) {
            return false;
        }
        for (int i = 0; i < str.length(); i++) {
            if(!Character.isDigit(str.charAt(i))) {
                return false;
            }
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

    private static int getBetweenTwoPositiveNumbers(int numOne, int numTwo) {
        int input;
        String promptForInvalidInput;

        if (numTwo > numOne)
            promptForInvalidInput = "\nPlease select a number between " 
            + numOne + " and " + numTwo;
        else promptForInvalidInput = "\nPlease select number " + numOne;

        System.out.println(promptForInvalidInput);
        while (true) {
            input = getPositiveInput();
            if (input == -1) return -1;
            if (input < numOne || input > numTwo)
                System.out.println(promptForInvalidInput);
            else
                break;
        }
        return input;       
    }

    private static int getPositiveInput() {
        // if user enters nothing it means they wish to go back
        // if user enters a number they wish to continue with the program
        String input = scan.nextLine();
        while (input.isEmpty() || !isInteger(input)) {
            // means user wants to go back
            if (input.isEmpty()) return -1;
            System.out.println("Please enter a valid number...");
            input = scan.nextLine();
        }
        return Integer.parseInt(input);
    }

    private static int getPromptInput(User user) {
        int input;
        // loops until user enters correct input
        while (true) {
            input = getPositiveInput();
            if (input < 1 || input > 5) {
                System.out.println("\nPlease enter a number (1-5)");
                askToContinue();
                break;
            } else if ((input == 2 || input == 3) && playersNotEntered(user)) {
                System.out.println("\nMust enter players first...");
                askToContinue();
                input = -1;
                break;
            } else
                break;
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
        System.out.println("WARNING: This will clear all players.");

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
        int input = getBetweenTwoPositiveNumbers(1, inputToClearAllPlayers);
        if (input != -1) {
            if (input == inputToClearAllPlayers) {
                if (wantsToClearPlayers())
                    user.clearPlayers();
            } else
                updatePlayers(user, input);
        }
    }

    private static void inputPlayers(User user) {

        System.out.println("\nEnter the names of " + 
        user.getNumberOfPlayersAllowed() + " players");
        System.out.println("Press enter without a name to exit...");
        // resetting the scanner so program doesn't pick up an empty input
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
                user.getPlayers().add(user.makePlayer(name));
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

        // TODO CHANGE SO WANTS TO ADD CAN GET NEGATIVE INPUT
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
        int playerNumber = getBetweenTwoPositiveNumbers(1, user.getNumberOfPlayers());
        if (playerNumber != -1) addGoals(playerNumber, user);
    }

    //
    // FUNCTIONS RELATING TO OPTION 4
    //

    public static void showPromptForSubscription() {
        System.out.println("\nAccess denied. Please contact a Sassy Soccer sales rep " +
        "for a VIP subscription");
        askToContinue();
    }

    public static void printVipList () {
        System.out.println("\n1. Didier Drogba 15,850\n" + 
            "2. Carli Lloyd 800\n" +
            "3. Megan Rapinoe 300\n" +
            "4. Frank Lampard 200\n");
        askToContinue();
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

        if (user.getVip())
            option4 = "4. View all-time scoring list\n";
        else
            option4 = "4. View all-time scoring list (VIP only)\n";

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
    private static void introScreen(User user) {
        boolean wants_to_exit = false;
        while (!wants_to_exit) {
            promptUser(user);
            int input = getPromptInput(user);
            switch (input) {
                case 1: enterPlayers(user);
                      break;
                case 2: viewPlayers(user);
                        break;
                case 3: updateGoals(user);
                        break;
                case 4: if (user.getVip())
                            printVipList();
                        else
                            showPromptForSubscription();
                        break;
                case 5: wants_to_exit = true;
                      break;
            }
        }
    }

    private static void start() {
        User user = new User();
        user.setVip();
        introScreen(user);
    }

    public static void main(String args[]) {
        start();
    }
}