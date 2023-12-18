package org.SAE.Referent;

import org.SAE.Main.BaseView;

import javax.swing.*;
import java.awt.*;

public class ReferentView extends BaseView {
	public ReferentView() {
		super();
		Referent.getFromDatabase();
		setLayout(new BorderLayout());
		add(topPanel, "North");
		add(mainPanel, "Center");
		add(bottomPanel, "South");
		displayView(false);
	}
	public void displayView(boolean isCreateMode) {
		if (!isCreateMode) {
			clear();
			for (Referent referent : Referent.referents) mainPanel.add(createListPanel(referent));
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
	public JPanel createListPanel(Referent referent) {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(1, 3));
		JLabel label = new JLabel(referent.toString());
		panel.add(label);
		Button editButton = new Button("Edit");
		editButton.addActionListener(e -> {
			clear();
			mainPanel.add(createEditPanel(referent));
			refresh();
			// rename create button to cancel
			createButton.setText("Cancel");
			inCreation = true;
		});
		panel.add(editButton);
		Button deleteButton = new Button("Delete");
		deleteButton.addActionListener(e -> {
			Referent.delete(referent);
			displayView(false);
		});
		panel.add(deleteButton);
		return panel;
	}
	public JPanel createFormPanel() {
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
		Button createButton = getCreateButton(nomField, telephoneField, emailField);
		panel.add(createButton);
		return panel;
	}

	private Button getCreateButton(JTextField nomField, JTextField telephoneField, JTextField emailField) {
		Button createButton = new Button("Create");
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

	public JPanel createEditPanel(Referent referent) {
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
		Button createButton = new Button("Edit");
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
