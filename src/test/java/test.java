
import org.junit.Test;

import dao.UserDAOImpl;
import model.User;

import static org.junit.Assert.*;

import org.junit.*;


public class test {
    static UserDAOImpl userDAOImpl = new UserDAOImpl();

    @Before
    public void setUp() {

    }

    @Test
    public void testLoginFail() {
        User user = userDAOImpl.login("asadasdsadasdsads", "asdasdsadasdasdsadsadsa");
        assertTrue(user==null);
    }

    @Test
    public void testLoginSuccess() {
        User user = userDAOImpl.login("customer1", "12345");
        assertTrue(user.getUsername().equals("customer1"));
    }
    @Test
    public void testGetUser() {
        User user = userDAOImpl.getUser("customer1");
        assertTrue(user.getUsername().equals("customer1"));
    }
}
