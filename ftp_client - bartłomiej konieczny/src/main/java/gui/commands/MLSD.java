package gui.commands;

import gui.Messenger;

import java.io.IOException;
import java.util.Vector;

import client.FTPClient;

public class MLSD extends Command {

	public Vector<Object[]> invokeCommand() throws IOException {

		class MLSDOutputParser {
			String currentLine;

			public MLSDOutputParser(String currentLine) {

				this.currentLine = currentLine;

			}

			private Object[] parseMLSDOutput() {
				
				String fileType = currentLine.substring(currentLine.lastIndexOf("type=") + 5,
						currentLine.indexOf(";", currentLine.lastIndexOf("type=") + 1));
				String fileName = currentLine.substring(currentLine.lastIndexOf(";") + 1).trim();

				return new Object[] { fileName, fileType };

			}
		}

		if (fileTransferSocket == null) {
			StringBuilder message = new StringBuilder("MLSD");
			communicationSocket.sendMessage(message);
			messenger.addAnUserMessage(message);

			messenger.addAnServerMessage(communicationSocket.readMessage(), false);

			return null;

		} else {
			StringBuilder message = new StringBuilder("MLSD");
			communicationSocket.sendMessage(message);
			messenger.addAnUserMessage(message);

			messenger.addAnServerMessage(communicationSocket.readMessage(), false);

			StringBuilder fileList = new StringBuilder(fileTransferSocket.readMessages());
			String[] lines = fileList.toString().split("\\n");
			Vector<Object[]> parsedFileList = new Vector<Object[]>();
			parsedFileList.add(new Object[]{"..", "dir"});
			for (String s : lines) {
				if(s.length() != 0){
					MLSDOutputParser parser = new MLSDOutputParser(s);
					parsedFileList.add(parser.parseMLSDOutput());
				}
				
			}
			messenger.addAnServerMessage(communicationSocket.readMessage(), false);

			return parsedFileList;
		}
	}

        /**
         * @inheritDoc
         */
	public MLSD(FTPClient communicationSocket, FTPClient fileTransferSocket, Messenger messenger) {
		super(communicationSocket, fileTransferSocket, messenger);
	}


}
