package gui.commands;

import java.io.IOException;

import gui.Messenger;
import client.FTPClient;

public class NOOP extends Command {

         /**
         * @inheritDoc
         */
	public NOOP(FTPClient communicationSocket, Messenger messenger) {
		super(communicationSocket, messenger);
		// TODO Auto-generated constructor stub
	}
	
	public void invokeCommand() throws IOException{
		
		StringBuilder message = new StringBuilder("NOOP");
		communicationSocket.sendMessage(message);
		messenger.addAnUserMessage(message);

		messenger.addAnServerMessage( communicationSocket.readMessage(), false);

	}
	

}
