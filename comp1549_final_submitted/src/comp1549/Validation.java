package comp1549;

import javax.swing.JTextField;

public class Validation {
    
    //method to check if port field is not empty
    public boolean checkForPort(JTextField field){
        if (!field.getText().matches("[0-9]+")){
            return false;
        } else {
            return true;
        }
    }
    
    //regex to check if given IP number by user is valid
    public boolean checkForIP(JTextField field){
        String IDNum = field.getText();
        String PATTERN = "^(([1-9]?\\d|1\\d\\d|2[0-5][0-5]|2[0-4]\\d)\\.){3}([1-9]?\\d|1\\d\\d|2[0-5][0-5]|2[0-4]\\d)$";
        return IDNum.matches(PATTERN);
    }
   
    //method to check if ID field is not empty
    public boolean checkForID(JTextField field){
        if (field.getText().equals("")){
            return false;
        } else {
            return true;
        }
    }
    
    
}