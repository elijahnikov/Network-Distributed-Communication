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
    // The set of all the print writers for all the clients, used for broadcast.
    public static Set<PrintWriter> writers = new HashSet<>();
    
    public static UserTable ut = new UserTable();
    
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
                            addToList("temp", name, "123");
                            UserTable.userList.printList(UserTable.userList);
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
                
                String type;
                if (members.size()){ 
                    type = "Coordinator";
                } else {
                    type = "Member";
                }
                out.println("USERTYPE" + type);
                
                out.println("USERID" + name);
                
                out.println("USERIP" + ip);
                
                // Accept messages from this client and broadcast them.
                while (true) {
                    String input = in.nextLine();
                    if (input.toLowerCase().startsWith("/quit")) {
                        return;
                    }
                    for (PrintWriter writer : writers) {
                        
                            writer.println("MESSAGE " + name + ": " + input);
                        
                    }
                }          
                      
            } catch (Exception e) {
            } finally {
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
                                UserTable.userList.removeFirst();
                                coordinator.setCoordinator(members.getNext());
                                UserTable.userList.head.setType("Coordinator");
                                UserTable.userList.head.setID(coordinator.getCoordinator());
                                UserTable.userList.head.setIP("123");
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
                try { socket.close(); } catch (IOException e) {}
            }
        }

        public void makeCoordinator(){
            coordinator.setCoordinator(name);
            System.out.println("Coordinator is: " + coordinator.getCoordinator());
            out.println("MESSAGE " + "[SERVER]" + " You are the coordinator!");
        }
        
        public void addToList(String type, String ID, String IP){
            if (members.isEmpty()){
                type = "Coordinator";
                UserTable.userList.head.setType(type);
                UserTable.userList.head.setID(ID);
                UserTable.userList.head.setIP(IP);
            } else {
                type = "Member";
                UserTable.userList.addLast(type, ID, IP);
            }
        }
    }
}