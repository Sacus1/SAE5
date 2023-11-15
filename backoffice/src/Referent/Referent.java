package Referent;

import Main.Main;
import Main.SQL;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Referent {
	public int id;
	String nom, telephone, mail;
	public static ArrayList<Referent> referents = new ArrayList<>();

	public Referent(int id, String nom, String telephone, String mail) {
		this.id = id;
		this.nom = nom;
		this.telephone = telephone;
		this.mail = mail;
		referents.add(this);
	}

	public Referent(String nom, String telephone, String mail) {
		this.id = referents.size();
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
		ResultSet res = sql.select("SELECT * FROM Referent");
		try {
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

	public static void update(Referent referent) {
		SQL sql = Main.sql;
		sql.executeUpdate(
						"UPDATE Referent SET " +
										"idReferent = " + referent.id + ", " +
										"nom = '" + referent.nom + "', " +
										"telephone = '" + referent.telephone + "', " +
										"mail = '" + referent.mail + "' " +
										"WHERE idReferent = " + referent.id + ";"
		);
		getFromDatabase();
	}

	public static void delete(Referent referent) {
		SQL sql = Main.sql;
		sql.executeUpdate("DELETE FROM Referent WHERE idReferent = " + referent.id + ";");
		getFromDatabase();
	}

	public static void create(Referent referent) {
		SQL sql = Main.sql;
		sql.executeUpdate(
						"INSERT INTO Referent (idReferent, nom, telephone, mail) VALUES (" +
										"'" + referent.id + "', " +
										"'" + referent.nom + "', " +
										"'" + referent.telephone + "', " +
										"'" + referent.mail + "');"
		);
		getFromDatabase();
	}

}
