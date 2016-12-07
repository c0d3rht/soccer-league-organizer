package com.hariidaran.io;

import com.hariidaran.model.Player;
import com.hariidaran.model.Team;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Players {

    // Import player data
    public static Object[] loadFrom(String fileName) {
        List<Team> teams = new ArrayList<>();
        List<Player> waitingList = new ArrayList<>();

        try (
                FileInputStream fis = new FileInputStream(fileName);
                BufferedReader reader = new BufferedReader(new InputStreamReader(fis))
        ) {

            Team team = null;

            String line;
            while (!(line = reader.readLine()).equals("Waiting List:") && (line != null || line.equals(""))) {
                if (line.equals("")) continue;

                if (line != null || !line.equals("")) {
                    String[] args = line.split(",");

                    if (args.length == 2) {
                        team = new Team(args[0], Assistant.capitalize(args[1].split(" managed by ")[1]));
                        teams.add(team);
                    } else {
                        String firstName = args[0].split("\\s")[0];
                        String lastName = args[0].split("\\s")[1];
                        float heightInInches = Float.parseFloat(args[1].split("\\s")[1]);
                        boolean previousExperience = args[2].equals(" has previous experience.");

                        Player player = new Player(firstName, lastName, heightInInches, previousExperience);
                        team.add(player);
                    }
                }
            }

            String other;
            while ((other = reader.readLine()) != null) {
                if (other.equals("")) continue;

                if (other != null || !other.equals("")) {
                    String[] args = other.split(",");

                    String firstName = Assistant.capitalize(args[0].split("\\s")[0]);
                    String lastName = Assistant.capitalize(args[0].split("\\s")[1]);
                    float heightInInches = Float.parseFloat(args[1].split("\\s")[1]);
                    boolean previousExperience = args[2].equals(" has previous experience.");

                    Player player = new Player(firstName, lastName, heightInInches, previousExperience);
                    waitingList.add(player);
                }
            }
        } catch (FileNotFoundException fnfe) {
            System.out.printf("%nFile not found... Creating \"%s\"%n", fileName);
        } catch (IOException ioe) {
            System.out.println("Something went wrong. Try again.");
        }


        return new Object[]{teams, waitingList};
    }

    // Save player data
    public static void saveTo(Assistant assistant, String fileName) {
        try (
                FileOutputStream fos = new FileOutputStream(fileName);
                PrintWriter writer = new PrintWriter(fos)
        ) {
            Object[] data = assistant.getData();
            List<Team> teams = (List<Team>) data[0];
            List<Player> waitingList = (List<Player>) data[1];

            for (Team team : teams) {
                writer.printf("%s, managed by %s%n%n", team.getName(), team.getCoachName());

                for (Player player : team.getPlayers()) {
                    String experience;

                    if (player.hasPreviousExperience()) {
                        experience = "has previous experience.";
                    } else {
                        experience = "does not have previous experience.";
                    }

                    if (player == team.getPlayers().get(team.getPlayers().size() - 1)) {
                        experience += String.format("%n");
                    }

                    writer.printf("%s %s, %f inches, %s%n",
                            player.getFirstName(),
                            player.getLastName(),
                            player.getHeightInInches(),
                            experience);
                }
            }

            writer.printf("Waiting List:%n%n");

            for (Player player : waitingList) {
                String experience;

                if (player.hasPreviousExperience()) {
                    experience = "has previous experience.";
                } else {
                    experience = "does not have previous experience.";
                }

                writer.printf("%s %s, %f inches, %s%n",
                        player.getFirstName(),
                        player.getLastName(),
                        player.getHeightInInches(),
                        experience);
            }
        } catch (IOException ioe) {
            System.out.println("Something went wrong. Try again.");
            ioe.printStackTrace();
        }

        System.out.printf("Data saved at '%s'.%n", fileName);
    }
}