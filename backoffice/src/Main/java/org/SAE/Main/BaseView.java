package org.SAE.Main;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * This abstract class represents a base view with a layout and some panels.
 * It extends JPanel, a generic lightweight container.
 */
public abstract class BaseView<T extends Base> extends JPanel {
	public final JButton createButton;
	protected static final int SCREEN_WIDTH = (int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth() * 0.8);
	static final int SCREEN_HEIGHT = (int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight() * 0.8);
	public static boolean inCreation = false;
	protected static JPanel mainPanel;
	protected static JPanel topPanel;
	protected static JPanel bottomPanel;
	private final String name;
	private JTextField searchBar;

	/**
	 * Constructor for BaseView.
	 * Sets up the layout and initializes the panels, and the create button.
	 */
	protected BaseView(String name) {
		this.name = name;
		createButton = new JButton("Create " + name);
		setLayout(new BorderLayout());
		initializePanels();
		setupCreateButton();
		searchBar = new JTextField();
		searchBar.setMaximumSize(new Dimension(1000, 30));
		searchBar.setPreferredSize(new Dimension(800, 30));
		searchBar.addActionListener(e -> search());
	}

	/**
	 * This method initializes the panels used in the view.
	 */
	private static void initializePanels() {
		topPanel = new JPanel();
		topPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
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
	 *
	 * @param isCreateMode a boolean indicating whether the view is in create mode or not.
	 */
	public void displayView(boolean isCreateMode) {
		if (isCreateMode) {
			clear();
			JPanel formPanel = createFormPanel();
			if (formPanel == null) {
				displayView(false);
				return;
			}
			mainPanel.add(formPanel);
			refresh();
			createButton.setText("Annuler");
			// hide search bar
			topPanel.remove(searchBar);
			inCreation = true;
			return;
		}
		topPanel.add(searchBar);
		searchBar.setText("");
		search();
		createButton.setText("Créer " + name);
		inCreation = false;
		refresh();
	}

	private void search() {
		ArrayList<T> list = new ArrayList<>(getList());
		ArrayList<T> filteredList = new ArrayList<>();
		for (T t : list) {
			if (t.toString().toLowerCase().contains(searchBar.getText().toLowerCase())) {
				filteredList.add(t);
			}
		}
		clear();
		JPanel listPanel = new JPanel();
		listPanel.setLayout(new GridLayout(0, 1));
		JScrollPane scrollPane = new JScrollPane(listPanel);
		scrollPane.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
		mainPanel.add(scrollPane);
		for (T t : filteredList) {
			JPanel p = createListPanel(t);
			if (p != null) {
				listPanel.add(p);
			}
		}
		refresh();
	}

	protected abstract ArrayList<T> getList();


	/**
	 * This method is used to create a list panel for a given Base object.
	 * The list panel contains a label displaying the Base object, an edit button, and a delete button.
	 * The edit button, when clicked, will display an edit panel for the Base object.
	 * The delete button, when clicked, will delete the Base object and refresh the view.
	 *
	 * @param t the Base object for which the list panel is created.
	 * @return the created list panel.
	 */
	protected JPanel createListPanel(T t) {
		JPanel panel = new JPanel();
		panel.setMaximumSize(new Dimension(SCREEN_WIDTH-10, 30));
		panel.setPreferredSize(new Dimension(SCREEN_WIDTH-10, 30));
		panel.setLayout(new GridLayout(1, 2));
		JLabel label = new JLabel("<html>" + t.toString().replace("<", "&lt;").replace(">", "&gt;").replace("\n"
						, "<br/>") + "</html>");
		JButton editButton = new JButton("Détailler");
		editButton.addActionListener(e -> {
			displayView(true);
			clear();
			mainPanel.add(createDetailPanel(t));
			refresh();
		});
		JButton deleteButton = new JButton("Supprimer");
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

	protected abstract JPanel createDetailPanel(T object);


}
