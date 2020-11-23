package Database;

import Model.Customer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/** Class for managing all database operations involving Customers */
public class CustomerDatabase extends Database {

    /**
     * Return a list of all the Customers
     * @return the list of Customers
     */
    public static ObservableList<Customer> getAllCustomers() {
        connect();
        ArrayList<Customer> customers = new ArrayList<>();

        try(Statement statement = connection.createStatement()){
            ResultSet customersResultSet = statement.executeQuery("SELECT * FROM customers;");

            while (customersResultSet.next()){
                customers.add(new Customer(
                        customersResultSet.getInt("customer_id"),
                        customersResultSet.getString("name"),
                        customersResultSet.getString("street_address"),
                        customersResultSet.getString("city"),
                        customersResultSet.getString("division"),
                        customersResultSet.getString("postal_code"),
                        customersResultSet.getString("country"),
                        customersResultSet.getString("phone_number")));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        disconnect();
        return FXCollections.observableArrayList(customers);
    }

    /**
     * Return customer by customer id
     * @param customerId the id to search for
     * @return the customer
     */
    public static Customer getCustomerById(int customerId){
        connect();
        Customer customer = null;

        try(Statement statement = connection.createStatement()){
            String customerQuery = "SELECT * FROM customers WHERE customer_id ='" + customerId + "';";
            ResultSet customerResultSet = statement.executeQuery(customerQuery);

            while (customerResultSet.next()) {
                customer = new Customer(
                        customerResultSet.getInt("customer_id"),
                        customerResultSet.getString("name"),
                        customerResultSet.getString("street_address"),
                        customerResultSet.getString("city"),
                        customerResultSet.getString("division"),
                        customerResultSet.getString("postal_code"),
                        customerResultSet.getString("country"),
                        customerResultSet.getString("phone_number"));
            }
        } catch (SQLException e){
            e.printStackTrace();
        }

        return customer;
    }

    /**
     * Add customer to the database
     * @param customer the customer to add
     */
    public static void addCustomer(Customer customer){

        connect();
        try(Statement statement = connection.createStatement()){
            String customerInsertQuery =    "INSERT INTO " +
                    "customers(customer_id, name, street_address, city, " +
                    "postal_code, country, division, phone_number, " +
                    "created_date, created_by, modified_date, modified_by) " +
                    "VALUES" +
                    "('" + customer.getCustomerID() +  "', '" + customer.getName() +
                    "', '" + customer.getStreetAddress() + "', '" + customer.getCity() +
                    "', '" + customer.getPostalCode() + "', '" + customer.getCountry() +
                    "', '" + customer.getDivision() + "', '" + customer.getPhoneNumber() +
                    "', '" + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) +
                    "', 'admin', '" + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) +
                    "', 'admin');";

            statement.executeUpdate(customerInsertQuery);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Remove customer from database
     * @param customerId the customer id of the customer to remove
     */
    public static void removeCustomer(int customerId){
        connect();

        try (Statement statement = connection.createStatement()){

            String removeQuery = "DELETE FROM customers WHERE customer_id = '" + customerId + "';";
            statement.executeUpdate(removeQuery);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        disconnect();
    }

    public static void updateCustomer(Customer customer){
            connect();

            try(Statement statement = connection.createStatement()){
                String updateQuery = "UPDATE customers SET name = '" + customer.getName() + "', " +
                                        "street_address = '" + customer.getStreetAddress() + "', city = '" + customer.getCity() + "', " +
                                        "postal_code = '" + customer.getPostalCode() + "', country = '" + customer.getCountry() + "', " +
                                        "division = '" + customer.getDivision() + "', phone_number = '" + customer.getPhoneNumber() + "', " +
                                        "modified_date = '" + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) +
                                        "', modified_by = 'admin' WHERE customer_id = '" + customer.getCustomerID() + "';";
                statement.executeUpdate(updateQuery);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            disconnect();
    }
}
