package p_Client;

import java.awt.Panel;

import javax.swing.JDialog;
import javax.swing.JTabbedPane;

public class preferencesDialog extends JDialog {

	/**
	 * TODO: dialog TODO : tabs, user control, SQL - control - server TODO : time
	 * format
	 */
	private static final long serialVersionUID = 1L;

	private JTabbedPane _menuPane;

	public preferencesDialog() {
		init();

	}

	private void init() {
		setSize(250, 250);
		setName("Preferences");
		setTitle("Preferences");

		_menuPane = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.WRAP_TAB_LAYOUT);
		_menuPane.setSize(200, 200);
		_menuPane.setBounds(50, 50, 200, 200);
		_menuPane.add( "dfmnjgoe",new Panel());
		_menuPane.add( "gdrgewrg",new Panel());
		_menuPane.add( "g34t3g3",new Panel());
		_menuPane.add( "g4gbgr5",new Panel());

		add(_menuPane);
		//setVisible(true);
	}

}
