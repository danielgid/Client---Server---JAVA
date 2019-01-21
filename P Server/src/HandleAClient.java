
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

	private static int keyI = 620453927;
	private static double keyD = Math.PI;

	private static final int failed = 0;
	private static final int succsses = 1;

	private Socket socket; // A connected socket

	private DataInputStream inputFromClient;
	private DataOutputStream outputToClient;

	private SQLReader serverDB;

	private int _id;
	private double _xFC;
	private double _yFC;
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

		this.socket = socket;
		serverDB = new SQLReader(_ls);
	}

	/**
	 * Get priority level from client
	 * 
	 * @return if succsefully get data
	 */
	public boolean setPriority() {
		int priority = 0;
		try {
			priority = secret.decodeI(inputFromClient.readInt(), keyI);
			Thread.currentThread().setPriority(priority);
			return true;
		} catch (IOException e) {
			_ls.writeLog(e.getMessage().toString());
			return false;
		}
	}

	/**
	 * get y cordiante
	 * 
	 * @return if succsefully get data
	 */
	public boolean setxFC() {
		_xFC = 0;
		try {
			_xFC = secret.decodeD(inputFromClient.readDouble(), keyD);
			return true;
		} catch (IOException e) {
			_ls.writeLog(e.getMessage().toString());
			return false;
		}
	}

	/**
	 * get y cordiante
	 * 
	 * @return if succsefully get data
	 */
	public boolean setyFC() {
		_yFC = 0;
		try {
			_yFC = secret.decodeD(inputFromClient.readDouble(), keyD);
			return true;
		} catch (IOException e) {
			_ls.writeLog(e.getMessage().toString());
			return false;
		}
	}

	/**
	 * 
	 * @param num
	 *            - data to write to client
	 * @return if succsefully write data
	 */
	private boolean writeDouble(double num) {
		try {
			outputToClient.writeDouble(num);
			return true;
		} catch (IOException e) {
			_ls.writeLog(e.getMessage().toString());
			return false;
		}
	}

	/**
	 * 
	 * @param num-
	 *            data to write to client
	 * @return if succsefully write data
	 */
	private boolean writeInt(int num) {
		try {
			outputToClient.writeInt(num);
			return true;
		} catch (IOException e) {
			_ls.writeLog(e.getMessage().toString());
			return false;
		}
	}

	/**
	 * Get data, decode data, check in SQL server
	 * 
	 * @if in sql - get data from sql and send decoded data to client
	 * @e_lse send error to client
	 */
	public void run() {
		try {
			// Create data input and output streams
			inputFromClient = new DataInputStream(socket.getInputStream());
			outputToClient = new DataOutputStream(socket.getOutputStream());

			setPriority();
			setxFC();
			setyFC();

			if (serverDB.minDistancePointIndex(secret.decodeD(_xFC, keyD), secret.decodeD(_yFC, keyD))) {

				_id = secret.decodeI(serverDB.getId(), keyI);
				_xFC = secret.decodeD(serverDB.getxCordinate(), keyD);
				_yFC = secret.decodeD(serverDB.getyCordinate(), keyD);

				writeInt(secret.decodeI(succsses, keyI));
				writeInt(secret.decodeI(_id, keyI));
				writeDouble(secret.decodeD(_xFC, keyD));
				writeDouble(secret.decodeD(_yFC, keyD));
				_ls.writeLog("Server and SQL succesfully work.");
			} else {
				writeInt(secret.decodeI(failed, keyI));
				_ls.writeLog("SQL server not work.");
			}

			socket.close();
		} catch (IOException e) {
			_ls.writeLog(e.getMessage().toString());
		}
	}

}
