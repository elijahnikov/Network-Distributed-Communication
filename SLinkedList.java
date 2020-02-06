package comp1549;

public class SLinkedList {
    
    public static String chosenFunction;
    public StringNode head;
        
    public SLinkedList() {
        head = new StringNode();
    }
    
    void addFirst(String ID, String IP){
        head = new StringNode(ID, IP, head);
    }
    
    void addLast(String ID, String IP){
        StringNode tail;
        tail = head;
        while(tail.getNext() != null){
            tail = tail.getNext();
        }
        
        tail.setNext( new StringNode(ID, IP, null));
    }
    
    public boolean isEmpty() {
        return head == null;
    }
    
    public static void printList(SLinkedList thelist){
        StringNode temp;
        String newLine = "\n";
        int count = 0;
        if (thelist.isEmpty()){
            System.out.println("List is empty");
        } else {
            temp = thelist.head;
            while (temp != null) {
                System.out.println(count + "ID: " + temp.getID()+ " IP: " + temp.getIP()+", " + newLine);
                temp = temp.getNext();
                count++;
            }
            System.out.println();
        }
    }
    
}