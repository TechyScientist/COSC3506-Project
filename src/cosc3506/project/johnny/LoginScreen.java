package cosc3506.project.johnny;

import cosc3506.project.Database;
import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;

public class LoginScreen extends Application {
    private Label status;

    //This class is the main entry point
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage ps) {
        //Set up the pane geometry
        GridPane pane = new GridPane();
        pane.setPadding(new Insets(20));
        pane.setVgap(10);
        pane.setHgap(10);

        //Set up title label
        Label title = new Label("Log In to the System");
        title.setFont(Font.font(16));
        pane.add(title, 0, 0, 2, 1);
        GridPane.setHalignment(title, HPos.CENTER);

        //Set up status label
        status = new Label();
        pane.add(status, 0, 1, 2, 1);
        GridPane.setHalignment(status, HPos.CENTER);

        //Create and add the username field
        pane.add(new Label("Enter UserID: "),0, 2);
        TextField user = new TextField();
        pane.add(user, 1, 2);

        //Create and add the password field
        pane.add(new Label("Enter Password: "),0,3);
        PasswordField pass = new PasswordField();
        pane.add(pass, 1, 3);

        //Add the buttons
        Button login = new Button("Log On");
        Button exit = new Button("Exit");
        login.setMaxWidth(Double.MAX_VALUE);
        exit.setMaxWidth(Double.MAX_VALUE);
        GridPane.setHalignment(login, HPos.CENTER);
        GridPane.setHalignment(exit, HPos.CENTER);
        pane.add(exit, 0, 5);
        pane.add(login, 1, 5);

        //Exit on a press of the exit button
        exit.setOnAction(e -> ps.close());

        //Log in button function
        login.setOnAction(e -> login(user, pass, ps));

        pane.setOnKeyPressed(k -> {
            switch(k.getCode()) {
                case ESCAPE:
                    ps.close();
                    break;
                case ENTER:
                    login(user, pass, ps);
                    break;
            }
        });

        //Set up the Scene and Stage
        ps.setScene(new Scene(pane));
        ps.setTitle("Please Log On");
        ps.show();
        ps.requestFocus();
    }


    /**
     * Logs a user on to the system if their
     * credentials are in the database
     * @param user The user's userID, user entered
     * @param pass The user's password, user entered
     * @param ps The stage of the main function
     */
    private void login(TextField user, TextField pass, Stage ps) {
        Connection conn = Database.connect(Database.HOST, Database.USER, Database.PASS, Database.DB);
        if(conn == null) {
            //If connection can't be made
            status.setText("Could not connect to database");
        }
        else {
            try {
                //Begin critical section, acquire lock
                Database.waitFor();

                //Run the query
                String sql = "SELECT * FROM users WHERE username=\"" + user.getText().toLowerCase() + "\";";
               Statement stmt = conn.createStatement();
                ResultSet set = stmt.executeQuery(sql);

                //Release the lock, done critical section
                Database.signal();

                //If the password matches the hash in the database:
                if(set.next()) {
                    if (BCrypt.checkpw(pass.getText(), set.getString("password"))) {
                        //Open the new scene
                        ps.close();
                        new MainScreen(user.getText(), set.getString("name"),
                                set.getString("type")).start(new Stage());
                    } else {
                        //Otherwise: Error message
                        status.setText("Invalid username or password");
                    }
                }
                else {
                    status.setText("No User Found");
                }
            }catch(SQLException ex) {
                //If the statement could not be executed
                ex.printStackTrace();
                status.setText("Database Error");
            }
        }
    }

}
