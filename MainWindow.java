import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.prefs.Preferences;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.filechooser.FileFilter;

public class MainWindow {

	public String connectState;
	private long lastModif;
	private String nameTable;
	private static final String LAST_MODIF = "last_modif";
	private JLabel lStatus;
	private static final String DB_NAME = "db_name";
	private static final String DB_USER = "db_user";
	private static final String DB_PASS = "db_pass";
	private static final String TABLE_NAME = "table_name";
	private static final String FILE_NAME = "file_name";
	private String selectAll;
	DBengine dbengine;
	private JPanel pManageFile, pManageDb, pManageNameDb, pManageNameUsr,
			pManageNamePass, panel2, panel3, servicePanel;
	String select;
	String sortBy;
	private File file;

	public MainWindow() {
		final JRadioButton rbName = new JRadioButton("Name", true);
		final JRadioButton rbSurName = new JRadioButton("Surname");
		final JRadioButton rbLogin = new JRadioButton("Login");
		final JRadioButton rbEmail = new JRadioButton("Email");
		final JRadioButton rbPhone = new JRadioButton("Phone");

		pManageFile = new JPanel();
		pManageFile.setLayout(new BoxLayout(pManageFile, BoxLayout.X_AXIS));

		JButton btnChuseFile = new JButton("Choose File");
		btnChuseFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileopen = new JFileChooser();

				fileopen.setFileFilter(new FileFilter() {

					@Override
					public String getDescription() {
						// TODO Auto-generated method stub
						return "CSV File (*.csv)";
					}

					@Override
					public boolean accept(File f) {
						String fName = f.getName().toLowerCase();
						if (fName.endsWith(".csv") || f.isDirectory()) {
							return true;
						} else {
							return false;
						}
					}
				});
				int ret = fileopen.showDialog(null, "Открыть файл");
				if (ret == JFileChooser.APPROVE_OPTION) {
					file = null;
					file = fileopen.getSelectedFile();
					long lastM = file.lastModified();

					System.out.println(lastM);
					System.out.println(getLastModif());

					saveToPref(FILE_NAME, file.getAbsolutePath());

					nameTable = file.getName().split("\\.")[0];
					System.out.println(nameTable);
					dbengine.createTable(nameTable);
					saveToPref(TABLE_NAME, nameTable);

					selectAll = "SELECT * FROM " + getFromPref(TABLE_NAME);

					boolean isEmptyT = dbengine.isEmptyTable(nameTable);
					if (isEmptyT) {
						dbengine.updateTable((ParceCsv.getPersFromFile(file
								.getAbsolutePath())), nameTable);
						lStatus.setForeground(Color.GREEN);
						lStatus.setText("Import successful");
					}
					if (lastM != getLastModif()) {
						setLastModif(lastM);

						dbengine.updateTable(ParceCsv.getPersFromFile(file
								.getAbsolutePath()), nameTable);
						lStatus.setForeground(Color.GREEN);
						lStatus.setText("Import successful");

					} else {
						if (!isEmptyT) {
							lStatus.setForeground(Color.BLUE);
							lStatus.setText("The same file , there is no need to reimport");// not
						} // insert
							// list
							// to
							// the
							// table
					}
					// label.setText(file.getName());
					// new TablePerson(ParceCsv
					// .getPersFromFile(file.getName()));
					// ParceCsv.getPersFromFile(file.getName());

				}
			}
		});

		btnChuseFile.setAlignmentX(JFrame.LEFT_ALIGNMENT);

		JButton btnUpdate = new JButton("Update");
		btnUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				file = new File(getFromPref(FILE_NAME));
				long lastMU = file.lastModified();
				nameTable = getFromPref(TABLE_NAME);

				if (lastMU != getLastModif()) {
					setLastModif(lastMU);
					dbengine.updateTable(
							(ParceCsv.getPersFromFile(file.getAbsolutePath())),
							getFromPref(TABLE_NAME));
					lStatus.setForeground(Color.GREEN);
					lStatus.setText("Table Update successful");
				} else {
					lStatus.setForeground(Color.BLUE);
					lStatus.setText("No Update necessary");
				}
			}
		});
		JButton btnShowTable = new JButton("Show Table");
		btnShowTable.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new TablePerson(dbengine.queryTable("SELECT * FROM "
						+ getFromPref(TABLE_NAME), getFromPref(TABLE_NAME)));

			}
		});

		lStatus = new JLabel("***");

		pManageFile.add(btnChuseFile);
		pManageFile.add(btnUpdate);
		pManageFile.add(btnShowTable);

		pManageDb = new JPanel();
		pManageDb.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		pManageDb.setLayout(new BoxLayout(pManageDb, BoxLayout.Y_AXIS));

		pManageNameDb = new JPanel();

		pManageNameDb.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		pManageNameDb.setLayout(new BoxLayout(pManageNameDb, BoxLayout.X_AXIS));
		final JTextArea etDbName = new JTextArea("db_name");
		etDbName.setBorder(BorderFactory.createLineBorder(Color.black));
		etDbName.setAlignmentX(JPanel.LEFT_ALIGNMENT);
		JLabel lEntName = new JLabel(
				"Please, enter name of existin db / name db to create:_");

		pManageNameDb.add(lEntName);
		pManageNameDb.add(etDbName);

		pManageNameUsr = new JPanel();
		// pManageNameUsr.setAlignmentX(JPanel.LEFT_ALIGNMENT);
		pManageNameUsr.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		pManageNameUsr
				.setLayout(new BoxLayout(pManageNameUsr, BoxLayout.X_AXIS));
		final JTextArea etDbUsr = new JTextArea("root");
		etDbUsr.setBorder(BorderFactory.createLineBorder(Color.black));
		etDbUsr.setAlignmentX(JPanel.LEFT_ALIGNMENT);
		JLabel lEntUsr = new JLabel(
				"Please, enter Login to access to your MySQL engine:_");

		pManageNameUsr.add(lEntUsr);
		pManageNameUsr.add(etDbUsr);

		pManageNamePass = new JPanel();
		// pManageNameUsr.setAlignmentX(JPanel.LEFT_ALIGNMENT);
		pManageNamePass.setBorder(BorderFactory.createEmptyBorder(5, 5, 15, 5));
		pManageNamePass.setLayout(new BoxLayout(pManageNamePass,
				BoxLayout.X_AXIS));
		final JTextArea etDbPass = new JTextArea("***");
		etDbPass.setBorder(BorderFactory.createLineBorder(Color.black));
		etDbPass.setAlignmentX(JPanel.LEFT_ALIGNMENT);
		JLabel lEntPass = new JLabel(
				"Please, enter Pass to access to your MySQL engine:_");

		pManageNamePass.add(lEntPass);
		pManageNamePass.add(etDbPass);

		JButton btnConnect = new JButton("Connect/create DB");
		btnConnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if ((etDbName.getText() != getFromPref(DB_NAME))
						&& (etDbUsr.getText() != getFromPref(DB_USER))
						&& (etDbPass.getText() != getFromPref(DB_PASS))) {
					saveToPref(DB_NAME, etDbName.getText());
					saveToPref(DB_USER, etDbUsr.getText());
					saveToPref(DB_PASS, etDbPass.getText());

				}
				if ((getFromPref(DB_NAME) != "")
						&& (getFromPref(DB_USER) != "")
						&& (getFromPref(DB_PASS)) != "") {
					dbengine.closeConnection();
					dbengine = new DBengine(getFromPref(DB_NAME),
							getFromPref(DB_USER), getFromPref(DB_PASS));

					dbengine.connectToDb();

				}
				try {
					boolean isConn = dbengine.isConnected();

					if (isConn) {
						lStatus.setText("connected do db "
								+ getFromPref(DB_NAME) + " succsessful!");
						lStatus.setForeground(Color.GREEN);
						pManageDb.setVisible(false);
						pManageFile.setVisible(true);
						servicePanel.setVisible(true);

					} else {
						lStatus.setText("connect do db failed!");
						lStatus.setForeground(Color.RED);
						pManageDb.setVisible(true);
						pManageFile.setVisible(false);
						servicePanel.setVisible(false);
					}
				} catch (NullPointerException ex) {
					ex.printStackTrace();

				}

			}

		});

		pManageDb.add(pManageNameDb);
		pManageDb.add(pManageNameUsr);
		pManageDb.add(pManageNamePass);
		pManageDb.add(btnConnect);

		panel2 = new JPanel();
		panel2.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		panel2.setLayout(new BoxLayout(panel2, BoxLayout.X_AXIS));

		panel3 = new JPanel();
		panel3.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		panel3.setLayout(new BoxLayout(panel3, BoxLayout.X_AXIS));

		servicePanel = new JPanel();
		servicePanel.setLayout(new BoxLayout(servicePanel, BoxLayout.Y_AXIS));

		JPanel mainpanel = new JPanel();
		mainpanel.setLayout(new BoxLayout(mainpanel, BoxLayout.Y_AXIS));
		JFrame frame = new JFrame("TestList");
		JLabel label1 = new JLabel("Please, click Rbutton to sort by:");

		label1.setAlignmentX(JPanel.LEFT_ALIGNMENT);
		// label1.setAlignmentY(JPanel.CENTER_ALIGNMENT);
		panel3.setAlignmentX(JPanel.LEFT_ALIGNMENT);

		JLabel label2 = new JLabel(
				"disconnect or remove the current database and table if necessary");

		servicePanel.add(label1);
		servicePanel.add(panel2);
		servicePanel.add(label2);
		servicePanel.add(panel3);
		pManageFile.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

		JButton sortUp = new JButton("ascending sorting");

		sortUp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				if (rbName.isSelected()) {
					sortBy = DBengine.COLUMN_NAME;
				} else if (rbSurName.isSelected()) {
					sortBy = DBengine.COLUMN_SURNAME;
				} else if (rbLogin.isSelected()) {
					sortBy = DBengine.COLUMN_LOGIN;
				} else if (rbEmail.isSelected()) {
					sortBy = DBengine.COLUMN_EMAIL;
				} else if (rbPhone.isSelected()) {
					sortBy = DBengine.COLUMN_PHONE;
				}
				select = selectAll + " ORDER BY " + sortBy;
				String selectUp = select + " ASC";
				new TablePerson(dbengine.queryTable(selectUp,
						getFromPref(TABLE_NAME)));
				System.out.println(selectUp);
			}
		});

		JButton sortDown = new JButton("descending sorting");
		sortDown.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (rbName.isSelected()) {
					sortBy = DBengine.COLUMN_NAME;
				} else if (rbSurName.isSelected()) {
					sortBy = DBengine.COLUMN_SURNAME;
				} else if (rbLogin.isSelected()) {
					sortBy = DBengine.COLUMN_LOGIN;
				} else if (rbEmail.isSelected()) {
					sortBy = DBengine.COLUMN_EMAIL;
				} else if (rbPhone.isSelected()) {
					sortBy = DBengine.COLUMN_PHONE;
				}
				select = selectAll + " ORDER BY " + sortBy;
				String selectDown = select + " DESC";
				new TablePerson(dbengine.queryTable(selectDown,
						getFromPref(TABLE_NAME)));
				System.out.println(selectDown);
			}
		});

		JButton deleteDB = new JButton("delete cur. DB");
		JButton deleteTab = new JButton("delete cur. table");
		deleteTab.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				dbengine.deleteTable(getFromPref(TABLE_NAME));
				setLastModif(0);

				if (!dbengine.isExistTable(getFromPref(TABLE_NAME)))
					lStatus.setText("Table deleted successful");

				saveToPref(FILE_NAME, "");
				saveToPref(TABLE_NAME, "");

			}
		});
		JButton btnDiscDb = new JButton("disconnect DB");
		btnDiscDb.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dbengine.closeConnection();
				lStatus.setText("connect do db failed!");
				lStatus.setForeground(Color.RED);
				pManageDb.setVisible(true);
				pManageFile.setVisible(false);
				servicePanel.setVisible(false);
			}
		});
		deleteDB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dbengine.closeConnection();
				dbengine.deleteDB(getFromPref(DB_NAME));
				lStatus.setText("connect do db failed!");
				lStatus.setForeground(Color.RED);
				pManageDb.setVisible(true);
				pManageFile.setVisible(false);
				servicePanel.setVisible(false);

				saveToPref(DB_USER, "");
				saveToPref(DB_PASS, "");
				saveToPref(DB_NAME, "");
				saveToPref(FILE_NAME, "");
				saveToPref(TABLE_NAME, "");
			}
		});

		ButtonGroup bgrp = new ButtonGroup();
		bgrp.add(rbName);
		bgrp.add(rbSurName);
		bgrp.add(rbLogin);
		bgrp.add(rbEmail);
		bgrp.add(rbPhone);

		panel2.add(sortUp);

		panel2.add(rbName);
		panel2.add(rbSurName);
		panel2.add(rbLogin);
		panel2.add(rbEmail);
		panel2.add(rbPhone);

		panel2.add(sortDown);
		panel3.add(btnDiscDb);
		panel3.add(deleteDB);
		panel3.add(deleteTab);

		mainpanel.add(pManageDb);
		mainpanel.add(lStatus);

		mainpanel.add(pManageFile);

		mainpanel.add(servicePanel);

		frame.add(mainpanel);

		frame.setSize(700, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		// pManageDb.setVisible(true);
		// pManageFile.setVisible(false);
		// servicePanel.setVisible(false);
		System.out.println(getFromPref(DB_NAME));
		System.out.println(getFromPref(DB_USER));
		System.out.println(getFromPref(DB_PASS));
		if ((getFromPref(DB_NAME) != "") && (getFromPref(DB_USER) != "")
				&& (getFromPref(DB_PASS)) != "") {

			dbengine = new DBengine(getFromPref(DB_NAME), getFromPref(DB_USER),
					getFromPref(DB_PASS));

			try {
				dbengine.connectDb();
			} catch (Exception e) {
				e.printStackTrace();
				lStatus.setText("connect do db failed!");
			}

		}
		try {
			boolean isConn = dbengine.isConnected();

			if (isConn) {
				lStatus.setText("connected do db " + getFromPref(DB_NAME)
						+ " succsessful!");
				lStatus.setForeground(Color.GREEN);
				pManageDb.setVisible(false);
				pManageFile.setVisible(true);
				servicePanel.setVisible(true);

			} else {
				lStatus.setText("connect do db failed!");
				lStatus.setForeground(Color.RED);
				pManageDb.setVisible(true);
				pManageFile.setVisible(false);
				servicePanel.setVisible(false);
			}
		} catch (NullPointerException e) {
			e.printStackTrace();

		}

	}

	public long getLastModif() {
		Preferences prefs = Preferences.userNodeForPackage(MainWindow.class);
		lastModif = prefs.getLong(LAST_MODIF, 0);
		return lastModif;
	}

	public void saveToPref(String key, String value) {
		Preferences prefs = Preferences.userNodeForPackage(MainWindow.class);
		prefs.put(key, value);

	}

	public String getFromPref(String key) {
		Preferences prefs = Preferences.userNodeForPackage(MainWindow.class);
		return prefs.get(key, "");
	}

	public void setLastModif(long lastM) {
		Preferences prefs = Preferences.userNodeForPackage(MainWindow.class);
		prefs.putLong(LAST_MODIF, lastM);
	}

	public static void main(String[] args) {
		new MainWindow();
	}
}
