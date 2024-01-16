package org.SAE.Adhesion;

import org.SAE.Client.Client;
import org.SAE.Jardin.Jardin;
import org.SAE.Main.Base;
import org.SAE.Main.Logger;
import org.SAE.Main.Main;
import org.SAE.Main.SQL;

import java.sql.Date;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class Adhesion extends Base {
	static final String TABLE_NAME = "Adhesion";
	static final String[] dbFields = {"Client_idClient", "TypeAdhesion_idTypeAdhesion", "Jardin_idJardin", "debut",
					"fin", "enCours", "frequencePaiement"};
	public final int id;
	public Client client;
	public TypeAdhesion typeAdhesion;
	public Jardin jardin;
	public Date debut;
	public Date fin;
	public boolean enCours;
	public int frequencePaiement;
	public static final List<Adhesion> adhesions = new ArrayList<>();

	public Adhesion(int id, Client client, TypeAdhesion typeAdhesion, Jardin jardin, Date debut, Date fin, boolean enCours, int frequencePaiement) {
		this.id = id;
		this.client = client;
		this.typeAdhesion = typeAdhesion;
		this.jardin = jardin;
		this.debut = debut;
		this.fin = fin;
		this.enCours = enCours;
		this.frequencePaiement = frequencePaiement;
		adhesions.add(this);
	}

	public Adhesion(Client client, TypeAdhesion typeAdhesion, Jardin jardin, Date debut, Date fin, boolean enCours, int frequencePaiement) {
		this.id = Main.sql.getNextId(TABLE_NAME);
		this.client = client;
		this.typeAdhesion = typeAdhesion;
		this.jardin = jardin;
		this.debut = debut;
		this.fin = fin;
		this.enCours = enCours;
		this.frequencePaiement = frequencePaiement;
		adhesions.add(this);
	}

	public static void getFromDatabase() {
		adhesions.clear();
		SQL sql = Main.sql;
		try {
			ResultSet res = sql.select(TABLE_NAME);
			if (res == null) return;
			while (res.next()) {
				int id = res.getInt("idAdhesion");
				int clientIdClient = res.getInt("Client_idClient");
				int typeAdhesionIdTypeAdhesion = res.getInt("TypeAdhesion_idTypeAdhesion");
				int jardinIdJardin = res.getInt("Jardin_idJardin");
				Date debut = res.getDate("debut");
				Date fin = res.getDate("fin");
				boolean enCours = res.getBoolean("enCours");
				int frequencePaiement = res.getInt("frequencePaiement");
				new Adhesion(id, Client.getClientById(clientIdClient), TypeAdhesion.getTypeAdhesionById(typeAdhesionIdTypeAdhesion), Jardin.getJardinById(jardinIdJardin), debut, fin, enCours, frequencePaiement);
			}
		} catch (Exception e) {
			Logger.error(String.valueOf(e));
		}
	}

	public static void update(Adhesion adhesion) {
		if (!Main.sql.updatePreparedStatement(TABLE_NAME, dbFields,
						new Object[]{adhesion.client.id, adhesion.typeAdhesion.id, adhesion.jardin.id, adhesion.debut,
										adhesion.fin, adhesion.enCours, adhesion.frequencePaiement},
						new String[]{"idAdhesion = " + adhesion.id}))
			Logger.error("Can't update adhesion");
		getFromDatabase();
	}

	public static void create(Adhesion adhesion) {
		if (!Main.sql.createPrepareStatement(TABLE_NAME, dbFields,
						new Object[]{adhesion.client.id, adhesion.typeAdhesion.id, adhesion.jardin.id, adhesion.debut,
										adhesion.fin, adhesion.enCours, adhesion.frequencePaiement}))
			Logger.error("Can't create adhesion");
		getFromDatabase();
	}

	public String toString() {
		return typeAdhesion.toString() + " dans le jardin"
						+ jardin.toString() + "  \ndu " + debut.toString() + " au " + fin.toString() + " payé tous les " + frequencePaiement
						+ (enCours ? " (en cours)" : "");
	}

	@Override
	public void loadFromDatabase() {
		getFromDatabase();
	}

	public void delete() {
		if (!Main.sql.deletePrepareStatement(TABLE_NAME, new String[]{"idAdhesion = " + id}))
			Logger.error("Can't delete adhesion");
	}
}
