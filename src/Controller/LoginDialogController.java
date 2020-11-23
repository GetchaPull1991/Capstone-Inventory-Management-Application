package Controller;

import Database.UserDatabase;
import javafx.fxml.FXML;
import Model.InputValidator;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginDialogController implements Initializable {

    @FXML
    public TextField usernameField;
    @FXML
    public PasswordField passwordField;
    @FXML
    public Label errorLabel;
    @FXML
    public Button loginButton;
    @FXML
    public Button cancelButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setButtonEventHandlers();
    }

    private void setButtonEventHandlers(){

        //Set Login button event
        loginButton.setOnAction(e -> {
            try {
                login();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });

        //Set Cancel button event
        cancelButton.setOnAction(e -> {

            //Get the login dialog stage and close it
            Stage stage = (Stage) cancelButton.getScene().getWindow();
            stage.close();
        });


    }

    private void login() throws IOException {

        if (InputValidator.validateLoginForm(this)){

            //Get the current user
            MainBorderPaneController.currentUser = UserDatabase.getUser(usernameField.getText(), passwordField.getText());

            //Get the current stage
            Stage currentStage = (Stage) loginButton.getScene().getWindow();

            //Get the MainBorderPane
            Parent root = FXMLLoader.load(getClass().getResource("../View/MainBorderPane.fxml"));

            //Create the stage and scene
            Stage stage = new Stage();
            Scene scene = new Scene(root, 1200, 745);

            scene.getStylesheets().add(Main.stylesheetUrl.toExternalForm());

            //Set the scene
            stage.setScene(scene);
            stage.setTitle("Capstone Inventory Management");
            stage.setResizable(false);
            //Close the current stage and open the new stage
            currentStage.close();
            stage.show();


        }
    }
}
