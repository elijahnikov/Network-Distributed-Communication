package comp1549;

public class Coordinator {
    
    private static Coordinator instance = null;
    private String coordinatorID;
    
    private Coordinator() {
    }
    
    public static synchronized Coordinator getInstance() {
        if (instance == null) {
            instance = new Coordinator();
        }
        return instance;
    } 
    
    public void setCoordinator(String name) {
        coordinatorID = name;
    }
    
    public String getCoordinator() {
        return coordinatorID;
    }
    
   
    public boolean isEmpty() {
        boolean isTrue = false;
        if (coordinatorID == null) {
           isTrue = true;
        }
        return isTrue;
    }
    
}
