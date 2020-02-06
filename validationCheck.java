package comp1549;

import javax.swing.JTextField;

public class ValidationCheck {
    
    //method to check if number fields (IP and Port) are empty
    private boolean checkForNumber(JTextField field){
        if (!field.getText().matches("[0-9]+")){
            return false;
        } else {
            return true;
        }
    }
   
    //method to check if ID field is empty
    private boolean checkForID(JTextField field){
        if (field.getText().equals("")){
            return false;
        } else {
            return true;
        }
    }
    
}
