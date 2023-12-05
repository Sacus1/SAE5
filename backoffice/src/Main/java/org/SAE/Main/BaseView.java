package org.SAE.Main;

import javax.swing.*;
import java.awt.*;

/**
 * This abstract class represents a base view with a layout and some panels.
 * It extends JPanel, a generic lightweight container.
 */
public abstract class BaseView extends JPanel {
	public final JButton createButton = new JButton("Create");
	public static boolean inCreation = false;
	protected static JPanel mainPanel;
	protected static JPanel topPanel;
	protected static JPanel bottomPanel;

	/**
	 * Constructor for BaseView.
	 * Sets up the layout and initializes the panels, and the create button.
	 */
	protected BaseView() {
		setLayout(new BorderLayout());
		initializePanels();
		setupCreateButton();
	}

	/**
	 * This method initializes the panels used in the view.
	 */
	private static void initializePanels() {
		topPanel = new JPanel();
		bottomPanel = new JPanel();
		mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

	}

	/**
	 * This method sets up the create button and its action listener.
	 */
	private void setupCreateButton() {
		topPanel.add(createButton);
		createButton.addActionListener(e -> displayView(!inCreation));
	}

	/**
	 * Abstract method to be implemented by subclasses.
	 * It is triggered when the create button is clicked.
	 *
	 * @param isCreateMode A boolean indicating whether to create or not.
	 */
	public abstract void displayView(boolean isCreateMode);

	/**
	 * This method clears all components from the main panel.
	 */
	public static void clear() {
		mainPanel.removeAll();
	}

	/**
	 * This method refreshes the main panel by revalidating and repainting it.
	 */
	public static void refresh() {
		mainPanel.revalidate();
		mainPanel.repaint();
	}
}
