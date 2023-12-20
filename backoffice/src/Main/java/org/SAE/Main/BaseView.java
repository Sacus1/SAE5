package org.SAE.Main;

import org.SAE.Depot.Depot;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * This abstract class represents a base view with a layout and some panels.
 * It extends JPanel, a generic lightweight container.
 */
public abstract class BaseView<T extends Base> extends JPanel {
	public final UButton createButton;
	public static boolean inCreation = false;
	protected static JPanel mainPanel;
	protected static JPanel topPanel;
	protected static JPanel bottomPanel;
	private String name;

	/**
	 * Constructor for BaseView.
	 * Sets up the layout and initializes the panels, and the create button.
	 */
	protected BaseView(String name) {
		this.name = name;
		createButton = new UButton("Create " + name);
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
		if (isCreateMode) {
			clear();
			JPanel formPanel = createFormPanel();
			if (formPanel != null) {
				mainPanel.add(formPanel);
			}
			refresh();
			createButton.setText("Cancel");
			inCreation = true;
			return;
		}
		clear();
		ArrayList<T> list = new ArrayList<>(GetList());
		for (T t : list) {
			JPanel listPanel = createListPanel(t);
			if (listPanel != null) {
				mainPanel.add(listPanel);
			}
		}
		refresh();
		createButton.setText("Create " + name);
		inCreation = false;
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
			t.loadFromDatabase();
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
	public void clear() {
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
	/**
	 * Creates and returns a panel for a field.
	 * @param fieldName The name of the field.
	 * @param isRequired Whether the field is required or not.
	 * @return JPanel for a field.
	 */
	protected JPanel createFieldPanel(String fieldName, boolean isRequired) {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(1, 2));
		panel.add(new Label(fieldName + (isRequired ? " *" : "")));
		panel.add(new TextField());
		return panel;
	}
}
