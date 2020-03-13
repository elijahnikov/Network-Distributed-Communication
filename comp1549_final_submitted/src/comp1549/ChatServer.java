package comp1549;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Set;
import java.util.HashSet;
import java.util.Scanner;
import java.util.TimerTask;
import java.util.concurrent.*;
import java.util.Timer;

public class ChatServer {

    public static Members members = new Members();
    public static Set<PrintWriter> writers = new HashSet<>();
    public static UserTable ut = new UserTable();
    public static SLinkedList ul = new SLinkedList();
    public static String memberCount;
    public static Timer timer;
    public static Coordinator coordinator = Coordinator.getInstance();
    public static void main(String[] args) throws Exception {
        System.out.println("The chat server is running...");
        ExecutorService pool = Executors.newFixedThreadPool(500);
        try (ServerSocket listener = new ServerSocket(59001)) {
            while (true) {
                pool.execute(new Handler(listener.accept()));
            }
        }
    }

    public static class Handler implements Runnable {
        public String name;
        public String ip;
        public String type;
        public String nextName;
        public String nextIP;
        public String existIP;
        private Socket socket;
        private Scanner in;
        private PrintWriter out;
        public Handler(Socket socket) {
            this.socket = socket;
        }
        
        public synchronized void run() {
            try {
                in = new Scanner(socket.getInputStream());
                out = new PrintWriter(socket.getOutputStream(), true);
                
                // Keep requesting a name, ip until we get a unique one.
                while (true) {
                    out.println("SUBMITNAME");
                    name = in.nextLine();
                    if (name == null) {
                        return;
                    }
                    out.println("SUBMITIP");
                    ip = in.nextLine();
                    if (ip == null){
                        return;
                    }
                    
                    out.println("EXISTIP");
                    existIP = in.nextLine();
                    if (existIP == null){
                        return;
                    }
                    
                    synchronized (members) {
                        //make first member to join coordinator, make rest normal members
                        if (coordinator.isEmpty()){ 
                            makeCoordinator();
                            out.println("COORDINATOR" + name);
                        }
                        if (!name.isEmpty() && !members.contains(name)) {
                            members.addMember(name);
                            members.addIP(ip);
                            break;
                        } 
                    }
                }
                
                out.println("NAMEACCEPTED " + name);
                //launches timer to check for coordinator activity
                launchTimer();
                
                for (PrintWriter writer : writers) {
                    writer.println("MESSAGE " + name + " has joined");
                }
                writers.add(out);
                
                //sets user type for data structure, either coordinator or member
                out.println("USERTYPE");
                type = in.nextLine();
                if (members.hasCoordinator()){
                    type = "Coordinator";
                } else {
                    type = "Member";
                }             
                
                //adds user details to list
                UserTable.addToList(ul, type, name, ip);
                ul.printList(ul);
                
                //for each user, send the list
                for (PrintWriter writer : writers) {
                    sendList(ul, writer);                    
                }
                
                
                // Accept messages from this client and broadcast them.
                while (true) {
                    String input = in.nextLine();
                    if (input.toLowerCase().startsWith("!check")) {
                        out.println("MESSAGE" + "hello");
                    }
                    for (PrintWriter writer : writers) {
                        writer.println("MESSAGE " + name + ": " + input);
                    }
                    //each time a message is sent by the coordinator, reset the timer
                    out.println("ACTIVITY" + coordinator.getCoordinator());
                    if (in.nextLine().equals("restart")){
                        resetTimer();
                    }
                }
            
            } catch (Exception e) {
            } finally {
                leave();
                try { 
                    socket.close(); 
                } catch (IOException e) {}
            }
        }
        
        //method to make coordinator, called when coordinator is empty upon joining
        public void makeCoordinator(){
            coordinator.setCoordinator(name);
            coordinator.setCoordinatorIP(ip);
            System.out.println("Coordinator is: " + coordinator.getCoordinator());
            out.println("MESSAGE " + "[SERVER]" + " You are the coordinator!");
        }
        
        //leave method to be called when a user leaves
        public synchronized void leave(){
            
            if (name != null) {
                System.out.println(name + " is leaving");
                nextName = members.getNextName();
                nextIP = members.getNextIP();
                members.removeMember(name);
                members.removeIP(ip);
                for (PrintWriter writer : writers) {
                    writer.println("REMOVEMEM" + name);
                    writer.println("MESSAGE " + name + " has left");
                }
            }
            if (out != null) {
                    writers.remove(out);
            }
            if (coordinator != null){
                members.removeMember(coordinator.getCoordinator());
                members.removeIP(coordinator.getCoordinatorIP());
                coordinator.setCoordinator(null);
                coordinator.setCoordinatorIP(null);
                    if (coordinator.isEmpty()){
                        if (members.isEmpty()) {
                            System.out.println("No more members");
                        } else {  
                            ul.removeFirst();
                            coordinator.setCoordinator(nextName);
                            coordinator.setCoordinatorIP(nextIP);
                            ul.head.setType("Coordinator");
                            ul.head.setID(coordinator.getCoordinator());
                            ul.head.setIP(coordinator.getCoordinatorIP());
                            ul.printList(ul);
                            System.out.println("New Coordinator is: " + coordinator.getCoordinator());
                            for (PrintWriter writer : writers){
                                 writer.println("MESSAGE " + "[SERVER]"  + " New Coordinator is: " + coordinator.getCoordinator());
                                 updateList(ul, writer);
                            }
                        }
                    }
            }
        }  
    }
    
    //method to send list to each client, in order to populate user table
    public static void sendList(SLinkedList list, PrintWriter writer){
            StringNode temp;
            if (list.isEmpty()){
                System.out.println("List is empty");
            } else {
                temp = list.head;
                while (temp !=null){
                    writer.println("SENDTYPE" + temp.getType());
                    writer.println("SENDID" + temp.getID());
                    writer.println("SENDIP" + temp.getIP());
                    writer.println("ASSEMBLE" + list.count());
                    temp = temp.getNext();
                }
            }
    }
    
    //method to update list if coordinator is changed
    public static void updateList(SLinkedList list, PrintWriter writer){
            StringNode temp;
            if (list.isEmpty()){
                System.out.println("List is empty");
            } else {
                temp = list.head;
                String ip = temp.getIP();
                writer.println("SENDTYPE" + temp.getType());
                writer.println("SENDID" + temp.getID());
                writer.println("SENDIP" + coordinator.getCoordinatorIP());
                writer.println("UPDATECO" + coordinator.getCoordinator());
            }
        }       
    
    //timer method
    private static void launchTimer() {
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    makeNewCoordinator(members.getNextName(), members.getNextIP());
                }
            };
            timer = new Timer();
            timer.scheduleAtFixedRate(timerTask, 300000, 300000);
    }
    
    //reset timer method
    public static void resetTimer(){
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                makeNewCoordinator(members.getNextName(), members.getNextIP());
            }
        };
        timer.cancel();
        timer = new Timer();
        timer.scheduleAtFixedRate(timerTask, 300000, 300000);
    }
    
    //make new coordinator method if current coordinator is inactive
    public static void makeNewCoordinator(String name, String ip){
        try{
            ul.addLast("Member", coordinator.getCoordinator(), coordinator.getCoordinatorIP());
            coordinator.setCoordinator(null);
            coordinator.setCoordinatorIP(null);
                if (coordinator.isEmpty()){
                    if (members.isEmpty()) {
                        System.out.println("No more members");
                    } else {  
                        ul.removeFirst();
                        coordinator.setCoordinator(name);
                        coordinator.setCoordinatorIP(ip);
                        ul.head.setType("Coordinator");
                        ul.head.setID(coordinator.getCoordinator());
                        ul.head.setIP(coordinator.getCoordinatorIP());
                        ul.printList(ul);
                        System.out.println("New Coordinator is: " + coordinator.getCoordinator());
                        for (PrintWriter writer : writers){
                            writer.println("MESSAGE " + "[SERVER]"  + " New Coordinator is: " + coordinator.getCoordinator());
                            updateList(ul, writer);
                            sendList(ul, writer);
                        }
                    }
                }
        } catch (NullPointerException ex){}
    }
}