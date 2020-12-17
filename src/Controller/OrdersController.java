package Controller;

import Database.CustomerDatabase;
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
import javafx.scene.text.Text;
import javafx.util.Callback;

import java.net.URL;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/** Class that handles the Order form GUI functionality */
public class OrdersController implements Initializable {
    @FXML
    public TextField idField;
    @FXML
    public ComboBox<Customer> customerComboBox;
    @FXML
    public Button addProductButton;
    @FXML
    public TableView<InventoryProduct> productsTableView;
    @FXML
    public TableColumn<InventoryProduct, Integer> productIdColumn;
    @FXML
    public TableColumn<InventoryProduct, String> productNameColumn;
    @FXML
    public TableColumn<InventoryProduct, Double> productPriceColumn;
    @FXML
    public TableColumn<InventoryProduct, Integer> productStockColumn;
    @FXML
    public DatePicker dueDatePicker;
    @FXML
    public Button submitButton;
    @FXML
    public TableView<Order> ordersTable;
    @FXML
    public TableColumn<Order, Integer> orderIdColumn;
    @FXML
    public TableColumn<Order, Integer> customerIdColumn;
    @FXML
    public TableColumn<Order, String> customerNameColumn;
    @FXML
    public TableColumn<Order, String> productsColumn;
    @FXML
    public TableColumn<Order, Double> orderCostColumn;
    @FXML
    public TableColumn<Order, LocalDate> createdDateColumn;
    @FXML
    public TableColumn<Order, LocalDate> dueDateColumn;
    @FXML
    public Button modifyButton;
    @FXML
    public Button deleteButton;
    @FXML
    public TableView<OrderProduct> orderProductsTableView;
    @FXML
    public TableColumn<OrderProduct, Integer> orderProductsIdColumn;
    @FXML
    public TableColumn<OrderProduct, String> orderProductsNameColumn;
    @FXML
    public TableColumn<OrderProduct, Double> orderProductsPriceColumn;
    @FXML
    public TableColumn<OrderProduct, Double> orderProductsQtyColumn;
    @FXML
    public Button removeProductButton;
    @FXML
    public Label errorLabel;
    @FXML
    public TextField orderSearchField;
    @FXML
    public TextField productSearchField;

    //Products associated with the order
    private ObservableList<OrderProduct> orderProducts = FXCollections.observableArrayList();

    //List of all products in the inventory
    private ObservableList<InventoryProduct> inventoryProducts = FXCollections.observableArrayList();

    //Create confirmation alert
    private final Alert orderFulfilledConfirmation = new Alert(Alert.AlertType.CONFIRMATION, "Was this order fulfilled?", ButtonType.YES, ButtonType.NO);

    //Crete information alert
    private final Alert infoAlert = new Alert(Alert.AlertType.INFORMATION);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setCellFactories();
        setEventListeners();

        //Populate tables after scene loads
        Platform.runLater(() -> new Thread(this::populateTables).start());
    }

    /** Populate tables and combo boxes */
    private void populateTables(){
        ordersTable.setItems(OrderDatabase.getAllOrders());
        inventoryProducts = ProductDatabase.getAllProducts();
        productsTableView.setItems(inventoryProducts);
        customerComboBox.setItems(CustomerDatabase.getAllCustomers());
    }

    /** Set event listeners */
    private void setEventListeners(){

        //Add list change listeners to order and inventory product lists
        orderProducts.addListener((ListChangeListener<Product>) change -> orderProductsTableView.setItems(orderProducts));
        inventoryProducts.addListener((ListChangeListener<Product>) change -> productsTableView.setItems(inventoryProducts));

        //Add Product to order
        addProductButton.setOnAction(e -> addProduct());

        //Remove product from order
        removeProductButton.setOnAction(e -> removeProduct());

        //Create new order
        submitButton.setOnAction(e -> createOrder());

        //Delete order
        deleteButton.setOnAction(e -> {
            Order order = ordersTable.getSelectionModel().getSelectedItem();

            if (order != null){
                showOrderFulfilledConfirmation(order);
            }
        });

        modifyButton.setOnAction(e -> populateFormForUpdate());

        //Search orders
        orderSearchField.setOnKeyPressed(e -> {if (e.getCode().equals(KeyCode.ENTER)){searchOrders();}});

        //Search products in inventory
        productSearchField.setOnKeyPressed(e -> {if (e.getCode().equals(KeyCode.ENTER)){searchProducts();}});

    }

    /** Add Product to Order */
    private void addProduct(){

        //Get the selected inventory product
        InventoryProduct product = productsTableView.getSelectionModel().getSelectedItem();

        //Check that product is not null and in stock
        if (product != null && product.getStock() > 0){

            //Check if the list contains any products with the same id
            if (orderProducts.stream().anyMatch(o -> o.getId() == product.getId())){

                //If a product is found, increment the quantity
                for (OrderProduct orderProduct : orderProducts){
                    if (orderProduct.getId() == product.getId()){
                        orderProduct.incrementQuantity();
                    }
                }
            } else {

                //If a product is not found, add the product
                orderProducts.add(new OrderProduct(product.getId(), product.getName(), product.getProductParts(), 1));
            }

            //Update inventory product stock
            inventoryProducts.stream().filter(inventoryProduct -> inventoryProduct.getId() == product.getId()).forEach(InventoryProduct::decrementStock);
            productsTableView.setItems(inventoryProducts);

        //Display an alert if no selection is made
        } else {
            infoAlert.setTitle("Add Product");
            infoAlert.setHeaderText("No Selection Made");
            infoAlert.setContentText("Please select a Product");
            infoAlert.showAndWait();
        }
    }

    /** Remove Product from Order */
    private void removeProduct(){

        //Get the selected product
        OrderProduct product = orderProductsTableView.getSelectionModel().getSelectedItem();

        //Check if the product is null
        if (product != null){

            //Check if the product quantity is 1
            if (product.getQuantity() == 1){
                //Remove product
                orderProducts.remove(product);

            //If quantity is greater than 1
            } else {
                //Decrement product quantity by 1
                product.decrementQuantity();
            }

            //Update product in list
            inventoryProducts.stream().filter(
                    inventoryProduct -> inventoryProduct.getId() == product.getId())
                    .forEach(InventoryProduct::incrementStock);
        //Display an alert if no selection is made
        } else {
            infoAlert.setTitle("Remove Product");
            infoAlert.setHeaderText("No Selection Made");
            infoAlert.setContentText("Please select a Product");
            infoAlert.showAndWait();
        }
    }

    /** Create new Order */
    private void createOrder(){

        //Validate Input
        if (InputValidator.validateOrderForm(this)){

            //Generate unique order id
            Random random = new Random();
            int id = random.nextInt(1000);
            while (ProductDatabase.getProductById(id) != null) {
                id = random.nextInt();
            }

            //Add order to database
            OrderDatabase.addOrder(new Order(id, customerComboBox.getValue(), orderProducts, LocalDate.now(), dueDatePicker.getValue()));

            //Update table
            ordersTable.setItems(OrderDatabase.getAllOrders());

            //Update part stock
            decreasePartStock(orderProducts);

            //Clear the Order form
            clearForm();
        }
    }

    private void populateFormForUpdate(){

        //Get selected order
        Order order = ordersTable.getSelectionModel().getSelectedItem();

        //Check if a selection was made
        if (order != null){

            //Set the fields
            idField.setText(Integer.toString(order.getOrderID()));
            customerComboBox.getSelectionModel().select(
                    customerComboBox.getItems()
                            .stream()
                            .filter(customer -> customer.getCustomerID() == order.getCustomerID())
                            .collect(Collectors.toList()).get(0));
            dueDatePicker.setValue(order.getDueDate());
            orderProducts = order.getAssociatedProducts();
            orderProductsTableView.setItems(orderProducts);

            //Set submit button text and event
            submitButton.setText("Update Order");
            submitButton.setOnAction(e -> updateOrder(order));
        } else {
            //Show alert
            infoAlert.setTitle("Update Order");
            infoAlert.setHeaderText("No Order Selection Made");
            infoAlert.setContentText("Please select an Order to Update");
            infoAlert.showAndWait();
        }

    }

    private void updateOrder(Order order){

        //Validate input
        if (InputValidator.validateOrderForm(this)){

            //Set new values
            order.setCustomer(customerComboBox.getValue());
            order.setDueDate(dueDatePicker.getValue());
            order.setAssociatedProducts(orderProducts);

            //Update order in database
            OrderDatabase.updateOrder(order);

            //Update table
            ordersTable.setItems(OrderDatabase.getAllOrders());

            //Clear form
            clearForm();

            //Reset submit button text and event
            submitButton.setText("Create New Order");
            submitButton.setOnAction(e -> createOrder());
        }

    }

    /** Create cell factories */
    private void setCellFactories(){

        /*
        Orders Table
         */
        orderIdColumn.setCellValueFactory(new PropertyValueFactory<>("orderID"));
        customerIdColumn.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        customerNameColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        productsColumn.setCellValueFactory(new PropertyValueFactory<>("associatedProductsString"));

        //Wrap Text in table cell
        productsColumn.setCellFactory(tableCell ->{
            TableCell<Order, String> cell = new TableCell<>();
            Text text = new Text();
            cell.setGraphic(text);
            cell.setPrefHeight(Control.USE_COMPUTED_SIZE);
            text.wrappingWidthProperty().bind(productsColumn.widthProperty());
            text.textProperty().bind(cell.itemProperty());
            return cell;
        });
        orderCostColumn.setCellValueFactory(new PropertyValueFactory<>("orderCost"));

        //Format currency
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
        orderCostColumn.setCellFactory(tableCell -> new TableCell<>(){
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

        createdDateColumn.setCellValueFactory(new PropertyValueFactory<>("createdDate"));

        //Format date
        createdDateColumn.setCellFactory(tableCell -> new TableCell<>(){
            @Override
            protected void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                if (empty){
                    setText(null);
                } else {
                    setText(date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                }
            }
        });
        dueDateColumn.setCellValueFactory(new PropertyValueFactory<>("dueDate"));

        //Format date
        dueDateColumn.setCellFactory(tableCell -> new TableCell<>(){
            @Override
            protected void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                if (empty){
                    setText(null);
                } else {
                    setText(date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                }
            }
        });

        /*
        Products Table
         */

        productIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        productNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        productPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        //Set currency format
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

        productStockColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));

        //Disable row and setText to "Out of stock" when the stock of the product is 0
        productStockColumn.setCellFactory(tableCell -> new TableCell<>() {
            @Override
            protected void updateItem(Integer stock, boolean empty) {
                super.updateItem(stock, empty);

                if (empty) {
                    setText(null);
                } else if (stock == 0) {
                    setText("Out of stock");
                } else {
                    setText(stock.toString());
                }
            }
        });

        /*
        Order products table
         */
        orderProductsIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        orderProductsNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        orderProductsPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        //Set currency format
        orderProductsPriceColumn.setCellFactory(tableCell -> new TableCell<>(){
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

        orderProductsQtyColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));


        /*
        Customer combo box
         */
        Callback<ListView<Customer>, ListCell<Customer>> factory = lv -> new ListCell<>() {

            @Override
            protected void updateItem(Customer customer, boolean empty) {
                super.updateItem(customer, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText("ID: " + customer.getCustomerID() + "\n" + "Name: " + customer.getName());
                }
            }
        };
        customerComboBox.setCellFactory(factory);
        customerComboBox.setButtonCell(factory.call(null));

        /*
        Due Date Picker
        Disable days before current day
         */
        dueDatePicker.setDayCellFactory(day -> new DateCell(){
            @Override
            public void updateItem(LocalDate localDate, boolean empty) {
                super.updateItem(localDate, empty);

                if (!empty){
                    if (LocalDate.now().isAfter(localDate)){
                        setDisable(true);
                    }
                }
            }
        });
    }

    /** Clear the form on submit */
    private void clearForm(){
        customerComboBox.getSelectionModel().clearSelection();
        dueDatePicker.setValue(null);
        orderProducts.clear();
        orderProductsTableView.getItems().clear();
    }

    /**
     * Increase Parts stock
     * @param orderProducts the products associated with the Part to modify
     */
    private void increasePartStock(ObservableList<OrderProduct> orderProducts){

        //For each product in the order
        for (OrderProduct orderProduct : orderProducts){
            //For each part in the product
            for (ProductPart productPart : orderProduct.getProductParts()){
                //Reduce increase stock by part quantity multiplied by product quantity
                PartDatabase.increasePartStock(productPart.getId(), (productPart.getQuantity() * orderProduct.getQuantity()));

            }
        }
    }

    /**
     * Decrease Parts stock
     * @param orderProducts the products associated with the Part to modify
     */
    private void decreasePartStock(ObservableList<OrderProduct> orderProducts) {

        //For each product in the order
        for (OrderProduct orderProduct : orderProducts) {
            //For each part in the product
            for (ProductPart productPart : orderProduct.getProductParts()) {
                //Decrease part stock by the part quantity multiplied by the product quantity
                PartDatabase.decreasePartStock(productPart.getId(), (productPart.getQuantity() * orderProduct.getQuantity()));
            }

        }
    }

    /**
     * Display a dialog to confirm the order was fulfilled when removed
     * @param order the order to remove
     */
    private void showOrderFulfilledConfirmation(Order order){

        //Set confirmation content
        orderFulfilledConfirmation.setTitle("Order Fulfilled Confirmation");
        orderFulfilledConfirmation.setHeaderText("Order Fulfilled Confirmation");
        orderFulfilledConfirmation.setContentText("Was this order fulfilled?");
        orderFulfilledConfirmation.showAndWait().ifPresent(response -> {
            if (response.equals(ButtonType.YES)){

                //Remove the Order from the database
                OrderDatabase.removeOrder(order.getOrderID());

                //Update the Orders table
                ordersTable.setItems(OrderDatabase.getAllOrders());
            } else if (response.equals(ButtonType.NO)){

                //Replace Part stock
                increasePartStock(order.getAssociatedProducts());

                //Update Products table
                productsTableView.setItems(ProductDatabase.getAllProducts());
                inventoryProducts = ProductDatabase.getAllProducts();

                //Remove Order from database
                OrderDatabase.removeOrder(order.getOrderID());

                //Update Orders table
                ordersTable.setItems(OrderDatabase.getAllOrders());
            }
        });
    }

    /** Search Orders and set results to Order Table */
    private void searchOrders(){
        ordersTable.setItems(OrderDatabase.searchOrders(orderSearchField.getText().trim()));
    }

    /** Search Products and set results to Products Table */
    private void searchProducts(){

        //Get search criteria
        String searchCriteria = productSearchField.getText();

        //Set products table items to result
        productsTableView.setItems(inventoryProducts.stream()
                .filter(inventoryProduct -> Integer.toString(inventoryProduct.getId()).contains(searchCriteria) || inventoryProduct.getName().contains(searchCriteria))
                .collect(Collectors.toCollection(FXCollections::observableArrayList)));
    }

}
