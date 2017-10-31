
import java.io.Serializable;

public class Player implements Serializable {
    
    private int goals = 0;
    private String name;
    
    public Player(String name) {
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addGoals(int goals) {
        this.goals += goals;
    }
    
    public void setGoals(int goals) {
        this.goals = goals;
    }
    
    public int getGoals() {
        return goals;
    }
    
    public String getName() {
        return name;
    }
}