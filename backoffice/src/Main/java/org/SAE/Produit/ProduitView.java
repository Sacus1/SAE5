package org.SAE.Produit;

import org.SAE.Main.BaseView;
import org.SAE.Unite.Unite;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

public class ProduitView extends BaseView<Produit>{
	public ProduitView() {
		super("Produit");
		setLayout(new BorderLayout());
		// add panels to the frame
		add(topPanel, "North");
		add(mainPanel, "Center");
		add(bottomPanel, "South");
		displayView(false);
	}

	@Override
	protected ArrayList<Produit> getList() {
		return (ArrayList<Produit>) Produit.produits;
	}

	@Override
	protected JPanel createFormPanel() {
		if (Unite.unites.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Veuillez créer une unité avant de créer un produit");
			return null;
		}
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(6, 1));
		// create the form
		JTextField nomField = new JTextField();
		panel.add(new JLabel("Nom"));
		panel.add(nomField);
		JTextField descriptionField = new JTextField();
		panel.add(new JLabel("Description"));
		panel.add(descriptionField);
		Choice uniteChoice = new Choice();
		for (int i = 0; i < Unite.unites.size(); i++) uniteChoice.add(Unite.unites.get(i).toString());
		panel.add(new JLabel("Unite"));
		panel.add(uniteChoice);
		// image choice
		AtomicReference<File> image = createImageChooserPanel(panel, null);
		// create a button to create the produit
		JButton createButton = new JButton("Créer");
		createButton.addActionListener(e -> {
			if (nomField.getText().equals("")) {
				JOptionPane.showMessageDialog(this, "Veuillez entrer un nom");
				return;
			}
			if (descriptionField.getText().equals("")) {
				JOptionPane.showMessageDialog(this, "Veuillez entrer une description");
				return;
			}
			Produit produit = new Produit(nomField.getText(), descriptionField.getText(), Unite.unites.get(uniteChoice.getSelectedIndex()).id, image.get());
			Produit.create(produit);
			displayView(false);
		});
		panel.add(createButton);
		return panel;
	}
	@Override
	protected JPanel createDetailPanel(Produit produit) {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(5, 2));
		// nom
		Label nomLabel = new Label("nom");
		panel.add(nomLabel);
		TextField nomField = new TextField(produit.nom);
		panel.add(nomField);
		// description
		Label descriptionLabel = new Label("description");
		panel.add(descriptionLabel);
		TextField descriptionField = new TextField(produit.description);
		panel.add(descriptionField);
		// unite
		Label uniteLabel = new Label("unite");
		panel.add(uniteLabel);
		Choice uniteChoice = new Choice();
		for (int i = 0; i < Unite.unites.size(); i++) uniteChoice.add(Unite.unites.get(i).toString());
		Unite unite = Unite.getUniteById(produit.idUnite);
		uniteChoice.select(unite.toString());
		panel.add(uniteChoice);
		// image choice
		AtomicReference<File> image = createImageChooserPanel(panel, produit);
		// create button
		JButton updateButton = new JButton("Modifier");
		updateButton.addActionListener(e -> {
			produit.nom = nomField.getText();
			produit.description = descriptionField.getText();
			produit.idUnite = Unite.unites.get(uniteChoice.getSelectedIndex()).id;
			produit.image = image.get();
			Produit.update(produit);
			displayView(false);
		});
		panel.add(updateButton);
		return panel;
	}
	private AtomicReference<File> createImageChooserPanel(JPanel parentPanel, Produit p) {
		parentPanel.add(new Label("Image"));
		JButton imageButton = null;
		if (p != null && p.image != null && p.image.exists()) {
			// copy and resize image
			Image image = new ImageIcon(p.image.getAbsolutePath()).getImage();
			Image newImage = image.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
			ImageIcon imageIcon = new ImageIcon(newImage);
			imageButton = new JButton(imageIcon);
		} else {
			imageButton = new JButton("Select");
		}
		AtomicReference<File> image = new AtomicReference<>();
		JButton finalImageButton = imageButton;
		imageButton.addActionListener(e -> {
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
			int result = fileChooser.showOpenDialog(this);
			if (result == JFileChooser.APPROVE_OPTION) {
				image.set(fileChooser.getSelectedFile());
				Image imager = new ImageIcon(fileChooser.getSelectedFile().getAbsolutePath()).getImage();
				Image newImage = imager.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
				ImageIcon imageIcon = new ImageIcon(newImage);
				finalImageButton.setIcon(imageIcon);
			}
		});
		parentPanel.add(imageButton);
		return image;
	}
}
