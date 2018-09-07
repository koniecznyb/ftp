package sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

/**
 * Klasa odpowiadajaca za polaczenie z serwerem sql.
 * @see sql.SQLException
 * @author redi
 * @version 1.0
 */
public class SQLConnection {
	
	private String address, username, password;
	private Connection connection = null;
	private Statement statement = null;
	private ResultSet resultset = null;
		
	public SQLConnection(String address, String username, String password) throws SQLException {
		this.address = address;
		this.username = username;
		this.password = password;
		
		
		////////////////////////////////////////to-do
//		this.address = "jdbc:mysql://mysql.agh.edu.pl:3306/redi";
//		this.username = "redi";
//		this.password = "WxP3abrB";
		
		connect();

	}
	
	public void addUserToGroup(String username, String groupname) throws SQLException{
		statement = connection.createStatement();
		
//		statement.executeUpdate("INSERT INTO usergroup (user_id, group_id) VALUES (SELECT id FROM users WHERE username='redi', SELECT id FROM groups WHERE group='admin' )");
	}
	
	public void createNewGroup(String groupname) throws SQLException{

		statement = connection.createStatement();
		
		statement.executeUpdate("INSERT INTO `groups` (`group`) VALUES ('" + groupname + "')" );
	}

	
	public boolean checkPassword(String username, String password) throws SQLException{
		
		statement = connection.createStatement();
		
		resultset = statement.executeQuery(
				"SELECT `password`=PASSWORD(CONCAT(PASSWORD('"+password+"'),`salt`)) FROM `users` WHERE `username`='"+username+"'"
		);
		
		while(resultset.next()){
			
			if(resultset.getString(1).compareTo("1") == 0)
				return true;
			else
				return false;
		}
		
		return false;
	}
	
	public Vector <String> listUsers() throws SQLException{
		
		Vector <String> userListVector  = new Vector<String>();

		statement = connection.createStatement();
		
		resultset = statement.executeQuery("SELECT username FROM users");
		
		while(resultset.next()){
			String name = resultset.getString(1);
			userListVector.add("Uzytkownik: " + name);
		}

		return userListVector;
		
	}
	
	public void addUser(String username, String password, String salt) throws SQLException{
		statement = connection.createStatement();
		
		
		statement.executeUpdate(
				
				"INSERT INTO `users`(`username`, `password`, `salt`) VALUES ('"+ username +"',PASSWORD(CONCAT(PASSWORD('" + password + "'),'"+salt+"')),'"+ salt +"')"				
				
		);
					
	}
	
	
	public void createTables() throws SQLException{
		
		statement = connection.createStatement();
		
		statement.executeUpdate(
			"CREATE TABLE IF NOT EXISTS `users` ( "+
			  "`id` int(1) NOT NULL AUTO_INCREMENT,"+
			  "`username` varchar(10) NOT NULL,"+
			  "`password` varchar(45) NOT NULL,"+
			  "`salt` varchar(20) NOT NULL,"+
			  "PRIMARY KEY  (`id`),"+
			  "UNIQUE KEY `username` (`username`)"+
			") ENGINE=MyISAM  DEFAULT CHARSET=latin2 AUTO_INCREMENT=3 ;"
		);
		
		statement.executeUpdate(	
			"CREATE TABLE IF NOT EXISTS `groups` ("+
			  "`id` int(1) NOT NULL AUTO_INCREMENT,"+
			  "`group` varchar(10) NOT NULL,"+
			  "PRIMARY KEY  (`id`),"+
			  "UNIQUE KEY `group` (`group`)"+
			") ENGINE=MyISAM  DEFAULT CHARSET=latin2 AUTO_INCREMENT=2 ;"
		);
		
		// dumping data for groups
		
		statement.executeUpdate(
			"INSERT INTO `groups` (`id`, `group`) VALUES"+
			"(1, 'admin');"
		);
		
		statement.executeUpdate(
			"CREATE TABLE IF NOT EXISTS `files` ("+
			  "`id` int(11) NOT NULL AUTO_INCREMENT,"+
			  "`filename` varchar(50) NOT NULL,"+
			  "`owner_id` int(11) NOT NULL,"+
			  "`group_id` int(11) NOT NULL,"+
			  "`user_read` tinyint(1) NOT NULL DEFAULT '1',"+
			  "`user_write` tinyint(1) NOT NULL DEFAULT '1',"+
			  "`group_read` tinyint(1) NOT NULL DEFAULT '0',"+
			  "`group_write` tinyint(1) NOT NULL DEFAULT '0',"+
			  "PRIMARY KEY  (`id`),"+
			  "UNIQUE KEY `filename` (`filename`)"+
			") ENGINE=MyISAM  DEFAULT CHARSET=latin2 AUTO_INCREMENT=2 ;"
		);
		
		statement.executeUpdate(
			"CREATE TABLE IF NOT EXISTS `usergroup` ("+
			"`user_id` int(11) NOT NULL,"+
			"`group_id` int(11) NOT NULL"+
			") ENGINE=MyISAM DEFAULT CHARSET=latin2;"				
		);

	}
	
	private void connect() throws SQLException{
		
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			
			connection = DriverManager.getConnection(this.address, this.username, this.password);
			
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		

	}
}
