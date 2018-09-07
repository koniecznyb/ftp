package gui.commands;

import gui.Messenger;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import client.FTPClient;

public class RETR extends Command implements Runnable {

	/**
	 * @uml.property  name="fileName"
	 */
	private String fileName;
	/**
	 * @uml.property  name="saveLocation"
	 */
	private String saveLocation;

        /**
         * @inheritDoc
         */
	public RETR(FTPClient communicationSocket, FTPClient fileTransferSocket, Messenger messenger, String fileName, String saveLocation) {
		super(communicationSocket, fileTransferSocket, messenger);
		this.fileName = fileName;
		this.saveLocation = saveLocation;
	}

	@Override
	public void run() {

		if (fileTransferSocket == null) {

			StringBuilder message = new StringBuilder("RETR " + fileName);
			communicationSocket.sendMessage(message);
			messenger.addAnUserMessage(message);

			try {
				messenger.addAnServerMessage(communicationSocket.readMessage(), false);
			} catch (IOException e) {
				e.printStackTrace();
			}

		} else {

			StringBuilder message = new StringBuilder("RETR " + fileName);
			communicationSocket.sendMessage(message);
			messenger.addAnUserMessage(message);

			StringBuilder response = new StringBuilder("");

			try {
				if ((response = communicationSocket.readMessage()).substring(0, 3).compareTo("150") == 0) {

					messenger.addAnServerMessage(response, false);

					OutputStream out = new FileOutputStream(saveLocation + "\\" + fileName);
					copy(fileTransferSocket.getFileIn(), out);

					out.close();
					
					messenger.addAnServerMessage(communicationSocket.readMessage(), false);
				} else {
					messenger.addAnServerMessage(response, false);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

	}

	private void copy(InputStream in, OutputStream out) throws IOException {
		byte[] buf = new byte[8192];
		int len = 0;
		while ((len = in.read(buf)) != -1) {
			out.write(buf, 0, len);
		}
	}
}
