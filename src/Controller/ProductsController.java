package Controller;

import Database.OrderDatabase;
import Database.PartDatabase;
import Database.ProductDatabase;
import Model.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;

import java.net.URL;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

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
    public TableView<InventoryPart> partsTableView;
    @FXML
    public TableColumn<InventoryPart, Integer> partIdColumn;
    @FXML
    public TableColumn<InventoryPart, String> partNameColumn;
    @FXML
    public TableColumn<InventoryPart, Double> partPriceColumn;
    @FXML
    public TableView<ProductPart> productPartsTableView;
    @FXML
    public TableColumn<ProductPart, Integer> productPartsIdColumn;
    @FXML
    public TableColumn<ProductPart, String> productPartNameColumn;
    @FXML
    public TableColumn<ProductPart, Double> productPartPriceColumn;
    @FXML
    public TableColumn<ProductPart, Integer> productPartQuantityColumn;
    @FXML
    public Button addPartButton;
    @FXML
    public Button submitButton;
    @FXML
    public TableView<InventoryProduct> productsTable;
    @FXML
    public TableColumn<InventoryProduct, Integer> productIdColumn;
    @FXML
    public TableColumn<InventoryProduct, String> productNameColumn;
    @FXML
    public TableColumn<InventoryProduct, String> partsColumn;
    @FXML
    public TableColumn<InventoryProduct, Double> productPriceColumn;
    @FXML
    public TableColumn<InventoryProduct, Integer> stockColumn;
    @FXML
    public Button modifyButton;
    @FXML
    public Button deleteButton;
    @FXML
    public Button removePartButton;
    @FXML
    public Label errorLabel;
    @FXML
    public TextField productSearchField;
    @FXML
    public TextField partSearchField;

    //Store the parts to be associated with the new product
    ObservableList<ProductPart> productParts = FXCollections.observableArrayList();

    //Create alerts
    Alert informationAlert = new Alert(Alert.AlertType.INFORMATION);
    Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);

    //Create currency format
    NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setCellFactories();
        setButtonEventHandlers();

        //Populate tables after load
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
        partsColumn.setCellValueFactory(new PropertyValueFactory<>("productPartsString"));

        //Wrap Text in table cell
        partsColumn.setCellFactory(tableCell ->{
            TableCell<InventoryProduct, String> cell = new TableCell<>();
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
        productPartQuantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));

    }

    /** Create event handlers for all buttons*/
    private void setButtonEventHandlers(){

        //Add listener to product parts to repopulate table on change
        productParts.addListener((ListChangeListener<ProductPart>) change -> productPartsTableView.setItems(productParts));

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

        //Set part search key event
        partSearchField.setOnKeyPressed(keyEvent -> {if (keyEvent.getCode().equals(KeyCode.ENTER)){searchParts();}});

        //Set product search key event
        productSearchField.setOnKeyPressed(keyEvent -> {if (keyEvent.getCode().equals(KeyCode.ENTER)){searchProducts();}});
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
            ProductDatabase.addProduct(new InventoryProduct(
                    id,
                    nameField.getText(),
                    productParts));

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

    private void updateProduct(InventoryProduct product){

        //Check if input is valid
        if (InputValidator.validateProductForm(this)){

            //Set new values
            product.setName(nameField.getText());
            product.setProductParts(productParts);
            product.setProductPrice();
            product.setProductStock();

            ProductDatabase.updateProduct(product);
            productsTable.setItems(ProductDatabase.getAllProducts());

            resetForm();

        }
    }

    private void populateProductFormForUpdate(){

        //Get the selected product
        InventoryProduct product = productsTable.getSelectionModel().getSelectedItem();

        //Check if a product was selected
        if (product != null){

            //Populate form
            nameField.setText(product.getName());
            idField.setText(Integer.toString(product.getId()));
            priceField.setText(currencyFormat.format(product.getPrice()));
            stockField.setText(Integer.toString(product.getStock()));
            productParts = product.getProductParts();
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
        InventoryPart part = partsTableView.getSelectionModel().getSelectedItem();

        //Check if part is null
        if (part != null){

            //Check if the list contains any parts with the same id
            if (productParts.stream().anyMatch(o -> o.getId() == part.getId())){

                //If a part is found, increment the quantity
                productParts.stream().filter(productPart -> productPart.getId() == part.getId()).collect(Collectors.toList()).get(0).incrementQuantity();

            } else {
                //If a part is not found, add a new part
                productParts.add(new ProductPart(part.getId(), part.getName(), part.getPrice(), 1));
            }
            //Update the stock and price fields
            setPriceAndStockFields();
        }
    }

    /** Handle remove Part */
    private void removePart(){
        ProductPart part = productPartsTableView.getSelectionModel().getSelectedItem();

        //Check if the part is null
        if (part != null){
            //Check if the part quantity is 1
            if (part.getQuantity() == 1){

                //Remove the part from the product parts list and table
                productParts.remove(part);
                partsTableView.setItems(PartDatabase.getAllParts());

                setPriceAndStockFields();

            //Decrement the stock if the quantity is greater than 1
            } else if (part.getQuantity() > 1) {
                part.decrementQuantity();
                productParts.set(productPartsTableView.getSelectionModel().getSelectedIndex(), part);
            }

            setPriceAndStockFields();
        //Display alert
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
        setPriceAndStockFields();
    }

    private void setPriceAndStockFields(){

        //Set the price and stock fields
        if (productParts.size() > 0) {

            double price = 0;

            for (ProductPart productPart : productParts){
                price += productPart.getPrice() * productPart.getQuantity();
            }

            priceField.setText(currencyFormat.format(price * 1.32));
            stockField.setText(Integer.toString(getProductStock()));
        } else {
            priceField.setText("Auto-Generated");
            stockField.setText("Auto-Generated");
        }

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
     * Get the stock of the product
     * @return the stock
     */
    private int getProductStock(){

        //Initialize stock with first product part
        int stock = PartDatabase.getPartById(productParts.get(0).getId()).getStock();

        //For each product part
        for (ProductPart part : productParts){

            /*
            The part stock is determined by the stock of the Inventory Part divided
            by the quantity of the Product Part in the product.
            Therefore if there are 12 Inventory Parts available
            and the Product Part requires 2 of the same part,
            only 6 products will be available.
            In the same sense, if the Inventory stock was 13,
            only 6 products would be available.
             */
            int partStock = PartDatabase.getPartById(part.getId()).getStock() / part.getQuantity();

            //If inventory stock is less than stock
            if (stock > partStock){

                //Update stock to inventory stock
                stock = partStock;
            }
        }
        return stock;
    }

    private void searchProducts(){
        productsTable.setItems(ProductDatabase.searchInventoryProducts(productSearchField.getText().trim()));
    }

    private void searchParts(){
        partsTableView.setItems(PartDatabase.searchInventoryParts(partSearchField.getText().trim()));
    }
}
