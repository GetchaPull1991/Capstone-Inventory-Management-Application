package Model;

/** Class for managing Inventory Parts */
public class InventoryPart extends Part {

    private int stock;

    /**
     * Create new Inventory Part
     * @param id the id to set
     * @param name the name to set
     * @param price the price to set
     * @param stock the stock to set
     */
    public InventoryPart(int id, String name, double price, int stock){
        super(id, name, price);
        this.stock = stock;
    }

    /**
     * Get stock
     * @return the stock
     */
    public int getStock() {
        return stock;
    }

    /**
     * Set stock
     * @param stock the stock to set
     */
    public void setStock(int stock) {
        this.stock = stock;
    }

    /** Increment the Inventory Part stock */
    public void incrementStock(){
        this.stock +=1;
    }

    /** Decrement the Inventory Part stock */
    public void decrementStock(){
        this.stock -=1;
    }
}
