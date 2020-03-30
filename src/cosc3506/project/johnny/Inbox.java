package cosc3506.project.johnny;

import cosc3506.project.Database;
import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.sql.*;

/**
 * @author Johnny Console
 * Class: Directory
 * Purpose: List user accounts in the system
 * Written: 16 Jan 2020
 */
public class Inbox extends Application {

    private Label[] headers = {
            new Label("From"),
            new Label("Subject"),
            new Label("Date Sent"),
            new Label("Status"),
            new Label("Read"),
            new Label("Delete")
    };
    @Override
    public void start(Stage ps) {
        GridPane outer = new GridPane(), inner = new GridPane();
        ScrollPane pane = new ScrollPane(inner);
        outer.setPadding(new Insets(20));
        outer.setVgap(10);
        outer.setHgap(10);
        inner.setPadding(new Insets(20));
        inner.setVgap(20);
        inner.setHgap(20);



        Button toMain = new Button("Return To Main Menu (ESC)"),
                refresh = new Button("Refresh Messages (F5)");
        outer.add(toMain, 0, 0 );
        toMain.setMaxWidth(Double.MAX_VALUE);
        outer.add(refresh, 1, 0);
        refresh.setMaxWidth(Double.MAX_VALUE);



        getMessages(inner);

        outer.add(pane, 0, 2,7,1);




        outer.setOnKeyPressed(k -> {
            switch(k.getCode()) {
                case ESCAPE:
                    ps.close();
                    new MainScreen().start(new Stage());
                    break;
                case F5:
                    getMessages(inner);
                    break;
            }
        });

        ps.setOnCloseRequest(e -> {
            ps.close();
            new MainScreen().start(new Stage());
        });

        toMain.setOnAction(e -> {
            ps.close();
            new MainScreen().start(new Stage());
        });

        refresh.setOnAction(e -> getMessages(inner));

        Scene scene = new Scene(outer,600,400);
        ps.setScene(scene);
        ps.setTitle("User Messages");
        ps.show();
        ps.requestFocus();
    }


    private void getMessages(GridPane gpane) {
        try {
            boolean hasNext = false;
            gpane.getChildren().remove(0, gpane.getChildren().size());



            Connection conn = Database.connect(Database.HOST, Database.USER, Database.PASS, Database.DB);

            String sql = "SELECT * FROM messages WHERE recipient=? ORDER BY sent;";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, MainScreen.user);
            stmt.execute();
            ResultSet set = stmt.getResultSet();

            int row = 1;

            while(set.next()) {
                hasNext = true;
                gpane.add(new Label(set.getString("sender")), 0, row);
                gpane.add(new Label(set.getString("subject")), 1, row);
                gpane.add(new Label(set.getString("sent")), 2, row);
                gpane.add(new Label(set.getString("status")), 3, row);

                IDButton delete = new IDButton(set.getInt("id"), "Delete"),
                        read = new IDButton(set.getInt("id"), "Read");

                gpane.add(read, 4, row);
                gpane.add(delete, 5, row++);

                read.setOnAction(e -> {
                    new MessageWindow(read.id).start(new Stage());
                    getMessages(gpane);
                });
                delete.setOnAction(e ->{
                 delete(delete.id);
                 getMessages(gpane);
                });



            }

            if(!hasNext) {
                Label l = new Label("No messages found");
                GridPane.setHalignment(l, HPos.CENTER);
                gpane.add(l, 0, 0 ,7, 1);
            }
            else {
                for (int i = 0; i < headers.length; i++) {
                    gpane.add(headers[i], i, 0);
                    GridPane.setHalignment(headers[i], HPos.CENTER);
                }
            }
        }
        catch(SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void delete(int id) {
        try {
            Connection conn = Database.connect(Database.HOST, Database.USER, Database.PASS, Database.DB);
            Statement stmt = conn.createStatement();
            stmt.execute("DELETE FROM messages WHERE id=" + id + ";");

        } catch(SQLException ex) {
            System.err.println("Server Error");
        }
    }

}
