package function.root;

public class StringNode {
    
    private double xvalue;
    private double fxvalue;
    private StringNode next;
    
    public StringNode(){
        this(0, 0, null);
    }
    
    public StringNode(double x, double fx, StringNode n){
        xvalue = x;
        fxvalue = fx;
        next = n;
    }
    
    public double getXValue(){
        return xvalue;
    }
    
    public double getFXValue(){
        return fxvalue;
    }
    
    public StringNode getNext(){
        return next;
    }
    
    public void setXValue(double newXValue){
        xvalue = newXValue;
    }
    
    public void setFXValue(double newFXValue){
        fxvalue = newFXValue;
    }
    
    public void setNext(StringNode newNext){
        next = newNext;
    }
}
