package Controller;

import Database.PartDatabase;
import Database.ProductDatabase;
import Model.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;

import java.net.URL;
import java.text.NumberFormat;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicBoolean;

/** Class that handles Part form GUI functionality */
public class PartsController implements Initializable {

    @FXML
    public TextField idField;
    @FXML
    public TextField nameField;
    @FXML
    public TextField priceField;
    @FXML
    public TextField stockField;
    @FXML
    public Button submitButton;
    @FXML
    public Label errorLabel;
    @FXML
    public TableView<InventoryPart> partsTable;
    @FXML
    public TableColumn<InventoryPart, Integer> idColumn;
    @FXML
    public TableColumn<InventoryPart, String> nameColumn;
    @FXML
    public TableColumn<InventoryPart, Double> priceColumn;
    @FXML
    public TableColumn<InventoryPart, Integer> stockColumn;
    @FXML
    public Button modifyButton;
    @FXML
    public Button deleteButton;
    @FXML
    public TextField partSearchField;


    //Alert dialogs
    private final Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
    private final Alert informationAlert = new Alert(Alert.AlertType.INFORMATION);


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setCellFactories();
        setButtonEventHandlers();

        //Set parts table items after scene loads
        Platform.runLater(() -> partsTable.setItems(PartDatabase.getAllParts()));
    }

    /** Set the Parts table cell factories */
    private void setCellFactories(){
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        //Format price column to currency
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
        priceColumn.setCellFactory(tableCell -> new TableCell<>() {
            @Override
            protected void updateItem(Double price, boolean empty) {
                super.updateItem(price, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText(currencyFormat.format(price));
                }
            }
        });
        stockColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
    }

    /** Set the button event handlers*/
    private void setButtonEventHandlers(){

        //Set the event for the "Create New Part" button
        submitButton.setOnAction(e -> addPart());

        //Set the event for the "Delete" button
        deleteButton.setOnAction( e -> deletePart());

        //Set the event for the "Modify" button
        modifyButton.setOnAction(e -> populateUpdatePart());

        //Set event for key pressed on part search field
        partSearchField.setOnKeyPressed(keyEvent -> {if (keyEvent.getCode().equals(KeyCode.ENTER)){searchParts();}});
    }

    /** Populate the part form for updating parts */
    private void populateUpdatePart(){
        //Get selected part
        InventoryPart part = partsTable.getSelectionModel().getSelectedItem();

        //Check if part is null
        if (part != null){

            //Fill fields with part information
            idField.setText(Integer.toString(part.getId()));
            nameField.setText(part.getName());
            priceField.setText(Double.toString(part.getPrice()));
            stockField.setText(Integer.toString(part.getStock()));

            //Update button text/event
            submitButton.setText("Update Part");
            submitButton.setOnAction( event -> updatePart(part));
        } else {
            //Display alert
            displayNoSelectionAlert();
        }
    }

    /** Handle adding a new part */
    private void addPart(){

        //Check if input is valid
        if (InputValidator.validatePartForm(this)){

            //Generate unique part id
            Random random = new Random();
            int id = random.nextInt(1000);
            while (PartDatabase.getPartById(id) != null) {
                id = random.nextInt();
            }

            //Add part to the database
            PartDatabase.addPart(new InventoryPart(id,
                    nameField.getText(),
                    Double.parseDouble(priceField.getText()),
                    Integer.parseInt(stockField.getText())));

            //Update parts table
            partsTable.setItems(PartDatabase.getAllParts());

            resetForm();
        }
    }

    /** Handle deleting a part */
    private void deletePart(){
        //Get the selected part
        Part part = partsTable.getSelectionModel().getSelectedItem();

        //Check if the part is null
        if (part != null) {

            //Confirm user wants to delete the part
            if (displayConfirmDelete()) {

                //Check if the part has associated products
                if (ProductDatabase.getPartProducts(part.getId()).size() == 0) {

                    //Remove the part from the database and update the table
                    PartDatabase.removePart(part.getId());
                    partsTable.setItems(PartDatabase.getAllParts());
                } else {
                    //Display alert
                    displayAssociatedProductsAlert(part);
                }
            }
        } else {
            //Display alert
            displayNoSelectionAlert();
        }
    }

    /**
     * Update Part
     * @param part the Part to update
     */
    private void updatePart(InventoryPart part){
        if (InputValidator.validatePartForm(this)){

            //Store the original part price
            double originalPartPrice = part.getPrice();

            //Set new values
            part.setName(nameField.getText());
            part.setPrice(Double.parseDouble(priceField.getText()));
            part.setStock(Integer.parseInt(stockField.getText()));

            //Update part in database
            PartDatabase.updatePart(part);

            //Update table
            partsTable.setItems(PartDatabase.getAllParts());

            //Update each product associated with the part being updated
            for (InventoryProduct product : ProductDatabase.getPartProducts(part.getId())){

                /*
                Set the new product price:
                    Divide the products price by the manufacturing fee to get the sum of part prices
                    Subtract the original price of the part being updated
                    Add the updated part price
                    Multiply the new sum of part prices by the manufacturing fee
                 */
                double MANUFACTURING_FEE = 1.32;

                //Update the product price if the part price changed
                if (part.getPrice() != originalPartPrice) {
                    //product.setPrice((product.getPrice() / MANUFACTURING_FEE) - originalPartPrice + part.getPrice() * MANUFACTURING_FEE);
                    product.setProductPrice();
                }

                //Set the new value of the associated parts string
                product.setProductPartsString();


                //Update the product in the database
                ProductDatabase.updateProduct(product);

            }

            //Reset the form and button text/event
            resetForm();
            submitButton.setText("Create New Part");
            submitButton.setOnAction(e -> addPart());

        }

    }

    /**
     * Display an alert if Part is associated with a Product
     * @param part the associated Part
     */
    private void displayAssociatedProductsAlert(Part part){

        //Create alert message
        StringBuilder builder = new StringBuilder();
        builder.append("This Part has the following Products associated with it:\n\n");
        for (Product product: ProductDatabase.getPartProducts(part.getId())){
            builder.append("Product ID: ");
            builder.append(product.getId());
            builder.append("\n");
            builder.append("Product Name: ");
            builder.append(product.getName());
            builder.append("\n\n");
        }
        builder.append("Please delete these Products before deleting this Part.");

        //Set alert content and show alert
        informationAlert.setTitle("Delete Part");
        informationAlert.setHeaderText("Part Has Associated Products");
        informationAlert.setContentText(builder.toString());
        informationAlert.showAndWait();
    }

    /** Display an alert when no selection is made when updating or deleting a part */
    private void displayNoSelectionAlert(){

        //Set alert content and show alert
        informationAlert.setTitle("No Selection");
        informationAlert.setHeaderText("No Part Selected");
        informationAlert.setContentText("Please select a part");
        informationAlert.showAndWait();
    }

    /**
     * Display alert to confirm user wants to delete selected Part
     * @return the response of the confirmation
     */
    private boolean displayConfirmDelete(){

        //Create atomic boolean result
        AtomicBoolean deleteConfirmed = new AtomicBoolean(false);

        //Set alert content
        confirmationAlert.setTitle("Delete Part");
        confirmationAlert.setHeaderText("Delete Part");
        confirmationAlert.setContentText("Are you sure you want to delete this part?");

        //Show alert and retrieve user response
        confirmationAlert.showAndWait().ifPresent(response -> deleteConfirmed.set(response.equals(ButtonType.OK)));

        //Return response
        return deleteConfirmed.get();
    }

    /** Reset the part form values when parts are added or updated */
    private void resetForm(){
        nameField.clear();
        priceField.clear();
        stockField.clear();
    }

    /** Search Parts and display result in Table */
    private void searchParts(){
        partsTable.setItems(PartDatabase.searchInventoryParts(partSearchField.getText().trim()));
    }
}
