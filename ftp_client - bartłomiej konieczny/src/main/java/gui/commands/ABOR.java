package gui.commands;

import gui.Messenger;

import java.io.IOException;

import client.FTPClient;

/**
 * Komenda przerywajaca watek innej komendy
 * @author redi
 * @version 1.0
 */
public class ABOR extends Command implements Runnable {

	/**
	 * @uml.property  name="threadToInterrupt"
	 */
	Thread threadToInterrupt;

	public ABOR(FTPClient communicationSocket, Messenger messenger, Thread threadToInterrupt) {
		super(communicationSocket, messenger);
		this.threadToInterrupt = threadToInterrupt;

	}

        /**
         * @inheritDoc
         */
	@Override
	public void run() {

		StringBuilder message = new StringBuilder("ABOR");
		communicationSocket.sendMessage(message);
		messenger.addAnUserMessage(message);
		threadToInterrupt.interrupt();
		try {
			messenger.addAnServerMessage(communicationSocket.readMessage(), false);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
