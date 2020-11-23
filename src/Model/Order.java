package Model;

import javafx.collections.ObservableList;

import java.time.LocalDate;

/** Clas for creating and managing orders */
public class Order {

    private int orderID;
    private String customerName;
    private int customerID;
    private ObservableList<Product> products;
    private double orderCost;
    private String productsString;
    private LocalDate createdDate;
    private LocalDate dueDate;

    /**
     * Create a new order object
     * @param orderID The order id to set
     * @param customer The customer of the order to set
     * @param products The list of products in the order to set
     * @param orderCost The cost of the order to set
     * @param createdDate The date the order was made to set
     * @param dueDate The date the order is due to set
     */
    public Order(int orderID, Customer customer, ObservableList<Product> products, Double orderCost, LocalDate createdDate, LocalDate dueDate){
        this.orderID = orderID;
        this.customerName = customer.getName();
        this.customerID = customer.getCustomerID();
        this.products = products;
        this.orderCost = orderCost;
        this.createdDate = createdDate;
        this.dueDate = dueDate;

        StringBuilder builder = new StringBuilder();
        for (Product product : products){
            builder.append(product.getName());
            builder.append("\n");
        }
        this.productsString = builder.toString();
    }

    public String getProductsString() {
        return productsString;
    }


    /**
     * Get the id of the order
     * @return the id
     */
    public int getOrderID() {
        return orderID;
    }

    /**
     * Set the id of the order
     * @param orderID the id to set
     */
    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public double getOrderCost() {
        return orderCost;
    }

    public void setOrderCost(double orderCost) {
        this.orderCost = orderCost;
    }

    /**
     * Get the list of products in the order
     * @return the list
     */
    public ObservableList<Product> getProducts() {
        return products;
    }


    /**
     * Set the list of products in the order
     * @param products the list to set
     */
    public void setProducts(ObservableList<Product> products) {
        this.products = products;
        StringBuilder builder = new StringBuilder();
        for (Product product : products){
            builder.append(product.getName());
            builder.append("\n");
        }
        this.productsString = builder.toString();
    }

    /**
     * Get the date the order was placed
     * @return the date
     */
    public LocalDate getCreatedDate() {
        return createdDate;
    }

    /**
     * Set the date of the order
     * @param createdDate the date to set
     */
    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }
}
