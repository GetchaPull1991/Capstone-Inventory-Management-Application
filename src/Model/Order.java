package Model;

import javafx.collections.ObservableList;

import java.time.LocalDate;

/** Clas for creating and managing orders */
public class Order {

    private int orderID;
    private String customerName;
    private int customerID;
    private ObservableList<OrderProduct> associatedProducts;
    private double orderCost;
    private String associatedProductsString;
    private LocalDate createdDate;
    private LocalDate dueDate;

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
        this.customerName = customer.getName();
        this.customerID = customer.getCustomerID();
        this.associatedProducts = associatedProducts;
        calculateOrderCost();
        this.createdDate = createdDate;
        this.dueDate = dueDate;
        setAssociatedProductsString();
    }

    public String getAssociatedProductsString() {
        return associatedProductsString;
    }

    public void setAssociatedProductsString() {

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < associatedProducts.size(); i++){
            builder.append(associatedProducts.get(i).getName());
            builder.append(" x ");
            builder.append(associatedProducts.get(i).getQuantity());
            if (associatedProducts.size() > 1 && i != associatedProducts.size() - 1){
                builder.append("\n");
            }
        }

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
    public ObservableList<OrderProduct> getAssociatedProducts() {
        return associatedProducts;
    }


    /**
     * Set the list of products in the order
     * @param associatedProducts the list to set
     */
    public void setAssociatedProducts(ObservableList<OrderProduct> associatedProducts) {
        this.associatedProducts = associatedProducts;
        StringBuilder builder = new StringBuilder();
        for (Product product : associatedProducts){
            builder.append(product.getName());
            builder.append("\n");
        }
        this.associatedProductsString = builder.toString();
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
