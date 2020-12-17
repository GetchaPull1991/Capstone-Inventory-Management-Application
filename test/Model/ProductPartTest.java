package Model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ProductPartTest {

    private ProductPart productPart;

    @Before
    public void setUp(){
        productPart = new ProductPart(1, "New Part", 14.99, 10);
    }

    @Test
    public void getId() {
        int expected = 1;
        int actual = productPart.getId();

        assertEquals(expected, actual);
    }

    @Test
    public void setId() {
        productPart.setId(2);

        int expected = 2;
        int actual = productPart.getId();

        assertEquals(expected, actual);
    }

    @Test
    public void getName() {
        String expected = "New Part";
        String actual = productPart.getName();

        assertEquals(expected, actual);
    }

    @Test
    public void setName() {
        productPart.setName("Old Part");

        String expected = "Old Part";
        String actual = productPart.getName();

        assertEquals(expected, actual);
    }

    @Test
    public void getPrice() {
        double expected = 14.99;
        double actual = productPart.getPrice();

        assertEquals(expected, actual, 0);
    }

    @Test
    public void setPrice() {
        productPart.setPrice(10.99);

        double expected = 10.99;
        double actual = productPart.getPrice();

        assertEquals(expected, actual, 0);
    }

    @Test
    public void getStock() {
        int expected = 10;
        int actual = productPart.getQuantity();

        assertEquals(expected, actual);
    }

    @Test
    public void setQuantity() {
        productPart.setQuantity(20);

        int expected = 20;
        int actual = productPart.getQuantity();

        assertEquals(expected, actual);
    }

    @Test
    public void incrementQuantity() {
        productPart.incrementQuantity();

        int expected = 11;
        int actual = productPart.getQuantity();

        assertEquals(expected, actual);
    }

    @Test
    public void decrementQuantity() {
        productPart.decrementQuantity();

        int expected = 9;
        int actual = productPart.getQuantity();

        assertEquals(expected, actual);
    }
}