package Database;

import Model.Part;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/** Class for managing all database operations involving Parts */
public class PartDatabase extends Database{

    /**
     * Return a list of all Parts
     * @return the list of Parts
     */
    public static ObservableList<Part> getAllParts(){
        connect();
        ArrayList<Part> parts = new ArrayList<>();

        try(Statement statement = connection.createStatement()){
            ResultSet partResult = statement.executeQuery("SELECT * FROM parts;");

            while (partResult.next()){
                parts.add(new Part(partResult.getInt("part_id"),
                        partResult.getString("name"),
                        partResult.getDouble("price"),
                        partResult.getInt("stock")));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return FXCollections.observableArrayList(parts);
    }

    /**
     * Returns a list of Parts associated with a product
     * @param productId the product id to search for
     * @return the list of Parts
     */
    public static ObservableList<Part> getProductParts(int productId){

        connect();
        ArrayList<Part> parts = new ArrayList<>();

        //Get the list of part id's associated with the product
        try(Statement statement = connection.createStatement()) {
            String partIdQuery = "SELECT part_id FROM product_parts WHERE product_id = '" + productId + "';";
            ResultSet partIdResultSet = statement.executeQuery(partIdQuery);

            //Get the parts associated with the product
            try(Statement statement1 = connection.createStatement()){
                while (partIdResultSet.next()) {
                    String partQuery = "SELECT * FROM parts WHERE part_id = '" + partIdResultSet.getInt("part_id") + "';";
                    ResultSet partResultSet = statement1.executeQuery(partQuery);

                    while (partResultSet.next()){
                        parts.add(new Part (partResultSet.getInt("part_id"),
                                partResultSet.getString("name"),
                                partResultSet.getDouble("price"),
                                partResultSet.getInt("stock")
                        ));
                    }
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


        disconnect();
        return FXCollections.observableArrayList(parts);
    }



    /**
     * Return Part by part id
     * @param partId the id to search for
     * @return the Part to return
     */
    public static Part getPartById(int partId){
        connect();
        Part part = null;

        try(Statement statement = connection.createStatement()){
            String partQuery = "SELECT * FROM parts WHERE part_id = '" + partId + "';";
            ResultSet partResult = statement.executeQuery(partQuery);

            while (partResult.next()){
                part = new Part(partResult.getInt("part_id"),
                                partResult.getString("name"),
                                partResult.getDouble("price"),
                                partResult.getInt("stock"));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return part;
    }

    /**
     * Add Part to the database
     * @param part the part to add
     */
    public static void addPart(Part part){

        connect();
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
    }

    /**
     * Remove Part from database
     * @param partId the customer id of the customer to remove
     */
    public static void removePart(int partId){
        connect();

        try (Statement statement = connection.createStatement()){

            String removeQuery = "DELETE FROM parts WHERE part_id = '" + partId + "';";
            statement.executeUpdate(removeQuery);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        disconnect();
    }

    /**
     * Update Part
     * @param part the part to update
     */
    public static void updatePart(Part part){
        connect();

        try (Statement statement = connection.createStatement()){
            String updateQuery = "UPDATE parts SET name = '" + part.getName() +
                                    "', price = '" + part.getPrice() + "', stock = '" +
                                    part.getStock() + "' WHERE part_id = '" + part.getId() + "';";
            statement.executeUpdate(updateQuery);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
