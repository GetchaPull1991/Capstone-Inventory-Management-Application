package View;

import Database.PartDatabase;
import Database.ProductDatabase;
import Model.InputValidator;
import Model.Part;
import Model.Product;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import java.net.URL;
import java.text.NumberFormat;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicBoolean;

/** Controller Class to handle GUI functionality for Parts */
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
    public TableView<Part> partsTable;
    @FXML
    public TableColumn<Part, Integer> idColumn;
    @FXML
    public TableColumn<Part, String> nameColumn;
    @FXML
    public TableColumn<Part, Double> priceColumn;
    @FXML
    public TableColumn<Part, Integer> stockColumn;
    @FXML
    public Button modifyButton;
    @FXML
    public Button deleteButton;

    //Displays as placeholder in parts table while database information is retrieved
    ProgressIndicator indicator = new ProgressIndicator();

    //Alert dialogs
    Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
    Alert informationAlert = new Alert(Alert.AlertType.INFORMATION);


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setCellFactories();
        setButtonEventHandlers();
        partsTable.setPlaceholder(indicator);
        Platform.runLater(() -> {
            partsTable.setItems(PartDatabase.getAllParts());
            partsTable.setPlaceholder(new Text("No Parts To Display"));
        });
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
    }

    /** Populate the part form for updating parts */
    private void populateUpdatePart(){
        //Get selected part
        Part part = partsTable.getSelectionModel().getSelectedItem();

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
            PartDatabase.addPart(new Part(id,
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
                    displayAssociatedPartsAlert(part);
                }
            }
        } else {
            //Display alert
            displayNoSelectionAlert();
        }
    }

    /** Handle updating a part */
    private void updatePart(Part part){
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
            for (Product product : ProductDatabase.getPartProducts(part.getId())){

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
                    product.setPrice((product.getPrice() / MANUFACTURING_FEE) - originalPartPrice + part.getPrice() * MANUFACTURING_FEE);
                }


                //Set the product stock to the lowest part stock
                int stock = product.getAssociatedParts().get(0).getStock();
                for (Part associatedPart : product.getAssociatedParts()){
                    if (associatedPart.getStock() < stock){
                        stock = associatedPart.getStock();
                    }
                }
                product.setStock(stock);

                //Set the new value of the associated parts string
                product.setAssociatedPartsString();

                //Update the product in the database
                ProductDatabase.updateProduct(product);

            }

            //Reset the form and button text/event
            resetForm();
            submitButton.setText("Create New Part");
            submitButton.setOnAction(e -> addPart());

        }

    }

    /** Display an alert when the part to be deleted has associated products */
    private void displayAssociatedPartsAlert(Part part){
        StringBuilder builder = new StringBuilder();
        builder.append("This Part has the following Products associated with it:\n\n");

        /*
        BUG : Displays the same product multiple times in dialog when
        the product has more than one of the same part associated with it
         */

        for (Product product: ProductDatabase.getPartProducts(part.getId())){
            builder.append("Product ID: ");
            builder.append(product.getId());
            builder.append("\n");
            builder.append("Product Name: ");
            builder.append(product.getName());
            builder.append("\n\n");
        }

        builder.append("Please delete these Products before deleting this Part.");

        informationAlert.setTitle("Delete Part");
        informationAlert.setHeaderText("Part Has Associated Products");
        informationAlert.setContentText(builder.toString());
        informationAlert.showAndWait();
    }

    /** Display an alert when no selection is made when updating or deleting a part */
    private void displayNoSelectionAlert(){
        informationAlert.setTitle("No Selection");
        informationAlert.setHeaderText("No Part Selected");
        informationAlert.setContentText("Please select a part");
        informationAlert.showAndWait();
    }

    /** Display an alert to confirm the user wants to delete the selected part */
    private boolean displayConfirmDelete(){
        AtomicBoolean deleteConfirmed = new AtomicBoolean(false);

        confirmationAlert.setTitle("Delete Part");
        confirmationAlert.setHeaderText("Delete Part");
        confirmationAlert.setContentText("Are you sure you want to delete this part?");
        confirmationAlert.showAndWait().ifPresent(response -> deleteConfirmed.set(response.equals(ButtonType.OK)));

        return deleteConfirmed.get();
    }

    /** Reset the part form values when parts are added or updated */
    private void resetForm(){
        nameField.clear();
        priceField.clear();
        stockField.clear();
    }
}
