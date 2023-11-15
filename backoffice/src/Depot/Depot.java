package Depot;
import Main.Main;
import Main.SQL;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import Main.Logger;
public class Depot {
	static final String[] fields = {"Adresse id", "Referent.Referent id", "Nom", "Telephone", "Presentation", "Image path",
					"Commentaire", "Mail", "Website"};
	static final String[] dbFields = {"Adresse_idAdresse", "Referent_idReferent", "nom", "telephone", "presentation", "imagePath",
					"commentaire", "mail", "website"};
	static final ArrayList<String> requiredFieldsList = new ArrayList<>(Arrays.asList("Adresse id", "Referent.Referent id",
					"Nom", "Telephone"));
	int id, Adresse_idAdresse, Referent_idReferent;
	boolean isArchived;
	String nom,telephone,presentation,imagePath, commentaire,mail,website;
	JourSemaine jourLivraison;
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
	static void getFromDatabase() {
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
				while (res2.next()) d.jourLivraison = JourSemaine.valueOf(res2.getString("nom"));
			}

		} catch (Exception e) {
			System.err.println("Main.SQL Exception : " + e.getMessage());
		}

	}

	void update(String[] values) {
		for (int i = 0; i < values.length; i++)
			if (values[i] != null) switch (i) {
				case 0:
					this.Adresse_idAdresse = Integer.parseInt(values[i]);
					break;
				case 1:
					this.Referent_idReferent = Integer.parseInt(values[i]);
					break;
				case 2:
					this.nom = values[i];
					break;
				case 3:
					this.telephone = values[i];
					break;
				case 4:
					this.presentation = values[i];
					break;
				case 5:
					this.imagePath = values[i];
					break;
				case 6:
					this.commentaire = values[i];
					break;
				case 7:
					this.mail = values[i];
					break;
				case 8:
					this.website = values[i];
					break;
			}
		StringBuilder query = new StringBuilder("UPDATE Depot SET ");
		for (int i = 0; i < 9; i++) {
			query.append("`").append(dbFields[i]).append("` = ");
			if (values[i] != null) query.append("'").append(values[i]).append("'");
			else query.append("NULL");
			if (i < 8) query.append(", ");
			else query.append(" WHERE `idDepot` = ").append(this.id).append(";");
		}
		if (Main.sql.executeUpdate(query.toString()))
			Logger.error("Can't edit depot");

	}
	static void create(String[] values) {
		Depot depot = new Depot(Integer.parseInt(values[0]), Integer.parseInt(values[1]), values[2], values[3], values[4],
						values[5], values[6], values[7], values[8]);
		depot.jourLivraison = JourSemaine.valueOf(values[9]);
		StringBuilder query = new StringBuilder("INSERT INTO Depot (`idDepot`, ");
		for (int i = 0; i < 9; i++) {
			query.append("`").append(Depot.dbFields[i]).append("`");
			if (i < 8) query.append(", ");
			else query.append(") VALUES (");
		}
		query.append(depot.id).append(", ");
		for (int i = 0; i < 9; i++) {
			query.append("'").append(values[i]).append("'");
			if (i < 8)
				query.append(", ");
			else
				query.append(")");
		}
		query.append(";");
		if (Main.sql.executeUpdate(query.toString()))
			Logger.error("Can't create depot");

		//INSERT INTO Depot_has_JourSemaine (Depot_idDepot, JourSemaine_idJourSemaine) VALUES (ID_Depot, ID_JourSemaine);
		query = new StringBuilder("INSERT INTO Depot_has_JourSemaine (`Depot_idDepot`, `JourSemaine_idJourSemaine`) VALUES (");
		query.append(depot.id).append(", ").append(depot.jourLivraison.ordinal()).append(");");
		if (Main.sql.executeUpdate(query.toString()))
			Logger.error("Can't create depot");
	}

	void delete() {
		String query = "DELETE FROM Depot WHERE `idDepot` = " + this.id + ";";
		if (Main.sql.executeUpdate(query))
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
