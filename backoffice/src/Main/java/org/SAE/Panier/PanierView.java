package org.SAE.Panier;

import org.SAE.Jardin.Jardin;
import org.SAE.Main.BaseView;
import org.SAE.Main.Logger;
import org.SAE.Produit.Produit;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

public class PanierView extends BaseView<Panier> {
	/**
	 * Constructor for the PanierView class.
	 * It initializes the UI components and fetches the Panier data from the database.
	 */
	Jardin jardin;
	public PanierView(Jardin jardin) {
		super("Panier");
		this.jardin = jardin;
		add(topPanel, "North");
		add(mainPanel, "Center");
		add(bottomPanel, "South");
		displayView(false);
	}

	@Override
	protected JPanel createListPanel(Panier panier) {
		if (!panier.jardin.equals(jardin)) return null;
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(1, 3));
		JLabel label = new JLabel(panier.toString());
		label.setHorizontalAlignment(JLabel.CENTER);
		JButton editButton = new JButton("Détailler");
		editButton.addActionListener(e -> {
			displayView(true);
			clear();
			mainPanel.add(createDetailPanel(panier));
			refresh();
		});
		JButton deleteButton = new JButton("Supprimer");
		deleteButton.addActionListener(e -> {
			panier.delete();
			panier.loadFromDatabase();
			displayView(false);
		});
		panel.add(label);
		panel.add(editButton);
		panel.add(deleteButton);
		return panel;
	}
	@Override
	protected ArrayList<Panier> getList() {
		return (ArrayList<Panier>) Panier.paniers;
	}
	/**
 * This function is used to get an image file from the user. It creates a panel with a button that opens a file chooser when clicked.
 * The selected file is stored in an AtomicReference<File> which is returned by the function.
 * @param parentPanel The panel to which the image chooser is added.
 * @return An AtomicReference<File> containing the selected image file.
 */
	private AtomicReference<File> createImageChooserPanel(JPanel parentPanel, Panier p) {
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
		parentPanel.add(imageButton);;
		return image;
	}

	/**
	 * This method creates a form for creating a new Panier.
	 * It includes text fields for the Panier details, and a submit button to create the Panier.
	 *
	 * @return A JPanel with the form for creating a new Panier.
	 */
	@Override
	public JPanel createFormPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(0, 2));
		JTextField nomField = new JTextField();
		JTextField prixField = new JTextField();
		panel.add(new JLabel("Nom *"));
		panel.add(nomField);
		panel.add(new JLabel("Prix *"));
		panel.add(prixField);
		AtomicReference<File> image = createImageChooserPanel( panel, null);
		JButton submitButton = new JButton("Modifier");
		panel.add(submitButton);
		submitButton.addActionListener(e -> {
			if (nomField.getText().isEmpty() || prixField.getText().isEmpty()) {
				Logger.error("Please fill all the required fields");
				return;
			}
			if (!prixField.getText().matches("\\d+.\\d\\d")) {
				Logger.error("Prix must be a number : 0.00");
				return;
			}
			String nom = nomField.getText();
			float prix = Float.parseFloat(prixField.getText());
			Panier panier = new Panier(nom, prix, image.get(), jardin);
			Panier.create(panier);
			displayView(false);
		});

		return panel;
	}

	/**
	 * This method creates a form for editing an existing Panier.
	 * It includes text fields for the Panier details and buttons to submit the changes.
	 *
	 * @param panier The Panier object to be edited.
	 * @return A JPanel with the form for editing the Panier.
	 */
	public JPanel createDetailPanel(Panier panier) {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(0, 2));
		JTextField nomField = new JTextField();
		nomField.setText(panier.nom);
		JTextField prixField = new JTextField();
		prixField.setText(String.valueOf(panier.prix));
		panel.add(new JLabel("Nom *"));
		panel.add(nomField);
		panel.add(new JLabel("Prix *"));
		panel.add(prixField);
		AtomicReference<File> image = createImageChooserPanel( panel, panier);
		// produit
		JButton addProduitButton = new JButton("Add Produit");
		// list of produits in scrollable panel
		JLabel produitsLabel = new JLabel("Produits");
		panel.add(produitsLabel);
		JPanel listPanel = new JPanel();
		listPanel.setLayout(new GridLayout(0, 1));
		JScrollPane scrollPane = new JScrollPane(listPanel);
		scrollPane.setPreferredSize(new Dimension(300, 50));
		panel.add(scrollPane);
		listPanel.add(addProduitButton);
		addProduitButton.addActionListener(e -> {
			clear();
			JPanel formPanel = new JPanel();
			formPanel.setLayout(new GridLayout(0, 2));
			JComboBox<Produit> produitField = new JComboBox<>();
			for (Produit produit : Produit.produits) produitField.addItem(produit);
			formPanel.add(new JLabel("Produit"));
			formPanel.add(produitField);
			JTextField quantiteField = new JTextField();
			formPanel.add(new JLabel("Quantite"));
			formPanel.add(quantiteField);
			JButton submitButton = new JButton("Modifier");
			formPanel.add(submitButton);
			submitButton.addActionListener(e1 -> {
				if (quantiteField.getText().isEmpty()) {
					Logger.error("Please fill all the required fields");
					return;
				}
				int quantite = Integer.parseInt(quantiteField.getText());
				Produit produit = Produit.produits.get(produitField.getSelectedIndex());
				panier.addProduit(new Panier.ProduitE(produit, quantite));
				panier.loadFromDatabase();
				displayView(false);
			});
			mainPanel.add(formPanel);
			refresh();
		});
		for (Panier.ProduitE produitPanier : panier.produits) {
			JPanel produitPanel = new JPanel();
			produitPanel.setLayout(new GridLayout(1, 3));
			JLabel produitLabel = new JLabel(produitPanier.toString());
			JButton deleteButton = new JButton("Supprimer");
			deleteButton.addActionListener(e -> {
				panier.deleteProduit(produitPanier.produit);
				panier.loadFromDatabase();
				displayView(false);
			});
			produitPanel.add(produitLabel);
			produitPanel.add(deleteButton);
			listPanel.add(produitPanel);
		}
		JButton submitButton = new JButton("Modifier");
		panel.add(submitButton);
		submitButton.addActionListener(e -> {
			if (nomField.getText().isEmpty() || prixField.getText().isEmpty()) {
				Logger.error("Please fill all the required fields");
				return;
			}
			String nom = nomField.getText();
			float prix = Float.parseFloat(prixField.getText());
			panier.nom = nom;
			panier.prix = prix;
			panier.image = image.get();
			panier.jardin = jardin;
			Panier.update(panier);
			displayView(false);
		});
		return panel;
	}
}
