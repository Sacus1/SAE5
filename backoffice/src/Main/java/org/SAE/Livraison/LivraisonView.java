package org.SAE.Livraison;

import org.SAE.Main.BaseView;

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
		JLabel label = new JLabel("<html>" + String.valueOf(l).replace("\n", "<br>") + "</html>", SwingConstants.CENTER);
		panelLabel.add(label);
		label.setHorizontalAlignment(JLabel.CENTER);
		// buttons to change etat
		JButton button = new JButton("En cours");
		button.addActionListener(e -> {
			l.etat = Etat.EN_COURS;
			Livraison.update(l);
			displayView(false);
		});
		panelButton.add(button);
		button = new JButton("Livré");
		button.addActionListener(e -> {
			l.etat = Etat.LIVRE;
			Livraison.update(l);
			displayView(false);
		});
		panelButton.add(button);
		button = new JButton("Annulé");
		button.addActionListener(e -> {
			l.etat = Etat.ANNULE;
			Livraison.update(l);
			displayView(false);
		});
		panelButton.add(button);
		button = new JButton("En attente");
		button.addActionListener(e -> {
			l.etat = Etat.EN_ATTENTE;
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
		return (ArrayList) Livraison.livraisons;
	}

	@Override
	protected JPanel createFormPanel() {
		return null;
	}

	@Override
	protected JPanel createDetailPanel(Livraison object) {
		return null;
	}

}
