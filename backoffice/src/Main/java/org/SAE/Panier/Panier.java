package org.SAE.Panier;
import org.SAE.Jardin.Jardin;
import org.SAE.Main.Base;
import org.SAE.Main.Logger;
import org.SAE.Main.Main;
import org.SAE.Main.SQL;
import org.SAE.Produit.Produit;

import javax.swing.*;
import java.io.File;
import java.io.InputStream;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
public class Panier extends Base {
	static final String TABLE_NAME = "Panier";
	static final String[] fields = {"Nom", "Prix", "Image", "Jardin"};
	static final String[] dbFields = {"nom", "prix", "image", "Jardin_idJardin"};
	static final ArrayList<String> requiredFieldsList = new ArrayList<>(Arrays.asList("Nom", "Prix", "Jardin"));
	public final int id;
	String nom;
	float prix;
	File image;
	Jardin jardin;
	public static final List<Panier> paniers = new ArrayList<>();
	static class ProduitE {
		Produit produit;
		int quantite;
		ProduitE(Produit produit, int quantite) {
			this.produit = produit;
			this.quantite = quantite;
		}

		@Override
		public String toString() {
			return produit.nom + " (" + quantite + ")";
		}


	}
	ArrayList<ProduitE> produits = new ArrayList<>();

	public Panier(String nom, float prix, File image, Jardin jardin) {
		this.id = -1;
		this.nom = nom;
		this.prix = prix;
		this.image = image;
		this.jardin = jardin;
		paniers.add(this);
	}

	private Panier(int id, String nom, float prix, File image, Jardin jardin) {
		this.id = id;
		this.nom = nom;
		this.prix = prix;
		this.image = image;
		this.jardin = jardin;
		paniers.add(this);
	}

	public static void getFromDatabase() {
		paniers.clear();
		SQL sql = Main.sql;
		try {
			ResultSet res = sql.select(TABLE_NAME);
			while (res.next()) {
				int id = res.getInt("idPanier");
				String nom = res.getString("nom");
				float prix = res.getFloat("prix");
				File image = Main.convertInputStreamToImage(res.getBinaryStream("image"));
				int jardinIdJardin = res.getInt("Jardin_idJardin");
				Panier p = new Panier(id, nom, prix, image,
								Jardin.jardins.stream().filter(j -> j.id == jardinIdJardin).findFirst().orElse(null));
				res = sql.select("Produit_has_Panier", new String[]{"Panier_idPanier = " + id});
				while (res.next()) {
					int panierIdPanier = res.getInt("Panier_idPanier");
					if (panierIdPanier != id) continue;
					int produitIdProduit = res.getInt("Produit_idProduit");
					int quantite = res.getInt("quantite");
					p.produits.add(new ProduitE(Produit.produits.stream().filter(pr -> pr.id == produitIdProduit).findFirst().orElse(null), quantite));
				}
			}
		} catch (Exception e) {
			Logger.error("Error while fetching Panier data from database: " + e.getMessage());
		}
	}

	public static void create(Panier panier) {
		SQL sql = Main.sql;
		try {
			sql.createPrepareStatement(TABLE_NAME, dbFields, new Object[]{panier.nom, panier.prix, panier.image,
							panier.jardin.id});
		} catch (Exception e) {
			Logger.error("Error while creating Panier: " + e.getMessage());
		}
		getFromDatabase();
	}

	public static void update(Panier panier) {
		SQL sql = Main.sql;
		try {
			sql.updatePreparedStatement(TABLE_NAME, dbFields, new Object[]{panier.nom, panier.prix, panier.image,
											panier.jardin.id}
			, new String[]{"idPanier = " + panier.id});
		} catch (Exception e) {
			Logger.error("Error while updating Panier: " + e.getMessage());
		}
		getFromDatabase();

	}

	protected void delete() {
		SQL sql = Main.sql;
		sql.deletePrepareStatement(TABLE_NAME, new String[]{"idPanier = " + id});
		paniers.remove(this);
	}
	@Override
	public void loadFromDatabase(){
		getFromDatabase();
	}

	@Override
	public String toString() {
		return nom;
	}

	void addProduit(ProduitE produit) {
		SQL sql = Main.sql;
		for (ProduitE p : produits) {
			if (p.produit.id == produit.produit.id) {
			  Logger.error("Produit already in Panier");
				return;
			}
		}
		try {
			sql.createPrepareStatement("Produit_has_Panier", new String[]{"Panier_idPanier", "Produit_idProduit", "quantite"},
							new Object[]{id, produit.produit.id, produit.quantite});
		} catch (Exception e) {
			Logger.error("Error while adding Produit to Panier: " + e.getMessage());
		}
	}

	void deleteProduit(Produit produitPanier) {
		SQL sql = Main.sql;
		try {
			sql.deletePrepareStatement("Produit_has_Panier", new String[]{"Panier_idPanier = " + id, "Produit_idProduit = " + produitPanier.id});
		} catch (Exception e) {
			Logger.error("Error while deleting Produit from Panier: " + e.getMessage());
		}
	}
}
