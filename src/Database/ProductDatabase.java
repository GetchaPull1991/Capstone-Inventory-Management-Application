package Database;

import Model.Part;
import Model.Product;
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
    public static ObservableList<Product> getAllProducts(){

        connect();
        ArrayList<Product> products = new ArrayList<>();

        try(Statement statement = connection.createStatement()){
                ResultSet productResultSet = statement.executeQuery("SELECT * FROM products;");

                while (productResultSet.next()){
                    products.add(new Product(productResultSet.getInt("product_id"),
                            productResultSet.getString("name"),
                            productResultSet.getDouble("price"),
                            productResultSet.getInt("stock"),
                            PartDatabase.getProductParts(productResultSet.getInt("product_id"))
                    ));
                }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        disconnect();
        return FXCollections.observableArrayList(products);
    }

    /**
     * Return a list of Products associated with an order
     * @param orderId the order id to search for
     * @return the list of Products
     */
    public static ObservableList<Product> getOrderProducts(int orderId){

        connect();
        ArrayList<Product> products = new ArrayList<>();


        try(Statement statement = connection.createStatement()) {
            String productIdQuery = "SELECT product_id FROM order_products WHERE order_id = '" + orderId + "';";
            ResultSet productIdResultSet = statement.executeQuery(productIdQuery);

            try (Statement statement1 = connection.createStatement()){
                while (productIdResultSet.next()) {
                    String productQuery = "SELECT * FROM products WHERE product_id = '" + productIdResultSet.getInt("product_id") + "';";
                    ResultSet productResultSet = statement1.executeQuery(productQuery);

                    while (productResultSet.next()){
                        products.add(new Product(productResultSet.getInt("product_id"),
                                productResultSet.getString("name"),
                                productResultSet.getDouble("price"),
                                productResultSet.getInt("stock"),
                                PartDatabase.getProductParts(productResultSet.getInt("product_id"))
                        ));
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        disconnect();
        return FXCollections.observableArrayList(products);
    }

    /**
     * Return a list of Products associated with a part
     * @param partId the Part id to search for
     * @return the list of Products
     */
    public static ObservableList<Product> getPartProducts(int partId){

        connect();
        ObservableList<Product> products = FXCollections.observableArrayList();


        try (Statement statement = connection.createStatement()){

            /*
            Part Products are retrieved with a distinct query to prevent retr
             */
            String productQuery = "SELECT DISTINCT p.product_id, p.name, p.price, p.stock " +
                                    "FROM products p JOIN product_parts pp " +
                                    "ON p.product_id = pp.product_id " +
                                    "WHERE pp.part_id = '" + partId + "';";
            ResultSet productResultSet = statement.executeQuery(productQuery);

            ObservableList<Part> parts = FXCollections.observableArrayList();

            while (productResultSet.next()){
                try(Statement statement1 = connection.createStatement()){
                    String partQuery = "SELECT * FROM parts p JOIN product_parts pp ON p.part_id = pp.part_id WHERE pp.product_id = '" + productResultSet.getInt("product_id") + "';";
                    ResultSet partResultSet = statement1.executeQuery(partQuery);

                    while (partResultSet.next()){
                        parts.add(new Part (partResultSet.getInt("part_id"),
                                partResultSet.getString("name"),
                                partResultSet.getDouble("price"),
                                partResultSet.getInt("stock")));
                    }
                }

                products.add(new Product(productResultSet.getInt("product_id"),
                                        productResultSet.getString("name"),
                                        productResultSet.getDouble("price"),
                                        productResultSet.getInt("stock"),
                                        parts
                ));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return products;
    }

    /**
     * Return product by product id
     * @param productId the id to search for
     * @return the product
     */
    public static Product getProductById(int productId){

        connect();
        Product product = null;

        try(Statement statement = connection.createStatement()){
            ResultSet productResultSet = statement.executeQuery("SELECT * FROM products WHERE product_id = '" + productId + "';");

            while (productResultSet.next()){
                product = new Product(productResultSet.getInt("product_id"),
                        productResultSet.getString("name"),
                        productResultSet.getDouble("price"),
                        productResultSet.getInt("stock"),
                        PartDatabase.getProductParts(productResultSet.getInt("product_id")));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        disconnect();
        return product;
    }

    /**
     * Add Product to the database
     * @param product the product to add
     */
    public static void addProduct(Product product){

        connect();

        for (Part part : product.getAssociatedParts()) {
            try (Statement statement = connection.createStatement()) {
                String associatedPartsQuery = "INSERT INTO product_parts(product_id, part_id) VALUES('" + product.getId() + "', '" + part.getId() + "')";
                statement.executeUpdate(associatedPartsQuery);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }

        try(Statement statement = connection.createStatement()){
            String productInsertQuery =    "INSERT INTO " +
                    "products(product_id, name, price, stock, " +
                    "created_date, created_by, modified_date, modified_by) " +
                    "VALUES" +
                    "('" + product.getId() +  "', '" + product.getName() +
                    "', '" + product.getPrice() + "', '" + product.getStock() +
                    "', '" + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) +
                    "', 'admin', '" + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) +
                    "', 'admin');";

            statement.executeUpdate(productInsertQuery);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Remove Product from database
     * @param productId the product id of the customer to remove
     */
    public static void removeProduct(int productId){
        connect();

        try (Statement statement = connection.createStatement()){

            String removeQuery = "DELETE FROM products WHERE product_id = '" + productId + "';";
            statement.executeUpdate(removeQuery);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        try (Statement statement = connection.createStatement()){

            String removeQuery = "DELETE FROM product_parts WHERE product_id = '" + productId + "';";
            statement.executeUpdate(removeQuery);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        disconnect();
    }

    public static void updateProduct(Product product){
        connect();

        //Clear associated parts from product parts table
        try(Statement statement = connection.createStatement()){
            String deleteQuery = "DELETE FROM product_parts WHERE product_id = '" + product.getId() + "';";
            statement.executeUpdate(deleteQuery);

            //Update associated parts in product parts table
            for (Part part : product.getAssociatedParts()){
                try (Statement statement1 = connection.createStatement()){
                    String associatedPartsQuery = "INSERT INTO product_parts(product_id, part_id) VALUES('" + product.getId() + "', '" + part.getId() + "')";
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
    }
}
