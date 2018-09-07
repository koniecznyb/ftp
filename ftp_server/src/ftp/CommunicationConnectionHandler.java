package ftp;
import main.java.ftp.FileTransferConnection;
import main.java.ftp.ConnectionHandler;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import sql.SQLConnection;

public class CommunicationConnectionHandler extends ConnectionHandler {

	private SQLConnection connection;
	private FileTransferConnection fileTransferConnection;

	private String currentPath = "files";
	private String previousPath = "files";
	private File dir;

	// this.address = ;
	// this.username = "redi";
	// this.password = "WxP3abrB";

        /**
         * @inheritDoc
         * @param socketToHandle 
         */
	public CommunicationConnectionHandler(Socket socketToHandle) {
		super(socketToHandle);
		try {
			connection = new SQLConnection("jdbc:mysql://mysql.agh.edu.pl:3306/redi", "redi", "WxP3abrB");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

        /**
         * @inheritDoc
         * @throws IOException 
         */
	@Override
	protected void handleConnection() throws IOException{
		
		String line1 = "";
		String line2 = "";
		String line3 = "";
		String line4 = "";
		
 		this.sendMessage("220 FTP Server ready!");
 				
 		line1 = readMessage();
		if( (line1.substring(0, 4)).compareTo("USER") == 0 ){
			this.sendMessage("331 Password required for " + line1.substring(5));
			
			line2 = readMessage();
			if( (line2.substring(0, 4)).compareTo("PASS") == 0 ){

				if ( isCorrectLoginPassword(line1.substring(5), line2.substring(5)) ){
					
					this.sendMessage("230 User logged in");
					
					do{
						String command = "";
						if( (command = readMessage()) == null)
							break;

						String[] splitMessage = command.toString().split("\\s+");

						String message = splitMessage[0];
						String filename = new String("");

						for (int i = 1; i < splitMessage.length; i++) {
							if (i == 1)
								filename = splitMessage[i];
							else
								filename = filename + " " + splitMessage[i];
						}

						String chmodPermissions = "";
						if (splitMessage.length == 3) {
							chmodPermissions = splitMessage[2];
						}
						
						
						
						switch(message){
							case "TYPE I":{
								this.sendMessage("200 Type set to I");
								break;
							}
							case "PASV":{
								Random r = new Random();
								int randomInt = r.nextInt(500) + 1;
								
								int port = 4 * 256 + randomInt;
								
								fileTransferConnection = new FileTransferConnection(port);
				
								this.sendMessage( "227 Entering Passive Mode (127,0,0,1,4,"+randomInt+")");
								fileTransferConnection.startListening();
								break;
							}
							case "PWD":{
								this.sendMessage("257 " + currentPath + " is current directory");
								break;
							}
							case "ABOR":{
								
							}
							case "QUIT": {
								this.sendMessage("221 Bye");
								break;
							}
							case "MLSD":{
								
								if(fileTransferConnection == null){
									
									this.sendMessage("503 Bad sequence of commands.");
								}
								else{
									
									MLSD();
									fileTransferConnection.incomingConnection = null;
								}
								break;
							}
							default:{
								if( (message + " " + filename).compareTo("CWD ..") == 0){
									currentPath = previousPath;
									this.sendMessage("250 CWD successful. " + currentPath + " is current directory.");
								}
								else if (message.matches("CWD.*")) {
									File file = new File(currentPath + "\\" + filename);
									previousPath = currentPath;
									if(file.exists()){
										currentPath = currentPath + "\\" + filename;
										this.sendMessage("250 CWD successful. " + currentPath + " is current directory.");
									}
									else{
										this.sendMessage("550 CWD failed "+ filename +" directory not found.");
									}
								}
								
								 else if (message.matches("RETR.*")) {

									if(fileTransferConnection.incomingConnection == null){
										this.sendMessage("503 Bad sequence of commands.");
									}
									else{
										File file = new File(currentPath + "\\" +  filename);
										if(file.exists()){
											RETR(file);
										}
										else{
											this.sendMessage("550 File not found.");
										}
										
										fileTransferConnection.incomingConnection = null;
									}

								} 
								else if (message.matches("STOR.*")) {
									
									if(fileTransferConnection.incomingConnection == null){
										this.sendMessage("503 Bad sequence of commands.");
									}
									else{
										File file = new File(currentPath + "\\" +  filename);
										STOR(file);
										
										fileTransferConnection.incomingConnection = null;
									}
								} 
//								else if (message.matches("CHMOD.*")) {
//									this.sendMessage("503 Bad sequence of commands.");
//
//								} 
//								else if (message.matches("APPE.*")) {
//									
//									if(fileTransferConnection.incomingConnection == null){
//										this.sendMessage("503 Bad sequence of commands.");
//									}
//									else{
//										File file = new File(currentPath + "\\" +  filename);
//										STOR(file);
//										
//										fileTransferConnection.incomingConnection = null;
//									}
//
//								} 
								
								else if (message.matches("DELE.*")) {
									File file = new File(currentPath + "\\" +  filename);
									if(file.exists()){
										if(file.delete())
											this.sendMessage("250 File deleted successfully");
										else
											this.sendMessage("Could not delete a file");
										
									}
									else{
										this.sendMessage("550 File not found");
									}

								} 
								else if (message.matches("MKD.*")) {
									
									File file = new File(currentPath + "\\" +  filename);
									if(file.exists()){
										this.sendMessage("550 Already exists");										
									}
									else{
										if(file.mkdir())
											this.sendMessage("257 "+ filename + " created successfully");
										else
											this.sendMessage("550 Could not create a directory");
									}
									
								}
								else if (message.matches("RMD.*")) {
									
									File file = new File(currentPath + "\\" +  filename);
									
									if(file.exists()){
										if(file.isDirectory()){
											
											if(file.delete()){
												this.sendMessage("257 "+ filename + " deleted successfully");
											}
											else{
												this.sendMessage("550 Could not delete a directory");
											}
											
										}
										else{
											this.sendMessage("550 " + filename + " is not a directory");
										}
									}
									else{

											this.sendMessage("550 " + filename + " does not exist");
									}

								}
								else{
									this.sendMessage("500 Syntax error, command unrecognized.");
								}
								
							}
						}
					}
					while( line1.compareTo("QUIT") != 0 );
					this.sendMessage("221 Bye");
				}
				else{
					//LOGIN failed
					this.sendMessage("530 Login incorrect.");
				}	
			}
			else{
				//PASS expected
				this.sendMessage("503 Bad sequence of commands.");	
			}
		}
		else{
			//USER expected
			this.sendMessage("503 Bad sequence of commands.");	
		}
		
	}

        /**
         * Sprawdza czy plik jest katalogiem czy plikiem
         * @param file podany plik
         * @return string file lub dir
         */
	private String checkIfIsAFile(File file) {

		if (file.isFile())
			return "file";
		else
			return "dir";
	}

        /**
         * Funkcja LIST
         * @return Lista plikow
         * @throws IOException gdy nie udało sie wysłac listy
         * @see FileTransferConnection
         */
	private List<String> MLSD() throws IOException {

		dir = new File(currentPath);
		List<String> fileList = new ArrayList<String>();
		for (File s : dir.listFiles()) {
			fileList.add("type=" + checkIfIsAFile(s) + ";modify=" + s.lastModified() + "; " + s.getName());
		}

		this.sendMessage("150 Opening data channel for directory listing of " + currentPath);
		fileTransferConnection.sendFileList(fileList);
		this.sendMessage("226 Successfully transferred " + currentPath);

		return null;
	}

        /**
         * Sprawdza czy podano prawidlowa nazwe uzytkownika i haslo
         * @param username nazwa
         * @param password haslo
         * @return true gdy sie udalo
         * @see sql.SQLConnection
         */
	private boolean isCorrectLoginPassword(String username, String password) {

		try {
			return connection.checkPassword(username, password);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return false;

	}

        /**
         * Odbierz plik
         * @param file podany plik
         * @throws IOException gdy nie udalo sie otworzyc pliku
         */
	public void RETR(File file) throws IOException {
		this.sendMessage("150 Opening data channel for file download from server of " + file.getName());
		fileTransferConnection.sendFile(file);
		this.sendMessage("226 Successfully transferred " + file.getName());
	}
	
        /**
         * Dodaj plik
         * @param file plik do dodania
         * @throws IOException gdy nie udalo sie otworzyc pliku
         */
	public void STOR(File file) throws IOException{
	
		this.sendMessage("150 Opening data channel for file upload to server of " + file.getName());
		fileTransferConnection.retriveFile(file);
		this.sendMessage("226 Successfully transferred " + file.getName());
	
	}
	

}
