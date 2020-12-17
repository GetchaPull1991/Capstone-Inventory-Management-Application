package Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;

import static org.junit.Assert.assertEquals;

public class OrderTest {

    private Order order;

    @Before
    public void setUp() {

        //Create product parts
        ProductPart part1 = new ProductPart(1, "Part 1", 10.99, 10);
        ProductPart part2 = new ProductPart(2, "Part 2", 4.99, 10);
        ProductPart part3 = new ProductPart(3, "Part 3", 8.99, 10);

        //Create order products
        OrderProduct product1 = new OrderProduct(1, "Product 1", FXCollections.observableArrayList(part1), 1);
        OrderProduct product2 = new OrderProduct(2, "Product 2", FXCollections.observableArrayList(part1, part2), 2);
        OrderProduct product3 = new OrderProduct(3, "Product 3", FXCollections.observableArrayList(part1, part2, part3), 3);


        //Create customer
        Customer customer = new Customer(1, "Joe Smith", "5555 Street Way", "Columbus",
                                            "Indiana", "99999", "United States", "555-555-5555");

        //Create order
        order = new Order(1, customer, FXCollections.observableArrayList(product1, product2, product3), LocalDate.now(), LocalDate.of(2021, 12, 12));

    }

    @Test
    public void getAssociatedProductsString() {

        //Expected/Actual
        String expected = "Product 1 x 1\nProduct 2 x 2\nProduct 3 x 3";
        String actual = order.getAssociatedProductsString();

        //Test
        assertEquals(expected, actual);
    }

    @Test
    public void setAssociatedProductsString() {

        //Create product parts
        ProductPart part1 = new ProductPart(1, "Part 1", 10.99, 10);
        ProductPart part2 = new ProductPart(2, "Part 2", 4.99, 10);
        ProductPart part3 = new ProductPart(3, "Part 3", 8.99, 10);

        //Create order products
        OrderProduct product1 = new OrderProduct(1, "Product 1", FXCollections.observableArrayList(part1), 3);
        OrderProduct product2 = new OrderProduct(2, "Product 2", FXCollections.observableArrayList(part1, part2), 2);
        OrderProduct product3 = new OrderProduct(3, "Product 3", FXCollections.observableArrayList(part1, part2, part3), 1);

        //Set new order products
        order.setAssociatedProducts(FXCollections.observableArrayList(product1, product2, product3));

        //Expected/Actual
        String expected = "Product 1 x 3\nProduct 2 x 2\nProduct 3 x 1";
        String actual = order.getAssociatedProductsString();

        //Test
        assertEquals(expected, actual);
    }

    @Test
    public void getOrderID() {

        //Expected/Actual
        int expected = 1;
        int actual = order.getOrderID();

        //Test
        assertEquals(expected, actual);
    }

    @Test
    public void setOrderID() {
        //Set new order id
        order.setOrderID(2);

        //Expected/Actual
        int expected = 2;
        int actual = order.getOrderID();

        //Test
        assertEquals(expected, actual);
    }

    @Test
    public void getCustomerID() {

        //Expected/Actual
        int expected = 1;
        int actual = order.getCustomer().getCustomerID();

        //Test
        assertEquals(expected, actual);
    }


    @Test
    public void getCustomerName() {

        //Expected/Actual
        String expected = "Joe Smith";
        String actual = order.getCustomer().getName();

        //Test
        assertEquals(expected, actual);
    }

    @Test
    public void setCustomer(){
        //Create customer
        Customer customer = new Customer(40, "Ricky Bobby", "5555 Street Way", "Columbus",
                "Indiana", "99999", "United States", "555-555-5555");

        //Set new order customer
        order.setCustomer(customer);

        //Expected/Actual id
        int expectedId = 40;
        int actualId = order.getCustomer().getCustomerID();

        //Test id
        assertEquals(expectedId, actualId);

        //Expected/Actual name
        String expectedName = "Ricky Bobby";
        String actualName = order.getCustomer().getName();

        //Test
        assertEquals(expectedName, actualName);
    }

    @Test
    public void getOrderCost() {

        //Expected/Actual
        double expected = 1555.75;
        double actual = order.getOrderCost();

        //Test
        assertEquals(expected, actual, 0.01);
    }

    @Test
    public void getAssociatedProducts() {

        //Expected products
        //Create product parts
        ProductPart part1 = new ProductPart(1, "Part 1", 10.99, 10);
        ProductPart part2 = new ProductPart(2, "Part 2", 4.99, 10);
        ProductPart part3 = new ProductPart(3, "Part 3", 8.99, 10);

        //Create order products
        OrderProduct product1 = new OrderProduct(1, "Product 1", FXCollections.observableArrayList(part1), 1);
        OrderProduct product2 = new OrderProduct(2, "Product 2", FXCollections.observableArrayList(part1, part2), 2);
        OrderProduct product3 = new OrderProduct(3, "Product 3", FXCollections.observableArrayList(part1, part2, part3), 3);

        //Actual Products
        ObservableList<OrderProduct> orderProducts = order.getAssociatedProducts();

        //Test Product 1
        assertEquals(product1.getId(), orderProducts.get(0).getId());
        assertEquals(product1.getName(), orderProducts.get(0).getName());
        assertEquals(product1.getPrice(), orderProducts.get(0).getPrice(), 0.01);
        assertEquals(product1.getQuantity(), orderProducts.get(0).getQuantity());

        //Test Product 2
        assertEquals(product2.getId(), orderProducts.get(1).getId());
        assertEquals(product2.getName(), orderProducts.get(1).getName());
        assertEquals(product2.getPrice(), orderProducts.get(1).getPrice(), 0.01);
        assertEquals(product2.getQuantity(), orderProducts.get(1).getQuantity());

        //Test Product 3
        assertEquals(product3.getId(), orderProducts.get(2).getId());
        assertEquals(product3.getName(), orderProducts.get(2).getName());
        assertEquals(product3.getPrice(), orderProducts.get(2).getPrice(), 0.01);
        assertEquals(product3.getQuantity(), orderProducts.get(2).getQuantity());
    }

    @Test
    public void setAssociatedProducts() {
        //Expected products/new Products
        //Create product parts
        ProductPart part1 = new ProductPart(10, "Part 10", 100.99, 107);
        ProductPart part2 = new ProductPart(20, "Part 20", 40.99, 106);
        ProductPart part3 = new ProductPart(30, "Part 30", 80.99, 105);

        //Create order products
        OrderProduct product1 = new OrderProduct(1, "Product 1", FXCollections.observableArrayList(part1), 3);
        OrderProduct product2 = new OrderProduct(2, "Product 2", FXCollections.observableArrayList(part1, part2), 2);
        OrderProduct product3 = new OrderProduct(3, "Product 3", FXCollections.observableArrayList(part1, part2, part3), 1);

        //Set new Products
        order.setAssociatedProducts(FXCollections.observableArrayList(product1, product2, product3));

        //Actual Products
        ObservableList<OrderProduct> orderProducts = order.getAssociatedProducts();

        //Test Product 1
        assertEquals(product1.getId(), orderProducts.get(0).getId());
        assertEquals(product1.getName(), orderProducts.get(0).getName());
        assertEquals(product1.getPrice(), orderProducts.get(0).getPrice(), 0.01);
        assertEquals(product1.getQuantity(), orderProducts.get(0).getQuantity());

        //Test Product 2
        assertEquals(product2.getId(), orderProducts.get(1).getId());
        assertEquals(product2.getName(), orderProducts.get(1).getName());
        assertEquals(product2.getPrice(), orderProducts.get(1).getPrice(), 0.01);
        assertEquals(product2.getQuantity(), orderProducts.get(1).getQuantity());

        //Test Product 3
        assertEquals(product3.getId(), orderProducts.get(2).getId());
        assertEquals(product3.getName(), orderProducts.get(2).getName());
        assertEquals(product3.getPrice(), orderProducts.get(2).getPrice(), 0.01);
        assertEquals(product3.getQuantity(), orderProducts.get(2).getQuantity());
    }

    @Test
    public void getCreatedDate() {

        //Expected/Actual
        LocalDate expected = LocalDate.now();
        LocalDate actual = order.getCreatedDate();

        //Test
        assertEquals(expected, actual);
    }

    @Test
    public void setCreatedDate() {
        //Set new date
        order.setCreatedDate(LocalDate.of(2021,12,12));

        //Expected/Actual
        LocalDate expected = LocalDate.of(2021,12,12);
        LocalDate actual = order.getCreatedDate();

        //Test
        assertEquals(expected, actual);
    }

    @Test
    public void getDueDate() {

        //Expected/Actual
        LocalDate expected = LocalDate.of(2021, 12, 12);
        LocalDate actual = order.getDueDate();

        //Test
        assertEquals(expected, actual);
    }

    @Test
    public void setDueDate() {
        //Set new date
        order.setDueDate(LocalDate.of(2022,12,12));

        //Expected/Actual
        LocalDate expected = LocalDate.of(2022,12,12);
        LocalDate actual = order.getDueDate();

        //Test
        assertEquals(expected, actual);
    }
}