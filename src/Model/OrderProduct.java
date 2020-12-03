package Model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.ObservableList;

/** Class for managing Products associated with Orders */
public class OrderProduct extends Product{

    private final SimpleIntegerProperty quantity;

    /**
     * Create new Order Product
     * @param id the id to set
     * @param name the name to set
     * @param associatedParts the associated parts to set
     * @param quantity the quantity to set
     */
    public OrderProduct(int id, String name, ObservableList<ProductPart> associatedParts, int quantity){
        super(id, name, associatedParts);
        this.quantity = new SimpleIntegerProperty(quantity);
    }

    /**
     * @return the quantity
     */
    public int getQuantity() {
        return quantity.get();
    }

    /**
     * @param quantity the quantity to set
     */
    public void setQuantity(int quantity) {
        this.quantity.set(quantity);
    }

    /** Increment the quantity by 1 */
    public void incrementQuantity(){
        this.quantity.set(this.quantity.get() + 1);
    }

    /** Decrement the quantity by 1 */
    public void decrementQuantity(){
        this.quantity.set(this.quantity.get() - 1);
    }

    /**
     * @return the quantity property
     */
    public SimpleIntegerProperty quantityProperty() {
        return quantity;
    }
}
