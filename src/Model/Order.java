package Model;

import javafx.collections.ObservableList;

import java.time.LocalDate;

/** Class for creating and managing orders */
public class Order {

    private int orderID;
    private String customerName;
    private int customerID;
    private ObservableList<OrderProduct> associatedProducts;
    private double orderCost;
    private String associatedProductsString;
    private LocalDate createdDate;
    private LocalDate dueDate;
    private Customer customer;

    /**
     * Create a new order object
     * @param orderID The order id to set
     * @param customer The customer of the order to set
     * @param associatedProducts The list of products in the order to set
     * @param createdDate The date the order was made to set
     * @param dueDate The date the order is due to set
     */
    public Order(int orderID, Customer customer, ObservableList<OrderProduct> associatedProducts, LocalDate createdDate, LocalDate dueDate){
        this.orderID = orderID;
        this.customer = customer;
        this.customerName = customer.getName();
        this.customerID = customer.getCustomerID();
        this.associatedProducts = associatedProducts;
        calculateOrderCost();
        this.createdDate = createdDate;
        this.dueDate = dueDate;
        setAssociatedProductsString();
    }

    /**
     * Get associated parts string
     * @return the associated parts string
     */
    public String getAssociatedProductsString() {
        return associatedProductsString;
    }

    /** Set the associated parts string */
    public void setAssociatedProductsString() {

        //Create associated parts string
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < associatedProducts.size(); i++){
            builder.append(associatedProducts.get(i).getName());
            builder.append(" x ");
            builder.append(associatedProducts.get(i).getQuantity());
            if (associatedProducts.size() > 1 && i != associatedProducts.size() - 1){
                builder.append("\n");
            }
        }

        //Set associated parts string
        this.associatedProductsString = builder.toString();
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

    public Customer getCustomer() {
        return customer;
    }

    /**
     * Get customer id
     * @return the customer id
     */
    public int getCustomerID() {
        return customerID;
    }

    /**
     * Get customer name
     * @return the customer name
     */
    public String getCustomerName() {
        return customerName;
    }

    /**
     * Set the Customer
     * @param customer the customer to set
     */
    public void setCustomer(Customer customer){
        this.customer = customer;
    }

    /**
     * Get the order cost
     * @return the order cost
     */
    public double getOrderCost() {
        return orderCost;
    }

    /**
     * Get the list of products in the order
     * @return the list
     */
    public ObservableList<OrderProduct> getAssociatedProducts() {
        return associatedProducts;
    }


    /**
     * Set the list of products in the order
     * @param associatedProducts the list to set
     */
    public void setAssociatedProducts(ObservableList<OrderProduct> associatedProducts) {
        this.associatedProducts = associatedProducts;
        calculateOrderCost();
        setAssociatedProductsString();
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

    /**
     * Get the due date
     * @return the due date
     */
    public LocalDate getDueDate() {
        return dueDate;
    }

    /**
     * Set the due date
     * @param dueDate the due date to set
     */
    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    /** Calculate the order cost */
    private void calculateOrderCost(){

        //Initialize cost
        double cost = 0;

        //Multiple product price by quantity and sum cost
        for (OrderProduct product : this.associatedProducts){
            cost += (product.getPrice() * product.getQuantity());
        }

        //Set order cost
        this.orderCost = cost;
    }
}
