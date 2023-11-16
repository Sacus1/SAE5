package Main;

import javax.swing.*;
import java.awt.*;

public abstract class BaseView extends JPanel {
	public JButton createButton = new JButton("Create");
	public boolean inCreation = false;
	protected static JPanel mainPanel, topPanel, bottomPanel;

	public BaseView() {
		setLayout(new BorderLayout());
		initializePanels();
		setupCreateButton();
		draw(false);
	}

	private void initializePanels() {
		topPanel = new JPanel();
		bottomPanel = new JPanel();
		mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		add(topPanel, "North");
		add(mainPanel, "Center");
		add(bottomPanel, "South");
	}

	private void setupCreateButton() {
		topPanel.add(createButton);
		createButton.addActionListener(e -> draw(!inCreation));
	}

	public abstract void draw(boolean isCreate);

	public static void clear() {
		mainPanel.removeAll();
	}

	public static void refresh() {
		mainPanel.revalidate();
		mainPanel.repaint();
	}
}
