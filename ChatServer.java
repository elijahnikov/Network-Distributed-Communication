package comp1549;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Set;
import java.util.HashSet;
import java.util.Scanner;
import java.util.concurrent.*;

public class ChatServer {

    public static Members members = new Members();
    public static Set<PrintWriter> writers = new HashSet<>();
    public static UserTable ut = new UserTable();
    public static SLinkedList ul = new SLinkedList();
    
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
        Coordinator coordinator = Coordinator.getInstance();
        public String name;
        public String ip;
        public String type;
        private Socket socket;
        private Scanner in;
        private PrintWriter out;

        public Handler(Socket socket) {
            this.socket = socket;
        }
        
        public void run() {
            try {
                in = new Scanner(socket.getInputStream());
                out = new PrintWriter(socket.getOutputStream(), true);

                // Keep requesting a name until we get a unique one.
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
                    
                    synchronized (members) {
                        if (coordinator.isEmpty()){ 
                            makeCoordinator();   
                        }
                        if (!name.isEmpty() && !members.contains(name)) {
                            members.addMember(name);
                            members.makeArray();
                            break;
                        } 
                    
                    }
                }
                
                out.println("NAMEACCEPTED " + name);
                for (PrintWriter writer : writers) {
                    writer.println("MESSAGE " + name + " has joined");
                }
                writers.add(out);
                
                out.println("USERTYPE");
                type = in.nextLine();
                if (members.hasCoordinator()){
                    type = "Coordinator";
                } else {
                    type = "Member";
                }          
                
                UserTable.addToList(ul, type, name, ip);
                ul.printList(ul);
                
                for (PrintWriter writer : writers) {
                    //writer.println("SENDTYPE");
                    //writer.println("SENDID");
                    //writer.println("SENDIP");
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
                }
            
            } catch (Exception e) {
            } finally {
                leave();
                try { 
                    socket.close(); 
                } catch (IOException e) {}
            }
        }

        public void makeCoordinator(){
            coordinator.setCoordinator(name);
            System.out.println("Coordinator is: " + coordinator.getCoordinator());
            out.println("MESSAGE " + "[SERVER]" + " You are the coordinator!");
        }
        
        public void leave(){
            if (out != null) {
                    writers.remove(out);
            }
            if (coordinator != null){
                members.removeMember(coordinator.getCoordinator());
                coordinator.setCoordinator(null);
                    if (coordinator.isEmpty()){
                        if (members.isEmpty()) {
                            System.out.println("No more members");
                        } else {  
                            ul.removeFirst();
                            coordinator.setCoordinator(members.getNext());
                            ul.head.setType("Coordinator");
                            ul.head.setID(coordinator.getCoordinator());
                            ul.head.setIP("123");
                            System.out.println("New Coordinator is: " + coordinator.getCoordinator());
                            for (PrintWriter writer : writers){
                                 writer.println("MESSAGE " + "[SERVER]"  + " New Coordinator is: " + coordinator.getCoordinator());
                            }
                        }
                    }
            }
            if (name != null) {
                System.out.println(name + " is leaving");
                members.removeMember(name);
                for (PrintWriter writer : writers) {
                     writer.println("MESSAGE " + name + " has left");
                }
            }
        }
        
        //method to send list to each client, in order to populate user table.
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
    }
}