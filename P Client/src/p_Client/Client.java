package p_Client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * 
 * @author daniel
 *
 */

/*
 * TODO : how wait for byte, maybe build a connection class
 * TODO : exceptions
 */

class Client {
	private static final int keyI = 620453927;
	private static final double keyD = Math.PI;
	private static final String keyS = "m48fgHGD4@%vRT3G7dfgd/[EWndsf9j458t34jgb04h3gijg43$T$Y38f43gm30H$%#Y$%WERG$W#$TG$54jog835hgj4ptjb 4  gti0 gbj4g/rtg34g $WG4w5hiubrnfgby4brth4wBN54gb$#%Y$%H$^YH$G";

	private static final int portIdD = 8000;
	private static final String hostNameD = "localhost";

	private int _portId = 8000;
	private String _hostName = "localhost";

	// IO streams
	private static DataOutputStream _toServer;
	private static DataInputStream _fromServer;
	private Socket _socket;
	private logServer _ls;

	/**
	 * Connstructor
	 * 
	 * @param ls
	 *            - pointer to logWriter
	 */
	public Client(logServer ls) {
		_ls = ls;
	}

	public Client(int portid, logServer ls) {
		_ls = ls;
		_portId = portid;
	}

	public Client(String hostName, logServer ls) {
		_ls = ls;
		_hostName = hostName;
	}

	public Client(int portId, String hostName, logServer ls) {
		_ls = ls;
		_portId = portId;
		_hostName = hostName;
	}

	/**
	 * open connectin with server if connectionn failed write to the log
	 */
	public void connectR() {
		if (_socket == null) {
			try {
				// Create a socket to connect to the server
				_socket = new Socket(_hostName, _portId);

				// Create an input stream to receive data from the server
				_fromServer = new DataInputStream(_socket.getInputStream());

				// Create an output stream to send data
				// to the server
				_toServer = new DataOutputStream(_socket.getOutputStream());
				_ls.writeLog("Connection success\tport:" + _portId + "\t" + _hostName);
			} catch (IOException ex) {
				_ls.writeLog("Connection error\tport:" + _portId + "\t" + _hostName);
			}
		} else {
			_ls.writeLog("Connection error\tport:" + _portId + "\t" + _hostName);
		}
	}

	/**
	 * disconnect connectionn with server
	 */
	public void disconnect() {
		try {
			if (_socket != null) {
				_socket.close();
				_socket = null;
				_ls.writeLog("Disconnect sucssefuly\tport:" + _portId + "\t" + _hostName);
			}
		} catch (IOException e) {
			_ls.writeLog(e.getMessage().toString());
		}
	}

	/**
	 * if server not found @return false else @return status of connection
	 */
	public boolean ifConnect() {
		connectR();

		if (_socket == null)
			return false;
		return _socket.isConnected();
	}

	public void reset() {
		_portId = portIdD;
		_hostName = hostNameD;
	}

	/**
	 * 
	 * @param i
	 *            - num send in server if failed close connection
	 */
	public void writeToServer(int i) {
		try {
			_toServer.writeInt(i);
			_toServer.flush();
		} catch (IOException e) {
			disconnect();
			_ls.writeLog(e.getMessage().toString());
		}
	}

	/**
	 * 
	 * @param d
	 *            - num send in server if failed close connection
	 */
	public void writeToServer(double d) {
		try {
			_toServer.writeDouble(d);
			_toServer.flush();
		} catch (IOException e) {
			disconnect();
			_ls.writeLog(e.getMessage().toString());
		}
	}

	public void writeToServer(String str) {
		try {
			_toServer.writeChars(str);
			_toServer.flush();
		} catch (IOException e) {
			disconnect();
			_ls.writeLog(e.getMessage().toString());
		}
	}
	
	public void writeToServer(Status status) {
		try {
			_toServer.writeInt(secret.decodeI(Status.convert(status), keyI));
			_toServer.flush();
		} catch (IOException e) {
			disconnect();
			_ls.writeLog(e.getMessage().toString());
		}
	}

	/**
	 * use writeTo server by values
	 * 
	 * @param status
	 *            - status data transfer
	 * @param prority
	 *            - priority
	 * @param x
	 *            - d
	 * @param y
	 *            - y
	 */
	public void writeToServer(Status status, int prority, double x, double y) {
		try {
			_toServer.writeInt(secret.decodeI(Status.convert(status), keyI));
			//_toServer.writeInt(secret.decodeI(prority, keyI));

			_toServer.writeDouble(secret.decodeD(x, keyD));
			_toServer.writeDouble(secret.decodeD(y, keyD));
			_toServer.flush();

			_ls.writeLog("Succssefully write to server numbers");
		} catch (IOException e) {
			disconnect();
			_ls.writeLog(e.getMessage().toString());
		}
	}

	/**
	 * 
	 * @param status
	 * @param user
	 * @param pass
	 */
	public void writeToServer(Status status, String user, String pass) {
		try {
			//_toServer.writeInt(secret.decodeI(5, keyI));
			_toServer.writeInt(secret.decodeI(Status.convert(status), keyI));

			_toServer.writeChars(secret.decodeS(user, keyS) + "\n");
			_toServer.writeChars(secret.decodeS(pass, keyS) + "\n");
			_toServer.flush();
			_ls.writeLog("Succssefully write to server strings");
		} catch (IOException e) {
			disconnect();
			_ls.writeLog(e.getMessage().toString());
		}
	}

	/**
	 * 
	 * @return double we get from server
	 */
	public double readDblFromServer() {
		try {
			return secret.decodeD(_fromServer.readDouble(), keyD);
		} catch (IOException e) {
			disconnect();
			_ls.writeLog(e.getMessage().toString());
		}
		return 0;
	}

	/**
	 * 
	 * @return integer we get from server
	 */
	public int readIntFromServer() {
		try {
			int secretN = _fromServer.readInt();
			secretN = secret.decodeI(secretN, keyI);
			return secretN;
		} catch (IOException e) {
			disconnect();
			_ls.writeLog(e.getMessage().toString());
			return 0;
		}
	}

	/**
	 * 
	 * @return string we get from server
	 */
	public String readLineFromServer() {
		char ch;
		String str = "";
		try {
			while ((ch = _fromServer.readChar()) != '\n') {
				str += ch;
			}

			return secret.decodeS(str, keyS);
		} catch (IOException e) {
			disconnect();
			_ls.writeLog(e.getMessage().toString());
		}
		return "";
	}

	public int get_portId() {
		return _portId;
	}

	public void set_portId(int _portId) {
		this._portId = _portId;
	}

	public String get_hostName() {
		return _hostName;
	}

	public void set_hostName(String _hostName) {
		this._hostName = _hostName;
	}
}