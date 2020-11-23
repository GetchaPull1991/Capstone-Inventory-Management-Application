package Database;

import Model.Order;
import Model.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/** Class for managing all database operations involving Orders */
public class OrderDatabase extends Database {

    /**
     * Return a list of all Orders
     * @return list of Orders
     */
    public static ObservableList<Order> getAllOrders() {

        connect();

        ArrayList<Order> orders = new ArrayList<>();

        try (Statement statement = connection.createStatement()){
            ResultSet ordersResultSet = statement.executeQuery("SELECT * FROM orders;");

            while (ordersResultSet.next()){
                orders.add(new Order(ordersResultSet.getInt("order_id"),
                        CustomerDatabase.getCustomerById(ordersResultSet.getInt("customer_id")),
                        ProductDatabase.getOrderProducts(ordersResultSet.getInt("order_id")),
                        ordersResultSet.getDouble("order_cost"),
                        ordersResultSet.getDate("created_date").toLocalDate(),
                        ordersResultSet.getDate("due_date").toLocalDate()));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        disconnect();

        return FXCollections.observableArrayList(orders);
    }

    /**
     * Return list of Orders associated with a Customer
     * @param customerId the customer id of the Customer to search for
     * @return the list of Orders
     */
    public static ObservableList<Order> getCustomerOrders(int customerId) {

        connect();

        ArrayList<Order> orders = new ArrayList<>();

        try (Statement statement = connection.createStatement()){
            ResultSet ordersResultSet = statement.executeQuery("SELECT * FROM orders WHERE customer_id = '" + customerId + "';");

            while (ordersResultSet.next()){
                orders.add(new Order(ordersResultSet.getInt("order_id"),
                        CustomerDatabase.getCustomerById(ordersResultSet.getInt("customer_id")),
                        ProductDatabase.getOrderProducts(ordersResultSet.getInt("order_id")),
                        ordersResultSet.getDouble("order_cost"),
                        ordersResultSet.getDate("created_date").toLocalDate(),
                        ordersResultSet.getDate("due_date").toLocalDate()));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        disconnect();

        return FXCollections.observableArrayList(orders);
    }

    /**
     * Add new order to database
     * @param order the order to add
     */
    public static void addOrder(Order order){

        connect();

        for (Product product : order.getProducts()) {
            try (Statement statement = connection.createStatement()) {
                String associatedPartsQuery = "INSERT INTO order_products(order_id, product_id) VALUES('" + order.getOrderID() + "', '" + product.getId() + "')";
                statement.executeUpdate(associatedPartsQuery);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }

        try(Statement statement = connection.createStatement()){
            String orderInsertQuery =    "INSERT INTO " +
                                            "orders(order_id, customer_id, order_cost," +
                                            "created_date, created_by, modified_date, modified_by, due_date) " +
                                        "VALUES" +
                                            "('" + order.getOrderID() +  "' , '" + order.getCustomerID() +
                                            "' , '" + order.getOrderCost() +
                                            "' , '" + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) +
                                            "' , 'admin' , '" + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) +
                                            "' , 'admin' , '" + order.getDueDate() + "');";

            statement.executeUpdate(orderInsertQuery);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Delete order from database
     * @param orderId the id of the order to delete
     */
    public static void removeOrder(int orderId){
        connect();

        try (Statement statement = connection.createStatement()){

            String removeQuery = "DELETE FROM orders WHERE order_id = '" + orderId + "';";
            statement.executeUpdate(removeQuery);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        try (Statement statement = connection.createStatement()){

            String removeQuery = "DELETE FROM order_products WHERE order_id = '" + orderId + "';";
            statement.executeUpdate(removeQuery);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        disconnect();
    }

    public static void updateOrder(Order order){
        connect();

        //Clear associated products from order products table
        try(Statement statement = connection.createStatement()){
            String deleteQuery = "DELETE FROM order_products WHERE order_id = '" + order.getOrderID() + "';";
            statement.executeUpdate(deleteQuery);

            //Update associated products in order parts table
            for (Product product : order.getProducts()){
                try (Statement statement1 = connection.createStatement()){
                    String associatedProductsQuery = "INSERT INTO order_products(order_id, product_id) VALUES('" + order.getOrderID() + "', '" + product.getId() + "')";
                    statement1.executeUpdate(associatedProductsQuery);
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        //Update order
        try (Statement statement = connection.createStatement()){
            String updateQuery = "UPDATE orders SET customer_id = '" + order.getCustomerID() +
                    "', order_cost = '" + order.getOrderCost() + "', modified_date = '" +
                    LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) +
                    "' , modified_by = 'admin', due_date = '" +
                    order.getDueDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + "';";
            statement.executeUpdate(updateQuery);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        disconnect();
    }


    public static ObservableList<Order> getPendingProductOrders(int productId){
        connect();
        ObservableList<Order> orders = FXCollections.observableArrayList();


        try (Statement statement = connection.createStatement()){
            String orderQuery = "SELECT DISTINCT " +
                                "o.order_id, o.customer_id, o.order_cost, o.created_date, o.due_date " +
                                "FROM orders o " +
                                "JOIN order_products op " +
                                "ON o.order_id = op.order_id " +
                                "WHERE op.product_id = '" + productId + "' AND due_date >= CURDATE();";
            ResultSet orderResultSet = statement.executeQuery(orderQuery);



            while (orderResultSet.next()){

                ObservableList<Product> products = ProductDatabase.getOrderProducts(orderResultSet.getInt("order_id"));

                orders.add(new Order(orderResultSet.getInt("order_id"),
                        CustomerDatabase.getCustomerById(orderResultSet.getInt("customer_id")),
                        products, orderResultSet.getDouble("order_cost"),
                        orderResultSet.getDate("created_date").toLocalDate(),
                        orderResultSet.getDate("due_date").toLocalDate()));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return orders;
    }
}
