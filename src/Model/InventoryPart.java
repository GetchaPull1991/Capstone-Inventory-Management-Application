package Model;

public class InventoryPart extends Part {

    private int stock;

    public InventoryPart(int id, String name, double price, int stock){
        super(id, name, price);
        this.stock = stock;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    private void incrementStock(){
        this.stock +=1;
    }

    private void decrementStock(){
        this.stock -=1;
    }
}
