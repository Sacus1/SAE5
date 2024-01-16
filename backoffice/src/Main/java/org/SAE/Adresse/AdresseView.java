package org.SAE.Adresse;

import org.SAE.Main.BaseView;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * This class represents the view for the Adresse module.
 * It extends the BaseView class and provides methods for creating and managing the UI components related to Adresse.
 */
public class AdresseView extends BaseView<Adresse> {
	/**
	 * Constructor for the AdresseView class.
	 * It initializes the UI components and fetches the Adresse data from the database.
	 */
	public AdresseView() {
		super("Adresse");
		add(topPanel, "North");
		add(mainPanel, "Center");
		add(bottomPanel, "South");
		displayView(false);
	}

	@Override
	protected ArrayList<Adresse> getList() {
		return (ArrayList<Adresse>) Adresse.adresses;
	}


	/**
	 * This method creates a form for creating a new Adresse.
	 * It includes text fields for the Adresse details, and a submit button to create the Adresse.
	 * @return A JPanel with the form for creating a new Adresse.
	 */
	@Override
	protected JPanel createFormPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(4, 2));
		JTextField adresseField = new JTextField();
		JTextField villeField = new JTextField();
		JTextField codePostalField = new JTextField();
		JButton submitButton = new JButton("Créer");
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
	 * It includes text fields for the Adresse details and buttons to submit the changes.
	 * @param adresse The Adresse object to be edited.
	 * @return A JPanel with the form for editing the Adresse.
	 */
	@Override
	protected JPanel createDetailPanel(Adresse adresse) {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(4, 2));
		JTextField adresseField = new JTextField(adresse.rue);
		JTextField villeField = new JTextField(adresse.ville);
		JTextField codePostalField = new JTextField(adresse.codePostal);
		JButton submitButton = new JButton("Modifier");
		panel.add(new Label("Adresse *"));
		panel.add(adresseField);
		panel.add(new Label("Ville *"));
		panel.add(villeField);
		panel.add(new Label("Code Postal *"));
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
			adresse.rue = adresseField.getText();
			adresse.ville = villeField.getText();
			adresse.codePostal = codePostalField.getText();
			Adresse.update(adresse);
			displayView(false);
		});
		return panel;
	}
}
