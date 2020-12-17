package Model;

import Controller.ProductsController;
import javafx.collections.ObservableList;

import java.time.LocalDate;

/** Abstract Part Class */
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
     * @param productParts the list of parts to set
     */
    public Product(int id, String name, ObservableList<ProductPart> productParts){
        this.id = id;
        this.name = name;
        this.productParts = productParts;
        this.createdDate = LocalDate.now();
        setProductPartsString();
        setProductPrice();
    }

    /**
     * Get created date
     * @return the created date
     */
    public LocalDate getCreatedDate() {
        return createdDate;
    }

    /**
     * Set created date
     * @param createdDate the created date to set
     */
    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    /**
     * Get product parts string
     * @return the product parts string
     */
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
     * Set the product parts
     * @param productParts the product parts to set
     */
    public void setProductParts(ObservableList<ProductPart> productParts){
        this.productParts = productParts;
        setProductPartsString();
    }

    /** Set the product parts string */
    public void setProductPartsString(){

        //Create product parts string
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < productParts.size(); i++){
            builder.append(productParts.get(i).getName());
            builder.append(" x ");
            builder.append(productParts.get(i).getQuantity());
            if (productParts.size() > 1 && i != productParts.size() - 1){
                builder.append("\n");
            }
        }

        //Set product parts string
        this.productPartsString = builder.toString();
    }

    /**
     * Get Product Parts
     * @return the associated parts
     */
    public ObservableList<ProductPart> getProductParts() {
        return productParts;
    }

    /** Set Product Price */
    public void setProductPrice(){

        //Initialize price to 0
        double price = 0;

        //Sum price of product parts * quantity
        for (ProductPart productPart : productParts){
            price += productPart.getPrice() * productPart.getQuantity();
        }

        //Set price to sum of parts * manufacturing fee (1.32)
        this.price = price * ProductsController.MANUFACTURING_FEE;
    }

}
