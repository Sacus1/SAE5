package org.SAE.Main;

import javax.swing.*;
/**
 * Logger class for displaying messages in a JFrame.
 */
public class Logger {
	private Logger() {}
	/**
	 * Logs a message with a specified color and duration.
	 *
	 * @param message The message to be displayed.
	 * @param color   The color of the message text.
	 * @param time    The duration for which the message is displayed (in seconds).
	 */
	public static void log(String message) {
		JOptionPane pane = new JOptionPane(message, JOptionPane.INFORMATION_MESSAGE);
		JDialog dialog = pane.createDialog(null, "Message");
		dialog.setModal(false);
		dialog.setVisible(true);
	}


	/**
	 * Logs a formatted message with default color and duration.
	 *
	 * @param message The message format string.
	 * @param args    The arguments for the format string.
	 */
	public static  void log(String message, Object... args) {
		log(String.format(message, args));
	}

	/**
	 * Logs an error message with red color.
	 *
	 * @param message The error message to be displayed.
	 */
	public static void error(String message) {
		JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
	}
}
