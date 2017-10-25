
public class Player {
    
        private int goals = 0;
        private String name = null;
    
        public void addGoals(int goals) {
            this.goals += goals;
        }
    
        public void setGoals(int goals) {
            this.goals = goals;
        }
    
        public int getGoals() {
            return goals;
        }
    
        public void setName(String name) {
            this.name = name;
        }
    
        public String getName() {
            return name;
        }
    }