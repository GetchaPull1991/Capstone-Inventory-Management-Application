package Model;

import Controller.*;
import Database.UserDatabase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** Class that handles input validation for GUI forms */
public class InputValidator {

    /**
     * Validate the input of the Customer form
     * @param controller the controller of the form to validate
     * @return the boolean result of the check
     */
    public static boolean validateCustomerForm(CustomersController controller){

        //Initialize check result to true
        boolean isValid = true;

        //If any field is blank, input is not valid
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

    /**
     * Validate the input of the Part form
     * @param controller the controller of the form to validate
     * @return the boolean result of the check
     */
    public static boolean validatePartForm(PartsController controller){

        //Store result of check
        boolean isValid = true;

        //Create price regex limiting price to number 0-9 of any length with 2 optional decimal places
        String priceRegex = "^[0-9]+(\\.[0-9]{1,2})?$";
        Pattern pricePattern = Pattern.compile(priceRegex);
        Matcher priceMatcher = pricePattern.matcher(controller.priceField.getText());

        //Create stock regex limiting stock to numbers 0-9 of any length
        String stockRegex = "\\d+";
        Pattern stockPattern = Pattern.compile(stockRegex);
        Matcher stockMatcher = stockPattern.matcher(controller.stockField.getText());

        //Check for blank fields
        if (controller.nameField.getText().equals("") ||
                controller.priceField.getText().equals("") ||
                controller.stockField.getText().equals("") ) {
            controller.errorLabel.setText("* All Fields Are Required");
            controller.errorLabel.setVisible(true);
            isValid = false;

        //Check if price is valid
        } else if (!priceMatcher.matches()){
            controller.errorLabel.setText("* Price field may only contain numbers 0-9 and 2 decimal places");
            controller.errorLabel.setVisible(true);
            isValid = false;

        //Check if stock is valid
        } else if (!stockMatcher.matches()){
            controller.errorLabel.setText("* Stock field may only contain numbers 0-9");
            controller.errorLabel.setVisible(true);
            isValid = false;

        //Hide error label if tests pass
        } else {
            controller.errorLabel.setVisible(false);
        }

        //Return result of check
        return isValid;
    }

    /**
     * Validate the input of the Product form
     * @param controller the controller of the form to validate
     * @return the boolean result of the check
     */
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

    /**
     * Validate the input of the Order form
     * @param controller the controller of the form to validate
     * @return the boolean result of the check
     */
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

    /**
     * Validate the input of the Login form
     * @param controller the controller of the form to validate
     * @return the boolean result of the check
     */
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

    /**
     * Validate the input of the User form
     * @param controller the controller of the form to validate
     * @return the boolean result of the check
     */
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

    /**
     * Validate the input of the change password form
     * @param controller the controller of the form to validate
     * @return the boolean result of the check
     */
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
        } else if (UserDatabase.getUser(MainBorderPaneController.currentUser.getUsername(), currentPassword) == null){
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

    /**
     * Validate the input of the Report form
     * @param controller the controller of the form to validate
     * @return the boolean result of the check
     */
    public static boolean validateReportForm(ReportsController controller){

        //Initialize the check result to true
        boolean isValid = true;

        //Check order product report input
        if (controller.orderProductRadioButton.isSelected() && controller.orderProductComboBox.getValue() == null){
            controller.errorLabel.setText("* Please select a Product");
            controller.errorLabel.setVisible(true);
            isValid = false;

        //Check order part report input
        } else if (controller.orderPartRadioButton.isSelected() && controller.orderPartComboBox.getValue() == null) {
            controller.errorLabel.setText("* Please select a Part");
            controller.errorLabel.setVisible(true);
            isValid = false;

        //Check order customer report input
        } else if (controller.orderCustomerRadioButton.isSelected() && controller.orderCustomerComboBox.getValue() == null){
            controller.errorLabel.setText("* Please select a Customer");
            controller.errorLabel.setVisible(true);
            isValid = false;

        //Hide error label
        } else {
            controller.errorLabel.setVisible(false);
        }

        //Return the result of the check
        return isValid;
    };

}
