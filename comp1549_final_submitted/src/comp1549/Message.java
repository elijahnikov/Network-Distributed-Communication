package comp1549;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import javax.swing.JTextArea;

public class Message {

    //class which contains method to get current system time, and construct message to send from client to server
    
    public String getTime(){
        String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
        return currentTime;
    }
    
    public void addMessage(JTextArea field, String message){ 
        field.append("[" + getTime() + "]" + " " + message + "\n"); 
    }
}
