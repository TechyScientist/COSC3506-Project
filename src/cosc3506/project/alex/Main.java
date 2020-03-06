package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.show();
        primaryStage.setTitle("Inbox");
        Scene scene1,scene2;
        String title = "select subject from messages where user=userName";
        String userName = "select sender from  messages ";
        String message="select message from messages";
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDate localDate = LocalDate.now();

        Button b1 = new Button(userName+"        "+ title+"       "+dtf.format(localDate));
        VBox layout1 = new VBox();
        layout1.getChildren().addAll(b1);
        scene1=new Scene(layout1,250,300);
        primaryStage.setScene(scene1);

        Label l1 = new Label("Title: "+title);
        Label l2 = new Label("Sent by: "+userName);
        Label l3 = new Label(message);

        VBox layout2=new VBox(10);
        layout2.getChildren().addAll(l1,l2,l3);
        scene2=new Scene(layout2,400,300);
        b1.setOnAction(e->
                primaryStage.setScene(scene2)
        );

        Button b2= new Button("Go back");
        b2.setOnAction(e->
                primaryStage.setScene(scene1)
        );
        layout2.getChildren().addAll(b2);



    }


    public static void main(String[] args) {
        launch(args);
    }
}
