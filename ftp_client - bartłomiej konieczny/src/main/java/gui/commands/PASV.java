package gui.commands;

import gui.Messenger;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import client.FTPClient;

public class PASV extends Command {

        /**
         * @inheritDoc
         */
	public PASV(FTPClient communicationSocket, Messenger messenger) {
		super(communicationSocket, messenger);
		// TODO Auto-generated constructor stub
	}

	public FTPClient invokeCommand() throws IOException {
		
		FTPClient transferSocket;

		StringBuilder message = new StringBuilder("PASV");
		communicationSocket.sendMessage(message);
		messenger.addAnUserMessage(message);

		StringBuilder fileTransferIP = new StringBuilder();
		messenger.addAnServerMessage(fileTransferIP = communicationSocket.readMessage(), false);

		Parser parser = new Parser(fileTransferIP);
		transferSocket = openDataConnection(parser.parseDataConnectionAddress(), parser.parseDataConnectionPort());

		return transferSocket;
	}
	
	private FTPClient openDataConnection(InetAddress address, int port) throws UnknownHostException, IOException {

		FTPClient dataConnection = new FTPClient(address, port);
		dataConnection.connect();
		return dataConnection;

	}

}
