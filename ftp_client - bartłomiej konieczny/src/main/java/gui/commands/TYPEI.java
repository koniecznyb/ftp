package gui.commands;

import gui.Messenger;

import java.io.IOException;

import client.FTPClient;

/**
 * TYPE I
 * @author redi
 */
public class TYPEI extends Command {
	
         /**
         * @inheritDoc
         */
	public TYPEI(FTPClient communicationSocket, Messenger messenger) {
		super(communicationSocket, messenger);
		// TODO Auto-generated constructor stub
	}

	public void invokeCommand() throws IOException {

		StringBuilder message = new StringBuilder("TYPE I");
		communicationSocket.sendMessage(message);
		messenger.addAnUserMessage(message);

		messenger.addAnServerMessage(communicationSocket.readMessage(), false);

	}

}
