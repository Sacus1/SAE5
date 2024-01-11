package org.SAE.Main;

import org.SAE.Abonnement.Abonnement;
import org.SAE.Adresse.Adresse;
import org.SAE.Client.Client;
import org.SAE.Depot.Depot;
import org.SAE.Depot.DepotView;
import org.SAE.Depot.PeriodeNonLivrable;
import org.SAE.Jardin.Jardin;
import org.SAE.Livraison.Livraison;
import org.SAE.Panier.Panier;
import org.SAE.Produit.Produit;
import org.SAE.Referent.Referent;
import org.SAE.Tournee.Tournee;
import org.SAE.Unite.Unite;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.Arrays;
import java.util.Comparator;

public class Main {
	public static SQL sql ;
	public static JFrame frame;
	static final String URL = "jdbc:mysql://localhost:3306/SAE";
	private static JPanel mainPanel;
	private static void resetSelectedButton(JButton[] buttons) {
		for (JButton JButton : buttons) JButton.setBackground(null);
	}
	public static void main(String[] args) {
		// get environment variables DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD
		// if they are not set, use default values
		String url = System.getenv("DATABASE_URL");
		if (url == null) url = Main.URL;
		String username = System.getenv("DATABASE_USERNAME");
		if (username == null) username = "root";
		String password = System.getenv("DATABASE_PASSWORD");
		sql = new SQL(url, username, password);
		// load all data from the database
		frame = new JFrame("Gestion");
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int windowWidth = (int) (screenSize.getWidth() * 0.8);
		int windowHeight = (int) (screenSize.getHeight() * 0.8);
		frame.setSize(windowWidth, windowHeight);
		// maximize the frame
		frame.setExtendedState(Frame.MAXIMIZED_BOTH);
		mainPanel = new JPanel();
		// show loading screen
		frame.add(mainPanel, "Center");
		frame.setVisible(true);
		loadAllData();
		// create a list of buttons on the side of the frame.
		JPanel leftPanel = new JPanel();
		// add buttons
		JButton[] buttons = {new JButton("Depot"), new JButton("Referent"), new JButton("Adresse"),new JButton("Unité"),
						new JButton("Abonnement"),new JButton("Jardin"),new JButton("Client"),new JButton("Produit"),new JButton(
										"Tournée"),new JButton("Livraison")};
		Arrays.sort(buttons, Comparator.comparing(JButton::getText));
		leftPanel.setLayout(new GridLayout(buttons.length, 1));
		for (JButton JButton : buttons) {
			JButton.addActionListener(e -> {
				// clear the frame
				mainPanel.removeAll();
				// add the view to the frame
				switch (JButton.getText()) {
					case "Depot":
						mainPanel.add(new DepotView());
						resetSelectedButton(buttons);
						JButton.setBackground(Color.LIGHT_GRAY);
						break;
					case "Referent":
						mainPanel.add(new org.SAE.Referent.ReferentView());
						resetSelectedButton(buttons);
						JButton.setBackground(Color.LIGHT_GRAY);
						break;
					case "Adresse":
						mainPanel.add(new org.SAE.Adresse.AdresseView());
						resetSelectedButton(buttons);
						JButton.setBackground(Color.LIGHT_GRAY);
						break;
					case "Unité":
						mainPanel.add(new org.SAE.Unite.UniteView());
						resetSelectedButton(buttons);
						JButton.setBackground(Color.LIGHT_GRAY);
						break;
					case "Produit":
						mainPanel.add(new org.SAE.Produit.ProduitView());
						resetSelectedButton(buttons);
						JButton.setBackground(Color.LIGHT_GRAY);
						break;
					case "Abonnement":
						mainPanel.add(new org.SAE.Abonnement.AbonnementView());
						resetSelectedButton(buttons);
						JButton.setBackground(Color.LIGHT_GRAY);
						break;
					case "Jardin":
						mainPanel.add(new org.SAE.Jardin.JardinView());
						resetSelectedButton(buttons);
						JButton.setBackground(Color.LIGHT_GRAY);
						break;
					case "Client":
						mainPanel.add(new org.SAE.Client.ClientView());
						resetSelectedButton(buttons);
						JButton.setBackground(Color.LIGHT_GRAY);
						break;
					case "Tournée":
						mainPanel.add(new org.SAE.Tournee.TourneeView());
						resetSelectedButton(buttons);
						JButton.setBackground(Color.LIGHT_GRAY);
						break;
					case "Livraison":
						mainPanel.add(new org.SAE.Livraison.LivraisonView());
						resetSelectedButton(buttons);
						JButton.setBackground(Color.LIGHT_GRAY);
						break;
					default:
						break;
				}
				// refresh the frame
				frame.revalidate();
				frame.repaint();
			});
			// change the size of the button to fit the frame
			leftPanel.add(JButton);
		}
		frame.add(leftPanel, "West");
		// refresh button
		JButton refreshButton = new JButton("Refresh");
		refreshButton.addActionListener(e -> loadAllData());
		frame.add(refreshButton, "South");
		frame.setVisible(true);
		// when the frame is closed, close the sql connection
		frame.addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosing(java.awt.event.WindowEvent windowEvent) {
				sql.close();
			}
		});
	}
	private static void loadAllData() {
		JLabel loadingLabel = new JLabel("Loading...");
		mainPanel.add(loadingLabel);
		Unite.getFromDatabase();
		Produit.getFromDatabase();
		Adresse.getFromDatabase();
		Referent.getFromDatabase();
		Jardin.getFromDatabase();
		Panier.getFromDatabase();
		Client.getFromDatabase();
		Depot.getFromDatabase();
		PeriodeNonLivrable.getFromDatabase();
		Abonnement.getFromDatabase();
		Tournee.getFromDatabase();
		Livraison.getFromDatabase();
		mainPanel.remove(loadingLabel);
	}

	/**
	 * Converts an InputStream to an image file.
	 *
	 * @param inputStream The InputStream to convert.
	 * @return The image file.
	 * @throws IOException If an I/O error occurs.
	 */
	public static File convertInputStreamToImage(InputStream inputStream) throws IOException {
		if (inputStream == null) return null;

    File tempFile = File.createTempFile("image", ".png");
    tempFile.deleteOnExit();
    try (OutputStream out = new FileOutputStream(tempFile)) {
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) != -1) out.write(buffer, 0, length);
    }
    return tempFile;
	}

	public static String[] addStringToArray(String[] tableColumns, String column) {
		String[] newTableColumns = new String[tableColumns.length + 1];
		System.arraycopy(tableColumns, 0, newTableColumns, 0, tableColumns.length);
		newTableColumns[tableColumns.length] = column;
		return newTableColumns;
	}

}
