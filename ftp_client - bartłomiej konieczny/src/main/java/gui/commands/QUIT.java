package gui.commands;

import java.io.IOException;

import gui.Messenger;
import client.FTPClient;

public class QUIT extends Command {

          /**
         * @inheritDoc
         */
	public QUIT(FTPClient communicationSocket, Messenger messenger) {
		super(communicationSocket, messenger);
		// TODO Auto-generated constructor stub
	}
	
	public void invokeCommand(){
		
		StringBuilder message = new StringBuilder("QUIT");
		communicationSocket.sendMessage(message);
		messenger.addAnUserMessage(message);
		try {
			messenger.addAnServerMessage(communicationSocket.readMessage(), false);
		} catch (IOException e) {
			e.printStackTrace();
			
		}
	}

}
