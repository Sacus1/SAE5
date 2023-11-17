package Adresse;

import Main.Main;
import Main.SQL;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Adresse {
	public int id;
	String adresse,ville,codePostal;

	public static ArrayList<Adresse> adresses = new ArrayList<>();

	public Adresse(int id, String adresse, String ville, String codePostal) {
		this.id = id;
		this.adresse = adresse;
		this.ville = ville;
		this.codePostal = codePostal;
		adresses.add(this);
	}

	public Adresse(String adresse, String ville, String codePostal) {
		this.id = adresses.size();
		this.adresse = adresse;
		this.ville = ville;
		this.codePostal = codePostal;
		adresses.add(this);
	}

	public static void update(Adresse adresse) {
		if (!(Main.sql.updatePreparedStatement("Adresse", new String[]{"adresse", "ville", "codePostal"},
						new Object[]{adresse.adresse, adresse.ville, adresse.codePostal},
						new String[]{"idAdresse = " + adresse.id}))) System.out.println("Update failed");
		getFromDatabase();
	}

	static void delete(Adresse adresse) {
		if (!Main.sql.deletePrepareStatement("Adresse", new String[]{"idAdresse = " + adresse.id}))
			System.out.println("Delete failed");
		adresses.remove(adresse);
	}

	public String toString() {
		return adresse + ", " + ville;
	}

	public static void getFromDatabase() {
		SQL sql = Main.sql;
		adresses.clear();
		try {
			ResultSet res = sql.select("Adresse");
			while (res.next()) {
				int id = res.getInt("idAdresse");
				String adresse = res.getString("adresse");
				String ville = res.getString("ville");
				String codePostal = res.getString("codePostal");
				new Adresse(id, adresse, ville, codePostal);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
	public static void create(Adresse adresse) {
		if (!Main.sql.createPrepareStatement("Adresse", new String[]{"idAdresse", "adresse", "ville", "codePostal"},
						new Object[]{adresse.id, adresse.adresse, adresse.ville, adresse.codePostal}))
			System.out.println("Create failed");
		getFromDatabase();
	}



}
