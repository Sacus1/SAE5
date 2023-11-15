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
		SQL sql = Main.sql;
		String query = "UPDATE Adresse SET " + "adresse = '" + adresse.adresse.replace("'", "''") + "', " +
						"ville = '" + adresse.ville.replace("'", "''") + "', " +
						"codePostal = '" + adresse.codePostal + "' " +
						"WHERE idAdresse = " + adresse.id + ";";

		sql.executeUpdate(query);
		getFromDatabase();
	}

	static void delete(Adresse adresse) {
		SQL sql = Main.sql;
		sql.executeUpdate("DELETE FROM Adresse WHERE idAdresse = " + adresse.id + ";");
		getFromDatabase();
	}

	public String toString() {
		return adresse + ", " + ville;
	}

	public static void getFromDatabase() {
		SQL sql = Main.sql;
		adresses.clear();
		try {
			ResultSet res = sql.select("SELECT * FROM Adresse");
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
		SQL sql = Main.sql;
		System.out.println(adresse.id + " " + adresse.adresse + " " + adresse.ville + " " + adresse.codePostal);
		String query = "INSERT INTO Adresse (idAdresse, adresse, ville, codePostal) VALUES ('" + adresse.id + "', '" +
						adresse.adresse.replace("'", "''") + "', '" +
						adresse.ville + "', '" +
						adresse.codePostal + "');";
		sql.executeUpdate(query);
		getFromDatabase();
	}



}
