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
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;

/**
 * @author Johnny Console
 * Class: UserAdd
 * Purpose: Adds a user into the
 * Database
 * Written: 16 Jan 2020
 */
public class UserAdd extends Application {

    @Override
    public void start(Stage ps) {
        GridPane pane = new GridPane();
        pane.setPadding(new Insets(20));
        pane.setVgap(10);
        pane.setHgap(10);

        Label status = new Label("");
        pane.add(status, 0, 0, 2, 1);
        GridPane.setHalignment(status, HPos.CENTER);

        pane.add(new Label("Name of User:"), 0, 1);
        pane.add(new Label("Username:"), 0, 2);
        pane.add(new Label("Password:"), 0, 3);
        pane.add(new Label("Confirm Password:"),0,4);
        pane.add(new Label("User Type:"), 0, 5);
        TextField name = new TextField(), username = new TextField();
        PasswordField pass = new PasswordField(), conf = new PasswordField();
        String[] params = {"Standard", "Admin"};
        ComboBox<String> uType = new ComboBox<>(FXCollections.observableArrayList(params));
        uType.getSelectionModel().selectFirst();
        Button execAdd = new Button("Execute Action");
        execAdd.setMaxWidth(Double.MAX_VALUE);
        execAdd.setOnAction(e -> add(name, username, pass, conf, uType, status));

        pane.add(name, 1, 1);
        pane.add(username, 1, 2);
        pane.add(pass, 1, 3);
        pane.add(conf, 1, 4);
        pane.add(uType, 1, 5);
        pane.add(execAdd, 0, 6, 2, 1);

        pane.setOnKeyPressed(k -> {
            switch(k.getCode()) {
                case ESCAPE:
                    ps.close();
                    new MainScreen().start(new Stage());
                    break;
                case ENTER:
                    add(name, username, pass, conf, uType, status);
                    break;
            }
        });

        ps.setOnCloseRequest(e -> {
            ps.close();
            new MainScreen().start(new Stage());
        });

        Scene scene = new Scene(pane);
        ps.setScene(scene);
        ps.setTitle("Add a User");
        ps.show();
        ps.requestFocus();

    }

    /**
     * Carries out the adding operation
     * @param name The new user's name
     * @param username The new user's username
     * @param pass The new user's password
     * @param conf The confirmation password
     * @param type The new user's type
     * @param status The status label
     */
    private void add(TextField name, TextField username, PasswordField pass,
                     PasswordField conf, ComboBox<String> type, Label status) {
        try {
            String sql = "SELECT * FROM users WHERE username=\"" + username.getText().toLowerCase() + "\";";
            Connection conn = Database.connect(Database.HOST, Database.USER, Database.PASS, Database.DB);
            Statement stmt = conn.createStatement();
            ResultSet set = stmt.executeQuery(sql);
            if(set.next()) {
                status.setText("User \"" + username.getText().toLowerCase() + "\" exists");
            }
            else {
                if(!name.getText().equals("") && !username.getText().equals("") && !pass.getText().equals("") &&
                    !conf.getText().equals("")) {
                    if (pass.getText().equals(conf.getText())) {
                        sql = "INSERT INTO users (username, name, password, type, added_by, status) VALUES(?,?,?,?,?, \"Offline\");";
                        PreparedStatement pstmt = conn.prepareStatement(sql);
                        pstmt.setString(1, username.getText().toLowerCase());
                        pstmt.setString(2, name.getText());
                        pstmt.setString(3, BCrypt.hashpw(pass.getText(), BCrypt.gensalt()));
                        pstmt.setString(4, type.getSelectionModel().getSelectedItem().toLowerCase());
                        pstmt.setString(5, MainScreen.user);
                        pstmt.execute();
                        status.setText("User \"" + username.getText().toLowerCase() + "\" added");
                        name.setText("");
                        username.setText("");
                        pass.setText("");
                        conf.setText("");
                        type.getSelectionModel().selectFirst();

                    }
                }
                else {
                    status.setText("Empty Fields");
                }
            }
        }
        catch (SQLException ex) {
            status.setText("Server error");
        }
    }

}
