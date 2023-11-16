package Main;
import Depot.DepotView;

import javax.swing.*;
import java.awt.*;

public class Main {
	public static SQL sql ;
	static String url = "jdbc:mysql://localhost:3306/SAE";
	private static void resetSelectedButton(JButton[] buttons) {
		for (JButton button : buttons) button.setBackground(null);
	}
	public static void main(String[] args) {
		sql = new SQL(url,"root","");
		DepotView depotView = new DepotView();
		JFrame frame = new JFrame("Gestion");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(800,600);
		JPanel mainPanel = new JPanel();
		// create a list of buttons on the side of the frame
		JPanel leftPanel = new JPanel();
		// add buttons
		JButton[] buttons = {new JButton("Depot"), new JButton("Referent"), new JButton("Adresse"),new JButton("")};
		leftPanel.setLayout(new GridLayout(buttons.length, 1));
		for (JButton button : buttons) {
			button.addActionListener(e -> {
				// clear the frame
				mainPanel.removeAll();
				// add the view to the frame
				switch (button.getText()) {
					case "Depot":
						mainPanel.add(depotView);
						resetSelectedButton(buttons);
						button.setBackground(Color.LIGHT_GRAY);
						break;
					case "Referent":
						mainPanel.add(new Referent.ReferentView());
						resetSelectedButton(buttons);
						button.setBackground(Color.LIGHT_GRAY);
						break;
					case "Adresse":
						mainPanel.add(new Adresse.AdresseView());
						resetSelectedButton(buttons);
						button.setBackground(Color.LIGHT_GRAY);
						break;
				}
				// refresh the frame
				frame.revalidate();
				frame.repaint();
			});
			// change the size of the button to fit the frame
			leftPanel.add(button);
		}
		frame.add(leftPanel, "West");
		frame.add(mainPanel, "Center");
		frame.setVisible(true);
		// when the frame is closed, close the sql connection
		frame.addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosing(java.awt.event.WindowEvent windowEvent) {
				sql.close();
			}
		});
	}
}
