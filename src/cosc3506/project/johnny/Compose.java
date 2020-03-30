package cosc3506.project.johnny;

import cosc3506.project.Database;
import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.sql.*;

public class Compose extends Application {

    @Override
    public void start(Stage ps) {
        GridPane pane = new GridPane();
        pane.setPadding(new Insets(20));
        pane.setVgap(10);
        pane.setHgap(10);

        Label status = new Label("");
        pane.add(status, 0, 0, 2, 1);
        GridPane.setHalignment(status, HPos.CENTER);
        Button retMain = new Button("Back (ESC)"),
                send = new Button("Send Message");

        TextField to = new TextField(), subject = new TextField();

        TextArea message = new TextArea();
        message.setMaxWidth(300);
        message.setWrapText(true);

        retMain.setMaxWidth(Double.MAX_VALUE);
        retMain.setOnAction(e -> {
            ps.close();
            new MainScreen().start(new Stage());
        });

        send.setOnAction(e -> send(to, subject, message, status));

        send.setMaxWidth(Double.MAX_VALUE);

        Label t = new Label("To:"),
                subj = new Label("Subject: "),
                msg = new Label("Message:");
        GridPane.setHalignment(t, HPos.RIGHT);
        GridPane.setHalignment(subj, HPos.RIGHT);
        GridPane.setHalignment(msg, HPos.RIGHT);

        pane.addRow(1, t, to);
        pane.addRow(2, subj, subject);
        pane.addRow(3, msg, message);
        pane.addRow(4, retMain, send);

        pane.setOnKeyPressed(k -> {
            if(k.getCode() == KeyCode.ESCAPE) {
                ps.close();
                new MainScreen().start(new Stage());
            }
        });

        ps.setOnCloseRequest(e -> {
            ps.close();
            new MainScreen().start(new Stage());
        });

        ps.setScene(new Scene(pane));
        ps.setTitle("Send a Message");
        ps.show();
        pane.requestFocus();
    }

    private void send(TextField to, TextField subject, TextArea message, Label status) {
        if(to.getText().equals("") || subject.getText().equals("") | message.getText().equals("")) {
            status.setText("Please fill in all fields");
        }
        else {
            try {
                Connection conn = Database.connect(Database.HOST, Database.USER, Database.PASS, Database.DB);
                String sql = "SELECT * FROM users WHERE username=?;";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, to.getText());
                stmt.execute();
                ResultSet set = stmt.getResultSet();
                if (set.next()) {
                    status.setText("Sending...");
                    sql = "INSERT INTO messages (recipient, sender, subject, message, sent, status) VALUES(?,?,?,?,CURRENT_DATE,?);";
                    stmt = conn.prepareStatement(sql);
                    stmt.setString(1, to.getText());
                    stmt.setString(2, MainScreen.user);
                    stmt.setString(3, subject.getText());
                    stmt.setString(4, message.getText());
                    stmt.setString(5, "unread");
                    stmt.execute();
                    status.setText("Message sent");
                    to.setText("");
                    subject.setText("");
                    message.setText("");
                } else {
                    status.setText("User \"" + to.getText() + "\" not found");
                }
            } catch(SQLException ex) {
                ex.printStackTrace();
                status.setText("Server Error");
            }

        }

    }

}
