package comp1549;

import java.util.NoSuchElementException;

public class SLinkedList {
   
    public StringNode head;
        
    public SLinkedList() {
        head = new StringNode();
    }
    
    void addFirst(String type, String ID, String IP){
        head = new StringNode(type, ID, IP, head);
    }
    
    void addLast(String type, String ID, String IP){
        StringNode tail;
        tail = head;
        while(tail.getNext() != null){
            tail = tail.getNext();
        }
        tail.setNext( new StringNode(type, ID, IP, null));
    }
    
    void removeMid(String type, String ID) {
        StringNode temp, previous;
        temp = head.getNext();
        previous = null;
        while (temp.getID() != ID && temp.getType() != type && temp.getNext() != null) {
            previous = temp;
            temp = temp.getNext();
            } if (previous != null && temp.getNext() != null) {
           previous.setNext(temp.getNext());
           temp.setNext(null);
        } else {
           throw new NoSuchElementException();
           }
    }
    
    void removeFirst() {
        StringNode old;
        old = head;
        if (head != null){
            head = head.getNext();
            old.setNext(null);
        } else {
            throw new NoSuchElementException();
        }
    }
    
    public boolean isEmpty() {
        return head == null;
    }
    
    public int count() {
        int count = 0;
        for (StringNode n = head; n != null; n = n.getNext()){
            count++;
        }
        return count;
    }
    
    public static void printList(SLinkedList thelist){
        StringNode temp;
        int count = 1;
        if (thelist.isEmpty()){
            System.out.println("List is empty");
        } else {
            temp = thelist.head;
            while (temp != null) {
                System.out.println(count + " Type: " + temp.getType() +  " ID: " + temp.getID()+ " IP: " + temp.getIP()+", ");
                temp = temp.getNext();
                count++;
            }
            System.out.println();
        }
    }
   
}