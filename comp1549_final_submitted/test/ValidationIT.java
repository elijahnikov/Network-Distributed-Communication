import comp1549.Validation;
import javax.swing.JTextField;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class ValidationIT {
    
    public ValidationIT() {
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
     * Test of checkForIP method, of class Validation.
     */
    @Test
    public void testCheckForIP() {//this checks for when ip format is correct and wrong to see if it detects it
        System.out.println("checkForIP");
        String IP = "192.168.100.1";
        JTextField field = new JTextField(IP);
        Validation instance = new Validation();
        boolean expResult = true;
        boolean result = instance.checkForIP(field);
        assertEquals(expResult, result);
        IP = "192.168";
        field = new JTextField(IP);
        instance = new Validation();
        expResult = false;
        result = instance.checkForIP(field);
        assertEquals(expResult, result);

    }

    /**
     * Test of checkForID method, of class Validation.
     */
    @Test
    public void testCheckForID() {  //this checks if method detects if the field is empty or not
        System.out.println("checkForID");
        String ID = "";
        JTextField field = new JTextField(ID);
        Validation instance = new Validation();
        boolean expResult = false;
        boolean result = instance.checkForID(field);
        assertEquals(expResult, result);
        ID = "#12345";
        field = new JTextField(ID);
        instance = new Validation();
        expResult = true;
        result = instance.checkForID(field);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }
    
}
