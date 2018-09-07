package gui;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.TransferHandler;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.DefaultCaret;
import javax.swing.tree.TreePath;

import net.miginfocom.swing.MigLayout;
import client.FTPClient;

public class mainWindow extends JFrame {

	/**
	 * 
	 */
	private static MouseAdapter remoteTableMouseListener;
	private static final long serialVersionUID = 1L;
	private static JPanel contentPane;
	private static JTextField txtServername;
	private static JLabel lblUsername;
	private static JTextField txtUsername;
	private static JLabel lblPassword;
	private static JLabel lblPort;
	private static JTextField txtPort;
	private static JLabel lblCommandLine;
	private static JTextField txtCommandLine;
	private static JLabel lblCommunication;
	private static JTextPane textPaneCommunication;
	private static JPanel fileManagementPanel;
	private static JPasswordField pwdPassword;
	private static JButton btnConnect;
	private static JLabel lblServer;
	private static mainWindow frame;
	private static JButton btnSend;
	private static JButton btnDisconnect;
	private static JScrollPane jsp;
	private static String currentDirectory = "./files";
	private static Vector<Object[]> remoteDirectory;
	private static JTable remoteTable;
	private static JFileChooser localList;
	private static DefaultTableModel model;
	private static Thread lastCommand;
	private static Messenger messenger;
	private static CommandCenter cc;
	private static FTPClient communicationSocket;
	private static Timer timer;
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					
					try {
						System.setErr(new PrintStream(new FileOutputStream("./error.log")));
					} catch (FileNotFoundException ex) {
						ex.printStackTrace();
					}
					
					frame = new mainWindow();
					switchPanelAvailability();
					// frame.setExtendedState(frame.getExtendedState() |
					// JFrame.MAXIMIZED_BOTH);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public mainWindow() {

		

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1200, 700);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new MigLayout("", "[][grow]", "[][150px:150px:150px][][grow]"));

		lblServer = new JLabel("Server:");
		contentPane.add(lblServer, "cell 0 0,alignx trailing");

		txtServername = new JTextField();

		contentPane.add(txtServername, "flowx,cell 1 0,alignx left");
		txtServername.setColumns(15);

		lblUsername = new JLabel("Username:");
		contentPane.add(lblUsername, "cell 1 0");

		txtUsername = new JTextField();

		contentPane.add(txtUsername, "cell 1 0");
		txtUsername.setColumns(15);

		lblPassword = new JLabel("Password:");
		contentPane.add(lblPassword, "cell 1 0");

		pwdPassword = new JPasswordField();
		pwdPassword.setColumns(15);

		contentPane.add(pwdPassword, "cell 1 0");

		lblPort = new JLabel("Port:");
		contentPane.add(lblPort, "cell 1 0");

		txtPort = new JTextField();

		contentPane.add(txtPort, "cell 1 0");
		txtPort.setColumns(4);

		// //////////////////////////////////////// DEFAULT SETTINGS
		// ///////////////////////////////////////////////////////////////////////

		txtServername.setText("localhost");
		txtUsername.setText("redi");

//		txtPort.setText("21");
		pwdPassword.setText("haslo");

		 txtPort.setText("3021");

		lblCommunication = new JLabel("Communication:");
		contentPane.add(lblCommunication, "cell 0 1");

		textPaneCommunication = new JTextPane();
		textPaneCommunication.setEditable(false);
		jsp = new JScrollPane(textPaneCommunication);

		DefaultCaret caret = (DefaultCaret) textPaneCommunication.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

		messenger = new Messenger(textPaneCommunication.getStyledDocument());
		jsp.setBorder(new LineBorder(new Color(0, 0, 0)));
		contentPane.add(jsp, "cell 1 1,grow");

		lblCommandLine = new JLabel("Command line:");
		contentPane.add(lblCommandLine, "cell 0 2,alignx trailing");

		txtCommandLine = new JTextField();
		contentPane.add(txtCommandLine, "flowx,cell 1 2,growx");
		txtCommandLine.setColumns(10);

		fileManagementPanel = new JPanel();
		fileManagementPanel.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0)), "File management", TitledBorder.LEADING, TitledBorder.TOP,
				null, null));
		contentPane.add(fileManagementPanel, "cell 1 3,grow");
		fileManagementPanel.setLayout(new GridLayout(1, 0, 0, 0));

		btnConnect = new JButton("Connect");
		btnConnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				try {
					InetAddress address = InetAddress.getByName(txtServername.getText());
					communicationSocket = new FTPClient(address, Integer.parseInt(txtPort.getText()));

					try {

						if (!communicationSocket.connect()) {

							showErrorMessage("Not connected ( returned false )");
						} else {

							switchPanelAvailability();
							StringBuilder currentMessage = new StringBuilder("Connected!\n");

							messenger.addAnServerMessage(currentMessage, true);
							messenger.addAnServerMessage(communicationSocket.readMessage(), false);

							String username = txtUsername.getText();
							@SuppressWarnings("deprecation")
							String password = pwdPassword.getText();

							cc = new CommandCenter(communicationSocket, messenger);
							cc.setUser();
							cc.getUser().invokeCommand(username);

							cc.setPass();

							String isPassCorrect = new String(cc.getPass().invokeCommand(password).toString());
							if (!isPassCorrect.substring(0, 3).matches("230")) {

								showErrorMessage(isPassCorrect);
								disconnect();

							} else {

								timer = new Timer();
								TimerTask minuteTask = new TimerTask() {
									@Override
									public void run() {
										try {
											cc.setNoop();
											cc.getNoop().invokeCommand();
										} catch (IOException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
									}
								};

								timer.schedule(minuteTask, 55 * 1000l, 1000 * 60);

								cc.setTypei();
								cc.getTypei().invokeCommand();

								cc.setPwd();
								cc.getPwd().invokeCommand();

								cc.setPasv();

								cc.setFileTransferSocket(cc.getPasv().invokeCommand());
								cc.setMlsd();
								remoteDirectory = cc.getMlsd().invokeCommand();
								cc.clearFileTransferSocket();

								localList = new JFileChooser();
								localList.setControlButtonsAreShown(false);
								localList.setCurrentDirectory(new File(currentDirectory));

								fileManagementPanel.add(localList);

								remoteTable = new JTable() {
									private static final long serialVersionUID = 1L;

									public boolean isCellEditable(int row, int column) {
										return false;
									}
								};
								remoteTable.setModel(new DefaultTableModel(new Object[][] { { null, null }, }, new String[] { "File Name",
										"File Type" }) {

									private static final long serialVersionUID = 1L;
									@SuppressWarnings("rawtypes")
									Class[] columnTypes = new Class[] { Object.class, String.class };

									@SuppressWarnings({ "rawtypes", "unchecked" })
									public Class getColumnClass(int columnIndex) {
										return columnTypes[columnIndex];
									}
								});

								model = (DefaultTableModel) remoteTable.getModel();

								JScrollPane remoteListScrollPane = new JScrollPane(remoteTable);

								remoteTable.setDragEnabled(true);
								fileManagementPanel.add(remoteListScrollPane);
								fileManagementPanel.revalidate();
								updateTheTable();

								remoteTableMouseListener = new MouseAdapter() {
									public void mousePressed(MouseEvent e) {

										if (e.getClickCount() == 2) {

											JTable target = (JTable) e.getSource();
											int row = target.getSelectedRow();
											int column = target.getSelectedColumn();
											if (column == 0
													&& (target.getValueAt(row, 1).toString().matches(".dir") || target.getValueAt(row, 1).toString()
															.matches("dir")))
												try {
													cc.setCwd();
													cc.getCwd().invokeCommand(target.getValueAt(row, column).toString());

													cc.setPasv();
													cc.setFileTransferSocket(cc.getPasv().invokeCommand());

													cc.setMlsd();
													remoteDirectory = cc.getMlsd().invokeCommand();
													updateTheTable();

													cc.clearFileTransferSocket();

													cc.setPwd();
													cc.getPwd().invokeCommand();

													fileManagementPanel.revalidate();

												} catch (IOException e1) {
													e1.printStackTrace();
												}

										}
									}
								};

								remoteTable.addMouseListener(remoteTableMouseListener);

								class JFileChooserHandler extends TransferHandler {

									private static final long serialVersionUID = 1L;

									public boolean canImport(TransferSupport support) {
										if (!support.isDrop()) {
											return false;
										}

										return support.isDataFlavorSupported(DataFlavor.stringFlavor);
									}

									public boolean importData(TransferSupport support) {
										if (!canImport(support)) {
											return false;
										}

										Transferable transferable = support.getTransferable();
										String line;
										try {
											line = (String) transferable.getTransferData(DataFlavor.stringFlavor);
										} catch (Exception e) {
											return false;
										}

										if (line.substring(line.lastIndexOf("\t") + 1).matches("file")) {

											String fileName = line.substring(0, line.lastIndexOf("\t") + 1).trim();
											try {
												cc.setPasv();
												cc.setFileTransferSocket(cc.getPasv().invokeCommand());

												cc.setRetr(fileName, localList.getCurrentDirectory().toString());
												lastCommand = new Thread(cc.getRetr());
												lastCommand.start();

												localList.rescanCurrentDirectory();

												cc.clearFileTransferSocket();

											} catch (IOException e) {
												e.printStackTrace();
											}
										} else {
											return false;
										}
										return true;
									}
								}

								class JTableHandler extends TransferHandler {

									private static final long serialVersionUID = 1L;

									public boolean canImport(TransferSupport support) {
										if (!support.isDrop()) {
											return false;
										}

										return support.isDataFlavorSupported(DataFlavor.stringFlavor);
									}

									protected void exportDone(JComponent c, Transferable t, int action) {

										System.out.print("siema");

									}

									public boolean importData(TransferSupport support) {
										if (!canImport(support)) {
											return false;
										}

										Transferable transferable = support.getTransferable();
										String line;
										try {
											line = (String) transferable.getTransferData(DataFlavor.stringFlavor);
										} catch (Exception e) {
											return false;
										}

										File file = new File(line);
										String fileName = file.getName();

										try {
											cc.setPasv();
											cc.setFileTransferSocket(cc.getPasv().invokeCommand());

											cc.setStor(fileName, localList.getCurrentDirectory().toString());
											cc.getStor().invokeCommand();

											cc.setPasv();
											cc.setFileTransferSocket(cc.getPasv().invokeCommand());

											cc.setMlsd();
											remoteDirectory = cc.getMlsd().invokeCommand();
											updateTheTable();

											if (cc.getFileTransferSocket() != null)
												cc.clearFileTransferSocket();

										} catch (IOException e) {
											e.printStackTrace();
										}

										return true;
									}
								}

								 localList.setTransferHandler(new
								 JFileChooserHandler());
//								 remoteTable.setTransferHandler(new
//								 JTableHandler());

								localList.setDragEnabled(true);
								remoteTable.setDragEnabled(true);

							}
						}
					} catch (IOException e) {
						showErrorMessage("Can't connect");
					}

				} catch (UnknownHostException e1) {
					showErrorMessage("Unrecognized address!");
					e1.printStackTrace();
				}

			}
		});
		contentPane.add(btnConnect, "cell 1 0");

		btnSend = new JButton("Send");

		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				try {

					StringBuilder command = new StringBuilder(txtCommandLine.getText());

					String[] splitMessage = command.toString().split("\\s+");

					String message = splitMessage[0];
					String filename = new String("");

					for (int i = 1; i < splitMessage.length; i++) {
						if (i == 1)
							filename = splitMessage[i];
						else
							filename = filename + " " + splitMessage[i];
					}

					String chmodPermissions = "";
					if (splitMessage.length == 3) {
						chmodPermissions = splitMessage[2];
					}

					switch (message.toString()) {
					case "PASV": {
						cc.setPasv();
						cc.setFileTransferSocket(cc.getPasv().invokeCommand());
						break;
					}
					case "MLSD": {
						cc.setMlsd();
						remoteDirectory = cc.getMlsd().invokeCommand();
						updateTheTable();

						if (cc.fileTransferSocket != null)
							cc.clearFileTransferSocket();
						break;
					}
					case "PWD": {
						cc.setPwd();
						cc.getPwd().invokeCommand();
						break;
					}
					case "QUIT": {
						cc.setQuit();
						cc.getQuit().invokeCommand();
						disconnect();
						break;
					}
					case "ABOR": {
						if (lastCommand != null) {
							cc.setAbor(lastCommand);
							new Thread(cc.getAbor()).start();
						} else {
							messenger.addAnUserMessage(new StringBuilder("Nothing is running"));
						}
						break;
					}
					default: {
						if (message.matches("RETR.*")) {
							cc.setRetr(filename, localList.getCurrentDirectory().toString());
							lastCommand = new Thread(cc.getRetr());
							lastCommand.start();
							localList.rescanCurrentDirectory();

							if (cc.fileTransferSocket != null)
								cc.clearFileTransferSocket();
						} else if (message.matches("CWD.*")) {

							cc.setCwd();
							cc.getCwd().invokeCommand(filename);

							if (cc.fileTransferSocket != null)
								cc.clearFileTransferSocket();

						} else if (message.matches("STOR.*")) {
							cc.setStor(filename, localList.getCurrentDirectory().toString());
							cc.getStor().invokeCommand();

							if (cc.fileTransferSocket != null)
								cc.clearFileTransferSocket();

						} else if (message.matches("CHMOD.*")) {
							cc.setChmod(filename, chmodPermissions);
							cc.getChmod().invokeCommand();
							// TODO do zrobienia

						} else if (message.matches("APPE.*")) {
							cc.setAppe(filename, localList.getCurrentDirectory().toString());
							cc.getAppe().invokeCommand();

							if (cc.fileTransferSocket != null)
								cc.clearFileTransferSocket();

						} else if (message.matches("DELE.*")) {
							cc.setDele(filename);
							cc.getDele().invokeCommand();

							if (cc.fileTransferSocket != null)
								cc.clearFileTransferSocket();

						} else if (message.matches("MKD.*")) {
							cc.setMkd(filename);
							cc.getMkd().invokeCommand();

							if (cc.fileTransferSocket != null)
								cc.clearFileTransferSocket();

						} else if (message.matches("RMD.*")) {
							cc.setRmd(filename);
							cc.getRmd().invokeCommand();

							if (cc.fileTransferSocket != null)
								cc.clearFileTransferSocket();

						} else if (message.matches("get.*")) {

							cc.getMessenger().addAnFtpCommandMessage(new StringBuilder("FTP COMMAND GET"));

							cc.setPasv();
							cc.setFileTransferSocket(cc.getPasv().invokeCommand());

							cc.setRetr(filename, localList.getCurrentDirectory().toString());
							lastCommand = new Thread(cc.getRetr());
							lastCommand.start();
							localList.rescanCurrentDirectory();

							if (cc.fileTransferSocket != null)
								cc.clearFileTransferSocket();

						} else if (message.matches("put.*")) {

							cc.getMessenger().addAnFtpCommandMessage(new StringBuilder("FTP COMMAND PUT"));

							cc.setPasv();
							cc.setFileTransferSocket(cc.getPasv().invokeCommand());

							cc.setStor(filename, localList.getCurrentDirectory().toString());
							cc.getStor().invokeCommand();
							localList.rescanCurrentDirectory();

							if (cc.fileTransferSocket != null)
								cc.clearFileTransferSocket();

							cc.setPasv();
							cc.setFileTransferSocket(cc.getPasv().invokeCommand());

							cc.setMlsd();
							remoteDirectory = cc.getMlsd().invokeCommand();
							updateTheTable();

							if (cc.fileTransferSocket != null)
								cc.clearFileTransferSocket();

						} else if (message.matches("dir.*")) {

							cc.getMessenger().addAnFtpCommandMessage(new StringBuilder("FTP COMMAND DIR"));

							cc.setPasv();
							cc.setFileTransferSocket(cc.getPasv().invokeCommand());

							cc.setMkd(filename);
							cc.getMkd().invokeCommand();
							localList.rescanCurrentDirectory();

							cc.setMlsd();
							remoteDirectory = cc.getMlsd().invokeCommand();
							updateTheTable();

							if (cc.fileTransferSocket != null)
								cc.clearFileTransferSocket();

						} else if (message.matches("user.*")) {

							cc.getMessenger().addAnFtpCommandMessage(new StringBuilder("FTP COMMAND DIR"));

							cc.setPasv();
							cc.setFileTransferSocket(cc.getPasv().invokeCommand());

							cc.setMkd(filename);
							cc.getMkd().invokeCommand();
							localList.rescanCurrentDirectory();

							cc.setMlsd();
							remoteDirectory = cc.getMlsd().invokeCommand();
							updateTheTable();

							if (cc.fileTransferSocket != null)
								cc.clearFileTransferSocket();

						} else if (splitMessage[0] != "")
							messenger.addAnUserMessage(new StringBuilder(splitMessage[0] + ": Command not found!"));
						else
							messenger.addAnUserMessage(new StringBuilder("Write a command."));
						break;
					}
					}

					txtCommandLine.setText("");
				} catch (IOException e) {

					e.printStackTrace();
				}

			}
		});
		contentPane.add(btnSend, "cell 1 2");

		btnDisconnect = new JButton("Disconnect");
		btnDisconnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				if (cc.getCommunicationSocket() == null) {
					showErrorMessage("You are not connected!");
				} else {
					disconnect();
				}
			}
		});
		contentPane.add(btnDisconnect, "cell 1 0");
	}

	protected void disconnect() {

		cc.getCommunicationSocket().disconnect();
		cc.setCommunicationSocket(null);
		communicationSocket = null;
		fileManagementPanel.removeAll();
		fileManagementPanel.validate();
		fileManagementPanel.repaint();
		switchPanelAvailability();

		if (timer != null) {
			timer.cancel();
			timer.purge();
		}

	}

	private void updateTheTable() {
		model.getDataVector().removeAllElements();

		for (Object[] o : remoteDirectory) {
			model.addRow(o);
		}

		remoteTable.revalidate();
	}

	private void openFile(int row, TreePath path) {
		if (path.getPath().length > 1) {
			String filePath = path.getPath()[0].toString() + "\\" + path.getPath()[1].toString();
			try {
				Desktop.getDesktop().open(new File(filePath));
			} catch (IOException e) {

				e.printStackTrace();
			}
		}
	}

	private void showErrorMessage(String text) {
		JOptionPane.showMessageDialog(frame, text);
	}

	private static void switchPanelAvailability() {

		if (communicationSocket == null) {

			frame.getRootPane().setDefaultButton(btnConnect);
			contentPane.setEnabled(false);
			txtCommandLine.setEnabled(false);
			lblCommandLine.setEnabled(false);

			lblCommunication.setEnabled(false);
			textPaneCommunication.setEnabled(false);

			fileManagementPanel.setEnabled(false);

			btnDisconnect.setEnabled(false);
			btnSend.setEnabled(false);

			btnConnect.setEnabled(true);

			txtServername.setEnabled(true);
			lblServer.setEnabled(true);

			lblPassword.setEnabled(true);
			pwdPassword.setEnabled(true);

			lblUsername.setEnabled(true);
			txtUsername.setEnabled(true);

			lblPort.setEnabled(true);
			txtPort.setEnabled(true);

			textPaneCommunication.setText("");
		} else {

			frame.getRootPane().setDefaultButton(btnSend);
			contentPane.setEnabled(true);
			txtCommandLine.setEnabled(true);
			lblCommandLine.setEnabled(true);

			lblCommunication.setEnabled(true);
			textPaneCommunication.setEnabled(true);

			fileManagementPanel.setEnabled(true);

			btnDisconnect.setEnabled(true);
			btnSend.setEnabled(true);

			btnConnect.setEnabled(false);

			txtServername.setEnabled(false);
			lblServer.setEnabled(false);

			lblPassword.setEnabled(false);
			pwdPassword.setEnabled(false);

			lblUsername.setEnabled(false);
			txtUsername.setEnabled(false);

			lblPort.setEnabled(false);
			txtPort.setEnabled(false);
		}
	}

}