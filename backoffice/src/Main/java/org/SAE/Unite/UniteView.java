package org.SAE.Unite;

import org.SAE.Main.BaseView;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class UniteView extends BaseView<Unite> {
	public UniteView() {
		super("Unite");
		setLayout(new BorderLayout());
		// add panels to the frame
		add(topPanel, "North");
		add(mainPanel, "Center");
		add(bottomPanel, "South");
		displayView(false);
	}

	@Override
	protected ArrayList<Unite> getList() {
		return (ArrayList<Unite>) Unite.unites;
	}

	@Override
	protected JPanel createEditPanel(Unite unite) {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(1, 2));
		// nom
		Label nomLabel = new Label("nom");
		panel.add(nomLabel);
		TextField nomField = new TextField(unite.nom);
		panel.add(nomField);
		// create button
		JButton updateButton = new JButton("Modifier");
		updateButton.addActionListener(e -> {
			unite.nom = nomField.getText();
			Unite.update(unite);
			displayView(false);
		});
		panel.add(updateButton);
		return panel;
	}

	@Override
	protected JPanel createFormPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(2, 2));
		// nom
		Label nomLabel = new Label("nom");
		panel.add(nomLabel);
		TextField nomField = new TextField();
		panel.add(nomField);
		// create button
		JButton createButton = new JButton("Créer");
		createButton.addActionListener(e -> {
			Unite unite = new Unite(nomField.getText());
			Unite.create(unite);
			displayView(false);
		});
		panel.add(createButton);
		return panel;
	}

}
