package org.SAE.Referent;

import org.SAE.Main.Base;
import org.SAE.Main.Main;
import org.SAE.Main.SQL;
import org.SAE.Main.Logger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Referent extends Base {
	private static final String TABLE_NAME = "Referent" ;
	public int id;
	String nom;
	String telephone;
	String mail;
	public static final List<Referent> referents = new ArrayList<>();

	public Referent(int id, String nom, String telephone, String mail) {
		this.id = id;
		this.nom = nom;
		this.telephone = telephone;
		this.mail = mail;
		referents.add(this);
	}

	public Referent(String nom, String telephone, String mail) {
		this.nom = nom;
		this.telephone = telephone;
		this.mail = mail;
		referents.add(this);
	}

	public String toString() {
		return nom;
	}

	public static void getFromDatabase() {
		SQL sql = Main.sql;
		referents.clear();
		ResultSet res = sql.select(TABLE_NAME);
		try {
			if (res == null) return;
			while (res.next())
			{
				int id = res.getInt("idReferent");
				String nom = res.getString("nom");
				String telephone = res.getString("telephone");
				String mail = res.getString("mail");
				new Referent(id, nom, telephone, mail);
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

	}
	@Override
	public void loadFromDatabase(){
		getFromDatabase();
	}

	public static void update(Referent referent) {
		if (!Main.sql.updatePreparedStatement(TABLE_NAME, new String[]{"nom", "telephone", "mail"},
						new Object[]{referent.nom, referent.telephone, referent.mail},
						new String[]{"idReferent = "+referent.id}))
			Logger.error("Failed to update Referent");
		getFromDatabase();

	}

	protected void delete() {
		if (Main.sql.deletePrepareStatement(TABLE_NAME, new String[]{"idReferent = " + id}))
			Logger.error("Failed to delete Referent");
		referents.remove(this);
	}

	public static void create(Referent referent) {
		if (!Main.sql.createPrepareStatement(TABLE_NAME, new String[]{"nom", "telephone", "mail"},
						new Object[]{referent.nom, referent.telephone, referent.mail}))
			Logger.error("Failed to create Referent");
		getFromDatabase();
	}

}
