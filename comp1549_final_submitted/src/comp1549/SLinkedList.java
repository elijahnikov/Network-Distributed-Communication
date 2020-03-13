package comp1549;

import java.util.NoSuchElementException;

public class SLinkedList {
    
    //data structure class to be used for storing current users and displayed on user table
    public StringNode head;   
    public SLinkedList() {
        head = new StringNode();
    }
    
    //sets the first node of the linked list
    void addFirst(String type, String ID, String IP){
        head = new StringNode(type, ID, IP, head);
    }
    
    //adds a node to the end of the linked list
    void addLast(String type, String ID, String IP){
        StringNode tail;
        tail = head;
        while(tail.getNext() != null){
            tail = tail.getNext();
        }
        tail.setNext( new StringNode(type, ID, IP, null));
    }
    
    //removes node in particular position in linked list
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
    
    //removes first node in linked list
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
    
    //check if list is empty
    public boolean isEmpty() {
        return head == null;
    }
    
    //returns number of nodes in linked list
    public int count() {
        int count = 0;
        for (StringNode n = head; n != null; n = n.getNext()){
            count++;
        }
        return count;
    }
    
    //prints linked list to console
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
    
    //method to check if linked list contains a particular IP
    public static boolean contains(SLinkedList list, String existIP){
        StringNode newtemp;
        boolean check = false;
        if (list.isEmpty()){
            System.out.println("List is empty");
        } else {
            newtemp = list.head;
            while(newtemp != null) {
                if (existIP.equals(newtemp.getIP())){
                    check = true;
                    break;
                } else {
                    newtemp = newtemp.getNext();
                }
            }
        }
        return check;
    }
}