package View;

import Database.UserDatabase;
import Model.InputValidator;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

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

    Alert infoAlert = new Alert(Alert.AlertType.INFORMATION);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setButtonEventHandlers();
    }

    private void setButtonEventHandlers(){

        submitButton.setOnAction(e -> changePassword());

        cancelButton.setOnAction(e -> closeDialog());
    }

    private void changePassword(){

        //Validate input
        if (InputValidator.validateChangePasswordForm(this)){
            MainBorderPaneController.currentUser.setPassword(newPasswordField.getText());
            MainBorderPaneController.currentUser.setModifiedDate(LocalDate.now());
            MainBorderPaneController.currentUser.setModifiedBy(MainBorderPaneController.currentUser.getUsername());
            UserDatabase.updateUserPassword(MainBorderPaneController.currentUser);

            infoAlert.setTitle("Password Change Successful");
            infoAlert.setHeaderText("Password Change Successful!");
            infoAlert.setContentText("You're password has been changed successfully.");
            infoAlert.showAndWait().ifPresent(response -> {
                if (response.equals(ButtonType.OK)){
                    closeDialog();
                }
            });

        }
    }

    private void closeDialog(){
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }
}
