package comp1549;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Members {
    
    int currentIndex = 0;
    private static Set<String> names = new HashSet<>();
    private static List<String> arr;
    
    public void addMember(String name){
        names.add(name);
    }
    
    public void removeMember(String name){
        names.remove(name);
        arr.remove(name);
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
    
    public void makeArray(){
         arr = new ArrayList<String>(names);
    }
    
    public void printArray(){
        for (String strings : arr){
            System.out.println(strings);
        }
    }
    
    public String getNext(){
        currentIndex++;
        if (currentIndex >= arr.size()){
            currentIndex = 0;
        }
        if (arr.size() == 0){
            return null;
        }
        return arr.get(currentIndex);
    }
    
    public boolean size(){
        boolean check = false;
        if (arr.size() == 1){
            check = true;
        } 
        return check;
    }
}
