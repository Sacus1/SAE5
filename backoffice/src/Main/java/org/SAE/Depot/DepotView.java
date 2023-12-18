package org.SAE.Depot;

import org.SAE.Adresse.Adresse;
import org.SAE.Error.CannotAccessFieldException;
import org.SAE.Main.BaseView;
import org.SAE.Main.Logger;
import org.SAE.Referent.Referent;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;
/**
 * This class represents the view for the Depot module.
 * It extends the BaseView class and provides the UI for managing depots.
 */
public class DepotView extends BaseView {
	boolean showArchived = false;
	final JCheckBox archivedCheckBox;
	/**
	 * Constructor for DepotView.
	 * Initializes the UI components and layout.
	 */
	public DepotView() {
		super();
		Depot.getFromDatabase();
		setLayout(new BorderLayout());
		archivedCheckBox = new JCheckBox("Show archived");
		archivedCheckBox.addActionListener(e -> {
			showArchived = !showArchived;
			displayView(false);
		});
		topPanel.add(archivedCheckBox);
		// add panels to the frame
		add(topPanel, "North");
		add(mainPanel, "Center");
		add(bottomPanel, "South");
		displayView(false);
	}

	/**
	 * Draws the view based on the isCreate flag.
	 * If isCreate is false, it shows the list of depots.
	 * If isCreate is true, it shows the form to create a new depot.
	 *
	 * @param isCreateMode Flag to determine whether to show the create form or the list of depots.
	 */
	public void displayView(boolean isCreateMode) {
		if (!isCreateMode) {
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

	/**
	 * Creates and returns a panel for creating a new depot.
	 * @return Panel for creating a new depot.
	 */
	private Panel createCreatePanel() {
		Panel panel = new Panel();
		Panel[] panels = new Panel[Depot.fields.length+2];
		Adresse.getFromDatabase();
		Referent.getFromDatabase();
		// adresse choice
		panels[0] = new Panel();
		panels[0].setLayout(new GridLayout(1, 2));
		panels[0].add(new Label("Adresse *"));
		Choice adresseChoice = new Choice();
		for (int i = 0; i < Adresse.adresses.size(); i++) adresseChoice.add(Adresse.adresses.get(i).toString());
		panels[0].add(adresseChoice);
		panel.add(panels[0]);
		// referent choice
		panels[1] = new Panel();
		panels[1].setLayout(new GridLayout(1, 2));
		panels[1].add(new Label("Referent *"));
		Choice referentChoice = new Choice();
		for (int i = 0; i < Referent.referents.size(); i++) referentChoice.add(Referent.referents.get(i).toString());
		panels[1].add(referentChoice);
		panel.add(panels[1]);
		for (int i = 2; i < Depot.fields.length; i++) {
			panels[i] = createFieldPanel(Depot.fields[i], Depot.requiredFieldsList.contains(Depot.fields[i]));
			panel.add(panels[i]);
		}
		panels[Depot.fields.length] = new Panel();
		panels[Depot.fields.length].setLayout(new GridLayout(1, 2));
		panels[Depot.fields.length].add(new Label("Jour de livraison"));
		// Create components
		ArrayList<JourSemaine> joursLivraisons = createDeliveryDaysPanel(panels, panel, null);
		// add image chooser
		AtomicReference<File> image = createImageChooserPanel(panels, panel);
		panel.setLayout(new GridLayout(panels.length/2 + 2, 2));
		Button createButton = new Button("Create");
		createButton.addActionListener(e -> createDepot(panels,joursLivraisons, image.get()));
		panel.add(createButton);
		return panel;
	}

	/**
	 * Creates and returns a panel for listing a depot.
	 * @param depot The depot to be listed.
	 * @return Panel for listing a depot.
	 */
	private Panel createListPanel(Depot depot) {
		Panel panel = new Panel();
		panel.setLayout(new GridLayout(1, 3));
		// add image if exists
		if (depot.image != null) {
			Panel panel2 = new Panel();
			panel2.setLayout(new GridLayout(2, 1));
			panel2.add(new JLabel(depot.nom, SwingConstants.CENTER));
			panel2.add(new JLabel(new ImageIcon(depot.image.getPath())));
			panel.add(panel2);
		} else panel.add(new JLabel(depot.nom, SwingConstants.CENTER));
		Button editButton = new Button("Edit");
		editButton.addActionListener(e -> {
			clear();
			mainPanel.add(createEditPanel(depot));
			refresh();
		});
		Button deleteButton = new Button("Delete");
		deleteButton.addActionListener(e -> {
			depot.delete();
			displayView(false);
		});
		Button archiveButton = new Button(depot.isArchived ? "Unarchive" : "Archive");
		archiveButton.addActionListener(e -> {
			depot.archive();
			displayView(false);
		});
		panel.add(editButton);
		panel.add(archiveButton);
		panel.add(deleteButton);
		return panel;
	}

	/**
	 * Creates a new depot based on the data entered in the form.
	 *
	 * @param fieldPanels  The panels containing the data.
	 * @param deliveryDays The days of delivery.
	 * @param depotImage   The image of the depot.
	 */
	private void createDepot(Panel[] fieldPanels, ArrayList<JourSemaine> deliveryDays, File depotImage) {
		String[] values = new String[fieldPanels.length];
		for (int i = 2; i < Depot.fields.length; i++) {
			boolean isRequired = Depot.requiredFieldsList.contains(Depot.fields[i]);
			String textFieldValue = ((TextField) fieldPanels[i].getComponent(1)).getText();

			if (isRequired && textFieldValue.isEmpty()) {
				Logger.error("Please fill all required fields");
				return;
			}
			values[i] = textFieldValue;
			// replace empty strings with “NULL”
			if (values[i].isEmpty()) values[i] = "NULL";
		}
		// adresse
		values[0] = Integer.toString(Adresse.adresses.get(((Choice) fieldPanels[0].getComponent(1)).getSelectedIndex()).id);
		// referent
		values[1] = Integer.toString(Referent.referents.get(((Choice) fieldPanels[1].getComponent(1)).getSelectedIndex()).id);
		Depot depot = new Depot(Integer.parseInt(values[0]), Integer.parseInt(values[1]), values[2], values[3], values[4],
						values[5], values[6], values[7], depotImage);
		depot.jourLivraison = deliveryDays.toArray(new JourSemaine[0]);
		Depot.create(depot);
		displayView(false);
	}

	/**
	 * Creates and returns a panel for a field.
	 * @param fieldName The name of the field.
	 * @param isRequired Whether the field is required or not.
	 * @return Panel for a field.
	 */
	private Panel createFieldPanel(String fieldName, boolean isRequired) {
		Panel panel = new Panel();
		panel.setLayout(new GridLayout(1, 2));
		panel.add(new Label(fieldName + (isRequired ? " *" : "")));
		panel.add(new TextField());
		return panel;
	}

	/**
	 * Creates and returns a panel for editing a depot.
	 * @param depotToEdit The depot to be edited.
	 * @return Panel for editing a depot.
	 */
	private Panel createEditPanel(Depot depotToEdit) {
		Panel panel = new Panel();
		Panel[] panels = new Panel[Depot.fields.length+2];
		// adresse choice
		panels[0] = new Panel();
		panels[0].setLayout(new GridLayout(1, 2));
		panels[0].add(new Label("Adresse *"));
		panels[0].add(createAddressChoiceComponent(depotToEdit));
		panel.add(panels[0]);
		// referent choice
		panels[1] = new Panel();
		panels[1].setLayout(new GridLayout(1, 2));
		panels[1].add(new Label("Referent *"));
		panels[1].add(createReferentChoiceComponent(depotToEdit));
		panel.add(panels[1]);
		populateFields(depotToEdit, panels, panel);
		// livraison
		ArrayList<JourSemaine> joursLivraisons = createDeliveryDaysPanel(panels, panel, depotToEdit);
		// add image chooser
		AtomicReference<File> image = createImageChooserPanel(panels, panel);
		Button createButton = new Button("Create");
		createButton.addActionListener(e -> {
			String[] values = new String[panels.length];
			for (int i = 2; i < panels.length; i++) {
				String textFieldValue = ((TextField) panels[i].getComponent(1)).getText();
				values[i] = textFieldValue.isEmpty() ? null : textFieldValue;
			}
			// adresse
			values[0] = Integer.toString(Adresse.adresses.get(((Choice) panels[0].getComponent(1)).getSelectedIndex()).id);
			// referent
			values[1] = Integer.toString(Referent.referents.get(((Choice) panels[1].getComponent(1)).getSelectedIndex()).id);
			depotToEdit.adresseIdAdresse = Integer.parseInt(values[0]);
			depotToEdit.referentIdReferent = Integer.parseInt(values[1]);
			depotToEdit.nom = values[2];
			depotToEdit.telephone = values[3];
			depotToEdit.presentation = values[4];
			depotToEdit.commentaire = values[5];
			depotToEdit.mail = values[6];
			depotToEdit.website = values[7];
			depotToEdit.image = image.get();
			depotToEdit.jourLivraison = joursLivraisons.toArray(new JourSemaine[0]);
			Depot.update(depotToEdit);
			Logger.log("Depot edited");

		});

		panel.add(createButton);
		return panel;
	}

	/**
	 * This function is used to get an image file from the user. It creates a panel with a button that opens a file chooser when clicked.
	 * The selected file is stored in an AtomicReference<File> which is returned by the function.
	 * @param fieldPanels The panels containing the data.
	 * @param parentPanel The panel to which the image chooser is added.
	 * @return An AtomicReference<File> containing the selected image file.
	 */
	private AtomicReference<File> createImageChooserPanel(Panel[] fieldPanels, Panel parentPanel) {
		fieldPanels[Depot.fields.length+1] = new Panel();
		fieldPanels[Depot.fields.length+1].setLayout(new GridLayout(1, 2));
		fieldPanels[Depot.fields.length+1].add(new Label("Image"));
		Button imageButton = new Button("Select");
		AtomicReference<File> image = new AtomicReference<>();
		imageButton.addActionListener(e -> {
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
			int result = fileChooser.showOpenDialog(this);
			if (result == JFileChooser.APPROVE_OPTION) image.set(fileChooser.getSelectedFile());
		});
		parentPanel.add(fieldPanels[Depot.fields.length]);
		parentPanel.setLayout(new GridLayout(fieldPanels.length/2 + 1, 2));
		parentPanel.add(imageButton);
		return image;

	}

	/**
	 * This function is used to create a panel for selecting delivery days. It creates a panel with a button that opens a popup menu with a list of days when clicked.
	 * The selected days are stored in an ArrayList<JourSemaine> which is returned by the function.
	 *
	 * @param fieldPanels The panels containing the data.
	 * @param parentPanel The panel to which the delivery days chooser is added.
	 * @param depotToEdit The depot to be edited.
	 * @return An ArrayList<JourSemaine> containing the selected delivery days.
	 */
	private ArrayList<JourSemaine> createDeliveryDaysPanel(Panel[] fieldPanels, Panel parentPanel, Depot depotToEdit) {
		fieldPanels[Depot.fields.length] = new Panel();
		fieldPanels[Depot.fields.length].setLayout(new GridLayout(1, 2));
		fieldPanels[Depot.fields.length].add(new Label("Jour de livraison"));
		// Create components
		Button button = new Button("Select");
		JPopupMenu popupMenu = new JPopupMenu();
		ArrayList<JourSemaine> joursLivraisons = new ArrayList<>();
		JList<String> list = new JList<>(new String[]{"Lundi", "Mardi", "Mercredi", "Jeudi", "Vendredi", "Samedi", "Dimanche"});
		// Set list to multiple interval selection
		list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		// select joursLivraisons if editing
		if (depotToEdit != null)
			for (int i = 0; i < depotToEdit.jourLivraison.length; i++)
				list.setSelectedIndex(depotToEdit.jourLivraison[i].ordinal());
		// Add list to popup
		popupMenu.add(new JScrollPane(list));
		// Add action listener to button to show popup
		button.addActionListener(e -> popupMenu.show(button, 0, button.getHeight()));
		list.addListSelectionListener(e -> {
			if (!e.getValueIsAdjusting()) {
				joursLivraisons.clear();
				for (int i : list.getSelectedIndices()) joursLivraisons.add(JourSemaine.values()[i]);
			}
		});
		fieldPanels[Depot.fields.length].add(button);
		parentPanel.add(fieldPanels[Depot.fields.length]);
		return joursLivraisons;
	}

	/**
	 * This function is used to populate the fields of a depot object. It creates a panel for each field and sets the text of the text field to the value of the corresponding field in the depot object.
	 * @param depot The depot to be edited.
	 * @param fieldPanels The panels containing the data.
	 * @param parentPanel The panel to which the fields are added.
	 */
	private void populateFields(Depot depot, Panel[] fieldPanels, Panel parentPanel) {
		for (int i = 2; i < Depot.fields.length; i++) {
	    fieldPanels[i] = createFieldPanel(Depot.fields[i], Depot.requiredFieldsList.contains(Depot.fields[i]));
	    try {
	        Field field = depot.getClass().getDeclaredField(Depot.dbFields[i]);
	        Object value = field.get(depot);
	        if (value != null) ((TextField) fieldPanels[i].getComponent(1)).setText(value.toString());
	    } catch (NoSuchFieldException | IllegalAccessException e) {
	        throw new CannotAccessFieldException("Can't access field" + Depot.dbFields[i]);
	    }
	    parentPanel.add(fieldPanels[i]);
		}
	}

	/**
	 * This function is used to create a choice component for selecting a referent. It adds all referents to the choice component and selects the referent of the depot object.
	 * @param depot The depot to be edited.
	 * @return A Choice component with all referents and the referent of the depot selected.
	 */
	private static Choice createReferentChoiceComponent(Depot depot) {
		Choice referentChoice = new Choice();
		for (int i = 0; i < Referent.referents.size(); i++) referentChoice.add(Referent.referents.get(i).toString());
		// select referent
		for (int i = 0; i < Referent.referents.size(); i++)
			if (Referent.referents.get(i).id == depot.referentIdReferent) {
				referentChoice.select(i);
				break;
			}
		return referentChoice;
	}

	/**
	 * This function is used to create a choice component for selecting an address. It adds all addresses to the choice component and selects the address of the depot object.
	 * @param depot The depot to be edited.
	 * @return A Choice component with all addresses, and the address of the depot selected.
	 */
	private static Choice createAddressChoiceComponent(Depot depot) {
		Choice adresseChoice = new Choice();
		for (int i = 0; i < Adresse.adresses.size(); i++) adresseChoice.add(Adresse.adresses.get(i).toString());
		// select adresse
		for (int i = 0; i < Adresse.adresses.size(); i++)
			if (Adresse.adresses.get(i).id == depot.adresseIdAdresse) {
				adresseChoice.select(i);
				break;
			}
		return adresseChoice;
	}
}
