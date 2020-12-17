package Controller;

import Database.CustomerDatabase;
import Database.OrderDatabase;
import Database.PartDatabase;
import Database.ProductDatabase;
import Model.*;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.util.Callback;

import java.net.URL;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class ReportsController implements Initializable {

    @FXML
    public TableView<Order> orderTable;
    @FXML
    public TableColumn<Order, Integer> orderIdColumn;
    @FXML
    public TableColumn<Order, String> customerNameColumn;
    @FXML
    public TableColumn<Order, Integer> customerIdColumn;
    @FXML
    public TableColumn<Order, String> orderProductsColumn;
    @FXML
    public TableColumn<Order, Double> orderCostColumn;
    @FXML
    public TableColumn<Order, LocalDate> orderDueDateColumn;
    @FXML
    public RadioButton orderProductRadioButton;
    @FXML
    public ComboBox<InventoryProduct> orderProductComboBox;
    @FXML
    public RadioButton orderPartRadioButton;
    @FXML
    public ComboBox<InventoryPart> orderPartComboBox;
    @FXML
    public RadioButton orderCustomerRadioButton;
    @FXML
    public ComboBox<Customer> orderCustomerComboBox;
    @FXML
    public Button generateOrderReportButton;
    @FXML
    public Label errorLabel;
    @FXML
    public Label reportTitleLabel;
    @FXML
    public Label reportTimestampLabel;

    //Create order toggle group
    private final ToggleGroup orderToggleGroup = new ToggleGroup();

    //Create report timestamp format
    private final DateTimeFormatter reportTimestampFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd 'at' hh:mm:ss");


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setButtonEvents();
        createRadioGroups();
        setRadioButtonEvents();
        setComboBoxCellFactories();
        setTableCellFactories();
        populateComboBoxes();
    }

    /** Create radio toggle groups */
    private void createRadioGroups(){

        //Add order radio buttons to toggle group
        orderPartRadioButton.setToggleGroup(orderToggleGroup);
        orderProductRadioButton.setToggleGroup(orderToggleGroup);
        orderCustomerRadioButton.setToggleGroup(orderToggleGroup);

        //Select first radio button
        orderProductRadioButton.setSelected(true);
        orderPartComboBox.setDisable(true);
        orderCustomerComboBox.setDisable(true);

    }

    /** Set radio button events */
    private void setRadioButtonEvents(){

        //Set product report event
        orderProductRadioButton.setOnAction(actionEvent -> {
            if (orderProductRadioButton.isSelected()){
                orderProductComboBox.setDisable(false);
                orderPartComboBox.setDisable(true);
                orderCustomerComboBox.setDisable(true);
                orderPartComboBox.getSelectionModel().clearSelection();
                orderCustomerComboBox.getSelectionModel().clearSelection();
            }
        });

        //Set part report event
        orderPartRadioButton.setOnAction(actionEvent -> {
            if (orderPartRadioButton.isSelected()){
                orderProductComboBox.setDisable(true);
                orderPartComboBox.setDisable(false);
                orderCustomerComboBox.setDisable(true);
                orderProductComboBox.getSelectionModel().clearSelection();
                orderCustomerComboBox.getSelectionModel().clearSelection();
            }
        });

        //Set customer report event
        orderCustomerRadioButton.setOnAction(actionEvent -> {
            if (orderCustomerRadioButton.isSelected()){
                orderProductComboBox.setDisable(true);
                orderPartComboBox.setDisable(true);
                orderCustomerComboBox.setDisable(false);
                orderPartComboBox.getSelectionModel().clearSelection();
                orderProductComboBox.getSelectionModel().clearSelection();
            }
        });
    }

    /** Set button events */
    private void setButtonEvents(){

        //Set event for Orders Generate Report button
        generateOrderReportButton.setOnAction(e -> {if (InputValidator.validateReportForm(this)) {generateOrderReport();}});
    }

    /** Populate combo boxes */
    private void populateComboBoxes(){
        orderProductComboBox.setItems(ProductDatabase.getAllProducts());
        orderPartComboBox.setItems(PartDatabase.getAllParts());
        orderCustomerComboBox.setItems(CustomerDatabase.getAllCustomers());
    }

    /** Set combo box cell factories */
    private void setComboBoxCellFactories(){

        //Set Cell Factory for orderProductComboBox
        Callback<ListView<InventoryProduct>, ListCell<InventoryProduct>> orderProductFactory = lv -> new ListCell<>(){
            @Override
            protected void updateItem(InventoryProduct inventoryProduct, boolean empty) {
                super.updateItem(inventoryProduct, empty);
                setText(empty ? "" : "ID: " + inventoryProduct.getId() + "\nName: " + inventoryProduct.getName());
            }
        };
        orderProductComboBox.setCellFactory(orderProductFactory);
        orderProductComboBox.setButtonCell(orderProductFactory.call(null));

        //Set Cell Factory for orderPartComboBox
        Callback<ListView<InventoryPart>, ListCell<InventoryPart>> orderPartFactory = lv -> new ListCell<>(){
            @Override
            protected void updateItem(InventoryPart inventoryPart, boolean empty) {
                super.updateItem(inventoryPart, empty);
                setText(empty ? "" : "ID: " + inventoryPart.getId() + "\nName: " + inventoryPart.getName());
            }
        };
        orderPartComboBox.setCellFactory(orderPartFactory);
        orderPartComboBox.setButtonCell(orderPartFactory.call(null));

        //Set Cell Factory for orderCustomerComboBox
        Callback<ListView<Customer>, ListCell<Customer>> orderCustomerFactory = lv -> new ListCell<>(){
            @Override
            protected void updateItem(Customer customer, boolean empty) {
                super.updateItem(customer, empty);
                setText(empty ? "" : "ID: " + customer.getCustomerID() + "\nName: " + customer.getName());
            }
        };
        orderCustomerComboBox.setCellFactory(orderCustomerFactory);
        orderCustomerComboBox.setButtonCell(orderCustomerFactory.call(null));

    }

    /** Set table cell factories */
    private void setTableCellFactories(){
                /*
        Orders Table
         */
        orderIdColumn.setCellValueFactory(new PropertyValueFactory<>("orderID"));
        customerIdColumn.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        customerNameColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        orderProductsColumn.setCellValueFactory(new PropertyValueFactory<>("associatedProductsString"));

        //Wrap Text in table cell
        orderProductsColumn.setCellFactory(tableCell ->{
            TableCell<Order, String> cell = new TableCell<>();
            Text text = new Text();
            cell.setGraphic(text);
            cell.setPrefHeight(Control.USE_COMPUTED_SIZE);
            text.wrappingWidthProperty().bind(orderProductsColumn.widthProperty());
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

        orderDueDateColumn.setCellValueFactory(new PropertyValueFactory<>("dueDate"));

        //Format date
        orderDueDateColumn.setCellFactory(tableCell -> new TableCell<>(){
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
    }

    /** Generate report */
    private void generateOrderReport(){

        //Create list to store report results
        ObservableList<Order> reportOrderResult;

        //Populate table with product report
        if (orderProductRadioButton.isSelected()){

            //Get the result from the database
            reportOrderResult = OrderDatabase.getProductOrders(orderProductComboBox.getValue().getId());

            //If the list is empty, set the table placeholder
            if (reportOrderResult.size() == 0){
                orderTable.setPlaceholder(new Text("There are no Orders associated with this Product"));
            }

            //Set the items to the table
            orderTable.setItems(reportOrderResult);

            reportTitleLabel.setText("Orders Associated with Product ID: " + orderProductComboBox.getValue().getId());
            reportTimestampLabel.setText(LocalDateTime.now().format(reportTimestampFormat));

        //Populate table with part report
        } else if (orderPartRadioButton.isSelected()){

            //Get the result from the database
            reportOrderResult = OrderDatabase.getPartOrders(orderPartComboBox.getValue().getId());

            //If the list is empty, set the table placeholder
            if (reportOrderResult.size() == 0){
                orderTable.setPlaceholder(new Text("There are no Orders associated with this Part"));
            }

            //Set the items to the table
            orderTable.setItems(reportOrderResult);

            reportTitleLabel.setText("Orders Associated with Part ID: " + orderPartComboBox.getValue().getId());
            reportTimestampLabel.setText(LocalDateTime.now().format(reportTimestampFormat));

        //Populate table with customer report
        } else if (orderCustomerRadioButton.isSelected()){

            //Get the result from the database
            reportOrderResult = OrderDatabase.getCustomerOrders(orderCustomerComboBox.getValue().getCustomerID());

            //If the list is empty, set the table placeholder
            if (reportOrderResult.size() == 0){
                orderTable.setPlaceholder(new Text("There are no Orders associated with this Customer"));
            }

            //Set the items to the table
            orderTable.setItems(reportOrderResult);

            reportTitleLabel.setText("Orders Associated with Customer ID: " + orderCustomerComboBox.getValue().getCustomerID());
            reportTimestampLabel.setText(LocalDateTime.now().format(reportTimestampFormat));
        }
    }

}
