package p_Client;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * 
 * @author daniel
 *
 *
 *         We use 2D-String array, c is the index. str[c][0] - username
 *         str[c][1] - password
 */

public class userControl {

	private static final String USERNAME = "admin";
	private static final String PASSWORD = "12345678";
	private static final String FILENAME = "users.dba";

	private static String keyS = "m48fgHGD4@%vRT3G7dfgd/[EWndsf9j458t34jgb04h3gijg43$T$Y38f43gm30H$%#Y$%WERG$W#$TG$54jog835hgj4ptjb 4  gti0 gbj4g/rtg34g $WG4w5hiubrnfgby4brth4wBN54gb$#%Y$%H$^YH$G";

	/***************************************************************************/

	/**
	 * Decode and call to userInFile(user, pass, ls) check if user and pass in file
	 * 
	 * @param pass
	 *            - pass
	 * @param user
	 *            - user
	 * @param ls
	 *            - pointer to log writer
	 * @return if user in database
	 */
	public static boolean userCheck(String user, String pass, logServer ls) {
		user = secret.decodeS(user, keyS);
		pass = secret.decodeS(pass, keyS);

		return userInFile(user, pass, ls);
	}

	/**
	 * 
	 * @param pass
	 *            - pass
	 * @param user
	 *            - user
	 * @param ls
	 *            - pointer to log writer
	 * @return true if user added
	 */
	public static boolean addUser(String user, String pass, logServer ls) {
		user = secret.decodeS(user, keyS);
		pass = secret.decodeS(pass, keyS);

		if (userInFileOU(user, ls)) {
			ls.writeLog("Cann't add " + user);
			return false;
		}

		String[][] usersObj = addUser(readFile(ls), user, pass);
		addToFile(usersObj, ls);

		return true;
	}

	/***************************************************************************/

	/**
	 * Check in datat from file readed by comparing data and pass
	 * 
	 * @param pass
	 *            - pass
	 * @param user
	 *            - user
	 * @param ls
	 *            - pointer to log writer
	 * @return true if user in file
	 */
	private static boolean userInFile(String user, String pass, logServer ls) {

		String[][] usersObj = readFile(ls);

		for (int i = 0; i < usersObj.length; i++) {
			if (user.matches(usersObj[i][0]) && pass.matches(usersObj[i][1]))
				return true;
		}

		return false;
	}

	/**
	 * Check in datat from file readed by comparing data
	 * 
	 * @param user
	 *            - user
	 * @param ls
	 *            - pointer to log writer
	 * @return true user in file
	 */
	private static boolean userInFileOU(String user, logServer ls) {
		String[][] usersObj = readFile(ls);

		for (int i = 0; i < usersObj.length; i++) {
			if (user.matches(usersObj[i][0]))
				return true;
		}

		return false;
	}

	/**
	 * read data from file and save to 2-d string
	 * 
	 * @param ls
	 *            - pointer to log writer
	 * @return read users from file in 2d string
	 */
	private static String[][] readFile(logServer ls) {
		String[][] usersObj = null;

		if (!isExistsF())
			createFile(ls);

		try {
			FileInputStream usersF = new FileInputStream(FILENAME);
			ObjectInputStream ois = new ObjectInputStream(usersF);

			usersObj = (String[][]) ois.readObject();

			ois.close();
		} catch (FileNotFoundException e) {
			ls.writeLog(e.getMessage().toString());
		} catch (IOException e) {
			ls.writeLog(e.getMessage().toString());
		} catch (ClassNotFoundException e) {
			ls.writeLog(e.getMessage().toString());
		}
		return usersObj;
	}

	/**
	 * add user to 2-dString (we make new in size +1) in the end
	 * 
	 * @param usersObj
	 *            - 2-d array of user data
	 * @param pass
	 *            - pass
	 * @param user
	 *            - user
	 * @return new 2-d of update data
	 */
	private static String[][] addUser(String[][] usersObj, String user, String pass) {
		int i = 0;
		String[][] rc = new String[usersObj.length + 1][2];

		for (; i < rc.length - 1; i++) {
			rc[i][0] = usersObj[i][0];
			rc[i][1] = usersObj[i][1];
		}

		rc[i][0] = user;
		rc[i][1] = pass;

		return rc;
	}

	/***************************************************************************/

	/**
	 * 
	 * @return if file exists in computer
	 */
	private static boolean isExistsF() {

		boolean rc = false;
		File file = new File(FILENAME);

		rc = file.exists();

		return rc;
	}

	/**
	 * Create new file
	 * 
	 * @param ls
	 *            - pointer to log writer
	 */
	private static void createFile(logServer ls) {
		FileOutputStream usersF;
		ObjectOutputStream oos;

		try {
			usersF = new FileOutputStream(FILENAME);
			oos = new ObjectOutputStream(usersF);

			String[][] usersObj = new String[1][2];
			usersObj[0][0] = USERNAME;
			usersObj[0][1] = PASSWORD;

			oos.writeObject(usersObj);
			oos.close();
			ls.writeLog(FILENAME + "\tCreated");
		} catch (FileNotFoundException e) {
			ls.writeLog(e.getMessage().toString());
		} catch (IOException e) {
			ls.writeLog(e.getMessage().toString());
		}
	}

	/**
	 * add data to file
	 * 
	 * @param usersObj
	 *            - 2-d array of user data
	 * @param ls
	 *            - pointer to log writer
	 */
	private static void addToFile(String[][] usersObj, logServer ls) {
		FileOutputStream usersF;
		ObjectOutputStream oos;

		try {
			usersF = new FileOutputStream(FILENAME);
			oos = new ObjectOutputStream(usersF);

			oos.writeObject(usersObj);
			oos.close();
			ls.writeLog(FILENAME + "\tUpdated" + usersObj[usersObj.length - 1] + "\tadded");
		} catch (FileNotFoundException e) {
			ls.writeLog(e.getMessage().toString());
		} catch (IOException e) {
			ls.writeLog(e.getMessage().toString());
		}
	}

	/***************************************************************************/
}
