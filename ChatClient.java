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
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ChatClient implements ActionListener {
    
    UserTable ut = new UserTable();
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
    GridBagLayout gridBagLayout = new GridBagLayout();
    GridBagConstraints gc = new GridBagConstraints();
    JScrollPane tableScroll;
    JButton userButton = new JButton("View/Update Users");
    JPanel mainPanel = new JPanel();

    public ChatClient(String serverAddress) {
        
        this.serverAddress = serverAddress;        
        tableScroll = new JScrollPane(ut.userTable);
        JScrollPane messageScroll = new JScrollPane(messageArea);
        messageScroll.setPreferredSize(new Dimension(600, 450));
        tableScroll.setPreferredSize(new Dimension(300, 450));
        
        userButton.addActionListener(this);
        userButton.setActionCommand("userButton");
        
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
        mainPanel.add(tableScroll, gc);
        gc.gridx = 1;
        gc.gridy = 1;
        mainPanel.add(userButton, gc);

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

    void run() throws IOException {
        try {
            Socket socket = new Socket(serverAddress, 59001);
            in = new Scanner(socket.getInputStream());
            out = new PrintWriter(socket.getOutputStream(), true);

            while (in.hasNextLine()) {
                String line = in.nextLine();
                if (line.startsWith("SUBMITNAME")) {
                    out.println(IG.getIDNum());
                } else if (line.startsWith("SUBMITIP")){
                    out.println(IG.getIP());
                } else if (line.startsWith("NAMEACCEPTED")) {
                    this.frame.setTitle("Chatter - " + line.substring(13));
                    textField.setEditable(true);
                } else if (line.startsWith("MESSAGE")) {
                    messageArea.append("[" + sm.getTime() + "]" + " " + line.substring(8) + "\n");
                } else if (line.startsWith("USERTYPE")){
                    System.out.println(line.substring(8));
                } else if (line.startsWith("USERID")){
                    System.out.println(line.substring(6));
                } else if (line.startsWith("USERIP")){
                    System.out.println(line.substring(6));
                } 
            }
        } finally {
            frame.setVisible(false);
            frame.dispose();
        }
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        
        if ("userButton".equals(e.getActionCommand())){
            //Object[] record = {"1", "2", "3"};
            //UserTable.model.addRow(record);
            UserTable.userList.printList(UserTable.userList);
        }
    }
}