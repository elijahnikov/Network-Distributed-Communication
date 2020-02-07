package comp1549;

public class StringNode {
    
    private String userID;
    private String userIP;
    private StringNode next;
    
    public StringNode(){
        this("0", "0", null);
    }
    
    public StringNode(String ID, String IP, StringNode n){
        userID = ID;
        userIP = IP;
        next = n;
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
