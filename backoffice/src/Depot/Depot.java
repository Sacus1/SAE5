package Depot;
import Main.Main;
import Main.SQL;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import Main.Logger;

import javax.swing.*;

public class Depot extends JPanel {
	static final String[] fields = {"Adresse id", "Referent.Referent id", "Nom", "Telephone", "Presentation", "Image path",
					"Commentaire", "Mail", "Website"};
	static final String[] dbFields = {"Adresse_idAdresse", "Referent_idReferent", "nom", "telephone", "presentation", "imagePath",
					"commentaire", "mail", "website"};
	static final ArrayList<String> requiredFieldsList = new ArrayList<>(Arrays.asList("Adresse id", "Referent.Referent id",
					"Nom", "Telephone"));
	int id,Adresse_idAdresse, Referent_idReferent;
	boolean isArchived;
	String nom,telephone,presentation,imagePath, commentaire,mail,website;
	JourSemaine[] jourLivraison;
	static ArrayList<Depot> depots = new ArrayList<>();

	public Depot(int id, int Adresse_idAdresse, int Referent_idReferent, String nom, String telephone, String presentation,
	             String imagePath, String commentaire, String mail, String website) {
		this.id = id;
		this.Adresse_idAdresse = Adresse_idAdresse;
		this.Referent_idReferent = Referent_idReferent;
		this.nom = nom;
		this.telephone = telephone;
		this.presentation = presentation;
		this.imagePath = imagePath;
		this.commentaire = commentaire;
		this.mail = mail;
		this.website = website;
		this.isArchived = false;
		depots.add(this);
	}

	public Depot(int Adresse_idAdresse, int Referent_idReferent, String nom, String telephone, String presentation,
	             String imagePath, String commentaire, String mail, String website) {
		this.id = depots.size();
		this.Adresse_idAdresse = Adresse_idAdresse;
		this.Referent_idReferent = Referent_idReferent;
		this.nom = nom;
		this.telephone = telephone;
		this.presentation = presentation;
		this.imagePath = imagePath;
		this.commentaire = commentaire;
		this.mail = mail;
		this.website = website;
		this.isArchived = false;
		depots.add(this);
	}
	public static void getFromDatabase() {
		depots.clear();
		SQL sql = Main.sql;
		try {
			ResultSet res = sql.select("SELECT * FROM Depot");
			while (res.next()) {
				int id = res.getInt("idDepot");
				int addressId = res.getInt("Adresse_idAdresse");
				int referentId = res.getInt("Referent_idReferent");
				String name = res.getString("nom");
				String telephone = res.getString("telephone");
				String presentation = res.getString("presentation");
				String imagePath = res.getString("imagePath");
				String comment = res.getString("commentaire");
				String mail = res.getString("mail");
				String website = res.getString("website");
				Depot d = new Depot(id,addressId, referentId, name, telephone, presentation, imagePath, comment, mail, website);
				ResultSet res2 = sql.select("SELECT JourSemaine.nom\n" +
								"FROM JourSemaine\n" +
								"JOIN Depot_has_JourSemaine ON JourSemaine.idJourSemaine = Depot_has_JourSemaine.JourSemaine_idJourSemaine\n" +
								"JOIN Depot ON Depot.idDepot = Depot_has_JourSemaine.Depot_idDepot\n" +
								"WHERE Depot.idDepot = " + d.id + ";");
				ArrayList<JourSemaine> joursLivraisons = new ArrayList<>();
				while (res2.next()) joursLivraisons.add(JourSemaine.valueOf(res2.getString("nom")));
				d.jourLivraison = joursLivraisons.toArray(new JourSemaine[0]);
			}

		} catch (Exception e) {
			System.err.println("Main.SQL Exception : " + e.getMessage());
		}

	}

	static void update(Depot depot) {
		StringBuilder query = new StringBuilder("UPDATE Depot SET ");
		query.append("idDepot, ");
		for (int i = 0; i < dbFields.length; i++) {
			query.append(dbFields[i]);
			if (i != dbFields.length - 1) query.append(", ");
		}
		query.append(") VALUES (");
		query.append(depot.id).append(", ");
		query.append(depot.Adresse_idAdresse).append(", ");
		query.append(depot.Referent_idReferent).append(", ");
		query.append("'").append(depot.nom).append("', ");
		query.append("'").append(depot.telephone).append("', ");
		query.append("'").append(depot.presentation).append("', ");
		query.append("'").append(depot.imagePath).append("', ");
		query.append("'").append(depot.commentaire).append("', ");
		query.append("'").append(depot.mail).append("', ");
		query.append("'").append(depot.website).append("');");
		if (Main.sql.executeUpdate(query.toString()))
			Logger.error("Can't create depot");

		// update jourLivraison
		query = new StringBuilder("DELETE FROM Depot_has_JourSemaine WHERE `Depot_idDepot` = " + depot.id + ";");
		if (Main.sql.executeUpdate(query.toString()))
			Logger.error("Can't create depot");
		for (JourSemaine jourSemaine : depot.jourLivraison) {
			query = new StringBuilder("INSERT INTO Depot_has_JourSemaine (Depot_idDepot, JourSemaine_idJourSemaine) VALUES (");
			query.append(depot.id).append(", ");
			query.append(jourSemaine.ordinal()).append(");");
			if (Main.sql.executeUpdate(query.toString()))
				Logger.error("Can't create depot");
		}


		if (Main.sql.executeUpdate(query.toString()))
			Logger.error("Can't create depot");
	}
	static void create(Depot depot) {
		StringBuilder query = new StringBuilder("INSERT INTO Depot (");
		query.append("idDepot, ");
		for (int i = 0; i < dbFields.length; i++) {
			query.append(dbFields[i]);
			if (i != dbFields.length - 1) query.append(", ");
		}
		query.append(") VALUES (");
		query.append(depot.id).append(", ");
		query.append(depot.Adresse_idAdresse).append(", ");
		query.append(depot.Referent_idReferent).append(", ");
		query.append("'").append(depot.nom).append("', ");
		query.append("'").append(depot.telephone).append("', ");
		query.append("'").append(depot.presentation).append("', ");
		query.append("'").append(depot.imagePath).append("', ");
		query.append("'").append(depot.commentaire).append("', ");
		query.append("'").append(depot.mail).append("', ");
		query.append("'").append(depot.website).append("');");
		if (Main.sql.executeUpdate(query.toString()))
			Logger.error("Can't create depot");

		for (JourSemaine jourSemaine : depot.jourLivraison) {
			query = new StringBuilder("INSERT INTO Depot_has_JourSemaine (Depot_idDepot, JourSemaine_idJourSemaine) VALUES (");
			query.append(depot.id).append(", ");
			query.append(jourSemaine.ordinal()).append(");");
			if (Main.sql.executeUpdate(query.toString()))
				Logger.error("Can't create depot");
		}
	}

	public void delete() {
		String query = "DELETE FROM Depot WHERE `idDepot` = " + this.id + ";";
		if (Main.sql.executeUpdate(query))
			Logger.error("Can't delete depot");
		String query2 = "DELETE FROM Depot_has_JourSemaine WHERE `Depot_idDepot` = " + this.id + ";";
		if (Main.sql.executeUpdate(query2))
			Logger.error("Can't delete depot");
		depots.remove(this);
	}

	void archive() {
		String query = "UPDATE Depot SET `estArchive` = " + (this.isArchived?0:1) + " WHERE `idDepot` = " + this.id + ";";
		if (Main.sql.executeUpdate(query))
			Logger.error("Can't archive depot");
		this.isArchived = !this.isArchived;
	}
}
