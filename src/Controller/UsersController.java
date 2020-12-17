package Controller;

import Database.UserDatabase;
import Model.InputValidator;
import Model.User;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Region;

import java.net.URL;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Random;
import java.util.ResourceBundle;

public class UsersController implements Initializable {

    @FXML
    public TextField usernameField;
    @FXML
    public PasswordField passwordField;
    @FXML
    public PasswordField confirmPasswordField;
    @FXML
    public ComboBox<String> privilegesComboBox;
    @FXML
    public TableView<User> usersTable;
    @FXML
    public TableColumn<User, Integer> idColumn;
    @FXML
    public TableColumn<User, String> usernameColumn;
    @FXML
    public TableColumn<User, LocalDate> createdDateColumn;
    @FXML
    public TableColumn<User, String> createdByColumn;
    @FXML
    public TableColumn<User, LocalDate> modifiedDateColumn;
    @FXML
    public TableColumn<User, String> modifiedByColumn;
    @FXML
    public TableColumn<User, String> privilegesColumn;
    @FXML
    public Label errorLabel;
    @FXML
    public Button modifyButton;
    @FXML
    public Button deleteButton;
    @FXML
    public Button submitButton;
    @FXML
    public TextField searchField;

    //Create confirmation alert
    Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //Populate the users table and privileges combo box
        usersTable.setItems(UserDatabase.getAllUsers());
        privilegesComboBox.setItems(FXCollections.observableList(Arrays.asList("ADMIN", "USER")));

        //Set the cell factories and event listeners
        setCellFactories();
        setButtonEventListeners();

    }

    /** Set the cell factories for the Users table */
    private void setCellFactories(){
        idColumn.setCellValueFactory(new PropertyValueFactory<>("userId"));
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        privilegesColumn.setCellValueFactory(new PropertyValueFactory<>("privileges"));
        createdDateColumn.setCellValueFactory(new PropertyValueFactory<>("createdDate"));
        createdByColumn.setCellValueFactory(new PropertyValueFactory<>("createdBy"));
        modifiedDateColumn.setCellValueFactory(new PropertyValueFactory<>("modifiedDate"));
        modifiedByColumn.setCellValueFactory(new PropertyValueFactory<>("modifiedBy"));
    }

    /** Set the button event listeners*/
    private void setButtonEventListeners(){

        //Set the "Create New User" button event
        submitButton.setOnAction(e -> addNewUser());

        //Set the "Modify Privileges" button event
        modifyButton.setOnAction(e -> populateFormForUpdate());

        //Set the "Delete" button event
        deleteButton.setOnAction(e -> deleteUser());

        //Search users
        searchField.setOnKeyPressed(keyEvent -> {if (keyEvent.getCode().equals(KeyCode.ENTER)){searchUsers();}});
    }

    /** Add a new User to the database */
    private void addNewUser(){

        //Generate unique User id
        Random random = new Random();
        int id = random.nextInt(1000);
        while (UserDatabase.getUserById(id) != null) {
            id = random.nextInt();
        }

        //Check if User input is valid
        if (InputValidator.validateUserForm(this)){

            //Add new User to the database
            UserDatabase.addNewUser(new User(id,
                                            usernameField.getText().trim(),
                                            privilegesComboBox.getValue(),
                                            LocalDate.now(),
                                            MainBorderPaneController.currentUser.getUsername(),
                                            LocalDate.now(),
                                            MainBorderPaneController.currentUser.getUsername()), passwordField.getText());

            //Update the Users table
            usersTable.setItems(UserDatabase.getAllUsers());

            //Clear the Users form
            clearForm();
        }
    }

    /** Populate the User form to modify User privileges */
    private void populateFormForUpdate(){

        //Get the selected user
        User user = usersTable.getSelectionModel().getSelectedItem();

        //Check if a selection was made
        if (user != null){

            //Populate the user form and disable all fields except privileges
            usernameField.setText(user.getUsername());
            usernameField.setDisable(true);
            passwordField.setText("0000000000000000000000000");
            passwordField.setDisable(true);
            confirmPasswordField.setText("0000000000000000000000000");
            confirmPasswordField.setDisable(true);
            privilegesComboBox.getSelectionModel().select(user.getPrivileges());

            //Update the submit button text and event
            submitButton.setText("Update Privileges");
            submitButton.setOnAction(e -> updatePrivileges(user));
        } else {

            //Display an error if no selection is made
            errorLabel.setText("* Please select a User to modify");
            errorLabel.setVisible(true);
        }
    }

    /** Update the User privileges in the database */
    private void updatePrivileges(User user){

            //Update the User privileges
            user.setPrivileges(privilegesComboBox.getValue());
            UserDatabase.updateUserPrivileges(user);

            //Update the users table
            usersTable.setItems(UserDatabase.getAllUsers());

            //Clear the User form
            clearForm();

            //Update the submit button text and event
            submitButton.setText("Create New User");
            submitButton.setOnAction(e -> addNewUser());
    }

    /** Delete the Selected User */
    private void deleteUser(){

        //Get the selected User
        User user = usersTable.getSelectionModel().getSelectedItem();

        //Check if a selection was made
        if (user != null){

            //Set the confirmation alert content
            confirmationAlert.setTitle("Delete User");
            confirmationAlert.setTitle("Delete User");
            confirmationAlert.setContentText("Are you sure you want to delete this User?\n" +
                                            "User ID:\t\t" + user.getUserId() +
                                            "\nUsername:\t" + user.getUsername() +
                                            "\nPrivileges:\t" + user.getPrivileges());
            confirmationAlert.getDialogPane().setPrefHeight(Region.USE_COMPUTED_SIZE);

            //Confirm the user wants to delete the User
            confirmationAlert.showAndWait().ifPresent(response -> {
                if (response.equals(ButtonType.OK)){

                    //Delete the User from the database and update the Users table
                    UserDatabase.deleteUser(user);
                    usersTable.setItems(UserDatabase.getAllUsers());
                }
            });

        } else {

            //If no User is selected, display an error
            errorLabel.setText("* Please select a User to delete");
            errorLabel.setVisible(true);
        }
    }

    /** Clear the Users form on Submit*/
    private void clearForm(){
        usernameField.clear();
        usernameField.setDisable(false);
        passwordField.clear();
        passwordField.setDisable(false);
        confirmPasswordField.clear();
        confirmPasswordField.setDisable(false);
        privilegesComboBox.getSelectionModel().clearSelection();
        errorLabel.setVisible(false);
    }

    /** Search users and display the result in the table*/
    private void searchUsers(){
        usersTable.setItems(UserDatabase.searchUsers(searchField.getText().trim()));
    }
}
