
import java.util.Scanner;
import java.util.Arrays;

public class Main {

    private static Scanner scan = new Scanner(System.in);

    //
    // MISCELLANEOUS FUNCTIONS
    //

    private static boolean playersNotEntered(User user) {
        if (user.getPlayers() == null) 
            return true;
        for (Player player : user.getPlayers())
            if (player.getName() != null)
                return false;
        return true;
    }

    private static boolean nameAlreadyInputted(String name, User user) {
        for (Player player : user.getPlayers()) {
            if (player.getName() != null && player.getName().equals(name))
                return true;
        }
        return false;
    }

    private static void askToContinue() {
        System.out.print("-Press enter to continue-");
        try{System.in.read();}
        catch(Exception e){}
    }

    // returns either singular or plural form of goal
    private static String goalOrGoals(int count) {
        return count == 1 ? "goal" : "goals";
    }

    private static int getBetweenTwoNumbers(int numOne, int numTwo) {
        int player;

        String promptForInvalidInput = "\nPlease select number between " 
        + numOne + " and " + numTwo;

        System.out.println(promptForInvalidInput);
        while (true) {
            player = getNumericInput();
            if (player < numOne || player > numTwo)
                System.out.println(promptForInvalidInput);
            else
                break;
        }
        return player;       
    }

    private static int getNumericInput() {
        while (!scan.hasNextInt()) {
            System.out.println("Please enter a valid number...");
            scan.next();
        }
        return scan.nextInt();
    }

    private static int getPromptInput(User user) {
        int input;
        // loops until user enters correct input
        while (true) {
            input = getNumericInput();

            if (input < 1 || input > 5) {
                System.out.println("\nPlease enter a number (1-5)");
                askToContinue();
                introScreen(user);
            } else if ((input == 2 || input == 3) && playersNotEntered(user)) {
                System.out.println("\nMust enter players first...");
                askToContinue();
                introScreen(user);
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
        scan.nextLine();
        String name = scan.nextLine();
        // goes back to list of players if user just hits enter
        if (name.isEmpty()) {
            showPlayers(user);
        }
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
            // clearing scanner
            scan.nextLine();
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
        int input = getBetweenTwoNumbers(1, inputToClearAllPlayers);
        if (input == inputToClearAllPlayers) {
            if (wantsToClearPlayers())
                user.createPlayers();
        } else
            updatePlayers(user, input);
    }

    private static void inputPlayers(User user) {

        System.out.println("\nEnter the names of " + user.getNumberOfPlayers() + " players");
        System.out.println("Press enter without a name to exit...");
        // resetting the scanner so program doesn't pick up an empty input
        scan.nextLine();
        for (int i = 0; i < user.getNumberOfPlayers(); i++) {
            System.out.println("\nPlease enter the name of player " + (i+1) + 
            " (max is " + user.getNumberOfPlayers() + ")");

            String name = scan.nextLine();
            while (i != 0 && nameAlreadyInputted(name, user)) {
                System.out.println("Name already entered");
                name = scan.nextLine();
            }
            if (name.isEmpty())
                break;
            else {
                user.getPlayer(i).setName(name);
                if (i == user.getNumberOfPlayers()-1)
                    System.out.println("\nMax players entered...");
            }
        }
    }
    
    private static void enterPlayers(User user) {
        if (playersNotEntered(user)) {
            inputPlayers(user);
            user.clearNullPlayers();
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

    private static int getMostGoalsScored(Player[] players) {
        int goalsOfTopScorer = 0;
        for (Player player: players) {
            if (player.getGoals() > goalsOfTopScorer)
                goalsOfTopScorer = player.getGoals();
        }
        return goalsOfTopScorer;
    }

    // method is necessary since there is a possibility of
    // multiple top scorers
    private static void printTopScorers(Player[] players) {
        int mostGoalsScored = getMostGoalsScored(players);
        for (int i = 0; i < players.length; i++) {
            if (players[i].getGoals() == mostGoalsScored) {
                System.out.println(players[i].getName() + " is a top goal scorer");
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
    // FUNCTIONS RELATED TO OPTION 3
    //
    
    private static void addGoals(int player, User user) {

        // subtracts one to get index of player
        int playerIndex = player - 1;

        System.out.printf("\n" + user.getPlayer(playerIndex).getName() + " has %,d " +
        goalOrGoals(user.getPlayer(playerIndex).getGoals()) + " how many do you want to add...\n", 
        user.getPlayer(playerIndex).getGoals());

        int wantsToAdd = getNumericInput();
        while (wantsToAdd < 1) {
            System.out.println("Enter a positive number please...");
            wantsToAdd = getNumericInput();
        }
        user.getPlayer(playerIndex).addGoals(wantsToAdd);
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
        int playerNumber = getBetweenTwoNumbers(1, user.getNumberOfPlayers());
        addGoals(playerNumber, user);
    }

    //
    // FUNCTIONS RELATING TO OPTION 4
    //

    public static void showPromptForSubscription() {
        System.out.println("\nAccess denied. Please contact a Sassy Soccer sales rep " +
        "for a VIP subscription\n");
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
                default: System.out.println("Invalid. Please re-enter.");
                    break;
            }
        }
    }

    private static void start() {
        User user = new User();
        user.setVip();
        user.createPlayers();
        introScreen(user);
    }

    public static void main(String args[]) {
        start();
    }
}