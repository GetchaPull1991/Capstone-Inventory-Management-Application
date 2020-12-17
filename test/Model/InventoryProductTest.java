package Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class InventoryProductTest {

    private InventoryProduct inventoryProduct;

    @Before
    public void setUp(){

        //Create product parts
        ProductPart part1 = new ProductPart(1, "Part 1", 10.99, 1);
        ProductPart part2 = new ProductPart(1, "Part 2", 4.99, 2);
        ProductPart part3 = new ProductPart(1, "Part 3", 8.99, 3);

        //Create inventory product
        inventoryProduct = new InventoryProduct(1, "Test Product", FXCollections.observableArrayList(part1, part2, part3), 10);
    }

    @Test
    public void getCreatedDate() {

        //Expected/Actual
        LocalDate expected = LocalDate.now();
        LocalDate actual = inventoryProduct.getCreatedDate();

        //Test
        assertEquals("Comparison Failure: \nExpected Result:\n" + expected + "\nActual Result:\n" + actual, expected, actual);
    }
    @Test
    public void setCreatedDate() {

        //Set new date
        inventoryProduct.setCreatedDate(LocalDate.of(2020, 12, 30));

        //Expected/Actual
        LocalDate expected = LocalDate.of(2020, 12, 30);
        LocalDate actual = inventoryProduct.getCreatedDate();

        //Test
        assertEquals("Comparison Failure: \nExpected Result:\n" + expected + "\nActual Result:\n" + actual, expected, actual);

    }

    @Test
    public void getProductPartsString() {

        //Expected/Actual
        String expected = "Part 1 x 1\nPart 2 x 2\nPart 3 x 3";
        String actual = inventoryProduct.getProductPartsString();

        //Test
        assertEquals("Comparison Failure: \nExpected Result:\n" + expected + "\nActual Result:\n" + actual, expected, actual);
    }

    @Test
    public void setId() {

        //Set new ID
        inventoryProduct.setId(2);

        //Expected/Actual
        int expected = 2;
        int actual = inventoryProduct.getId();

        //Test
        assertEquals("Comparison Failure: \nExpected Result:\n" + expected + "\nActual Result:\n" + actual, expected, actual);
    }

    @Test
    public void setName() {

        //Set new name
        inventoryProduct.setName("Inventory Product");

        //Expected/Actual
        String expected = "Inventory Product";
        String actual = inventoryProduct.getName();

        //Test
        assertEquals("Comparison Failure: \nExpected Result:\n" + expected + "\nActual Result:\n" + actual, expected, actual);
    }

    @Test
    public void getId() {

        //Expected/Actual
        int expected = 1;
        int actual = inventoryProduct.getId();

        //Test
        assertEquals("Comparison Failure: \nExpected Result:\n" + expected + "\nActual Result:\n" + actual, expected, actual);
    }

    @Test
    public void getName() {

        //Expected/Actual
        String expected = "Test Product";
        String actual = inventoryProduct.getName();

        //Test
        assertEquals("Comparison Failure: \nExpected Result:\n" + expected + "\nActual Result:\n" + actual, expected, actual);
    }

    @Test
    public void getPrice() {

        //Expected/Actual
        double expected = 63.28;
        double actual = inventoryProduct.getPrice();

        //Test
        assertEquals("Comparison Failure: \nExpected Result:\n" + expected + "\nActual Result:\n" + actual, expected, actual, 0.001);
    }

    @Test
    public void setProductParts() {

        //Set new parts/Expected
        ProductPart part1 = new ProductPart(4, "Part 1", 5.99, 1);
        ProductPart part2 = new ProductPart(5, "Part 2", 2.99, 2);
        ProductPart part3 = new ProductPart(6, "Part 3", 4.99, 3);
        inventoryProduct.setProductParts(FXCollections.observableArrayList(part1, part2, part3));

        //Get product parts/Actual
        ObservableList<ProductPart> productParts = inventoryProduct.getProductParts();

        //Check for null
        assertNotNull("Part 1 is null", productParts.get(0));
        assertNotNull("Part 2 is null", productParts.get(1));
        assertNotNull("Part 3 is null", productParts.get(2));

        //Check fields for part 1
        assertEquals("ID's not equal", part1.getId(), productParts.get(0).getId());
        assertEquals("Names not equal", part1.getName(), productParts.get(0).getName());
        assertEquals("Prices not equal", part1.getPrice(), productParts.get(0).getPrice(), 0);
        assertEquals("Quantities not equal", part1.getQuantity(), productParts.get(0).getQuantity());

        //Check fields for part 2
        assertEquals("ID's not equal", part2.getId(), productParts.get(1).getId());
        assertEquals("Names not equal", part2.getName(), productParts.get(1).getName());
        assertEquals("Prices not equal", part2.getPrice(), productParts.get(1).getPrice(), 0);
        assertEquals("Quantities not equal", part2.getQuantity(), productParts.get(1).getQuantity());

        //Check fields for part 3
        assertEquals("ID's not equal", part3.getId(), productParts.get(2).getId());
        assertEquals("Names not equal", part3.getName(), productParts.get(2).getName());
        assertEquals("Prices not equal", part3.getPrice(), productParts.get(2).getPrice(), 0);
        assertEquals("Quantities not equal", part3.getQuantity(), productParts.get(2).getQuantity());

    }

    @Test
    public void setProductPartsString() {

        //Set new parts
        ProductPart part1 = new ProductPart(4, "Part 1", 5.99, 5);
        ProductPart part2 = new ProductPart(5, "Part 2", 2.99, 1);
        ProductPart part3 = new ProductPart(6, "Part 3", 4.99, 15);
        inventoryProduct.setProductParts(FXCollections.observableArrayList(part1, part2, part3));

        //Expected/Actual
        String expected = "Part 1 x 5\nPart 2 x 1\nPart 3 x 15";
        String actual = inventoryProduct.getProductPartsString();

        //Test
        assertEquals("Comparison Failure: \nExpected Result:\n" + expected + "\nActual Result:\n" + actual, expected, actual);
    }

    @Test
    public void getProductParts() {

        //Expected
        ProductPart part1 = new ProductPart(1, "Part 1", 10.99, 1);
        ProductPart part2 = new ProductPart(1, "Part 2", 4.99, 2);
        ProductPart part3 = new ProductPart(1, "Part 3", 8.99, 3);

        //Get product parts/Actual
        ObservableList<ProductPart> productParts = inventoryProduct.getProductParts();

        //Check for null
        assertNotNull("Part 1 is null", productParts.get(0));
        assertNotNull("Part 2 is null", productParts.get(1));
        assertNotNull("Part 3 is null", productParts.get(2));

        //Check fields for part 1
        assertEquals("ID's not equal", part1.getId(), productParts.get(0).getId());
        assertEquals("Names not equal", part1.getName(), productParts.get(0).getName());
        assertEquals("Prices not equal", part1.getPrice(), productParts.get(0).getPrice(), 0);
        assertEquals("Quantities not equal", part1.getQuantity(), productParts.get(0).getQuantity());

        //Check fields for part 2
        assertEquals("ID's not equal", part2.getId(), productParts.get(1).getId());
        assertEquals("Names not equal", part2.getName(), productParts.get(1).getName());
        assertEquals("Prices not equal", part2.getPrice(), productParts.get(1).getPrice(), 0);
        assertEquals("Quantities not equal", part2.getQuantity(), productParts.get(1).getQuantity());

        //Check fields for part 3
        assertEquals("ID's not equal", part3.getId(), productParts.get(2).getId());
        assertEquals("Names not equal", part3.getName(), productParts.get(2).getName());
        assertEquals("Prices not equal", part3.getPrice(), productParts.get(2).getPrice(), 0);
        assertEquals("Quantities not equal", part3.getQuantity(), productParts.get(2).getQuantity());

    }

    @Test
    public void setProductPrice() {

        //Set test parts
        ProductPart part1 = new ProductPart(4, "Part 1", 5.99, 1);
        ProductPart part2 = new ProductPart(5, "Part 2", 2.99, 2);
        ProductPart part3 = new ProductPart(6, "Part 3", 4.99, 3);
        inventoryProduct.setProductParts(FXCollections.observableArrayList(part1, part2, part3));

        //Set new price
        inventoryProduct.setProductPrice();

        //Expected/Actual
        double expected = 35.56;
        double actual = inventoryProduct.getPrice();

        //Test
        assertEquals("Comparison Failure: \nActual Product price has a " +
                             "difference greater that 0.001 from that of expected price\n"
                             + actual, expected, actual, 0.001);
    }

    @Test
    public void getStock() {

        //Expected/Actual
        int expected = 10;
        int actual = inventoryProduct.getStock();

        //Test
        assertEquals("Comparison Failure: \nExpected Result:\n" + expected + "\nActual Result:\n" + actual, expected, actual);
    }

    @Test
    public void setStock() {
        //Set new stock
        inventoryProduct.setStock(20);

        //Expected/Actual
        int expected = 20;
        int actual = inventoryProduct.getStock();

        //Test
        assertEquals("Comparison Failure: \nExpected Result:\n" + expected + "\nActual Result:\n" + actual, expected, actual);
    }

    @Test
    public void incrementStock() {
        //Increment stock
        inventoryProduct.incrementStock();

        //Expected/Actual
        int expected = 11;
        int actual = inventoryProduct.getStock();

        //Test
        assertEquals("Comparison Failure: \nExpected Result:\n" + expected + "\nActual Result:\n" + actual, expected, actual);
    }

    @Test
    public void decrementStock() {

        //Increment stock
        inventoryProduct.decrementStock();

        //Expected/Actual
        int expected = 9;
        int actual = inventoryProduct.getStock();

        //Test
        assertEquals("Comparison Failure: \nExpected Result:\n" + expected + "\nActual Result:\n" + actual, expected, actual);
    }

}