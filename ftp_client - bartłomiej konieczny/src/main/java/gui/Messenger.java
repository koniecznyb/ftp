package gui;

import java.awt.Color;

import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

/**
 * Klasa odpowiadajaca za wyswietlanie wiadomosci z serwera i z uzytkownika na panelu graficznym.
 * @author redi
 * @see gui.mainWindow
 * @version 1.0
 */
public class Messenger {
	
	/**
	 * @uml.property  name="doc"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private StyledDocument doc;
	
	public Messenger(StyledDocument doc){
		this.doc = doc;
	}

	/**
         * Dodaj wiadomosc od uzytkownika
         * @param message nazwa wiadomosci
         */
	public void addAnUserMessage(StringBuilder message) {

		SimpleAttributeSet keyWord = new SimpleAttributeSet();
		StyleConstants.setForeground(keyWord, Color.BLUE);
		StyleConstants.setBold(keyWord, true);

		try {
			doc.insertString(doc.getLength(), message.toString() + "\n", keyWord);
		} catch (Exception e) {
			System.out.println(e);
		}

	}
	
        /**
         * Dodaj komende FTP
         * @param message tresc
         */
	public void addAnFtpCommandMessage(StringBuilder message) {

		SimpleAttributeSet keyWord = new SimpleAttributeSet();
		StyleConstants.setForeground(keyWord, Color.RED);
		StyleConstants.setBold(keyWord, true);

		try {
			doc.insertString(doc.getLength(), message.toString() + "\n", keyWord);
		} catch (Exception e) {
			System.out.println(e);
		}

	}
	
        /**
         * Dodaj wiadomosc od serwera
         * @param message wiadomosc
         * @param atTheBeginning czy wiadomosc bedzie rozpoczeciem komunikacji z serwerem?
         */
	public void addAnServerMessage(StringBuilder message, boolean atTheBeginning) {

		SimpleAttributeSet keyWord = new SimpleAttributeSet();
		// StyleConstants.setBold(keyWord, true);

		try {
			if (atTheBeginning)
				doc.insertString(0, "Connected!\n", null);
			else
				doc.insertString(doc.getLength(), message.toString(), keyWord);
		} catch (Exception e) {
			System.out.println(e);
		}

	}
}
