package cosc3506.project.johnny;

import cosc3506.project.Database;
import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.sql.*;

/**
 * @author Johnny Console
 * Class: Directory
 * Purpose: List user accounts in the system
 * Written: 16 Jan 2020
 */
public class MessageWindow extends Application {
    private int id;

    MessageWindow(int id) {
        this.id = id;
    }

    private Label[] headers = {
            new Label("From:"),
            new Label("Date Sent:"),
            new Label("Subject:"),
            new Label("Message:"),
    };

    private Label[] text = {
            new Label(),
            new Label(),
            new Label(),
            new Label()
    };

    @Override
    public void start(Stage ps) {
        for (Label header : headers) {
            GridPane.setHalignment(header, HPos.RIGHT);
        }

        GridPane pane = new GridPane();
        pane.setPadding(new Insets(20));
        pane.setVgap(20);
        pane.setHgap(20);


        Button close = new Button("Close (ESC)");
        close.setMaxWidth(Double.MAX_VALUE);

        pane.addColumn(0, headers[0], headers[1], headers[2], headers[3], close);

        try {
            Connection conn = Database.connect(Database.HOST, Database.USER, Database.PASS, Database.DB);
            Statement stmt = conn.createStatement();
            ResultSet set = stmt.executeQuery("SELECT * FROM messages WHERE id=" + id + ";");
            if (set.next()) {
                text[0].setText(set.getString("sender"));
                text[1].setText(set.getString("sent"));
                text[2].setText(set.getString("subject"));
                text[3].setText(set.getString("message"));
                stmt.execute("UPDATE messages SET status=\"read\" WHERE id=" + id + ";");
            }
        } catch(SQLException ex) {
            System.err.println("Server Error");
        }

        pane.addColumn(1, text[0], text[1], text[2], text[3]);
        pane.setOnKeyPressed(k -> {
            if(k.getCode() == KeyCode.ESCAPE) {
                ps.close();
            }
        });

        ps.setOnCloseRequest(e -> ps.close());

        close.setOnAction(e -> ps.close());

        Scene scene = new Scene(pane);
        ps.setScene(scene);
        ps.setTitle("User Message");
        ps.show();
        ps.requestFocus();
    }

}
