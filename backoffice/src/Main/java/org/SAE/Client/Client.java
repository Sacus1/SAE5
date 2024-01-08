package org.SAE.Client;

import org.SAE.Adresse.Adresse;
import org.SAE.Client.Client;
import org.SAE.Depot.JourSemaine;
import org.SAE.Main.Base;
import org.SAE.Main.Logger;
import org.SAE.Main.Main;
import org.SAE.Main.SQL;

import javax.swing.*;
import java.sql.Date;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Client extends Base {
	static final String TABLE_NAME = "Client";
	static final String[] dbFields = {"Adresse_idAdresse", "raisonSociale", "civilite", "nom", "prenom", "telephone"
					, "telephone2", "telephone3", "mail", "profession", "dateNaissance", "estDispense"};
	static final ArrayList<String> requiredFieldsList = new ArrayList<>(Arrays.asList("civilite", "nom", "prenom",
					"adresseIdAdresse", "telephone", "mail"));
	public final int id;
	public Adresse adresse;
	String raisonSociale;
	String civilite;
	String nom;
	String prenom;
	String telephone;
	String telephone2;
	String telephone3;
	String mail;
	String profession;
	Date dateNaissance;
	boolean estDispense;
	public static final List<Client> clients = new ArrayList<>();

	public Client(int id, Adresse adresse, String raisonSociale, String civilite, String nom, String prenom,
	              String[] telephone, String mail, String profession,
	              Date dateNaissance, boolean estDispense) {
		this.id = id;
		this.adresse = adresse;
		this.raisonSociale = raisonSociale;
		this.civilite = civilite;
		this.nom = nom;
		this.prenom = prenom;
		this.telephone = telephone[0];
		this.telephone2 = telephone[1];
		this.telephone3 = telephone[2];
		this.mail = mail;
		this.profession = profession;
		this.dateNaissance = dateNaissance;
		this.estDispense = estDispense;
		clients.add(this);
	}

	public static void getFromDatabase() {
		clients.clear();
		SQL sql = Main.sql;
		try {
			ResultSet res = sql.select(TABLE_NAME);
			if (res == null) {
				return;
			}
			while (res.next()) {
				int id = res.getInt("idClient");
				int adresseIdAdresse = res.getInt("Adresse_idAdresse");
				String raisonSociale = res.getString("raisonSociale");
				String civilite = res.getString("civilite");
				String nom = res.getString("nom");
				String prenom = res.getString("prenom");
				String telephone = res.getString("telephone");
				String telephone2 = res.getString("telephone2");
				String telephone3 = res.getString("telephone3");
				String mail = res.getString("mail");
				String profession = res.getString("profession");
				Date dateNaissance = res.getDate("dateNaissance");
				boolean estDispense = res.getBoolean("estDispense");
				new Client(id, Adresse.getAdresseById(adresseIdAdresse), raisonSociale, civilite, nom, prenom, new String[]{telephone,
								telephone2, telephone3}, mail, profession, dateNaissance, estDispense);
			}
		} catch (Exception e) {
			Logger.error("Main.SQL Exception : " + e.getMessage());
		}
	}

	public static void update(Client client) {
		if (!Main.sql.updatePreparedStatement(TABLE_NAME, dbFields,
						new Object[]{client.adresse.id, client.raisonSociale, client.civilite, client.nom,
										client.prenom, client.telephone, client.telephone2, client.telephone3, client.mail,
										client.profession, client.dateNaissance, client.estDispense},
						new String[]{"idClient = " + client.id}))
			Logger.error("Can't update client");
		getFromDatabase();

	}


	public void delete() {
		if (!Main.sql.deletePrepareStatement(TABLE_NAME, new String[]{"idClient = " + id}))
			Logger.error("Can't delete client");
		clients.remove(this);
	}

	@Override
	public String toString() {
		return nom + " " + prenom;
	}
	@Override
	public void loadFromDatabase(){
		getFromDatabase();
	}
}
