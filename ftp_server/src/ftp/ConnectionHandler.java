package ftp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Klasa bazowa implementująca threadPool serwera.
 * @author redi
 * @version 1.0
 * @see ftp.CommunicationConnectionHandler
 */
public abstract class ConnectionHandler implements Runnable {

	protected Socket socketToHandle;
	protected PrintWriter out;
	protected BufferedReader in;
	
	public ConnectionHandler(Socket socketToHandle){
		this.socketToHandle = socketToHandle;
	}
	
        /**
         * Odczytywane wiadomosci z serwera
         * @return odczytana wiadomosc
         * @throws IOException gdy nie powiodlo sie
         */
	protected String readMessage() throws IOException{
		
		String inputLine = in.readLine();
		
		
		return inputLine;
	}
	
        /**
         * Wysylanie wiadomosci na serwer
         * @param message wiadomosc do wyslania
         */
	protected void sendMessage(String message){
		
		out.println(message);
		
	}
	
        /**
         * Zasadnicza funkcja obsługujaca polaczenie
         * @throws IOException gdy nie powiedzie sie otworzenie strumieni
         * @see ftp.FileTransferConnection
         */
	protected void handleConnection() throws IOException{}

	@Override
	public void run() {
		
		try {
			out = new PrintWriter(socketToHandle.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(socketToHandle.getInputStream()));;
			
			handleConnection();
			
			out.close();
			in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
