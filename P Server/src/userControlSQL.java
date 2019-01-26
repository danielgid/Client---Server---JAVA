import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class userControlSQL {

	// private static int keyI = 620453927;
	// private static double keyD = Math.PI;
	private static final String keySI = "m48fgHGD4@%vRT3G7dfgd/[EWndsf9j458t34jgb04h3gijg43$T$Y38f43gm30H$%#Y$%WERG$W#$TG$54jog835hgj4ptjb 4  gti0 gbj4g/rtg34g $WG4w5hiubrnfgby4brth4wBN54gb$#%Y$%H$^YH$G";
	private static final String keySO = "vq934fhvt089h3j ngu4erj9g845jgotj =-ol jofi5hgufoiwi %$&df 'fpovwjsm,v;qlrkjgo8i3jkwem,d mvnbri4ulk fmvp9urokl mklg, ,kgjipog fm3lkgbj noklvb9 if'tjg p2 itgk 0F3";

	// Deafult username and pass
	private static final String USERNAME = "admin";
	private static final String PASSWORD = "12345678";

	// JDBC driver name and database URL
	private static final String USER = "root";
	private static final String PASS = "12345678";
	private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	private static final String DB_URL = "jdbc:mysql://127.0.0.1/userDB";

	/********************************************************************************/

	private boolean _ifConncet;

	private logServer _ls;
	private Connection _conn;

	/**
	 * Connect to SQL
	 * 
	 * @param ls2
	 *            - pointer to log writer
	 */
	public userControlSQL(logServer ls2) {
		_ls = ls2;
		connect();
	}

	/**
	 * Connect to SQL server by defualt user
	 */
	public void connect() {
		try {
			Class.forName(JDBC_DRIVER).newInstance();
			_conn = DriverManager.getConnection(DB_URL, USER, PASS);
			_ls.writeLog("SQL - succesfully");
			_ifConncet = true;
		} catch (Exception e) {
			_ifConncet = false;
			_ls.writeLog(e.getMessage().toString());
		}
	}

	/********************************************************************************/

	/**
	 * Create new data table if no exists table
	 */
	public void createNewTable() {
		// SQL statement for creating a new table

		String sql = "CREATE TABLE IF NOT EXISTS `users`.`users` (\n" + "`username` VARCHAR(45) NOT NULL,\n"
				+ "`password` VARCHAR(45) NOT NULL,\n" + "`email` VARCHAR(45) NOT NULL,\n " + "`borthYear` INT NULL,\n"
				+ "PRIMARY KEY (`username`, `email`));";

		try {
			Statement stmt = _conn.createStatement();
			stmt.execute(sql);

			addUser(USERNAME, PASSWORD, "null", 0);
		} catch (SQLException e) {
			_ls.writeLog(e.getMessage().toString());
		}
	}

	/********************************************************************************/

	/**
	 * @return SQL connection status
	 */
	public boolean connectionStatus() {
		return _ifConncet;
	}

	public static String decode(String str) {
		str = secret.decodeS(str, keySI);
		str = secret.decodeS(str, keySO);

		return str;
	}

	// INSERT INTO `userDB`.`users` (`user`, `pass`, `email`, `birth`) VALUES ('d',
	// 'd', 'd', '1');

	/**
	 * 
	 * @param user
	 * @param pass
	 * @param email
	 * @param year
	 * @return
	 */
	public int addUser(String user, String pass, String email, int year) {
		String addQuery = "INSERT INTO `userDB`.`users` VALUES ('";

		addQuery += user + "','" + pass + "','" + email + "','" + Integer.toString(year) + "');";

		try {
			// Step 1: Allocate a database 'Connection' object
			if (_conn == null)
				connect();

			Statement stmt = (Statement) _conn.createStatement();
			// here is the query you will execute
			stmt.executeUpdate(addQuery);
			_ls.writeLog(user + "sucssefully added");
		} catch (SQLException e) {
			_ls.writeLog(e.getMessage().toString());
			return 0;
		}
		return 1;
	}

	/********************************************************************************/

	/**
	 * 
	 * @param user
	 * @param pass
	 * @return
	 */
	public int checkUser(String user, String pass) {
		/*
		 * user = decode(user); pass = decode(pass);
		 */
		String userS = "", passS = "";

		try {
			// Step 1: Allocate a database 'Connection' object
			if (_conn == null)
				connect();

			Statement stmt = (Statement) _conn.createStatement();
			// here is the query you will execute
			ResultSet rs;
			rs = stmt.executeQuery("SELECT user, pass FROM users");

			while (rs.next()) {
				userS = rs.getString("user");
				passS = rs.getString("pass");
				if (user.equals(userS) && pass.equals(passS))
					return 1;

			}
		} catch (SQLException e) {
			_ls.writeLog(e.getMessage().toString());
		}

		return 0;
	}

	/********************************************************************************/

}
