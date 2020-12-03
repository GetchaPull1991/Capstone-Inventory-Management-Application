package Model;

import Database.PartDatabase;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.ObservableList;

/** Class for managing Products in the inventory */
public class InventoryProduct extends Product {

    private final SimpleIntegerProperty stock;

    /**
     * Create new
     * @param id the id to set
     * @param name the name to set
     * @param productParts the associated parts to set
     */
    public InventoryProduct(int id, String name, ObservableList<ProductPart> productParts){
        super(id, name, productParts);
        this.stock = new SimpleIntegerProperty();
        setProductStock();
    }

    /**
     * @return the stock
     */
    public int getStock() {
        return stock.get();
    }

/*    *//**
     * @param stock the stock to set
     *//*
    public void setStock(int stock) {
        this.stock.set(stock);
    }*/

    /**
     * Get the stock of the product
     * @return the stock
     */
    public void setProductStock(){

        //Initialize stock with first product part
        int stock = PartDatabase.getPartById(getProductParts().get(0).getId()).getStock();

        //For each product part
        for (ProductPart part : getProductParts()){

            /*
            The part stock is determined by the stock of the Inventory Part divided
            by the quantity of the Product Part in the product.
            Therefore if there are 12 Inventory Parts available
            and the Product Part requires 2 of the same part,
            only 6 products will be available.
            In the same sense, if the Inventory stock was 13,
            only 6 products would be available.
             */
            int partStock = PartDatabase.getPartById(part.getId()).getStock() / part.getQuantity();

            //If inventory stock is less than stock
            if (stock > partStock){

                //Update stock to inventory stock
                stock = partStock;
            }
        }
        this.stock.set(stock);
    }

    public void setStock(int stock){
        this.stock.set(stock);
    }

    /** Increment the stock by 1 */
    public void incrementStock(){
        this.stock.set(this.stock.get() + 1);
    }

    /** Decrement the stock by 1 */
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
