
import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 
 * @author Daniel
 * 
 * 
 */

/*
 * TODO : setter for DB_URL USER PASS TODO : exceptions
 */

public class SQLReader {

	private static int keyI = 620453927;
	private static double keyD = Math.PI;

	// JDBC driver name and database URL
	private final String USER = "root";
	private final String PASS = "12345678";
	private final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	private final String DB_URL = "jdbc:mysql://127.0.0.1/testDB";

	/********************************************************************************/

	private int _id;
	private double _xCordinate;
	private double _yCordinate;
	private boolean _ifConncet;

	private logServer _ls;
	private Connection _conn;

	/**
	 * Connect to SQL
	 * 
	 * @param ls2
	 *            - pointer to log writer
	 */
	public SQLReader(logServer ls2) {
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

		String sql = "CREATE TABLE IF NOT EXISTS comands (\n" + "	name text NOT NULL,\n"
				+ "	id integer PRIMARY KEY,\n" + "xCordinate double NOT NULL,\n " + "yCordinate double NOT NULL\n);";

		try {
			Statement stmt = _conn.createStatement();
			stmt.execute(sql);
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

	/**
	 * check minDistannceConected()
	 * 
	 * @param x
	 *            - xcor
	 * @param y-
	 *            y cor
	 * @return SQL connection status
	 */
	public boolean minDistancePointIndex(final double x, final double y) {
		if (_ifConncet)
			minDistannceConected(secret.decodeD(x, keyD), secret.decodeD(y, keyD));
		return _ifConncet;
	}

	/**
	 * run on all points and check which point is the nearest to cordinats.
	 * 
	 * @param x
	 *            x cor
	 * @param y
	 *            y cor
	 */
	private void minDistannceConected(final double x, final double y) {
		double xC = 0.0, yC = 0.0;
		double dis = 0.0, min = Double.MAX_VALUE;

		try {
			Statement stmt = (Statement) _conn.createStatement();
			// here is the query you will execute
			ResultSet rs;
			rs = stmt.executeQuery("SELECT xCordinate,yCordinate,id FROM comands");

			while (rs.next()) {
				xC = getXCordinate(rs);
				yC = getYCordinate(rs);
				dis = distance(x, xC, y, yC);

				if (dis < min) {
					min = dis;

					_xCordinate = xC;
					_yCordinate = yC;
					_id = getID(rs);
				}
			}
		} catch (SQLException e) {
			_ls.writeLog(e.getMessage().toString());
		}
	}

	/**
	 * Calculate distance between (x1,y1) to (x2, y2)
	 * 
	 * @return distance between points
	 */
	public double distance(double x1, double x2, double y1, double y2) {
		double dis = 0;

		dis = Math.pow((y1 - y2), 2);
		dis += Math.pow((x1 - x2), 2);

		dis = Math.sqrt(dis);

		return dis;
	}

	/********************************************************************************/

	/**
	 * 
	 * @return string that contains data of all the table
	 */
	public String getData() {
		String rc = "";

		try {
			// Step 1: Allocate a database 'Connection' object
			if (_conn == null)
				connect();

			Statement stmt = (Statement) _conn.createStatement();
			// here is the query you will execute
			ResultSet rs;

			rs = stmt.executeQuery("SELECT id,name, xCordinate,yCordinate FROM comands");

			while (rs.next()) {
				rc += getNextRow(rs);
			}
		} catch (SQLException e) {
			_ls.writeLog(e.getMessage().toString());
		}

		return rc;
	}

	/**
	 * read xCordinate from SQL
	 * 
	 * @param rs
	 *            pointer to SQL
	 * @return readed data
	 */
	private double getXCordinate(ResultSet rs) {
		Double rc = 0.0;

		try {
			rc = rs.getDouble("xCordinate");
		} catch (SQLException e) {
			_ls.writeLog(e.getMessage().toString());
		}

		return rc;
	}

	/**
	 * read yCordinate from SQL
	 * 
	 * @param rs
	 *            pointer to SQL
	 * @return readed data
	 */
	private double getYCordinate(ResultSet rs) {
		Double rc = 0.0;

		try {
			rc = rs.getDouble("yCordinate");
		} catch (SQLException e) {
			_ls.writeLog(e.getMessage().toString());
		}

		return rc;
	}

	/**
	 * read id from SQL
	 * 
	 * @param rs
	 *            pointer to SQL
	 * @return readed data
	 */
	private int getID(ResultSet rs) {
		int rc = 0;

		try {
			rc = rs.getInt("id");
		} catch (SQLException e) {
			_ls.writeLog(e.getMessage().toString());
		}

		return rc;
	}

	/********************************************************************************/

	/**
	 * read next row from SQL
	 * 
	 * @param rs
	 *            pointer to SQL
	 * @return readed data
	 */
	private String getNextRow(ResultSet rs) {
		String rc = "";

		try {
			// rs contains the result of the query
			// with getters you can obtain column values
			int id = rs.getInt("id");
			String s = rs.getString("name");
			double x = rs.getDouble("xCordinate");
			double y = rs.getDouble("yCordinate");

			rc += id + "\t" + s + "\t" + x + "\t" + y + "\n";

		} catch (SQLException e) {
			_ls.writeLog(e.getMessage().toString());
		}

		return rc;
	}

	/********************************************************************************/

	/**
	 * 
	 * @return decode id
	 */
	public int getId() {
		return secret.decodeI(_id, keyI);
	}

	/**
	 * 
	 * @return decode x cordinate
	 */
	public double getxCordinate() {
		return secret.decodeD(_xCordinate, keyD);
	}

	/**
	 * 
	 * @return decode y cordinate
	 */
	public double getyCordinate() {
		return secret.decodeD(_yCordinate, keyD);
	}

	/********************************************************************************/

}
