package org.SAE.Tournee;

import org.SAE.Depot.Depot;
import org.SAE.Depot.JourSemaine;
import org.SAE.Main.Base;

import java.sql.ResultSet;
import java.util.ArrayList;

import static org.SAE.Main.Main.sql;

public class Tournee extends Base {
	static final String TABLE_NAME = "Tournee";
	public int id;
	JourSemaine jourPreparation;
	JourSemaine jourLivraison;
	String nom;
	String color;
	boolean estLivreMatin;
	public static ArrayList<Tournee> tournees = new ArrayList<>();
	ArrayList<Depot> depots = new ArrayList<>();

	public Tournee(int id, JourSemaine jourPreparation, JourSemaine jourLivraison, String nom, String color, boolean estLivreMatin) {
		this.id = id;
		this.jourPreparation = jourPreparation;
		this.jourLivraison = jourLivraison;
		this.nom = nom;
		this.color = color;
		this.estLivreMatin = estLivreMatin;
	}

	public Tournee(JourSemaine jourPreparation, JourSemaine jourLivraison, String nom, String color, boolean estLivreMatin) {
		this.id = sql.getNextId(TABLE_NAME);
		this.jourPreparation = jourPreparation;
		this.jourLivraison = jourLivraison;
		this.nom = nom;
		this.color = color;
		this.estLivreMatin = estLivreMatin;
	}
	public static void getFromDatabase() {
		tournees.clear();
		try {
			ResultSet res = sql.select(TABLE_NAME);
			while (res.next()) {
				int id = res.getInt("idTournee");
				JourSemaine jourPreparation = JourSemaine.values()[res.getInt("jourPreparation")-1]; //
				JourSemaine jourLivraison = JourSemaine.values()[res.getInt("jourLivraison")-1]; //
				String nom = res.getString("nom");
				String color = res.getString("couleur");
				boolean estLivreMatin = res.getBoolean("estMatin");
				Tournee tournee = new Tournee(id, jourPreparation, jourLivraison, nom, color, estLivreMatin);
				tournees.add(tournee);
				ResultSet res2 = sql.select("Tournee_has_Depot", new String[]{"Tournee_idTournee = " + id});
				while (res2.next()) {
					int depotId = res2.getInt("Depot_idDepot");
					Depot depot = Depot.getDepotById(depotId);
					tournee.depots.add(depot);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Tournee getTourneeById(int tourneeId) {
		for (Tournee tournee : tournees) {
			if (tournee.id == tourneeId) {
				return tournee;
			}
		}
		return null;
	}

	@Override
	public void loadFromDatabase() {
		getFromDatabase();
	}

	@Override
	protected void delete() {
		sql.deletePrepareStatement(TABLE_NAME, new String[]{"idTournee = " + id});
		loadFromDatabase();
	}

	@Override
	public String toString() {
		return nom;
	}

	public static void create(Tournee tournee) {
		int jourPreparation = tournee.jourPreparation.ordinal()+1;
		int jourLivraison = tournee.jourLivraison.ordinal()+1;
		sql.createPrepareStatement(TABLE_NAME, new String[]{"jourPreparation", "jourLivraison", "nom", "couleur",
										"estMatin"},
						new Object[]{jourPreparation,jourLivraison, tournee.nom, tournee.color,
										tournee.estLivreMatin});
		for (Depot depot : tournee.depots) {
			sql.createPrepareStatement("Tournee_has_Depot", new String[]{"Tournee_idTournee", "Depot_idDepot"},
							new Object[]{tournee.id, depot.id});
		}
		getFromDatabase();
	}

	static void update(Tournee tournee) {
		int jourPreparation = tournee.jourPreparation.ordinal()+1;
		int jourLivraison = tournee.jourLivraison.ordinal()+1;
		sql.updatePreparedStatement(TABLE_NAME, new String[]{"jourPreparation", "jourLivraison", "nom", "couleur",
										"estMatin"},
						new Object[]{jourPreparation, jourLivraison, tournee.nom, tournee.color,
										tournee.estLivreMatin},
						new String[]{"idTournee = " + tournee.id});
		// update linked depots
		sql.deletePrepareStatement("Tournee_has_Depot", new String[]{"Tournee_idTournee = " + tournee.id});
		for (Depot depot : tournee.depots) {
			sql.createPrepareStatement("Tournee_has_Depot", new String[]{"Tournee_idTournee", "Depot_idDepot"},
							new Object[]{tournee.id, depot.id});
		}
		getFromDatabase();
	}
}
