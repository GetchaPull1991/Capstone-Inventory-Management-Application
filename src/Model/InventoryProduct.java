package Model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.ObservableList;

/** Class for managing Inventory Products */
public class InventoryProduct extends Product {

    private final SimpleIntegerProperty stock;

    /**
     * Create new
     * @param id the id to set
     * @param name the name to set
     * @param productParts the associated parts to set
     */
    public InventoryProduct(int id, String name, ObservableList<ProductPart> productParts, int stock){
        super(id, name, productParts);
        this.stock = new SimpleIntegerProperty(stock);
    }

    /**
     * @return the stock
     */
    public int getStock() {
        return stock.get();
    }

    /**
     * Set stock
     * @param stock the stock to set
     */
    public void setStock(int stock){
        this.stock.set(stock);
    }

    /** Increment the stock */
    public void incrementStock(){
        this.stock.set(this.stock.get() + 1);
    }

    /** Decrement the stock */
    public void decrementStock(){
        this.stock.set(this.stock.get() - 1);
    }

    /**
     * @return the stock property
     */
    public SimpleIntegerProperty stockProperty() {
        return stock;
    }
}
