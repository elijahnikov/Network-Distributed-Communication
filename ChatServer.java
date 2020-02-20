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
                for (PrintWriter writer : writers) {
                    writer.println("SENDTYPE" + type);
                    writer.println("SENDID" + name);
                    writer.println("SENDIP" + ip);
                    writer.println("ASSEMBLE");
                }         

                // Accept messages from this client and broadcast them.
                while (true) {
                    String input = in.nextLine();
                    if (input.toLowerCase().startsWith("true")) {
                        break;
                    }
                    for (PrintWriter writer : writers) {
                        writer.println("MESSAGE " + name + ": " + input);
                    }
                }
            
            } catch (Exception e) {
            } finally {
                leave();
                try { socket.close(); } catch (IOException e) {}
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
                            UserTable.list.removeFirst();
                            coordinator.setCoordinator(members.getNext());
                            UserTable.list.head.setType("Coordinator");
                            UserTable.list.head.setID(coordinator.getCoordinator());
                            UserTable.list.head.setIP("123");
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
    }
}