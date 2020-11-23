package Model;

import javafx.collections.ObservableList;

import java.time.LocalDate;

public class Product {

    private ObservableList<Part> associatedParts;
    private String associatedPartsString;
    private int id;
    private String name;
    private double price;
    private int stock;
    private LocalDate createdDate;

    /**
     * @param id set the id
     * @param name set the name
     * @param price set the price
     * @param stock set the stock
     */
    public Product(int id, String name, double price, int stock, ObservableList<Part> associatedParts){
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.associatedParts = associatedParts;
        this.createdDate = LocalDate.now();
        setAssociatedPartsString();
    }


    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    public String getAssociatedPartsString() {
        return associatedPartsString;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id){
        this.id = id;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name){
        this.name = name;
    }

    /**
     * @param price the price to set
     *
     */
    public void setPrice(double price){
        this.price = price;
    }

    /**
     * @param stock the stock to set
     */
    public void setStock(int stock){
        this.stock = stock;
    }

    /**
     * @param min the min to set
     */
    public void setMin(int min){

    }

    /**
     * @param max the max to set
     */
    public void setMax(int max){

    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the price
     */
    public double getPrice() {
        return price;
    }

    /**
     * @return the stock
     */
    public int getStock() {
        return stock;
    }


    public void setAssociatedParts(ObservableList<Part> associatedParts){
        this.associatedParts = associatedParts;
        setAssociatedPartsString();
    }

    public void setAssociatedPartsString(){

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < associatedParts.size(); i++){
            builder.append(associatedParts.get(i).getName());
            if (associatedParts.size() > 1 && i != associatedParts.size() - 1){
                builder.append("\n");
            }
        }

        this.associatedPartsString = builder.toString();
    }

    public void updateStock(){

        //Set the stock of the product to the lowest stock of any part
        for (Part part : associatedParts){
            if (part.getStock() < this.stock){
                this.stock = part.getStock();
            }
        }
    }

    /**
     * @return the associated parts
     */
    public ObservableList<Part> getAssociatedParts() {
        return associatedParts;
    }

}
