package Components.Menu;

import Entities.Entity;

public interface MenuAction {
    public void run(Entity entity);
    public void run();
}