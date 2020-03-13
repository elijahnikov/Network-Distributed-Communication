import comp1549.Members;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class MembersIT {
    
    public MembersIT() {
    }
    //added from here
    Members members;

    //to here
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        members = new Members();

    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of contains method, of class Members.
     */
    @Test
    public void testContains() {//This tests the method called contains for the name to check if the method returns the right values////////////////////////////////////////////////////////////////
        System.out.println("contains");
        String name = "John";
        boolean expResult = true;
        members.addMember(name);
        boolean result = members.contains(name);
        assertEquals(expResult, result);
        expResult = false;
        members.removeMember(name);
        result = members.contains(name);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of isEmpty method, of class Members.
     */
    @Test
    public void testIsEmpty() {//tests method to see that when a you add to the list it isn't empty
        System.out.println("isEmpty");
        String name = "John";
        boolean expResult = false;
        members.addMember(name);
        boolean result = members.isEmpty();
        assertEquals(expResult, result);
    }

}
