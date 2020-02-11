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

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class Directory extends Application {

    private Label[] headers = {
            new Label("User ID"),
            new Label("Username"),
            new Label("Name"),
            new Label("User Type")
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

        outer.add(new Label("Search:"), 0, 0);
        TextField search = new TextField();
        outer.add(search, 1, 0);

       String[] params = {"Username", "Name"};

        outer.add(new Label("On:"), 2, 0);
        ComboBox<String> searchOn = new ComboBox<>(FXCollections.observableArrayList(params));
        searchOn.getSelectionModel().selectFirst();
        outer.add(searchOn, 3, 0);


        Button execSearch = new Button("Search");
        outer.add(execSearch, 4, 0);

        outer.add(pane, 0, 1,
                (!MainScreen.type.equalsIgnoreCase("standard") ? 4 : 3), 1);

        execSearch.setOnAction(e -> search(search, inner, searchOn.getSelectionModel().getSelectedItem()));


        outer.setOnKeyPressed(k -> {
            switch(k.getCode()) {
                case ESCAPE:
                    ps.close();
                    new MainScreen().start(new Stage());
                    break;
                case ENTER:
                    search(search, inner, searchOn.getSelectionModel().getSelectedItem());
                    break;
            }
        });

        ps.setOnCloseRequest(e -> {
            ps.close();
            new MainScreen().start(new Stage());
        });

        Scene scene = new Scene(outer, 550, 400);
        ps.setScene(scene);
        ps.setTitle("User Directory");
        ps.show();
        ps.requestFocus();
    }

    private void search(TextField term, GridPane gpane, String on) {
        try {
            boolean hasNext = false;
            gpane.getChildren().remove(0, gpane.getChildren().size());

            for (int i = 0; i < headers.length - 1; i++) {
                gpane.add(headers[i], i, 0);
            }

            if(!MainScreen.type.equalsIgnoreCase("standard")){
                gpane.add(headers[headers.length - 1], headers.length - 1, 0);
            }

            Connection conn = Database.connect(Database.HOST, Database.USER, Database.PASS, Database.DB);

            Database.waitFor();
            String sql = "SELECT * FROM users" +
                    (term.getText() == null ? ";" : " WHERE " + on.toLowerCase() + " LIKE \"%" + term.getText() + "%\";");
            Statement stmt = conn.createStatement();
            ResultSet set = stmt.executeQuery(sql);
            Database.signal();

            int row = 0;

            while(set.next()) {
                hasNext = true;
                gpane.add(new Label(set.getInt("id") + ""), 0, ++row);
                gpane.add(new Label(set.getString("username")), 1, row);
                gpane.add(new Label(set.getString("name")), 2, row);
                if(!MainScreen.type.equalsIgnoreCase("standard")) {
                   gpane.add(new Label(set.getString("type")), 3, row);
                }


            }

            if(!hasNext) {
                Label l = new Label("No users found matching \"" + term.getText() + "\"");
                GridPane.setHalignment(l, HPos.CENTER);
                gpane.add(l, 0, 1,
                        (!MainScreen.type.equalsIgnoreCase("standard") ? 4 : 3), 1);
            }
        }
        catch(SQLException ex) {
            ex.printStackTrace();
        }
    }


}
