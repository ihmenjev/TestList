import java.awt.Dimension;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;


public class TablePerson extends JFrame{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	static int i = 0;
	 
    public TablePerson(ArrayList<Person> person) {
 
        super("Table of persons");
       //setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 
        
 
        TableModel model = new MyTableModel(person);
        JTable table = new JTable(model);
 
        getContentPane().add(new JScrollPane(table));
 
        setPreferredSize(new Dimension(800, 500));
        //setBounds(100, 100, 800, 500);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
 
    
 
    public class MyTableModel implements TableModel {
 
        private Set<TableModelListener> listeners = new HashSet<TableModelListener>();
 
        private List<Person> persons;
 
        public MyTableModel(List<Person> persons) {
            this.persons = persons;
        }
 
        public void addTableModelListener(TableModelListener listener) {
            listeners.add(listener);
        }
 
        public Class<?> getColumnClass(int columnIndex) {
            return String.class;
        }
 
        public int getColumnCount() {
            return 6;
        }
 
        public String getColumnName(int columnIndex) {
            switch (columnIndex) {
            case 0:
                return DBengine.COLUMN_ID;
            case 1:
                return DBengine.COLUMN_NAME;
            case 2:
                return DBengine.COLUMN_SURNAME;
            case 3:
                return DBengine.COLUMN_LOGIN;
            case 4:
                return DBengine.COLUMN_EMAIL;
            case 5:
                return DBengine.COLUMN_PHONE;
            }
            
            return "";
        }
 
        public int getRowCount() {
            return persons.size();
        }
 
        public Object getValueAt(int rowIndex, int columnIndex) {
            Person person = persons.get(rowIndex);
            switch (columnIndex) {
            case 0:
                return person.getiId();
            case 1:
                return person.getName();
            case 2:
                return person.getSurname();
            case 3:
                return person.getLogin();
            case 4:
                return person.getEmail();
            case 5:
                return person.getPhoneN();
            }
            return "";
        }
 
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return false;
        }
 
        public void removeTableModelListener(TableModelListener listener) {
            listeners.remove(listener);
        }
 
        public void setValueAt(Object value, int rowIndex, int columnIndex) {
 
        }
 
    }
 
    /*public class MyBean {
 
        private String name;
        private String size;
        private String description;
 
        public MyBean(String name, String size, String description) {
            this.setName(name);
            this.setSize(size);
            this.setDescription(description);
        }
 
        public void setName(String name) {
            this.name = name;
        }
 
        public String getName() {
            return name;
        }
 
        public void setSize(String size) {
            this.size = size;
        }
 
        public String getSize() {
            return size;
        }
 
        public void setDescription(String description) {
            this.description = description;
        }
 
        public String getDescription() {
            return description;
        }
    }*/
}
