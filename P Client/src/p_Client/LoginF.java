package p_Client;

import java.awt.Choice;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * 
 * @author daniel
 *
 */
public class LoginF extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static String keyS = "m48fgHGD4@%vRT3G7dfgd/[EWndsf9j458t34jgb04h3gijg43$T$Y38f43gm30H$%#Y$%WERG$W#$TG$54jog835hgj4ptjb 4  gti0 gbj4g/rtg34g $WG4w5hiubrnfgby4brth4wBN54gb$#%Y$%H$^YH$G";

	private logServer _ls;
	private JFrame _loginF;

	private Choice _userRoot;
	private JTextField _usernameTxt;
	private JTextField _hostAdress;
	private JRadioButton _manualHost;
	private JPasswordField _passwordTxt;

	private Client _clientControl;
	private KeyListener _enterKey;
	private loginFrameListener _loginListtener;

	public LoginF(logServer ls1) {
		_ls = ls1;

		//_clientControl = new Client(_ls);
		init();
	}

	/**
	 * init login frame
	 */
	private void init() {
		_loginF = new JFrame("Login");
		_loginF = new JFrame();
		_loginF.setSize(500, 400);
		_loginF.setLayout(null);

		_enterKey = new enterLoginKeyBoard();

		JTextArea textArea = new JTextArea(25, 25);
		_hostAdress = new JTextField(15);
		_usernameTxt = new JTextField(25);
		_passwordTxt = new JPasswordField(25);
		_manualHost = new JRadioButton();

		JLabel usernameLbl = new JLabel("Username: ");
		JLabel passwordLbl = new JLabel("Password: ");
		JLabel userRootLbl = new JLabel("Host: ");
		JLabel userRootLblM = new JLabel("Manually\nHost: ");

		_userRoot = new Choice();
		_userRoot.add("localhost : //127.0.0.1/");
		_userRoot.add("My computer");

		JButton singUp = new JButton("Sing up");
		JButton loginButton = new JButton("Sing in");

		singUp.addActionListener(new signUpButtonListener());
		loginButton.addActionListener(new loginButtonListener());

		_usernameTxt.setBounds(140, 50, 150, 20);
		usernameLbl.setBounds(20, 50, 80, 20);
		_passwordTxt.setBounds(140, 100, 150, 20);
		passwordLbl.setBounds(20, 100, 80, 20);
		_userRoot.setBounds(140, 150, 250, 22);
		userRootLbl.setBounds(20, 150, 80, 20);
		userRootLblM.setBounds(20, 200, 120, 20);

		_hostAdress.setBounds(140, 200, 150, 20);
		_hostAdress.setEnabled(false);
		_manualHost.setBounds(360, 200, 250, 20);
		_manualHost.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				_userRoot.setEnabled(!_userRoot.isEnabled());
				_hostAdress.setEnabled(!_hostAdress.isEnabled());
			}
		});
		;

		singUp.setBounds(50, 270, 150, 20);
		loginButton.setBounds(250, 270, 150, 20);

		_hostAdress.addKeyListener(_enterKey);
		_passwordTxt.addKeyListener(_enterKey);
		_usernameTxt.addKeyListener(_enterKey);

		_loginF.add(textArea);
		_loginF.add(usernameLbl);
		_loginF.add(_usernameTxt);
		_loginF.add(passwordLbl);
		_loginF.add(_passwordTxt);
		_loginF.add(userRootLbl);
		_loginF.add(_userRoot);
		_loginF.add(singUp);
		_loginF.add(_manualHost);
		_loginF.add(loginButton);
		_loginF.add(userRootLblM);
		_loginF.add(_hostAdress);

		_loginF.setVisible(true);
	}

	/**
	 * 
	 */
	public void setVisible(boolean status) {
		_loginF.setVisible(status);
	}

	/**
	 * We use usercontrol to check if user name annd pass in system
	 * 
	 * if succsefully login. so we call singUpListener
	 */
	@SuppressWarnings("deprecation")
	private void checkActionSQL() {
		String user = _usernameTxt.getText().toString();
		String pass = _passwordTxt.getText().toString();

		if (user.length() == 0 || pass.length() == 0) {
			JOptionPane.showMessageDialog(_loginF, "Please fill user or data\n", "Login Error",
					JOptionPane.ERROR_MESSAGE);

			_ls.writeLog("Data not filled\n");
		} else {
			if (_hostAdress.isEditable()) {
				_clientControl.set_hostName(_hostAdress.getText().toString());
			}

			_clientControl.connectR();
			if (_clientControl.ifConnect()) {
				_clientControl.writeToServer(Status.checkUser, user, pass);

				if (_clientControl.readIntFromServer() == 1) {

					_ls.writeLog(user);
					_ls.writeLog("Have successefuly sign in\n");

					if (_loginListtener != null) {
						_loginListtener.singUpListener(_clientControl);

					}
				} else {
					JOptionPane.showMessageDialog(_loginF, "User or password is wrong\n", "Login Error",
							JOptionPane.ERROR_MESSAGE);

					_ls.writeLog("User or password is wrong\n");
				}
			} else {
				JOptionPane.showMessageDialog(_loginF, "Server doesnt connect\n", "Server", JOptionPane.ERROR_MESSAGE);

				_ls.writeLog("Server doesnt connect\n");

			}
		}
	}

	/**
	 * add user to the database. If user name in datase we write error else we add
	 * user to the system database
	 */
	@SuppressWarnings("deprecation")
	private void addUserSQL() {
		String user = _usernameTxt.getText().toString();
		String pass = _passwordTxt.getText().toString();

		if (user.length() == 0 || pass.length() == 0) {
			JOptionPane.showMessageDialog(_loginF, "Please fill user or data\n", "Login Error",
					JOptionPane.ERROR_MESSAGE);

			_ls.writeLog("Data not filled\n");
		} else {
			if (_hostAdress.isEditable()) {
				_clientControl.set_hostName(_hostAdress.getText().toString());
			}

			_clientControl.connectR();
			if (_clientControl.ifConnect()) {
				_clientControl.writeToServer(Status.addUser, user, pass);

				if (_clientControl.readIntFromServer() == 1) {

					_ls.writeLog(user);
					_ls.writeLog("You have successefuly sign up:\n");

					JOptionPane.showMessageDialog(_loginF, user + "\nYou have successefuly sign up:\n", "Succes",
							JOptionPane.INFORMATION_MESSAGE);

				} else {
					JOptionPane.showMessageDialog(_loginF, "User is exists\n", "Login Error",
							JOptionPane.ERROR_MESSAGE);

					_ls.writeLog("User is exists\n");
				}
			} else {

				JOptionPane.showMessageDialog(_loginF, "Server doesnt connect\n", "Server", JOptionPane.ERROR_MESSAGE);
				_ls.writeLog("Server connection error");
			}
		}
	}

	/**
	 * We use usercontrol to check if user name annd pass in system
	 * 
	 * if succsefully login. so we call singUpListener
	 */
	@SuppressWarnings("deprecation")
	private void checkActionLocal() {
		String user = _usernameTxt.getText().toString();
		String pass = _passwordTxt.getText().toString();

		if (user.length() == 0 || pass.length() == 0) {
			JOptionPane.showMessageDialog(_loginF, "Please fill user or data\n", "Login Error",
					JOptionPane.ERROR_MESSAGE);

			_ls.writeLog("Data not filled\n");
		} else if (userControl.checkUser(secret.decodeS(user, keyS), secret.decodeS(pass, keyS), _ls)) {

			_ls.writeLog(user);
			_ls.writeLog("Have successefuly sign in\n");

			if (_loginListtener != null) {
				_loginListtener.singUpListener(_clientControl);
			}
		} else {

			JOptionPane.showMessageDialog(_loginF, "User or password is wrong\n", "Login Error",
					JOptionPane.ERROR_MESSAGE);

			_ls.writeLog("User or password is wrong\n");
		}
	}

	/**
	 * add user to the database. If user name in datase we write error else we add
	 * user to the system database
	 */
	@SuppressWarnings("deprecation")
	private void addUserLocal() {
		String user = _usernameTxt.getText().toString();
		String pass = _passwordTxt.getText().toString();

		if (user.length() == 0 || pass.length() == 0) {
			JOptionPane.showMessageDialog(_loginF, "Please fill user or data\n", "Login Error",
					JOptionPane.ERROR_MESSAGE);

			_ls.writeLog("Data not filled\n");
		} else if (userControl.addUser(secret.decodeS(user, keyS), secret.decodeS(pass, keyS), _ls)) {

			_ls.writeLog(user);
			_ls.writeLog("You have successefuly sign up:\n");

			JOptionPane.showMessageDialog(_loginF, user + "\nYou have successefuly sign up:\n", "Succes",
					JOptionPane.INFORMATION_MESSAGE);

		} else {

			JOptionPane.showMessageDialog(_loginF, "User is exists\n", "Login Error", JOptionPane.ERROR_MESSAGE);

			_ls.writeLog("User is exists\n");
		}
	}

	/*****************************************************************************/

	/**
	 * get lisyener from ClientGUI
	 * 
	 * @param loginListener
	 *            - listener
	 */
	public void setLoginListener(loginFrameListener loginListener) {
		_loginListtener = loginListener;
	}
	
	/**
	 * 
	 * @param clientControl
	 */
	public void setClientPointer(Client clientControl) {
		_clientControl = clientControl;
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
				checkActionSQL();

				_usernameTxt.setText("");
				_passwordTxt.setText("");
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

	/**
	 * 
	 * @author daniel
	 *
	 */
	public class loginButtonListener implements ActionListener {
		/**
		 * checkAction clear the fields
		 */
		public void actionPerformed(ActionEvent ev) {
			int hostC = _userRoot.getSelectedIndex();
			if (hostC == 0)
				checkActionSQL();
			else
				checkActionLocal();

			_usernameTxt.setText("");
			_passwordTxt.setText("");
		}
	}

	/**
	 * 
	 * @author daniel
	 *
	 */
	public class signUpButtonListener implements ActionListener {
		/**
		 * addUser and clear the fields
		 */
		public void actionPerformed(ActionEvent ev) {
			int hostC = _userRoot.getSelectedIndex();

			if (hostC == 0)
				addUserSQL();
			else
				addUserLocal();

			_usernameTxt.setText("");
			_passwordTxt.setText("");
		}
	}

}
