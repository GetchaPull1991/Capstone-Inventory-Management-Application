package Model;

import javafx.beans.property.SimpleIntegerProperty;

/** Class for managing Product Parts */
public class ProductPart extends Part{

    /*
    This class uses a SimpleIntegerProperty to represent the quantity.
    When represented by a SimpleIntegerProperty and having
    getQuantity() and quantityProperty() methods
    the PropertyValueFactory automatically assigns a listener to the property
    allowing a table to automatically update whenever the objects property is changed
     */
    private final SimpleIntegerProperty quantity;

    /**
     * Create new Product Part
     * @param id the id to set
     * @param name the name to set
     * @param price the price to set
     * @param quantity the quantity to set
     */
    public ProductPart(int id, String name, double price, int quantity){
        super(id, name, price);
        this.quantity = new SimpleIntegerProperty(quantity);
    }

    /**
     * Get quantity
     * @return the quantity
     */
    public int getQuantity() {
        return quantity.get();
    }

    /**
     * Set quantity
     * @param quantity the quantity to set
     */
    public void setQuantity(int quantity) {
        this.quantity.set(quantity);
    }

    /** Increment the quantity */
    public void incrementQuantity(){
        this.quantity.set(quantity.get() + 1);
    }

    /** Decrement the quantity */
    public void decrementQuantity(){
        this.quantity.set(quantity.get() -1);
    }

    /**
     * Get quantity property
     * @return the quantity property
     */
    public SimpleIntegerProperty quantityProperty(){
        return quantity;
    }
}
