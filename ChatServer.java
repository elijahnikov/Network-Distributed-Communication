package comp1549;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Scanner;
import java.util.concurrent.*;

public class ChatServer {

    public static Members members = new Members();
    
    // The set of all the print writers for all the clients, used for broadcast.
    public static Set<PrintWriter> writers = new HashSet<>();

    public static void main(String[] args) throws Exception {
        System.out.println("The chat server is running...");
        ExecutorService pool = Executors.newFixedThreadPool(500);
        try (ServerSocket listener = new ServerSocket(59001)) {
            while (true) {
                pool.execute(new Handler(listener.accept()));
            }
        }
    }

    /**
     * The client handler task.
     */
    private static class Handler implements Runnable {
        Coordinator coordinator = Coordinator.getInstance();
        private String name;
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
                    synchronized (members) {
                        if (coordinator.isEmpty()){
                            makeCoordinator();
                        }
                        if (!name.isEmpty() && !members.contains(name)) {
                            members.addMember(name);
                            break;
                        } 
                    }
                }
                
                out.println("NAMEACCEPTED " + name);
                for (PrintWriter writer : writers) {
                    writer.println("MESSAGE " + name + " has joined");
                }
                writers.add(out);

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
                    coordinator.setCoordinator(null);
                    
                        if (coordinator.isEmpty()){
                            coordinator.setCoordinator(members.getNext());
                            System.out.println("Coordinator is: " + coordinator.getCoordinator());
                            out.println("MESSAGE " + "You are the coordinator!");
                        }
                    
                    //coordinator.add(nextCo);
                    //out.println("MESSAGE " + "You are the new coordinator!");
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
            out.println("MESSAGE " + "You are the coordinator!");
        }
    }
}