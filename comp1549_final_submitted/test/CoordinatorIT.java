
import static comp1549.ChatServer.coordinator;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoordinatorIT {

    public CoordinatorIT() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }
   
    /**
     * Test of getCoordinator method, of class Coordinator.
     */
    @Test
    public void testGetCoordinator() {//this tests if method returns the expected ID
        System.out.println("getCoordinator");
        String expResult = "#12345";
        coordinator.setCoordinator(expResult);
        String result = coordinator.getCoordinator();
        assertEquals(expResult, result);
    }

    /**
     * Test of getCoordinatorIP method, of class Coordinator.
     */
    @Test
    public void testGetCoordinatorIP() {//tests is method returns expected ip
        System.out.println("getCoordinatorIP");
        String IP = "192.168.100.1";
        coordinator.setCoordinatorIP(IP);
        String result = coordinator.getCoordinatorIP();
        assertEquals(IP, result);
    }

    /**
     * Test of contains method, of class Coordinator.
     */
    @Test
    public void testContains() {//this tests if the method can detect if there is an admin
        System.out.println("contains");
        String name = "#12345";
        boolean expResult = false;
        boolean result = coordinator.contains(name);
        assertEquals(expResult, result);
        String ID="#12345";
        coordinator.setCoordinator(ID);
        expResult = true;
        result = coordinator.contains(name);
        assertEquals(expResult, result);
    }

    /**
     * Test of isEmpty method, of class Coordinator.
     */
    @Test
    public void testIsEmpty() {//tests to see if the method returns the expected result when the ID is empty and when ith isn't
        System.out.println("isEmpty"); 
        String ID="#12345";
        boolean expResult = true;
        boolean result = coordinator.isEmpty();
        assertEquals(expResult, result);
        coordinator.setCoordinator(ID);
        expResult = false;
        result = coordinator.isEmpty();
        assertEquals(expResult, result);
    }
    
}
