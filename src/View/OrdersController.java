package View;

import Database.CustomerDatabase;
import Database.OrderDatabase;
import Database.PartDatabase;
import Database.ProductDatabase;
import Model.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
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

public class OrdersController implements Initializable {

    @FXML
    public TextField idField;
    @FXML
    public ComboBox<Customer> customerComboBox;
    @FXML
    public Button addProductButton;
    @FXML
    public TableView<Product> productsTableView;
    @FXML
    public TableColumn<Product, Integer> productIdColumn;
    @FXML
    public TableColumn<Product, String> productNameColumn;
    @FXML
    public TableColumn<Product, Double> productPriceColumn;
    @FXML
    public TableColumn<Product, Integer> productStockColumn;
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
    public TableView<Product> orderProductsTableView;
    @FXML
    public TableColumn<Product, Integer> orderProductsIdColumn;
    @FXML
    public TableColumn<Product, String> orderProductsNameColumn;
    @FXML
    public TableColumn<Product, Double> orderProductsPriceColumn;
    @FXML
    public Button removeProductButton;
    @FXML
    public Label errorLabel;
    @FXML
    public TextField orderSearchField;

    ObservableList<Product> orderProducts = FXCollections.observableArrayList();

    ObservableList<Product> allProducts = FXCollections.observableArrayList();

    TableRow<Product> selectedRow = new TableRow<>();

    Alert orderFulfilledConfirmation = new Alert(Alert.AlertType.CONFIRMATION, "Was this order fulfilled?", ButtonType.YES, ButtonType.NO);

    //Final variables to determine whether an order was created or cancelled
    private final int ORDER_CREATED = 0;
    private final int ORDER_CANCELLED = 1;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setCellFactories();
        setButtonEventListeners();
        Platform.runLater(() -> new Thread(() -> {
            ordersTable.setItems(OrderDatabase.getAllOrders());
            allProducts = ProductDatabase.getAllProducts();
            productsTableView.setItems(allProducts);
            customerComboBox.setItems(CustomerDatabase.getAllCustomers());
        }).start());
    }

    private void setButtonEventListeners(){

        //Add Product to order
        addProductButton.setOnAction(e -> {
            Product product = productsTableView.getSelectionModel().getSelectedItem();

            //Check if the product is null or the table row is disabled
            if (product != null && !selectedRow.isDisabled()){

                //Add the product to the order products list and table
                orderProducts.add(product);
                orderProductsTableView.setItems(orderProducts);

                //Reduce the product stock by 1
                product.setStock(product.getStock() - 1);
                productsTableView.getItems().set(productsTableView.getSelectionModel().getSelectedIndex(), product);
            }
        });

        //Remove product from order
        removeProductButton.setOnAction(e -> {
            Product product = orderProductsTableView.getSelectionModel().getSelectedItem();

            //Check if the product is null
            if (product != null){

                //Remove the product from the order products list and table
                orderProducts.remove(product);
                orderProductsTableView.setItems(orderProducts);

                //Return the unused stock to the all products list and table
                for (int i = 0; i < allProducts.size(); i++){
                        if (product.getId() == allProducts.get(i).getId()){
                            product.setStock(product.getStock() + 1);
                            allProducts.set(i, product);
                        }
                }
                productsTableView.setItems(allProducts);
            }
        });

        //Create new order
        submitButton.setOnAction(e -> {
            if (InputValidator.validateOrderForm(this)){

                //Generate unique order id
                Random random = new Random();
                int id = random.nextInt(1000);
                while (ProductDatabase.getProductById(id) != null) {
                    id = random.nextInt();
                }

                //Get customer
                Customer customer = customerComboBox.getValue();

                //Get total cost
                double orderCost = 0;
                for (Product product : orderProducts){
                    orderCost += product.getPrice();
                }

                //Get due date
                LocalDate dueDate = dueDatePicker.getValue();

                //Add order to database
                OrderDatabase.addOrder(new Order(id, customer, orderProducts, orderCost, LocalDate.now(), dueDate));

                updateStock(ORDER_CREATED, orderProducts);

                //Update table
                ordersTable.setItems(OrderDatabase.getAllOrders());

                clearForm();
            }
        });

        //Delete order
        deleteButton.setOnAction(e -> {
            Order order = ordersTable.getSelectionModel().getSelectedItem();

            if (order != null){
                showOrderFulfilledConfirmation(order);
            }
        });

        orderSearchField.setOnKeyPressed(e -> {
            if (e.getCode().equals(KeyCode.ENTER)){
                searchOrders();
            }
        });

    }

    /** Create cell factories */
    private void setCellFactories(){

        /*
        Orders Table
         */
        orderIdColumn.setCellValueFactory(new PropertyValueFactory<>("orderID"));
        customerIdColumn.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        customerNameColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        productsColumn.setCellValueFactory(new PropertyValueFactory<>("productsString"));

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
                    setText(date.format(DateTimeFormatter.ofPattern("MM/dd/yyyy")));
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
                    setText(date.format(DateTimeFormatter.ofPattern("MM/dd/yyyy")));
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
        productStockColumn.setCellFactory(tableCell -> new TableCell<>(){
            @Override
            protected void updateItem(Integer stock, boolean empty) {
                super.updateItem(stock, empty);

                if (empty){
                    setText(null);
                } else if (stock == 0){
                    setText("Out of stock");
                    getTableRow().setDisable(true);
                    selectedRow = getTableRow();
                    selectedRow.setDisable(true);
                } else {
                    setText(stock.toString());
                    getTableRow().setDisable(false);
                    selectedRow = getTableRow();
                    selectedRow.setDisable(false);
                }
            }
        });

        /*
        Disable row if stock == 0
         */
/*        productsTableView.setRowFactory(row -> {
            return new TableRow<>() {
                @Override
                protected void updateItem(Product product, boolean empty) {
                    super.updateItem(product, empty);
                    if (!empty) {
                        setDisable(product.getStock() <= 0);
                    }
                }
            };
        });*/

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
     * Update the stock of products and parts
     * @param actionId the id of the action (ORDER_CANCELLED or ORDER_CREATED)
     * @param orderProducts the list of products to update (Parts are a property of product)
     */
    private void updateStock(int actionId, ObservableList<Product> orderProducts){

        /*
        For each product in the order:
            For each part associated with the product:
                Decrease the stock of the part by 1
                Update the part in the database
         */
        for (Product product : orderProducts){
            for (Part part : product.getAssociatedParts()){
                if (actionId == 0) {
                    part.setStock(part.getStock() - 1);
                } else {
                    part.setStock(part.getStock() + 1);
                }
                PartDatabase.updatePart(part);
            }

            product.updateStock();
            ProductDatabase.updateProduct(product);
            productsTableView.setItems(ProductDatabase.getAllProducts());
        }
    }

    /**
     * Display a dialog to confirm the order was fulfilled when removed
     * @param order the order to remove
     */
    private void showOrderFulfilledConfirmation(Order order){


        /*
        BUG: Only one product is replaced in stock due to select distinct from database
         */
        orderFulfilledConfirmation.setTitle("Order Fulfilled Confirmation");
        orderFulfilledConfirmation.setHeaderText("Order Fulfilled Confirmation");
        orderFulfilledConfirmation.setContentText("Was this order fulfilled?");
        orderFulfilledConfirmation.showAndWait().ifPresent(response -> {
            if (response.equals(ButtonType.YES)){
                OrderDatabase.removeOrder(order.getOrderID());
                ordersTable.setItems(OrderDatabase.getAllOrders());
            } else if (response.equals(ButtonType.NO)){
                updateStock(ORDER_CANCELLED, order.getProducts());
                productsTableView.setItems(ProductDatabase.getAllProducts());
                OrderDatabase.removeOrder(order.getOrderID());
                ordersTable.setItems(OrderDatabase.getAllOrders());
            }
        });
    }

    private void searchOrders(){

        String searchCriteria = orderSearchField.getText();
        ObservableList<Order> searchResult = FXCollections.observableArrayList();

        if (!searchCriteria.trim().equals("")) {
            for (Order order : OrderDatabase.getAllOrders()) {
                if (Integer.toString(order.getOrderID()).contains(searchCriteria) ||
                        Integer.toString(order.getCustomerID()).contains(searchCriteria) ||
                        order.getCustomerName().contains(searchCriteria) ||
                        order.getCreatedDate().format(DateTimeFormatter.ofPattern("MM/dd/yyyy")).contains(searchCriteria) ||
                        order.getDueDate().format(DateTimeFormatter.ofPattern("MM/dd/yyyy")).contains(searchCriteria)) {
                    searchResult.add(order);
                }
            }

            ordersTable.setItems(searchResult);
        } else {
            ordersTable.setItems(OrderDatabase.getAllOrders());
        }

    }

}
