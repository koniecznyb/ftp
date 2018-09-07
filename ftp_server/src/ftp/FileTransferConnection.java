package ftp;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

/**
 * Połaczenie do przesylania danych.
 * @version 1.0
 * @author redi
 */
public class FileTransferConnection {

	// TODO change to private
	public Socket incomingConnection;
	public ServerSocket serverSocket;
	protected PrintWriter out;
	protected BufferedReader in;
	public int port;
	
	private void openStreams() throws IOException{
		out = new PrintWriter(incomingConnection.getOutputStream(), true);
		in = new BufferedReader(new InputStreamReader(incomingConnection.getInputStream()));;
	}
	private void closeStreams() throws IOException{
		if(out != null && in!= null){
			out.close();
			in.close();
		}
	}

	public FileTransferConnection(int port) {

		this.port = port;

	}

	private boolean openSocket() throws IOException {

		serverSocket = new ServerSocket(port, 5);
		return true;

	}
	
        /**
         * Wysyla liste plikow 
         * @param fileList lista do wyslania
         * @throws IOException gdy nie powiodlo sie otwieranie streamow
         * @see ftp.CommunicationConnectionHandler
         */
	public void sendFileList(List<String> fileList) throws IOException{
		
		openStreams();
		for(String s : fileList){
			out.write(s + "\n");
		}
		closeStreams();
		
	}
	
        /**
         * Wysyla zawrtosc plikow
         * @param file plik do wyslania
         * @throws IOException gdy nie powiodlo sie otwieranie streamow 
         * @see ftp.CommunicationConnectionHandler
         */
	public void sendFile(File file) throws IOException{
		
		BufferedInputStream in = new BufferedInputStream(new FileInputStream(file));
		BufferedOutputStream out = new BufferedOutputStream(incomingConnection.getOutputStream());
		
		send(out, in);
		
		out.close();
		in.close();
		
	}
        
        /**
         * Pobierz plik
         * @param file plik do pobrania
         * @throws IOException IOException gdy nie powiodlo sie otwieranie streamow 
         *  @see ftp.CommunicationConnectionHandler
         */
	
	public void retriveFile(File file) throws IOException{
		
		OutputStream out = new FileOutputStream(file.getAbsolutePath());
		InputStream fileIn = incomingConnection.getInputStream();
		
		copy(fileIn, out);
		
		out.close();
		fileIn.close();
		
	}
	private void copy(InputStream in, OutputStream out) throws IOException {
		byte[] buf = new byte[8192];
		int len = 0;
		while ((len = in.read(buf)) != -1) {
			out.write(buf, 0, len);
		}
	}
	
	private void send(BufferedOutputStream out, BufferedInputStream input) throws IOException {
		byte[] buffer = new byte[4096];
		int bytesRead = 0;
		while ((bytesRead = input.read(buffer)) != -1) {
			out.write(buffer, 0, bytesRead);
		}
	}
	
/**
 * Rozpocznij nasłuchiwanie
 * @throws IOException 
 */
	public void startListening() throws IOException {

		if (openSocket()) {

			incomingConnection = null;

			incomingConnection = serverSocket.accept();


		}

	}

}