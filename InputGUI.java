package comp1549;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

public class InputGUI implements ActionListener {
    
    private final JFrame frame = new JFrame("Connect Menu");
    private final JPanel mainPanel = new JPanel();
    private final JTextField userIDField = new JTextField();
    private final JTextField userIPField = new JTextField();
    private final JTextField userPortField = new JTextField();
    public JTextField existingUserIPField = new JTextField();
    public JTextField existingUserPortField = new JTextField();
    private final JLabel userIDLabel = new JLabel("User ID: ");
    private final JLabel userIPLabel = new JLabel("User IP Address: ");
    private final JLabel userPortLabel = new JLabel("User Port: ");
    public JLabel existingUserIPLabel = new JLabel("Existing User IP Address: ");
    public JLabel existingUserPortLabel = new JLabel("Existing User Port: ");
    private final JButton connectButton = new JButton("Connect");
    private final JButton requestIDButton = new JButton("Request ID");
    private final GridBagLayout gridBagLayout = new GridBagLayout();
    private final GridBagConstraints gc = new GridBagConstraints();
    private static String tempID;
    private static String tempIP;

    Validation vc = new Validation();
    Members member = new Members();
    Coordinator coordinator = Coordinator.getInstance();
   
    public void createGUI(){
       
        //sets preferred size for text fields
        userIDField.setPreferredSize(new Dimension(150, 27));
        userIPField.setPreferredSize(new Dimension(150, 27));
        userPortField.setPreferredSize(new Dimension(150, 27));
        existingUserIPField.setPreferredSize(new Dimension(150, 27));
        existingUserPortField.setPreferredSize(new Dimension(150, 27));

        existingUserIPLabel.setVisible(true);
        existingUserPortLabel.setVisible(true);
        existingUserIPField.setVisible(true);
        existingUserPortField.setVisible(true);
            
        connectButton.addActionListener(this);
        connectButton.setActionCommand("connectButton");
        requestIDButton.addActionListener(this);
        requestIDButton.setActionCommand("requestIDButton");
        
        mainPanel.setLayout(gridBagLayout);
        
        gc.anchor = GridBagConstraints.LINE_END;

        gc.gridx = 0;
        gc.gridy = 0;
        mainPanel.add(userIDLabel, gc);
        gc.gridx = 0;
        gc.gridy = 1;
        mainPanel.add(userIPLabel, gc);
        gc.gridx = 0;
        gc.gridy = 2;
        mainPanel.add(userPortLabel, gc);
        gc.gridx = 0;
        gc.gridy = 3;
        mainPanel.add(existingUserIPLabel, gc);
        gc.gridx = 0;
        gc.gridy = 4;
        mainPanel.add(existingUserPortLabel, gc);
        
        gc.anchor = GridBagConstraints.LINE_START;
        gc.gridx = 1;
        gc.gridy = 0;
        mainPanel.add(userIDField, gc);
        gc.gridx = 1;
        gc.gridy = 1; 
        mainPanel.add(userIPField, gc);
        gc.gridx = 1;
        gc.gridy = 2;
        mainPanel.add(userPortField, gc);
        gc.gridx = 1;
        gc.gridy = 3;
        mainPanel.add(existingUserIPField, gc);
        gc.gridx = 1;
        gc.gridy = 4;
        mainPanel.add(existingUserPortField, gc);
        gc.weighty = 0;
        gc.gridx = 1;
        gc.gridy = 5;
        mainPanel.add(connectButton, gc);
        gc.gridx = 0;
        gc.gridy = 5;
        mainPanel.add(requestIDButton, gc);
        
        frame.setPreferredSize(new Dimension(400, 230));
        frame.setResizable(false);
        frame.add(mainPanel);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true); 
               
    }
    
    //array list containing the id's to ensure system does not generate a duplicate
    ArrayList<String> ar = new ArrayList<String>();
    
    
    @Override
    public void actionPerformed(ActionEvent e) {
        
        if ("requestIDButton".equals(e.getActionCommand())){
            
            //validation to ensure field does not take an ID that already exists.
            tempID = new String(getID());
            
            if (!ar.contains(tempID)){
                userIDField.setText(tempID);
                ar.add(tempID);
                System.out.println(Arrays.toString(ar.toArray()));
            } else {
                System.out.println("number already in array");
            }
           
        }
        
        
        
        if ("connectButton".equals(e.getActionCommand())){ 
            
            
            //if (vc.checkForID(userIDField) && vc.checkForIP(userIPField) && vc.checkForPort(userPortField){
                Thread connectThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            ChatClient clientClass = new ChatClient("192.168.0.11");
                            clientClass.run();
                        } catch (IOException ex) {
                            JOptionPane.showMessageDialog(null, "Invalid IP, please retry.");
                        }
                    } 
                });
                connectThread.start();
                frame.dispose();
                setIDNum(userIDField.getText());
                setIP(userIPField.getText());
            /*} else {
                JOptionPane.showMessageDialog(null, "Please enter correct data in all fields.");
            } */
            
        }
    }

    //method to generate an ID for the user  
    public static String getID() {
        Random rnd = new Random();
        int number = rnd.nextInt(99999);
        return String.format("#" + "%05d", number);
    }
    
    public String getIDNum() {
        return tempID;
    }
    
    public void setIDNum(String setID) {
        this.tempID = setID;
    }
    
    public String getIP(){
        return tempIP;
    }
    
    public void setIP(String IP){
        this.tempIP = IP;
    }
}