//Swing JTable demo with my sql
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableCellRenderer;

class ObjAccess 
{
	static String url = "jdbc:mysql://10.222.115.115:3306/automationDB";
	static String userid = "automationDB";
	static String password = "automationDB@123";
	static Connection connection;
	static Statement st;
	static JFrame f;
	static JPanel p1, p2, p3,p4;
	static JTabbedPane tp;
	 
	static JLabel l1, l2, l3, l4,ap1,ap2,slobj,slpage;
	
	static JTextField tf1, tf2, tf3, tf4,pn,pd,sobj,spage;
	
	static JScrollPane sp1;
	
	static JButton savebtn, resetbtn, editbtn, addPage,displayPage,clearPage,searchobj,searchPage,selectPage;
	
	
	static JComboBox PageList = new JComboBox();
	
	static Image addList,refresh,search,clear;
	
static String pages[]=new String[500];
 static int page_id=1;
 static{ // would have to be surrounded by try catch
	    try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection(url,userid, password);
            st = connection.createStatement();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}   // this will load the class Driver
	}
 static void selectPage()
 {
	 String sql = "SELECT * FROM page_repo";
	 int i=0;
	 try {
		ResultSet rs= st.executeQuery(sql);
		//System.out.println(rs.toString());
		//System.out.println(rs.getMetaData());
		//rs.next();
		while(rs.next())
		{
			//pageValues.add(rs.getObject(1).toString());
			//pageList.addItem(rs.getObject("page_name").toString());
			pages[i] = rs.getObject("page_name").toString();
			i++;
		}
		PageList.removeAllItems();
   	 for (int j=0;j<pages.length;j++)
   	 {
   		 if(pages[j]!=null)
   		 {
   		 PageList.addItem(pages[j]);
   		//System.out.println("Adding pages");
   		 //System.out.println(pages[j]);
   		 }
   	 }
		
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	 
 }
 ObjAccess() {
	 selectPage();
     f = new JFrame("Object Repository");
     p1 = new JPanel(new GridLayout(13, 1));
     p2 = new JPanel(new GridLayout(6, 2));
     p4 = new JPanel(new GridLayout(9, 2));

     tp = new JTabbedPane();
     l1 = new JLabel("Id");
     l2 = new JLabel("Name");
     l3 = new JLabel("Value");
     l4 = new JLabel("Page");
     ap1 = new JLabel("Page Name");
     ap2 = new JLabel("Page Description");
     slobj= new JLabel("Object Name/Value/Page to be searched");
     slpage= new JLabel("Page Name/Description to be searched");
     tf1 = new JTextField(12);
     tf2 = new JTextField(12);
     tf3 = new JTextField(12);
     tf4 = new JTextField(12);
     pn = new JTextField(12);
     pd = new JTextField(12);
     sobj= new JTextField(12);
     spage= new JTextField(12);
     try {
    	    addList = ImageIO.read(getClass().getResource("AddList-48.png"));
    	   refresh = ImageIO.read(getClass().getResource("Refresh-50.png"));
    	    search = ImageIO.read(getClass().getResource("Search-48.png"));
    	    clear = ImageIO.read(getClass().getResource("Broom-48.png"));
    	    
    	  } catch (IOException ex) {
    		  System.out.println("Image not found");
    	  }
     
     /*try {
		ResultSet rs = st.executeQuery("select page_name from page_repo");
		 int i = 1;
		while(rs.next())
        {
			comboBox.addItem(rs.getObject(i).toString());
        }
		
		
	} catch (SQLException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}*/
     pn.setText("");
     pd.setText("");
     
     savebtn = new JButton(" Add Object");
     savebtn.setIcon(new ImageIcon(addList));
     savebtn.setFont(new Font("Arial", Font.BOLD, 30));
     resetbtn = new JButton(" Clear");
     resetbtn.setFont(new Font("Arial", Font.BOLD, 30));
     resetbtn.setIcon(new ImageIcon(clear));
     editbtn = new JButton(" Display Object Repo ");
     editbtn.setIcon(new ImageIcon(search));
     editbtn.setFont(new Font("Arial", Font.BOLD, 30));
     addPage= new JButton(" Add Page");
     addPage.setIcon(new ImageIcon(addList));
     addPage.setFont(new Font("Arial", Font.BOLD, 30));
     //addPage.setBackground(Color.green);
     displayPage= new JButton(" Display Page Repo");
     displayPage.setIcon(new ImageIcon(search));
     displayPage.setFont(new Font("Arial", Font.BOLD, 30));
     clearPage= new JButton(" Clear ");
     clearPage.setIcon(new ImageIcon(clear));
     clearPage.setFont(new Font("Arial", Font.BOLD, 30));
     searchobj= new JButton(" Search Object by name/value/page ");
     //searchobj.setBackground(Color.cyan);
     searchobj.setIcon(new ImageIcon(search));
     searchobj.setFont(new Font("Arial", Font.BOLD, 30));
     searchPage= new JButton(" Search Page by Page Name/Description ");
     searchPage.setIcon(new ImageIcon(search));
     searchPage.setFont(new Font("Arial", Font.BOLD, 30));
     selectPage= new JButton(" Refresh Page ");
     selectPage.setIcon(new ImageIcon(refresh));
     selectPage.setFont(new Font("Arial", Font.BOLD, 30));
     /*comboBox.addActionListener(new ActionListener() { 
    	   public void actionPerformed(ActionEvent e) {
    		   comboBox.setToolTipText("You will Select : " +      
    	       ((JComboBox)e.getSource()).getSelectedItem());
    	   }
    	 });*/
     //p1.add(l1);
     //p1.add(tf1);
     
     //pageName="Root";
     
		PageList.setEditable(false);
		PageList.addActionListener(new ActionListener() {
	         public void actionPerformed(ActionEvent ae) {
	        	 
	        	 
	        	 //PageList= new JComboBox(pages);
	        	 //PageList.removeAllItems();
	        	 
	        	 //PageList = PageList.addItem(pages);
	        	 String pageName;
	        	 
	        	 /*String sql = "SELECT page_name FROM page_repo";
	        	 int i=0;
	        	 try {
					ResultSet rs= st.executeQuery(sql);
					while(rs.next())
					{
						//pageValues.add(rs.getObject(1).toString());
						pages[i] = rs.getObject(1).toString();
						i++;
					}*/
	        		 
					pageName = (String)PageList.getSelectedItem();
				    //PageList.addActionListener(this);
					//System.out.println(pageName);
					tf4.setText(pageName);
					//selectPage();
				}/* catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        	 
	             
	         }*/
	     });
	    
     tf4.setEditable(false);
     tf4.setText("Root");
     p1.add(slobj);
     p1.add(sobj);
     p1.add(searchobj);
     
     //p1.add(editbtn);
     
     p1.add(l2);
     p1.add(tf2);
     p1.add(l3);
     p1.add(tf3);
     p1.add(l4);
     p1.add(tf4);
     p1.add(PageList);
     
     //pageList = JComboBox(pageValues);
     //p1.add(comboBox);
     p1.add(savebtn);
     p1.add(selectPage);
     p1.add(resetbtn);

     p4.add(slpage);
     p4.add(spage);
     p4.add(searchPage);
     //p4.add(displayPage);
     
     p4.add(ap1);
     p4.add(pn);
     p4.add(ap2);
     p4.add(pd);
     pn.setText(null);
     pd.setText(null);
     p4.add(addPage);
     p4.add(clearPage);
     
     selectPage.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent ae) {
        	 selectPage();
							
			 
         }
     });
     
     clearPage.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent ae) {
             clearPage();
         }
     });
     
     displayPage.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent ae) {
        	 String sql = "SELECT * FROM page_repo";
        	 
             PageTableFromMySqlDatabase frame = new PageTableFromMySqlDatabase(sql);
             // frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
             frame.pack();
             frame.setVisible(true);
         }
     });
     searchPage.addActionListener(new ActionListener() {
    	 String searchValue;
     public void actionPerformed(ActionEvent ae) {
    	 searchValue=spage.getText();
    	 String sql = "SELECT * FROM page_repo where page_name like '%"+searchValue+"%' or page_desc like '%"+searchValue+"%'";
    	 PageTableFromMySqlDatabase frame = new PageTableFromMySqlDatabase(sql);
         // frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
         frame.pack();
         frame.setVisible(true);
     }
 });
     addPage.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent ae) {
             int id;
             String pageName,pageDescvalue;
             //id = Integer.parseInt(tf1.getText());
             pageName = pn.getText();
             pageDescvalue = pd.getText();
             //System.out.println(name);
             //System.out.println(pageName);
            // System.out.println(pageDescvalue);
             String url = "jdbc:mysql://10.222.115.161:3306/automationDB";
             String userid = "root";
             String password = "root@123";
             String sql =null;
             try {
                 Connection connection = DriverManager.getConnection(url,
                         userid, password);
                 Statement st = connection.createStatement();
                 //value=value.replace("'","''");
                 if (pageName !=null && pageDescvalue !=null ) {
                	 sql = "INSERT INTO page_repo (page_name, page_desc, parent_page_id) VALUES('" + pageName + "','" + pageDescvalue + "','1')";
                     st.executeUpdate(sql);
                     //"INSERT INTO obj_repo (obj_name, obj_value) VALUES('" + key + "','" + value + "')";
                     //System.out.println("Insert record successfully");
                     clearPage();
                 } else {
                     //System.out.println("Please fill up textfield");
                 }
                 PageList.addItem(pageName);
             } catch (Exception ex) {
                 ex.printStackTrace();
             }
         }
     });
     
     resetbtn.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent ae) {
             clear();
         }
     });
     savebtn.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent ae) {
             int id;
             String name,value,page;
             //id = Integer.parseInt(tf1.getText());
             name = tf2.getText();
             value = tf3.getText();
             page = tf4.getText();
             System.out.println(name);
             System.out.println(value);
             System.out.println(page);
             String url = "jdbc:mysql://10.222.115.161:3306/automationDB";
             String userid = "root";
             String password = "root@123";
             String sql =null;
             try {
                 Connection connection = DriverManager.getConnection(url,
                         userid, password);
                 Statement st = connection.createStatement();
                 value=value.replace("'","''");
                 if(!page.equalsIgnoreCase("Root"))
                 {
                	 sql = "select page_id from page_repo where page_name = '"+page+"'";
                	 ResultSet rs=st.executeQuery(sql);
                	 rs.next();
                	 page_id=(int) rs.getObject(1);
                	// System.out.println(page_id);
                     
                 }
                 if (name != "" && value != "" && page != "") {
                	 sql = "INSERT INTO obj_repo (obj_name, obj_value,PageName,page_id) VALUES('" + name + "','" + value + "','" + page + "','" + page_id + "')";
                     st.executeUpdate(sql);
                     //"INSERT INTO obj_repo (obj_name, obj_value) VALUES('" + key + "','" + value + "')";
                     //System.out.println("Insert record successfully");
                     clear();
                 } else {
                    // System.out.println("Please fill up textfield");
                 }
             } catch (Exception ex) {
                 ex.printStackTrace();
             }
         }
     });

     editbtn.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent ae) {
        	 String sql = "SELECT * FROM obj_repo";
             TableFromMySqlDatabase frame = new TableFromMySqlDatabase(sql);
             // frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
             frame.pack();
             frame.setVisible(true);
         }
     });
 
     searchobj.addActionListener(new ActionListener() {
    	 String searchValue;
     public void actionPerformed(ActionEvent ae) {
    	 searchValue=sobj.getText();
    	 String sql = "SELECT * FROM obj_repo where obj_name like '%"+searchValue+"%' or obj_value like '%"+searchValue+"%' or PageName like '%"+searchValue+"%'";
         TableFromMySqlDatabase frame = new TableFromMySqlDatabase(sql);
         // frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
         frame.pack();
         frame.setVisible(true);
     }
 });
}
 void dis() {
	 //selectPage();
     f.getContentPane().add(tp);
     tp.addTab("Add/Edit/Delete Object", p1);
     //tp.addTab("Edit/Delete Object", p2);
     tp.addTab("Add/Edit/Delete Page", p4);

     //f.setSize(550, 280);
     f.setSize(1500, 750);
     f.setVisible(true);
     f.setResizable(true);
 }
 
 void clear()
 {
     tf1.setText("");
     tf2.setText("");
     tf3.setText("");
     tf4.setText("Root");
 } 
 void clearPage()
 {
     pn.setText("");
     pd.setText("");
  }  

 public static void main(String z[]) {
	 
	 ObjAccess data = new ObjAccess();
     data.dis();
 }
}

class TableFromMySqlDatabase extends JFrame {
	
 public TableFromMySqlDatabase(String sql) {
     Vector columnNames = new Vector();
     Vector data = new Vector();

     // Connect to an MySQL Database, run query, get result set
     String url = "jdbc:mysql://10.222.115.161:3306/automationDB";
     String userid = "root";
     String password = "root@123";
     //String sql = "SELECT * FROM obj_repo";

     try {
         Connection connection = DriverManager.getConnection(url, userid,
                 password);
         Statement stmt = connection.createStatement();
         ResultSet rs = stmt.executeQuery(sql);
         ResultSetMetaData md = rs.getMetaData();
         int columns = md.getColumnCount();

         // Get column names
         for (int i = 1; i <= columns; i++) {
             columnNames.add(md.getColumnName(i));
         }

         // Get row data
         while (rs.next()) {
             Vector row = new Vector(columns);

             for (int i = 1; i <= columns; i++) {
                 row.add(rs.getObject(i));
             }
             row.add("Delete");
             row.add("Update");
             data.add(row);
         }
     } catch (SQLException e) {
         System.out.println(e.getMessage());
     }

     columnNames.add("Delete");
     columnNames.add("Update");
     //columnNames.add("Object_Value");
     //columnNames.add("Page");
     // Create table with database data
     JTable table = new JTable(data, columnNames) {
         public Class getColumnClass(int column) {
             for (int row = 0; row < getRowCount(); row++) {
                 Object o = getValueAt(row, column);

                 if (o != null) {
                     return o.getClass();
                 }
             }
             return Object.class;
         }
     };

     table.getColumn("Delete").setCellRenderer(new objButtonRenderer());
     table.getColumn("Delete").setCellEditor(
             new objButtonEditor(new JCheckBox(), table));
     table.getColumn("Update").setCellRenderer(new objButtonRenderer());
     table.getColumn("Update").setCellEditor(
             new objButtonEditor(new JCheckBox(), table));

     JScrollPane scrollPane = new JScrollPane(table);
     getContentPane().add(scrollPane);

     JPanel buttonPanel = new JPanel();
     getContentPane().add(buttonPanel, BorderLayout.NORTH);
 }

 /*
  * public static void main(String[] args) { TableFromMySqlDatabase frame =
  * new TableFromMySqlDatabase();
  * frame.setDefaultCloseOperation(EXIT_ON_CLOSE); frame.pack();
  * frame.setVisible(true); }
  */

}
class PageTableFromMySqlDatabase extends JFrame {
	
	 public PageTableFromMySqlDatabase(String sql) {
	     Vector columnNames = new Vector();
	     Vector data = new Vector();

	     // Connect to an MySQL Database, run query, get result set
	     String url = "jdbc:mysql://10.222.115.161:3306/automationDB";
	     String userid = "root";
	     String password = "root@123";
	     //String sql = "SELECT * FROM obj_repo";

	     try {
	         Connection connection = DriverManager.getConnection(url, userid,
	                 password);
	         Statement stmt = connection.createStatement();
	         ResultSet rs = stmt.executeQuery(sql);
	         ResultSetMetaData md = rs.getMetaData();
	         int columns = md.getColumnCount();

	         // Get column names
	         for (int i = 1; i <= columns; i++) {
	             columnNames.add(md.getColumnName(i));
	         }

	         // Get row data
	         while (rs.next()) {
	             Vector row = new Vector(columns);

	             for (int i = 1; i <= columns; i++) {
	                 row.add(rs.getObject(i));
	             }
	             row.add("Delete");
	             row.add("Update");
	             data.add(row);
	         }
	     } catch (SQLException e) {
	         System.out.println(e.getMessage());
	     }

	     columnNames.add("Delete");
	     columnNames.add("Update");
	     //columnNames.add("Object_Value");
	     //columnNames.add("Page");
	     // Create table with database data
	     JTable table = new JTable(data, columnNames) {
	         public Class getColumnClass(int column) {
	             for (int row = 0; row < getRowCount(); row++) {
	                 Object o = getValueAt(row, column);

	                 if (o != null) {
	                     return o.getClass();
	                 }
	             }
	             return Object.class;
	         }
	     };

	     table.getColumn("Delete").setCellRenderer(new pageButtonRenderer());
	     table.getColumn("Delete").setCellEditor(
	             new pageButtonEditor(new JCheckBox(), table));
	     table.getColumn("Update").setCellRenderer(new pageButtonRenderer());
	     table.getColumn("Update").setCellEditor(
	             new pageButtonEditor(new JCheckBox(), table));

	     JScrollPane scrollPane = new JScrollPane(table);
	     getContentPane().add(scrollPane);

	     JPanel buttonPanel = new JPanel();
	     getContentPane().add(buttonPanel, BorderLayout.NORTH);
	 }

	 /*
	  * public static void main(String[] args) { TableFromMySqlDatabase frame =
	  * new TableFromMySqlDatabase();
	  * frame.setDefaultCloseOperation(EXIT_ON_CLOSE); frame.pack();
	  * frame.setVisible(true); }
	  */

	}

class pageButtonRenderer extends JButton implements TableCellRenderer {
 public pageButtonRenderer() {
     setOpaque(true);
 }

 public Component getTableCellRendererComponent(JTable table, Object value,
         boolean isSelected, boolean hasFocus, int row, int column) {

     setText((value == null) ? "" : value.toString());
     return this;
 }
}

class pageButtonEditor extends DefaultCellEditor {
 protected JButton button;

 private String label;
 JTable t1;
 private boolean isPushed;
 int index;
 String pageName, pageValue,Page,parentPage;

 public pageButtonEditor(JCheckBox checkBox, JTable t1) {
     super(checkBox);
     this.t1 = t1;
     button = new JButton();
     button.setOpaque(true);
     button.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
             fireEditingStopped();
         }
     });
 }

 public Component getTableCellEditorComponent(JTable table, Object value,
         boolean isSelected, int row, int column) {

     index = Integer.parseInt(t1.getModel().getValueAt(row, 0).toString());
     pageName = t1.getModel().getValueAt(row, 1).toString();
     pageValue = t1.getModel().getValueAt(row, 2).toString();
     parentPage= t1.getModel().getValueAt(row, 3).toString();
     //ObjValue = ObjValue.replace("'","''");
     //Page = t1.getModel().getValueAt(row, 3).toString();

     label = (value == null) ? "" : value.toString();
     button.setText(label);
     isPushed = true;
     return button;
 }

 // update, delete
 public Object getCellEditorValue() {
     if (isPushed) {
         String url = "jdbc:mysql://10.222.115.161:3306/automationDB";
         String userid = "root";
         String password = "root@123";

         try {
             Connection connection = DriverManager.getConnection(url,
                     userid, password);
             Statement stmt = connection.createStatement();
String updateSql="update page_repo set page_name='" + pageName
+ "', page_desc='" + pageValue + "',parent_page_id='" + parentPage + "' where page_id='"
+ index + "'";
//System.out.println(updateSql);
             if (label.equalsIgnoreCase("Update")) {
                 stmt.executeUpdate(updateSql);
                 JOptionPane.showMessageDialog(button, label
                         + ":Update page_repo record success");

             } else if (label.equalsIgnoreCase("Delete")) {
                 stmt.executeUpdate("delete from page_repo where page_id='" + index
                         + "'");

                 JOptionPane.showMessageDialog(button, label
                         + ":Delete page_repo record success");

             }

         } catch (Exception ex) {
             System.out.println(ex);
         }
     }
     isPushed = false;
     return new String(label);
 }

 public boolean stopCellEditing() {
     isPushed = false;
     return super.stopCellEditing();
 }

 protected void fireEditingStopped() {
     super.fireEditingStopped();
 }
}
//For Object

class objButtonRenderer extends JButton implements TableCellRenderer {
	 public objButtonRenderer() {
	     setOpaque(true);
	 }

	 public Component getTableCellRendererComponent(JTable table, Object value,
	         boolean isSelected, boolean hasFocus, int row, int column) {

	     setText((value == null) ? "" : value.toString());
	     return this;
	 }
	}

	class objButtonEditor extends DefaultCellEditor {
	 protected JButton button;

	 private String label;
	 JTable t1;
	 private boolean isPushed;
	 int index,pageID;
	 String objName, objValue=null,Page;

	 public objButtonEditor(JCheckBox checkBox, JTable t1) {
	     super(checkBox);
	     this.t1 = t1;
	     button = new JButton();
	     button.setOpaque(true);
	     button.addActionListener(new ActionListener() {
	         public void actionPerformed(ActionEvent e) {
	             fireEditingStopped();
	         }
	     });
	 }

	 public Component getTableCellEditorComponent(JTable table, Object value,
	         boolean isSelected, int row, int column) {

	     index = Integer.parseInt(t1.getModel().getValueAt(row, 0).toString());
	     objName = t1.getModel().getValueAt(row, 1).toString();
	     objValue = t1.getModel().getValueAt(row, 2).toString();
	     objValue = objValue.replace("'","''");
	     pageID= Integer.parseInt(t1.getModel().getValueAt(row, 3).toString());
	     //if(!t1.getModel().getValueAt(row, 3).toString().equalsIgnoreCase(null))
	     Page = t1.getModel().getValueAt(row, 4).toString();

	     label = (value == null) ? "" : value.toString();
	     button.setText(label);
	     isPushed = true;
	     return button;
	 }

	 // update, delete
	 public Object getCellEditorValue() {
	     if (isPushed) {
	         String url = "jdbc:mysql://10.222.115.161:3306/automationDB";
	         String userid = "root";
	         String password = "root@123";

	         try {
	             Connection connection = DriverManager.getConnection(url,
	                     userid, password);
	             Statement stmt = connection.createStatement();
	String updateSql="update obj_repo set obj_name='" + objName
	+ "', obj_value='" + objValue + "', PageName='" + Page + "', page_id='" + pageID + "' where obj_id='"
	+ index + "'";
	//System.out.println(updateSql);
	             if (label.equalsIgnoreCase("Update")) {
	                 stmt.executeUpdate(updateSql);
	                 JOptionPane.showMessageDialog(button, label
	                         + ":Update obj_repo record success");

	             } else if (label.equalsIgnoreCase("Delete")) {
	                 stmt.executeUpdate("delete from obj_repo where obj_id='" + index
	                         + "'");

	                 JOptionPane.showMessageDialog(button, label
	                         + ":Delete obj_repo record success");

	             }

	         } catch (Exception ex) {
	             System.out.println(ex);
	         }
	     }
	     isPushed = false;
	     return new String(label);
	 }

	 public boolean stopCellEditing() {
	     isPushed = false;
	     return super.stopCellEditing();
	 }

	 protected void fireEditingStopped() {
	     super.fireEditingStopped();
	 }
	}
	