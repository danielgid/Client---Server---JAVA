package p_Client;

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

	private JTextField _usernameTxt;
	private JPasswordField _passwordTxt;

	private KeyListener _enterKey;
	private loginFrameListener _loginListtener;

	public LoginF(logServer ls1) {
		_ls = ls1;

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
		_usernameTxt = new JTextField(25);
		_passwordTxt = new JPasswordField(25);
		JLabel usernameLbl = new JLabel("Username: ");
		JLabel passwordLbl = new JLabel("Password: ");

		JButton singUp = new JButton("Sing up");
		JButton loginButton = new JButton("Sing in");

		singUp.addActionListener(new signUpButtonListener());
		loginButton.addActionListener(new loginButtonListener());

		_usernameTxt.setBounds(100, 50, 150, 20);
		usernameLbl.setBounds(20, 50, 150, 20);
		_passwordTxt.setBounds(100, 100, 150, 20);
		passwordLbl.setBounds(20, 100, 150, 20);
		singUp.setBounds(100, 200, 150, 20);
		loginButton.setBounds(100, 150, 150, 20);
		
		_passwordTxt.addKeyListener(_enterKey);
		_usernameTxt.addKeyListener(_enterKey);

		_loginF.add(textArea);
		_loginF.add(usernameLbl);
		_loginF.add(_usernameTxt);
		_loginF.add(passwordLbl);
		_loginF.add(_passwordTxt);
		_loginF.add(singUp);
		_loginF.add(loginButton);

		_loginF.setVisible(true);
	}
	
	public void setVisible(boolean status) {
		_loginF.setVisible(status);
	}
	
	/**
	 * We use usercontrol to check if user name annd pass in system
	 * 
	 * if succsefully login. so we call singUpListener
	 */
	@SuppressWarnings("deprecation")
	private void checkAction() {
		String user = _usernameTxt.getText().toString();
		String pass = _passwordTxt.getText().toString();

		if (user.length() == 0 || pass.length() == 0) {
			JOptionPane.showMessageDialog(_loginF, "Please fill user or data\n", "Login Error",
					JOptionPane.ERROR_MESSAGE);

			_ls.writeLog("Data not filled\n");
		} else if (userControl.userCheck(secret.decodeS(user, keyS), secret.decodeS(pass, keyS), _ls)) {

			_ls.writeLog(user);
			_ls.writeLog("Have successefuly sign in\n");

			if (_loginListtener != null)
				_loginListtener.singUpListener();
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
	private void addUser() {
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

	
	/**
	 * get lisyener from ClientGUI
	 * 
	 * @param loginListener
	 *            - listener
	 */
	public void setLoginListener(loginFrameListener loginListener) {
		_loginListtener = loginListener;
	}
	
	/*****************************************************************************/

	/**
	 * 
	 * @author daniel
	 *
	 */
	public class enterLoginKeyBoard implements KeyListener{
		@Override
		public void keyPressed(KeyEvent e) {
			if(e.getKeyChar() == KeyEvent.VK_ENTER){
				checkAction();

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
			checkAction();

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
			addUser();

			_usernameTxt.setText("");
			_passwordTxt.setText("");
		}
	}

}
