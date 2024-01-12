package org.SAE.Adhesion;

import org.SAE.Client.Client;
import org.SAE.Client.DateLabelFormatter;
import org.SAE.Jardin.Jardin;
import org.SAE.Main.BaseView;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import javax.swing.*;
import java.awt.*;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Properties;

public class AdhesionView extends BaseView<Adhesion> {
	Client client;

	public AdhesionView(Client client) {
		super("Adhesion");
		this.client = client;
		add(topPanel, "North");
		add(mainPanel, "Center");
		add(bottomPanel, "South");
		displayView(false);
	}

	@Override
	protected ArrayList<Adhesion> getList() {
		return (ArrayList<Adhesion>) Adhesion.adhesions;
	}

	@Override
	protected JPanel createFormPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(0, 2));
		JComboBox<TypeAdhesion> typeAdhesionComboBox = new JComboBox<>();
		for (TypeAdhesion typeAdhesion : TypeAdhesion.typeAdhesions) typeAdhesionComboBox.addItem(typeAdhesion);
		JComboBox<Jardin> jardinComboBox = new JComboBox<>();
		for (Jardin jardin : Jardin.jardins) jardinComboBox.addItem(jardin);
		Properties p = new Properties();
		p.put("text.today", "Today");
		p.put("text.month", "Month");
		p.put("text.year", "Year");
		JDatePanelImpl debutPanel = new JDatePanelImpl(new UtilDateModel(), p);
		JDatePickerImpl debutField = new JDatePickerImpl(debutPanel, new DateLabelFormatter());
		JDatePanelImpl finPanel = new JDatePanelImpl(new UtilDateModel(), p);
		JDatePickerImpl finField = new JDatePickerImpl(finPanel, new DateLabelFormatter());
		JCheckBox enCoursCheckBox = new JCheckBox();
		JTextField frequencePaiementField = new JTextField();
		panel.add(new JLabel("Type d'adhésion"));
		panel.add(typeAdhesionComboBox);
		panel.add(new JLabel("Jardin"));
		panel.add(jardinComboBox);
		panel.add(new JLabel("Date début"));
		panel.add(debutField);
		panel.add(new JLabel("Date fin"));
		panel.add(finField);
		panel.add(new JLabel("Fréquence de paiement"));
		panel.add(frequencePaiementField);
		panel.add(new JLabel("En cours"));
		panel.add(enCoursCheckBox);
		JButton createButton = new JButton("Créer");
		createButton.addActionListener(e -> {
			Adhesion adhesion = new Adhesion(
							client,
							(TypeAdhesion) typeAdhesionComboBox.getSelectedItem(),
							(Jardin) jardinComboBox.getSelectedItem(),
							Date.valueOf(debutField.getJFormattedTextField().getText()),
							Date.valueOf(finField.getJFormattedTextField().getText()),
							enCoursCheckBox.isSelected(),
							Integer.parseInt(frequencePaiementField.getText())
			);
			Adhesion.create(adhesion);
			displayView(false);
		});
		panel.add(createButton);
		return panel;
	}

	@Override
	protected JPanel createDetailPanel(Adhesion object) {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(0, 2));
		JComboBox<TypeAdhesion> typeAdhesionComboBox = new JComboBox<>();
		for (TypeAdhesion typeAdhesion : TypeAdhesion.typeAdhesions) typeAdhesionComboBox.addItem(typeAdhesion);
		typeAdhesionComboBox.setSelectedItem(object.typeAdhesion);
		JComboBox<Jardin> jardinComboBox = new JComboBox<>();
		for (Jardin jardin : Jardin.jardins) jardinComboBox.addItem(jardin);
		jardinComboBox.setSelectedItem(object.jardin);
		Properties p = new Properties();
		p.put("text.today", "Today");
		p.put("text.month", "Month");
		p.put("text.year", "Year");
		JDatePanelImpl debutPanel = new JDatePanelImpl(new UtilDateModel(), p);
		JDatePickerImpl debutField = new JDatePickerImpl(debutPanel, new DateLabelFormatter());
		debutField.getJFormattedTextField().setText(object.debut.toString());
		JDatePanelImpl finPanel = new JDatePanelImpl(new UtilDateModel(), p);
		JDatePickerImpl finField = new JDatePickerImpl(finPanel, new DateLabelFormatter());
		finField.getJFormattedTextField().setText(object.fin.toString());
		JCheckBox enCoursCheckBox = new JCheckBox();
		enCoursCheckBox.setSelected(object.enCours);
		JTextField frequencePaiementField = new JTextField();
		frequencePaiementField.setText(String.valueOf(object.frequencePaiement));
		panel.add(new JLabel("Type d'adhésion"));
		panel.add(typeAdhesionComboBox);
		panel.add(new JLabel("Jardin"));
		panel.add(jardinComboBox);
		panel.add(new JLabel("Date début"));
		panel.add(debutField);
		panel.add(new JLabel("Date fin"));
		panel.add(finField);
		panel.add(new JLabel("Fréquence de paiement"));
		panel.add(frequencePaiementField);
		panel.add(new JLabel("En cours"));
		panel.add(enCoursCheckBox);
		JButton updateButton = new JButton("Modifier");
		updateButton.addActionListener(e -> {
			if (object == null) return;
			if (typeAdhesionComboBox.getSelectedItem() == null) return;
			if (jardinComboBox.getSelectedItem() == null) return;
			if (debutField.getJFormattedTextField().getText().isEmpty()) return;
			if (finField.getJFormattedTextField().getText().isEmpty()) return;
			if (frequencePaiementField.getText().isEmpty()) return;
			object.typeAdhesion = (TypeAdhesion) typeAdhesionComboBox.getSelectedItem();
			object.jardin = (Jardin) jardinComboBox.getSelectedItem();
			object.debut = Date.valueOf(debutField.getJFormattedTextField().getText());
			object.fin = Date.valueOf(finField.getJFormattedTextField().getText());
			object.enCours = enCoursCheckBox.isSelected();
			object.frequencePaiement = Integer.parseInt(frequencePaiementField.getText());
			Adhesion.update(object);
			displayView(false);
		});
		panel.add(updateButton);
		return panel;
	}


}
