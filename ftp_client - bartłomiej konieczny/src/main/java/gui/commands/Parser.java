package gui.commands;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * Parser który przetwarza liste plikow na bardziej przyjazna uzytkownikowi
 * @author redi
 * @see gui.commands.MLSD
 * @version 1.0
 */
public class Parser {
	
	/**
	 * @uml.property  name="serverMessage"
	 */
	private StringBuilder serverMessage;
	
	public Parser(StringBuilder serverMessage){
		this.serverMessage = serverMessage;
	}
	
	
	public InetAddress parseDataConnectionAddress() throws UnknownHostException {

		final Pattern pattern = Pattern.compile("\\d+"); // the regex
		final Matcher matcher = pattern.matcher(serverMessage); // your string

		final ArrayList<Integer> ints = new ArrayList<Integer>(); // results

		while (matcher.find()) { // for each match
			ints.add(Integer.parseInt(matcher.group())); // convert to int
		}

		String address = ints.get(1) + "." + ints.get(2) + "." + ints.get(3) + "." + ints.get(4);
		InetAddress resultAddress = InetAddress.getByName(address);

		return resultAddress;
	}

	public int parseDataConnectionPort() throws UnknownHostException {

		final Pattern pattern = Pattern.compile("\\d+"); // the regex
		final Matcher matcher = pattern.matcher(serverMessage); // your string

		final ArrayList<Integer> ints = new ArrayList<Integer>(); // results

		while (matcher.find()) { // for each match
			ints.add(Integer.parseInt(matcher.group())); // convert to int
		}

		int resultPort = ints.get(5) * 256 + ints.get(6);

		return resultPort;
	}

}
