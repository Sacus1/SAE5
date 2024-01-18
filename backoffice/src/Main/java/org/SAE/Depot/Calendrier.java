package org.SAE.Depot;

import javax.swing.*;

public class Calendrier extends javax.swing.JPanel {
	public Object[][] data;
	public String[] columnNames = new String[12];
	int frequency = 15;
	int year = 2023;
	String depotName;

	public Calendrier(Object[][] data, String depotName) {
		this.data = data;
		this.depotName = depotName;
		columnNames[0] = "Janvier";
		columnNames[1] = "Février";
		columnNames[2] = "Mars";
		columnNames[3] = "Avril";
		columnNames[4] = "Mai";
		columnNames[5] = "Juin";
		columnNames[6] = "Juillet";
		columnNames[7] = "Août";
		columnNames[8] = "Septembre";
		columnNames[9] = "Octobre";
		columnNames[10] = "Novembre";
		columnNames[11] = "Décembre";
		System.arraycopy(columnNames, 0, data[0], 0, 12);
		initComponents();
	}



	private void initComponents() {
		setLayout(new java.awt.BorderLayout());
		JLabel label = new JLabel("Planning + " + year + " de livraison des paniers tous les " + frequency + " jours au " + depotName);
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setPreferredSize(new java.awt.Dimension(200, 100));
		add(label, java.awt.BorderLayout.NORTH);
		javax.swing.JPanel panel = new javax.swing.JPanel();
		panel.setLayout(new java.awt.GridLayout(1, 1));
		add(panel, java.awt.BorderLayout.CENTER);
		javax.swing.JTable table = new javax.swing.JTable();
		table.setModel(new javax.swing.table.DefaultTableModel(
						data,
						columnNames
		));
		panel.add(table);
		JLabel label2 = new JLabel("Ce planning est établi par défaut.\n" +
						"Si ces dates ne vous conviennent pas, nous pouvons, à tout moment, procéder à des modifications");
		label2.setHorizontalAlignment(SwingConstants.CENTER);
		label2.setPreferredSize(new java.awt.Dimension(200, 100));
		add(label2, java.awt.BorderLayout.SOUTH);
		table.setRowHeight(50);
		table.setRowSelectionAllowed(false);
		table.setCellSelectionEnabled(true);
		table.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {
			@Override
			public java.awt.Component getTableCellRendererComponent(javax.swing.JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
				final java.awt.Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
				// center the text
				((javax.swing.JLabel) c).setHorizontalAlignment(SwingConstants.CENTER);
				if (row == 0) {
					((javax.swing.JComponent) c).setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 2, 1,
									java.awt.Color.BLACK));
					c.setBackground(java.awt.Color.LIGHT_GRAY);
				} else {
					((javax.swing.JComponent) c).setBorder(javax.swing.BorderFactory.createMatteBorder(0, 1, 0, 1,
									java.awt.Color.BLACK));

					c.setBackground(java.awt.Color.WHITE);
				}
				if (row == data.length - 1)
					((javax.swing.JComponent) c).setBorder(javax.swing.BorderFactory.createMatteBorder(0, 1, 1, 1,
									java.awt.Color.BLACK));
				return c;
			}
		});
	}

}
