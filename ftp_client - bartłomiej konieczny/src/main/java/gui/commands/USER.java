package gui.commands;

import gui.Messenger;

import java.io.IOException;

import client.FTPClient;

public class USER extends Command {
	
     /**
         * @inheritDoc
         */
	public USER(FTPClient communicationSocket, Messenger messenger) {
		super(communicationSocket, messenger);
		// TODO Auto-generated constructor stub
	}

        /**
         * Wywolaj komende
         * @param username username
         * @return rezultat
         * @throws IOException gdy sie nie powiodlo 
         */
	public StringBuilder invokeCommand(String username) throws IOException {

		StringBuilder resultString = new StringBuilder();
		StringBuilder message = new StringBuilder("USER " + username);
		communicationSocket.sendMessage(message);
		messenger.addAnUserMessage(message);

		messenger.addAnServerMessage(resultString = communicationSocket.readMessage(), false);

		return resultString;

	}

}
