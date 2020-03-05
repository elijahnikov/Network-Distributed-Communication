package comp1549;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import javax.swing.JTextArea;

public class Message {

    public String getTime(){
        String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
        return currentTime;
    }
    
    public void addMessage(JTextArea field, String message){ 
        field.append("[" + getTime() + "]" + " " + message + "\n"); 
    }
    
    public int shortenTime(String time) {
        int length = time.length();
        int shortTime = Integer.parseInt(time.substring(length/2-1, length/2+1));
        return shortTime;
    }
}
