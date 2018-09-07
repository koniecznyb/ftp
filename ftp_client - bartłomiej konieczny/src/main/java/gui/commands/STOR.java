package gui.commands;

import gui.Messenger;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import client.FTPClient;

/**
 * STOR
 * @author redi
 */
public class STOR extends Command{

	/**
	 * @uml.property  name="fileName"
	 */
	private String fileName;
	/**
	 * @uml.property  name="fileLocation"
	 */
	private String fileLocation;

        /**
         * @inheritDoc
         */
	public STOR(FTPClient communicationSocket, FTPClient fileTransferSocket, Messenger messenger, String fileName, String fileLocation) {
		super(communicationSocket, fileTransferSocket, messenger);
		this.fileName = fileName;
		this.fileLocation = fileLocation;
	}

        /**
         * Wywolaj komende
         */
	public void invokeCommand() {

		if (fileTransferSocket == null) {

			StringBuilder message = new StringBuilder("STOR " + fileName);
			communicationSocket.sendMessage(message);
			messenger.addAnUserMessage(message);

			try {
				messenger.addAnServerMessage(communicationSocket.readMessage(), false);
			} catch (IOException e) {
				e.printStackTrace();
			}

		} else {

			StringBuilder message = new StringBuilder("STOR " + fileName);
			communicationSocket.sendMessage(message);
			messenger.addAnUserMessage(message);
			
			
			StringBuilder response;
			try {
				response = new StringBuilder(communicationSocket.readMessage());
				if (response.substring(0, 3).compareTo("150") == 0) {

					messenger.addAnServerMessage(response, false);

					BufferedInputStream in = new BufferedInputStream(new FileInputStream(new File(fileLocation + "\\" + fileName)));

					BufferedOutputStream out = new BufferedOutputStream(fileTransferSocket.getFileOut());
					out.flush();

					send(out, in);

					out.close();
					in.close();

					messenger.addAnServerMessage(communicationSocket.readMessage(), false);

				} else {
					messenger.addAnServerMessage(response, false);
				}
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

	}

	private void send(BufferedOutputStream out, BufferedInputStream input) throws IOException {
		byte[] buffer = new byte[4096];
		int bytesRead = 0;
		while ((bytesRead = input.read(buffer)) != -1) {
			out.write(buffer, 0, bytesRead);
		}
	}

}
