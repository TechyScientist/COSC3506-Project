package cosc3506.project.johnny;

import cosc3506.project.Database;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.sql.*;
import java.util.ArrayList;

/**
 * @author Johnny Console
 * Class: UserDel
 * Purpose: Deletion of user profiles by
 * Administrators
 * Written: 16 Jan 2020
 */
public class UserDel extends Application {

    private Label status;

    @Override
    public void start(Stage ps) {
        GridPane pane = new GridPane();
        pane.setPadding(new Insets(20));
        pane.setVgap(10);
        pane.setHgap(10);

        status = new Label("");
        pane.add(status, 0, 0, 2, 1);
        GridPane.setHalignment(status, HPos.CENTER);
        ArrayList<String> params = new ArrayList<>();
        try {
            String sql = "SELECT username FROM users WHERE username <> \"system\" AND username <> \""
                    + MainScreen.user + "\" AND added_by = \"" + MainScreen.user + "\";";
            Connection conn = Database.connect(Database.HOST, Database.USER, Database.PASS, Database.DB);
            Statement stmt = conn.createStatement();
            ResultSet set = stmt.executeQuery(sql);
            while(set.next()) {
                params.add(set.getString("username"));
            }
        }
        catch(SQLException ex) {
            status.setText("Could not remove user");
        }

        pane.add(new Label("User to Delete:"), 0, 1);
        ComboBox<String> user = new ComboBox<>(FXCollections.observableArrayList(params));

        Button execDel = new Button("Execute Action");
        execDel.setMaxWidth(Double.MAX_VALUE);
        execDel.setOnAction(e -> remove(user, status, ps));

        pane.add(user, 1, 1);
        pane.add(execDel, 0, 2, 2, 1);
        execDel.setMaxWidth(Double.MAX_VALUE);


        pane.setOnKeyPressed(k -> {
            switch(k.getCode()) {
                case ESCAPE:
                    ps.close();
                    new MainScreen().start(new Stage());
                    break;
                case ENTER:
                    remove(user, status, ps);
                    break;
            }
        });

        ps.setOnCloseRequest(e -> {
            ps.close();
            new MainScreen().start(new Stage());
        });

        Scene scene = new Scene(pane);
        ps.setScene(scene);
        ps.setTitle("Remove a User");
        ps.show();
        ps.requestFocus();

    }

    /**
     * Removes a user from the database
     * @param user The user selector
     * @param status The status label
     * @param ps The stage
     */
    private void remove(ComboBox<String> user, Label status, Stage ps) {
        try {
            if (!user.getSelectionModel().isEmpty()) {
                String sql = "DELETE FROM users WHERE username=?;";
                Connection conn = Database.connect(Database.HOST, Database.USER, Database.PASS, Database.DB);
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, user.getSelectionModel().getSelectedItem());
                pstmt.execute();
                ps.close();
                UserDel d = new UserDel();
                d.start(new Stage());
                d.status.setText("Deleted user \"" + user.getSelectionModel().getSelectedItem() + "\".");
            }
        } catch(SQLException ex) {
            status.setText("Unable to Delete User");
        }
    }

}
