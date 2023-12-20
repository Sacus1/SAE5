package org.SAE.Unite;

import org.SAE.Main.Base;
import org.SAE.Main.Logger;
import org.SAE.Main.Main;
import org.SAE.Main.SQL;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
public class Unite extends Base {
	private static final String TABLE_NAME = "Unite" ;
	public final int id;
	String nom;
	public static final List<Unite> unites = new ArrayList<>();
	public Unite(int id, String nom) {
		this.id = id;
		this.nom = nom;
		unites.add(this);
	}
	public Unite(String nom) {
		this.id = Main.sql.getNextId(TABLE_NAME);;
		this.nom = nom;
		unites.add(this);
	}
	public String toString() {
		return nom;
	}
	public static void getFromDatabase() {
		unites.clear();
		SQL sql = Main.sql;
		try {
			ResultSet res = sql.select(TABLE_NAME);
			if (res == null) return;
			while (res.next()) {
				int id = res.getInt("idUnite");
				String nom = res.getString("nom");
				new Unite(id, nom);
			}
		} catch (Exception e) {
			Logger.error(String.valueOf(e));
		}
	}
	public static void update(Unite unite) {
		if (!Main.sql.updatePreparedStatement(TABLE_NAME, new String[]{"idUnite", "nom"},
						new Object[]{unite.id, unite.nom},
						new String[]{"idUnite = " +unite.id})) Logger.error("Failed to update Unite");
		getFromDatabase();
	}
	public static void create(Unite unite) {
		if (Main.sql.createPrepareStatement(TABLE_NAME, new String[]{"idUnite", "nom"},
						new Object[]{unite.id, unite.nom}))
			Logger.error("Failed to insert Unite");
		getFromDatabase();
	}
	protected void delete() {
		if (Main.sql.deletePrepareStatement(TABLE_NAME, new String[]{"idUnite = " + id}))
			Logger.error("Failed to delete Unite");
		unites.remove(this);
	}
	@Override
	public void loadFromDatabase(){
		getFromDatabase();
	}

}
