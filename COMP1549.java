package comp1549;

import java.io.IOException;
import javax.swing.UIManager;

public class COMP1549 {
    
    public static InputGUI runInputFrame = new InputGUI();  

    public static void main(String[] args) throws IOException {  
        try { 
            //sets the theme for the gui
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");             
        } catch (Exception ignored){} 
        runInputFrame.createGUI();          
    }    
}
