package ftp;

import java.io.File;
import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Połaczenie komunikacyjne serwera ftp
 * @author redi
 * @version 1.0
 * @see ftp.CommunicationConnectionHandler
 */
public class CommunicationConnection {
	
	private Socket incomingConnection;
	private ServerSocket serverSocket;
	private int port;


	public CommunicationConnection(int port) {
		
		this.port = port;
		
	}
        
        /**
         * Otworz socket
         * @return czy powiodlo sie
         */
	private boolean openSocket(){
		
		try {
			serverSocket = new ServerSocket(port, 5);
			return true;
		} catch (IOException e) {
			System.out.println("Could not listen on port: " + port);
			e.printStackTrace();
			return false;
		}
		
	}
	
        /**
         * Rozpocznij nasłuchiwanie
         * @see ftp.ConnectionHandler
         */
	public void startListening(){

		if( openSocket() ){

			incomingConnection = null;
			
			while(true){
				
				try {
					incomingConnection = serverSocket.accept();
					handleConnection(incomingConnection);
				} catch (BindException e) {
					System.out.println("Unable to bind to port " + port);
				} catch (IOException e) {
					System.out.println("Unable to instantiate a ServerSocket on port: " + port);
				}
			}
		}
		
	}
	
        /**
         * ThreadPool funkcja startująca watek
         * @param connectionToHandle polaczenie do ktorego uruchamiamy watek
         */
	public void handleConnection(Socket connectionToHandle){
		
			new Thread(
					new CommunicationConnectionHandler(connectionToHandle)
			).start();
	}
	
}
