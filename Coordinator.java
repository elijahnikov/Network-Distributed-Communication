package comp1549;

public class Coordinator {
    
    private static Coordinator instance = null;
    private String coordinatorID;
    private String coordinatorIP;
    
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
    
    public void setCoordinatorIP(String IP) {
        coordinatorIP = IP;
    }
    
    public String getCoordinatorIP() {
        return coordinatorIP;
    }
    
    public boolean contains(String name){
        boolean check = false;
        if (coordinatorID == name){
            check = true;
        }
        return check;
    }
   
    public boolean isEmpty() {
        boolean check = false;
        if (coordinatorID == null) {
           check = true;
        }
        return check;
    }
    
}
