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
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

public class ChatClient implements ActionListener {
    
    Socket socket;
    UserTable ut = new UserTable();
    Members members = new Members();
    Message sm = new Message();
    String serverAddress;
    Scanner in;
    PrintWriter out;
    JFrame frame = new JFrame("Chatter");
    JTextField textField = new JTextField(54);
    JTextArea messageArea = new JTextArea(16, 50);
    JPanel mainPanel = new JPanel();
    InputGUI IG = new InputGUI();    
    JPanel containerPanel = new JPanel(new GridLayout(1, 1));
    JPanel chatPanel;
    JPanel tablePanel;
    GridBagLayout gridBagLayout = new GridBagLayout();
    GridBagConstraints gc = new GridBagConstraints();
    JScrollPane tableScroll;
    JScrollPane messageScroll;
    JButton userButton = new JButton("View/Update Users");
    public String type;
    public String id; 
    public String ip;

    public ChatClient(String serverAddress) throws IOException {
        
        this.serverAddress = serverAddress;   
        this.socket = new Socket(serverAddress, 59001);
        
        tableScroll = new JScrollPane(ut.userTable);
        messageScroll = new JScrollPane(messageArea);
        messageScroll.setPreferredSize(new Dimension(600, 450));
        tableScroll.setPreferredSize(new Dimension(300, 450));
        
        userButton.addActionListener(this);
        userButton.setActionCommand("userButton");
        
        textField.setEditable(false);
        textField.requestFocusInWindow();
        messageArea.setEditable(false);
        
        mainPanel.setLayout(gridBagLayout);        
        gc.anchor = GridBagConstraints.LINE_END;
        
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
        gc.gridx = 0;
        gc.gridy = 0;
        mainPanel.add(messageScroll, gc);
               
        frame.add(mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.pack();  
        mainPanel.requestFocusInWindow();
        frame.setVisible(true);
        
        Action closeAction;
        closeAction = new AbstractAction(){   
            @Override
            public void actionPerformed(ActionEvent ae) {
                try {
                    socket.close();
                } catch (IOException ex) {}
            }
        };
        
        KeyStroke ctrlC = KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_DOWN_MASK);
        messageArea.getInputMap().put(ctrlC, "closeAction");
        messageArea.getActionMap().put("closeAction", closeAction);
        textField.getInputMap().put(ctrlC, "closeAction");
        textField.getActionMap().put("closeAction", closeAction);
        tableScroll.getInputMap().put(ctrlC, "closeAction");
        tableScroll.getActionMap().put("closeAction", closeAction);

        // Send on enter then clear to prepare for next message
        textField.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                if (!textField.getText().equals("")){
                    out.println(textField.getText());
                    textField.setText("");
                    sm.getTime();  
                } 
            }
        });
    }

    void run() throws IOException {
        try {
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
                    sm.addMessage(messageArea, line.substring(8));
                } else if (line.startsWith("USERTYPE")){
                    out.println("temp");
                } else if (line.startsWith("SENDTYPE")){
                    type = line.substring(8);
                    //UserTable.model.addRow(new Object[] {line.substring(8), null, null});
                    /*if (UserTable.userList.isEmpty()){
                        UserTable.userList.head.setType(type);
                        UserTable.userList.head.setID(id);
                        UserTable.userList.head.setIP(ip);
                    } else {
                        UserTable.userList.addLast(type, id, ip);
                    }*/
                } else if (line.startsWith("SENDID")){
                    id = line.substring(6);
                } else if (line.startsWith("SENDIP")){
                    ip = line.substring(6);
                    //UserTable.addToList(type, id, ip);
                    //UserTable.list.printList(UserTable.list);
                } else if (line.startsWith("ASSEMBLE")){
                    UserTable.model.addRow(new Object[] {type, id, ip});
                }    
            }            
        } finally {
            frame.setVisible(false);
            frame.dispose();
        }
    }
    
    @Override
    public void actionPerformed(ActionEvent ex) {        
        if ("userButton".equals(ex.getActionCommand())){

        }
    }

}