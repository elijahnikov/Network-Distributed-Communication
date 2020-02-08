package comp1549;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
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
    JTextField textField = new JTextField(54);
    JTextArea messageArea = new JTextArea(16, 50);
    InputGUI IG = new InputGUI();    
    JPanel containerPanel = new JPanel(new GridLayout(1, 1));
    JPanel chatPanel;
    JPanel tablePanel;
    
    //variables for the user information table
    JTable userTable;
    String[] columnNames;
    String[][] data;
    DefaultTableModel model;
    Object[][] rowData = {{1, 2, 3}};
    String[] column = {"Type", "User ID", "User IP"};
    String userType;
    String userID;
    String userIP;
    GridBagLayout gridBagLayout = new GridBagLayout();
    GridBagConstraints gc = new GridBagConstraints();
    JPanel mainPanel = new JPanel();

    public ChatClient(String serverAddress) {
        
        this.serverAddress = serverAddress;        
        
        //table functionality
        model = new DefaultTableModel(rowData, column);
        userTable = new JTable(model);
        userTable.getColumnModel().getColumn(0).setPreferredWidth(40);
        userTable.getColumnModel().getColumn(1).setPreferredWidth(30);
        userTable.getColumnModel().getColumn(2).setPreferredWidth(80);
        
        JScrollPane tableScroll = new JScrollPane(userTable);
        JScrollPane messageScroll = new JScrollPane(messageArea);
        messageScroll.setPreferredSize(new Dimension(600, 450));
        tableScroll.setPreferredSize(new Dimension(300, 450));
        
        textField.setEditable(false);
        messageArea.setEditable(false);
        
        mainPanel.setLayout(gridBagLayout);
        gc.anchor = GridBagConstraints.LINE_END;
        
        gc.gridx = 0;
        gc.gridy = 0;
        mainPanel.add(messageScroll, gc);
        gc.gridx = 0;
        gc.gridy = 1;
        mainPanel.add(textField, gc);
        
        gc.anchor = GridBagConstraints.LINE_START;
        gc.gridx = 1;
        gc.gridy = 0;
        mainPanel.add(tableScroll);

        frame.add(mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
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