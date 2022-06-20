package Controller;

import Controller.Interactions.Input;
import Controller.Interactions.Output;
import Factories.EntityFactory;

public class Controller extends EntityFactory {
    protected static Input inputInstance;
    protected static Output outputInstance;
    public Controller() {
        Input.getInstance();
        Output.getInstance();
    }
}
