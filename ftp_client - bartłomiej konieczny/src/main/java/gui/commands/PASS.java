package gui.commands;

import gui.Messenger;

import java.io.IOException;

import client.FTPClient;

public class PASS extends Command {
	
	public StringBuilder invokeCommand(String password) throws IOException {

		StringBuilder response = new StringBuilder();
		StringBuilder message = new StringBuilder("PASS " + password);
		communicationSocket.sendMessage(message);
		messenger.addAnUserMessage(message);

		messenger.addAnServerMessage(response = communicationSocket.readMessage(), false);
		
		return response;
	}

        /**
         * @inheritDoc
         */
	public PASS(FTPClient communicationSocket, Messenger messenger) {
		super(communicationSocket, messenger);
		// TODO Auto-generated constructor stub
	}

}
