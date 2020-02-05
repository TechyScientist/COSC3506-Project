package cosc3506.project.johnny;

import javafx.application.Application;
import javafx.stage.Stage;

public class MainScreen extends Application {

    public static String user, name;

    public MainScreen(String user, String name) {
        MainScreen.name = name;
        MainScreen.user = user;
    }

    @Override
    public void start(Stage ps) {
        System.out.println("In The Main Screen");
    }
}
