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
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

public class InputGUI extends ValidationCheck implements ActionListener {
    
    private JFrame frame = new JFrame("Connect Menu");
    private JPanel mainPanel = new JPanel();
    private static JTextField userIDField = new JTextField();
    private JTextField userIPField = new JTextField();
    private JTextField userPortField = new JTextField();
    private JTextField existingUserIPField = new JTextField();
    private JTextField existingUserPortField = new JTextField();
    private JLabel userIDLabel = new JLabel("User ID: ");
    private JLabel userIPLabel = new JLabel("User IP Address: ");
    private JLabel userPortLabel = new JLabel("User Port: ");
    private JLabel existingUserIPLabel = new JLabel("Existing User IP Address: ");
    private JLabel existingUserPortLabel = new JLabel("Existing User Port: ");
    private JButton connectButton = new JButton("Connect");
    private JButton requestIDButton = new JButton("Request ID");
    private GridBagLayout gridBagLayout = new GridBagLayout();
    private GridBagConstraints gc = new GridBagConstraints();
    private static String tempID;
    
    private static final long LIMIT = 999999L;
    private static long last = 0;
   
    public void createGUI(){
       
        //sets preferred size for text fields
        userIDField.setPreferredSize(new Dimension(150, 27));
        userIPField.setPreferredSize(new Dimension(150, 27));
        userPortField.setPreferredSize(new Dimension(150, 27));
        existingUserIPField.setPreferredSize(new Dimension(150, 27));
        existingUserPortField.setPreferredSize(new Dimension(150, 27));

        existingUserIPField.setVisible(false);
        existingUserPortField.setVisible(false);
        existingUserIPLabel.setVisible(false);
        existingUserPortLabel.setVisible(false);
        //sets existing user input fields and labels to invisible if no one is connected
       
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
    
    //just to check if validation works, delete after
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
            
            Thread connectThread = new Thread(new Runnable() {
                @Override
                public void run() {
                   ChatClient clientClass = new ChatClient("127.0.0.1");
                    try {
                        clientClass.run();
                    } catch (IOException ex) {
                        Logger.getLogger(InputGUI.class.getName()).log(Level.SEVERE, null, ex);
                    }
               } 
            });
            connectThread.start();
            frame.dispose();
            
            setIDNum(userIDField.getText());
            
        }
 
    }
    
    //method to generate an ID for the user  
    public static String getID() {
        Random rnd = new Random();
        int number = rnd.nextInt(99999);
        return String.format("#" + "%05d", number);
    }
    
    public static String getIDNum() {
        return tempID;
    }
    
    public void setIDNum(String setID) {
        this.tempID = setID;
    }
}
