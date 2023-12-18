package org.SAE.Main;

import javax.swing.*;
import java.awt.*;

public class Button extends JButton {
		public Button(String text) {
				super(text);
			setFont(new Font("Arial", Font.BOLD, 16));  // Set font
			setForeground(Color.WHITE);                // Set text color
			setBackground(new Color(100, 149, 237));   // Set background color
			setBorder(BorderFactory.createRaisedBevelBorder()); // Set border
		}

}
