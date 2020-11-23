package Controller;

import Database.OrderDatabase;
import Database.PartDatabase;
import Database.ProductDatabase;
import Model.InputValidator;
import Model.Order;
import Model.Part;
import Model.Product;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;

import java.net.URL;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicBoolean;

public class ProductsController implements Initializable {
    @FXML
    public TextField nameField;
    @FXML
    public TextField idField;
    @FXML
    public TextField priceField;
    @FXML
    public TextField stockField;
    @FXML
    public TableView<Part> partsTableView;
    @FXML
    public TableColumn<Part, Integer> partIdColumn;
    @FXML
    public TableColumn<Part, String> partNameColumn;
    @FXML
    public TableColumn<Part, Double> partPriceColumn;
    @FXML
    public TableView<Part> productPartsTableView;
    @FXML
    public TableColumn<Part, Integer> productPartsIdColumn;
    @FXML
    public TableColumn<Part, String> productPartNameColumn;
    @FXML
    public TableColumn<Part, Double> productPartPriceColumn;
    @FXML
    public Button addPartButton;
    @FXML
    public Button submitButton;
    @FXML
    public TableView<Product> productsTable;
    @FXML
    public TableColumn<Product, Integer> productIdColumn;
    @FXML
    public TableColumn<Product, String> productNameColumn;
    @FXML
    public TableColumn<Product, String> partsColumn;
    @FXML
    public TableColumn<Product, Double> productPriceColumn;
    @FXML
    public TableColumn<Product, Integer> stockColumn;
    @FXML
    public Button modifyButton;
    @FXML
    public Button deleteButton;
    @FXML
    public Button removePartButton;
    @FXML
    public Label errorLabel;

    //Store the parts to be associated with the new product
    ObservableList<Part> productParts = FXCollections.observableArrayList();

    //Create alerts
    Alert informationAlert = new Alert(Alert.AlertType.INFORMATION);
    Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);

    NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setCellFactories();
        setButtonEventHandlers();
        Platform.runLater(() -> new Thread (() -> {
            productsTable.setItems(ProductDatabase.getAllProducts());
            partsTableView.setItems(PartDatabase.getAllParts());
        }).start());
    }

    /** Create cell factories for the Parts and Products tables */
    private void setCellFactories(){

        //Product Table
        productIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        productNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        partsColumn.setCellValueFactory(new PropertyValueFactory<>("associatedPartsString"));

        //Wrap Text in table cell
        partsColumn.setCellFactory(tableCell ->{
            TableCell<Product, String> cell = new TableCell<>();
            Text text = new Text();
            cell.setGraphic(text);
            cell.setPrefHeight(Control.USE_COMPUTED_SIZE);
            text.wrappingWidthProperty().bind(partsColumn.widthProperty());
            text.textProperty().bind(cell.itemProperty());
            return cell;
        });

        productPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        //Format currency
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
        productPriceColumn.setCellFactory(tableCell -> new TableCell<>(){
            @Override
            protected void updateItem(Double price, boolean empty) {
                super.updateItem(price, empty);
                if (empty){
                    setText(null);
                } else {
                    setText(currencyFormat.format(price));
                }
            }
        });
        stockColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));


        //Available Parts Table
        partIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        partPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        //Format currency
        partPriceColumn.setCellFactory(tableCell -> new TableCell<>(){
            @Override
            protected void updateItem(Double price, boolean empty) {
                super.updateItem(price, empty);
                if (empty){
                    setText(null);
                } else {
                    setText(currencyFormat.format(price));
                }
            }
        });


        //Product parts table
        productPartsIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        productPartNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        productPartPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        //Format currency
        productPartPriceColumn.setCellFactory(tableCell -> new TableCell<>(){
            @Override
            protected void updateItem(Double price, boolean empty) {
                super.updateItem(price, empty);
                if (empty){
                    setText(null);
                } else {
                    setText(currencyFormat.format(price));
                }
            }
        });

    }

    /** Create event handlers for all buttons*/
    private void setButtonEventHandlers(){

        //Set event for "Add Part" button
        addPartButton.setOnAction(e -> addPart());

        //Set event for "Remove Part" button
        removePartButton.setOnAction(e -> removePart());

        //Set event for "Create New Product" button
        submitButton.setOnAction(e -> addProduct());

        //Set event for "Delete" button
        deleteButton.setOnAction(e -> removeProduct());

        //Set event for "Modify" button
        modifyButton.setOnAction(e -> populateProductFormForUpdate());
    }

    /** Handle add new Product*/
    private void addProduct(){

        //Check if input is valid
        if (InputValidator.validateProductForm(this)){

            //Generate unique customer id
            Random random = new Random();
            int id = random.nextInt(1000);
            while (ProductDatabase.getProductById(id) != null) {
                id = random.nextInt();
            }

            //Add product to database
            ProductDatabase.addProduct(new Product(id,
                    nameField.getText(),
                    getProductPrice(), getProductStock(), productParts));

            //Update the products table
            productsTable.setItems(ProductDatabase.getAllProducts());

            //Reset the products form
            resetForm();
        }
    }

    /** Handle remove Product */
    private void removeProduct(){

        //Get the selected product from the products table
        Product product = productsTable.getSelectionModel().getSelectedItem();

        //Check if a product was selected
        if (product != null){

            //Confirm the user wants to remove the product
            if (userConfirmedRemoveProduct()) {

                //Check if the product has any orders associated with it
                if (OrderDatabase.getPendingProductOrders(product.getId()).size() == 0) {

                    //Remove the product from the database and update the products table
                    ProductDatabase.removeProduct(product.getId());
                    productsTable.setItems(ProductDatabase.getAllProducts());
                } else {

                    //Display alert
                    displayAssociatedOrdersAlert(product);
                }
            }
        } else {

            //Display alert
            displayNoSelectionAlert("Product", "Delete");
        }
    }

    private void updateProduct(Product product){

        //Check if input is valid
        if (InputValidator.validateProductForm(this)){

            //Set new values
            product.setName(nameField.getText());
            product.setAssociatedParts(productParts);
            product.setPrice(getProductPrice());
            product.setStock(getProductStock());

            ProductDatabase.updateProduct(product);
            productsTable.setItems(ProductDatabase.getAllProducts());

            resetForm();

        }
    }

    private void populateProductFormForUpdate(){

        //Get the selected product
        Product product = productsTable.getSelectionModel().getSelectedItem();

        //Check if a product was selected
        if (product != null){

            //Populate form
            nameField.setText(product.getName());
            idField.setText(Integer.toString(product.getId()));
            priceField.setText(currencyFormat.format(product.getPrice()));
            stockField.setText(Integer.toString(product.getStock()));
            productParts = product.getAssociatedParts();
            productPartsTableView.setItems(productParts);

            //Set submit button text and event
            submitButton.setText("Update Product");
            submitButton.setOnAction(e -> updateProduct(product));

        } else {
            //Display alert
            displayNoSelectionAlert("Product", "Update");
        }
    }

    /** Handle add Part */
    private void addPart(){
        Part part = partsTableView.getSelectionModel().getSelectedItem();

        //Check if part is null or the table row is disabled
        if (part != null){

            //Add part to product parts list and table
            productParts.add(part);
            productPartsTableView.setItems(productParts);

            priceField.setText(currencyFormat.format(getProductPrice()));
            stockField.setText(Integer.toString(getProductStock()));

        }
    }

    /** Handle remove Part */
    private void removePart(){
        Part part = productPartsTableView.getSelectionModel().getSelectedItem();

        //Check if the part is null
        if (part != null){

            //Remove the part from the product parts list and table
            productParts.remove(part);
            productPartsTableView.setItems(productParts);
            partsTableView.setItems(PartDatabase.getAllParts());

            if (productParts.size() > 0) {
                priceField.setText(currencyFormat.format(getProductPrice()));
                stockField.setText(Integer.toString(getProductStock()));
            } else {
                priceField.setText("Auto-Generated");
                stockField.setText("Auto-Generated");
            }
        } else {
            displayNoSelectionAlert("Part", "Remove");
        }
    }

    /** Reset form on Create and Update Product*/
    private void resetForm(){
        nameField.clear();
        productParts.clear();
        productPartsTableView.getItems().clear();
        partsTableView.getSelectionModel().clearSelection();
    }

    /**
     * Display alert to confirm user wants to delete selected Product
     * @return the users response
     */
    private boolean userConfirmedRemoveProduct(){

        AtomicBoolean deleteConfirmed = new AtomicBoolean();

        confirmationAlert.setTitle("Remove Product");
        confirmationAlert.setHeaderText("Remove Product");
        confirmationAlert.setContentText("Are you sure you want to remove this product?");
        confirmationAlert.showAndWait().ifPresent(response -> deleteConfirmed.set(response.equals(ButtonType.OK)));

        return deleteConfirmed.get();
    }

    /**
     * Display alert with orders associated with the selected product
     * @param product the product that was selected
     */
    private void displayAssociatedOrdersAlert(Product product){
        StringBuilder builder = new StringBuilder();
        builder.append("This Product has the following pending Orders associated with it:\n\n");

        for (Order order: OrderDatabase.getPendingProductOrders(product.getId())){
            builder.append("Order ID: ");
            builder.append(order.getOrderID());
            builder.append("\n");
            builder.append("Order Due Date: ");
            builder.append(order.getDueDate().format(DateTimeFormatter.ofPattern("MM/dd/yyyy")));
            builder.append("\n\n");
        }

        builder.append("Please cancel these Orders before deleting this Product.");

        informationAlert.setTitle("Delete Product");
        informationAlert.setHeaderText("Product Has Pending Orders");
        informationAlert.setContentText(builder.toString());
        informationAlert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        informationAlert.showAndWait();
    }

    /**
     * Display alert that no selection has been made
     * @param selectionType the type of selection that is missing ('Part', 'Product')
     * @param actionType the type of action being performed ('Remove', 'Delete', 'Update')
     */
    private void displayNoSelectionAlert(String selectionType, String actionType){
        informationAlert.setTitle(actionType + " " + selectionType);
        informationAlert.setHeaderText("No " + selectionType + " Selection Made");
        informationAlert.setContentText("Please select a " + selectionType + " to " + actionType);
        informationAlert.showAndWait();
    }

    /**
     * Get the price of the product
     * @return the price
     */
    private double getProductPrice(){

        //Store the base price and stock
        double price = 0;

        if (productParts.size() > 0) {
            //Get the sum of the part prices
            for (Part part : productParts) {
                price += part.getPrice();
            }

            //Add manufacturing fee to product price
            double MANUFACTURING_FEE = 1.32;
            price *= MANUFACTURING_FEE;
        }

        return price;
    }

    /**
     * Get the stock of the product
     * @return the stock
     */
    private int getProductStock(){

        //Initialize Stock
        int stock = 0;

        //Check if product parts has parts
        if (productParts.size() > 0) {

            //Set base stock
            stock = productParts.get(0).getStock();

            //Get the lowest stock of associated parts
            for (Part part : productParts) {
                if (part.getStock() < stock) {
                    stock = part.getStock();
                }
            }
        }

        return stock;
    }

}
