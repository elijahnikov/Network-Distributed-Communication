package comp1549;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Message {

    public String getTime(){
        String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
        return currentTime;
    }
    
    public String addMessage(String message, String time, String sender){
        
        
        
        return null;
    }
    
}
