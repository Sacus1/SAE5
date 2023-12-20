package org.SAE.Depot;

import org.SAE.Adresse.Adresse;
import org.SAE.Error.CannotAccessFieldException;
import org.SAE.Main.BaseView;
import org.SAE.Main.Logger;
import org.SAE.Main.UButton;
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
public class DepotView extends BaseView<Depot> {
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

	@Override
	protected ArrayList<Depot> GetList() {
		ArrayList<Depot> list = new ArrayList<>();
		for (Depot depot : Depot.depots) {
			if (showArchived || !depot.isArchived) list.add(depot);
		}
		return list;
	}


	@Override
	protected JPanel createListPanel(Depot t) {
		Depot.getFromDatabase();
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(1, 3));
		panel.add(new Label(t.toString()));
		UButton editButton = new UButton("Edit");
		editButton.addActionListener(e -> {
			clear();
			mainPanel.add(createEditPanel(t));
			refresh();
		});
		UButton deleteButton = new UButton("Delete");
		UButton archiveButton = new UButton(t.isArchived ? "Unarchive" : "Archive");
		deleteButton.addActionListener(e -> {
			t.delete();
			Depot.getFromDatabase();
			displayView(false);
		});
		archiveButton.addActionListener(e -> {
			t.isArchived = !t.isArchived;
			Depot.update(t);
			Depot.getFromDatabase();
			displayView(false);
		});

		panel.add(editButton);
		panel.add(deleteButton);
		panel.add(archiveButton);
		return panel;
	}
	/**
	 * Creates and returns a panel for creating a new depot.
	 *
	 * @return JPanel for creating a new depot.
	 */
	@Override
	protected JPanel createFormPanel() {
		DepotFormComponents depotFormComponents = prepareDepotPanelData(null);
		for (int i = 0; i < Depot.fields.length; i++) {
			depotFormComponents.fieldPanels[i] = createFieldPanel(Depot.fields[i],
							Depot.requiredFieldsList.contains(Depot.fields[i]));
			depotFormComponents.panel.add(depotFormComponents.fieldPanels[i]);
		}
		depotFormComponents.panel.setLayout(new GridLayout(depotFormComponents.fieldPanels.length / 2 + 2, 2));
		UButton createButton = new UButton("Create");
		createButton.addActionListener(e -> createDepot(depotFormComponents.fieldPanels, depotFormComponents.joursLivraisons, depotFormComponents.image.get(),
						depotFormComponents.addressChoice, depotFormComponents.referentChoice));
		depotFormComponents.panel.add(createButton);
		return depotFormComponents.panel();
	}


	/**
	 * Creates a new depot based on the data entered in the form.
	 *
	 * @param fieldPanels  The panels containing the data.
	 * @param deliveryDays The days of delivery.
	 * @param depotImage   The image of the depot.
	 */
	private void createDepot(JPanel[] fieldPanels, ArrayList<JourSemaine> deliveryDays, File depotImage, JPanel adresseChoice, JPanel referentChoice) {
		String[] values = getValues(fieldPanels, adresseChoice, referentChoice);
		if (values.length == 0) return;
		if (deliveryDays.isEmpty()) {
			Logger.error("No delivery day selected");
			return;
		}
		Depot depot = new Depot(Integer.parseInt(values[0]), Integer.parseInt(values[1]), values[2], values[3], values[4],
						values[5], values[6], values[7], depotImage);
		depot.jourLivraison = deliveryDays.toArray(new JourSemaine[0]);
		Depot.create(depot);
		displayView(false);
	}


	/**
	 * Creates and returns a panel for editing a depot.
	 *
	 * @param depotToEdit The depot to be edited.
	 * @return JPanel for editing a depot.
	 */
	@Override
	protected JPanel createEditPanel(Depot depotToEdit) {
		DepotFormComponents depotFormComponents = prepareDepotPanelData(depotToEdit);
		// add fields to the panel
		populateFields(depotToEdit, depotFormComponents.fieldPanels, depotFormComponents.panel);
		depotFormComponents.panel.setLayout(new GridLayout(depotFormComponents.fieldPanels.length / 2 + 1, 2));
		UButton createButton = new UButton("Edit");
		UButton cancelButton = new UButton("Cancel");
		// add cancel button
		cancelButton.addActionListener(e -> displayView(false));
		createButton.addActionListener(e -> {
			String[] values = getValues(depotFormComponents.fieldPanels(), depotFormComponents.addressChoice(), depotFormComponents.referentChoice());
			if (values.length == 0) return;
			depotToEdit.jourLivraison = depotFormComponents.joursLivraisons().toArray(new JourSemaine[0]);
			if (depotToEdit.jourLivraison.length == 0) {
				Logger.error("No delivery day selected");
				return;
			}
			depotToEdit.adresseIdAdresse = Integer.parseInt(values[0]);
			depotToEdit.referentIdReferent = Integer.parseInt(values[1]);
			depotToEdit.nom = values[2];
			depotToEdit.telephone = values[3];
			depotToEdit.presentation = values[4];
			depotToEdit.commentaire = values[5];
			depotToEdit.mail = values[6];
			depotToEdit.website = values[7];
			depotToEdit.image = depotFormComponents.image().get();
			depotToEdit.jourLivraison = depotFormComponents.joursLivraisons().toArray(new JourSemaine[0]);
			Depot.update(depotToEdit);
			Logger.log("Depot edited");
			displayView(false);
		});
		depotFormComponents.panel().add(cancelButton);
		depotFormComponents.panel().add(createButton);
		return depotFormComponents.panel();
	}

	private DepotFormComponents prepareDepotPanelData(Depot depotToEdit) {
		Adresse.getFromDatabase();
		Referent.getFromDatabase();
		JPanel panel = new JPanel();
		// adresse choice
		JPanel addressChoice = new JPanel();
		addressChoice.setLayout(new GridLayout(1, 2));
		addressChoice.add(new Label("Adresse *"));
		addressChoice.add(createAddressChoiceComponent(depotToEdit));
		panel.add(addressChoice);
		// referent choice
		JPanel referentChoice = new JPanel();
		referentChoice.setLayout(new GridLayout(1, 2));
		referentChoice.add(new Label("Referent *"));
		referentChoice.add(createReferentChoiceComponent(depotToEdit));
		panel.add(referentChoice);
		JPanel[] fieldPanels = new JPanel[Depot.fields.length];
		// livraison
		ArrayList<JourSemaine> joursLivraisons = createDeliveryDaysPanel(panel, depotToEdit);
		// add image chooser
		AtomicReference<File> image = createImageChooserPanel(panel);
		return new DepotFormComponents(panel, addressChoice, referentChoice, fieldPanels, joursLivraisons, image);
	}

	private record DepotFormComponents(JPanel panel, JPanel addressChoice, JPanel referentChoice, JPanel[] fieldPanels,
	                                   ArrayList<JourSemaine> joursLivraisons, AtomicReference<File> image) {
	}

	private static String[] getValues(JPanel[] fieldPanels, JPanel addressChoice, JPanel referentChoice) {
		String[] values = new String[fieldPanels.length + 2];
		for (int i = 0; i < Depot.fields.length; i++) {
			boolean isRequired = Depot.requiredFieldsList.contains(Depot.fields[i]);
			String textFieldValue = ((TextField) fieldPanels[i].getComponent(1)).getText();

			if (isRequired && textFieldValue.isEmpty()) {
				Logger.error("Please fill all required fields");
				return new String[0];
			}
			values[i + 2] = textFieldValue;
		}
		// adresse
		values[0] = Integer.toString(Adresse.adresses.get(((Choice) addressChoice.getComponent(1)).getSelectedIndex()).id);
		// referent
		values[1] = Integer.toString(Referent.referents.get(((Choice) referentChoice.getComponent(1)).getSelectedIndex()).id);
		return values;
	}

	/**
	 * This function is used to get an image file from the user. It creates a panel with a button that opens a file chooser when clicked.
	 * The selected file is stored in an AtomicReference<File> which is returned by the function.
	 *
	 * @param parentPanel The panel to which the image chooser is added.
	 * @return An AtomicReference<File> containing the selected image file.
	 */
	private AtomicReference<File> createImageChooserPanel(JPanel parentPanel) {
		JPanel imagePanel = new JPanel();
		imagePanel.setLayout(new GridLayout(1, 2));
		imagePanel.add(new Label("Image"));
		UButton imageButton = new UButton("Select");
		AtomicReference<File> image = new AtomicReference<>();
		imageButton.addActionListener(e -> {
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
			int result = fileChooser.showOpenDialog(this);
			if (result == JFileChooser.APPROVE_OPTION) image.set(fileChooser.getSelectedFile());
		});
		imagePanel.add(imageButton);
		parentPanel.add(imagePanel);
		return image;

	}

	/**
	 * This function is used to create a panel for selecting delivery days. It creates a panel with a button that opens a popup menu with a list of days when clicked.
	 * The selected days are stored in an ArrayList<JourSemaine> which is returned by the function.
	 *
	 * @param parentPanel The panel to which the delivery days chooser is added.
	 * @param depotToEdit The depot to be edited.
	 * @return An ArrayList<JourSemaine> containing the selected delivery days.
	 */
	private ArrayList<JourSemaine> createDeliveryDaysPanel(JPanel parentPanel, Depot depotToEdit) {
		JPanel delivery = new JPanel();
		delivery.setLayout(new GridLayout(1, 2));
		delivery.add(new Label("Jour de livraison *"));
		// Create components
		UButton button = new UButton("Select");
		JPopupMenu popupMenu = new JPopupMenu();
		ArrayList<JourSemaine> joursLivraisons = new ArrayList<>();
		JList<String> list = new JList<>(new String[]{"Lundi", "Mardi", "Mercredi", "Jeudi", "Vendredi", "Samedi", "Dimanche"});
		// Set list to multiple interval selection
		list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		// select joursLivraisons if editing
		if (depotToEdit != null) {
			int[] selectedIndices = new int[depotToEdit.jourLivraison.length];
			for (int i = 0; i < depotToEdit.jourLivraison.length; i++) {
				selectedIndices[i] = depotToEdit.jourLivraison[i].ordinal();
			}
			list.setSelectedIndices(selectedIndices);
		}
		for (int i : list.getSelectedIndices()) joursLivraisons.add(JourSemaine.values()[i]);
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
		delivery.add(button);
		parentPanel.add(delivery);
		return joursLivraisons;
	}

	/**
	 * This function is used to populate the fields of a depot object. It creates a panel for each field and sets the text of the text field to the value of the corresponding field in the depot object.
	 *
	 * @param depot       The depot to be edited.
	 * @param fieldPanels The panels containing the data.
	 * @param parentPanel The panel to which the fields are added.
	 */
	private void populateFields(Depot depot, JPanel[] fieldPanels, JPanel parentPanel) {
		for (int i = 0; i < Depot.fields.length; i++) {
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
	 *
	 * @param depot The depot to be edited.
	 * @return A Choice component with all referents and the referent of the depot selected.
	 */
	private static Choice createReferentChoiceComponent(Depot depot) {
		Choice referentChoice = new Choice();
		for (int i = 0; i < Referent.referents.size(); i++) referentChoice.add(Referent.referents.get(i).toString());
		// select referent
		if (depot == null) return referentChoice;
		for (int i = 0; i < Referent.referents.size(); i++)
			if (Referent.referents.get(i).id == depot.referentIdReferent) {
				referentChoice.select(i);
				break;
			}
		return referentChoice;
	}

	/**
	 * This function is used to create a choice component for selecting an address. It adds all addresses to the choice component and selects the address of the depot object.
	 *
	 * @param depot The depot to be edited.
	 * @return A Choice component with all addresses, and the address of the depot selected.
	 */
	private static Choice createAddressChoiceComponent(Depot depot) {
		Choice adresseChoice = new Choice();
		for (int i = 0; i < Adresse.adresses.size(); i++) adresseChoice.add(Adresse.adresses.get(i).toString());
		// select adresse
		if (depot == null) return adresseChoice;
		for (int i = 0; i < Adresse.adresses.size(); i++)
			if (Adresse.adresses.get(i).id == depot.adresseIdAdresse) {
				adresseChoice.select(i);
				break;
			}
		return adresseChoice;
	}
}
