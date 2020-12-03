package Model;

import javafx.collections.ObservableList;

import java.time.LocalDate;

public abstract class Product {

    private ObservableList<ProductPart> productParts;
    private String productPartsString;
    private int id;
    private String name;
    private double price;
    private LocalDate createdDate;

    /**
     * @param id set the id
     * @param name set the name
     */
    public Product(int id, String name, ObservableList<ProductPart> productParts){
        this.id = id;
        this.name = name;
        this.productParts = productParts;
        this.createdDate = LocalDate.now();
        setProductPartsString();
        setProductPrice();
    }


    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    public String getProductPartsString() {
        return productPartsString;
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

    public void setProductParts(ObservableList<ProductPart> productParts){
        this.productParts = productParts;
        setProductPartsString();
    }

    public void setProductPartsString(){

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < productParts.size(); i++){
            builder.append(productParts.get(i).getName());
            builder.append(" x ");
            builder.append(productParts.get(i).getQuantity());
            if (productParts.size() > 1 && i != productParts.size() - 1){
                builder.append("\n");
            }
        }

        this.productPartsString = builder.toString();
    }


    /**
     * @return the associated parts
     */
    public ObservableList<ProductPart> getProductParts() {
        return productParts;
    }

    public void setProductPrice(){
        double price = 0;

        for (ProductPart productPart : productParts){
            price += productPart.getPrice() * productPart.getQuantity();
        }

        this.price = price * 1.32;
    }

}
