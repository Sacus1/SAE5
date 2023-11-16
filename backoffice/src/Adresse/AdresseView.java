package Adresse;

import Main.BaseView;

import javax.swing.*;
import java.awt.*;

public class AdresseView extends BaseView {
	public AdresseView() {
		super();
		Adresse.getFromDatabase();
		draw(false);
	}
	public void draw(boolean isCreate) {
		if (isCreate) {
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
	public JPanel createListPanel(Adresse adresse) {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(1, 3));
		JLabel label = new JLabel(adresse.toString());
		JButton editButton = new JButton("Edit");
		editButton.addActionListener(e -> {
			clear();
			mainPanel.add(createEditPanel(adresse));
			refresh();
		});
		JButton deleteButton = new JButton("Delete");
		deleteButton.addActionListener(e -> {
			Adresse.delete(adresse);
			draw(false);
		});
		panel.add(label);
		panel.add(editButton);
		panel.add(deleteButton);
		return panel;
	}



	public JPanel createFormPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(4, 2));
		JTextField adresseField = new JTextField();
		JTextField villeField = new JTextField();
		JTextField codePostalField = new JTextField();
		JButton submitButton = new JButton("Submit");
		JButton cancelButton = new JButton("Cancel");
		panel.add(new JLabel("Adresse *"));
		panel.add(adresseField);
		panel.add(new JLabel("Ville *"));
		panel.add(villeField);
		panel.add(new JLabel("Code Postal *"));
		panel.add(codePostalField);
		panel.add(submitButton);
		panel.add(cancelButton);
		submitButton.addActionListener(e -> {
			// check all field are filled
			if (adresseField.getText().isEmpty() || villeField.getText().isEmpty() || codePostalField.getText().isEmpty()) {
				Main.Logger.error("All fields must be filled");
				return;
			}
			Adresse adresse = new Adresse(adresseField.getText(), villeField.getText(), codePostalField.getText());
			Adresse.create(adresse);
			draw(false);
		});
		cancelButton.addActionListener(e -> draw(false));
		return panel;
	}

	private Panel createEditPanel(Adresse adresse) {
		Panel panel = new Panel();
		panel.setLayout(new GridLayout(4, 2));
		JTextField adresseField = new JTextField(adresse.adresse);
		JTextField villeField = new JTextField(adresse.ville);
		JTextField codePostalField = new JTextField(adresse.codePostal);
		JButton submitButton = new JButton("Submit");
		JButton cancelButton = new JButton("Cancel");
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
				Main.Logger.error("All fields must be filled");
				return;
			}
			adresse.adresse = adresseField.getText();
			adresse.ville = villeField.getText();
			adresse.codePostal = codePostalField.getText();
			Adresse.update(adresse);
			draw(false);
		});
		return panel;
	}
}
