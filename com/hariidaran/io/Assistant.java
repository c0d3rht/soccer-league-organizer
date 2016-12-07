package com.hariidaran.io;

import com.hariidaran.model.*;

import java.io.*;
import java.util.*;

public class Assistant {
    public static final String VERSION_NUMBER = "1.0";

    // Username
    private String mUserName = "";

    // List of teams
    private List<Team> mTeams;

    // Waiting list
    private List<Player> mWaitingList;

    // List of commands
    private Map<String, String> mCommands;

    // The thing which reads the user's input
    private BufferedReader mReader;

    // Action navigator
    private String actionTowards = "";

    public Assistant(List<Team> teams, List<Player> waitingList) {
        mTeams = teams;
        mWaitingList = waitingList;

        mCommands = new LinkedHashMap<>();
        mCommands.put("addteam     ", "Create a team");
        mCommands.put("addplayer   ", "Add a player");
        mCommands.put("removeplayer", "Remove a player from a team, and send him to the waiting list");
        mCommands.put("deleteplayer", "Delete a player from the database");
        mCommands.put("teamreport  ", "Prints the data of a chosen team (in a .txt file)");
        mCommands.put("leaguereport", "Prints a report of experienced and inexperienced teams (in a .txt file)");
        mCommands.put("showdata    ", "Shows the database");
        mCommands.put("exit/quit   ", "Exit the program");

        mReader = new BufferedReader(new InputStreamReader(System.in));
    }

    /* HELPER METHODS */

    // Capitalizes each word in a string
    public static String capitalize(String string) {
        if (!string.isEmpty()) {
            String[] words = string.split("\\s");
            String other = "";

            for (int i = 0; i < words.length; i++) {
                words[i] = words[i].substring(0, 1).toUpperCase() + words[i].substring(1, words[i].length()).toLowerCase() + " ";

                other += words[i];
            }

            return other.trim();
        } else {
            return null;
        }
    }

    // Displays commands and asks to enter option
    private String displayCommands() throws IOException {
        // Checks for input of username
        while (mUserName.isEmpty()) {
            System.out.printf("%nWhat is your name?  ");
            mUserName = mReader.readLine().trim();
        }

        // Prints title "COMMANDS"
        System.out.printf("%n%n===============================%n");
        System.out.println("COMMANDS");
        System.out.printf("===============================%n%n");

        // Prints out list of commands
        for (Map.Entry option : mCommands.entrySet()) {
            System.out.printf("%s  -  %s%n", option.getKey(), option.getValue());
        }

        // Ask for input of command
        System.out.printf("%nWhat do you want to do, %s?  ", mUserName);

        // In case the user types in between which won't make the program work again. Trust me, it sucks.
        String command = mReader.readLine().toLowerCase().trim();
        System.out.println("");

        return command;
    }

    // Checks if a string is alpha-only
    private boolean isAlpha(String string) {
        char[] chars = string.toCharArray();

        if (string.isEmpty()) return false;

        for (char c : chars) {
            if (!Character.isLetter(c)) return false;
        }

        return true;
    }

    private boolean isAlpha(String[] list) {
        for (String string : list) return isAlpha(string);

        return false;
    }

    // Checks if a variable is an int or float
    private boolean isNaN(String string, String type) {
        try {
            if (type.equals("int")) {
                int number = Integer.parseInt(string);
            } else {
                float number = Float.parseFloat(string);
            }
        } catch (NumberFormatException nfe) {
            return true;
        }

        return false;
    }

    // Return data
    public Object[] getData() {
        return new Object[] {mTeams, mWaitingList};
    }

    // Returns all the players in th database
    private List<Player> getPlayers() {
        List<Player> players = new ArrayList<>();


        for (Team team : mTeams) {
            players.addAll(team.getPlayers());
        }

        players.addAll(mWaitingList);
        return players;
    }

    // Starts to do whatever required based on what the user has typed
    public void run() {
        String command = "";

        do {
            try {
                command = displayCommands();

                switch (command) {
                    // If "addteam" is typed
                    case "addteam":
                    case "add team":
                        addTeam();
                        break;

                    // If "addplayer" is typed
                    case "addplayer":
                    case "add player":
                        addPlayer();
                        break;

                    // If "removeplayer" is typed
                    case "removeplayer":
                    case "remove player":
                        removePlayer();
                        break;

                    // If "deleteplayer" is typed
                    case "deleteplayer":
                    case "delete player":
                        deletePlayer();
                        break;

                    // If "teamreport" is typed
                    case "teamreport":
                    case "team report":
                        teamReport();
                        break;

                    // If "leaguereport" is typed
                    case "leaguereport":
                    case "league report":
                        leagueReport();
                        break;

                    // If "showdata" is typed
                    case "showdata":
                    case "show data":
                        showData();
                        break;

                    // If "exit"/"quit" is typed
                    case "exit":
                    case "quit":
                        System.out.printf("Exiting...%n");
                        break;

                    // If input entered is not available in list of commands
                    default:
                        System.out.printf("\"%s\" is not present in the list of commands. Try again.", command);
                        break;
                }
            } catch (IOException ioe) {
                System.out.println("Something went wrong. Try again.");
                ioe.printStackTrace();
            }
        } while (!(command.equals("exit") || command.equals("quit")));
    }

    // Prompt for team
    private List<Player> promptTeam(String func) {
        if (!(mTeams == null)) {
            String string = "";
            int input = 0;

            for (int i = 0; i < mTeams.size(); i++) {
                System.out.printf("%d. %s%n", i + 1, mTeams.get(i));
            }

            if (func.equals("add") ||
                func.equals("show") ||
                func.equals("delete")) {
                System.out.printf("%d. Waiting list%n", mTeams.size() + 1);
            }

            try {
                while (isNaN(string, "int") || input > mTeams.size() || input < 0) {
                    System.out.printf("%nEnter the number along the desired team:  ");
                    string = mReader.readLine().trim();

                    if (!isNaN(string, "int")) {
                        input = Integer.parseInt(string) - 1;
                    }
                }
            } catch (IOException ioe) {
                System.out.println("%n%nSomething went wrong. Try again.");
            }

            if (input == mTeams.size()) {
                actionTowards = "Waiting List";
                return mWaitingList;
            } else {
                actionTowards = mTeams.get(input).getName();
                return mTeams.get(input).getPlayers();
            }
        } else {
            System.out.println("%nERROR: No teams present in the database.");
            return null;
        }
    }

    // Prompt for player
    private Player promptPlayer(List<Player> players) {
        String string = "";
        int input = 0;

        for (int i = 0; i < players.size(); i++) {
            System.out.printf("%d. %s%n", i + 1, players.get(i));
        }

        try {
            while (isNaN(string, "int") || input > players.size() || input < 0) {
                System.out.printf("%nEnter the number along the desired player:  ");
                string = mReader.readLine().trim();

                if (!isNaN(string, "int")) {
                    input = Integer.parseInt(string) - 1;
                }
            }
        } catch (IOException ioe) {
            System.out.println("Something went wrong. Try again.");
        }

        return players.get(input);
    }

    private Object promptData(String type, String func) {
        List<Player> players = promptTeam(func);

        if (!(players == null || players.size() == 0)) {
            if (type.equals("team")) {
                for (Team team : mTeams) {
                    if (team.getPlayers().containsAll(players)) {
                        actionTowards = team.getName();
                        return team;
                    }
                }

                actionTowards = "Waiting List";
                return mWaitingList;
            } else {
                return promptPlayer(players);
            }
        } else {
            System.out.printf("%nERROR: No players present in this team.");
            return null;
        }
    }

    /* MAIN METHODS */

    // Adds a team
    private void addTeam() throws IOException {
        // Asks for team name
        String teamName = "";
        while (!isAlpha(teamName.split("\\s"))) {
            System.out.printf("Enter the team's name:  ");
            teamName = mReader.readLine().trim();
        }

        // Asks for coach's name
        String coachName = "";
        while (!isAlpha(coachName.split("\\s"))) {
            System.out.printf("Enter the coach's name:  ");
            coachName = mReader.readLine().trim();
        }

        // Create new team with details
        Team team = new Team(teamName, coachName);

        // Team is added
        if (mTeams.contains(team)) {
            System.out.printf("%nERROR: %s already exists in the database.", team.getName());
        } else {
            mTeams.add(team);
            System.out.printf("%n%s is added!", team);
        }
    }

    // Adds a player
    private void addPlayer() throws IOException {
        // Asks for first name of player
        String firstName = "";
        while (!isAlpha(firstName)) {
            System.out.printf("Enter his first name:  ");
            firstName = mReader.readLine().trim();
        }

        // Asks for last name of player
        String lastName = "";
        while (!isAlpha(lastName)) {
            System.out.printf("Enter his last name:  ");
            lastName = mReader.readLine().trim();
        }

        // Asks for height (in inches) of player
        String heightInInchesString = "";
        while (isAlpha(heightInInchesString) || isNaN(heightInInchesString, "float")) {
            System.out.printf("Enter his height (in inches):  ");
            heightInInchesString = mReader.readLine().trim();
        }

        float heightInInches = Float.parseFloat(heightInInchesString);

        // Checks for previous experience
        String previousExperienceString = "";
        while (!(previousExperienceString.equals("yes") || previousExperienceString.equals("no"))) {
            System.out.printf("Does he have previous experience? (enter yes or no)  ");
            previousExperienceString = mReader.readLine().trim().toLowerCase();
        }

        Player player = new Player(firstName, lastName, heightInInches, previousExperienceString.equals("yes"));

        if (getPlayers().contains(player)) {
            System.out.printf("%nERROR: %s already exists in the database.%n", player);
        } else {
            System.out.printf("Where do you want to send %s to?%n%n", player);
            Object data = promptData("team", "add");

            if (!actionTowards.equals("Waiting List")) {
                Team team = (Team) data;

                if (team.size() < Team.MAX_PLAYERS) {
                    team.add(player);
                    System.out.printf("%n%s, %s is added!", player.getLastName(), player.getFirstName());
                } else {
                    System.out.printf("%nERROR: Cannot exceed limit of %d players.%n", Team.MAX_PLAYERS);
                }
            } else {
                mWaitingList.add(player);
                System.out.printf("%n%s, %s is added!", player.getLastName(), player.getFirstName());
            }
        }
    }

    // Remove player from team and send to waiting list
    private void removePlayer() throws IOException {
        if (!mTeams.isEmpty()) System.out.printf("Which player do you want to send to the waiting list?%n%n");

        Player player = (Player) promptData("team", "remove");

        if (player != null) {
            // Remove the player according to the "actionTowards" field
            Team team = null;

            for (Team other : mTeams) {
                if (other.getName().equals(actionTowards)) team = other;
            }

            team.remove(player);
            mWaitingList.add(player);

            System.out.printf("%n%s is sent to the waiting list.", player);
        }
    }

    // Delete player from database
    private void deletePlayer() throws IOException {
        if (!mTeams.isEmpty()) System.out.printf("Which player do you want to delete from the database?%n%n");

        Player player = (Player) promptData("player", "delete");

        if (player != null) {
            // Delete the player according to the "actionTowards" field
            if (!actionTowards.equals("Waiting List")) {
                for (Team team : mTeams) {
                    if (team.getName().equals(actionTowards)) team.remove(player);
                }
            } else {
                mWaitingList.remove(player);
            }

            System.out.printf("%n%s is deleted from the database.", player);
        }
    }

    // Show players from a specific group
    private void teamReport() throws IOException {
        if (!mTeams.isEmpty()) System.out.printf("Which team's data do you want to print?%n%n");

        Object data = promptData("team", "show");
        Team team;

        if (data instanceof Team) {
            team = (Team) data;
        } else {
            team = new Team(null, null);

            for (Player player : (List<Player>) data) {
                team.add(player);
            }
        }

        Collections.sort(team.getPlayers(), (player, other) -> Float.compare(player.getHeightInInches(), other.getHeightInInches()));

        String fileName = actionTowards + ".txt";

        try (
                FileOutputStream fos = new FileOutputStream(fileName);
                PrintWriter writer = new PrintWriter(fos)
        ) {
            if (!actionTowards.equals("Waiting List")) {
                writer.printf("%s%n%nAVERAGE EXPERIENCE LEVEL: %d%%%n===============================%n",
                        team, team.getExperienceLevel());
            } else {
                writer.printf("Waiting List:%n===============================%n");
            }

            for (int i = 0; i < team.size(); i++) {
                Player player = team.get(i);
                writer.printf("%n%d. %s", i + 1, player);
            }
        } catch (IOException ioe) {
            System.out.println("Something went wrong. Try again.");
            ioe.printStackTrace();
        }

        System.out.printf("Data saved at '%s'.", fileName);
    }

    // Show players from a specific group
    private void leagueReport() throws IOException {
        String fileName = "League Balance Report.txt";

        try (
                FileOutputStream fos = new FileOutputStream(fileName);
                PrintWriter writer = new PrintWriter(fos)
        ) {
            writer.printf("LEAGUE BALANCE REPORT%n===============================%n");

            writer.printf("%nEXPERIENCED TEAMS:");

            int i = 0;
            for (Team team : mTeams) {
                if (team.isExperienced()) {
                    i++;
                    writer.printf("%n%n%d. %s:", i, team);

                    Map<Double, Integer> heightMap = new TreeMap<>();

                    int otherIndex = 0;
                    for (Player player : team.getPlayers()) {
                        otherIndex++;
                        double height = Math.round((player.getHeightInInches() + 5) / 10.0) * 10.0;

                        if (!heightMap.containsKey(height)) {
                            otherIndex = 0;
                            otherIndex++;
                            heightMap.put(height, null);
                        }

                        heightMap.put(height, otherIndex);
                    }

                    for (Double number : heightMap.keySet()) {
                        if (number != null) {
                            String word = "player";
                            if (heightMap.get(number) != 1) word += "s";

                            writer.printf("%n(%.0f to %.0f inches) -> %d %s", number, number + 10, heightMap.get(number), word);
                        }
                    }
                }
            }

            writer.printf("%n%nINEXPERIENCED TEAMS:%n");

            i = 0;
            for (Team team : mTeams) {
                if (!team.isExperienced()) {
                    i++;
                    writer.printf("%n%d. %s", i, team);
                } else {
                    writer.printf("%nThere are no inexperienced teams.");
                }
            }
        } catch (IOException ioe) {
            System.out.println("Something went wrong. Try again.");
            ioe.printStackTrace();
        }

        System.out.printf("Data saved at '%s'.", fileName);
    }

    // Show players from a specific group
    private void showData() throws IOException {
        Player player = (Player) promptData("player", "show");

        // Delete the player according to the "actionTowards" field
        if (player != null) {
            if (actionTowards.equals("Waiting List")) {
                String input = "";
                while (!(input.equals("add") || input.equals("details"))) {
                    System.out.printf("%nDo you want to add him to a team, or just see his details (enter add or details):  ");
                    input = mReader.readLine();
                }

                if (input.equals("add")) {
                    Team team = (Team) promptData("team", null);

                    mWaitingList.remove(player);
                    team.add(player);

                    System.out.printf("%n%s is added!", player);
                } else {
                    System.out.println(player.details());
                }
            } else {
                System.out.println(player.details());
            }
        }
    }
}