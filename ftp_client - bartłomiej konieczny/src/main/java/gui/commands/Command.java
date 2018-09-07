package gui.commands;

import gui.Messenger;
import client.FTPClient;

/**
 * Klasa bazowa dla wszystkich komend
 * @author redi
 * @version 1.0
 * @see gui.mainWindow
 */
public class Command {
	
	/**
	 * @uml.property  name="communicationSocket"
	 * @uml.associationEnd  
	 */
	protected FTPClient communicationSocket;
	/**
	 * @uml.property  name="fileTransferSocket"
	 * @uml.associationEnd  
	 */
	protected FTPClient fileTransferSocket;
	/**
	 * @uml.property  name="messenger"
	 * @uml.associationEnd  
	 */
	protected Messenger messenger;
	
	Command(){};
	
        /**
         * Konstruktor nadajacy domyslne sockety
         * @param communicationSocket socket do komunikacji
         * @param fileTransferSocket socket do przesyłu danych
         * @param messenger messenger
         * @see gui.Messenger
         */
	public Command(FTPClient communicationSocket, FTPClient fileTransferSocket, Messenger messenger) {
		this.communicationSocket = communicationSocket;
		this.fileTransferSocket = fileTransferSocket;
		this.messenger = messenger;
	}

        /**
         * Konstruktor nadajacy domyslne sockety
         * @param communicationSocket socket do komunikacji
         * @param messenger messenger
         * @see gui.Messenger
         */
	public Command(FTPClient communicationSocket, Messenger messenger) {
		this.communicationSocket = communicationSocket;
		this.messenger = messenger;
	}
	
	

}