package Database;

import Controller.MainBorderPaneController;
import Model.Order;
import Model.OrderProduct;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/** Class for managing all database operations involving Orders */
public class OrderDatabase extends Database {

    /**
     * Return a list of all Orders
     * @return list of Orders
     */
    public static ObservableList<Order> getAllOrders() {

        //Connect to the database
        connect();

        //Create Orders list
        ObservableList<Order> orders = FXCollections.observableArrayList();

        //Get Orders from database
        try (Statement statement = connection.createStatement()){
            ResultSet ordersResultSet = statement.executeQuery("SELECT * FROM orders;");

            //Add Orders to list
            while (ordersResultSet.next()){
                orders.add(new Order(ordersResultSet.getInt("order_id"),
                        CustomerDatabase.getCustomerById(ordersResultSet.getInt("customer_id")),
                        ProductDatabase.getOrderProducts(ordersResultSet.getInt("order_id")),
                        ordersResultSet.getDate("created_date").toLocalDate(),
                        ordersResultSet.getDate("due_date").toLocalDate()));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        //Disconnect from database
        disconnect();

        //Return Orders
        return orders;
    }

    /**
     * Return list of Orders associated with a Customer
     * @param customerId the customer id of the Customer to search for
     * @return the list of Orders
     */
    public static ObservableList<Order> getCustomerOrders(int customerId) {

        //Connect to database
        connect();

        //Create customerOrders list
        ObservableList<Order> customerOrders = FXCollections.observableArrayList();

        //Get Orders by Customer ID
        try (Statement statement = connection.createStatement()){
            ResultSet ordersResultSet = statement.executeQuery("SELECT * FROM orders WHERE customer_id = '" + customerId + "';");

            //Add Customer Orders to list
            while (ordersResultSet.next()){
                customerOrders.add(new Order(ordersResultSet.getInt("order_id"),
                        CustomerDatabase.getCustomerById(ordersResultSet.getInt("customer_id")),
                        ProductDatabase.getOrderProducts(ordersResultSet.getInt("order_id")),
                        ordersResultSet.getDate("created_date").toLocalDate(),
                        ordersResultSet.getDate("due_date").toLocalDate()));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        //Disconnect from database
        disconnect();

        //Return Customer Orders
        return customerOrders;
    }

    /**
     * Add new order to database
     * @param order the order to add
     */
    public static void addOrder(Order order){

        //Connect to database
        connect();

        //For each Product associated with the Order
        for (OrderProduct product : order.getAssociatedProducts()) {

            //Insert association into order_products table
            try (Statement statement = connection.createStatement()) {
                String associatedPartsQuery = "INSERT INTO order_products(order_id, product_id, quantity) " +
                                              "VALUES('" + order.getOrderID() +
                                              "', '" + product.getId() +
                                              "', '" + product.getQuantity() + "')";
                statement.executeUpdate(associatedPartsQuery);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }

        //Insert new Order into orders table
        try(Statement statement = connection.createStatement()){
            String orderInsertQuery =    "INSERT INTO " +
                                            "orders(order_id, customer_id, order_cost," +
                                            "created_date, created_by, modified_date, modified_by, due_date) " +
                                        "VALUES" +
                                            "('" + order.getOrderID() +  "' , '" + order.getCustomer().getCustomerID() +
                                            "' , '" + order.getOrderCost() +
                                            "' , '" + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) +
                                            "' , 'admin' , '" + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) +
                                            "' , 'admin' , '" + order.getDueDate() + "');";

            statement.executeUpdate(orderInsertQuery);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //Disconnect from database
        disconnect();
    }

    /**
     * Delete order from database
     * @param orderId the id of the order to delete
     */
    public static void removeOrder(int orderId){

        //Connect to database
        connect();

        //Remove Order from orders table
        try (Statement statement = connection.createStatement()){

            String removeQuery = "DELETE FROM orders WHERE order_id = '" + orderId + "';";
            statement.executeUpdate(removeQuery);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        //Remove Order association from order_products table
        try (Statement statement = connection.createStatement()){

            String removeQuery = "DELETE FROM order_products WHERE order_id = '" + orderId + "';";
            statement.executeUpdate(removeQuery);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        //Disconnect from database
        disconnect();
    }

    /**
     * Update Order
     * @param order the updated Order
     */
    public static void updateOrder(Order order){

        //Connect from database
        connect();

        //Clear associated products from order products table
        try(Statement statement = connection.createStatement()){
            String deleteQuery = "DELETE FROM order_products WHERE order_id = '" + order.getOrderID() + "';";
            statement.executeUpdate(deleteQuery);

            //Update associated products in order parts table
            for (OrderProduct product : order.getAssociatedProducts()){
                try (Statement statement1 = connection.createStatement()){
                    String associatedProductsQuery = "INSERT INTO order_products(order_id, product_id, quantity) " +
                                                     "VALUES('" + order.getOrderID() + "', '" + product.getId() + "', '" + product.getQuantity() + "')";
                    statement1.executeUpdate(associatedProductsQuery);
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        //Update order
        try (Statement statement = connection.createStatement()){
            String updateQuery = "UPDATE orders SET customer_id = '" + order.getCustomer().getCustomerID() +
                    "', order_cost = '" + order.getOrderCost() + "', modified_date = '" +
                    LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) +
                    "' , modified_by = '" + MainBorderPaneController.currentUser.getUsername() + "', due_date = '" +
                    order.getDueDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + "';";
            statement.executeUpdate(updateQuery);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        //Disconnect from database
        disconnect();
    }

    /**
     * Get Product Orders
     * @param productId the product id associated with the Order
     * @return the list of Orders associated with the Product
     */
    public static ObservableList<Order> getProductOrders(int productId){

        //Connect to the database
        connect();

        //Create list of Orders
        ObservableList<Order> productOrders = FXCollections.observableArrayList();

        //Get Orders from database
        try (Statement statement = connection.createStatement()){
            String orderQuery = "SELECT o.order_id, o.customer_id, o.order_cost, o.created_date, o.due_date " +
                                "FROM orders o " +
                                "JOIN order_products op " +
                                "ON o.order_id = op.order_id " +
                                "WHERE op.product_id = '" + productId + "';";
            ResultSet orderResultSet = statement.executeQuery(orderQuery);


            //Get Order products from database
            while (orderResultSet.next()){

                //Create list of products
                ObservableList<OrderProduct> products = ProductDatabase.getOrderProducts(orderResultSet.getInt("order_id"));

                //Add Order to list
                productOrders.add(new Order(orderResultSet.getInt("order_id"),
                        CustomerDatabase.getCustomerById(orderResultSet.getInt("customer_id")),
                        products,
                        orderResultSet.getDate("created_date").toLocalDate(),
                        orderResultSet.getDate("due_date").toLocalDate()));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        //Disconnect from database
        disconnect();

        //Return Pending Orders
        return productOrders;
    }

    /**
     * Get Part Orders
     * @param partId the part id associated with the Order
     * @return the list of Orders associated with the Part
     */
    public static ObservableList<Order> getPartOrders(int partId){

        //Connect to the database
        connect();

        //Create list of Orders
        ObservableList<Order> partOrders = FXCollections.observableArrayList();

        //Get Orders from database
        try (Statement statement = connection.createStatement()){
            String orderQuery = "SELECT o.order_id, o.customer_id, o.order_cost, o.created_date, o.due_date " +
                                "FROM orders o JOIN order_products op ON o.order_id = op.order_id " +
                                "JOIN product_parts pp ON pp.product_id = op.product_id " +
                                "WHERE pp.part_id = '" + partId + "';";
            ResultSet orderResultSet = statement.executeQuery(orderQuery);


            //Get Order products from database
            while (orderResultSet.next()){

                //Create list of products
                ObservableList<OrderProduct> products = ProductDatabase.getOrderProducts(orderResultSet.getInt("order_id"));

                //Add Order to list
                partOrders.add(new Order(orderResultSet.getInt("order_id"),
                        CustomerDatabase.getCustomerById(orderResultSet.getInt("customer_id")),
                        products,
                        orderResultSet.getDate("created_date").toLocalDate(),
                        orderResultSet.getDate("due_date").toLocalDate()));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        //Disconnect from database
        disconnect();

        //Return Part order
        return partOrders;
    }

    /**
     * Search Orders
     * @param searchCriteria the criteria to search with
     * @return the list of Orders that match the criteria
     */
    public static ObservableList<Order> searchOrders(String searchCriteria){

        //Connect to the database
        connect();

        //Create list of Orders
        ObservableList<Order> orders = FXCollections.observableArrayList();

        //Retrieve Orders
        try(Statement statement = connection.createStatement()){
            String searchQuery = "SELECT o.order_id, o.customer_id, o.order_cost, " +
                                 "o.created_date, o.due_date, p.name, c.name " +
                                 "FROM orders o JOIN order_products op ON o.order_id = op.order_id " +
                                 "JOIN customers c ON o.customer_id = c.customer_id " +
                                 "JOIN products p ON p.product_id = op.product_id " +
                                 "WHERE o.order_id LIKE '%" + searchCriteria + "%' OR " +
                                 "o.customer_id LIKE '%" + searchCriteria + "%' OR " +
                                 "c.name LIKE '%" + searchCriteria + "%' OR " +
                                 "o.created_date LIKE CONCAT('%', DATE_FORMAT('" + searchCriteria + "', '%Y-%m-%d'), '%') OR " +
                                 "o.due_date LIKE CONCAT('%', DATE_FORMAT('" + searchCriteria + "', '%Y-%m-%d'), '%') OR " +
                                 "p.name LIKE '%" + searchCriteria + "%';";
            ResultSet orderSearchResultSet = statement.executeQuery(searchQuery);

            //Add Orders to the list
            while (orderSearchResultSet.next()){
                orders.add(new Order(orderSearchResultSet.getInt("order_id"),
                        CustomerDatabase.getCustomerById(orderSearchResultSet.getInt("customer_id")),
                        ProductDatabase.getOrderProducts(orderSearchResultSet.getInt("order_id")),
                        orderSearchResultSet.getDate("created_date").toLocalDate(),
                        orderSearchResultSet.getDate("due_date").toLocalDate()));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        //Disconnect from the database
        disconnect();

        //Return the Orders list
        return orders;
    }
}
