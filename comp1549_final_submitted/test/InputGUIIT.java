import comp1549.InputGUI;
import java.awt.event.ActionEvent;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class InputGUIIT {
    private static String tempID;
    private static String tempIP;
    InputGUI inputgui;
    
    public InputGUIIT() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        inputgui = new InputGUI();
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getID method, of class InputGUI.
     */
    @Test
    public void testGetID() {
        System.out.println("getID (random number generator)");
        for (int i=0;i<5;i++){//loops 5 times to give 5 ids so you can compare if they of the right format and not the same.
            String result = inputgui.getID();
            System.out.println(result);
        }
    }

    /**
     * Test of getIDNum method, of class InputGUI.
     */
    @Test
    public void testGetIDNum() {//checks if method got the expected ID
        System.out.println("getIDNum");
        String expResult = "#12345";
        inputgui.setIDNum(expResult);
        String result = inputgui.getIDNum();
        assertEquals(expResult, result);
    }

    /**
     * Test of getIP method, of class InputGUI.
     */
    @Test
    public void testGetIP() {//checks if method got the expected ip
        System.out.println("getIP");
        String expResult = "192.168.100.1";
        inputgui.setIP(expResult);
        String result = inputgui.getIP();
        assertEquals(expResult, result);
    }

    /**
     * Test of getExistIP method, of class InputGUI.
     */
    @Test
    public void testGetExistIP() {//checks if method got the expected existing IP
        System.out.println("getExistIP");
        String expResult = "192.168.100.2";
        inputgui.setExistIP(expResult);
        String result = inputgui.getExistIP();
        assertEquals(expResult, result);
    }  
}

