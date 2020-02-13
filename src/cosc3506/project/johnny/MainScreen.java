package cosc3506.project.johnny;

import javafx.application.Application;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class MainScreen extends Application {

    public static String user, name, type;


    public MainScreen(String user, String name, String type) {
        MainScreen.name = name;
        MainScreen.user = user;
        MainScreen.type = type;
    }

    public MainScreen(){}

    @Override
    public void start(Stage ps) {
        int row = 1;
        GridPane pane = new GridPane();
        pane.setPadding(new Insets(20));
        pane.setVgap(10);
        pane.setHgap(10);

        Label title = new Label("Welcome,  " + (!name.contains(" ")? name : name.substring(0, name.indexOf(" "))) + "!");
        GridPane.setHalignment(title, HPos.CENTER);
        title.setFont(Font.font(title.getFont().getFamily(), 16));
        pane.add(title, 0, 0, 3, 1);

        Button inbox = new Button("Inbox (I)");
        inbox.setFont(Font.font(inbox.getFont().getFamily(), 12));
        inbox.setMaxWidth(Double.MAX_VALUE);
        pane.add(inbox, 0, row);

        Button directory = new Button("Directory (D)");
        directory.setFont(Font.font(directory.getFont().getFamily(), 12));
        directory.setMaxWidth(Double.MAX_VALUE);
        pane.add(directory, 1, row);

        Button profile = new Button("Your Profile (P)");
        profile.setFont(Font.font(profile.getFont().getFamily(), 12));
        profile.setMaxWidth(Double.MAX_VALUE);
        pane.add(profile, 2, row);

        Button logout = new Button("Log Out (Esc)");
        logout.setFont(Font.font(logout.getFont().getFamily(), 12));
        logout.setMaxWidth(Double.MAX_VALUE);
        pane.add(logout, 0, ++row, 3, 1);

        if(!MainScreen.type.equalsIgnoreCase("standard")) {
            Button addU = new Button("Add User (A)");
            Button delU = new Button("Delete User (R)");
            Button modU = new Button("Modify User (M)");
            Button chUPass = new Button("Reset Password (X)");

            addU.setFont(Font.font(addU.getFont().getFamily(), 12));
            addU.setMaxWidth(Double.MAX_VALUE);
            pane.add(addU, 0, ++row, 3, 1);
            addU.setOnAction(e -> {
                ps.close();
                new UserAdd().start(new Stage());
            });

            delU.setOnAction(e -> {
                ps.close();
                new UserDel().start(new Stage());
            });

            modU.setOnAction(e -> {
                ps.close();
                new UserMod().start(new Stage());
            });

            chUPass.setOnAction(e -> {
                ps.close();
                new AdminPasswordReset().start(new Stage());
            });
            modU.setFont(Font.font(modU.getFont().getFamily(), 12));
            modU.setMaxWidth(Double.MAX_VALUE);
            pane.add(modU, 0, ++row, 3, 1);

            delU.setFont(Font.font(delU.getFont().getFamily(), 12));
            delU.setMaxWidth(Double.MAX_VALUE);
            pane.add(delU, 0, ++row, 3, 1);

            chUPass.setFont(Font.font(chUPass.getFont().getFamily(), 12));
            chUPass.setMaxWidth(Double.MAX_VALUE);
            pane.add(chUPass, 0, ++row, 3, 1);
        }

        directory.setOnAction(e -> {
            ps.close();
            new Directory().start(new Stage());
        });

        logout.setOnAction(e -> {
            user = name = type = null;
            ps.close();
            new LoginScreen().start(new Stage());
        });


        pane.setOnKeyPressed(k -> {
            switch(k.getCode()) {
                case ESCAPE:
                    name = user = type = null;
                    ps.close();
                    new LoginScreen().start(new Stage());
                    break;
                case I:
                    break;
                case D:
                    ps.close();
                    new Directory().start(new Stage());
                    break;
                case P:
                    break;
                case A:
                    if(!MainScreen.type.equalsIgnoreCase("standard")) {
                        ps.close();
                        new UserAdd().start(new Stage());
                    }
                    break;
                case R:
                    if(!MainScreen.type.equalsIgnoreCase("standard")) {
                        ps.close();
                        new UserDel().start(new Stage());
                    }
                    break;
                case M:
                    if(!MainScreen.type.equalsIgnoreCase("standard")) {
                        ps.close();
                        new UserMod().start(new Stage());
                    }
                case X:
                    if(!MainScreen.type.equalsIgnoreCase("standard")) {
                        ps.close();
                        new AdminPasswordReset().start(new Stage());
                    }
                    break;
            }
        });

        ps.setOnCloseRequest(e -> {
            name = user = type = null;
            ps.close();
            new LoginScreen().start(new Stage());
        });

        Scene scene = new Scene(pane,300, (!MainScreen.type.equalsIgnoreCase("standard") ? 400 : 200));
        ps.setScene(scene);
        ps.setTitle("Session: " + user);
        ps.show();
        pane.requestFocus();

    }
}
