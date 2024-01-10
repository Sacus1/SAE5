package org.SAE.Jardin;


import org.SAE.Adresse.Adresse;
import org.SAE.Main.Base;
import org.SAE.Main.Logger;
import org.SAE.Main.Main;
import org.SAE.Main.SQL;
import org.SAE.Panier.Panier;
import org.SAE.Referent.Referent;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Jardin extends Base {

	private static final String TABLE_NAME = "Jardin";
	private static final String[] fields = {"Referent","Adresse siège social","Adresse gestion","Nom commercial",
					"Raison sociale"};
	private static final String[] dbFields = {"Referent_idReferent","Adresse_idAdresseSiegeSocial","Adresse_idAdresseGestion",
					"nomCommercial","raison"};

	public int id;
	public Referent referent;
	public Adresse adresseSiegeSocial;
	public Adresse adresseGestion;
	public String nomCommercial;
	public String raisonSociale;
	Panier[] paniers;
	public static final List<Jardin> jardins = new ArrayList<>();

	public Jardin(Referent referent, Adresse adresseSiegeSocial, Adresse adresseGestion, String nomCommercial, String raisonSociale) {
		this.referent = referent;
		this.adresseSiegeSocial = adresseSiegeSocial;
		this.adresseGestion = adresseGestion;
		this.nomCommercial = nomCommercial;
		this.raisonSociale = raisonSociale;
		jardins.add(this);
	}

	private Jardin(int id, Referent referent, Adresse adresseSiegeSocial, Adresse adresseGestion, String nomCommercial, String raisonSociale) {
		this.id = id;
		this.referent = referent;
		this.adresseSiegeSocial = adresseSiegeSocial;
		this.adresseGestion = adresseGestion;
		this.nomCommercial = nomCommercial;
		this.raisonSociale = raisonSociale;
		jardins.add(this);
	}

	public static void getFromDatabase() {
		jardins.clear();
		SQL sql = Main.sql;
		try {
			ResultSet res = sql.select(TABLE_NAME);
			if (res == null) return;
			while (res.next()) {
				int id = res.getInt("idJardin");
				int referentIdReferent = res.getInt("Referent_idReferent");
				int adresseSiegeSocialIdAdresse = res.getInt("Adresse_idAdresseSiegeSocial");
				int adresseGestionIdAdresse = res.getInt("Adresse_idAdresseGestion");
				String nomCommercial = res.getString("nomCommercial");
				String raisonSociale = res.getString("raison");
				Referent referent = Referent.referents.stream().filter(r -> r.id == referentIdReferent).findFirst().orElse(null);
				Adresse adresseSiegeSocial = Adresse.adresses.stream().filter(a -> a.id == adresseSiegeSocialIdAdresse).findFirst().orElse(null);
				Adresse adresseGestion = Adresse.adresses.stream().filter(a -> a.id == adresseGestionIdAdresse).findFirst().orElse(null);
				new Jardin(id, referent, adresseSiegeSocial, adresseGestion, nomCommercial, raisonSociale);
			}
		} catch (Exception e) {
			Logger.error("Error while fetching Jardin data from database: " + e.getMessage());
		}
	}

	public static void create(Jardin jardin) {
		SQL sql = Main.sql;
		try {
			sql.createPrepareStatement(TABLE_NAME, dbFields, new Object[]{jardin.referent.id,jardin.adresseSiegeSocial.id,jardin.adresseGestion.id,jardin.nomCommercial,jardin.raisonSociale});
		} catch (Exception e) {
			Logger.error("Error while creating Jardin: " + e.getMessage());
		}
		getFromDatabase();
	}

	public static void update(Jardin jardin) {
		SQL sql = Main.sql;
		try {
			sql.updatePreparedStatement(TABLE_NAME, dbFields, new Object[]{jardin.referent.id,jardin.adresseSiegeSocial.id,jardin.adresseGestion.id,jardin.nomCommercial,jardin.raisonSociale}
			, new String[]{"idJardin = " + jardin.id});
		} catch (Exception e) {
			Logger.error("Error while updating Jardin: " + e.getMessage());
		}
		getFromDatabase();

	}

	@Override
	protected void delete() {
		SQL sql = Main.sql;
		try {
			sql.deletePrepareStatement(TABLE_NAME, new String[]{"idJardin = " + id});
		} catch (Exception e) {
			Logger.error("Error while deleting Jardin: " + e.getMessage());
		}
		jardins.remove(this);
	}

	@Override
	public String toString() {
		return nomCommercial;
	}
	@Override
	public void loadFromDatabase(){
		getFromDatabase();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Jardin jardin) {
			return jardin.id == id;
		}
		return false;
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}
}
