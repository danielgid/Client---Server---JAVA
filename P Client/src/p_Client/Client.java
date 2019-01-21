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
 * TODO : manually set for ("localhost", 8000) TODO : read byte and know if it
 * close or send data TODO : how wait for byte, maybe build a connection class
 * TODO : exceptions
 */

class Client {
	private static int keyI = 620453927;
	private static double keyD = Math.PI;

	private static int portId = 8000;
	private static String hostName = "localhost";

	// IO streams
	private static DataOutputStream toServer;
	private static DataInputStream fromServer;
	private Socket _socket;
	private logServer _ls;

	/**
	 * Connstructor
	 * 
	 * @param ls2
	 *            - pointer to logWriter
	 */
	public Client(logServer ls2) {
		this._ls = ls2;
	}

	/**
	 * open connectin with server if connectionn failed write to the log
	 */
	public void connectR() {
		if (_socket == null) {
			try {
				// Create a socket to connect to the server
				_socket = new Socket(hostName, portId);

				// Create an input stream to receive data from the server
				fromServer = new DataInputStream(_socket.getInputStream());

				// Create an output stream to send data
				// to the server
				toServer = new DataOutputStream(_socket.getOutputStream());
				_ls.writeLog("Connection success\tport:" + portId + "\t" + hostName);
			} catch (IOException ex) {
				_ls.writeLog("Connection error\tport:" + portId + "\t" + hostName);
			}
		} else {
			_ls.writeLog("Connection error\tport:" + portId + "\t" + hostName);
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
				_ls.writeLog("Disconnect sucssefuly\tport:" + portId + "\t" + hostName);
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

	/**
	 * 
	 * @param i - num send in server
	 * if failed close connection
	 */
	public void writeToServer(int i) {
		try {
			toServer.writeInt(i);
			toServer.flush();
		} catch (IOException e) {
			disconnect();
			_ls.writeLog(e.getMessage().toString());
		}
	}

	/**
	 * 
	 * @param d - num send in server
	 * if failed close connection
	 */
	public void writeToServer(double d) {
		try {
			toServer.writeDouble(d);
			toServer.flush();
		} catch (IOException e) {
			disconnect();
			_ls.writeLog(e.getMessage().toString());
		}
	}

	/**
	 * use writeTo server by values 
	 * @param prority - priority
	 * @param x - d
	 * @param y - y
	 */
	public void writeToServer(int prority, double x, double y) {
		try {
			toServer.writeInt(secret.decodeI(prority, keyI));
			toServer.writeDouble(secret.decodeD(x, keyD));
			toServer.writeDouble(secret.decodeD(y, keyD));
			toServer.flush();
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
			return secret.decodeD(fromServer.readDouble(), keyD);
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
			return secret.decodeI(fromServer.readInt(), keyI);
		} catch (IOException e) {
			disconnect();
			_ls.writeLog(e.getMessage().toString());
		}
		return 0;
	}

	/**
	 * 
	 * @return string we get from server
	 */
	public String readLineFromServer() {
		char ch;
		String str = "";
		try {
			while ((ch = fromServer.readChar()) != '\n') {
				str += ch;
			}
			return str;
		} catch (IOException e) {
			disconnect();
			_ls.writeLog(e.getMessage().toString());
		}
		return "";
	}
}