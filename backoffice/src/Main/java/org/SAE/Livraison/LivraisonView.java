package org.SAE.Livraison;

import org.SAE.Adresse.Adresse;
import org.SAE.Depot.Depot;
import org.SAE.Depot.PeriodeNonLivrable;
import org.SAE.Main.BaseView;
import org.SAE.Main.BaseView;
import org.SAE.Main.UButton;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class LivraisonView extends BaseView<Livraison> {
	public LivraisonView() {
		super("Livraison");
		add(topPanel, "North");
		add(mainPanel, "Center");
		add(bottomPanel, "South");
		displayView(false);
		topPanel.remove(createButton);
	}

	@Override
	protected JPanel createListPanel(Livraison l){
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new GridLayout(1, 1));
		JPanel panelLabel = new JPanel();
		panelLabel.setLayout(new GridLayout(3, 2));
		panelLabel.setBorder(BorderFactory.createLineBorder(Color.black));
		JPanel panelButton = new JPanel();
		panelButton.setBorder(BorderFactory.createLineBorder(Color.black));
		panelLabel.add(new JLabel("<html>"+String.valueOf(l).replace("\n", "<br>")+"</html>", SwingConstants.CENTER));
		// buttons to change etat
		UButton button = new UButton("En cours");
		button.addActionListener(e -> {
			l.etat = "En cours";
			Livraison.update(l);
			displayView(false);
		});
		panelButton.add(button);
		button = new UButton("Livré");
		button.addActionListener(e -> {
			l.etat = "Livré";
			Livraison.update(l);
			displayView(false);
		});
		panelButton.add(button);
		button = new UButton("Annulé");
		button.addActionListener(e -> {
			l.etat = "Annulé";
			Livraison.update(l);
			displayView(false);
		});
		panelButton.add(button);
		button = new UButton("En attente");
		button.addActionListener(e -> {
			l.etat = "En attente";
			Livraison.update(l);
			displayView(false);
		});
		panelButton.add(button);
		mainPanel.add(panelLabel);
		mainPanel.add(panelButton);
		return mainPanel;
	}
	@Override
	protected ArrayList getList() {
		return Livraison.livraisons;
	}

	@Override
	protected JPanel createFormPanel() {
		return null;
	}

	@Override
	protected JPanel createEditPanel(Livraison object) {
		return null;
	}

}
