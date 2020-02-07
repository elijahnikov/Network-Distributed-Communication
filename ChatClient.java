package comp1549;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class ChatClient {
    
    Message sm = new Message();
    String serverAddress;
    Scanner in;
    PrintWriter out;
    JFrame frame = new JFrame("Chatter");
    JTextField textField = new JTextField(50);
    JTextArea messageArea = new JTextArea(16, 50);
    InputGUI IG = new InputGUI();
    
    JPanel containerPanel = new JPanel(new GridLayout(1, 1));
    JPanel chatPanel;
    JPanel tablePanel;
    
    //variables for the user information table
    JTable userTable;
    String [] columnNames;
    String [][] data;
    DefaultTableModel model;
    Object [][] rowData = {{1, 2, 3}};
    String[] column = {"Type", "User ID", "User IP"};
    String userType;
    String userID;
    String userIP;

    public ChatClient(String serverAddress) {
        this.serverAddress = serverAddress;        
        
        //table functionality
        model = new DefaultTableModel(rowData, column);
        userTable = new JTable(model);
        JScrollPane scroll = new JScrollPane(userTable);
        scroll.setPreferredSize(new Dimension(300, 200));
        
        textField.setEditable(false);
        messageArea.setEditable(false);
        frame.getContentPane().add(textField, BorderLayout.SOUTH);
        frame.getContentPane().add(new JScrollPane(messageArea), BorderLayout.CENTER);
        frame.add(new JScrollPane(userTable), BorderLayout.EAST);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.pack();

        // Send on enter then clear to prepare for next message
        textField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                out.println(textField.getText());
                textField.setText("");
                sm.getTime();
            }
        });
    }

    /*private String getName() {
        return JOptionPane.showInputDialog(
            frame,
            "Choose a screen name:",
            "Screen name selection",
            JOptionPane.PLAIN_MESSAGE
        );
    }*/

    void run() throws IOException {
        try {
            Socket socket = new Socket(serverAddress, 59001);
            in = new Scanner(socket.getInputStream());
            out = new PrintWriter(socket.getOutputStream(), true);

            while (in.hasNextLine()) {
                String line = in.nextLine();
                if (line.startsWith("SUBMITNAME")) {
                    out.println(IG.getIDNum());
                } else if (line.startsWith("NAMEACCEPTED")) {
                    this.frame.setTitle("Chatter - " + line.substring(13));
                    textField.setEditable(true);
                } else if (line.startsWith("MESSAGE")) {
                    messageArea.append("[" + sm.getTime() + "]" + " " + line.substring(8) + "\n");
                }
            }
        } finally {
            frame.setVisible(false);
            frame.dispose();
        }
    }
}