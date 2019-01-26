
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * 
 * @author Daniel
 * 
 * 
 */

/*
 * TODO : close thread TODO : exceptions
 */

class HandleAClient implements Runnable {

	private static final int keyI = 620453927;
	private static final double keyD = Math.PI;
	private static final String keyS = "m48fgHGD4@%vRT3G7dfgd/[EWndsf9j458t34jgb04h3gijg43$T$Y38f43gm30H$%#Y$%WERG$W#$TG$54jog835hgj4ptjb 4  gti0 gbj4g/rtg34g $WG4w5hiubrnfgby4brth4wBN54gb$#%Y$%H$^YH$G";

	private static final int failed = 0;
	private static final int succsses = 1;

	private Socket _socket; // A connected socket

	private DataInputStream _inputFromClient;
	private DataOutputStream _outputToClient;

	private SQLReader _serverDB;

	private boolean _error = false;
	private Status _status;
	private logServer _ls;

	/**
	 * Constructor for handle client data transfer
	 * 
	 * @param socket
	 *            - pointer to our sockect
	 * @param _ls2
	 *            - pointer to log writer
	 */
	public HandleAClient(Socket socket, logServer _ls2) {
		_ls = _ls2;

		this._socket = socket;
		_serverDB = new SQLReader(_ls);
	}

	/**
	 * Get priority level from client
	 * 
	 * @return if succsefully get data
	 */
	public void setPriority() {
		int priority = 0;
		try {
			priority = secret.decodeI(_inputFromClient.readInt(), keyI);
			Thread.currentThread().setPriority(priority);
		} catch (IOException e) {
			_ls.writeLog(e.getMessage().toString());
			_error = true;
		}
	}

	/**
	 * 
	 * @return if succsefully get data
	 */
	public double getDouble() {
		double data = 0;
		try {
			data = secret.decodeD(_inputFromClient.readDouble(), keyD);
		} catch (IOException e) {
			_ls.writeLog(e.getMessage().toString());
			_error = true;
			return -1;
		}

		return data;
	}

	/**
	 * 
	 * @return string we get from server
	 */
	public String getString() {
		char ch;
		String str = "";
		try {
			while ((ch = _inputFromClient.readChar()) != '\n') {
				str += ch;
			}
			return secret.decodeS(str, keyS);
		} catch (IOException e) {
			_ls.writeLog(e.getMessage().toString());
			_error = true;
			return "";
		}
	}

	/**
	 * 
	 * @param num
	 *            - data to write to client
	 * @return if succsefully write data
	 */
	private void writeDouble(double num) {
		try {
			_outputToClient.writeDouble(secret.decodeD(num, keyD));
		} catch (IOException e) {
			_ls.writeLog(e.getMessage().toString());
			_error = true;
		}
	}

	/**
	 * 
	 * @param num-
	 *            data to write to client
	 * @return if succsefully write data
	 */
	private void writeInt(int num) {
		try {
			_outputToClient.writeInt(secret.decodeI(num, keyI));
		} catch (IOException e) {
			_ls.writeLog(e.getMessage().toString());
			_error = true;
		}
	}

	/**
	 * 
	 */
	private void setStatus() {
		try {
			_status = Status.convert(secret.decodeI(_inputFromClient.readInt(), keyI));
		} catch (IOException e) {
			_ls.writeLog(e.getMessage().toString());
			_error = true;
		}
	}

	/**
	 * 
	 */
	private void cordinatesControl() {
		int id;
		// setPriority();

		double xFC = getDouble(), yFC = getDouble();

		if (_error) {
			// TODO : error situation
		} else {
			if (_serverDB.minDistancePointIndex(secret.decodeD(xFC, keyD), secret.decodeD(yFC, keyD))) {

				id = secret.decodeI(_serverDB.getId(), keyI);
				xFC = secret.decodeD(_serverDB.getxCordinate(), keyD);
				yFC = secret.decodeD(_serverDB.getyCordinate(), keyD);

				writeInt(succsses);
				writeInt(id);
				writeDouble(xFC);
				writeDouble(yFC);
				_ls.writeLog("Server and SQL succesfully work.");
			} else {
				writeInt(secret.decodeI(failed, keyI));
				_ls.writeLog("SQL server not work.");
			}
		}
	}

	/**
	 * 
	 */
	private void userC() {
		String user, pass;

		user = getString();
		pass = getString();

		userControlSQL userSql = new userControlSQL(_ls);
		int checked = userSql.checkUser(user, pass);

		writeInt(checked);
	}

	/**
	 * 
	 */
	private void userAdd() {
		String user, pass;

		user = getString();
		pass = getString();

		userControlSQL userSql = new userControlSQL(_ls);
		int checked = userSql.addUser(user, pass, "", 0);

		writeInt(checked);
	}

	/**
	 * return to server succes signal
	 */
	private void checkConnectionTransfer() {
		writeInt(succsses);
	}

	@SuppressWarnings("deprecation")
	private void disconnect() {
		try {
			_socket.close();
			_ls.writeLog("The socket closed.");
			Thread.currentThread().stop();
		} catch (IOException e) {
			_ls.writeLog(e.getMessage().toString());
		}
	}

	/**
	 * Get data, decode data, check in SQL server
	 * 
	 * @if in sql - get data from sql and send decoded data to client
	 * @else send error to client
	 */
	public void run() {
		while (true) {
			try {
				// Create data input and output streams
				_inputFromClient = new DataInputStream(_socket.getInputStream());
				_outputToClient = new DataOutputStream(_socket.getOutputStream());

				setStatus();

				if (!_error) {
					switch (_status) {
					case disconnect:
						disconnect();
						break;

					case addUser:
						userAdd();
						break;

					case cordinates:
						cordinatesControl();
						break;

					case checkUser:
						userC();
						break;

					case checkConnection:
						checkConnectionTransfer();
						break;

					default:
						break;
					}
				}
			} catch (IOException e) {
				_ls.writeLog(e.getMessage().toString());
			}
		}
	}

}
