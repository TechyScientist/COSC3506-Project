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
import javafx.scene.control.PasswordField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;
import java.util.ArrayList;

public class AdminPasswordReset extends Application {

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
            status.setText("Could not reset password");
        }

        pane.add(new Label("User to Reset:"), 0, 1);
        ComboBox<String> user = new ComboBox<>(FXCollections.observableArrayList(params));

        pane.add(new Label("New Password:"), 0, 2);
        pane.add(new Label("Confirm Password:"), 0, 3);

        PasswordField pass = new PasswordField(), conf = new PasswordField();

        pane.add(pass, 1, 2);
        pane.add(conf, 1, 3);

        Button execDel = new Button("Execute Action");
        execDel.setMaxWidth(Double.MAX_VALUE);
        execDel.setOnAction(e ->reset(user, pass, conf, status));

        pane.add(user, 1, 1);
        pane.add(execDel, 0, 4, 2, 1);
        execDel.setMaxWidth(Double.MAX_VALUE);


        pane.setOnKeyPressed(k -> {
            switch(k.getCode()) {
                case ESCAPE:
                    ps.close();
                    new MainScreen().start(new Stage());
                    break;
                case ENTER:
                    reset(user, pass, conf, status);
                    break;
            }
        });

        ps.setOnCloseRequest(e -> {
            ps.close();
            new MainScreen().start(new Stage());
        });

        Scene scene = new Scene(pane);
        ps.setScene(scene);
        ps.setTitle("Reset a Password");
        ps.show();
        ps.requestFocus();

    }

    private void reset(ComboBox<String> user,PasswordField pass, PasswordField conf, Label status) {
        try {
            if (!user.getSelectionModel().isEmpty() && !pass.getText().equals("")
                    && !conf.getText().equals("")) {
                if(pass.getText().equals(conf.getText())) {
                    String sql = "UPDATE users SET password=? WHERE username=?;";
                    Connection conn = Database.connect(Database.HOST, Database.USER, Database.PASS, Database.DB);
                    PreparedStatement pstmt = conn.prepareStatement(sql);
                    pstmt.setString(1, BCrypt.hashpw(pass.getText(), BCrypt.gensalt()));
                    pstmt.setString(2, user.getSelectionModel().getSelectedItem());
                    pstmt.execute();
                    status.setText("Reset password for \"" + user.getSelectionModel().getSelectedItem() + "\".");
                    user.getSelectionModel().clearSelection();
                    pass.setText("");
                    conf.setText("");
                }
                else {
                    status.setText("Passwords Don't Match");
                }
            }
        } catch(SQLException ex) {
            status.setText("Unable to Reset Password");
        }
    }

}
