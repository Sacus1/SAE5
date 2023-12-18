package org.SAE.Referent;

import org.SAE.Main.BaseView;
import org.SAE.Main.UButton;

import javax.swing.*;
import java.awt.*;

public class ReferentView extends BaseView<Referent> {
	public ReferentView() {
		super();
		Referent.getFromDatabase();
		setLayout(new BorderLayout());
		add(topPanel, "North");
		add(mainPanel, "Center");
		add(bottomPanel, "South");
		displayView(false);
	}
	@Override
	protected JPanel createFormPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(2, 2));
		// create text fields
		JTextField nomField = new JTextField();
		JTextField telephoneField = new JTextField();
		JTextField emailField = new JTextField();
		// create labels
		JLabel nomLabel = new JLabel("Nom *");
		JLabel telephoneLabel = new JLabel("Telephone *");
		JLabel emailLabel = new JLabel("Email *");
		// add labels and fields to the panel
		panel.add(nomLabel);
		panel.add(nomField);
		panel.add(telephoneLabel);
		panel.add(telephoneField);
		panel.add(emailLabel);
		panel.add(emailField);
		// add a button to create the referent
		UButton createButton = getCreateButton(nomField, telephoneField, emailField);
		panel.add(createButton);
		return panel;
	}

	private UButton getCreateButton(JTextField nomField, JTextField telephoneField, JTextField emailField) {
		UButton createButton = new UButton("Create");
		createButton.addActionListener(e -> {
			// check if all required fields are filled
			if (nomField.getText().isEmpty() || telephoneField.getText().isEmpty() || emailField.getText().isEmpty()) {
				org.SAE.Main.Logger.error("All fields must be filled");
				return;
			}
			// check if the email is valid
			if (!emailField.getText().matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
				org.SAE.Main.Logger.error("Invalid email");
				return;
			}
			// check if the telephone is valid
			if (!telephoneField.getText().matches("^(?:(?:\\+|00)33|0)[1-9](?:\\s?\\d{2}){4}$")) {
				org.SAE.Main.Logger.error("Invalid telephone");
				return;
			}
			Referent r = new Referent(nomField.getText(), telephoneField.getText(), emailField.getText());
			Referent.create(r);
			displayView(false);
		});
		return createButton;
	}

	@Override
	protected JPanel createEditPanel(Referent referent) {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(2, 2));
		// create text fields
		JTextField nomField = new JTextField(referent.nom);
		JTextField telephoneField = new JTextField(referent.telephone);
		JTextField emailField = new JTextField(referent.mail);
		// create labels
		JLabel nomLabel = new JLabel("Nom *");
		JLabel telephoneLabel = new JLabel("Telephone *");
		JLabel emailLabel = new JLabel("Email *");
		// add labels and fields to the panel
		panel.add(nomLabel);
		panel.add(nomField);
		panel.add(telephoneLabel);
		panel.add(telephoneField);
		panel.add(emailLabel);
		panel.add(emailField);
		// add a button to create the referent
		UButton createButton = new UButton("Edit");
		createButton.addActionListener(e -> {
			// check if all required fields are filled
			if (nomField.getText().isEmpty() || telephoneField.getText().isEmpty() || emailField.getText().isEmpty()) {
				org.SAE.Main.Logger.error("All fields must be filled");
				return;
			}
			// check if the email is valid
			if (!emailField.getText().matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$\n")) {
				org.SAE.Main.Logger.error("Invalid email");
				return;
			}
			// check if the telephone is valid
			if (!telephoneField.getText().matches("^(?:(?:\\+|00)33|0)[1-9](?:\\s?\\d{2}){4}$\n")) {
				org.SAE.Main.Logger.error("Invalid telephone");
				return;
			}
			referent.nom = nomField.getText();
			referent.telephone = telephoneField.getText();
			referent.mail = emailField.getText();
			Referent.update(referent);
			displayView(false);
		});
		panel.add(createButton);
		return panel;
	}
}
