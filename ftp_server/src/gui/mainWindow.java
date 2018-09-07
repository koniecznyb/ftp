package gui;
import main.java.gui.mainWindow;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JButton;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.JPasswordField;

import sql.SQLConnection;
import javax.swing.JTree;
import javax.swing.JList;
import javax.swing.border.TitledBorder;
import javax.swing.border.LineBorder;
import java.awt.Color;
import javax.swing.border.EtchedBorder;

/**
 * Glowne okno programu zarzadania uzytkownikami.
 * @author redi
 * @see ftp.FileTransferConnection
 * @see ftp.CommunicationConnectionHandler
 */
public class mainWindow extends JFrame {

	private final String SQL_ADDRESS = "jdbc:mysql://mysql.agh.edu.pl:3306/redi";
	private String username;
	private String password;
	private JPanel contentPane;
	private JPanel loginpanel;
	private JTextField txtUsername;	
	private JPasswordField passwordField;
	private SQLConnection sqlconn;
	private static mainWindow frame;
	private JPanel logoutpanel;
	private JLabel lblLoggedInAs;
	private JButton btnLogOut;
	private JPanel managementpanel;
	private static JList<String> userList;
	private JPanel adduserpanel;
	private JLabel lblNewuserName;
	private static JTextField txtNewuserName;
	private JLabel lblNewuserPassword;
	private static JPasswordField pwdNewuserPassword;
	private JLabel lblGroup;
	private static JTextField txtNewuserGroup;
	private static JButton btnAdd;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					frame = new mainWindow();
					frame.setVisible(true);
					
					switchControlPanelAvailability();
//					frame.setExtendedState(frame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	/**
	 * Głowne okno programu
	 */
	public mainWindow() {
		setTitle("FTP Users Management");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 600, 400);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		/////////////////////////////////////////////////////////////////////LOGIN PANEL//////////////////////////////////////////////////
		loginpanel = new JPanel();
		loginpanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		
		logoutpanel = new JPanel();
		logoutpanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		
		managementpanel = new JPanel();
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
						.addComponent(managementpanel, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 578, Short.MAX_VALUE)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(logoutpanel, GroupLayout.DEFAULT_SIZE, 289, Short.MAX_VALUE)
							.addComponent(loginpanel, GroupLayout.DEFAULT_SIZE, 289, Short.MAX_VALUE)))
					.addContainerGap())
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(loginpanel, GroupLayout.PREFERRED_SIZE, 46, GroupLayout.PREFERRED_SIZE)
						.addComponent(logoutpanel, GroupLayout.PREFERRED_SIZE, 46, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(managementpanel, GroupLayout.PREFERRED_SIZE, 290, Short.MAX_VALUE)
					.addContainerGap())
		);
		
		userList = new JList();
		userList.setBorder(new LineBorder(new Color(0, 0, 0)));
		
		adduserpanel = new JPanel();
		adduserpanel.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0)), "Add new user", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(51, 51, 51)));
		GroupLayout gl_managementpanel = new GroupLayout(managementpanel);
		gl_managementpanel.setHorizontalGroup(
			gl_managementpanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_managementpanel.createSequentialGroup()
					.addContainerGap()
					.addComponent(userList, GroupLayout.PREFERRED_SIZE, 341, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(adduserpanel, GroupLayout.PREFERRED_SIZE, 195, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		gl_managementpanel.setVerticalGroup(
			gl_managementpanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_managementpanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_managementpanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(userList, GroupLayout.PREFERRED_SIZE, 265, GroupLayout.PREFERRED_SIZE)
						.addComponent(adduserpanel, GroupLayout.PREFERRED_SIZE, 264, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		
		lblNewuserName = new JLabel("Username:");
		
		txtNewuserName = new JTextField();
		txtNewuserName.setColumns(15);
		
		lblNewuserPassword = new JLabel("Password:");
		
		pwdNewuserPassword = new JPasswordField();
		pwdNewuserPassword.setColumns(15);
		
		lblGroup = new JLabel("Group:");
		
		txtNewuserGroup = new JTextField();
		txtNewuserGroup.setColumns(15);
		
		btnAdd = new JButton("Add");
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				try {
					sqlconn.addUser(txtNewuserName.getText(), pwdNewuserPassword.getText(), "random");
					sqlconn.addUserToGroup(txtNewuserName.getText(), txtNewuserGroup.getText());
					userList.setListData(sqlconn.listUsers());
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					showErrorMessage("User cannot be created");
				}
				
			}
		});
		GroupLayout gl_adduserpanel = new GroupLayout(adduserpanel);
		gl_adduserpanel.setHorizontalGroup(
			gl_adduserpanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_adduserpanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_adduserpanel.createParallelGroup(Alignment.LEADING)
						.addComponent(lblNewuserName)
						.addComponent(txtNewuserName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblNewuserPassword)
						.addComponent(pwdNewuserPassword, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblGroup)
						.addComponent(txtNewuserGroup, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnAdd, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 169, Short.MAX_VALUE))
					.addContainerGap())
		);
		gl_adduserpanel.setVerticalGroup(
			gl_adduserpanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_adduserpanel.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblNewuserName)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(txtNewuserName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblNewuserPassword)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(pwdNewuserPassword, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblGroup)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(txtNewuserGroup, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED, 58, Short.MAX_VALUE)
					.addComponent(btnAdd)
					.addContainerGap())
		);
		adduserpanel.setLayout(gl_adduserpanel);
		managementpanel.setLayout(gl_managementpanel);
		logoutpanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		lblLoggedInAs = new JLabel();
		logoutpanel.add(lblLoggedInAs);
		
		btnLogOut = new JButton("Log out");
		btnLogOut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				sqlconn = null;
				logoutpanel.setVisible(false);
				loginpanel.setVisible(true);
				switchControlPanelAvailability();
				
			}
		});
		logoutpanel.add(btnLogOut);
		loginpanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		logoutpanel.setVisible(false);
		JLabel lblUsername = new JLabel("Username:");
		loginpanel.add(lblUsername);
		
		txtUsername = new JTextField("redi");
		loginpanel.add(txtUsername);
		txtUsername.setColumns(10);
		
		JLabel lblPassword = new JLabel("Password");
		loginpanel.add(lblPassword);
		
		passwordField = new JPasswordField("WxP3abrB");
		passwordField.setColumns(10);
		loginpanel.add(passwordField);
		
		JButton btnLogIn = new JButton("Log in");
		btnLogIn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				password = passwordField.getText();
				username = txtUsername.getText();
				

				try {
					sqlconn = new SQLConnection(SQL_ADDRESS, username, password);
					lblLoggedInAs.setText("Logged in as " + username);
					
					userList.setListData(sqlconn.listUsers());
					
					switchControlPanelAvailability();
					
					logoutpanel.setVisible(true);
					loginpanel.setVisible(false);
					try {
						sqlconn.listUsers();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} catch (SQLException e1) {
					showErrorMessage("Could not connect. Incorrect address, username or password.");
				}

			}
		});
		loginpanel.add(btnLogIn);
		/////////////////////////////////////////////////////////////////////LOGIN PANEL//////////////////////////////////////////////////
		
		contentPane.setLayout(gl_contentPane);
	}
        
        /**
         * Pokaz blad
         * @param text tekst bledu 
         */
	
	private void showErrorMessage(String text){
		JOptionPane.showMessageDialog(frame, text);
	}
	
        /**
         * Zmien dostepnosc panelu
         */
	private static void switchControlPanelAvailability(){
		
		if(userList.isEnabled()){
			
			Vector <String> emptyVector = new Vector<String>();
			userList.setListData(emptyVector);
			
			txtNewuserName.setText("");
			pwdNewuserPassword.setText("");
			txtNewuserGroup.setText("");
			
			userList.setEnabled(false);
			txtNewuserName.setEnabled(false);
			pwdNewuserPassword.setEnabled(false);
			txtNewuserGroup.setEnabled(false);
			btnAdd.setEnabled(false);
			
			
			// TODO clear userlist
		}
		else{
			
			userList.setEnabled(true);
			txtNewuserName.setEnabled(true);
			pwdNewuserPassword.setEnabled(true);
			txtNewuserGroup.setEnabled(true);
			btnAdd.setEnabled(true);

		}
		
	
	}

}
