import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Properties;

public class DBengine {
	private ArrayList<Person> persons;
	private String tableName;
	private Connection connection;
	private String url = "jdbc:mysql://localhost";
	private String driver = "com.mysql.jdbc.Driver";
	private String user = "root";
	private String pass;
	boolean isConnect = false;
	private String dbName;
	private String condConn;
	public static final String COLUMN_ID = "id";
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_SURNAME = "surname";
	public static final String COLUMN_LOGIN = "login";
	public static final String COLUMN_EMAIL = "email";
	public static final String COLUMN_PHONE = "phone";
	private boolean isExistTable;
	private boolean isEmpty;

	// public static final String CREATE_TABLE = "phone";

	public DBengine(String dbName, String user, String pass) {
		this.dbName = dbName;
		this.user = user;
		this.pass = pass;

	}

	public void connectToDb() {
		connection = null;
		try {
			connectDb();
			if (connection != null)
				condConn = "database " + dbName + " connected";
		} catch (Exception e) {
			e.printStackTrace();
			createDB();
		} finally {
			try {
				connectDb();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void createDB() {
		connection = null;
		condConn = "";
		try {
			Class.forName(driver);
			Properties connectionProperties = new Properties();
			connectionProperties.put("driver", driver);
			connectionProperties.put("user", user);
			connectionProperties.put("password", pass);
			connection = DriverManager.getConnection(url, connectionProperties);

			String query = "CREATE DATABASE " + dbName;

			PreparedStatement ps = connection.prepareStatement(query);
			ps.execute();
			if (connection != null) {
				condConn = "Connection succesfull, database " + dbName
						+ " created !!";

			}
		} catch (Exception e) {
			e.printStackTrace();
			condConn = "Connecion failed !!";
		} finally {
			closeConnection();
		}

	}

	public boolean isExistTable() {
		return isExistTable;
	}

	public boolean isEmptyTable(String tableN) {
		if (connection == null) {
			try {
				connectDb();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		String query = "SELECT * FROM " + tableN;

		try {
			ResultSet rs = connection.createStatement().executeQuery(query);
			isEmpty = !rs.next();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return isEmpty;
	}

	public void clearTable(String tableN) {
		try {
			if (isExistTable(tableN)) {
				Statement st = connection.createStatement();
				st.execute("DELETE FROM " + tableN);

			}
		} catch (Exception e) {

		}
	}

	public void updateTable(ArrayList<Person> list, String tableN) {
		try {
			if (isExistTable(tableN)) {
				clearTable(tableN);
				insertList(list, tableN);
			}
		} catch (Exception e) {

		}
	}

	public ArrayList<Person> queryTable(String query, String tableN) {
		persons = new ArrayList<Person>();
		if (isExistTable(tableN)) {
			try {
				Statement st = connection.createStatement();
				ResultSet rs = st.executeQuery(query);
				while (rs.next()) {
					persons.add(new Person(rs.getInt(COLUMN_ID), rs.getString(COLUMN_NAME), rs
							.getString(COLUMN_SURNAME), rs
							.getString(COLUMN_LOGIN), rs
							.getString(COLUMN_EMAIL), rs
							.getString(COLUMN_PHONE)));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return persons;
	}

	public void deleteTable(String tableN) {
		try {
			if (isExistTable(tableN)) {
				Statement st = connection.createStatement();
				st.execute("DROP TABLE " + tableN);
			}
		} catch (Exception e) {

		}
	}

	public boolean isExistTable(String tableN) {
		try {
			java.sql.DatabaseMetaData md = connection.getMetaData();
			ResultSet res = md.getTables(null, null, tableName, null);
			isExistTable = res.next();

		} catch (Exception e) {
			e.printStackTrace();

		}
		return isExistTable;
	}

	public String getCondConn() {
		return condConn;
	}

	public boolean isConnected() {
		if (connection != null) {
			return true;
		} else {
			return false;
		}
	}

	public void closeConnection() {
		condConn = "";
		if (connection != null) {
			try {
				connection.close();
				if (connection == null) {
					condConn = "Connection to " + dbName + " closed !!";
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void deleteDB(String dbName) {

		try {
			Class.forName(driver);
			Properties connectionProperties = new Properties();
			connectionProperties.put("driver", driver);
			connectionProperties.put("user", user);
			connectionProperties.put("password", pass);
			connection = DriverManager.getConnection(url, connectionProperties);

			String query = "DROP DATABASE " + dbName;

			PreparedStatement ps = connection.prepareStatement(query);
			ps.execute();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeConnection();
		}
	}

	public void connectDb() throws Exception {
		Class.forName(driver);
		connection = DriverManager
				.getConnection(url + "/" + dbName, user, pass);
	}

	public void createTable(String tableName) {
		if (connection == null) {
			try {
				connectDb();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		try {
			java.sql.DatabaseMetaData md = connection.getMetaData();
			ResultSet res = md.getTables(null, null, tableName, null);
			isExistTable = res.next();
			System.out.println("from create table table " + tableName
					+ " is exsist " + isExistTable);
			if (!isExistTable) {

				String createTable = "CREATE TABLE " + tableName + " ("
						+ COLUMN_ID + " INT PRIMARY KEY AUTO_INCREMENT, "
						+ COLUMN_NAME + " TEXT(50), " + COLUMN_SURNAME
						+ " TEXT(50), " + COLUMN_LOGIN + " TEXT(50), "
						+ COLUMN_EMAIL + " TEXT(50), " + COLUMN_PHONE
						+ " TEXT(50) )";
				Statement statement = connection.createStatement();
				statement.execute(createTable);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void insertList(ArrayList<Person> list, String tableName) {
		if (connection == null) {
			try {
				connectDb();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		System.out.println("from insertList " + isConnected());
		try {
			// createTable(tableName);
			System.out.println("from insertList " + list.get(2).getEmail());
			Statement statement = connection.createStatement();
			System.out.println(statement.toString());
			for (int i = 0; i < list.size(); i++) {
				String sqlString = "INSERT INTO " + tableName + " ("
						+ COLUMN_NAME + ", " + COLUMN_SURNAME + ", "
						+ COLUMN_LOGIN + ", " + COLUMN_EMAIL + ", "
						+ COLUMN_PHONE + ")" + " VALUES('"
						+ list.get(i).getName() + "', '"
						+ list.get(i).getSurname() + "', '"
						+ list.get(i).getLogin() + "', '"
						+ list.get(i).getEmail() + "', '"
						+ list.get(i).getPhoneN() + "')";
				statement.execute(sqlString);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		// new DBengine();
	}

}
