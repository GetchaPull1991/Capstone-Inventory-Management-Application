package Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;

import static org.junit.Assert.*;

public class OrderProductTest {

    private OrderProduct orderProduct;

    @Before
    public void setUp() {
        //Create product parts
        ProductPart part1 = new ProductPart(1, "Part 1", 10.99, 1);
        ProductPart part2 = new ProductPart(1, "Part 2", 4.99, 2);
        ProductPart part3 = new ProductPart(1, "Part 3", 8.99, 3);

        //Create order product
        orderProduct = new OrderProduct(1, "Test Product", FXCollections.observableArrayList(part1, part2, part3), 10);
    }

    @Test
    public void getCreatedDate() {

        //Expected/Actual
        LocalDate expected = LocalDate.now();
        LocalDate actual = orderProduct.getCreatedDate();

        //Test
        assertEquals(expected, actual);
    }
    @Test
    public void setCreatedDate() {

        //Set new date
        orderProduct.setCreatedDate(LocalDate.of(2020, 12, 30));

        //Expected/Actual
        LocalDate expected = LocalDate.of(2020, 12, 30);
        LocalDate actual = orderProduct.getCreatedDate();

        //Test
        assertEquals(expected, actual);

    }

    @Test
    public void getProductPartsString() {

        //Expected/Actual
        String expected = "Part 1 x 1\nPart 2 x 2\nPart 3 x 3";
        String actual = orderProduct.getProductPartsString();

        //Test
        assertEquals(expected, actual);
    }

    @Test
    public void setId() {

        //Set new ID
        orderProduct.setId(2);

        //Expected/Actual
        int expected = 2;
        int actual = orderProduct.getId();

        //Test
        assertEquals(expected, actual);
    }

    @Test
    public void setName() {

        //Set new name
        orderProduct.setName("Inventory Product");

        //Expected/Actual
        String expected = "Inventory Product";
        String actual = orderProduct.getName();

        //Test
        assertEquals(expected, actual);
    }

    @Test
    public void getId() {

        //Expected/Actual
        int expected = 1;
        int actual = orderProduct.getId();

        //Test
        assertEquals(expected, actual);
    }

    @Test
    public void getName() {

        //Expected/Actual
        String expected = "Test Product";
        String actual = orderProduct.getName();

        //Test
        assertEquals(expected, actual);
    }

    @Test
    public void getPrice() {

        //Expected/Actual
        double expected = 63.2808;
        double actual = orderProduct.getPrice();

        //Test
        assertEquals(expected, actual, 0);
    }

    @Test
    public void setProductParts() {

        //Set new parts/Expected
        ProductPart part1 = new ProductPart(4, "Part 1", 5.99, 1);
        ProductPart part2 = new ProductPart(5, "Part 2", 2.99, 2);
        ProductPart part3 = new ProductPart(6, "Part 3", 4.99, 3);
        orderProduct.setProductParts(FXCollections.observableArrayList(part1, part2, part3));

        //Get product parts/Actual
        ObservableList<ProductPart> productParts = orderProduct.getProductParts();

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
        orderProduct.setProductParts(FXCollections.observableArrayList(part1, part2, part3));

        //Expected/Actual
        String expected = "Part 1 x 5\nPart 2 x 1\nPart 3 x 15";
        String actual = orderProduct.getProductPartsString();

        //Test
        assertEquals(expected, actual);
    }

    @Test
    public void getProductParts() {

        //Expected
        ProductPart part1 = new ProductPart(1, "Part 1", 10.99, 1);
        ProductPart part2 = new ProductPart(1, "Part 2", 4.99, 2);
        ProductPart part3 = new ProductPart(1, "Part 3", 8.99, 3);

        //Get product parts/Actual
        ObservableList<ProductPart> productParts = orderProduct.getProductParts();

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

        //Set new parts
        ProductPart part1 = new ProductPart(4, "Part 1", 5.99, 1);
        ProductPart part2 = new ProductPart(5, "Part 2", 2.99, 2);
        ProductPart part3 = new ProductPart(6, "Part 3", 4.99, 3);
        orderProduct.setProductParts(FXCollections.observableArrayList(part1, part2, part3));

        //Set new price
        orderProduct.setProductPrice();

        //Expected/Actual
        double expected = 35.5608;
        double actual = orderProduct.getPrice();

        //Test
        assertEquals(expected, actual, 0);
    }

    @Test
    public void getQuantity() {

        //Expected/Actual
        int expected = 10;
        int actual = orderProduct.getQuantity();

        //Test
        assertEquals(expected, actual);
    }

    @Test
    public void setQuantity() {
        //Set new quantity
        orderProduct.setQuantity(20);

        //Expected/Actual
        int expected = 20;
        int actual = orderProduct.getQuantity();

        //Test
        assertEquals(expected, actual);
    }

    @Test
    public void incrementQuantity() {
        //Increment quantity
        orderProduct.incrementQuantity();

        //Expected/Actual
        int expected = 11;
        int actual = orderProduct.getQuantity();

        //Test
        assertEquals(expected, actual);
    }

    @Test
    public void decrementQuantity() {

        //Decrement quantity
        orderProduct.decrementQuantity();

        //Expected/Actual
        int expected = 9;
        int actual = orderProduct.getQuantity();

        //Test
        assertEquals(expected, actual);
    }
}