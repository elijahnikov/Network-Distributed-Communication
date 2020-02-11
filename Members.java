package comp1549;

import java.util.HashSet;
import java.util.Set;

public class Members {
    
    private static Set<String> names = new HashSet<>();
    
    public void addMember(String name){
        names.add(name);
    }
    
    public void removeMember(String name){
        names.remove(name);
    }
    
    public boolean contains(String name) {
        boolean isTrue = false;
        if (names.contains(name)){
            isTrue = true;
        }
        return isTrue;
    }
    
    public String getNext(){
        String[] arr = names.toArray(new String[names.size()]);
        String nextMem = arr[0];
        return nextMem;
    }

}
