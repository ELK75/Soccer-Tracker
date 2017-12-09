
import cs401.sassy.tracker.data.*;
import java.util.ArrayList;

public class SassyUser extends User {

    private String name;
    
    public SassyUser(cs401.sassy.tracker.data.User sassyUser) {
        this.name = sassyUser.getName();
        addPlayers(sassyUser);
    }

    
    public String getName() {
        return name;
    }

    public void addPlayers(cs401.sassy.tracker.data.User sassyUser) {
        Sasser sassers[] = sassyUser.getSassers();
        for (int i = 0; i < sassers.length; i++) {
            Player player = new Player(sassers[i].getName());
            player.setGoals(sassers[i].getGoals());
            getPlayers().add(player);
        }
    }

    public static ArrayList<String> getUserNames(ArrayList<SassyUser> sassyUsers) {
        ArrayList<String> userNames = new ArrayList<String>();
        for (int i = 0; i < sassyUsers.size(); i++) {
            userNames.add(sassyUsers.get(i).getName());
        }
        
        return userNames;
    }

    public boolean equals(SassyUser sassyUser) {
        return (name.equals(sassyUser.getName()));
    }
}