package gui.commands;

import java.io.IOException;

import gui.Messenger;
import client.FTPClient;

public class CHMOD extends Command {

	/**
	 * @uml.property  name="fileName"
	 */
	private String fileName;
	/**
	 * @uml.property  name="permissions"
	 */
	private String permissions;
	
        /**
         * @inheritDoc
         */
	public CHMOD(FTPClient communicationSocket, Messenger messenger, String fileName, String permissions) {
		super(communicationSocket, messenger);
		this.fileName = fileName;
		this.permissions = permissions;
		// TODO Auto-generated constructor stub
	}
	
	public void invokeCommand() throws IOException{
		
		StringBuilder message = new StringBuilder("CHMOD " + fileName + " " + permissions);
		communicationSocket.sendMessage(message);
		messenger.addAnUserMessage(message);

		messenger.addAnServerMessage(communicationSocket.readMessage(), false);

	}

}
