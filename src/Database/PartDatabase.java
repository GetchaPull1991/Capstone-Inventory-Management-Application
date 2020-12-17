package Database;

import Model.InventoryPart;
import Model.ProductPart;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/** Class for managing all database operations involving Parts */
public class PartDatabase extends Database{

    /**
     * Return a list of all Parts
     * @return the list of Parts
     */
    public static ObservableList<InventoryPart> getAllParts(){
        //Connect to the database
        connect();

        //Create list of parts
        ObservableList<InventoryPart> parts = FXCollections.observableArrayList();

        //Retrieve Parts
        try(Statement statement = connection.createStatement()){
            ResultSet partResult = statement.executeQuery("SELECT * FROM parts;");

            //Add Parts to list
            while (partResult.next()){
                parts.add(new InventoryPart(partResult.getInt("part_id"),
                        partResult.getString("name"),
                        partResult.getDouble("price"),
                        partResult.getInt("stock")));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        //Disconnect from database
        disconnect();

        //Return parts list
        return parts;
    }

    /**
     * Returns a list of Parts associated with a product
     * @param productId the product id to search for
     * @return the list of Parts
     */
    public static ObservableList<ProductPart> getProductParts(int productId){

        //Connect to the database
        connect();

        //Create list of parts
        ObservableList<ProductPart> parts = FXCollections.observableArrayList();

        //Get the list of part id's associated with the product
        try(Statement statement = connection.createStatement()) {
            String partIdQuery = "SELECT part_id, quantity FROM product_parts WHERE product_id = '" + productId + "';";
            ResultSet partIdResultSet = statement.executeQuery(partIdQuery);

            //Retrieve parts
            try(Statement statement1 = connection.createStatement()){
                while (partIdResultSet.next()) {
                    String partQuery = "SELECT * FROM parts WHERE part_id = '" + partIdResultSet.getInt("part_id") + "';";
                    ResultSet partResultSet = statement1.executeQuery(partQuery);

                    //Add parts to list
                    while (partResultSet.next()){
                        parts.add(new ProductPart(partResultSet.getInt("part_id"),
                                partResultSet.getString("name"),
                                partResultSet.getDouble("price"),
                                partIdResultSet.getInt("quantity")
                        ));
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //Disconnect from database
        disconnect();

        //Return Parts list
        return parts;
    }

    /**
     * Search Parts
     * @param searchCriteria the search criteria to search with
     * @return the list of Parts matching the search criteria
     */
    public static ObservableList<InventoryPart> searchInventoryParts(String searchCriteria){
        //Connect to the database
        connect();

        //Create list of inventory products
        ObservableList<InventoryPart> inventoryParts = FXCollections.observableArrayList();

        //Get products matching search criteria
        try(Statement statement = connection.createStatement()){
            String searchQuery = "SELECT * " +
                                 "FROM parts " +
                                 "WHERE " +
                                 "part_id LIKE '%" + searchCriteria + "%' OR " +
                                 "name LIKE '%" + searchCriteria + "%';";
            ResultSet partResult = statement.executeQuery(searchQuery);

            //Add products to list
            while(partResult.next()){
                inventoryParts.add(new InventoryPart(partResult.getInt("part_id"),
                                                     partResult.getString("name"),
                                                     partResult.getDouble("price"),
                                                     partResult.getInt(  "stock")));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        //Disconnect from database
        disconnect();

        //Return inventory parts
        return inventoryParts;
    }

    /**
     * Return Part by part id
     * @param partId the id to search for
     * @return the Part to return
     */
    public static InventoryPart getPartById(int partId){

        //Connect to the database
        connect();

        //Create inventory part
        InventoryPart part = null;

        //Retrieve part
        try(Statement statement = connection.createStatement()){
            String partQuery = "SELECT * FROM parts WHERE part_id = '" + partId + "';";
            ResultSet partResult = statement.executeQuery(partQuery);

            //Set Part ot result
            while (partResult.next()){
                part = new InventoryPart(partResult.getInt("part_id"),
                                partResult.getString("name"),
                                partResult.getDouble("price"),
                                partResult.getInt("stock"));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        //Disconnect from database
        disconnect();

        //Return the Part
        return part;
    }

    /**
     * Add Part to the database
     * @param part the part to add
     */
    public static void addPart(InventoryPart part){

        //Connect to the database
        connect();

        //Insert Part into database
        try(Statement statement = connection.createStatement()){
            String partInsertQuery =    "INSERT INTO " +
                    "parts(part_id, name, price, stock, " +
                    "created_date, created_by, modified_date, modified_by) " +
                    "VALUES" +
                    "('" + part.getId() +  "', '" + part.getName() +
                    "', '" + part.getPrice() + "', '" + part.getStock() +
                    "', '" + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) +
                    "', 'admin', '" + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) +
                    "', 'admin');";

            statement.executeUpdate(partInsertQuery);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //Disconnect from the database
        disconnect();
    }

    /**
     * Remove Part from database
     * @param partId the customer id of the customer to remove
     */
    public static void removePart(int partId){

        //Connect to the database
        connect();

        //Remove Part from database
        try (Statement statement = connection.createStatement()){
            String removeQuery = "DELETE FROM parts WHERE part_id = '" + partId + "';";
            statement.executeUpdate(removeQuery);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        //Disconnect from the database
        disconnect();
    }

    /**
     * Update Part
     * @param part the part to update
     */
    public static void updatePart(InventoryPart part){

        //Connect to the database
        connect();

        //Update Part in database
        try (Statement statement = connection.createStatement()){
            String updateQuery = "UPDATE parts SET name = '" + part.getName() +
                                    "', price = '" + part.getPrice() + "', stock = '" +
                                    part.getStock() + "' WHERE part_id = '" + part.getId() + "';";
            statement.executeUpdate(updateQuery);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        //Disconnect from the database
        disconnect();
    }

    /**
     * Increase the stock of a Part
     * @param partId the id of the Part to increase
     * @param quantity the amount to increase the stock
     */
    public static void increasePartStock(int partId, int quantity){

        //Connect to database
        connect();

        //Increase part stock by quantity
        try (Statement statement = connection.createStatement()){
            String updateQuery = "UPDATE parts SET stock = stock + " + quantity + " WHERE part_id = '" + partId + "';";
            statement.executeUpdate(updateQuery);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        //Disconnect from database
        disconnect();
    }

    /**
     * Decrease the stock of a Part
     * @param partId the id of the part to increase
     * @param quantity the amount to increase the stock
     */
    public static void decreasePartStock(int partId, int quantity){

        //Connect to database
        connect();

        //Decrease Part stock by quantity
        try (Statement statement = connection.createStatement()){
            String updateQuery = "UPDATE parts SET stock = stock - " + quantity + " WHERE part_id = '" + partId + "';";
            statement.executeUpdate(updateQuery);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        //Disconnect from database
        disconnect();
    }
}
