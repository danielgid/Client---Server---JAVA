package p_Client;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 
 * @author daniel
 *
 */

/**
 * TODO:
 * 
 * TODO : menu for set manual server - user - pass TODO : button default values
 * 
 * TODO : api TODO : check all catch - in all classes - write logs TODO : code
 * review
 * 
 */

public class ClientGUI implements ActionListener {
	private JFrame _clientFrame;
	private JTextField _nameTf, _xTf, _yTf;

	private JButton _sendBtn;
	private JMenuBar _menuBar;
	private Choice _priorityCh;
	private JTextArea _missionTa;
	private JScrollPane jScrollPane1;

	private logServer _ls;
	private LoginF _loginFrame;
	private KeyListener _enterKey;
	private Client _clientControl;

	public static void main(String[] args) {
		new ClientGUI();
	}

	/**
	 * Init the frame, log annd client wait for Login and init gui
	 */
	ClientGUI() {
		_ls = new logServer("ClientGui");

		_clientFrame = new JFrame();
		_loginFrame = new LoginF(_ls);
		_clientControl = new Client(_ls);

		_clientFrame.setSize(500, 820);
		_clientFrame.setLayout(null);
		_clientFrame.add(_loginFrame);
		init();

		/**
		 * event for login frame to close and open new frame
		 */
		_loginFrame.setLoginListener(new loginFrameListener() {
			@Override
			public void singUpListener() {
				_loginFrame.setVisible(false);
				_clientFrame.setVisible(true);
			}
		});

	}

	/**********************************************************/

	/**
	 * Init gui screen
	 */
	private void init() {
		JLabel nameL, priorityL, xL, yL;
		_enterKey = new enterLoginKeyBoard();

		nameL = new JLabel("Name :");
		nameL.setBounds(20, 50, 150, 20);

		priorityL = new JLabel("Priority :");
		priorityL.setBounds(20, 100, 150, 20);

		xL = new JLabel("X cordinate :");
		xL.setBounds(20, 150, 150, 20);

		yL = new JLabel("Y cordinate :");
		yL.setBounds(20, 200, 150, 20);

		_nameTf = new JTextField();
		_nameTf.setBounds(100, 50, 150, 20);

		_priorityCh = new Choice();
		_priorityCh.setBounds(100, 100, 150, 20);
		for (priorityEnum p : priorityEnum.values()) {
			_priorityCh.add(p.toString());
		}
		_priorityCh.select(3);

		_xTf = new JTextField();
		_xTf.setBounds(100, 150, 150, 20);

		_yTf = new JTextField();
		_yTf.setBounds(100, 200, 150, 20);

		_xTf.addKeyListener(_enterKey);
		_yTf.addKeyListener(_enterKey);
		_nameTf.addKeyListener(_enterKey);

		_missionTa = new JTextArea();
		jScrollPane1 = new JScrollPane(_missionTa);
		jScrollPane1.setBounds(15, 320, 470, 470);

		_sendBtn = new JButton("Send");
		_sendBtn.setBounds(300, 250, 50, 50);
		_sendBtn.addActionListener(this);

		_menuBar = new JMenuBar();
		setMenu();

		_clientFrame.add(_xTf);
		_clientFrame.add(_yTf);
		_clientFrame.add(_nameTf);
		_clientFrame.add(_sendBtn);
		_clientFrame.add(_priorityCh);
		_clientFrame.add(jScrollPane1);
		_clientFrame.setJMenuBar(_menuBar);
		
		_clientFrame.add(nameL);
		_clientFrame.add(priorityL);
		_clientFrame.add(xL);
		_clientFrame.add(yL);
	}

	/**
	 * Menu inition
	 */
	private void setMenu() {
		JMenu fileMenu = new JMenu("File");
		JMenuItem logOutMI = new JMenuItem("Log Out");

		logOutMI.addActionListener(new ActionListener() {
			/**
			 * Log out from system
			 */
			@Override
			public void actionPerformed(ActionEvent e) {
				_loginFrame.setVisible(true);
				_clientFrame.setVisible(false);
			}
		});

		_menuBar.add(fileMenu);
		fileMenu.add(logOutMI);

	}

	/**********************************************************/

	/**
	 * Data control check textfields, and send the data to server.
	 */
	public void dataControl() {
		String name = "", xCor = "", yCor = "";
		name = _nameTf.getText().toString();
		xCor = _xTf.getText().toString();
		yCor = _yTf.getText().toString();

		_missionTa.append(getCurrentTimeStamp() + "\n");

		if (checkV(name, xCor, yCor)) {
			sendData(name, xCor, yCor);
		}

		clearText();
	}

	/**
	 * Clear texTfield
	 */
	public void clearText() {
		_nameTf.setText("");
		_xTf.setText("");
		_yTf.setText("");
		_priorityCh.select(3);
	}

	/**
	 * Check string value
	 * 
	 * @param name
	 *            - priority
	 * @param xCor
	 *            - x
	 * @param yCor
	 *            - y
	 * @return true if fields have the right data
	 */
	private boolean checkV(String name, String xCor, String yCor) {
		if (name.length() > 0 && checkDouble(xCor) && checkDouble(yCor))
			return true;

		String problem = "";

		if (name.length() == 0)
			problem += "You need fill your name\n";
		if (xCor.length() == 0)
			problem += "Please fill x cordinate\n";
		else if (!checkDouble(xCor))
			problem += "Cordinate x need be a number\n";
		if (yCor.length() == 0)
			problem += "Please fill y cordinate\n";
		else if (!checkDouble(yCor))
			problem += "Cordinate y need be a number\n";

		problem += "\n";
		_ls.writeLog(problem);
		_missionTa.append(problem);

		return false;
	}

	/**
	 * check if the string have a double contains double by running on the string
	 * 
	 * @param str
	 *            - contains the string
	 * @return true if str contains a double numer
	 */
	private boolean checkDouble(String str) {
		int i = 0, flag = 0;
		char ch;

		if (str.length() == 0)
			return false;

		while (i < str.length()) {
			ch = str.charAt(i);

			if (ch == '-' && flag == 0) {
				flag = 1;
			} else if (ch >= '0' && ch <= '9' && (flag <= 2)) {
				flag = 2;
			} else if (ch == '.' && (flag < 3)) {
				flag = 3;
			} else if (ch >= '0' && ch <= '9' && (flag == 3 || flag == 4)) {
				flag = 4;
			} else if (whiteSpace(ch) && (flag == 2 || flag == 4)) {
				flag = 5;
			} else if (!(whiteSpace(ch) && flag == 0)) {
				return false;
			}

			i++;
		}
		if (flag < 2)
			return false;
		return true;
	}

	/**
	 * 
	 * @param ch
	 *            - contains value of char
	 * @return true if ch is white char
	 */
	private boolean whiteSpace(char ch) {
		if (ch == '\n' || ch == '\t' || ch == ' ' || ch == '\r' || ch == '\f') {
			return true;
		}

		return false;
	}

	/**
	 * This function check connection with server if connection work we send the
	 * data to server after we get data from server and write to the screen
	 * 
	 * @param name
	 *            - priority
	 * @param xCor
	 *            - x
	 * @param yCor
	 *            - y
	 */
	private void sendData(String name, String xCor, String yCor) {
		int priority = _priorityCh.getSelectedIndex() + 1;
		double xCorD = Double.parseDouble(xCor);
		double yCorD = Double.parseDouble(yCor);

		if (!_clientControl.ifConnect()) {
			_missionTa.append("Server not found\n\n");
			_ls.writeLog("Server not found\n\n");
		} else {// Write data to server
			_clientControl.writeToServer(priority, xCorD, yCorD);
			// Display to the text area
			if (_clientControl.readIntFromServer() == 1) {
				_missionTa.append("Id Code: " + _clientControl.readIntFromServer() + "\n");
				_missionTa.append("Mission nearest coordinates: " + _clientControl.readDblFromServer() + ", "
						+ _clientControl.readDblFromServer() + "\n");

				_missionTa.append(name + " have suucssed \n");
				_ls.writeLog("You have suucssed \n");
			} else {
				_missionTa.append("SQL Server or Server not found\n\n");
				_ls.writeLog("SQL Server or Server not found\n\n");
			}
			_clientControl.disconnect();
		}
	}

	/**********************************************************/

	/**
	 * 
	 * @return currentTime
	 */
	public static String getCurrentTimeStamp() {
		return LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
	}

	/**********************************************************/

	/**
	 * Action for button send we check the te_xTfield with checkV if checkV - true
	 * we send data to the server
	 */
	public void actionPerformed(ActionEvent e) {
		dataControl();
	}

	/**
	 * 
	 * @author daniel
	 *
	 */
	public class enterLoginKeyBoard implements KeyListener {
		@Override
		public void keyPressed(KeyEvent e) {
			if (e.getKeyChar() == KeyEvent.VK_ENTER) {
				dataControl();
			}
		}

		@Override
		public void keyTyped(KeyEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void keyReleased(KeyEvent e) {
			// TODO Auto-generated method stub

		}
	}

}
