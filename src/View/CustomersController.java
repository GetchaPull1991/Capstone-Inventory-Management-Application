package View;

import Model.Customer;
import Database.CustomerDatabase;
import Database.OrderDatabase;
import Model.InputValidator;
import Model.Order;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Region;

import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicBoolean;

public class CustomersController implements Initializable {

    @FXML
    public TableView<Customer> customersTable;
    @FXML
    public TableColumn<Customer, String> nameColumn;
    @FXML
    public TableColumn<Customer, String> addressColumn;
    @FXML
    public TableColumn<Customer, String> phoneColumn;
    @FXML
    public TableColumn<Customer, Integer> idColumn;
    @FXML
    public TableColumn<Customer, String> cityColumn;
    @FXML
    public TableColumn<Customer, String> divisionColumn;
    @FXML
    public TableColumn<Customer, String> postalCodeColumn;
    @FXML
    public TableColumn<Customer, String> countryColumn;
    @FXML
    public TextField nameField;
    @FXML
    public TextField idField;
    @FXML
    public TextField phoneField;
    @FXML
    public Button deleteButton;
    @FXML
    public Button modifyButton;
    @FXML
    public Button submitButton;
    @FXML
    public TextField streetAddressField;
    @FXML
    public TextField postalCodeField;
    @FXML
    public TextField countryField;
    @FXML
    public TextField divisionField;
    @FXML
    public Label errorLabel;
    @FXML
    public TextField cityField;

    Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
    Alert informationAlert = new Alert(Alert.AlertType.INFORMATION);


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setCellFactories();
        setButtonEventHandlers();
        Platform.runLater(() -> {customersTable.setItems(CustomerDatabase.getAllCustomers());});
    }

    private void setButtonEventHandlers(){

        //Set event for "Add New Customer" button
        submitButton.setOnAction(e -> addNewCustomer());

        //Set event for "Delete" button
        deleteButton.setOnAction(e -> deleteCustomer());

        //Set event for "Modify" button
        modifyButton.setOnAction(e -> populateCustomerFormForUpdate());
    }

    private void addNewCustomer(){
        if (InputValidator.validateCustomerForm(this)){

            //Generate unique customer id
            Random random = new Random();
            int id = random.nextInt(1000);
            while (CustomerDatabase.getCustomerById(id) != null) {
                id = random.nextInt();
            }

            CustomerDatabase.addCustomer(new Customer(id,
                    nameField.getText(),
                    streetAddressField.getText(),
                    cityField.getText(),
                    divisionField.getText(),
                    postalCodeField.getText(),
                    countryField.getText(),
                    phoneField.getText()));

            clearForm();

            customersTable.setItems(CustomerDatabase.getAllCustomers());
        }
    }

    private void deleteCustomer(){

        //Get the selected customer
        Customer customer = customersTable.getSelectionModel().getSelectedItem();

        //Check if a customer was selected
        if (customer != null){

            //Confirm the user wants to delete the customer
            if (confirmCustomerDelete()) {

                //Check if the Customer has pending orders
                if (OrderDatabase.getCustomerOrders(customer.getCustomerID()).size() == 0) {

                    //Remove the customer from the customer database and table
                    CustomerDatabase.removeCustomer(customer.getCustomerID());
                    customersTable.setItems(CustomerDatabase.getAllCustomers());
                } else {
                    //Display alert
                    displayPendingOrdersAlert(customer);
                }
            }
        } else {
            //Display alert
            displayNoSelectionAlert("Delete");
        }
    }

    private void populateCustomerFormForUpdate(){

        //Get the selected Customer
        Customer customer = customersTable.getSelectionModel().getSelectedItem();

        //Check if a customer was selected
        if (customer != null){

            //Populate fields for update
            nameField.setText(customer.getName());
            phoneField.setText(customer.getPhoneNumber());
            streetAddressField.setText(customer.getStreetAddress());
            cityField.setText(customer.getCity());
            postalCodeField.setText(customer.getPostalCode());
            countryField.setText(customer.getCountry());
            divisionField.setText(customer.getDivision());

            //Set submit button text and event
            submitButton.setText("Update Customer");
            submitButton.setOnAction(e -> updateCustomer(customer));

        } else {
        displayNoSelectionAlert("Update");
        }
    }

    private void updateCustomer(Customer customer){

            //Set the new Customer values
            customer.setName(nameField.getText());
            customer.setPhoneNumber(phoneField.getText());
            customer.setStreetAddress(streetAddressField.getText());
            customer.setCity(cityField.getText());
            customer.setPostalCode(postalCodeField.getText());
            customer.setCountry(countryField.getText());
            customer.setDivision(divisionField.getText());

            //Update the Customer in the customer database and customers table
            CustomerDatabase.updateCustomer(customer);
            customersTable.setItems(CustomerDatabase.getAllCustomers());

            //Set submit button text and event
            submitButton.setText("Create New Customer");
            submitButton.setOnAction(e -> addNewCustomer());

            //Clear the form on update
            clearForm();

    }

    private void setCellFactories(){
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("streetAddress"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        idColumn.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        cityColumn.setCellValueFactory(new PropertyValueFactory<>("city"));
        postalCodeColumn.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        divisionColumn.setCellValueFactory(new PropertyValueFactory<>("division"));
        countryColumn.setCellValueFactory(new PropertyValueFactory<>("country"));
    }

    private boolean confirmCustomerDelete(){
        //Store result
        AtomicBoolean confirmDelete = new AtomicBoolean();

        //Display alert and wait for response
        confirmationAlert.setTitle("Delete Customer");
        confirmationAlert.setHeaderText("Delete Customer");
        confirmationAlert.setContentText("Are you sure you want to delete this Customer?");
        confirmationAlert.showAndWait().ifPresent(response -> confirmDelete.set(response.equals(ButtonType.OK)));

        //Return result
        return confirmDelete.get();
    }

    private void displayNoSelectionAlert(String actionType){
        informationAlert.setTitle(actionType + " Customer");
        informationAlert.setHeaderText("No Customer Selected");
        informationAlert.setContentText("Please select a Customer to " + actionType);
    }

    private void displayPendingOrdersAlert(Customer customer){

        StringBuilder builder = new StringBuilder();
        builder.append("The Customer has the following pending orders associated with it: \n\n");

        for (Order order : OrderDatabase.getCustomerOrders(customer.getCustomerID())){
            builder.append("Order ID: ");
            builder.append(order.getOrderID());
            builder.append("\n");
            builder.append("Order Due Date: ");
            builder.append(order.getDueDate().format(DateTimeFormatter.ofPattern("MM/dd/yyyy")));
            builder.append("\n\n");
        }
        builder.append("Please fulfill or cancel these Orders before deleting the Customer.");

        informationAlert.setTitle("Delete Customer");
        informationAlert.setHeaderText("Customer Has Pending Orders");
        informationAlert.setContentText(builder.toString());
        informationAlert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        informationAlert.showAndWait();
    }


    private void clearForm(){
        idField.clear();
        nameField.clear();
        phoneField.clear();
        streetAddressField.clear();
        cityField.clear();
        postalCodeField.clear();
        countryField.clear();
        divisionField.clear();
    }

}
