package Depot;

import Main.Main;
import Main.SQL;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;

import Main.Logger;

import javax.swing.*;

public class Depot extends JPanel {
	static final String[] fields = {"Adresse id", "Referent.Referent id", "Nom", "Telephone", "Presentation",
					"Commentaire", "Mail", "Website"};
	static final String[] dbFields = {"Adresse_idAdresse", "Referent_idReferent", "nom", "telephone", "presentation",
					"commentaire", "mail", "website"};
	static final ArrayList<String> requiredFieldsList = new ArrayList<>(Arrays.asList("Adresse id", "Referent.Referent id",
					"Nom", "Telephone"));
	int id, Adresse_idAdresse, Referent_idReferent;
	boolean isArchived;
	String nom, telephone, presentation, commentaire, mail, website;
	File image;
	JourSemaine[] jourLivraison;
	static ArrayList<Depot> depots = new ArrayList<>();

	public Depot(int id, int Adresse_idAdresse, int Referent_idReferent, String nom, String telephone, String presentation,
	             String commentaire, String mail, String website, File image) {
		this.id = id;
		this.Adresse_idAdresse = Adresse_idAdresse;
		this.Referent_idReferent = Referent_idReferent;
		this.nom = nom;
		this.telephone = telephone;
		this.presentation = presentation;
		this.commentaire = commentaire;
		this.mail = mail;
		this.website = website;
		this.isArchived = false;
		this.image = image;
		depots.add(this);
	}

	public Depot(int Adresse_idAdresse, int Referent_idReferent, String nom, String telephone, String presentation,
	             String commentaire, String mail, String website, File image) {
		this.id = depots.size();
		this.Adresse_idAdresse = Adresse_idAdresse;
		this.Referent_idReferent = Referent_idReferent;
		this.nom = nom;
		this.telephone = telephone;
		this.presentation = presentation;
		this.commentaire = commentaire;
		this.mail = mail;
		this.website = website;
		this.isArchived = false;
		this.image = image;
		depots.add(this);
	}

	public static void getFromDatabase() {
		depots.clear();
		SQL sql = Main.sql;
		try {
			ResultSet res = sql.select("Depot");
			while (res.next()) {
				int id = res.getInt("idDepot");
				int addressId = res.getInt("Adresse_idAdresse");
				int referentId = res.getInt("Referent_idReferent");
				String name = res.getString("nom");
				String telephone = res.getString("telephone");
				String presentation = res.getString("presentation");
				String comment = res.getString("commentaire");
				String mail = res.getString("mail");
				String website = res.getString("website");
				InputStream imageStream = res.getBinaryStream("image");
				File image = Main.convertInputStreamToImage(imageStream);
				Depot d = new Depot(id, addressId, referentId, name, telephone, presentation, comment, mail, website, image);
				String query = "SELECT * FROM JourSemaine JOIN Depot_has_JourSemaine ON JourSemaine.idJourSemaine = Depot_has_JourSemaine.JourSemaine_idJourSemaine JOIN Depot ON Depot.idDepot = Depot_has_JourSemaine.Depot_idDepot WHERE Depot.idDepot = " + d.id + ";";
				ResultSet res2 = sql.selectRaw(query);
				ArrayList<JourSemaine> joursLivraisons = new ArrayList<>();
				while (res2.next()) joursLivraisons.add(JourSemaine.valueOf(res2.getString("nom")));
				d.jourLivraison = joursLivraisons.toArray(new JourSemaine[0]);
			}

		} catch (Exception e) {
			System.err.println("Main.SQL Exception : " + e.getMessage());
		}

	}

	private static void insertDeliveryDays(Depot depot) {
		for (JourSemaine jourSemaine : depot.jourLivraison) {
			if (Main.sql.createPrepareStatement("JourSemaine_idJourSemaine", new String[]{"Depot_idDepot"},
							new Object[]{jourSemaine.ordinal(), depot.id}))
				Logger.error("Can't create depot");
		}
	}

	static void update(Depot depot) {
		Main.sql.updatePreparedStatement("Depot", new String[]{
										"Adresse_idAdresse", "Referent_idReferent", "nom", "telephone", "presentation",
										"commentaire", "mail", "website","image"},
						new Object[]{depot.Adresse_idAdresse, depot.Referent_idReferent, depot.nom, depot.telephone,
										depot.presentation, depot.commentaire, depot.mail, depot.website,depot.image},
						new String[]{"idDepot = " + depot.id});
		// update jourLivraison
		if (Main.sql.deletePrepareStatement("Depot_has_JourSemaine", new String[]{"Depot_idDepot = " + depot.id}))
			Logger.error("Failed to update depot");
		insertDeliveryDays(depot);
	}

	static void create(Depot depot) {
		Main.sql.createPrepareStatement("Depot", new String[]{"idDepot",
										"Adresse_idAdresse", "Referent_idReferent", "nom", "telephone", "presentation",
										"commentaire", "mail", "website","image"},
						new Object[]{depot.id,depot.Adresse_idAdresse, depot.Referent_idReferent, depot.nom, depot.telephone,
										depot.presentation, depot.commentaire, depot.mail, depot.website,depot.image});
		insertDeliveryDays(depot);
	}

	public void delete() {
		if (Main.sql.deletePrepareStatement("Depot", new String[]{"idDepot = " + this.id}))
			Logger.error("Can't delete depot");
		if (Main.sql.deletePrepareStatement("Depot_has_JourSemaine", new String[]{"Depot_idDepot = " + this.id}))
			Logger.error("Can't delete depot");
		depots.remove(this);
	}

	void archive() {
		if (Main.sql.updatePreparedStatement("Depot", new String[]{"estArchive"}, new Object[]{this.isArchived ? 0 : 1},
						new String[]{"idDepot = " + this.id}))
			Logger.error("Can't archive depot");
		this.isArchived = !this.isArchived;
	}
}
