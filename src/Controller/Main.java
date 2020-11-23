package Controller;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;


public class Main extends Application {

    public static URL stylesheetUrl;
    //Clear part form on submit
    //Calculate manufacturing fee with all parts in product

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("../View/LoginDialog.fxml"));
        primaryStage.setTitle("Capstone Inventory Management Login");
        Scene mainUI = new Scene(root, 400, 225);

        stylesheetUrl = getClass().getResource("../Resources/Stylesheets/stylesheet.css");

        mainUI.getStylesheets().add(stylesheetUrl.toExternalForm());
        primaryStage.setScene(mainUI);
        primaryStage.setResizable(false);
        primaryStage.show();
    }



    public static void main(String[] args) {
        launch(args);
    }


}

/*
Create "About" dialog with link to https://icons8.com/ for use of icons
 */