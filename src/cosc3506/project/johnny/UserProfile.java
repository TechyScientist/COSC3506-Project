package cosc3506.project.johnny;

import cosc3506.project.Database;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.sql.*;

public class UserProfile extends Application {
    static String myStatus;

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
        save = new Button("Save Password");
        String[] items = {"Available", "In A Meeting", "On The Phone"};

        PasswordField pass = new PasswordField(), conf = new PasswordField();

        ComboBox<String> uStatus = new ComboBox<>(FXCollections.observableArrayList(items));
        uStatus.getSelectionModel().select(myStatus);
        uStatus.valueProperty().addListener(e -> {
            try {
                Connection conn = Database.connect(Database.HOST, Database.USER, Database.PASS, Database.DB);
                String sql = "UPDATE users SET status=? WHERE username=?;";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, uStatus.getSelectionModel().getSelectedItem());
                stmt.setString(2, MainScreen.user);
                stmt.execute();
                myStatus = uStatus.getSelectionModel().getSelectedItem();
            }
            catch (SQLException ex) {
               status.setText("Could Not Save Status");
            }
        });
        pane.addRow(1, new Label("Change Status:"), uStatus);
        pane.addRow(2, new Label("Change Password:"), pass);
        pane.addRow(3, new Label(), conf);
        pane.addRow(4, retMain, save);

        save.setOnAction(e -> {

        });

        retMain.setOnAction(e-> {
            ps.close();
            new MainScreen().start(new Stage());
        });

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
        ps.setTitle("Change User Profile");
        ps.show();
        pane.requestFocus();
    }


}
