package org.SAE.Produit;
import org.SAE.Main.Base;
import org.SAE.Main.Logger;
import org.SAE.Main.Main;
import org.SAE.Main.SQL;

import java.io.File;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class Produit extends Base {
	static final String TABLE_NAME = "Produit";
	public final int id;
	public int idUnite;
	public String nom;
	String description;
	File image;
	public static final List<Produit> produits = new ArrayList<>();
	public Produit(int id, String nom, String description, int idUnite,File image) {
		this.id = id;
		this.nom = nom;
		this.description = description;
		this.idUnite = idUnite;
		this.image = image;
		produits.add(this);
	}
	public Produit(String nom, String description, int idUnite,File image) {
		this.id =-1;
		this.nom = nom;
		this.description = description;
		this.idUnite = idUnite;
		this.image = image;
		produits.add(this);
	}
	public String toString() {
		return nom;
	}
	public static void getFromDatabase() {
		produits.clear();
		SQL sql = Main.sql;
		try {
			ResultSet res = sql.select("Produit");
			if (res == null) return;
			while (res.next()) {
				int id = res.getInt("idProduit");
				String nom = res.getString("nom");
				String description = res.getString("description");
				File image = Main.convertInputStreamToImage(res.getBinaryStream("image"));
				int idUnite = res.getInt("Unite_idUnite");
				new Produit(id, nom, description, idUnite, image);
			}
		} catch (Exception e) {
			Logger.error(String.valueOf(e));
		}
	}
	public static void update(Produit produit) {
		if (!Main.sql.updatePreparedStatement("Produit", new String[]{"nom", "image", "description", "Unite_idUnite"},
						new Object[]{produit.nom, produit.image, produit.description, produit.idUnite},
						new String[]{"idProduit = "+produit.id}))
			Logger.error("Update failed");
		getFromDatabase();
	}
	public static void create(Produit produit) {
		if (!Main.sql.createPrepareStatement("Produit", new String[]{"nom", "image", "description", "Unite_idUnite"},
						new Object[]{produit.nom, produit.image, produit.description,  produit.idUnite}))
			Logger.error("Create failed");
		getFromDatabase();
	}
	public void delete() {
		if (!Main.sql.deletePrepareStatement("Produit", new String[]{"idProduit = " + id}))
			Logger.error("Echec de la suppression");
		produits.remove(this);
	}
	@Override
	public void loadFromDatabase(){
		getFromDatabase();
	}
}
