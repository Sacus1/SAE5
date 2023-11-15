package Referent;

import javax.swing.*;
import java.awt.*;

public class ReferentView extends JPanel {
	JButton createButton = new JButton("Create");
	boolean inCreation = false;
	static JPanel mainPanel,topPanel,bottomPanel;
	public ReferentView() {
		super();
		Referent.getFromDatabase();
		setLayout(new BorderLayout());
		// top panel
		topPanel = new JPanel();
		topPanel.add(createButton);
		createButton.addActionListener(e -> draw(!inCreation));
		// bottom panel
		bottomPanel = new JPanel();
		// a main panel (list of referents or create referent)
		mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		draw(false);
		// add panels to the frame
		add(topPanel, "North");
		add(mainPanel, "Center");
		add(bottomPanel, "South");
	}
	public static void clear() {
		mainPanel.removeAll();
	}
	public static void refresh() {
		mainPanel.revalidate();
		mainPanel.repaint();
	}
	public void draw(boolean isCreate) {
		if (!isCreate) {
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
		JButton editButton = new JButton("Edit");
		JButton deleteButton = new JButton("Delete");
		panel.add(label);
		panel.add(editButton);
		panel.add(deleteButton);
		editButton.addActionListener(e -> {
			clear();
			mainPanel.add(createEditPanel(referent));
			refresh();
			// rename create button to cancel
			createButton.setText("Cancel");
			inCreation = true;
		});
		deleteButton.addActionListener(e -> {
			Referent.delete(referent);
			draw(false);
		});
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
		JButton createButton = new JButton("Create");
		createButton.addActionListener(e -> {
			// check if all required fields are filled
			if (nomField.getText().isEmpty() || telephoneField.getText().isEmpty() || emailField.getText().isEmpty()) {
				Main.Logger.error("All fields must be filled");
				return;
			}
			Referent r = new Referent(nomField.getText(), telephoneField.getText(), emailField.getText());
			Referent.create(r);
			draw(false);
		});
		panel.add(createButton);
		return panel;
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
		JButton createButton = new JButton("Edit");
		createButton.addActionListener(e -> {
			// check if all required fields are filled
			if (nomField.getText().isEmpty() || telephoneField.getText().isEmpty() || emailField.getText().isEmpty()) {
				Main.Logger.error("All fields must be filled");
				return;
			}
			referent.nom = nomField.getText();
			referent.telephone = telephoneField.getText();
			referent.mail = emailField.getText();
			Referent.update(referent);
			draw(false);
		});
		panel.add(createButton);
		return panel;
	}
}
