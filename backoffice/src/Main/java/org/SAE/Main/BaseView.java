package org.SAE.Main;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * This abstract class represents a base view with a layout and some panels.
 * It extends JPanel, a generic lightweight container.
 */
public abstract class BaseView<T extends Base> extends JPanel {
	public final UButton createButton = new UButton("Create");
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
	 * This method is used to display the view based on the mode.
	 * If the mode is not create mode, it will display a list of Base objects.
	 * If the mode is create mode, it will display a form for creating a new Base object.
	 * @param isCreateMode a boolean indicating whether the view is in create mode or not.
	 */
	public void displayView(boolean isCreateMode){
		if (!isCreateMode) {
			clear();
			ArrayList<T> list = GetList();
			for (T t : list) {
				mainPanel.add(createListPanel(t));
			}
			refresh();
			// rename cancel button to create
			createButton.setText("Create");
			inCreation = false;
		}
		else {
			clear();
			mainPanel.add(createFormPanel());
			refresh();
			// rename create button to cancel
			createButton.setText("Cancel");
			inCreation = true;
		}
	}

	protected abstract ArrayList<T> GetList();


	/**
	 * This method is used to create a list panel for a given Base object.
	 * The list panel contains a label displaying the Base object, an edit button, and a delete button.
	 * The edit button, when clicked, will display an edit panel for the Base object.
	 * The delete button, when clicked, will delete the Base object and refresh the view.
	 * @param t the Base object for which the list panel is created.
	 * @return the created list panel.
	 */
	protected JPanel createListPanel(T t){
		t.loadFromDatabase();
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(1, 3));
		JLabel label = new JLabel(t.toString());
		UButton editButton = new UButton("Edit");
		editButton.addActionListener(e -> {
			clear();
			mainPanel.add(createEditPanel(t));
			refresh();
		});
		UButton deleteButton = new UButton("Delete");
		deleteButton.addActionListener(e -> {
			t.delete();
			displayView(false);
		});
		panel.add(label);
		panel.add(editButton);
		panel.add(deleteButton);
		return panel;
	}

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
	protected abstract JPanel createFormPanel();
	protected abstract JPanel createEditPanel(T object);
}
