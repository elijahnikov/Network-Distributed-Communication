package comp1549;

import java.util.ArrayList;
import java.util.List;

public class Members {
    
    //member class to store current members on chat server
    int currentIndex = 0;
    private static List<String> names = new ArrayList<>();
    private static List<String> ip = new ArrayList<>();
    
    public void addMember(String name){
        names.add(name);
    }
    
    public void removeMember(String name){
        names.remove(name);
    }
    
    public void addIP(String IP){
        ip.add(IP);;
    }
    
    public void removeIP(String IP){
        ip.remove(IP);
    }
    
    public boolean contains(String name) {
        boolean check = false;
        if (names.contains(name)){
            check = true;
        }
        return check;
    }
    
    public boolean isEmpty(){
        boolean check = false;
        if (names.isEmpty()){
            check = true;
        }
        return check;
    }
    
    public void printArray(){
        for (String strings : names){
            System.out.println(strings);
        }
        
        for (String strings : ip){
            System.out.println(strings);
        }
    }
    
    public String getNextName(){
        currentIndex++;
        if (currentIndex >= names.size()){
            currentIndex = 0;
        }
        if (names.size() == 0){
            return null;
        }
        return names.get(currentIndex);
    }
    
    public String getNextIP(){
        currentIndex++;
        if (currentIndex >= ip.size()){
            currentIndex = 0;
        }
        if (ip.size() == 0){
            return null;
        }
        return ip.get(currentIndex);
    }
    
    public int getSize(){
        int size = names.size();
        return size;
    }
    
    public boolean hasCoordinator(){
        boolean check = false;
        if (names.size() == 1){
            check = true;
        } 
        return check;
    }
}
