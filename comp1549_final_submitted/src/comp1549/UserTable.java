package comp1549;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class UserTable {
    
    public static SLinkedList list = new SLinkedList();
    public static JTable userTable;
    public static DefaultTableModel model;
    public static String [] column = {"Type", "User ID", "User IP"};
    public static Members members = new Members();
    
    public UserTable() {
        model = new DefaultTableModel(null, column);
        userTable = new JTable(model);
        userTable.getColumnModel().getColumn(0).setPreferredWidth(40);
        userTable.getColumnModel().getColumn(1).setPreferredWidth(30);
        userTable.getColumnModel().getColumn(2).setPreferredWidth(80);
    }
    
    //takes each record from linked list and adds to user table
    public static void tableData(SLinkedList list, DefaultTableModel tempModel){
       StringNode temp;
       if (list.isEmpty()){
           System.out.println("List is empty");
       } else {
           temp = list.head;
           System.out.println("test");
           try {
               while (temp == null){
               String typeString = temp.getType();
               String IDString = temp.getID();
               String IPString = temp.getIP();
               temp = temp.getNext();
               Object[] record = {typeString, IDString, IPString};
               tempModel.addRow(record);
           }
           } catch (NullPointerException ex){
               
           }
       }
   }
    
    //adds each record from server and adds to linked list
    public static void addToList(SLinkedList list, String type, String ID, String IP){
        if (members.hasCoordinator()){
            list.head.setType("Coordinator");
            list.head.setID(ID);
            list.head.setIP(IP);
        } else {
            list.addLast(type, ID, IP);
        }
    }
}
