package Database;

import Model.Customer;
import javafx.collections.ObservableList;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;

public class CustomerDatabaseTest {

    private Customer customer1;
    private Customer customer2;

    @Before
    public void setUp() {
        customer1 = new Customer(1, "Ricky Bobby", "2014 Reginald Dr.",
                "Towncity", "Colorado", "77777",
                "United States", "555-555-5555");
        customer2 = new Customer(2, "Joe Smith", "2018 Brooklyn Dr.",
                "Citytown", "Indiana", "99999",
                "United States", "666-666-6666");
    }


    @Test
    public void connect_disconnect() throws SQLException {

        //Connect to database
        Database.connect();

        //Test connection
        Assert.assertNotNull(Database.connection);

        //Disconnect
        Database.disconnect();

        //Test connection is closed
        Assert.assertTrue(Database.connection.isClosed());
    }

    @Test
    public void getAllCustomers() {

        //Add customers to database
        CustomerDatabase.addCustomer(customer1);
        CustomerDatabase.addCustomer(customer2);

        //Get list of customers from database
        ObservableList<Customer> customers = CustomerDatabase.getAllCustomers();
        Assert.assertNotNull(customers);

        //Test customer 1
        Assert.assertEquals(customer1.getCustomerID(), customers.get(0).getCustomerID());
        Assert.assertEquals(customer1.getName(), customers.get(0).getName());
        Assert.assertEquals(customer1.getStreetAddress(), customers.get(0).getStreetAddress());
        Assert.assertEquals(customer1.getPostalCode(), customers.get(0).getPostalCode());
        Assert.assertEquals(customer1.getCity(), customers.get(0).getCity());
        Assert.assertEquals(customer1.getDivision(), customers.get(0).getDivision());
        Assert.assertEquals(customer1.getCountry(), customers.get(0).getCountry());
        Assert.assertEquals(customer1.getPhoneNumber(), customers.get(0).getPhoneNumber());

        //Test customer 2
        Assert.assertEquals(customer2.getCustomerID(), customers.get(1).getCustomerID());
        Assert.assertEquals(customer2.getName(), customers.get(1).getName());
        Assert.assertEquals(customer2.getStreetAddress(), customers.get(1).getStreetAddress());
        Assert.assertEquals(customer2.getPostalCode(), customers.get(1).getPostalCode());
        Assert.assertEquals(customer2.getCity(), customers.get(1).getCity());
        Assert.assertEquals(customer2.getDivision(), customers.get(1).getDivision());
        Assert.assertEquals(customer2.getCountry(), customers.get(1).getCountry());
        Assert.assertEquals(customer2.getPhoneNumber(), customers.get(1).getPhoneNumber());

        //Remove customers from database
        CustomerDatabase.removeCustomer(customer1.getCustomerID());
        CustomerDatabase.removeCustomer(customer2.getCustomerID());
    }

    @Test
    public void searchCustomers() {

        //Add customers to database
        CustomerDatabase.addCustomer(customer1);
        CustomerDatabase.addCustomer(customer2);

        //Get customer 1
        Customer customerSearch1 = CustomerDatabase.searchCustomers("Colorado").get(0);
        Assert.assertNotNull(customerSearch1);

        //Test customer 1
        Assert.assertEquals(customerSearch1.getCustomerID(), customer1.getCustomerID());
        Assert.assertEquals(customerSearch1.getName(), customer1.getName());
        Assert.assertEquals(customerSearch1.getStreetAddress(), customer1.getStreetAddress());
        Assert.assertEquals(customerSearch1.getPostalCode(), customer1.getPostalCode());
        Assert.assertEquals(customerSearch1.getCity(), customer1.getCity());
        Assert.assertEquals(customerSearch1.getDivision(), customer1.getDivision());
        Assert.assertEquals(customerSearch1.getCountry(), customer1.getCountry());
        Assert.assertEquals(customerSearch1.getPhoneNumber(), customer1.getPhoneNumber());

        //Get customer 2
        Customer customerSearch2 = CustomerDatabase.searchCustomers("Joe Smith").get(0);

        //Test customer 2
        Assert.assertEquals(customerSearch2.getCustomerID(), customer2.getCustomerID());
        Assert.assertEquals(customerSearch2.getName(), customer2.getName());
        Assert.assertEquals(customerSearch2.getStreetAddress(), customer2.getStreetAddress());
        Assert.assertEquals(customerSearch2.getPostalCode(), customer2.getPostalCode());
        Assert.assertEquals(customerSearch2.getCity(), customer2.getCity());
        Assert.assertEquals(customerSearch2.getDivision(), customer2.getDivision());
        Assert.assertEquals(customerSearch2.getCountry(), customer2.getCountry());
        Assert.assertEquals(customerSearch2.getPhoneNumber(), customer2.getPhoneNumber());

        //Remove customers from database
        CustomerDatabase.removeCustomer(customer1.getCustomerID());
        CustomerDatabase.removeCustomer(customer2.getCustomerID());

    }

    @Test
    public void add_remove_update_retrieve() {

        //Add customer to database
        CustomerDatabase.addCustomer(customer1);

        //Test retrieve customer
        //Get customer from database
        Customer customer = CustomerDatabase.getCustomerById(customer1.getCustomerID());
        Assert.assertNotNull(customer);

        //Test add customer
        Assert.assertEquals(customer.getCustomerID(), customer1.getCustomerID());
        Assert.assertEquals(customer.getName(), customer1.getName());
        Assert.assertEquals(customer.getStreetAddress(), customer1.getStreetAddress());
        Assert.assertEquals(customer.getPostalCode(), customer1.getPostalCode());
        Assert.assertEquals(customer.getCity(), customer1.getCity());
        Assert.assertEquals(customer.getDivision(), customer1.getDivision());
        Assert.assertEquals(customer.getCountry(), customer1.getCountry());
        Assert.assertEquals(customer.getPhoneNumber(), customer1.getPhoneNumber());

        //Update Customer
        customer1.setName("New name");
        customer1.setPhoneNumber("999-999-9999");
        customer1.setStreetAddress("2018 Michelin Dr.");
        customer1.setPostalCode("00000");
        customer1.setDivision("Colorado");
        CustomerDatabase.updateCustomer(customer1);

        //Retrieve updated customer
        customer = CustomerDatabase.getCustomerById(customer1.getCustomerID());

        //Test updated customer
        Assert.assertEquals(customer.getCustomerID(), customer1.getCustomerID());
        Assert.assertEquals(customer.getName(), customer1.getName());
        Assert.assertEquals(customer.getStreetAddress(), customer1.getStreetAddress());
        Assert.assertEquals(customer.getPostalCode(), customer1.getPostalCode());
        Assert.assertEquals(customer.getCity(), customer1.getCity());
        Assert.assertEquals(customer.getDivision(), customer1.getDivision());
        Assert.assertEquals(customer.getCountry(), customer1.getCountry());
        Assert.assertEquals(customer.getPhoneNumber(), customer1.getPhoneNumber());

        //Delete customer
        CustomerDatabase.removeCustomer(customer1.getCustomerID());

        //Test delete customer
        Assert.assertNull(CustomerDatabase.getCustomerById(customer1.getCustomerID()));

    }
}