package gui.commands;

import java.io.IOException;

import gui.Messenger;
import client.FTPClient;

public class DELE extends Command {
	
	/**
	 * @uml.property  name="fileName"
	 */
	private String fileName;

        /**
         * @inheritDoc
         */
	public DELE(FTPClient communicationSocket, Messenger messenger, String fileName) {
		super(communicationSocket, messenger);
		this.fileName = fileName;
		// TODO Auto-generated constructor stub
	}
	
	public void invokeCommand() throws IOException{
		
		StringBuilder message = new StringBuilder("DELE " + fileName);
		communicationSocket.sendMessage(message);
		messenger.addAnUserMessage(message);

		messenger.addAnServerMessage(communicationSocket.readMessage(), false);
		
	}

}
