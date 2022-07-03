package Components.Menu;

import Entities.Entity;

public class Option implements MenuAction {
    String title, description;
    MenuAction action;
    public Option(String description, MenuAction action) {
        this(description);
        this.action = action;
    }

    public Option(String description) {
        this.description = description;
    }
    
    public void setAction(MenuAction action) {
        this.action = action;
    }

    @Override
    public void run(Entity entity) {
        if (action == null) {
            System.out.println("TODO: Something");
        } else {
            action.run(entity);
        }
    }

    @Override
    public void run() {
        if (action == null) {
            System.out.println("TODO: Something");
        } else {
            action.run();
        }
    }

    @Override
    public String toString() {
        return description;
    }
}
