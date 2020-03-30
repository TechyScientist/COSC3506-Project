package cosc3506.project.johnny;

import cosc3506.project.Database;
import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.*;
import java.sql.*;


public class BackupRestore extends Application {

    @Override
    public void start(Stage ps) {
        GridPane pane = new GridPane();
        pane.setPadding(new Insets(20));
        pane.setVgap(10);
        pane.setHgap(10);




        Button toMain = new Button("Return To Main Menu (ESC)"),
                backup = new Button("Perform Backup (B)"),
                restore = new Button("Restore From Backup File... (R)");
        pane.add(toMain, 0, 0 );
        toMain.setMaxWidth(Double.MAX_VALUE);
        pane.add(backup, 1, 0);
        backup.setMaxWidth(Double.MAX_VALUE);
        pane.add(restore, 2, 0);
        restore.setMaxWidth(Double.MAX_VALUE);

        pane.setOnKeyPressed(k -> {
            switch(k.getCode()) {
                case ESCAPE:
                    ps.close();
                    new MainScreen().start(new Stage());
                    break;
                case B:
                    backup();
                    backup.setText("Backup Completed");
                    break;
                case R:
                    restore();
                    restore.setText("Restore Completed");
                    break;
            }
        });

        ps.setOnCloseRequest(e -> {
            ps.close();
            new MainScreen().start(new Stage());
        });

        backup.setOnAction(e -> {
            backup();
            backup.setText("Backup Completed");
        });

        restore.setOnAction(e -> {
            restore();
            restore.setText("Restore Completed");
        });

        toMain.setOnAction(e -> {
            ps.close();
            new MainScreen().start(new Stage());
        });



        Scene scene = new Scene(pane);
        ps.setScene(scene);
        ps.setTitle("Message Backup/Restore");
        ps.show();
        ps.requestFocus();
    }

    private void backup() {
        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(new File("messages-" + MainScreen.user + ".bak")));
            Connection conn = Database.connect(Database.HOST, Database.USER, Database.PASS, Database.DB);

            String sql = "SELECT * FROM messages WHERE recipient=? ORDER BY sent;";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, MainScreen.user);
            stmt.execute();
            ResultSet set = stmt.getResultSet();

            while(set.next()) {
                Message msg = new Message(set.getInt(1), MainScreen.user, set.getString(3),
                        set.getString(4), set.getString(5), set.getString(6),
                        set.getString(7));
                out.writeObject(msg);
            }
            out.writeObject(null);
            out.close();
        } catch(SQLException ex) {
            System.err.println("Server Error");
        }
        catch(IOException ex) {
            System.err.println("File IO Error");
        }
    }
    private void restore() {
        try {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(new File("messages-" + MainScreen.user + ".bak")));
            Connection conn = Database.connect(Database.HOST, Database.USER, Database.PASS, Database.DB);

            String sql = "DELETE FROM messages WHERE recipient=?;";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, MainScreen.user);
            stmt.execute();
            sql = "INSERT INTO messages (id, recipient, sender, subject, message, sent, status) VALUES (?,?,?,?,?,?,?);";
            stmt = conn.prepareStatement(sql);
            Message msg;

            while((msg = (Message)(in.readObject())) != null) {
                stmt.setInt(1, msg.id);
                stmt.setString(2, msg.recipient);
                stmt.setString(3,  msg.sender);
                stmt.setString(4, msg.subject);
                stmt.setString(5, msg.message);
                stmt.setString(6, msg.sent);
                stmt.setString(7, msg.status);
                stmt.execute();
            }
            in.close();
        } catch(SQLException ex) {
            System.err.println("Server Error");
        }
        catch(IOException | ClassNotFoundException ex) {
            System.err.println("File IO Error");
        }
    }
}
