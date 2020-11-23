package Model;

import Controller.*;
import Database.UserDatabase;

public class InputValidator {


    public static boolean validateCustomerForm(CustomersController controller){

        boolean isValid = true;

        if (controller.nameField.getText().equals("") ||
                controller.phoneField.getText().equals("") ||
                controller.streetAddressField.getText().equals("") ||
                controller.postalCodeField.getText().equals("") ||
                controller.countryField.getText().equals("") ||
                controller.divisionField.getText().equals("") ||
                controller.cityField.getText().equals("")){
            controller.errorLabel.setText("* All Fields Are Required");
            controller.errorLabel.setVisible(true);
            isValid = false;
        } else {
            controller.errorLabel.setVisible(false);
        }

        return isValid;
    }

    public static boolean validatePartForm(PartsController controller){

        boolean isValid = true;

        if (controller.nameField.getText().equals("") ||
                controller.priceField.getText().equals("") ||
                controller.stockField.getText().equals("") ){
            controller.errorLabel.setText("* All Fields Are Required");
            controller.errorLabel.setVisible(true);
            isValid = false;

        } else {
            controller.errorLabel.setVisible(false);
        }


        return isValid;
    }

    public static boolean validateProductForm(ProductsController controller){

        boolean isValid = true;

        if (controller.nameField.getText().equals("") || controller.productPartsTableView.getItems().size() == 0){
            controller.errorLabel.setText("* All Fields Are Required");
            controller.errorLabel.setVisible(true);
            isValid = false;
        } else {
            controller.errorLabel.setVisible(false);
        }


        return isValid;
    }

    public static boolean validateOrderForm(OrdersController controller){

        boolean isValid = true;

        if (controller.customerComboBox.getSelectionModel().getSelectedItem() == null ||
            controller.dueDatePicker.getValue() == null ||
            controller.orderProductsTableView.getItems() == null){
            controller.errorLabel.setText("* All Fields Are Required");
            controller.errorLabel.setVisible(true);
            isValid = false;
        } else {
            controller.errorLabel.setVisible(false);
        }

        return isValid;
    }

    public static boolean validateLoginForm(LoginDialogController controller){

        boolean isValid;

        String username = controller.usernameField.getText().trim();
        String password = controller.passwordField.getText().trim();

        if (username.equals("") || password.equals("")){
            controller.errorLabel.setText("* All Fields Are Required");
            controller.errorLabel.setVisible(true);
            isValid = false;
        } else if (UserDatabase.getUser(username, password) == null){
            controller.errorLabel.setText("* Username or Password is incorrect");
            controller.errorLabel.setVisible(true);
            isValid = false;
        } else {
            controller.errorLabel.setVisible(false);
            isValid = true;
        }

        return isValid;
    }

    public static boolean validateUserForm(UsersController controller){

        boolean isValid = true;

        if (controller.usernameField.getText().trim().equals("") || controller.passwordField.getText().trim().equals("") ||
            controller.confirmPasswordField.getText().trim().equals("") || controller.privilegesComboBox.getValue() == null){
            isValid = false;
            controller.errorLabel.setText("* All Fields Are Required");
            controller.errorLabel.setVisible(true);
        } else if (!controller.passwordField.getText().trim().equals(controller.confirmPasswordField.getText().trim())){
            isValid = false;
            controller.errorLabel.setText("* Password Fields Must Match");
            controller.errorLabel.setVisible(true);
        } else if (UserDatabase.usernameInUse(controller.usernameField.getText().trim())){
            isValid = false;
            controller.errorLabel.setText("* The username you have chosen is not available");
            controller.errorLabel.setVisible(true);
        } else {
            controller.errorLabel.setVisible(false);
        }

        return isValid;
    }

    public static boolean validateChangePasswordForm(ChangePasswordDialogController controller){

        boolean isValid = true;

        //Get the field values
        String currentPassword = controller.currentPasswordField.getText();
        String newPassword = controller.newPasswordField.getText();
        String confirmNewPassword = controller.confirmNewPasswordField.getText();

        //Check if any fields are blank
        if (currentPassword.trim().equals("") || newPassword.trim().equals("") || confirmNewPassword.trim().equals("")){
            controller.errorLabel.setText("* All Fields Are Required");
            controller.errorLabel.setVisible(true);
            isValid = false;

        //Check if the current password is correct
        } else if (!MainBorderPaneController.currentUser.getPassword().equals(currentPassword)){
            controller.errorLabel.setText("* Current Password Is Incorrect");
            controller.errorLabel.setVisible(true);
            isValid = false;

        //Check if the new password is the same as the confirmation new password
        } else if (!newPassword.equals(confirmNewPassword)){
            controller.errorLabel.setText("* New Password Fields Must Match");
            controller.errorLabel.setVisible(true);
            isValid = false;
        } else {

            //Hide error label if tests pass
            controller.errorLabel.setVisible(false);
        }

        //Return result
        return isValid;
    }
}
