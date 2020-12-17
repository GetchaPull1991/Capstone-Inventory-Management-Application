package Model;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;

import static org.junit.Assert.*;

public class UserTest {

    private User user;

    @Before
    public void setUp() {
        user = new User(1, "username", "ADMIN",
                        LocalDate.of(2020, 12, 12), "admin",
                        LocalDate.of(2020, 12, 12), "admin");
    }

    @Test
    public void getUsername() {
        String expected = "username";
        String actual = user.getUsername();

        assertEquals(expected, actual);
    }

    @Test
    public void setUsername() {
        user.setUsername("name_of_user");

        String expected = "name_of_user";
        String actual = user.getUsername();

        assertEquals(expected, actual);
    }

    @Test
    public void getPrivileges() {
        String expected = "ADMIN";
        String actual = user.getPrivileges();

        assertEquals(expected, actual);
    }

    @Test
    public void setPrivileges() {
        user.setPrivileges("USER");

        String expected = "USER";
        String actual = user.getPrivileges();

        assertEquals(expected, actual);
    }

    @Test
    public void getUserId() {
        int expected = 1;
        int actual = user.getUserId();

        assertEquals(expected, actual);
    }

    @Test
    public void setUserId() {
        user.setUserId(2);

        int expected = 2;
        int actual = user.getUserId();

        assertEquals(expected, actual);
    }

    @Test
    public void getCreatedDate() {

    }

    @Test
    public void getModifiedDate() {

    }

    @Test
    public void getCreatedBy() {

    }

    @Test
    public void getModifiedBy() {

    }

    @Test
    public void setModifiedBy() {

    }

    @Test
    public void setModifiedDate() {

    }
}