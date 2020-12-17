package Database;

import Controller.MainBorderPaneController;
import Model.InventoryProduct;
import Model.OrderProduct;
import Model.ProductPart;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/** Class for managing all database operations involving Products */
public class ProductDatabase extends Database {

    /**
     * Return a list of all Products
     * @return the list of Products
     */
    public static ObservableList<InventoryProduct> getAllProducts(){
        updateProductStock();
        //Connect to the database
        connect();

        //Create list of products
        ObservableList<InventoryProduct> products = FXCollections.observableArrayList();

        //Retrieve products
        try(Statement statement = connection.createStatement()){
                ResultSet productResultSet = statement.executeQuery("SELECT * FROM products;");
                //Add Products to list
                while (productResultSet.next()){
                    products.add(new InventoryProduct(productResultSet.getInt("product_id"),
                            productResultSet.getString("name"),
                            PartDatabase.getProductParts(productResultSet.getInt("product_id")),
                            productResultSet.getInt("stock")));
                }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        //Disconnect from the database
        disconnect();

        //Return products list
        return products;
    }

    /**
     * Return a list of Products associated with an order
     * @param orderId the order id to search for
     * @return the list of Products
     */
    public static ObservableList<OrderProduct> getOrderProducts(int orderId){

        //Connect to database
        connect();

        //Create OrderProduct list
        ObservableList<OrderProduct> orderProducts = FXCollections.observableArrayList();

        //Get the product id and product quantity associated with the order
        try(Statement statement = connection.createStatement()) {
            String productIdQuery = "SELECT product_id, quantity FROM order_products WHERE order_id = '" + orderId + "';";
            ResultSet productIdResultSet = statement.executeQuery(productIdQuery);

            //For each product id asociated with the order
            try (Statement statement1 = connection.createStatement()){
                while (productIdResultSet.next()) {
                    String productQuery = "SELECT * FROM products WHERE product_id = '" + productIdResultSet.getInt("product_id") + "';";
                    ResultSet productResultSet = statement1.executeQuery(productQuery);

                    //Get all the products associated with the order
                    while (productResultSet.next()){
                        orderProducts.add(new OrderProduct(productResultSet.getInt("product_id"),
                                productResultSet.getString("name"),
                                PartDatabase.getProductParts(productResultSet.getInt("product_id")),
                                productIdResultSet.getInt("quantity")));
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        //Disconnect from the database
        disconnect();

        //Return the OrderProducts
        return orderProducts;
    }

    /**
     * Return a list of Products associated with a part
     * @param partId the Part id to search for
     * @return the list of Products
     */
    public static ObservableList<InventoryProduct> getPartProducts(int partId){
        //Connect to the database
        connect();

        //Create part products list
        ObservableList<InventoryProduct> partProducts = FXCollections.observableArrayList();

        //Get all products associated with the part
        try (Statement statement = connection.createStatement()){
            String productQuery = "SELECT *" +
                                    "FROM products p JOIN product_parts pp " +
                                    "ON p.product_id = pp.product_id " +
                                    "WHERE pp.part_id = '" + partId + "';";
            ResultSet productResultSet = statement.executeQuery(productQuery);

            //For each product associated with the part
            while (productResultSet.next()){

                //Create products parts list
                ObservableList<ProductPart> productParts = FXCollections.observableArrayList();

                //Get all parts associated with the product
                try(Statement statement1 = connection.createStatement()){
                    String partQuery = "SELECT pp.quantity, p.part_id, p.name, p.price " +
                                       "FROM parts p JOIN product_parts pp ON p.part_id = pp.part_id " +
                                       "WHERE pp.product_id ='" + productResultSet.getInt("product_id") + "';";
                    ResultSet partResultSet = statement1.executeQuery(partQuery);

                    //For each part associated with the product
                    while (partResultSet.next()){

                        //Add the part to the product parts list
                        productParts.add(new ProductPart(partResultSet.getInt("part_id"),
                                partResultSet.getString("name"),
                                partResultSet.getDouble("price"),
                                partResultSet.getInt("quantity")));
                    }
                }

                //Add the product to the part products list
                partProducts.add(new InventoryProduct(productResultSet.getInt("product_id"),
                                        productResultSet.getString("name"),
                                        productParts, productResultSet.getInt("stock")));

            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        //Disconnect from the database
        disconnect();

        //Return the list of part products
        return partProducts;
    }

    /**
     * Return product by product id
     * @param productId the id to search for
     * @return the product
     */
    public static InventoryProduct getProductById(int productId){

        //Connect to the database
        connect();

        //Create Inventory Product
        InventoryProduct product = null;

        //Get the product with the provided product id
        try(Statement statement = connection.createStatement()){
            ResultSet productResultSet = statement.executeQuery("SELECT * FROM products WHERE product_id = '" + productId + "';");

            //Set the product to the product returned
            while (productResultSet.next()){
                product = new InventoryProduct(productResultSet.getInt("product_id"),
                        productResultSet.getString("name"),
                        PartDatabase.getProductParts(productResultSet.getInt("product_id")), productResultSet.getInt("stock"));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        //Disconnect from the database
        disconnect();

        //Return the product
        return product;
    }

    /**
     * Add Product to the database
     * @param product the product to add
     */
    public static void addProduct(InventoryProduct product){

        //Connect to the database
        connect();

        //Add a product part association for each associated part
        for (ProductPart part : product.getProductParts()) {
            try (Statement statement = connection.createStatement()) {
                String associatedPartsQuery = "INSERT INTO product_parts(product_id, part_id, quantity) " +
                                              "VALUES('" + product.getId() +
                                              "', '" + part.getId() +
                                              "' ,'" + part.getQuantity() + "')";
                statement.executeUpdate(associatedPartsQuery);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }

        //Insert the new product into the database
        try(Statement statement = connection.createStatement()){
            String productInsertQuery =    "INSERT INTO " +
                    "products(product_id, name, price, stock, " +
                    "created_date, created_by, modified_date, modified_by) " +
                    "VALUES" +
                    "('"   + product.getId() + "', '" + product.getName() +
                    "', '" + product.getPrice() + "', '" + product.getStock() +
                    "', '" + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) +
                    "', '" + MainBorderPaneController.currentUser.getUsername() +
                    "', '" + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) +
                    "', '" + MainBorderPaneController.currentUser.getUsername() + "');";
            statement.executeUpdate(productInsertQuery);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //Disconnect from the database
        disconnect();

        updateProductStock();
    }

    /** Update the product based on the lowest stock of a part divided by its quantity */
    public static void updateProductStock(){

        //Connect to the database
        connect();

        //Create product id list
        ArrayList<Integer> productIdList = new ArrayList<>();

        //Retrieve product ids
        try(Statement statement = connection.createStatement()){
            ResultSet productIdResult = statement.executeQuery("SELECT product_id FROM products;");

            //Add ids to list
            while(productIdResult.next()){
                productIdList.add(productIdResult.getInt("product_id"));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        //For each id
        for (Integer productId : productIdList) {

            //Set the product stock to the value of the part stock divided by the part quantity
            try (Statement statement = connection.createStatement()) {
                statement.executeUpdate("UPDATE products pro " +
                        "SET pro.stock = (SELECT MIN(p.stock DIV pp.quantity)  " +
                                         "FROM parts p JOIN product_parts pp ON p.part_id = pp.part_id " +
                                         "WHERE product_id = '" + productId + "') " +
                        "WHERE pro.product_id = '" + productId + "';");
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }

        //Disconnect from the database
        disconnect();
    }

    /**
     * Remove Product from database
     * @param productId the product id of the customer to remove
     */
    public static void removeProduct(int productId){

        //Connect to the database
        connect();

        //Delete product
        try (Statement statement = connection.createStatement()){

            String removeQuery = "DELETE FROM products WHERE product_id = '" + productId + "';";
            statement.executeUpdate(removeQuery);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        //Delete product part association
        try (Statement statement = connection.createStatement()){

            String removeQuery = "DELETE FROM product_parts WHERE product_id = '" + productId + "';";
            statement.executeUpdate(removeQuery);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        //Disconnect from the database
        disconnect();
    }

    /**
     * Update Product
     * @param product the Product to update
     */
    public static void updateProduct(InventoryProduct product){

        //Connect to the database
        connect();

        //Clear associated parts from product parts table
        try(Statement statement = connection.createStatement()){
            String deleteQuery = "DELETE FROM product_parts WHERE product_id = '" + product.getId() + "';";
            statement.executeUpdate(deleteQuery);

            //Update associated parts in product parts table
            for (ProductPart part : product.getProductParts()){
                try (Statement statement1 = connection.createStatement()){
                    String associatedPartsQuery = "INSERT INTO product_parts(product_id, part_id, quantity) " +
                                                  "VALUES('" + product.getId() +
                                                  "', '" + part.getId() + "', '" + part.getQuantity() + "');";
                    statement1.executeUpdate(associatedPartsQuery);
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        //Update product
        try (Statement statement = connection.createStatement()){
            String updateQuery = "UPDATE products SET name = '" + product.getName() +
                    "', price = '" + product.getPrice() + "', stock = '" +
                    product.getStock() + "' WHERE product_id = '" + product.getId() + "';";
            statement.executeUpdate(updateQuery);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        //Disconnect from database
        disconnect();

        //Update Product stock
        updateProductStock();
    }

    /**
     * Search Products
     * @param searchCriteria the search criteria to search with
     * @return the list of Products matching the search criteria
     */
    public static ObservableList<InventoryProduct> searchInventoryProducts(String searchCriteria){

        //Connect to the database
        connect();

        //Create list of inventory products
        ObservableList<InventoryProduct> inventoryProducts = FXCollections.observableArrayList();

        //Get products matching search criteria
        try(Statement statement = connection.createStatement()){
            String searchQuery = "SELECT pp.quantity, pro.product_id, pro.name, p.name, p.part_id FROM products pro " +
                                 "JOIN product_parts pp ON pro.product_id = pp.product_id " +
                                 "JOIN parts p ON p.part_id = pp.part_id  " +
                                 "WHERE " +
                                 "pro.product_id LIKE '%" + searchCriteria + "%' OR " +
                                 "pro.name LIKE '%" + searchCriteria + "%' OR " +
                                 "p.name LIKE '%" + searchCriteria + "%' OR " +
                                 "p.part_id LIKE '%" + searchCriteria + "%';";
            ResultSet productResult = statement.executeQuery(searchQuery);

            //Add products to list
            while(productResult.next()){
                ObservableList<ProductPart> productParts = PartDatabase.getProductParts(productResult.getInt("product_id"));
                int stock = getProductStock(productParts);

                inventoryProducts.add(new InventoryProduct(productResult.getInt("product_id"),
                        productResult.getString("name"), productParts, stock));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        //Disconnect from the database
        disconnect();

        //Return the list of products
        return inventoryProducts;
    }

    private static int getProductStock(ObservableList<ProductPart> productParts) {

        //Initialize stock with first product part
        int stock = PartDatabase.getPartById(productParts.get(0).getId()).getStock();

        //For each product part
        for (ProductPart part : productParts) {

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
            if (stock > partStock) {

                //Update stock to inventory stock
                stock = partStock;
            }
        }

        return stock;
    }
}
