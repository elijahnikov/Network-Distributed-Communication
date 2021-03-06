package comp1549;

import static comp1549.UserTable.model;
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
    Scanner in;
    PrintWriter out;
    int count = 0;
    JFrame frame = new JFrame("");
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
    public static String ip;
    public SLinkedList cl = new SLinkedList();
    public String removeCo;
    public String coordinator;
    public String coordinatorIP;
    public String member;
    public static String memberCount;
    public String messageText;
    public String firstCoordinator;

    public ChatClient(String serverAddress, int port) throws IOException {
        
        this.socket = new Socket(serverAddress, port);
        
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
                } else if (line.startsWith("COORDINATOR")){
                    firstCoordinator = line.substring(11);
                } else if (line.startsWith("SUBMITIP")){
                    out.println(IG.getIP());
                } else if (line.startsWith("EXISTIP")){
                    out.println(IG.getExistIP());
                } else if (line.startsWith("NAMEACCEPTED")) {
                    this.frame.setTitle("Chat client for user: " + line.substring(13));
                    textField.setEditable(true);
                } else if (line.startsWith("MESSAGE")) {
                    messageText = line.substring(8);
                    sm.addMessage(messageArea, messageText);
                } else if (line.startsWith("USERTYPE")){
                    out.println("temp");
                } else if (line.startsWith("SENDTYPE")){
                    type = line.substring(8);
                } else if (line.startsWith("SENDID")){
                    id = line.substring(6);
                } else if (line.startsWith("SENDIP")){
                    ip = line.substring(6);
                } else if (line.startsWith("ASSEMBLE")){  
                    int count = Integer.parseInt(line.substring(8));
                    model.addRow(new Object[] {type, id, ip});
                    updateTable(count);
                } else if (line.startsWith("UPDATECO")){ 
                    coordinator = line.substring(8);
                    model.setValueAt(coordinator, 0, 1);
                    model.setValueAt(ip, 0, 2);
                    for (int i = 1; i < model.getRowCount(); i++){
                        if (model.getValueAt(i, 1).equals(coordinator)){
                            model.removeRow(i);
                        }
                    }
                } else if (line.startsWith("REMOVEMEM")){
                    member = line.substring(9);
                    System.out.println(member);
                    for (int i = 1; i < model.getRowCount(); i++){
                        if (model.getValueAt(i, 1).equals(member)){
                            model.removeRow(i);
                        }
                    }
                } else if (line.startsWith("ACTIVITY")){
                    if (messageText.contains(firstCoordinator)){
                        if (messageText.contains(firstCoordinator)){
                            out.println("restart");
                        }
                        String time = sm.getTime();
                        System.out.println(sm.shortenTime(time));
                    }
                } else if (line.startsWith("TIME1")){
                    System.out.println(sm.getTime());
                } else if (line.startsWith("TIME2")){
                    System.out.println(sm.getTime());
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
    
    public void updateTable(int count){
        if (model.getRowCount() > count){
            model.removeRow(model.getRowCount() - count);
        }
    }
    
    public void removeUser(String user){
        for (int i = 0; i < model.getRowCount(); i++){
            System.out.println("here");
            if (model.getValueAt(i, 1).equals(user)){
                model.removeRow(i);
                
            }
        }
    }
}