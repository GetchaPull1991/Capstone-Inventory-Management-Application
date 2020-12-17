package Model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CustomerTest {

    private Customer customer;

    @Before
    public void beforeTest(){
        customer = new Customer(1, "Jim", "2018 Brooklyn Dr.",
                            "Citytown", "Indiana", "99999",
                         "United States", "555-555-5555");
    }


    @Test
    public void getNameTest() {
        String expected = "Jim";
        String actual = customer.getName();

        assertEquals(expected, actual);
    }

    @Test
    public void setName() {
        customer.setName("Ricky");

        String expected = "Ricky";
        String actual = customer.getName();

        assertEquals(expected, actual);
    }

    @Test
    public void getStreetAddress() {
        String expected = "2018 Brooklyn Dr.";
        String actual = customer.getStreetAddress();

        assertEquals(expected, actual);
    }

    @Test
    public void setStreetAddress() {
        customer.setStreetAddress("1999 England Dr.");

        String expected = "1999 England Dr.";
        String actual = customer.getStreetAddress();

        assertEquals(expected, actual);

    }

    @Test
    public void getPhoneNumber() {
        String expected = "555-555-5555";
        String actual = customer.getPhoneNumber();

        assertEquals(expected, actual);
    }

    @Test
    public void setPhoneNumber() {
        customer.setPhoneNumber("999-999-9999");

        String expected = "999-999-9999";
        String actual = customer.getPhoneNumber();

        assertEquals(expected, actual);
    }

    @Test
    public void getCustomerID() {
        int expected = 1;
        int actual = customer.getCustomerID();

        assertEquals(expected, actual);
    }

    @Test
    public void setCustomerID() {
        customer.setCustomerID(2);

        int expected = 2;
        int actual = customer.getCustomerID();

        assertEquals(expected, actual);
    }

    @Test
    public void getCity() {
        String expected = "Citytown";
        String actual = customer.getCity();

        assertEquals(expected, actual);
    }

    @Test
    public void setCity() {
        customer.setCity("Towncity");

        String expected = "Towncity";
        String actual = customer.getCity();

        assertEquals(expected, actual);
    }

    @Test
    public void getCountry() {
        String expected = "United States";
        String actual = customer.getCountry();

        assertEquals(expected, actual);
    }

    @Test
    public void setCountry() {
        customer.setCountry("Canada");

        String expected = "Canada";
        String actual = customer.getCountry();

        assertEquals(expected, actual);
    }

    @Test
    public void getDivision() {
        String expected = "Indiana";
        String actual = customer.getDivision();

        assertEquals(expected, actual);
    }

    @Test
    public void setDivision() {
        customer.setDivision("Colorado");

        String expected = "Colorado";
        String actual = customer.getDivision();

        assertEquals(expected, actual);
    }

    @Test
    public void getPostalCode() {
        String expected = "99999";
        String actual = customer.getPostalCode();

        assertEquals(expected, actual);
    }

    @Test
    public void setPostalCode() {
        customer.setPostalCode("55555");

        String expected = "55555";
        String actual = customer.getPostalCode();

        assertEquals(expected, actual);
    }
}