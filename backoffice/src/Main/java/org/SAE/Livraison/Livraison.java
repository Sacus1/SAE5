package org.SAE.Livraison;

import org.SAE.Abonnement.Abonnement;
import org.SAE.Depot.Depot;
import org.SAE.Main.Base;
import org.SAE.Tournee.Tournee;

import java.sql.Date;
import java.sql.ResultSet;
import java.util.ArrayList;

import static org.SAE.Main.Main.sql;

public class Livraison extends Base {
	int id;
	Depot depot;
	Tournee tournee;
	public Abonnement abonnement;
	public Date date;
	/**
	 * etat peut prendre les valeurs suivantes : <br>
	 * "livre" : la livraison a été effectuée <br>
	 * "annule" : la livraison a été annulée <br>
	 * "en cours" : la livraison est en cours <br>
	 * "en attente" : la livraison est en attente
	 */
	public String etat;

	public static final String TABLE_NAME = "Livraison";
	public static ArrayList<Livraison> livraisons = new ArrayList<>();

	public Livraison(int id, Depot depot, Tournee tournee, Abonnement abonnement, Date date, String etat) {
		this.id = id;
		this.depot = depot;
		this.tournee = tournee;
		this.abonnement = abonnement;
		this.date = date;
		this.etat = etat;
	}

	public Livraison(Depot depot, Tournee tournee, Abonnement abonnement, Date date, String etat) {
		this.depot = depot;
		this.tournee = tournee;
		this.abonnement = abonnement;
		this.date = date;
		this.etat = etat;
	}

	public static void getFromDatabase() {
		livraisons.clear();
		try {
			ResultSet res = sql.select(TABLE_NAME);
			while (res.next()) {
				int id = res.getInt("idLivraison");
				int depotId = res.getInt("Depot_idDepot");
				Depot depot = Depot.getDepotById(depotId);
				int tourneeId = res.getInt("Tournee_idTournee");
				Tournee tournee = Tournee.getTourneeById(tourneeId);
				int abonnementId = res.getInt("Abonnement_idAbonnement");
				Abonnement abonnement = Abonnement.getAbonnementById(abonnementId);
				Date date = res.getDate("semaine");
				String etat = res.getString("etat");
				livraisons.add(new Livraison(id, depot, tournee, abonnement, date, etat));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void loadFromDatabase() {
		getFromDatabase();
	}

	@Override
	public void delete() {
		sql.deletePrepareStatement(TABLE_NAME, new String[]{"idLivraison = " + id});
		livraisons.remove(this);
	}

	public static void create(Livraison livraison) {
		sql.createPrepareStatement(TABLE_NAME, new String[]{"Depot_idDepot", "Tournee_idTournee",
										"Abonnement_idAbonnement", "semaine",
										"etat"},
						new Object[]{livraison.depot == null ? null : livraison.depot.id, livraison.tournee == null ? null : livraison.tournee.id,
										livraison.abonnement.id, livraison.date, livraison.etat});
		getFromDatabase();
	}

	public static void update(Livraison livraison) {
		sql.updatePreparedStatement(TABLE_NAME, new String[]{"Depot_idDepot", "Tournee_idTournee",
										"Abonnement_idAbonnement", "semaine",
										"etat"},
						new Object[]{livraison.depot == null ? null : livraison.depot.id, livraison.tournee == null ? null : livraison.tournee.id,
										livraison.abonnement.id, livraison.date, livraison.etat},
						new String[]{"idLivraison = " + livraison.id});
		getFromDatabase();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Identifiant : ").append(id);
		sb.append("\nAbonnement : ").append(abonnement.id);
		if (depot != null) sb.append("\nDepot : ").append(depot.id);
		sb.append("\nDate : ").append(date);
		sb.append("\nEtat : ").append(etat);
		return sb.toString();
	}
}
