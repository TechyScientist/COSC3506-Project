package cosc3506.project.johnny;

import javafx.application.Application;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import static javafx.scene.text.FontWeight.BOLD;
import static javafx.scene.text.FontPosture.ITALIC;

public class MainScreen extends Application {

    public static String user, name, type;

    public MainScreen(String user, String name, String type) {
        MainScreen.name = name;
        MainScreen.user = user;
        MainScreen.type = type;
    }

    public MainScreen() {

    }

    @Override
    public void start(Stage ps) {
        int row = 1;
        GridPane pane = new GridPane();
        pane.setPadding(new Insets(20));
        pane.setVgap(10);
        pane.setHgap(10);

        Label title = new Label("Welcome,  " + (!name.contains(" ")? name : name.substring(0, name.indexOf(" "))) + "!");
        title.setFont(Font.font(title.getFont().getFamily(), 16));
        pane.add(title, 0, 0, 3, 1);

        Button directory = new Button("Directory");
        directory.setFont(Font.font(directory.getFont().getFamily(), 12));
        directory.setMaxWidth(Double.MAX_VALUE);
        pane.add(directory, 1, row);

        Button inbox = new Button("Inbox");
        inbox.setFont(Font.font(inbox.getFont().getFamily(), 12));
        inbox.setMaxWidth(Double.MAX_VALUE);
        pane.add(inbox, 0, row);

        Button logout = new Button("Log Out");
        logout.setFont(Font.font(logout.getFont().getFamily(), 12));
        logout.setMaxWidth(Double.MAX_VALUE);
        pane.add(logout, 0, ++row, 3, 1);

        directory.setOnAction(e -> {
            ps.close();
            //TODO: new Directory().start(new Stage());
        });

        logout.setOnAction(e -> {
            user = name = type = null;
            ps.close();
            new LoginScreen().start(new Stage());
        });


        pane.setOnKeyPressed(k -> {
            if (k.getCode() == KeyCode.ESCAPE) {
                name = user = type = null;
                ps.close();
                new LoginScreen().start(new Stage());

            }
        });

        Scene scene = new Scene(pane);
        ps.setScene(scene);
        ps.setTitle("Session: " + user);
        ps.show();
        pane.requestFocus();

    }
}
