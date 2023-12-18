package org.SAE.Adresse;

import org.SAE.Main.BaseView;

import javax.swing.*;
import java.awt.*;
/**
 * This class represents the view for the Adresse module.
 * It extends the BaseView class and provides methods for creating and managing the UI components related to Adresse.
 */
public class AdresseView extends BaseView {
	/**
	 * Constructor for the AdresseView class.
	 * It initializes the UI components and fetches the Adresse data from the database.
	 */
	public AdresseView() {
		super();
		Adresse.getFromDatabase();
		add(topPanel, "North");
		add(mainPanel, "Center");
		add(bottomPanel, "South");
		displayView(false);
	}

	/**
	 * This method is used to draw the UI components on the screen.
	 * It takes a boolean parameter to decide whether to create a new Adresse or display the existing ones.
	 *
	 * @param isCreateMode A boolean value to decide the mode of operation.
	 */
	public void displayView(boolean isCreateMode) {
		if (isCreateMode) {
			clear();
			mainPanel.add(createFormPanel());
			refresh();
			// rename create button to cancel
			createButton.setText("Cancel");
			inCreation = true;
		} else {
			clear();
			for (Adresse adresse : Adresse.adresses) mainPanel.add(createListPanel(adresse));
			refresh();
			// rename cancel button to create
			createButton.setText("Create");
			inCreation = false;
		}
	}

	/**
	 * This method creates a JPanel for each Adresse object.
	 * It includes a label to display the Adresse and buttons to edit and delete the Adresse.
	 * @param adresse The Adresse object for which the JPanel is to be created.
	 * @return A JPanel with the Adresse details and action buttons.
	 */
	public JPanel createListPanel(Adresse adresse) {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(1, 3));
		JLabel label = new JLabel(adresse.toString());
		Button editButton = new Button("Edit");
		editButton.addActionListener(e -> {
			clear();
			mainPanel.add(createEditPanel(adresse));
			refresh();
		});
		Button deleteButton = new Button("Delete");
		deleteButton.addActionListener(e -> {
			Adresse.delete(adresse);
			displayView(false);
		});
		panel.add(label);
		panel.add(editButton);
		panel.add(deleteButton);
		return panel;
	}

	/**
	 * This method creates a form for creating a new Adresse.
	 * It includes text fields for the Adresse details, and a submit button to create the Adresse.
	 * @return A JPanel with the form for creating a new Adresse.
	 */
	public JPanel createFormPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(4, 2));
		JTextField adresseField = new JTextField();
		JTextField villeField = new JTextField();
		JTextField codePostalField = new JTextField();
		Button submitButton = new Button("Submit");
		panel.add(new JLabel("Adresse *"));
		panel.add(adresseField);
		panel.add(new JLabel("Ville *"));
		panel.add(villeField);
		panel.add(new JLabel("Code Postal *"));
		panel.add(codePostalField);
		panel.add(submitButton);
		submitButton.addActionListener(e -> {
			// check all field are filled
			if (adresseField.getText().isEmpty() || villeField.getText().isEmpty() || codePostalField.getText().isEmpty()) {
				org.SAE.Main.Logger.error("All fields must be filled");
				return;
			}
			// check if postal code is a number and is 5 digits long.
			if (!codePostalField.getText().matches("\\d+") || codePostalField.getText().length() != 5) {
				org.SAE.Main.Logger.error("Code postal must be a number and 5 digits long");
				return;
			}
			Adresse adresse = new Adresse(adresseField.getText(), villeField.getText(), codePostalField.getText());
			Adresse.create(adresse);
			displayView(false);
		});
		return panel;
	}

	/**
	 * This method creates a form for editing an existing Adresse.
	 * It includes text fields for the Adresse details and buttons to submit the changes or cancel the operation.
	 * @param adresse The Adresse object to be edited.
	 * @return A Panel with the form for editing the Adresse.
	 */
	private Panel createEditPanel(Adresse adresse) {
		Panel panel = new Panel();
		panel.setLayout(new GridLayout(4, 2));
		JTextField adresseField = new JTextField(adresse.rue);
		JTextField villeField = new JTextField(adresse.ville);
		JTextField codePostalField = new JTextField(adresse.codePostal);
		Button submitButton = new Button("Submit");
		Button cancelButton = new Button("Cancel");
		panel.add(new Label("Adresse *"));
		panel.add(adresseField);
		panel.add(new Label("Ville *"));
		panel.add(villeField);
		panel.add(new Label("Code Postal *"));
		panel.add(codePostalField);
		panel.add(submitButton);
		panel.add(cancelButton);
		submitButton.addActionListener(e -> {
			// check all field are filled
			if (adresseField.getText().isEmpty() || villeField.getText().isEmpty() || codePostalField.getText().isEmpty()) {
				org.SAE.Main.Logger.error("All fields must be filled");
				return;
			}
			// check if postal code is a number and is 5 digits long.
			if (!codePostalField.getText().matches("\\d+") || codePostalField.getText().length() != 5) {
				org.SAE.Main.Logger.error("Code postal must be a number and 5 digits long");
				return;
			}
			adresse.rue = adresseField.getText();
			adresse.ville = villeField.getText();
			adresse.codePostal = codePostalField.getText();
			Adresse.update(adresse);
			displayView(false);
		});
		return panel;
	}
}
