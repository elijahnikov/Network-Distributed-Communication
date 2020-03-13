package comp1549;

public class StringNode {
    
    //string node class which uses SLinkedList class and setters and getters
    private String userType;
    private String userID;
    private String userIP;
    private StringNode next;
    
    public StringNode(){
        this(null, null, null, null);
    }
    
    public StringNode(String type, String ID, String IP, StringNode n){
        userID = ID;
        userIP = IP;
        userType = type;
        next = n;
    }
    
    public String getType(){
        return userType;
    }
    
    public String getID(){
        return userID;
    }
    
    public String getIP(){
        return userIP;
    }
    
    public StringNode getNext(){
        return next;
    }
    
    public void setType(String newType){
        userType = newType;
    }
    
    public void setID(String newID){
        userID = newID;
    }
    
    public void setIP(String newIP){
        userIP = newIP;
    }
    
    public void setNext(StringNode newNext){
        next = newNext;
    }
}
