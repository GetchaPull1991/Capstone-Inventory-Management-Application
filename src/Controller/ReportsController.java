package Controller;

import Model.Order;
import Model.Part;
import Model.Product;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class ReportsController implements Initializable {

    @FXML
    public TableView<Part> partsTable;
    @FXML
    public TableColumn<Part, Integer> partIdColumn;
    @FXML
    public TableColumn<Part, String> partNameColumn;
    @FXML
    public TableColumn<Part, Double> partPriceColumn;
    @FXML
    public TableColumn<Part, Integer> partStockColumn;
    @FXML
    public TableView<Order> orderTable;
    @FXML
    public TableColumn<Order, Integer> orderIDColumn;
    @FXML
    public TableColumn<Order, String> customerName;
    @FXML
    public TableColumn<Order, Integer> customerIdColumn;
    @FXML
    public TableColumn<Order, String> orderProductsColumn;
    @FXML
    public TableColumn<Order, Double> orderCostColumn;
    @FXML
    public TableColumn<Order, LocalDate> orderDueDateColumn;
    @FXML
    public TableView<Product> productsTable;
    @FXML
    public TableColumn<Product, Integer> productIDColumn;
    @FXML
    public TableColumn<Product, String> productNameColumn;
    @FXML
    public TableColumn<Product, Double> productPriceColumn;
    @FXML
    public TableColumn<Product, Integer> productStockColumn;
    @FXML
    public VBox comboVBox;
    @FXML
    public Tab ordersTab;
    @FXML
    public ComboBox<String> searchTypeComboBox;
    @FXML
    public Tab productsTab;
    @FXML
    public Tab partsTab;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }
}
