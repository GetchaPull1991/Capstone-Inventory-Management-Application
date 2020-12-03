package Model;

import javafx.beans.property.SimpleIntegerProperty;

public class ProductPart extends Part{

    /*
    This class uses a SimpleIntegerProperty to represent the quantity.
    When represented by a SimpleIntegerProperty and having
    getQuantity() and quantityProperty() methods
    the PropertyValueFactory automatically assigns a listener to the property
    allowing a table to automatically update whenever the objects property is changed
     */
    private final SimpleIntegerProperty quantity;

    public ProductPart(int id, String name, double price, int quantity){
        super(id, name, price);
        this.quantity = new SimpleIntegerProperty(quantity);
    }

    public int getQuantity() {
        return quantity.get();
    }

    public void setQuantity(int quantity) {
        this.quantity.set(quantity);
    }


    public void incrementQuantity(){
        this.quantity.set(quantity.get() + 1);
    }

    public void decrementQuantity(){
        this.quantity.set(quantity.get() -1);
    }

    public SimpleIntegerProperty quantityProperty(){
        return quantity;
    }
}
