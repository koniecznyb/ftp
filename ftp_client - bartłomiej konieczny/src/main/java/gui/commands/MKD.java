package gui.commands;

import java.io.IOException;

import gui.Messenger;
import client.FTPClient;

public class MKD extends Command {
	
	/**
	 * @uml.property  name="folderName"
	 */
	private String folderName;

        /**
         * @inheritDoc
         */
	public MKD(FTPClient communicationSocket, Messenger messenger, String folderName) {
		super(communicationSocket, messenger);
		this.folderName = folderName;
		// TODO Auto-generated constructor stub
	}
	
	public void invokeCommand() throws IOException{
		StringBuilder message = new StringBuilder("MKD " + folderName);
		communicationSocket.sendMessage(message);
		messenger.addAnUserMessage(message);

		messenger.addAnServerMessage(communicationSocket.readMessage(), false);
	}

}
