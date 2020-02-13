package cosc3506.project.johnny;

import cosc3506.project.Database;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.sql.*;
import java.util.ArrayList;

public class UserMod extends Application {

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
            status.setText("Could not modify user");
        }

        pane.add(new Label("User to Modify:"), 0, 1);
        ComboBox<String> user = new ComboBox<>(FXCollections.observableArrayList(params));

        String[] params2 = {"Standard", "Admin"};

        pane.add(new Label("New User Type:"), 0, 2);
        ComboBox<String> newType = new ComboBox<>(FXCollections.observableArrayList(params2));

        Button execDel = new Button("Execute Action");
        execDel.setMaxWidth(Double.MAX_VALUE);
        execDel.setOnAction(e ->modify(user, newType, status));

        pane.add(user, 1, 1);
        pane.add(newType, 1, 2);
        pane.add(execDel, 0, 3, 2, 1);
        execDel.setMaxWidth(Double.MAX_VALUE);


        pane.setOnKeyPressed(k -> {
            switch(k.getCode()) {
                case ESCAPE:
                    ps.close();
                    new MainScreen().start(new Stage());
                    break;
                case ENTER:
                    modify(user, newType, status);
                    break;
            }
        });

        ps.setOnCloseRequest(e -> {
            ps.close();
            new MainScreen().start(new Stage());
        });

        Scene scene = new Scene(pane);
        ps.setScene(scene);
        ps.setTitle("Modify a User");
        ps.show();
        ps.requestFocus();

    }

    private void modify(ComboBox<String> user, ComboBox<String> newType, Label status) {
        try {
            if (!user.getSelectionModel().isEmpty() && !newType.getSelectionModel().isEmpty()) {
                String sql = "UPDATE users SET type=? WHERE username=?;";
                Connection conn = Database.connect(Database.HOST, Database.USER, Database.PASS, Database.DB);
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, newType.getSelectionModel().getSelectedItem().toLowerCase());
                pstmt.setString(2, user.getSelectionModel().getSelectedItem());
                pstmt.execute();
                status.setText("Modified user \"" + user.getSelectionModel().getSelectedItem() + "\".");
                user.getSelectionModel().clearSelection();
                newType.getSelectionModel().clearSelection();
            }
        } catch(SQLException ex) {
            status.setText("Unable to Modify User");
        }
    }

}
