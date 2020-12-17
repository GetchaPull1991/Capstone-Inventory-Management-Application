package Controller;

import Database.UserDatabase;
import Model.InputValidator;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

/** Class that handles Change Password Dialog functionality */
public class ChangePasswordDialogController implements Initializable {
    @FXML
    public PasswordField currentPasswordField;
    @FXML
    public PasswordField confirmNewPasswordField;
    @FXML
    public Button submitButton;
    @FXML
    public Button cancelButton;
    @FXML
    public PasswordField newPasswordField;
    @FXML
    public Label errorLabel;

    //Create information alert
    private final Alert infoAlert = new Alert(Alert.AlertType.INFORMATION);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setButtonEventHandlers();
    }

    /** Set button event handlers */
    private void setButtonEventHandlers(){

        //Set the "Change Password" button event
        submitButton.setOnAction(e -> changePassword());

        //Set the "Cancel" button event
        cancelButton.setOnAction(e -> closeDialog());
    }

    /** Change the Users password */
    private void changePassword(){

        //Validate input
        if (InputValidator.validateChangePasswordForm(this)){

            //Update the current User
            MainBorderPaneController.currentUser.setModifiedDate(LocalDate.now());
            MainBorderPaneController.currentUser.setModifiedBy(MainBorderPaneController.currentUser.getUsername());

            //Update the Users password in the database
            UserDatabase.updateUserPassword(MainBorderPaneController.currentUser, newPasswordField.getText());

            //Display a confirmation that the password was changed successfully
            infoAlert.setTitle("Password Change Successful");
            infoAlert.setHeaderText("Password Change Successful!");
            infoAlert.setContentText("You're password has been changed successfully.");

            //Close alert on "OK" clicked
            infoAlert.showAndWait().ifPresent(response -> {
                if (response.equals(ButtonType.OK)){
                    closeDialog();
                }
            });

        }
    }

    /** Close the Change Password Dialog*/
    private void closeDialog(){
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }
}
