package Depot;

import javax.swing.*;

public class DepotController {
	public DepotView depotView;

	public DepotController(DepotView depotView) {
		JButton createButton = depotView.createButton;
		createButton.addActionListener(e -> CreatePanel());
		this.depotView = depotView;
	}

	private void CreatePanel() {
		depotView.draw(!depotView.inCreation);
	}

}
