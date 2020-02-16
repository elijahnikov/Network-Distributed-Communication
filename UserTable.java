package comp1549;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class UserTable {
    
    public static SLinkedList userList = new SLinkedList();
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
  
    public static void tableData(SLinkedList list){
       StringNode temp;
       if (list.isEmpty()){
           System.out.println("List is empty");
       } else {
           temp = list.head;
           System.out.println("test");
           try {
               while (temp != null){
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
   }
}
