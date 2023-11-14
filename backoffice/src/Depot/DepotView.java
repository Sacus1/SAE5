package Depot;

import Main.Logger;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Field;
public class DepotView extends JPanel {
	JButton createButton = new JButton("Create");
	boolean inCreation = false;
	boolean showArchived = false;
	static JPanel mainPanel,topPanel,bottomPanel;
	JCheckBox archivedCheckBox;
	public DepotView() {
		super();
		Depot.getFromDatabase();
		setLayout(new BorderLayout());
		// top panel
		topPanel = new JPanel();
		topPanel.add(createButton);
		archivedCheckBox = new JCheckBox("Show archived");
		archivedCheckBox.addActionListener(e -> {
			showArchived = !showArchived;
			draw(false);
		});
		topPanel.add(archivedCheckBox);
		// bottom panel
		bottomPanel = new JPanel();
		// a main panel (list of depots or create depot)
		mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		draw(false);
		// add panels to the frame
		add(topPanel, "North");
		add(mainPanel, "Center");
		add(bottomPanel, "South");
	}
	public static void clear() {
		mainPanel.removeAll();
	}
	public static void refresh() {
		mainPanel.revalidate();
		mainPanel.repaint();
	}
	public void draw(boolean isCreate) {
		if (!isCreate) {
			// show archived checkbox
			archivedCheckBox.setVisible(true);
			clear();
			for (Depot depot : Depot.depots) {
				if (depot.isArchived && !showArchived) continue;
				mainPanel.add(createListPanel(depot));
			}
			refresh();
			// rename cancel button to create
			createButton.setText("Create");
			inCreation = false;
		}
		else {
			// hide archived checkbox
			archivedCheckBox.setVisible(false);
			clear();
			mainPanel.add(createCreatePanel());
			refresh();
			// rename create button to cancel
			createButton.setText("Cancel");
			inCreation = true;
		}
	}

	private Panel createCreatePanel() {
		Panel panel = new Panel();
		Panel[] panels = new Panel[Depot.fields.length];
		for (int i = 0; i < Depot.fields.length; i++) {
			panels[i] = createField(Depot.fields[i], Depot.requiredFieldsList.contains(Depot.fields[i]));
			panel.add(panels[i]);
		}
		panel.setLayout(new GridLayout(panels.length/2 + 1, 2));

		JButton createButton = new JButton("Create");
		createButton.addActionListener(e -> create(panels));

		panel.add(createButton);
		return panel;
	}

	private Panel createListPanel(Depot depot) {
		Panel panel = new Panel();
		panel.setLayout(new GridLayout(1, 3));
		panel.add(new Label(depot.nom));
		JButton editButton = new JButton("Edit");
		editButton.addActionListener(e -> {
			clear();
			mainPanel.add(createEditPanel(depot));
			refresh();
		});
		JButton deleteButton = new JButton("Delete");
		deleteButton.addActionListener(e -> {
			depot.delete();
			draw(false);
		});
		JButton archiveButton = new JButton(depot.isArchived ? "Unarchive" : "Archive");
		archiveButton.addActionListener(e -> {
			depot.archive();
			draw(false);
		});
		panel.add(editButton);
		panel.add(deleteButton);
		panel.add(archiveButton);
		return panel;
	}
	private void create(Panel[] panels) {
		String[] values = new String[panels.length];
		for (int i = 0; i < panels.length; i++) {
			boolean isRequired = Depot.requiredFieldsList.contains(Depot.fields[i]);
			String textFieldValue = ((TextField) panels[i].getComponent(1)).getText();

			if (isRequired && textFieldValue.isEmpty()) {
				Logger.error("Please fill all required fields");
				return;
			}
			values[i] = textFieldValue;
			// replace empty strings with “NULL”
			if (values[i].isEmpty()) values[i] = "NULL";
		}
		Logger.log("Depot created");
		Depot.create(values);
	}

	private Panel createField(String name, boolean required) {
		Panel panel = new Panel();
		panel.setLayout(new GridLayout(1, 2));
		panel.add(new Label(name + (required ? " *" : "")));
		panel.add(new TextField());
		return panel;
	}

	private Panel createEditPanel(Depot depot) {
		Panel panel = new Panel();
		Panel[] panels = new Panel[Depot.fields.length];
		for (int i = 0; i < Depot.fields.length; i++) {
			panels[i] = createField(Depot.fields[i], Depot.requiredFieldsList.contains(Depot.fields[i]));
			Field field;
			try {
				field = depot.getClass().getDeclaredField(Depot.dbFields[i]);
			} catch (NoSuchFieldException e) {
				System.err.println("Depot don't have field " + Depot.dbFields[i]);
				throw new RuntimeException(e);
			}
			try {
				Object value = field.get(depot);
				if (value != null) ((TextField) panels[i].getComponent(1)).setText(value.toString());
			} catch (IllegalAccessException e) {
				System.err.println("Can't access field " + Depot.dbFields[i]);
				throw new RuntimeException(e);
			}
			panel.add(panels[i]);
		}
		panel.setLayout(new GridLayout(panels.length/2 + 1, 2));

		JButton createButton = new JButton("Create");
		createButton.addActionListener(e -> {
			String[] values = new String[panels.length];
			for (int i = 0; i < panels.length; i++) {
				String textFieldValue = ((TextField) panels[i].getComponent(1)).getText();
				values[i] = textFieldValue.isEmpty() ? null : textFieldValue;
			}
			Logger.log("Depot edited");
			depot.edit(values);
		});

		panel.add(createButton);
		return panel;
	}
}
