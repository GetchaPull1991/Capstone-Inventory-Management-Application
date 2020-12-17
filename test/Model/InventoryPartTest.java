package Model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class InventoryPartTest {

    private InventoryPart inventoryPart;

    @Before
    public void setUp(){
        inventoryPart = new InventoryPart(1, "New Part", 14.99, 10);
    }

    @Test
    public void getId() {
        int expected = 1;
        int actual = inventoryPart.getId();

        assertEquals(expected, actual);
    }

    @Test
    public void setId() {
        inventoryPart.setId(2);

        int expected = 2;
        int actual = inventoryPart.getId();

        assertEquals(expected, actual);
    }

    @Test
    public void getName() {
        String expected = "New Part";
        String actual = inventoryPart.getName();

        assertEquals(expected, actual);
    }

    @Test
    public void setName() {
        inventoryPart.setName("Old Part");

        String expected = "Old Part";
        String actual = inventoryPart.getName();

        assertEquals(expected, actual);
    }

    @Test
    public void getPrice() {
        double expected = 14.99;
        double actual = inventoryPart.getPrice();

        assertEquals(expected, actual, 0);
    }

    @Test
    public void setPrice() {
        inventoryPart.setPrice(10.99);

        double expected = 10.99;
        double actual = inventoryPart.getPrice();

        assertEquals(expected, actual, 0);
    }

    @Test
    public void getStock() {
        int expected = 10;
        int actual = inventoryPart.getStock();

        assertEquals(expected, actual);
    }

    @Test
    public void setStock() {
        inventoryPart.setStock(20);

        int expected = 20;
        int actual = inventoryPart.getStock();

        assertEquals(expected, actual);
    }

    @Test
    public void incrementStock() {
        inventoryPart.incrementStock();

        int expected = 11;
        int actual = inventoryPart.getStock();

        assertEquals(expected, actual);
    }

    @Test
    public void decrementStock() {
        inventoryPart.decrementStock();

        int expected = 9;
        int actual = inventoryPart.getStock();

        assertEquals(expected, actual);
    }
}