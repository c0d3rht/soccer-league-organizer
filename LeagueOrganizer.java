import com.hariidaran.io.Assistant;
import com.hariidaran.io.Players;
import com.hariidaran.model.Player;
import com.hariidaran.model.Team;

import java.util.List;

public class LeagueOrganizer {

    public static void main(String[] args) {

        // File from which data is to be imported from and exported to
        final String SAVE_FILE = "data.txt";

        // Import data from file
        Object[] data = Players.loadFrom(SAVE_FILE);

        // Data is distributed
        List<Team> teams = (List<Team>) data[0];
        List<Player> waitingList = (List<Player>) data[1];

        System.out.printf("%nWelcome to Soccer League Organizer v%s!%n", Assistant.VERSION_NUMBER);

        // Send data to assistant
        Assistant assistant = new Assistant(teams, waitingList);

        // Start prompting
        assistant.run();

        // Save data to file
        Players.saveTo(assistant, SAVE_FILE);

        // TODO: D.R.Y. promptData() and promptPlayer(), and try to merge them
    }
}