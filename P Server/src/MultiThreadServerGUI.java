
import java.net.*;
import java.util.*;
import java.awt.*;
import javax.swing.*;

/**
 * 
 * @author Daniel
 *
 */

public class MultiThreadServerGUI extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private logServer _ls;

	private JTextArea _jta;
	private Server _server;

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		new MultiThreadServerGUI();
	}

	/**
	 * Set frame for server and wait for new client quest. For each client we make
	 * new thread.
	 */
	public MultiThreadServerGUI() {
		_jta = new JTextArea();
		// Place text area on the frame
		setLayout(new BorderLayout());
		add(new JScrollPane(_jta), BorderLayout.CENTER);
		setTitle("MultiThreadServer");
		setSize(500, 300);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true); // It is necessary to show the frame here!

		_jta.append("MultiThreadServer started at " + new Date() + '\n');

		int clientNo = 1;

		_ls = new logServer("server");
		_server = new Server(_ls);

		while (true) {
			// Listen for a new connection request
			Socket socket = _server.Accept();

			// Display the client number
			_jta.append("Starting thread for client " + clientNo + " at " + new Date() + '\n');

			// Find the client's host name, and IP address
			InetAddress inetAddress = socket.getInetAddress();
			_jta.append("Client " + clientNo + "'s host name is " + inetAddress.getHostName() + "\n");
			_jta.append("Client " + clientNo + "'s IP Address is " + inetAddress.getHostAddress() + "\n");
			_ls.writeLog("Client " + clientNo + "'s IP Address is " + inetAddress.getHostAddress() + "\n");

			// Create a new task for the connection
			Thread task = new Thread(new HandleAClient(socket, _ls));
			task.start();
			clientNo++;
		}
	} 
}
