package gui.commands;

import java.io.IOException;

import client.FTPClient;
import gui.Messenger;

public class CWD extends Command {

        /**
         * @inheritDoc
         */
	public CWD(FTPClient communicationSocket, FTPClient fileTransferSocket, Messenger messenger) {
		super(communicationSocket, fileTransferSocket, messenger);
	}

	public boolean invokeCommand(String folderName) throws IOException {

		StringBuilder message = new StringBuilder("CWD " + folderName);
		communicationSocket.sendMessage(message);
		messenger.addAnUserMessage(message);
	
		StringBuilder response = new StringBuilder();
		messenger.addAnServerMessage(response = communicationSocket.readMessage(), false);
		
		if (response.substring(0, 3).compareTo("250") == 0) {
			return true;
		}

		return false;	
	}

}
