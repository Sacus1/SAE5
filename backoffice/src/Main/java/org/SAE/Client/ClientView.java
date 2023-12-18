package org.SAE.Client;

import org.SAE.Adresse.Adresse;
import org.SAE.Main.BaseView;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import org.SAE.Main.UButton;
import javax.swing.*;
import java.awt.*;
import java.sql.Date;
import java.util.Objects;
import java.util.Properties;

public class ClientView extends BaseView {

	public ClientView() {
		super();
		Client.getFromDatabase();
		topPanel.remove(createButton);
		add(topPanel, "North");
		add(mainPanel, "Center");
		add(bottomPanel, "South");
		displayView(false);
	}

	public void displayView(boolean isCreateMode) {
		inCreation = false;
		clear();
		for (Client client : Client.clients) mainPanel.add(createListPanel(client));
		refresh();
	}

	private JPanel createListPanel(Client client) {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(1, 3));
		JLabel label = new JLabel(client.toString());
		UButton editUButton = new UButton("Edit");
		editUButton.addActionListener(e -> {
			clear();
			mainPanel.add(createEditPanel(client));
			refresh();
		});
		UButton deleteUButton = new UButton("Delete");
		deleteUButton.addActionListener(e -> {
			Client.delete(client);
			displayView(false);
		});
		panel.add(label);
		panel.add(editUButton);
		panel.add(deleteUButton);
		return panel;
	}

	private JPanel createEditPanel(Client client) {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(0, 2));
		Adresse.getFromDatabase();
		JComboBox<Adresse> adresseComboBox = new JComboBox<>();
		for (Adresse adresse : Adresse.adresses) adresseComboBox.addItem(adresse);
		JTextField raisonSocialeField = new JTextField();
		JTextField civiliteField = new JTextField();
		JTextField nomField = new JTextField();
		JTextField prenomField = new JTextField();
		JTextField telephoneField = new JTextField();
		JTextField telephone2Field = new JTextField();
		JTextField telephone3Field = new JTextField();
		JTextField mailField = new JTextField();
		JTextField professionField = new JTextField();
		UtilDateModel model = new UtilDateModel();
		Properties p = new Properties();
		p.put("text.today", "Today");
		p.put("text.month", "Month");
		p.put("text.year", "Year");
		JDatePanelImpl datePanel = new JDatePanelImpl(model, p);
		JDatePickerImpl datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());
		JCheckBox estDispenseCheckBox = new JCheckBox();
		panel.add(new JLabel("Adresse"));
		panel.add(adresseComboBox);
		panel.add(new JLabel("Raison sociale"));
		panel.add(raisonSocialeField);
		panel.add(new JLabel("Civilité"));
		panel.add(civiliteField);
		panel.add(new JLabel("Nom"));
		panel.add(nomField);
		panel.add(new JLabel("Prénom"));
		panel.add(prenomField);
		panel.add(new JLabel("Téléphone"));
		panel.add(telephoneField);
		panel.add(new JLabel("Téléphone 2"));
		panel.add(telephone2Field);
		panel.add(new JLabel("Téléphone 3"));
		panel.add(telephone3Field);
		panel.add(new JLabel("Mail"));
		panel.add(mailField);
		panel.add(new JLabel("Profession"));
		panel.add(professionField);
		panel.add(new JLabel("Date de naissance"));
		panel.add(datePicker);
		panel.add(new JLabel("Est dispensé"));
		panel.add(estDispenseCheckBox);
		adresseComboBox.setSelectedItem(Adresse.adresses.get(client.adresseIdAdresse - 1));
		raisonSocialeField.setText(client.raisonSociale);
		civiliteField.setText(client.civilite);
		nomField.setText(client.nom);
		prenomField.setText(client.prenom);
		telephoneField.setText(client.telephone);
		telephone2Field.setText(client.telephone2);
		telephone3Field.setText(client.telephone3);
		mailField.setText(client.mail);
		professionField.setText(client.profession);
		model.setValue(client.dateNaissance);
		estDispenseCheckBox.setSelected(client.estDispense);
		UButton cancelUButton = new UButton("Cancel");
		panel.add(cancelUButton);
		cancelUButton.addActionListener(e -> displayView(false));
		UButton createUButton = new UButton("Update");
		panel.add(createUButton);
		createUButton.addActionListener(e -> {
			client.adresseIdAdresse = ((Adresse) Objects.requireNonNull(adresseComboBox.getSelectedItem())).id;
			client.raisonSociale = raisonSocialeField.getText();
			client.civilite = civiliteField.getText();
			client.nom = nomField.getText();
			client.prenom = prenomField.getText();
			client.telephone = telephoneField.getText();
			client.telephone2 = telephone2Field.getText();
			client.telephone3 = telephone3Field.getText();
			client.mail = mailField.getText();
			client.profession = professionField.getText();
			client.dateNaissance = Date.valueOf(datePicker.getJFormattedTextField().getText());
			client.estDispense = estDispenseCheckBox.isSelected();
			Client.update(client);
			displayView(false);
		});
		return panel;
	}


}
