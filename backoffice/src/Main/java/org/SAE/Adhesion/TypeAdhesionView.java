package org.SAE.Adhesion;

import org.SAE.Main.BaseView;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class TypeAdhesionView extends BaseView<TypeAdhesion> {
	public TypeAdhesionView() {
		super("Type d'adhésion");
		add(topPanel, "North");
		add(mainPanel, "Center");
		add(bottomPanel, "South");
		displayView(false);
	}
	@Override
	protected JPanel createListPanel(TypeAdhesion t) {
		JPanel panel = new JPanel();
		panel.setMaximumSize(new Dimension(SCREEN_WIDTH-10, 30));
		panel.setPreferredSize(new Dimension(SCREEN_WIDTH-10, 30));
		panel.setLayout(new GridLayout(1, 2));
		JLabel label = new JLabel("<html>" + t.toString().replace("<", "&lt;").replace(">", "&gt;").replace("\n"
						, "<br/>") + "</html>");
		JButton deleteButton = new JButton("Supprimer");
		deleteButton.addActionListener(e -> {
			t.delete();
			t.loadFromDatabase();
			displayView(false);
		});
		panel.add(label);
		panel.add(deleteButton);
		return panel;
	}
	@Override
	protected ArrayList<TypeAdhesion> getList() {
		return (ArrayList<TypeAdhesion>) TypeAdhesion.typeAdhesions;
	}

	@Override
	protected JPanel createFormPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(0, 2));
		JTextField nomField = new JTextField();
		JTextField tarifField = new JTextField();
		panel.add(new JLabel("Nom"));
		panel.add(nomField);
		panel.add(new JLabel("Tarif"));
		panel.add(tarifField);
		JButton createButton = new JButton("Créer");
		createButton.addActionListener(e -> {
			String nom = nomField.getText();
			int tarif = Integer.parseInt(tarifField.getText());
			TypeAdhesion.create(new TypeAdhesion(nom, tarif));
			displayView(false);
		});
		panel.add(createButton);
		return panel;
	}

	@Override
	protected JPanel createDetailPanel(TypeAdhesion object) {
		return null;
	}

}
