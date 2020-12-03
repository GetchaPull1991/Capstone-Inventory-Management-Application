package Database;

import Model.Customer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/** Class for managing all database operations involving Customers */
public class CustomerDatabase extends Database {

    /**
     * Return a list of all the Customers
     * @return the list of Customers
     */
    public static ObservableList<Customer> getAllCustomers() {

        //Connect to database
        connect();

        //Create list of customers
        ObservableList<Customer> customers = FXCollections.observableArrayList();

        //Get all customers
        try(Statement statement = connection.createStatement()){
            ResultSet customersResultSet = statement.executeQuery("SELECT * FROM customers;");

            //Add customers to list
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

        //Disconnect from database
        disconnect();

        //REturn list of customers
        return customers;
    }

    /**
     * Return customer by customer id
     * @param customerId the id to search for
     * @return the customer
     */
    public static Customer getCustomerById(int customerId){

        //Connect to database
        connect();

        //Create customer
        Customer customer = null;

        //Get customer with the provided customer id
        try(Statement statement = connection.createStatement()){
            String customerQuery = "SELECT * FROM customers WHERE customer_id ='" + customerId + "';";
            ResultSet customerResultSet = statement.executeQuery(customerQuery);

            //Set the customer to the result
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

        //Disconnect from the database
        disconnect();

        //Return the customer
        return customer;
    }

    /**
     * Get a list of Customers matching search criteria
     * @param searchCriteria the criteria to search with
     * @return the list of customers
     */
    public static ObservableList<Customer> searchCustomers(String searchCriteria){

        //Connect to the database
        connect();

        //Create list of customers
        ObservableList<Customer> customers = FXCollections.observableArrayList();

        //Get customers that match search criteria
        try(Statement statement = connection.createStatement()){
            String searchQuery = "SELECT * " +
                                 "FROM customers " +
                                 "WHERE " +
                                 "customer_id LIKE '%" + searchCriteria + "%' OR " +
                                 "name LIKE '%" + searchCriteria + "%' OR " +
                                 "street_address LIKE '%" + searchCriteria + "%' OR " +
                                 "city LIKE '%" + searchCriteria + "%' OR " +
                                 "postal_code LIKE '%" + searchCriteria + "%' OR " +
                                 "country LIKE '%" + searchCriteria + "%' OR " +
                                 "division LIKE '%" + searchCriteria + "%' OR " +
                                 "phone_number LIKE '%" + searchCriteria + "%';";
            ResultSet customersResultSet = statement.executeQuery(searchQuery);

            //Add customers to list
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

        //Disconnect from database
        disconnect();

        //Return customer list
        return customers;
    }

    /**
     * Add customer to the database
     * @param customer the customer to add
     */
    public static void addCustomer(Customer customer){

        //Connect to database
        connect();

        //Insert customer into database
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

        //Disconnect from database
        disconnect();
    }

    /**
     * Remove customer from database
     * @param customerId the customer id of the customer to remove
     */
    public static void removeCustomer(int customerId){

        //Connect to database
        connect();

        //Delete customer from database
        try (Statement statement = connection.createStatement()){

            String removeQuery = "DELETE FROM customers WHERE customer_id = '" + customerId + "';";
            statement.executeUpdate(removeQuery);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        //Disconnect from database
        disconnect();
    }

    /**
     * Update customer in the database
     * @param customer the customer to update
     */
    public static void updateCustomer(Customer customer){

        //Connect to the database
        connect();

        //Update the Customer
        try(Statement statement = connection.createStatement()){
            String updateQuery = "UPDATE customers SET name = '" + customer.getName() + "', " +
                                    "street_address = '" + customer.getStreetAddress() + "', " +
                                    "city = '" + customer.getCity() + "', " +
                                    "postal_code = '" + customer.getPostalCode() + "', " +
                                    "country = '" + customer.getCountry() + "', " +
                                    "division = '" + customer.getDivision() + "', " +
                                    "phone_number = '" + customer.getPhoneNumber() + "', " +
                                    "modified_date = '" + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) +
                                    "', modified_by = 'admin' " +
                                    "WHERE customer_id = '" + customer.getCustomerID() + "';";
            statement.executeUpdate(updateQuery);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        //Disconnect from the database
        disconnect();
    }
}
