package org.SAE.Client;

import org.SAE.Adhesion.AdhesionView;
import org.SAE.Adresse.Adresse;
import org.SAE.Main.BaseView;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import javax.swing.*;
import java.awt.*;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Properties;

public class ClientView extends BaseView<Client> {

	public ClientView() {
		super("Client");
		add(topPanel, "North");
		add(mainPanel, "Center");
		add(bottomPanel, "South");
		displayView(false);
	}

	@Override
	protected ArrayList<Client> getList() {
		return (ArrayList<Client>) Client.clients;
	}

	@Override
	protected JPanel createFormPanel() {
		if (Adresse.adresses.isEmpty()) {
			org.SAE.Main.Logger.error("Veuillez créer une adresse avant de créer un client");
			return null;
		}
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(0, 2));
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
		panel.add(new JLabel("Adresse*"));
		panel.add(adresseComboBox);
		panel.add(new JLabel("Raison sociale*"));
		panel.add(raisonSocialeField);
		panel.add(new JLabel("Civilité*"));
		panel.add(civiliteField);
		panel.add(new JLabel("Nom*"));
		panel.add(nomField);
		panel.add(new JLabel("Prénom*"));
		panel.add(prenomField);
		panel.add(new JLabel("Téléphone*"));
		panel.add(telephoneField);
		panel.add(new JLabel("Téléphone 2"));
		panel.add(telephone2Field);
		panel.add(new JLabel("Téléphone 3"));
		panel.add(telephone3Field);
		panel.add(new JLabel("Mail*"));
		panel.add(mailField);
		panel.add(new JLabel("Profession"));
		panel.add(professionField);
		panel.add(new JLabel("Date de naissance*"));
		panel.add(datePicker);
		panel.add(new JLabel("Est dispensé"));
		panel.add(estDispenseCheckBox);
		JButton createJButton = new JButton("Créer");
		panel.add(createJButton);
		createJButton.addActionListener(e -> {
			// check for field
			if (raisonSocialeField.getText().isEmpty()) {
				JOptionPane.showMessageDialog(null, "Veuillez renseigner la raison sociale");
				return;
			}
			if (civiliteField.getText().isEmpty()) {
				JOptionPane.showMessageDialog(null, "Veuillez renseigner la civilité");
				return;
			}
			if (nomField.getText().isEmpty()) {
				JOptionPane.showMessageDialog(null, "Veuillez renseigner le nom");
				return;
			}
			if (prenomField.getText().isEmpty()) {
				JOptionPane.showMessageDialog(null, "Veuillez renseigner le prénom");
				return;
			}
			if (telephoneField.getText().isEmpty()) {
				JOptionPane.showMessageDialog(null, "Veuillez renseigner le téléphone");
				return;
			}
			if (mailField.getText().isEmpty()) {
				JOptionPane.showMessageDialog(null, "Veuillez renseigner le mail");
				return;
			}
			if (datePicker.getJFormattedTextField().getText().isEmpty()) {
				JOptionPane.showMessageDialog(null, "Veuillez renseigner la date de naissance");
				return;
			}
			Client client = new Client((Adresse) Objects.requireNonNull(adresseComboBox.getSelectedItem()),
							raisonSocialeField.getText(),
							civiliteField.getText(),
							nomField.getText(),
							prenomField.getText(),
							new String[]{telephoneField.getText(), telephone2Field.getText(), telephone3Field.getText()},
							mailField.getText(),
							professionField.getText(),
							Date.valueOf(datePicker.getJFormattedTextField().getText()),
							estDispenseCheckBox.isSelected());
			Client.create(client);
			displayView(false);
		});
		return panel;
	}


	@Override
	protected JPanel createDetailPanel(Client client) {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(0, 2));
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
		adresseComboBox.setSelectedItem(client.adresse);
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
		// Adhesion list
		// panier
		JButton adhesionButton = new JButton("Adhesion");
		adhesionButton.addActionListener(e -> {
			clear();
			topPanel.remove(createButton);
			topPanel.add(new JLabel("Adhesion de " + client.raisonSociale), "West");
			mainPanel.add(new AdhesionView(client));
			refresh();
		});
		panel.add(adhesionButton);
		JButton createJButton = new JButton("Modifier");
		panel.add(createJButton);
		createJButton.addActionListener(e -> {
			client.adresse = (Adresse) Objects.requireNonNull(adresseComboBox.getSelectedItem());
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
