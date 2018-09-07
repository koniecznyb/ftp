package ftp;

/**
 * Głowny program otwierajacy serwer
 * @author redi
 * @version 1.0
 * @see ftp.CommunicationConnectionHandler
 */
public class FTPMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		CommunicationConnection ftpServer = new CommunicationConnection(3029);

		ftpServer.startListening();

	}

}
