package gui.commands;

import gui.Messenger;

import java.io.IOException;

import client.FTPClient;

public class PWD extends Command {
	
        /**
         * @inheritDoc
         */
	public PWD(FTPClient communicationSocket, Messenger messenger) {
		super(communicationSocket, messenger);
		// TODO Auto-generated constructor stub
	}

	public void invokeCommand() throws IOException {

		StringBuilder message = new StringBuilder("PWD");
		communicationSocket.sendMessage(message);
		messenger.addAnUserMessage(message);

		messenger.addAnServerMessage(communicationSocket.readMessage(), false);

	}

}
