package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Głowna klasa reprezentujaca konkretne połaczenie.
 * @author redi
 * @see gui.mainWindow
 * @version 1.0
 */
public class FTPClient {

	/**
	 * @uml.property  name="connection"
	 */
	private Socket connection;
	/**
	 * @uml.property  name="address"
	 */
	private InetAddress address;
	/**
	 * @uml.property  name="port"
	 */
	private int port;
	/**
	 * @uml.property  name="out"
	 */
	private PrintWriter out;
	/**
	 * @uml.property  name="in"
	 */
	private BufferedReader in;
	/**
	 * @uml.property  name="fileIn"
	 */
	private InputStream fileIn;
	/**
	 * @uml.property  name="fileOut"
	 */
	private OutputStream fileOut;

	public FTPClient(InetAddress address, int port) {

		this.address = address;
		this.port = port;

	}

	/**
	 * @return
	 * @uml.property  name="fileIn"
	 */
	public InputStream getFileIn() {
		return fileIn;
	}

	public boolean connect() throws IOException {

		connection = new Socket(address, port);

		if (connection == null)
			return false;
		else {
			openStreams();
			return true;
		}

	}

	/**
	 * @return
	 * @uml.property  name="connection"
	 */
	public Socket getConnection() {
		return connection;
	}

	public boolean isConnected(){
		
		if(connection == null)
			return false;
		return true;
		
	}
	public void disconnect() {

		this.sendMessage(new StringBuilder("QUIT"));

	}

        /**
         * Otwiera zródla polaczen
         * @throws IOException gdy nie udało sie otworzyc
         */
	private void openStreams() throws IOException {

		out = new PrintWriter(connection.getOutputStream(), true);
		in = new BufferedReader(new InputStreamReader(
				connection.getInputStream()));
		fileIn = connection.getInputStream();
		fileOut = connection.getOutputStream();

	}
	
	/**
	 * @return
	 * @uml.property  name="fileOut"
	 */
	public OutputStream getFileOut() {
		return fileOut;
	}

	/**
	 * @param connection
	 * @uml.property  name="connection"
	 */
	public void setConnection(Socket connection) {
		this.connection = connection;
	}

	public void sendMessage(StringBuilder message) {

		out.println(message.toString());

	}

        /**
         * Odczytaj wiadomosc
         * @return odczytana wiadomosc
         * @throws IOException  gdy nie powiodlo sie
         */
	public StringBuilder readMessage() throws IOException {

		StringBuilder result = new StringBuilder();
		String line = "";
	    line = in.readLine();
	    		
	    result.append(line);
	    result.append("\n");

		return result;
		
	}

        /**
         * Odczytaj wiecej wiadomosci
         * @return odczytane wiadomosci
         * @throws IOException gdy nie powiodlo sie
         */
	public StringBuilder readMessages() throws IOException {

		StringBuilder result = new StringBuilder();
		String line = "";
		while( (line = in.readLine()) != null){
			result.append(line);
			result.append("\n");
		}
		
		

		return result;
	}

}
