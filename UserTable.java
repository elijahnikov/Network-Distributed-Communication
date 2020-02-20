package comp1549;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class UserTable {
    
    public static SLinkedList list = new SLinkedList();
    JTable userTable;
    public static DefaultTableModel model;
    String [] column = {"Type", "User ID", "User IP"};
    
    public UserTable() {
        model = new DefaultTableModel(null, column);
        userTable = new JTable(model);
        userTable.getColumnModel().getColumn(0).setPreferredWidth(40);
        userTable.getColumnModel().getColumn(1).setPreferredWidth(30);
        userTable.getColumnModel().getColumn(2).setPreferredWidth(80);
    }
  
    public static String tableData(SLinkedList list){
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
               model.addRow(record);
           }
           } catch (NullPointerException ex){
               
           }
       }
       return null;
   }
    
    public static void addToList(String type, String ID, String IP){
            if (type == "Coordinator"){
                list.head.setType(type);
                list.head.setID(ID);
                list.head.setIP(IP);
            } else {
                list.addLast(type, ID, IP);
            }
        }
}
