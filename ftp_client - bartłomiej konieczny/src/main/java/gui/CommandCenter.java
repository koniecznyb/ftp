package gui;

import gui.commands.*;
import client.FTPClient;
/**
 * Baza wszystkich komend
 * @author redi
 * @see gui.commands.Command
 * @version 1.0
 */
public class CommandCenter {

	/**
	 * @return
	 * @uml.property  name="quit"
	 */
	public QUIT getQuit() {
		return quit;
	}

	public void setQuit() {
		this.quit = new QUIT(communicationSocket, messenger);
	}

	/**
	 * @uml.property  name="messenger"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	protected Messenger messenger;
	/**
	 * @uml.property  name="communicationSocket"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	protected FTPClient communicationSocket;
	/**
	 * @uml.property  name="fileTransferSocket"
	 * @uml.associationEnd  
	 */
	protected FTPClient fileTransferSocket;

	/**
	 * @uml.property  name="pasv"
	 * @uml.associationEnd  
	 */
	private PASV pasv;
	/**
	 * @uml.property  name="user"
	 * @uml.associationEnd  
	 */
	private USER user;
	/**
	 * @uml.property  name="pass"
	 * @uml.associationEnd  
	 */
	private PASS pass;
	/**
	 * @uml.property  name="abor"
	 * @uml.associationEnd  
	 */
	private ABOR abor;
	/**
	 * @uml.property  name="cwd"
	 * @uml.associationEnd  
	 */
	private CWD cwd;
	/**
	 * @uml.property  name="mlsd"
	 * @uml.associationEnd  
	 */
	private MLSD mlsd;
	/**
	 * @uml.property  name="pwd"
	 * @uml.associationEnd  
	 */
	private PWD pwd;
	/**
	 * @uml.property  name="retr"
	 * @uml.associationEnd  
	 */
	private RETR retr;
	

	/**
	 * @uml.property  name="typei"
	 * @uml.associationEnd  
	 */
	private TYPEI typei;
	/**
	 * @uml.property  name="quit"
	 * @uml.associationEnd  
	 */
	private QUIT quit;
	/**
	 * @uml.property  name="stor"
	 * @uml.associationEnd  
	 */
	private STOR stor;
	/**
	 * @uml.property  name="noop"
	 * @uml.associationEnd  
	 */
	private NOOP noop;
	/**
	 * @uml.property  name="chmod"
	 * @uml.associationEnd  
	 */
	private CHMOD chmod;
	/**
	 * @uml.property  name="appe"
	 * @uml.associationEnd  
	 */
	private APPE appe;
	/**
	 * @uml.property  name="dele"
	 * @uml.associationEnd  
	 */
	private DELE dele;
	/**
	 * @uml.property  name="mkd"
	 * @uml.associationEnd  
	 */
	private MKD mkd;
	/**
	 * @uml.property  name="rmd"
	 * @uml.associationEnd  
	 */
	private RMD rmd;
	
	/**
	 * @return
	 * @uml.property  name="messenger"
	 */
	public Messenger getMessenger() {
		return messenger;
	}
	
	/**
	 * @return
	 * @uml.property  name="chmod"
	 */
	public CHMOD getChmod() {
		return chmod;
	}

	public void setChmod(String fileName, String permissions) {
		this.chmod = new CHMOD(communicationSocket, messenger, fileName, permissions);
	}

	/**
	 * @return
	 * @uml.property  name="appe"
	 */
	public APPE getAppe() {
		return appe;
	}

	public void setAppe(String fileName, String fileLocation) {
		this.appe = new APPE(communicationSocket, fileTransferSocket, messenger, fileName, fileLocation);
	}

	/**
	 * @return
	 * @uml.property  name="dele"
	 */
	public DELE getDele() {
		return dele;
	}

	public void setDele(String fileName) {
		this.dele = new DELE(communicationSocket, messenger, fileName);
	}

	/**
	 * @return
	 * @uml.property  name="mkd"
	 */
	public MKD getMkd() {
		return mkd;
	}

	public void setMkd(String folderName) {
		this.mkd = new MKD(communicationSocket, messenger, folderName);
	}

	/**
	 * @return
	 * @uml.property  name="rmd"
	 */
	public RMD getRmd() {
		return rmd;
	}

	public void setRmd(String folderName) {
		this.rmd = new RMD(communicationSocket, messenger, folderName);
	}

	/**
	 * @return
	 * @uml.property  name="stor"
	 */
	public STOR getStor() {
		return stor;
	}

	public void setStor(String fileName, String saveLocation) {
		this.stor = new STOR(communicationSocket, fileTransferSocket, messenger, fileName, saveLocation);
	}

	/**
	 * @return
	 * @uml.property  name="noop"
	 */
	public NOOP getNoop() {
		return noop;
	}

	public void setNoop() {
		this.noop = new NOOP(communicationSocket, messenger);
	}

	
	
	public void clearFileTransferSocket(){
		fileTransferSocket.disconnect();
		fileTransferSocket = null;
	}

	/**
	 * @return
	 * @uml.property  name="fileTransferSocket"
	 */
	public FTPClient getFileTransferSocket() {
		return fileTransferSocket;
	}

	/**
	 * @param fileTransferSocket
	 * @uml.property  name="fileTransferSocket"
	 */
	public void setFileTransferSocket(FTPClient fileTransferSocket) {
		this.fileTransferSocket = fileTransferSocket;
	}
	

	public void setPasv() {
		this.pasv = new PASV(communicationSocket, messenger);
	}

	public void setUser() {
		this.user = new USER(communicationSocket, messenger);
	}

	public void setPass() {
		this.pass = new PASS(communicationSocket, messenger);
	}

	public void setAbor(Thread lastCommand) {
		this.abor = new ABOR(communicationSocket, messenger, lastCommand);
	}

	public void setCwd() {
		this.cwd = new CWD(communicationSocket, fileTransferSocket, messenger);
	}

	public void setMlsd() {
		this.mlsd = new MLSD(communicationSocket, fileTransferSocket, messenger);
	}

	public void setPwd() {
		this.pwd = new PWD(communicationSocket, messenger);
	}

	public void setRetr(String fileName, String saveLocation) {
		this.retr = new RETR(communicationSocket, fileTransferSocket, messenger, fileName, saveLocation);
	}

	public void setTypei() {
		this.typei = new TYPEI(communicationSocket, messenger);
	}

	/**
	 * @return
	 * @uml.property  name="communicationSocket"
	 */
	public FTPClient getCommunicationSocket() {
		return communicationSocket;
	}

	/**
	 * @param communicationSocket
	 * @uml.property  name="communicationSocket"
	 */
	public void setCommunicationSocket(FTPClient communicationSocket) {
		this.communicationSocket = communicationSocket;
	}

	/**
	 * @return
	 * @uml.property  name="abor"
	 */
	public ABOR getAbor() {
		return abor;
	}

	/**
	 * @return
	 * @uml.property  name="cwd"
	 */
	public CWD getCwd() {
		return cwd;
	}

	/**
	 * @return
	 * @uml.property  name="mlsd"
	 */
	public MLSD getMlsd() {
		return mlsd;
	}

	/**
	 * @return
	 * @uml.property  name="pwd"
	 */
	public PWD getPwd() {
		return pwd;
	}

	/**
	 * @return
	 * @uml.property  name="retr"
	 */
	public RETR getRetr() {
		return retr;
	}

	/**
	 * @return
	 * @uml.property  name="typei"
	 */
	public TYPEI getTypei() {
		return typei;
	}

	/**
	 * @return
	 * @uml.property  name="pass"
	 */
	public PASS getPass() {
		return pass;
	}

	/**
	 * @return
	 * @uml.property  name="user"
	 */
	public USER getUser() {
		return user;
	}

	/**
	 * @return
	 * @uml.property  name="pasv"
	 */
	public PASV getPasv() {
		return pasv;
	}

	public CommandCenter(FTPClient communicationSocket, Messenger messenger) {
		this.communicationSocket = communicationSocket;
		this.messenger = messenger;
	}

	public CommandCenter(FTPClient communicationSocket, FTPClient fileTransferSocket, Messenger messenger) {
		this.communicationSocket = communicationSocket;
		this.fileTransferSocket = fileTransferSocket;
		this.messenger = messenger;
	}
}
