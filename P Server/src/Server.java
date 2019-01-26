
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 
 * @author Daniel
 * 
 * 
 */

class Server {
	private static final int portId = 8000;

	private logServer _ls;
	private ServerSocket _serverSocket;

	/**
	 * server constructor
	 * 
	 * @param ls2
	 *            - pointer to log writter
	 */
	public Server(logServer ls2) {
		_ls = ls2;
		try {
			_serverSocket = new ServerSocket(portId);
			_ls.writeLog("Server start\tport: " + portId);
		} catch (IOException e) {
			_ls.writeLog(e.getMessage().toString());
		}
	}

	/**
	 * 
	 * @return serverSocket pointer, if failed return null
	 */
	public Socket Accept() {
		try {
			return _serverSocket.accept();
		} catch (IOException e) {
			_ls.writeLog(e.getMessage().toString());
			return null;
		}
	}

}
